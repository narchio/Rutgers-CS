all: test

test: second.c
	gcc -g -Wall -Werror -fsanitize=address -std=c99 second.c -o second
clean: 
	rm -rf second
