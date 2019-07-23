module Project3 where

data RE a
    = Symbol a
    | Empty
    | RE a :+: RE a
    | RE a :|: RE a
    | Repeat (RE a)
    | Plus (RE a)
    deriving (Show, Read, Eq, Ord)

data ABC = A | B | C deriving (Show, Read, Eq, Ord)


--------------------------------------------------------------
--Part I: Creating Regular Expressions
--------------------------------------------------------------

-- The language for atMost2As includes exactly those strings in which A occurs
-- no more than twice
atMost2As :: RE ABC
atMost2As = ( (Symbol A :|: Empty) :+: (Repeat (Symbol B) :+: Repeat (Symbol C)) :+: (Symbol A :|: Empty) :+: (Repeat (Symbol B) :+: Repeat (Symbol C)) )  


-- anyOf alts returns a regular expression that matches any of the symbols
-- given in alts (assuming alts is non-empty)
anyOf :: [a] -> RE a
anyOf (x:[]) = Symbol x
anyOf (x:xs) = Symbol x :|: anyOf xs 


----------
--helper--
----------
create :: Int -> RE a -> RE a
create a r 
       | a == 0 = Empty 
       | a == 1 = r 
       | a > 1 = r :+: create (a-1) r

-- repeats m n r returns a regular expression which matches r at least m but no
-- more than n times (assuming m <= n)
repeats :: Int -> Int -> RE a -> RE a
repeats m n r 
	| m < n = (create m r) :|: repeats (m+1) n r
	| m == n = create m r  
		      



--------------------------------------------------------------
--Part II: Analyzing Regular Expressions
--------------------------------------------------------------

-- matchEmpty r indicates whether r will accept the empty string
matchEmpty :: RE a -> Bool
matchEmpty r = case r of Empty -> True
	       	      	 Symbol _ -> False
			 Repeat (_) -> True 
			 Plus (r1) -> matchEmpty(r1)
			 r1 :|: Empty -> True
			 r1 :|: r2 -> matchEmpty(r1) || matchEmpty(r2)
			 r1 :+: r2 -> matchEmpty(r1) && matchEmpty(r2)	
			 

-- minLength r returns the minimum length of any string that matches r
minLength :: RE a -> Integer
minLength r = case r of Empty -> 0
	      	     	Symbol a -> 1
			Repeat (_) -> 0
			Plus (r1) -> minLength(r1)
			r1 :|: Empty -> 0
			r1 :|: r2 -> minLength (r1) + minLength(r2)  
                        r1 :+: r2 -> minLength (r1) + minLength(r2)
			

-- maxLength r returns Just n if the maximum length of any string matched by
-- r is n, or Nothing if there is no upper limit
maxLength :: RE a -> Maybe Integer
maxLength r = case r of Empty -> Just 0
	      	     	Symbol a -> Just 1  
			Repeat (Empty) -> Just 0
			Repeat (_) -> Nothing
			Plus (r1) -> Nothing
			r1 :|: Empty -> Just 1
			r1 :|: r2 -> fmap (+1) (maxLength (r1)) 
			r1 :+: r2 -> fmap (+1) (maxLength (r1))


-- firsts r returns a list containing every symbol that occurs first in some
-- string that matches r
firsts :: RE a -> [a]
firsts r = case r of Empty -> []
       	   	     Symbol a -> [a]
		     Repeat(r1) -> firsts(r1)
		     Plus (r1) -> firsts(r1)
		     
		     Symbol a :|: Empty   -> [a]
		     Symbol a :+: Symbol b -> [a]
		     (Symbol a :|: Empty) :+: Symbol b ->  [a,b]
		     (Symbol a :|: Empty) :+: (Symbol b :|: Empty) -> [a,b]
		    	     
		     r1 :|: r2 -> firsts(r1) ++ firsts(r2)
		     r1 :+: r2 -> firsts(r1)
		    