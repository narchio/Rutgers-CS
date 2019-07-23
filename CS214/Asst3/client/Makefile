all: WTF.c clientfunctions.o 
	gcc -lcrypto WTF.c -o WTF clientfunctions.o 

clientfunctions.o: clientfunctions.c 
	gcc -c clientfunctions.c 

clean: 
	rm WTF; rm clientfunctions.o; rm .configure