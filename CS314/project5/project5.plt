:- include(project5).

:- begin_tests(tree).

test(1, [true(X =@= _)]) :- tree(bin(tip,X,tip)).
test(2, [fail]) :- tree(bin(tip,1,2)).
test(3) :- tree(bin(bin(tip,1,tip),0,bin(tip,not_an_integer,tip))).

:- end_tests(tree).

:- begin_tests(symmetric).

test(1, [fail]) :- symmetric(17).
test(2) :- symmetric(tip).
test(3) :- symmetric(bin(bin(tip,1,tip), 0, bin(tip,1,tip))).
test(4, [true(X-Y =@= atom-_)]) :-
	symmetric(bin(bin(tip,X,tip), Y, bin(tip,atom,tip))).
test(5, [true(X-Y =@= Z-_)]) :-
	symmetric(bin(bin(tip,X,tip), Y, bin(tip,Z,tip))).

:- end_tests(symmetric).

:- begin_tests(height).

test(1) :- height(bin(tip,_,tip), 1).
test(2, [true(N == 2)]) :-
	height(bin(bin(tip, _, tip), _, tip), N).

:- end_tests(height).

:- begin_tests(perfect).

test('++ 1', [true(X =@= _)]) :- perfect(bin(tip,X,tip), 1).
test('++ 2', [fail]) :- perfect(tip, 1).

test('+- 1', [true(X-N =@= _-1)]) :-
	 perfect(bin(tip,X,tip), N).

test('+- 2', [fail]) :-
	 perfect(bin(bin(tip,_,tip),_,tip), _).

test('-+ 1', [all(T == [tip])]) :- perfect(T, 0).

test('-+ 2', [fail]) :- perfect(T, 1), T \=@= bin(tip,_,tip).

test('-- 1', [nondet, true(N == 5)]) :-
	 findnsols(5, T-N, perfect(T, N), Sols),
	 length(Sols, N).

test('-- 2', [nondet, true(N-Bad == 5-[])]) :-
	 findnsols(5, L-X-R-H, perfect(bin(L,X,R), H), Sols),
	 length(Sols, N),
	 exclude(check_perfect, Sols, Bad).

check_perfect(L-X-R-N) :-
		       X =@= _,
		       M #= N - 1,
		       perfect(L, M),
		       perfect(R, M).

:- end_tests(perfect).

:- begin_tests(sorted).

test(1) :- sorted([1,2,3]).
test(2) :- sorted([1,1,1]).
test(3, [fail]) :- sorted([1,2,1]).

:- end_tests(sorted).

:- begin_tests(sselect).

test('1.1') :- sselect(2, [3], [2,3]).
test('1.2', [fail]) :- sselect(2, [3], [3,2]).
test('2.1', [true(L == [1,2,3])]) :-
	    sselect(2, [1,3], L).
test('3.1', [true(L == [1,3])]) :-
	    sselect(2, L, [1,2,3]).
test('4.1', [true(X == 2)]) :- sselect(X, [1,3], [1,2,3]).
test('4.2', [fail]) :- sselect(_, [1,3], [1,2,3,4]).

:- end_tests(sselect).