all: test

test: third.c
	gcc -g -Wall -Werror -fsanitize=address -std=c99 third.c -o third
clean: 
	rm -rf third
