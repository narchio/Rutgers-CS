CS 314
Programming Assignment IV
=========================

Due: Tuesday, April 23
Submit by 3:00 AM on Wednesday, April 24

Release 2

> import Control.Exception
> import Control.Monad
> import Data.Either
> import Data.List
> import System.Timeout
> import Test.QuickCheck
>
> import Project4 (DFA(..), ABC(..), atMost2As, oddAs, hasABC, extra_credit, multiplex)


For this project, you will define three DFAs using the DFA data type defined
in class.

    data DFA state symbol = DFA
        { alphabet :: [symbol]
        , states   :: [state]
        , initial  :: state
        , transit  :: state -> symbol -> state
        , accept   :: state -> Bool
        }

The three DFAs are:

atMost2As - matches all strings over the alphabet {A,B,C} containing at most
    two A's

oddAs - matches all strings over the alphabet {A,B,C} containing an odd number
    of A's

hasABC - matches all strings over the alphabet {A,B,C} containing the sequence
    ABC
    
In your definitions, you will use Ints to identify states and the pre-defined
type ABC to represent symbols.

    data ABC = A | B | C deriving (Show, Read, Eq, Ord)

You may use the accepts function defined in class (and in this module) to
experiment with your DFAs.

> accepts :: DFA state symbol -> [symbol] -> Bool
> accepts d = accept d . foldl (transit d) (initial d)


This file will run several tests to check the correctness of your DFAs. These
include:

    1. Is the initial state listed in the list of states?
    2. Does every combination of symbol and state give a state in the list of
       states?
    3. For a large number of randomly-generated strings, does the DFA accept
       or reject strings correctly?

Test 1 is worth 2 points. Tests 2 and 3 are each worth 4 points.


Process
-------

Save the provided skeleton file Project4.hs and use it as the basis of your
assignment. You may copy accepts into your module for testing, or use the
one defined in this module.

When submitting, upload your Project4.hs file only.

To use this module to test your code, you will need to have QuickCheck
installed. Follow the instructions given for PA2 or test using an iLab machine.

If you are attempting the extra credit, change the definition of extra_credit
to True.

You MUST include definitions for the types and DFAs described above, and the
function multiplex, even if that definition is just "undefined". You must define
a Boolean constant extra_credit. Your code will not compile with the test script
if your file is incomplete.

SUBMISSIONS THAT DO NOT COMPILE WILL RECEIVE ZERO POINTS. Again, you are
strongly encouraged to save a copy of the provided skeleton file and use it as
the basis of your submission.

LATE ASSIGNMENTS WILL NOT BE ACCEPTED. There will be no extensions and no grace
period. You are advised to submit at least once before April 23, as insurance
against accident or disaster. You are also advised to keep several copies of
your files while working, just in case your finger slips and you delete
something important.

After submitting, be sure to double-check that you have submitted the correct
file.


Extra Credit
------------

For an additional 10 points, you may write the function multiplex, which takes
two DFAs and creates a new DFA that accepts interleaved strings accepted by
both. Given two DFAs, d1 :: DFA s1 a1 and d2 :: DFA s2 a2, multiplex d1 d2
will take strings in the alphabet Either a1 a2.

We expect this property to hold:

> prop_multiplex d1 d2 str = 
>     accepts (multiplex d1 d2) str ==
>         (accepts d1 (lefts str) && accepts d2 (rights str))

where lefts :: [Either a b] -> [a] and rights :: [Right a b] -> [b], extract the
Left-marked and Right-marked values in a list, respectively.

    lefts [Left 1, Right 'a', Left 2] = [1,2]

More concretely:

    accepts (multiplex oddAs hasABC) [Left A, Right A, Right B, Right C]
        = True

here, we send [A] to oddAs and [A,B,C] to hasABC. Both accept.

    accepts (multiplex oddAs hasABC) [Right A, Left A, Right B, Left A, Right C]
        = False

here, we send [A,A] to oddAs and [A,B,C] to hasABC. Only hasABC accepts, so
the interleaved string is rejected.

Note that multiplex is given a placeholder type:

    multiplex :: DFA s1 a1 -> DFA s2 a2 -> DFA () (Either a1 a2)

You will need to decide the type of states used by the multiplexed DFA, and
replace () with that type. However, the types of the states and symbols for the
arguments must remain polymorphic. You will be tested with different DFAs using
multiple state and symbol types.




Testing code
------------

These are the three correctness tests.

First, check that the initial state is in the list of states.

> check_initial d 
>     | initial d `elem` states d = return 2
>     | otherwise = do
>         putStrLn "Initial state not in list of states"
>         return 0

Next, check every combination of symbol and state, and confirm that the
destination state is in the list of states.

> check_transit d = check `catch` \(SomeException e) -> do
>     putStrLn $ "Caught exception " ++ show e
>     return 0
> 
>     where
>     errors = do
>         s <- states d
>         x <- alphabet d
>         let t = transit d s x
>         guard $ not (t `elem` states d)
>         return $ concat ["  ", show s, " -", show x, "-> ", show t]
>     
>     check = do
>         errorlist <- timeout timelimit (evaluate (rnf errors))
>         case errorlist of
>             Nothing -> do
>                 putStrLn "Timed out."
>                 return 0
>             Just [] -> return 4
>             Just errs -> do
>                 mapM_ putStrLn $ "Invalid transitions:" : errs
>                 return 0
> 
> -- ensure that a list gets fully evaluated
> rnf l = length l `seq` l

This performs the three tests for a DFA, including the third that that
checks that the DFA accepts exactly those strings which satsify 'condition'.

> check_dfa name d condition = do
>     putStrLn $ "\nTesting " ++ name
>     checks `catch` \(SomeException e) -> do
>         putStrLn $ "Caught exception: " ++ show e
>         return 0
>     where
>     checks = fmap sum $ sequence
>             [ check_initial d
>             , check_transit d
>             , prop $ \str -> within timelimit (accepts d str == condition str)
>             ]

These are the correctness conditions for the three DFAs.

> prop_atMost2As str = length (filter (== A) str) < 3
>
> prop_oddAs = odd . length . filter (== A)
>
> prop_hasABC = any ((== [A,B,C]) . take 3) . tails

This is another DFA which will be used to test multiplex. It accepts only
the string [()].

> one = DFA
>     { alphabet = [()]
>     , states   = [A,B,C]
>     , initial  = A
>     , transit  = delta
>     , accept   = (== B)
>     }
>     where
>     delta A () = B
>     delta _ _  = C

This code checks the correctness of multiplex.

> check_multiplex name d1 d2 =
>      check_dfa name (multiplex d1 d2) $ \str ->
>          accepts d1 (lefts str) && accepts d2 (rights str)
>
> check_extra_credit
>     | extra_credit = do
>           putStrLn "\n** Testing extra credit"
>           scores <- sequence tests
>           return (sum scores / genericLength tests)
>     | otherwise = return 0
>     where
>     tests = 
>        [ check_multiplex "multiplex oddAs one" oddAs one
>        , check_multiplex "multiplex one atMost2As" one atMost2As
>        , check_multiplex "multiplex oddAs atMost2As" oddAs atMost2As
>        , check_multiplex "multiplex atMost2As hasABC" atMost2As hasABC
>        ]
> 
> timelimit = 5000000
> 
> args = stdArgs { maxSuccess = 500 }
> 
> prop p = do
>     r <- quickCheckWithResult args p
>     case r of
>         Success{} -> return 4
>         _ -> return 0
> 
> instance Arbitrary ABC where
>     arbitrary = oneof [return A, return B, return C]
>
> main = do
>     putStrLn "PA4 release 2"
>     scores <- sequence
>         [ check_dfa "atMost2As" atMost2As prop_atMost2As
>         , check_dfa "oddAs" oddAs prop_oddAs
>         , check_dfa "hasABC" hasABC prop_hasABC
>         , check_extra_credit
>         ]
>     putStrLn $ "\nComplete.\nScore: " ++ show (sum scores)