all: WTFserver.c serverfunctions.o 
	gcc -lcrypto -pthread WTFserver.c -o WTFserver serverfunctions.o 

serverfunctions.o: serverfunctions.c 
	gcc -c serverfunctions.c 

clean: 
	rm WTFserver; rm serverfunctions.o