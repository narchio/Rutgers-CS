all: test

test: fourth.c
	gcc -g -Wall -Werror -fsanitize=address -std=c99 fourth.c -o fourth
clean: 
	rm -rf fourth
