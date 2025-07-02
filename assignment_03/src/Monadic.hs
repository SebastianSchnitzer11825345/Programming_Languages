module Monadic (
  parseToMaybe,
  topLevel,
  program,
  Parser
) where

import Control.Applicative
import Data.Char
import Data.Maybe
import Cfg

newtype Parser a = Parser (String -> [(a, String)])

instance Monad Parser where
  p >>= f = Parser $ \str -> concat [parse (f v) str'
                                     | (v, str') <- parse p str]

instance Functor Parser where
  fmap g p = Parser (\x -> case parse p x of
                             []           -> []
                             ((v, out):_) -> [(g v, out)])

instance Applicative Parser where
  pure v    = Parser (\x -> [(v, x)])
  pg <*> px = Parser (\x -> case parse pg x of
                         []           -> []
                         ((g, out):_) -> parse (fmap g px) out)

instance Alternative Parser where
  empty   = Parser (\_ -> [])
  a <|> b = Parser (\x -> case parse a x of
                            []           -> parse b x
                            ((v, out):_) -> [(v, out)])

parse :: Parser a -> String -> [(a, String)]
parse (Parser p) input = p input

failure :: Parser a
failure = Parser $ \_ -> []

item :: Parser Char
item = Parser $ \str -> case str of
                          [] -> []
                          (x:xs) -> [(x, xs)]

satisfies :: (Char -> Bool) -> Parser Char
satisfies p = item >>= \x -> if p x then return x else failure

digit :: Parser Char
digit = satisfies isDigit

lower :: Parser Char
lower = satisfies isLower

lower_digit :: Parser Char
lower_digit = satisfies isLower <|> satisfies isDigit

upper :: Parser Char
upper = satisfies isUpper

spaces :: Parser String
spaces = many (satisfies isSpace)

token :: Parser a -> Parser a
token p = p >>= \v -> spaces >> return v

nat :: Parser Int
nat = some digit >>= return . read

int :: Parser Int
int = neg <|> nat
  where neg = do _ <- nibble '-'
                 n <- nat
                 return (-n)

float :: Parser Float
float = token fp_num
  where fp_num = do l <- int
                    _ <- char '.'
                    r <- nat
                    -- Is converting Int -> String -> Float dumb? Yes
                    -- Does it work? Also yes
                    return (read ((show l) ++ "." ++ (show r)))

char :: Char -> Parser Char
char c = satisfies (== c)

string :: String -> Parser String
string []     = return []
string (x:xs) = do _ <- char x
                   _ <- string xs
                   return (x:xs)

-- Make it easier than constantly writing 'token (string ...)'
chomp :: String -> Parser String
chomp = token . string

nibble :: Char -> Parser Char
nibble  = token . char

operator :: Parser Op
operator = token (plus <|> minus <|> times <|> divide)
  where plus   = char '+' >> return Plus
        minus  = char '-' >> return Minus
        times  = char '*' >> return Times
        divide = char '/' >> return Div

relator :: Parser Rel
relator = token (eq <|> neq <|> geq <|> leq)
  where eq  = string "==" >> return Equal
        neq = string "/=" >> return NotEqual
        geq = string ">=" >> return GreaterEq
        leq = string "<=" >> return LessEq

variable :: Parser VariableName
variable = token var_name
  where var_name = do x  <- lower
                      xs <- many lower_digit
                      return (x:xs)

expression :: Parser Expression
expression = token (var_expr <|> fp_expr <|> int_expr <|> op_expr)
  where var_expr = variable >>= return . Var
        int_expr = token int >>= return . IntConst
        fp_expr  = float >>= return . FloatConst
        op_expr  = do op  <- operator
                      lhs <- expression
                      rhs <- expression
                      return (BinOp op lhs rhs)

assignment :: Parser Statement
assignment = token assign
  where assign = do var  <- variable
                    _    <- nibble '='
                    expr <- expression
                    return (Assign var expr)

pred_expression :: Parser Expression
pred_expression = token pred_expr
  where pred_expr = do p <- relator
                       l <- expression
                       r <- expression
                       return (RelOp p l r)

statement :: Parser Statement
statement = token (noop <|> while <|> assignment <|> branch <|> stmt_seq)
  where noop     = chomp "NOOP" >> return Skip
        stmt_seq = do _     <- nibble '{'
                      stmts <- statement_sequence
                      _     <- nibble '}'
                      return (Block stmts)

statement_sequence :: Parser [Statement]
statement_sequence = token (stmt_chain <|> stmt)
  where stmt = statement >>= \s -> return [s]
        stmt_chain = do s  <- statement
                        _  <- nibble ';'
                        xs <- statement_sequence
                        return (s:xs)

while :: Parser Statement
while = token loop
  where loop = do _    <- chomp "WHILE"
                  cond <- pred_expression
                  _    <- chomp "DO"
                  body <- statement
                  return (While cond body)

branch :: Parser Statement
branch = token (if_else <|> when)
  where when    = do _    <- chomp "IF"
                     cond <- pred_expression
                     _    <- chomp "THEN"
                     jeq  <- statement
                     return (If cond jeq)
        if_else = do _    <- chomp "IF"
                     cond <- pred_expression
                     _    <- chomp "THEN"
                     jeq  <- statement
                     _    <- chomp "ELSE"
                     jne  <- statement
                     return (IfElse cond jeq jne)

program_name :: Parser ProgramName
program_name = token name
  where name = do x  <- upper
                  xs <- many lower_digit
                  return (x:xs)

program :: Parser String
program = do _     <- chomp "PROGRAM"
             _     <- program_name
             stmts <- statement_sequence
             _     <- nibble '.'
             return $ to_ast (Program (Block stmts))

parseToMaybe :: Parser a -> String -> Maybe a
parseToMaybe fn input = case (parse fn input) of
                      []        -> Nothing
                      ((a,s):_) -> if 0 == length s
                                      then Just a
                                      else Nothing

topLevel :: Parser a -> String -> a
topLevel fn input = fromMaybe (error "parse unsuccessful") parsed
  where parsed = parseToMaybe fn input
