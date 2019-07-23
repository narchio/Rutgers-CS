#ifndef _mymalloc_h
#define _mymalloc_h

#include <stdio.h> 
#include <stdlib.h> 

//defining memory
static char memory[4096]; 

//replacing calls
#define malloc( x ) mymalloc(x, __FILE__, __LINE__)
#define free( x ) myfree(x, __FILE__, __LINE__)

//metadata structure
typedef struct _MetaData MetaData; 
struct _MetaData{
	unsigned short allocationSize; 
	unsigned short magicNumber; 
	char inUse; 
	MetaData* next; 
};

//function signatures 
void* mymalloc(size_t requestSize, char* file, int line); 
void myfree(void* ptr, char* file, int line);

#endif
