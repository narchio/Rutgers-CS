CS 314
Programming Assignment III
==========================

Due: Tuesday, April 16
Submit by 3:00 AM on Wednesday, April 17

For this project, you will write one regular expression and six functions,
described below. You are strongly encouraged to save a copy of the provided
skeleton file and use it as the basis of your submission.

You MUST submit your project as a single text document named "Project3.hs". Your
project must declare a module "Project3" and include the following data type
definitions exactly as shown:

    data RE a
        = Symbol a
        | Empty
        | RE a :+: RE a
        | RE a :|: RE a
        | Repeat (RE a)
        | Plus (RE a)
        deriving (Show, Read, Eq, Ord)

    data ABC = A | B | C deriving (Show, Read, Eq, Ord)

You MUST include definitions for each of the regular expressions and functions
described below, even if that definition is just "undefined". Your code will
not compile with the test script if your file is incomplete.

SUBMISSIONS THAT DO NOT COMPILE WILL RECEIVE ZERO POINTS. Again, you are
strongly encouraged to save a copy of the provided skeleton file and us it as
the basis of your submission.

LATE ASSIGNMENTS WILL NOT BE ACCEPTED. There will be no extensions and no grace
period. You are advised to submit at least once before April 16, as insurance
against accident or disaster. You are also advised to keep several copies of
your files while working, just in case your finger slips and you delete
something important.

After submitting, be sure to double-check that you have submitted the correct
file.


Using this test script
----------------------

This document is a literate Haskell file. Lines beginning with ">" will be
interpreted as Haskell source code by GHC and GHCi. This document defines a
Haskell program that will execute several test cases using your code and report
any failed tests.

Be aware that these tests are not sufficient to prove your code correct. You
must apply your own reasoning to determine whether your code returns the correct
answers for all valid inputs.

If you like, you may extend the test cases given here by adding your own tests.
Doing so will not affect your grade directly.


Part I: Creating Regular Expressions
------------------------------------

atMost2As :: RE ABC

Create a regular expression over the ABC alphabet that matches any string
containing no more than two A's.

Using the match function we defined in class (also provided in this document),
you would see:

    *Main> match atMost2As [A,B]
    True
    *Main> match atMost2As [C,B]
    True
    *Main> match atMost2As [A,A,A]
    False



anyOf :: [a] -> RE a

Given a non-empty list of symbols in some alphabet, return a regular expression
that matches any symbol in the list.

    *Main> match (anyOf [B,C]) [A]
    False
    *Main> match (anyOf [B,C]) [B]
    True
    *Main> match (anyOf [B,C]) [B,C]
    False



repeats :: Int -> Int -> RE a -> RE a

repeats m n r returns a regular expression that matches the regular expression
r no fewer than m and no more than n times.

    *Main> match (repeats 2 4 (Symbol B :+: Symbol C)) [B,C]
    False
    *Main> match (repeats 2 4 (Symbol B :+: Symbol C)) [B,C,B,C]
    True
    *Main> match (repeats 2 4 (Symbol B :+: Symbol C)) [B,C,B,C,B,C,B,C,B,C]
    False



Part II: Analyzing Regular Expressions
--------------------------------------

matchEmpty :: RE a -> Bool

Determine whether a regular expression can match an empty string. Note that the
type does not restrict a to types with equality tests, so you cannot simply
use matches with an empty string.

    *Main> matchEmpty Empty
    True
    *Main> matchEmpty (Symbol A)
    False
    *Main> matchEmpty (Repeat (Symbol A))
    True
    *Main> 



minLength :: RE a -> Integer

Return the minimum length of any string that matches the regular expression.

    *Main> minLength (Repeat (Symbol A))
    0
    *Main> minLength (Symbol C :+: (Symbol B :|: Empty) :+: Symbol A)
    2



maxLength :: RE a -> Maybe Integer

Return the maximum length of any string that matches the regular expression,
or Nothing if there is no maximum (i.e., strings may be arbitrarily long).

    *Main> maxLength (Repeat (Symbol A))
    Nothing
    *Main> maxLength (Symbol C :+: (Symbol B :|: Empty) :+: Symbol A)
    Just 3



firstMatches :: RE a -> [a]

Return a finite list containing every symbol that occurs first in some string
that matches the regular expression.

Note: Because we do not know anything about the symbol type, it is not possible
to ensure that the list contains no duplicates or is in any particular order.
We do require that the list be finite, even if the regular expression matches
infinitely many strings.

For illustrative purposes, the examples below use sort and nub to put the
symbols in a deterministic order and remove any duplicates.

    *Main> nub (sort (firsts (Symbol A)))
    [A]
    *Main> nub (sort (firsts ((Symbol A :|: Empty) :+: Symbol B)))
    [A,B]
    *Main> nub (sort (firsts (Symbol A :+: Repeat (Symbol B))))
    [A]



> module Main where
>
> import Control.Monad
> import Control.Exception
> import Data.List
> import System.Timeout
>
> import Project3 (RE(..), ABC(..))
> import qualified Project3 as P
>
> -- ensure correct types
> atMost2As :: RE ABC
> atMost2As = P.atMost2As
>
> anyOf :: [a] -> RE a
> anyOf = P.anyOf
>
> repeats :: Int -> Int -> RE a -> RE a
> repeats = P.repeats
>
> matchEmpty :: RE a -> Bool
> matchEmpty = P.matchEmpty
>
> minLength :: RE a -> Integer
> minLength = P.minLength
>
> maxLength :: RE a -> Maybe Integer
> maxLength = P.maxLength
>
> firsts :: RE a -> [a]
> firsts = P.firsts
>
> -- test cases
> tests =
>     [( "atMost2As",
>         [ testRE atMost2As "AB" True
>         , testRE atMost2As "AAB" True
>         , testRE atMost2As "CB" True
>         , testRE atMost2As "" True
>         , testRE atMost2As "AAA" False
>         , testRE atMost2As "ABBACCA" False
>         ])
>     , ( "anyOf",
>         [ testRE bc "A" False
>         , testRE bc "B" True
>         , testRE bc "C" True
>         , testRE bc "BC" False
>         ])
>     , ( "repeats",
>         [ testRE bcs "" False
>         , testRE bcs "BC" False
>         , testRE bcs "BCBC" True
>         , testRE bcs "BCBCBC" True
>         , testRE bcs "BCBCBCBC" True
>         , testRE bcs "BCBCBCBCBC" False
>         , testRE (repeats 0 3 ac) "" True
>         , testRE (repeats 0 3 ac) "ACACAC" True
>         , testRE (repeats 0 3 ac) "ACACACAC" False
>         , testRE (repeats 10 10 (Symbol A)) "AAAAAAAAAA" True
>         ])
>     , ( "matchEmpty",
>         [ test "A" (matchEmpty (Symbol A)) False
>         , test "A*" (matchEmpty (Repeat (Symbol A))) True
>         , test "A+" (matchEmpty (Plus (Symbol A))) False
>         , test "e" (matchEmpty Empty) True
>         , test "e*" (matchEmpty (Repeat Empty)) True
>         , test "A|e" (matchEmpty (opt (Symbol A))) True
>         , test "(A|e)(B|e)" (matchEmpty (opt (Symbol A) :+: opt (Symbol B))) True
>         , test "(A|e)B" (matchEmpty (opt (Symbol A) :+: Symbol B)) False
>         , test "(A|e)+" (matchEmpty (Plus (opt (Symbol A)))) True
>         , test "C(B|e)(A|e)" (matchEmpty (Symbol C :+: opt (Symbol B) :+: opt (Symbol A))) False
>         , test "C(B|e)A" (matchEmpty (Symbol C :+: opt (Symbol B) :+: Symbol A)) False
>         ])
>     , ( "minLength",
>         [ test "A" (minLength (Symbol A)) 1
>         , test "A*" (minLength (Repeat (Symbol A))) 0
>         , test "A+" (minLength (Plus (Symbol A))) 1
>         , test "e" (minLength Empty) 0
>         , test "e*" (minLength (Repeat Empty)) 0
>         , test "A|e" (minLength (opt (Symbol A))) 0
>         , test "(A|e)(B|e)" (minLength (opt (Symbol A) :+: opt (Symbol B))) 0
>         , test "(A|e)B" (minLength (opt (Symbol A) :+: Symbol B)) 1
>         , test "(A|e)+" (minLength (Plus (opt (Symbol A)))) 0
>         , test "C(B|e)(A|e)" (minLength (Symbol C :+: opt (Symbol B) :+: opt (Symbol A))) 1
>         , test "C(B|e)A" (minLength (Symbol C :+: opt (Symbol B) :+: Symbol A)) 2
>         ])
>     , ( "maxLength",
>         [ test "A" (maxLength (Symbol A)) (Just 1)
>         , test "A*" (maxLength (Repeat (Symbol A))) Nothing
>         , test "A+" (maxLength (Plus (Symbol A))) Nothing
>         , test "e" (maxLength Empty) (Just 0)
>         , test "e*" (maxLength (Repeat Empty)) (Just 0)
>         , test "A|e" (maxLength (opt (Symbol A))) (Just 1)
>         , test "(A|e)(B|e)" (maxLength (opt (Symbol A) :+: opt (Symbol B))) (Just 2)
>         , test "(A|e)B" (maxLength (opt (Symbol A) :+: Symbol B)) (Just 2)
>         , test "(A|e)+" (maxLength (Plus (opt (Symbol A)))) Nothing
>         , test "C(B|e)(A|e)" (maxLength (Symbol C :+: opt (Symbol B) :+: opt (Symbol A))) (Just 3)
>         , test "C(B|e)A" (maxLength (Symbol C :+: opt (Symbol B) :+: Symbol A)) (Just 3)
>         ])
>     , ( "firsts",
>         [ testBy seteq "A" (firsts (Symbol A)) [A]
>         , testBy seteq "A*" (firsts (Repeat (Symbol A))) [A]
>         , testBy seteq "A+" (firsts (Plus (Symbol A))) [A]
>         , testBy seteq "e" (firsts Empty) ([] :: [ABC])
>         , testBy seteq "e*" (firsts (Repeat Empty)) ([] :: [ABC])
>         , testBy seteq "A|e" (firsts (opt (Symbol A))) [A]
>         , testBy seteq "(A|e)(B|e)" (firsts (opt (Symbol A) :+: opt (Symbol B))) [A,B]
>         , testBy seteq "(A|e)B" (firsts (opt (Symbol A) :+: Symbol B)) [A,B]
>         , testBy seteq "(A|e)+" (firsts (Plus (opt (Symbol A)))) [A]
>         , testBy seteq "C(B|e)(A|e)" (firsts (Symbol C :+: opt (Symbol B) :+: opt (Symbol A))) [C]
>         , testBy seteq "C(B|e)A" (firsts (Symbol C :+: opt (Symbol B) :+: Symbol A)) [C]
>         ])
>     ]
> 
> testRE r str = test str (match r (map (read . return) str))
>
> bc = P.anyOf [B, C]
> ac = Symbol A :+: Symbol C
> bcs = P.repeats 2 4 (Symbol B :+: Symbol C)
> 
> opt r = r P.:|: P.Empty
>
> seteq s1 s2 = all (`elem` s1) s2 && all (`elem` s2) s1
>
> match :: (Eq a) => P.RE a -> [a] -> Bool
> match r = any (null . snd) . matchPre r
> 
> matchPre :: (Eq a) => P.RE a -> [a] -> [(Bool,[a])]
> matchPre (P.Symbol a) (x:xs) | x == a = [(True, xs)]
> matchPre (P.Symbol a) _ = []
> matchPre P.Empty xs = [(False, xs)]
> matchPre (r P.:|: s) xs = matchPre r xs ++ matchPre s xs
> matchPre (r P.:+: s) xs = do
>     (r_consumed, ys) <- matchPre r xs
>     (s_consumed, zs) <- matchPre s ys
>     return (r_consumed || s_consumed, zs)
> matchPre (P.Repeat r) xs = (False, xs) : do
>     (r_consumed, ys) <- matchPre r xs
>     guard r_consumed
>     (_, zs) <- matchPre (P.Repeat r) ys
>     return (True, zs)
> matchPre (P.Plus r) xs = do
>     (r_consumed, ys) <- matchPre r xs
>     (s_consumed, zs) <- matchPre (P.Repeat r) ys
>     return (r_consumed || s_consumed, zs)
>
> timelimit = 5000000
>
> testBy :: (Show a) => (a -> a -> Bool) -> String -> a -> a -> IO Bool
> testBy eq name got want = catch body handle
>     where
>     body = do
>         ok <- timeout timelimit (evaluate (eq got want))
>         case ok of
>             Just True -> return True
>             Just False -> fail "FAIL " (show got)
>             Nothing -> fail "TIMEOUT" "impatient"
> 
>     handle :: SomeException -> IO Bool
>     handle e = fail "ERROR" (show e)
> 
>     fail msg txt = do
>         putStrLn $ msg ++ ": " ++ name
>         putStrLn $ "       wanted: " ++ show want
>         putStrLn $ "       got:    " ++ txt
>         return False
>
> test :: (Eq a, Show a) => String -> a -> a -> IO Bool
> test = testBy (==)
>
> testGroup :: (String, [IO Bool]) -> IO Int
> testGroup (name, units) = do
>     putStrLn $ "\nTesting " ++ name
>     fmap (length . filter not) (sequence units)
> 
> runTests groups = do
>     putStrLn "Running unit tests for PA3."
>     fails <- fmap sum $ mapM testGroup groups
>     putStrLn "\nComplete."
>     case () of
>         _ | fails == 0 -> putStrLn "No failures."
>           | fails == 1 -> putStrLn "1 failure."
>           | otherwise  -> putStrLn $ show fails ++ " failures."
> 
> main = runTests tests