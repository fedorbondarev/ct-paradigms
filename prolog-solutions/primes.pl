divided(N, M) :- 0 =:= mod(N, M).

%find_first([H | T], P, R) :- G =.. [P, R], !.
%find_first([H | T], P, R) :- find_first(T, P, R).

find_first_divisor_from([H | T], N, R) :- divided(N, H), !, R = H.
find_first_divisor_from([H | T], N, R) :- find_first_divisor_from(T, N, R).

smallest_divisor_in_range(S, E, N, S) :-
	S < E,
	divided(N, S).
smallest_divisor_in_range(S, E, N, R) :-
	S < E,
	S1 is S + 1,
	smallest_divisor_in_range(S1, E, N, R).

range(N, N, []).
range(N, M, [HR | TR]) :- N < M, N1 is N + 1, HR = N, range(N1, M, TR).

is_sqrt(N, R) :- N >= 0, R is round(sqrt(N)).

smallest_prime_divisor(N, R) :-
	\+ var(N), NRT1 is round(sqrt(N)) + 1,
	smallest_divisor_in_range(2, NRT1, N, R), !.
smallest_prime_divisor(N, N).

prime_divisors(1, []) :- !.
prime_divisors(N, [HR | TR]) :- 
	smallest_prime_divisor(N, HR), N1 is N / HR, prime_divisors(N1, TR).

prime(N) :- \+ composite(N).

composite(N) :-
	NRT1 is round(sqrt(N)) + 1,
	smallest_divisor_in_range(2, NRT1, N, _).
