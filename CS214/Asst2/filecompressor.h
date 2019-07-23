#ifndef _filecompressor_h
#define _filecompressor_h

#include "avltree.h"
#include "minheap.h"

char* create_path(char* currentPath, char* name);
AVLNode* tokenize_file(char* filename, AVLNode* root);
hnode** avl_to_minheap(AVLNode* root, hnode** minheap, int* index);
void print_tree(AVLNode* root);
void print_heap(hnode** heap, int size);
void avl_to_array(AVLNode* root, AVLNode*** arr, int* index);
void array_to_minheap(AVLNode** arr, hnode*** heap, int avlSize);
void build_codebook(AVLNode* root, char* path, int r);
void recursive_index_files(char* path, AVLNode** freqs, int recur);
void parse_original_file(char* filename, bnode** hcbarray);
void compress_file(char* codebookFilename, char* filename);
void decompress_file(char* codebook, char* compressedFile);
void recursive_compress(char* path, char* codebook);
void recursive_decompress(char* path, char* codebook);







#endif
