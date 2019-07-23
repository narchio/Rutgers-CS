#ifndef _avltree_h 
#define _avltree_h 

#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 

typedef struct _AVLNode AVLNode; 
struct _AVLNode{
    char* token; 
    int freq; 
    int height; 
    AVLNode *left, *right; 
};

AVLNode* create_avl_node(char* token);
AVLNode* insert_avl_node(AVLNode* root, char* token);
AVLNode* rotate_right(AVLNode* node);
AVLNode* rotate_left(AVLNode* node);

int get_avl_node_height(AVLNode* node);
int get_avl_node_bf(AVLNode* node);
int get_avl_size(AVLNode* root);

void update_avl_tree_heights(AVLNode** root);
void balance_avl_node(AVLNode** node);

#endif