all: test

test: fifth.c
	gcc -g -Wall -Werror -fsanitize=address -std=c99 fifth.c -o fifth
clean: 
	rm -rf fifth
