module Project2 where

import Data.Ord
import Data.List

----------------------
-- Helper Functions --
----------------------


-- helper, splitEvery (from Data.List.Grouping, but would not work on iLab most likely because it is fairly new)
splitEvery :: Int -> [a] -> [[a]]
splitEvery _ [] = []
splitEvery i xs = 
  	     let 
	     	(x1,x2) = splitAt i xs
	     in 
		x1 : splitEvery i x2

-- helper computeZip to place indeces on the lists
computeZip :: [[Double]] -> [(Integer, Double)]
computeZip [[]] = []
computeZip [] = []
computeZip (m:ms) = (zip [0..] m) ++ (computeZip ms)

-- helper to computeMap to map the 2nd and remove the indeces
computeMap :: [[(Integer, Double)]] -> [Double]
computeMap [] = []
computeMap [[]] = []
computeMap (m:ms) = (map snd m) ++ (computeMap ms)


-- helper to get the first element of a tripple
first :: (Int, Int, Double) -> Int
first (n,_,_) = n

-- helper to get the second element of a tripple
secnd :: (Int, Int, Double) -> Int
secnd (_,n,_) = n

-- helper definiton to get the third element of a tripple 
third :: (Int, Int, Double) -> Double
third (_,_,n) = n

-- This function creates all diagonals with 0's in them 
diag0 :: Int -> [(Int, Double)] 
diag0 0 = []
diag0 n = [(x,0) | x <- [0..n-1]]

-- This function will sort the diags and the 0'd diags to return the correct order 
sortThrough :: [(Int, Double)] -> [(Int, Double)] -> [(Int, Double)]
sortThrough [] [] = []
sortThrough [] list2 = list2
sortThrough list1 [] = list1
sortThrough list1 list2 = 
	      let 
	      	  a = fst (list1!!0)
		  b = snd (list1!!0)
		  c = fst (list2!!0)
		  d = snd (list2!!0)
	      	  
		  x1 = (tail list1) ++ ([head list1])
                  y1 = (tail list2) ++ ([(c,b)])
	      	  x2 = list1                    
		  y2 = (tail list2) ++ ([head list2])
	      in 
	      	 if ((a == c) && (b /= d))
                     then [] ++ sortThrough x1 y1
                          else if (a /= c)
           	            	   then [] ++ sortThrough x2 y2
                                       	else if ((a == c) && (b == d))
                                 		  then list2
                                                        else list2


------------------------
-- Project2 functions --
------------------------



-- Creates a 0'ed out matrix, using the replicate function twice, replicating 0's for the columns that many times (rows times) 
zero :: Int -> Int -> [[Double]]
zero 0 0 = [[]]
zero rows cols = [[ 0 | y <- [0..cols-1]] | x <- [0..rows-1]]



-- Given a positive integer, return an identity matrix of that size
ident :: Int -> [[Double]]
ident 0 = [[]]
ident i = [[ if (x == y) then (1) else (0) | y <- [0..i-1]] | x <- [0..i-1]]



--Given a well-formed matrix, return its diagonal (that is, the values where the row and column number are equal)
diag :: [[Double]] -> [Double]
diag m = 
     let 
     	cols = length (m !! 0)
	rows = length (m)
     	matrix = [[(m !! x !! y) | x <- [0..rows-1], x == y] | y <- [0..cols-1]] 
     in
	concat matrix


--Given two well-formed matrices of the same dimensions, return their matrix sum (computed by element-wise addition)
add :: [[Double]] -> [[Double]] -> [[Double]]
add [] [] = []
add [[]] [[]] = [[]]
add (m1:m1s) (m2:m2s) = (zipWith (+) m1 m2) : (add m1s m2s) 



{-Given a well-formed matrix of size r * c, return its transpose. This will be a matrix of size c * r, where the element in row i and column j of the input will be in row j and column i of the transpose -}
transp :: [[Double]] -> [[Double]]
transp m = 
       let 
       	   size = length m
	   zs = computeZip (m)
	   sortedPairs = sortBy(comparing fst) zs
	   listOfLists = splitEvery size sortedPairs
	   zss = computeMap (listOfLists)
       in
	   splitEvery size zss


---------------------
--SPARSE functions --
---------------------

data Sparse = Sparse Int Int [(Int,[(Int,Double)])]
    deriving (Show, Eq)

-- helper to get the list part of the sparse
getLS :: Sparse -> [(Int,[(Int,Double)])]
getLS (Sparse m n ls) = ls

-- helper to get the rows
getR :: Sparse -> Int
getR (Sparse m n ls) = m 

-- helper to get the cols
getC :: Sparse -> Int
getC (Sparse m n ls) = n



--Given a positive integer n, return the identity matrix of size n
sident :: Int -> Sparse
sident n = 
       let 
       	   list = [1 | x <- [0..n-1]]
	   zipList = zip [0..] list
	   splitList = splitEvery 1 zipList
	   final = zip [0..] splitList
       in 
       	  Sparse n n final 



-- Given a Sparse matrix, return its diagonal as a list of doubles. Note that this may include 0.0 values that are not directly given in the Sparse representation
sdiag :: Sparse -> [Double]
sdiag spar =
      let 
      	  ls = getLS spar
	  m = getR spar
	  n = getC spar
	  
	  -- rows
	  rowsLS = map (fst) ls
	  len = [length (snd (ls!!x)) | x <- [0..(length ls)-1]]
	  zipedRows = zip rowsLS len
	  
	  -- cols and vals
	  colsLS = map (snd) ls
	  cols = [map (fst) (colsLS!!x) | x <- [0..(length colsLS) - 1]] 	  
	  concols = concat cols
	  vals = [map (snd) (colsLS!!x) | x <- [0..(length colsLS) - 1]]
	  convals = concat vals
	  
	  -- now replicate 
	  lists = [replicate (snd (zipedRows!!x)) (fst (zipedRows!!x)) | x <- [0..(length zipedRows) - 1]]
	  rep = concat lists
	  
	  -- now merge them all together
	  trippp = zip3 rep concols convals

	  -- new bounds (aka how many diagonals we will have) 
	  bound = if (m < n) then m else n

	  -- now turn tripple into list1 and list2
	  eqcols = [secnd (trippp!!x) | x <- [0..(length trippp)-1], first (trippp!!x) == secnd (trippp!!x)]
	  eqvals = [third (trippp!!x) | x <- [0..(length trippp)-1], first (trippp!!x) == secnd (trippp!!x)]
	  list1 = zip eqcols eqvals
	  zeroList = [0.0 | x <- [0..bound-1]]
	  list2 = zip [0..bound-1] zeroList 
	  beforeSortCombined = sortThrough list1 list2	  
      	  afterSortCombined = sortBy (comparing fst) beforeSortCombined
      in	  	  
	  [snd (afterSortCombined!!x) | x <- [0..bound-1], x == fst (afterSortCombined!!x)]       	  
    	



-- Given two Sparse matrices of equal dimensions, compute their sum
sadd :: Sparse -> Sparse -> Sparse
sadd = undefined