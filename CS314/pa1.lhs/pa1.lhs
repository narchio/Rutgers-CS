CS 314
Programming Assignment I
========================

Due: Friday, March 1
Hand in by: March 2, 3:00 AM

For this assignment, you will write 5 short functions of varying degrees of
complexity, described below. The functions described here may involve some ideas
that have not been discussed as of Feb. 18, but will be covered before the
assignment is due.

Create a module named Project1 containing definitions for the six functions
described below. You may use the Project1.hs skeleton file as the basis for your
project, but this is not required. You are not required to include type
signatures in your module, but our test code will expect your functions to have
these types.

div7or9 :: Integer -> Boolean

    Given an integer, return whether the integer is divisible by either 7 or 9.

    Project1> div7or9 0
    True
    Project1> div7or9 1
    False
    Project1> 7
    True
    
    As a reminder, mod x y calculates the remainder after dividing x by y.

echo :: [Char] -> [Char]

    Given a list of characters, return a list where each character is repeated
    twice.
    
    Project1> echo "abc"
    "aabbcc"
    Project1> echo "d6 ?"
    "dd66  ??"
    
echons :: [Char] -> [Char]

    Given a list of characters, return a list where each character has been
    doubled, except spaces.
    
    Project1> echons "abc"
    "aabbcc"
    Project1> echons "d6 ?"
    "dd66 ??"
    Project1> echons "don't duplicate spaces"
    "ddoonn''tt dduupplliiccaattee ssppaacceess"

countEvens :: [Integer] -> Integer

    Given a list of integers, return the number of even integers in the list.
    
    Project1> countEvens [0,1,2,3]
    2
    Project1> countEvens [3]
    0
    Project1> countEvens []
    0
    
    You are free to use the standard function "even", which returns true
    if and only if its argument is an even integer.
    
    If you use "length", be aware that it returns Int, not Integer. To convert,
    you may use fromIntegral.

centroid :: [(Double, Double)] -> (Double, Double)

    Given a list of points, represented as integer pairs, return the centroid
    of the points. This will be a point where the x-coordinate is the average
    of all x-coordinates, and the y-coordinate is the average of all
    y-coordinates.
    
    You may assume that the list is non-empty.
    
    Project1> centroid [(0,0)]
    (0.0,0.0)
    Project1> centroid [(0,0),(2,2)]
    (1.0,1.0)
    Project1> centroid [(0,2),(2,0),(4,4)]
    (2.0,2.0)

hailstone :: Integer -> Integer

    Given a positive integer n, return the length of the hailstone sequence
    beginning with n.
    
    The hailstone sequence for an integer n can be found by repeatedly applying
    a specific function. We will write e[i] for element i in the sequence, and
    define:
    
        e[0] = n
    
        e[i+1] = e[i] / 2,     if e[i] is even
        e[i+1] = 3 * e[i] + 1, if e[i] is odd
        
    The sequence ends once e[i] is 1.

    Thus, the hailstone sequence for 3 is [3,10,5,16,8,4,2,1], and the length
    of the sequence is 8.
    
    Project1> hailstone 3
    8
    Project1> hailstone 1
    1
    Project1> hailstone 8
    4


Testing
-------

This file may be used to test your project. Place this file in the same
directory as Project1.hs, and use one of the following methods:

1. Run the test script using runghc

    $ runghc pa1.lhs

2. Compile the test script using ghc, and execute.

    $ ghc pa1.lhs
    $ ./pa1

3. Load the file into ghci, and run main.

    $ ghci pa1.lhs
    GHCi, version ...
    Ok, modules loaded ...
    *Main> main


In order for the test script to run, your project file must provide definitions
for each function. If you are not using the provided skeleton, you should still
define any unimplemented functions using "undefined". This will permit testing
before all your implementations are complete.


Submission
----------

Submit your Project1.hs file through the assignment page on Sakai. Do not submit
this file.


Grading
-------

We will test your code using the provided test cases and by comparing against
a reference implementation. You will receive half credit for any function
that passes the provided test cases, but does not agree with the reference
implementation for some valid argument.

You are strongly encouraged to perform additional testing on your own. The
test cases provided here are a starting point, but are not sufficient to
demonstrate correctness. Testing may simply be a matter of loading your module
in GHCi and trying out different arguments to your functions.


Test Script
-----------

This file is "literate" Haskell source code. In this style, lines beginning with
">" are interpreted as source code, and all other lines are documentation.

The following code will test your functions. It involves several Haskell
features that we have not discussed in class, but you are welcome to look
through it and discuss any questions you may have during office hours.

> module Main where
>
> import Control.Exception (catch, ErrorCall(..), evaluate)
> import qualified Project1 as P1
>
> 
> tests =
>   [ test "div7or9 0" (P1.div7or9 0) True
>   , test "div7or9 1" (P1.div7or9 1) False
>   , test "div7or9 7" (P1.div7or9 7) True
>   , test "div7or9 9" (P1.div7or9 9) True
>   , test "echo \"abc\"" (P1.echo "abc") "aabbcc"
>   , test "echo \"d6 ?\"" (P1.echo "d6 ?") "dd66  ??"
>   , test "echo \" . a f\"" (P1.echo " . a f") "  ..  aa  ff"
>   , test "echo \"   \"" (P1.echo "   ") "      "
>   , test "echons \"abc\"" (P1.echons "abc") "aabbcc"
>   , test "echons \"d6 ?\"" (P1.echons "d6 ?") "dd66 ??"
>   , test "echons \" . a f\"" (P1.echons " . a f") " .. aa ff"
>   , test "echons \"   \"" (P1.echons "   ") "   "
>   , test "countEvens [0,1,2,3]" (P1.countEvens [0,1,2,3]) 2
>   , test "countEvens [3]" (P1.countEvens [3]) 0
>   , test "countEvens []" (P1.countEvens []) 0
>   , test "countEvens [4,6,7,8]" (P1.countEvens [4,6,7,8]) 3
>   , test "centroid [(0,0)]" (P1.centroid [(0,0)]) (0,0)
>   , test "centroid [(0,0),(2,2)]" (P1.centroid [(0,0),(2,2)]) (1,1)
>   , test "centroid [(0,0),(0,2),(4,4)]" (P1.centroid [(2,0),(0,2),(4,4)]) (2,2)
>   , test "centroid [(10,1),(8,5),(-3,2),(2,2),(-1,1),(2,1)]" (P1.centroid [(10,1),(8,5),(-3,2),(2,2),(-1,1),(2,1)]) (3,2)
>   , test "hailstone 3" (P1.hailstone 3) 8
>   , test "hailstone 1" (P1.hailstone 1) 1
>   , test "hailstone 8" (P1.hailstone 8) 4
>   , test "hailstone 104" (P1.hailstone 104) 13
>   , test "hailstone 103" (P1.hailstone 103) 88
>   ]
> 
>
> testBy :: (Show a) => (a -> a -> Bool) -> String -> a -> a -> IO Bool
> testBy eq name got want = catch body (\(ErrorCall s) -> fail "ERROR" s)
>     where
>     body = do
>         ok <- evaluate (eq got want)
>         if ok
>             then do
>                 putStrLn $ "PASS : " ++ name
>                 return True
>             else fail "FAIL " (show got)
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
> runTests n f [] = 
>     putStrLn $ "Completed " ++ show n ++ " tests. " ++ show f ++ " failures."
> runTests n f (t:ts) = do
>     pass <- t
>     runTests (n+1) (if pass then f else f + 1) ts
>
> main = runTests 0 0 tests