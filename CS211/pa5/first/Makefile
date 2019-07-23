all: test

test: first.c
	gcc -g -Wall -Werror -fsanitize=address -std=c99 first.c -o first -lm
clean: 
	rm -rf first
