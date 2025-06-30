module Lib (
  topLevel1,
  topLevel2,
  parser1,
  parser2
) where

import Monadic as M (topLevel, program, Parser)
import Combinator as C (topLevel, program, Parser)

parser1 :: C.Parser Char String
parser1 = C.program

topLevel1 :: C.Parser Char a -> String -> a
topLevel1 = C.topLevel

parser2 :: M.Parser String
parser2 = M.program

topLevel2 :: M.Parser a -> String -> a
topLevel2 = M.topLevel
