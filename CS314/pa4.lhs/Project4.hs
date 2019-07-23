module Project4 where
-- Release 2

data DFA state symbol = DFA
    { alphabet :: [symbol]
    , states   :: [state]
    , initial  :: state
    , transit  :: state -> symbol -> state
    , accept   :: state -> Bool
    }

data ABC = A | B | C deriving (Show, Read, Eq, Ord)

-- a DFA that accepts all strings over the alphabet {A,B,C} containing at most
-- two A's
atMost2As :: DFA Int ABC
atMost2As = DFA 
	  { alphabet = [A,B,C]
	  , states = [1,2,3,4]
	  , initial = 1
	  , transit = delta
	  , accept = (/= 4)
	  }
	  where 
	  	delta 1 A = 2
	  	delta 1 _ = 1
	  	delta 2 A = 3
	  	delta 2 _ = 2
	  	delta 3 A = 4
	  	delta 3 _ = 3
	  	delta 4 _ = 4

-- a DFA that accepts all strings over the alphabet {A,B,C} containing and odd
-- number of A's
oddAs :: DFA Int ABC
oddAs =  DFA
          { alphabet = [A,B,C]
          , states = [1,2,3]
          , initial = 1
          , transit = delta
          , accept = (== 2)
          }
	  where
		delta 1 A = 2
		delta 1 _ = 1
		delta 2 A = 3
		delta 2 _ = 2
		delta 3 A = 2
		delta 3 _ = 3


-- a DFA that accepts all strings over the alphabet {A,B,C} containing the
-- sequence A,B,C
hasABC :: DFA Int ABC
hasABC =  DFA
          { alphabet = [A,B,C]
          , states = [1,2,3,4]
          , initial = 1
          , transit = delta
          , accept = (== 4)
          }
	  where 
	  	delta 1 A = 2
		delta 1 _ = 1
		delta 2 B = 3
		delta 2 A = 2
		delta 2 C = 1
		delta 3 C = 4
		delta 3 A = 2
		delta 3 B = 1
		delta 4 _ = 4

-- change this to True if you are attempting the extra credit
extra_credit = False

-- multiplex d1 d2 returns a DFA that reads a string of symbols intended for
-- either d1 or d2. The DFA accepts a string if d1 accepts the portion of the
-- string marked Left and d2 accepts the portion marked Right

multiplex :: DFA s1 a1 -> DFA s2 a2 -> DFA () (Either a1 a2)
-- This type is a placeholder. You will need to change the state type for the
-- return DFA to something else if you attempt this problem.

multiplex d1 d2 = undefined