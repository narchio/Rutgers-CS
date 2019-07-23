#ifndef _minheap_h
#define _minheap_h

#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 
#include <ctype.h>
#include <dirent.h>
#include <unistd.h>
#include <string.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>

typedef struct bnode_ bnode; 
struct bnode_{
  int data;
  char* word;
  char* huffmanCode;
  bnode* left, *right;
}; 

typedef struct hnode_{
    int data;
    char* word;
    bnode* bnode; 
}hnode;

int minheapsize; 
int hcbarraysize;

void siftUp (hnode*** array); 
void siftDown (hnode*** array); 
void calculateHuffmanCode (bnode* root, int* codes, int index); 
void createCodebook(bnode* root, char* codebook); 

hnode** deletehnode (hnode** array, int index); 
hnode** inserthnode (hnode** array, hnode* insertnode); 
hnode** initializeheap(int capacity);
hnode* createhnode(int freq, char* token);

//void huffToBnodeInsert (bnode* root, bnode* insertnode);
bnode* huffToBnodeInsert (bnode* root, bnode* insertnode);
bnode* insertbnode (hnode** hnode1, hnode** hnode2); 
bnode* createHuffmanTree (hnode** array); 
bnode** hcb_to_array(char* filename);
//bnode** hcb_to_array(char* filename, bnode** arr);

//bnode* traverseHuffTree (bnode* root, char* token);
bnode* traverseHuffTree (bnode* root, char* token); 
void encoder (bnode** root, char* token, char* filename); 
void decoder (bnode** root, char* file, char* filename);

char* get_encoding_from_token(char* token, bnode** arr);
char* get_token_from_encoding(char* encoding, bnode** arr);
char redo_control_codes(char* token);

#endif
