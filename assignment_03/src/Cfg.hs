module Cfg (
  Program (Program),
  VariableName,
  ProgramName,
  Statement (Skip, Assign, If, IfElse, While, Block),
  Expression (Var, IntConst, FloatConst, BinOp, RelOp),
  Rel (Equal, LessEq, GreaterEq, NotEqual),
  Op (Plus, Minus, Div, Times),
  to_ast
) where

import Data.List

type ProgramName = String
type VariableName = String
data Statement =  Skip
               | Assign VariableName Expression
               | If Expression Statement
               | IfElse Expression Statement Statement
               | While Expression Statement
               | Block [Statement]
               deriving (Eq)
data Expression = Var String
                | IntConst Int
                | FloatConst Float
                | BinOp Op Expression Expression
                | RelOp Rel Expression Expression
                deriving (Eq)
data Op = Plus | Minus | Times | Div deriving (Eq)
data Rel = LessEq | GreaterEq | Equal | NotEqual deriving (Eq)
data Program = Program Statement deriving (Show, Eq)

instance Show Statement where
  show Skip                = "skip"
  show (If cond e1)        = "if " ++ show cond ++ " then " ++ show e1 ++ " fi"
  show (IfElse cond e1 e2) = "if " ++ show cond ++ " then " ++ show e1 ++ " else " ++ show e2 ++ " fi"
  show (Assign v expr)     = id v ++ ":=" ++ show expr
  show (While cond body)   = "while " ++ show cond ++ " do " ++ show body ++ " od"
  show (Block stms)        = intercalate ";" $ map show stms

instance Show Expression where
  show (Var s)            = s
  show (IntConst i)       = show i
  show (FloatConst f)     = show f
  show (BinOp op lhs rhs) = "(" ++ show lhs ++ show op ++ show rhs ++ ")"
  show (RelOp op lhs rhs) = "(" ++ show lhs ++ show op ++ show rhs ++ ")"

instance Show Op where
  show Plus  = "+"
  show Minus = "-"
  show Times = "*"
  show Div   = "/"

instance Show Rel where
  show LessEq    = "<="
  show GreaterEq = ">="
  show Equal     = "="
  show NotEqual  = "=/="

to_ast :: Program -> String
to_ast (Program body) = show body
