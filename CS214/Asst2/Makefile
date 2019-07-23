all: fileCompressor.c minheap.o avltree.o
	gcc fileCompressor.c -o fileCompressor minheap.o avltree.o

minheap.o: minheap.c
	gcc -c minheap.c

avltree.o: avltree.c
	gcc -c avltree.c

clean:
	rm fileCompressor; rm avltree.o; rm minheap.o
