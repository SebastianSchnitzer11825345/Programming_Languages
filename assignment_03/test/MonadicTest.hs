module MonadicTest (tests) where

import Monadic
import Test.HUnit

valid_program_name :: Test
valid_program_name = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = "var:=1"
        actual =  topLevel program "PROGRAM Foo var = 1."

invalid_program_name :: Test
invalid_program_name = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual = parseToMaybe program "PROGRAM foo var = 1."

valid_program_while :: Test
valid_program_while = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = "while (1.5=-1) do f1:=-2.2139535 od"
        actual =  topLevel program "PROGRAM Foo WHILE == 1.5 -1 DO f1 = -2.2139535."

invalid_program_while :: Test
invalid_program_while = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual = parseToMaybe program "PROGRAM Foo while == 1.5 -1 DO f1 = -2.2139535."

invalid_program_while_do :: Test
invalid_program_while_do = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual = parseToMaybe program "PROGRAM Foo WHILE == 1.5 -1 do f1 = -2.2139535."

invalid_program_while_pred :: Test
invalid_program_while_pred = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual = parseToMaybe program "PROGRAM Foo WHILE + 1.5 -1 DO f1 = -2.2139535."

valid_program_branch :: Test
valid_program_branch = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = "if (-1.5=(a-1)) then v:=1 fi"
        actual =  topLevel program "PROGRAM F IF == -1.5 - a 1 THEN v = 1."

invalid_program_branch :: Test
invalid_program_branch = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual = parseToMaybe program "PROGRAM F if == -1.5 - a 1 THEN v = 1."

invalid_program_branch_then :: Test
invalid_program_branch_then = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual = parseToMaybe program "PROGRAM F IF == -1.5 - a 1 then v = 1."

invalid_program_branch_pred :: Test
invalid_program_branch_pred = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual = parseToMaybe program "PROGRAM F IF - -1.5 - a 1 THEN v = 1."

valid_program_branch_else :: Test
valid_program_branch_else = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = "if (-1.5=(a-1)) then v:=1;b:=(a+1) else v:=2;b:=(v-2) fi"
        actual =  topLevel program "PROGRAM Abc123 \
          \ IF == \
          \    -1.5 \
          \    - a 1 \
          \ THEN \
          \ { \
          \   v = 1; \
          \   b = + a 1 \
          \ } \
          \ ELSE \
          \ { \
          \   v = 2; \
          \   b = - v 2 \
          \ }."

invalid_program_branch_else :: Test
invalid_program_branch_else = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual =  parseToMaybe program "PROGRAM Abc123 \
          \ IF == \
          \    -1.5 \
          \    - a 1 \
          \ THEN \
          \ { \
          \   v = 1; \
          \   b = + a 1 \
          \ } \
          \ else \
          \ { \
          \   v = 2; \
          \   b = - v 2 \
          \ }."

valid_program_while_branch :: Test
valid_program_while_branch = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = "f1:=0;while (f1=/=10) do f1:=(f1+1) od;if (f1=10) then f1:=0 fi"
        actual =  topLevel program "PROGRAM Foo f1 = 0; \
          \ WHILE /= f1 10 DO \
          \ f1 = + f1 1; \
          \ IF == f1 10 THEN f1 = 0."

invalid_program_while_branch_no_semicolon :: Test
invalid_program_while_branch_no_semicolon = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual = parseToMaybe program "PROGRAM Foo f1 = 0; \
          \ WHILE /= f1 10 DO \
          \ f1 = + f1 1 \
          \ IF == f1 10 THEN f1 = 0."

valid_program_fib :: Test
valid_program_fib = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = "n:=100;ret:=0;if (n<=1) then ret:=a else index:=0;a:=0;b:=1;while (index<=(n-1)) do c:=(a+b);a:=b;b:=c od;ret:=b fi;skip"
        actual =  topLevel program "PROGRAM Fib \
        \ n = 100; \
        \ ret = 0; \
        \ IF <= n 1 THEN { \
        \   ret = a \
        \ } ELSE { \
        \   index = 0; \
        \   a = 0; \
        \   b = 1; \
        \   WHILE <= index - n 1 DO { \
        \     c = + a b; \
        \     a = b; \
        \     b = c \
        \   }; \
        \   ret = b \
        \ }; \
        \ NOOP."

invalid_program_fib_no_period :: Test
invalid_program_fib_no_period = TestCase $ assertEqual errStr expected actual
  where errStr = "Expected does not match actual"
        expected = Nothing
        actual =  parseToMaybe program "PROGRAM Fib \
        \ n = 100; \
        \ ret = 0; \
        \ IF <= n 1 THEN { \
        \   ret = a \
        \ } ELSE { \
        \   index = 0; \
        \   a = 0; \
        \   b = 1; \
        \   WHILE <= index - n 1 DO { \
        \     c = + a b; \
        \     a = b; \
        \     b = c \
        \   }; \
        \   ret = b \
        \ }; \
        \ NOOP"

tests :: Test
tests =
  TestList
    [TestLabel "A program with a valid name" valid_program_name
    ,TestLabel "A program with a valid while loop" valid_program_while
    ,TestLabel "A program with a loop and if statement" valid_program_while_branch
    ,TestLabel "A program with an if statement (no else)" valid_program_branch
    ,TestLabel "A program with an if-else statement" valid_program_branch_else
    ,TestLabel "A valid implementation of fibonacci" valid_program_fib
    ,TestLabel "A program with an invalid name" invalid_program_name
    ,TestLabel "A program with WHILE in the incorrect format" invalid_program_while
    ,TestLabel "A program with WHILE DO in the incorrect format" invalid_program_while_do
    ,TestLabel "A program with the loop predicate in an invalid format" invalid_program_while_pred
    ,TestLabel "A program with an invalid IF" invalid_program_branch
    ,TestLabel "A program with an invalid IF-THEN" invalid_program_branch_then
    ,TestLabel "A program with an invalid IF predicate" invalid_program_branch_pred
    ,TestLabel "A program with an invalid IF-THEN-ELSE" invalid_program_branch_else
    ,TestLabel "A program missing a ;" invalid_program_while_branch_no_semicolon
    ,TestLabel "A Program that does not end in ." invalid_program_fib_no_period
    ]
