module Main where

import Lib (topLevel2, parser2)
import System.Environment
import Control.Exception
import System.Exit

-- Program to take in a single file as an argument, read the file
-- and attempt to parse it. If there are no parsing errors, return 0
-- otherwise return that an error occurred
main :: IO ()
main = do
  args <- getArgs
  case args of
    [file] -> catch program handler
      where program = readFile file >>= return . (topLevel2 parser2) >>= print
            handler :: ErrorCall -> IO ()
            handler _ = exitFailure
    _ -> error "Wrong number of arguments"

