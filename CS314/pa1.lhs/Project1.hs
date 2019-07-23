module Project1 where

div7or9 :: Integer -> Bool
div7or9 i   
	| mod i 7 == 0 = True
	| mod i 9 == 0 = True
	| otherwise = False


-- This function will echo the [Char] but will not echo spaces, similar to factorial in implementation --
echo :: [Char] -> [Char]
echo [] = []
echo (ch:chs)  = ch:ch:(echo chs) 


-- This function will echo the [Char] but will not echo spaces, an adaptation of echo --
echons :: [Char] -> [Char]
echons [] = []
echons (' ':chs) = ' ':(echons chs)
echons (ch:chs) = ch:ch:(echons chs)	


-- This function will keep track of the total number of even #'s by counting the even #'s -- 
countEvens :: [Integer] -> Integer
countEvens [] = 0
countEvens (i:is)
	   | even i = 1 + (countEvens is)
	   | otherwise = (countEvens is) 


-- This function takes the average of all the x and y coords and returns that point -- 
centroid :: [(Double,Double)] -> (Double,Double)
centroid [] = (0,0)
centroid numbers = 
	 let 
	     len = length numbers
	     x = [fst x | x <- numbers]
	     y = [snd y | y <- numbers] 
	     sumX = sum x
	     sumY = sum y
	     newlen = fromIntegral len
	     avgX = sumX / newlen
	     avgY = sumY / newlen 	 
	  in 
   	     (avgX, avgY)


-- This function will return the number of hailstones for each certain input by counting the calls --
hailstone :: Integer -> Integer
hailstone 1 = 1
hailstone n 
	  | even n =  hailstone(div n 2) + 1
	  | otherwise  = hailstone((3*n) + 1) + 1



