all: test

test: ninth.c
	gcc -g -Wall -Werror -fsanitize=address -std=c99 ninth.c -o ninth
clean: 
	rm -rf ninth
