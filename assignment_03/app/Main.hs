module Main where

import Lib

main :: IO ()
main = do
  print $ topLevel1 parser1 program
  print $ topLevel2 parser2 program
    where program = "PROGRAM Foo2 WHILE == 1 1 DO var = 1."
