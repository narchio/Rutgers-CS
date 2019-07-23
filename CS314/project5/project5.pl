:- use_module(library(clpfd)).

% tree(+Tree)  - succeeds when Tree is a binary tree
tree(tip).
tree(bin(Left,Num,Right)) :- tree(Left), tree(Right).


% symmetric(+Tree) - succeeds when Tree is a symmetric binary tree
issym(tip, tip). 
issym(bin(_,A,_),bin(_,B,_)) :- (A = B). 
symmetric(tip). 
symmetric(bin(Left, Num, Right)) :- issym(Left, Right), symmetric(Left) , symmetric(Right).  


% height(+Tree, ?Height) - succeeds when Tree is a binary tree with height
height(tip, X) :- (X #< 0), false. 
height(tip,0).
height(tip,_,tip). 
height(bin(Left, Num, Right), Height) :- height(Left, LeftHeight), height(Right, RightHeight), 
    Height #= max(LeftHeight, RightHeight) + 1. 


% perfect(?Tree, ?Height) - succeeds when Tree is a perfect binary tree with
% height Height

perfect(tip, Height) :- (Height #< 0), false. 
perfect(tip, 0). 
perfect(bin(L, _, R), H) :- 
		   H #> 0,
		   Curr #= H - 1, 
		   perfect(L, Curr), 
		   perfect(R, Curr). 

  
 
% sorted(+List) - succeeds when List is a list of integers in non-decreasing
% order
sorted([]).
sorted([_|[]]). 
sorted([L1|[]]). 
sorted([L1,L2|Ls]) :- (L1 =< L2), sorted([L2|Ls]). 


% sselect(?Item, ?List, ?ListWithItem) - succeeds when List and ListWithItem
% are sorted lists of intgers, and ListWithItem is the result of inserting
% Item into List

sselect(Item, [], [Item]). 
sselect(Item, [L1|[]], [Item, L1|[]]) :- (Item =< L1). 
sselect(Item, [L1|[]], [L1, Item|[]]) :- (Item > L1).
sselect(Item, [L1, L2|Ls], [L1, Item, L2|Ls]) :- (Item =< L2), (Item >= L1). 
sselect(Item, [L1, L2|Ls], [Item, L1, L2|Ls]) :- (Item =< L1). 
sselect(Item, [L1, L2|Ls], List2) :- (Item => L2), (Item => L1), sselect(Item, [L2|Ls], List2). 
