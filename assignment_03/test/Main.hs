module Main (main) where

import MonadicTest as Mt (tests)
import CombinatorTest as Ct (tests)

import Test.HUnit

all_tests :: Test
all_tests = TestList [Mt.tests, Ct.tests]

main :: IO Counts
main = runTestTT $ all_tests
