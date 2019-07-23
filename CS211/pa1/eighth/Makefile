all: test

test: eighth.c
	gcc -g -Wall -Werror -fsanitize=address -std=c99 eighth.c -o eighth
clean: 
	rm -rf eighth
