module Combinator (
  parseToMaybe,
  topLevel,
  program,
  Parser
) where

import Data.Maybe
import Data.Char
import Cfg

 -- Type Definition
type Parser a b = [a] -> [(b,[a])]

-- Checks if a character satisfies a predicate
-- Returns the character if it does, otherwise returns an empty list
sat :: (a -> Bool) -> Parser a a
sat p = \input -> case input of
  [] -> []
  (x:xs) | p x -> [(x, xs)]
         | otherwise -> []

-- Matches a character
char :: Eq a => a -> Parser a a
char c = sat (== c)

-- Matches a string
string :: String -> Parser Char String
string [] = \input -> [("", input)]
string (x:xs) = \input ->
  case input of
    [] -> []
    (y:ys) | y == x ->
      case string xs ys of
        [] -> []
        ((rest, restInput):_) -> [(y:rest, restInput)]
    _ -> []

-- Parses one or more repetitions of a parser
many1 :: Parser a b -> Parser a [b]
many1 p = \input ->
  case p input of
    [] -> []
    ((x,xs):_) -> [(x:ys, zs) | (ys, zs) <- many p xs]

-- Parses zero or more repetitions of a parser
many :: Parser a b -> Parser a [b]
many p = many1 p <|> mypure []

-- Parses a single char
-- It is called mypure since pure is already defined in the Prelude
mypure :: a -> Parser b a
mypure x = \input -> [(x, input)]

-- Tries the first parser, if it fails, tries the second parser
-- Returns the result of the first parser if it succeeds, otherwise returns the result of the second parser
(<|>) :: Parser a b -> Parser a b -> Parser a b
p <|> q = \input -> let res = p input in
  if null res then q input else res

--Chains together two parsers
(>>>=) :: Parser a b -> (b -> Parser a c) -> Parser a c
p >>>= f = \inp -> concat [f v inp' | (v, inp') <- p inp]

-- Helps with Parsing whitespace
space :: Parser Char ()
space = many (sat isSpace) >>>= \_ -> mypure ()

-- Consume whitespace after parsing
lexeme :: Parser Char a -> Parser Char a
lexeme p = p >>>= \x -> space >>>= \_ -> mypure x

-- Remove whitespace around a token
token :: Parser Char a -> Parser Char a
token p = space >>>= \_ -> p >>>= \v -> space >>>= \_ -> mypure v
-- Parses a keyword or symbol
symbol :: String -> Parser Char String
symbol s = lexeme (string s)

-- Parses a variable name
identifier :: Parser Char String
identifier = lexeme (sat isLower >>>= \x -> many (sat isAlphaNum) >>>= \xs -> mypure (x:xs))

-- Parses the program name
programName :: Parser Char String
programName = lexeme (sat isUpper >>>= \x -> many (sat isAlphaNum) >>>= \xs -> mypure (x:xs))

-- Parses an integer literal
-- We do two parsers, one for positive and one for negative integers
integer :: Parser Char Int
integer = token (
  (char '-' >>>= \_ -> many1 (sat isDigit) >>>= \digits ->
     let val = read digits in mypure (-val)) <|>
  (many1 (sat isDigit) >>>= \digits ->
     let val = read digits in mypure val)
 )

-- Parses a float literal
float :: Parser Char Float
float = token $ integer >>>= \i -> char '.' >>>= \_ -> many1 (sat isDigit) >>>= \f ->
  mypure (read (show i ++ "." ++ f))

-- Parses arithmetic operators
op :: Parser Char Op
op = token $ plus <|> minus <|> times <|> divide
  where plus   = (char '+' >>>= \_ -> mypure Plus)
        minus  = (char '-' >>>= \_ -> mypure Minus)
        times  = (char '*' >>>= \_ -> mypure Times)
        divide = (char '/' >>>= \_ -> mypure Div)

-- Parses relational operators
relOp :: Parser Char Rel
relOp = token $ leq <|> geq <|> eq <|> neq
  where leq = (string "<=" >>>= \_ -> mypure LessEq)
        geq = (string ">=" >>>= \_ -> mypure GreaterEq)
        eq  = (string "==" >>>= \_ -> mypure Equal)
        neq = (string "/=" >>>= \_ -> mypure NotEqual)

-- Parses an expression
expr :: Parser Char Expression
expr = var <|> floatConst <|> intConst <|> binExpression

-- Parse the different Expressionessions

var :: Parser Char Expression
var = (identifier >>>= \x -> mypure (Var x))

intConst :: Parser Char Expression
intConst = (integer >>>= \x -> mypure (IntConst x))

floatConst :: Parser Char Expression
floatConst = (float >>>= \x -> mypure (FloatConst x))

binExpression :: Parser Char Expression
binExpression = op >>>= \op' -> expr >>>= \e1 -> expr >>>= \e2 -> mypure (BinOp op' e1 e2)

relExpression :: Parser Char Expression
relExpression = relOp >>>= \op' -> expr >>>= \e1 -> expr >>>= \e2 -> mypure (RelOp op' e1 e2)

-- Parses a statement
stmt :: Parser Char Statement
stmt = ifStatement <|> whileStatement <|> block <|> assign <|> skipStatement

-- Parses the different statements

skipStatement :: Parser Char Statement
skipStatement = symbol "NOOP" >>>= \_ -> mypure Skip

assign :: Parser Char Statement
assign = identifier >>>= \v -> symbol "=" >>>= \_ -> expr >>>= \e -> mypure (Assign v e)

ifStatement :: Parser Char Statement
ifStatement = symbol "IF" >>>= \_ ->
         relExpression >>>= \cond ->
         symbol "THEN" >>>= \_ ->
         stmt >>>= \thenStatement ->
         ((symbol "ELSE" >>>= \_ -> stmt >>>= \elseStatement -> mypure (IfElse cond thenStatement elseStatement))
          <|> mypure (If cond thenStatement))

whileStatement :: Parser Char Statement
whileStatement = symbol "WHILE" >>>= \_ -> relExpression >>>= \cond -> symbol "DO" >>>= \_ -> stmt >>>= \body ->
  mypure (While cond body)

block :: Parser Char Statement
block = symbol "{" >>>= \_ -> stmtSeq >>>= \ss -> symbol "}" >>>= \_ -> mypure (Block ss)

-- Parses a sequence of statements, separated by semicolons
stmtSeq :: Parser Char [Statement]
stmtSeq = space >>>= \_ -> stmt >>>= \s1 ->
  ((symbol ";" >>>= \_ -> stmtSeq >>>= \s2 -> mypure (s1:s2)) <|> mypure [s1])

-- Parses the entire program
program :: Parser Char String
program = symbol "PROGRAM " >>>= \_ ->
  space >>>= \_ ->
  programName >>>= \_ ->
  space >>>= \_ ->
  stmtSeq >>>= \stmts ->
  symbol "." >>>= \_ ->
  mypure $ to_ast (Program (Block stmts))

parseToMaybe :: Parser Char a -> String -> Maybe a
parseToMaybe p input = case p input of
  [(res, [])] -> Just res
  _ -> Nothing

-- Top-level runner
topLevel :: Parser Char a -> String -> a
topLevel p input = fromMaybe (error "parse failed") parsed
  where parsed = parseToMaybe p input
