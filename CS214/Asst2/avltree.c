#include "avltree.h"

//creates an avl tree node
AVLNode* create_avl_node(char* token){
    AVLNode* node = (AVLNode*) malloc(1*sizeof(AVLNode)); 
    if(!node){
        printf("Unable to allocate enough memory to create avl tree node!\n"); 
        return NULL;
    }
    node->token = (char*) malloc((strlen(token)+1)*sizeof(char)); 
    if(!(node->token)){
        printf("Unable to allocate enough memory to store token in avl tree node!\n"); 
        return NULL;
    }
    strcpy(node->token, token); 
    node->freq = 1;
    node->height = 0;
    node->left = NULL; 
    node->right = NULL; 
    return node;
}

//gets height of an avl tree node 
int get_avl_node_height(AVLNode* node){
    if(!node){
        return -1; 
    }
    int left = get_avl_node_height(node->left); 
    int right = get_avl_node_height(node->right); 
    return left > right ? left + 1 : right + 1; 
}

//updates the heights of avl tree going towards leaf nodes starting from argument 
void update_avl_tree_heights(AVLNode** root){
    (*root)->height = get_avl_node_height((*root)); 
    if((*root)->right){
        update_avl_tree_heights((&(*root)->right)); 
    }
    if((*root)->left){
        update_avl_tree_heights((&(*root)->left)); 
    }
}

//inserts avl node into tree accordingly
AVLNode* insert_avl_node(AVLNode* root, char* token){
    if(!root){
        return create_avl_node(token); 
    }
    int cmp = strcmp(root->token, token); 
    if(cmp == 0){
        root->freq += 1; 
        return root; 
    }
    else if(cmp < 0){
        root->right = insert_avl_node(root->right, token); 
    }
    else{
        root->left = insert_avl_node(root->left, token); 
    }
    root->height = get_avl_node_height(root); 
    balance_avl_node(&root);
    return root;
}

//right rotation
AVLNode* rotate_right(AVLNode* node){
    AVLNode* temp = node->left; 
    node->left = temp->right; 
    temp->right = node; 
    update_avl_tree_heights(&temp); 
    return temp; 
}

//left rotation
AVLNode* rotate_left(AVLNode* node){
    AVLNode* temp = node->right; 
    node->right = temp->left; 
    temp->left = node; 
    update_avl_tree_heights(&temp); 
    return temp; 
}

//gets balance factor of an avl node
int get_avl_node_bf(AVLNode* node){
    int left = get_avl_node_height(node->left) + 1; 
    int right = get_avl_node_height(node->right) + 1; 
    return left - right; 
}

//balances a given avl node 
void balance_avl_node(AVLNode** node){
    if(get_avl_node_bf((*node)) > 1){
        if(get_avl_node_bf((*node)->left) < 0){
            (*node)->left = rotate_left((*node)->left); 
        }
        (*node) = rotate_right((*node)); 
    }
    else if(get_avl_node_bf((*node)) < -1){
        if(get_avl_node_bf((*node)->right) > 0){
            (*node)->right = rotate_right((*node)->right);
        }
        (*node) = rotate_left((*node)); 
    }
}

//counts the number of nodes in the avl tree
int get_avl_size(AVLNode* root){
    if(!root){
        return 0;
    }
    return 1 + get_avl_size(root->left) + get_avl_size(root->right);
}
