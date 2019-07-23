CS 314
Programming Assignment II
=========================

Due: March 29
Submit by March 30, 3:00 AM

For this assignment, you will write 7 or 8 Haskell functions of varying
complexity. It is recommended that you begin with the skeleton Project2.hs file 
provided with the assignment.

LATE ASSIGNMENTS WILL NOT BE ACCEPTED. There will be no extensions and no grace
period. You are advised to submit at least once before March 29, as insurance
against accident or disaster. You are also advised to keep several copies of
your files while working, just in case your finger slips and you delete
something important.

DO NOT WAIT UNTIL THE LAST DAY TO BEGIN. While several of these functions are
simple and can be implemented in a few minutes, others will require some
thought. 

Make sure to test your submission before submitting, using this test script.
This script uses the QuickCheck library, which may not be included in all
Haskell distributions. However, it can be used with the GHC installed on the
iLab machines.


Using this Document
-------------------

This document is a literate Haskell file. Lines beginning with ">" will be
interpreted as Haskell source code by GHC and GHCi.

> module Main where
> 
> import Control.Applicative
> import Test.QuickCheck
> import qualified Project2 as P

When loading this module in GHCi, your functions will be available with the
prefix "P." (that is, "zero" will be "P1.zero").

In addition to describing the functions your project must define, this document
defines several "properties" that your functions should satisfy. The test script
uses the QuickCheck library to test these properties with many, randomly
selected inputs; if the property is not satisfied for any input, QuickCheck
will print that input to the screen for your reference.

In addition to the test script, you may use QuickCheck directly to test these
properties as part of your development process. Simply load this file in GHCi,
and provide the property to the `quickCheck` function:

    *Main> quickCheck prop_zero_bounds
    +++ OK, passed 100 tests.

The 100 tests are the randomly chosen inputs given to `prop_zero_bounds`. In
this example, it returned `True` for every input, meaning that the property it
tests was satisfied for those inputs.

To run the complete set of tests, you may load this module in GHCi and enter
`main`. Alternately, you may use runghc on the Unix command line:

    $ runghc pa2.lhs
    Running zero: bounds
    +++ OK, passed 500 tests.
    ...



You may notice that the properties take unusual types as arguments, such as
`Dimension`. These are used by QuickCheck to ensure that only appropriate
values are used for testing. Several types, such as `Matrix` and `MatrixPair`
are defined in this document for similar purposes.


Grading
-------

Your submission will be graded using the properties defined here and by
comparison to a reference implementation. Each function is worth 10 points,
of which 5 will be given in proportion to the number of properties satisfied,
and the other 5 will be given if the function matches the reference
implementation for 500 randomly-chosen inputs.

To receive full credit, your implementation of the first seven functions must
be correct. You may implement an eighth function for extra credit.



Part I: Matrices
----------------

In this part of the assignment, you will write five functions that manipulate
matrices, represented as lists of lists of Doubles.

You may assume that any matrices given to your functions will be well-formed,
meaning that they contain at least one row and that each row contains the same
number of columns (that is, the matrix is a true rectangle). Where your function
is given a integer representing a matrix dimension, that integer will always be
positive.


The function below is used to confirm that a matrix is well-formed, and to
obtain its dimensions. It is not necessary for your code to use it, as your
functions will always be given well-formed matrices.

> bounds :: [[Double]] -> Maybe (Int,Int)
> bounds m
>     | rows < 1            = Nothing
>     | cols < 1            = Nothing
>     | any (/= cols) colss = Nothing
>     | otherwise           = Just (rows, cols)
>     where
>     rows = length m
> 
>     cols : colss = map length m

The functions you will define are:



    zero :: Int -> Int -> [[Double]]

Given positive integers representing the rows and columns, return a matrix
of those dimensions containing all zeros.

Properties:

1. The matrix must be well-formed and have the correct dimensions

> prop_zero_bounds (Dimension r) (Dimension c) = bounds (P.zero r c) == Just (r,c)

2. All values in the matrix must be 0.0

> prop_zero_allzero (Dimension r) (Dimension c) = all (all (== 0)) (P.zero r c)



    ident :: Int -> [[Double]]

Given a positive integer, return an identity matrix of that size.

    Project2> ident 3
    [[1.0,0.0,0.0],[0.0,1.0,0.0],[0.0,0.0,1.0]]

Properties:

1. The matrix must be well-formed and have the proper dimensions

> prop_ident_bounds (Dimension n) = bounds (P.ident n) == Just (n,n)

2. Every value in the matrix must be 0 or 1

> prop_ident_01 (Dimension n) = all (all (\x -> x == 0 || x == 1)) (P.ident n)


    
    diag :: [[Double]] -> [Double]
    
Given a well-formed matrix, return its diagonal (that is, the values where
the row and column number are equal).

    Project1> diag [[1,2],[3,4],[5,6]]
    [1,4]
    Project1> diag (ident 5)
    [1.0,1.0,1.0,1.0,1.0]

Properties:

1. Given a matrix of size r * c, the diagonal will have as many elements as the
smaller dimension.

> prop_diag_bounds (Matrix r c m) = length (P.diag m) == min r c



    add :: [[Double]] -> [[Double]] -> [[Double]]

Given two well-formed matrices of the same dimensions, return their matrix
sum (computed by element-wise addition).

    Project2> add [[1,2],[3,4]] [[10,20],[30,40]]
    [[11,22],[33,44]]


Properties:

1. Given two matrices of size r * c, their sum will be a well-formed matrix of
size r * c.

> prop_add_bounds (MatrixPair r c a b) = bounds (P.add a b) == Just (r,c)

2. For appropriately sized matrices X and Y, X + Y = Y + X.

> prop_add_commute (MatrixPair _ _ a b) = P.add a b == P.add b a

3. For a well-formed matrix X, X + (-X) = 0

> prop_add_neg (Matrix _ _ m) = all (all (== 0)) (P.add m (map (map negate) m) )

4. For a well-formed matrix X, X + 0 = X. (Note that this also relies on your
implementation of zero.)

> prop_add_zero (Matrix r c m) = P.add m (P.zero r c) == m



    transp :: [[Double]] -> [[Double]]
    
Given a well-formed matrix of size r * c, return its transpose. This will be
a matrix of size c * r, where the element in row i and column j of the input
will be in row j and column i of the transpose.

    Project2> transp [[1,2],[3,4],[5,6]]
    [[1.0,3.0,5.0],[2.0,4.0,6.0]]

Properties:

1. If the input has size r * c, the output will have size c * r.

> prop_transp_bounds (Matrix r c m) = bounds (P.transp m) == Just (c,r)

2. The transpose of the transpose of a matrix is the original matrix.

> prop_transp_idem (Matrix _ _ m) = P.transp (P.transp m) == m



Part II: Sparse Matrices
------------------------

In this section, you will write two or three functions that work with a sparse
matrix representation. Ensure that your Project2.hs contains this definition:

    data Sparse = Sparse Int Int [(Int, [(Int, Double)])]
        deriving (Show, Eq)

Sparse matrices are used in applications where most entries in the matrix are 0.
The representation only includes the non-zero entries. In this representation,
the number of rows and columns are given as the first two fields. The remaining
field is a list of tuples that identify non-zero rows by index, and provide
the non-zero values in that row, also with indices. The indicies start with 0.

For example, this matrix

    [[1, 0, 0]
    ,[0, 0, 0]
    ,[2, 0, 3]]

has the following sparse representation

    Sparse 3 3 [(0, [(0,1)]), (2, [(0,2), (2,3)])]


A Sparse must meet several conditions to be valid. Among them:

1. The number of rows and columns must be positive
2. The row entries must have positive indices less than the number of rows, and
   must be strictly increasing.
3. If a row is given in the representation, it must contain at least one entry.
4. The entries in each row must have positive indices less than the number of
   columns, and must be strictly increasing.
5. No entry may contain zero.

This function tests that these properties hold. It is not necessary for your
code to use it, as your functions will only be tested with valid Sparse values.

> validSparse (P.Sparse r c m) = r > 0 && c > 0
>     && increasingRange (-1) r (map fst m)
>     && all (not . null . snd) m
>     && all (increasingRange (-1) c . map fst . snd) m
>     && all (all ((/= 0) . snd) . snd) m
> 
> increasingRange a z []     = True
> increasingRange a z (x:xs) = a < x && x < z && increasingRange x z xs

These functions are used to define certain properties:

> negSparse (P.Sparse r c m) = P.Sparse r c (map (fmap (map (fmap negate))) m)
> 
> sparseLookup r c (P.Sparse _ _ m) = maybe 0 (maybe 0 id . lookup c) (lookup r m)



    ident :: Int -> Sparse

Given a positive integer n, return the identity matrix of size n.

    Project2> ident 3
    Sparse 3 3 [(0,[(0,1.0)]),(1,[(1,1.0)]),(2,[(2,1.0)])]

Properties:

1. ident must produce valid Sparse matrices

> prop_sident_valid (Dimension n) = validSparse (P.sident n)

2. ident n must produce an n * n matrix.

> prop_sident_bounds (Dimension n) = r == n && c == n
>     where P.Sparse r c _ = P.sident n

3. ident n must contain n non-zero values

> prop_sident_len (Dimension n) = length m == n
>     where P.Sparse _ _ m = P.sident n

4. All non-zero values in the identity matrix must be 1.0

> prop_sident_ones (Dimension n) = all (== 1) . map snd . concatMap snd $ m
>     where P.Sparse _ _ m = P.sident n



    sdiag :: Sparse -> [Double]

Given a Sparse matrix, return its diagonal as a list of doubles. Note that this
may include 0.0 values that are not directly given in the Sparse representation.

    Project2> sdiag (Sparse 3 3 [(0,[(0,1)]), (2,[(2,3)])])
    [1.0,0.0,3.0]
    Project2> sdiag (Sparse 4 6 [])
    [0.0,0.0,0.0,0.0]


Properties:

1. Given a matrix of size r * c, the length of the diagonal is the smaller of
r and c.

> prop_sdiag_bounds m@(P.Sparse r c _) = length (P.sdiag m) == min r c

2. Element i in the diagonal equals the entry in row i, column i.

> prop_sdiag_diag m = and $ zipWith match [0..] (P.sdiag m)
>     where
>     match n x = sparseLookup n n m == x



    sadd :: Sparse -> Sparse -> Sparse
    
This function may be implemented for extra credit.

Given two Sparse matrices of equal dimensions, compute their sum.

    Project2> sadd (Sparse 2 2 [(0,[(1,1)]),(1,[(0,1)])]) (Sparse 2 2 [(0,[(0,2)])])
    Sparse 2 2 [(0,[(0,2),(1,1)]),(1,[(0,1)])]

Properties:

1. If matrices X and Y have size r * c, then X + Y has size r * c.

> prop_sadd_bounds (SparsePair x@(P.Sparse r c _) y) = r == r' && c == c'
>     where P.Sparse r' c' _ = P.sadd x y

2. The output of sadd must be a valid Sparse matrix.

> prop_sadd_valid (SparsePair x y) = validSparse (P.sadd x y)

3. X + Y = Y + X.

> prop_sadd_commute (SparsePair x y) = P.sadd x y == P.sadd y x

4. X + (-X) = 0
 
> prop_sadd_neg m = null xs
>     where P.Sparse _ _ xs = P.sadd m (negSparse m)



Support Code
------------

The remainder of this document is support code for testing properties.
QuickCheck uses the class `Arbitrary` to generate random values for testing,
but the instances provided for lists are not appropriate for testing matrices.
We define auxiliary types `Matrix` and `MatrixPair`, and provide instances of
`Arbitrary` that only generate well-formed matrices.

The iLab has QuickCheck 2.6 installed, which generates integers with a bad
distribution. To avoid this, we will use a custom `Arbitrary` instance for
positive integers.

> newtype Dimension = Dimension Int deriving (Show)
>
> instance Arbitrary Dimension where
>     arbitrary = sized $ \n -> Dimension <$> choose (1,n+1)
>
>     shrink (Dimension n) = [ Dimension m | m <- shrinkIntegral n, m > 0 ]


> arbmatrix :: Int -> Int -> Gen [[Double]]
> arbmatrix row col = vectorOf row (vector col)
> 
> drops :: [a] -> [[a]]
> drops [] = []
> drops (x:xs) = xs : map (x:) (drops xs)
> 
> shrinks :: (a -> [a]) -> [a] -> [[a]]
> shrinks s [] = []
> shrinks s (x:xs) = map (:xs) (s x) ++ map (x:) (shrinks s xs)

`Matrix` includes the number of rows and columns, so that we don't have to
recompute them later.

> data Matrix = Matrix { rows :: Int, cols ::  Int, matr :: [[Double]] }

We define a custom instance of Show to make the error messages more readable.

> instance Show Matrix where
>     showsPrec _ (Matrix r c (m:ms)) = showString "Matrix\n  { rows = "
>         . shows r
>         . showString "\n  , cols = "
>         . shows c
>         . showString "\n  , matr = ["
>         . shows m
>         . foldr (\m r -> showString ",\n            " . shows m . r) id ms
>         . showString "]\n  }"
> 
> instance Arbitrary Matrix where
>     arbitrary = do
>         Dimension rows <- arbitrary
>         Dimension cols <- arbitrary
>         Matrix rows cols <$> arbmatrix rows cols

QuickCheck uses `shrink` whenever it finds a failed test case, to see if there
is a smaller test case that also fails.

>     shrink (Matrix r c m) = shrow ++ shcol ++ shelem
>         where
>         shrow
>             | r > 1 = Matrix (r-1) c <$> drops m
>             | otherwise  = []
> 
>         shcol
>             | c > 1 = Matrix r (c-1) <$> mapM drops m
>             | otherwise = []
> 
>         shelem = Matrix r c <$> shrinks (shrinks shrink) m

`MatrixPair` includes two matrices with the same dimensions.

> data MatrixPair = MatrixPair
>     { prows :: Int
>     , pcols :: Int
>     , matrx :: [[Double]]
>     , matry :: [[Double]]
>     }
> 
> instance Show MatrixPair where
>     showsPrec _ (MatrixPair r c (x:xs) (y:ys))
>         = showString "Matrix\n  { prows = "
>         . shows r
>         . showString "\n  , pcols = "
>         . shows c
>         . showString "\n  , matrx = ["
>         . shows x
>         . foldr (\m r -> showString ",\n            " . shows m . r) id xs
>         . showString "]\n  , matry = ["
>         . shows y
>         . foldr (\m r -> showString ",\n            " . shows m . r) id ys
>         . showString "]\n  }"
> 
> instance Arbitrary MatrixPair where
>     arbitrary = do
>         Dimension rows <- arbitrary
>         Dimension cols <- arbitrary
>         MatrixPair rows cols <$> arbmatrix rows cols <*> arbmatrix rows cols
> 
>     shrink (MatrixPair r c x y) = shrow ++ shcol ++ shx ++ shy
>         where
>         shrow
>             | r > 1     = MatrixPair (r-1) c <$> drops x <*> drops y
>             | otherwise = []
> 
>         shcol
>             | c > 1     = MatrixPair r (c-1) <$> mapM drops x <*> mapM drops y
>             | otherwise = []
> 
>         shx = flip (MatrixPair r c) y <$> shrinks (shrinks shrink) x
> 
>         shy = MatrixPair r c x <$> shrinks (shrinks shrink) x

Generating Sparse matrices is more involved.

> arbsparseOfNE g n k
>     | n <= k    = (\k v -> [(k,v)]) <$> choose (0,n-1) <*> g
>     | otherwise = oneof
>         [ (\x xs -> (k,x):xs) <$> g <*> arbsparseOf g n (k+1)
>         , arbsparseOfNE g n (k+1)
>         ]
> 
> arbsparseOf :: Gen a -> Int -> Int -> Gen [(Int,a)]
> arbsparseOf g n k
>     | n <= k    = return []
>     | otherwise = oneof
>         [ (\x xs -> (k,x):xs) <$> g <*> arbsparseOf g n (k+1)
>         , arbsparseOf g n (k+1)
>         ]
> 
> arbsparse :: Int -> Int -> Gen [(Int,[(Int,Double)])]
> arbsparse rows cols = arbsparseOf (arbsparseOfNE (getNonZero <$> arbitrary) cols 0) rows 0
> 
> shrinkNZ 0    = []
> shrinkNZ 1    = []
> shrinkNZ (-1) = []
> shrinkNZ x    = signum x : [ signum x * (abs x ** (1 - 0.5^d)) | d <- [1..10]]
> 
> instance Arbitrary P.Sparse where
>     arbitrary = do
>         Dimension rows <- arbitrary
>         Dimension cols <- arbitrary
>         P.Sparse rows cols <$> arbsparse rows cols
> 
>     shrink (P.Sparse r c m)
>         =  (P.Sparse r c <$> drops m)              -- clobber rows
>         ++ (P.Sparse r c <$> shrinks shrinkRow m)  -- clobber items
> 
>         where
>         shrinkRow (r,[(c,x)]) = [(r,[(c,y)]) | y <- shrinkNZ x]
>         shrinkRow (r,cs) = map ((,) r) (drops cs ++ shrinks shrinkItem cs)
> 
>         shrinkItem (n,x) = map ((,) n) (shrinkNZ x)
> 
> data SparsePair = SparsePair P.Sparse P.Sparse deriving Show
> 
> instance Arbitrary SparsePair where
>     arbitrary = do
>         Dimension rows <- arbitrary
>         Dimension cols <- arbitrary
>         x <- arbsparse rows cols
>         y <- arbsparse rows cols
>         return $ SparsePair (P.Sparse rows cols x) (P.Sparse rows cols y)

Finally, the test harness below will test all the properties in sequence,
counting the number that succeed.

> tests =
>     [ ("zero: bounds", property prop_zero_bounds)
>     , ("zero: allzero", property prop_zero_allzero)
>     , ("ident: bounds", property prop_ident_bounds)
>     , ("ident: all 0 or 1", property prop_ident_01)
>     , ("diag: bounds", property prop_diag_bounds)
>     , ("add: bounds", property prop_add_bounds)
>     , ("add: x+y = y+x", property prop_add_commute)
>     , ("add: x-x = 0", property prop_add_neg)
>     , ("add: x+0 = x", property prop_add_zero)
>     , ("transp: bounds", property prop_transp_bounds)
>     , ("transp: T(T(x)) = x", property prop_transp_idem)
>     , ("sident: valid", property prop_sident_valid)
>     , ("sident: bounds", property prop_sident_bounds)
>     , ("sident: nonzeros", property prop_sident_len)
>     , ("sident: ones", property prop_sident_ones)
>     , ("sdiag: bounds", property prop_sdiag_bounds)
>     , ("sdiag: correct", property prop_sdiag_diag)
>     , ("sadd: bounds", property prop_sadd_bounds)
>     , ("sadd: valid", property prop_sadd_valid)
>     , ("sadd: x-x=0", property prop_sadd_neg)
>     , ("sadd: x+y=y+x", property prop_sadd_commute)
>     ]
> 
> args = stdArgs { maxSuccess = 500 }
> 
> runTests n s [] = putStrLn $ "Passed " ++ show s ++ " of " ++ show n
> runTests n s ((name,test):ts) = do
>     putStrLn $ "Running " ++ name
>     r <- quickCheckWithResult args test
>     runTests (n+1) (case r of Success{} -> s+1; _ -> s) ts
> 
> 
> main = runTests 0 0 tests
> 