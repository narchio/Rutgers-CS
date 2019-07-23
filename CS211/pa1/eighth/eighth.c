#include<stdio.h>
#include<stdlib.h>

struct node{
  int val; 
  int height; 
  struct node* left; 
  struct node* right; 
};

// returns the height of the inserted node
int insertanode(struct node* root, int val) {

  int height = 1; 
  int newheight = 0; 
  struct node* ptr = root; 
  struct node* parent = NULL; 
  
   // check if it is the first node (the root)
  if (root == NULL || root->height == 0) {
    // root = (struct node*) malloc(sizeof(struct node)); 
    root->val = val; 
    root->height = height; 
    root->left = NULL; 
    root->right = NULL; 
    return height; 
  
  } else {
 
    // allocate 
    // root = (struct node*) malloc(sizeof(struct node)); 
    
    /* 
  if (ptr == NULL) {
    ptr->val = val; 
    root = ptr; 
    height = height + 1; 
    return height; 

  } else {
    */

    height = 1;  
    // ensure parent starts at the correct position
    parent = ptr; 
    // create our node to be inserted
    struct node* insertnode; 
    insertnode = (struct node*) malloc(sizeof(struct node)); 
    // initialize it
    insertnode->left = NULL; 
    insertnode->right = NULL; 
    insertnode->height = 0; 
    insertnode->val = val; 

    // loop through the tree to see where to be inserted
    while (ptr != NULL) {
      // duplicate check 
      if (ptr->val == insertnode->val) {
	return 0; 
      } else if (ptr->val > insertnode->val) {
	parent = ptr; 
	ptr = ptr->left; 
	height = height + 1;
      } else if (ptr->val < insertnode->val) {
	parent = ptr; 
	ptr = ptr->right; 
	height = height + 1; 
      }
    } // end of while
      insertnode->height = height; 
      // duplicate check
      if (parent->val == insertnode->val) {
	return 0; 
      } // if root is greater than insertnode, add to left
      else if (parent->val > insertnode->val) {
	parent->left = insertnode; 
	newheight = insertnode->height; 
	return newheight; 
      } // if root is less than insertnode, add to right 
      else if (parent->val < insertnode->val) {
	parent->right = insertnode; 
	newheight = insertnode->height; 
	return newheight; 
      }
  } // end of else
  return 0; 
}



// search for a node
int searchforanode(struct node* root, int val) {
  // int height = 1; 
  int height = 0; 
  struct node* ptr = root; 
    // loop through entirity of tree
    while (ptr != NULL) {
      // if it is greater than target, go left (smaller)
      if (ptr->val > val) {
	height = height+1; 
	ptr = ptr->left; 
      } // if it is less than target, go right (larger)
      else if (ptr->val < val) { 
	height = height+1; 
	ptr = ptr->right; 
      } // if target is found
      else if (ptr->val == val) {
	height = height+1;  
	return height; 
      }
    }
  // if target is not found, return 0 (absent)
  return 0; 

}

int main(int argc, char** argv){

  FILE *fp; 
  fp = fopen(argv[1], "r"); 
  
  // error check
  if (fp == NULL) {
    printf("error"); 
    return 0; 
  }
  // for file
  int val; 
  char op; 

  // for functions
  int insert; 
  int search; 

  // root, define and allocate
  struct node* root = NULL; 
  root = (struct node*) malloc(sizeof(struct node)); 
  
  //struct node* parent = (struct node*) malloc(sizeof(struct node)); 
  //parent = NULL; 

  // loop through file until it is empty (as long as there are 2 arguments
  while (fscanf(fp, "%c\t %d\n", &op, &val) == 2) {

    // check for insert
    if (op == 'i') {
      //insert and look for the result
      insert = insertanode(root, val); 
      //insertanode(root, val);
      
      // if duplicate
      if (insert == 0) {
	printf("duplicate\n"); 
      } // else if not a duplicate
      else if (insert != 0) {
	printf("inserted %d\n", insert); 
      }
      
    } else if (op == 's') {
      // preform a search
      search = searchforanode(root, val); 

      // check if absent
      if (search == 0) {
	printf("absent\n"); 
      } // if present
      else if (search != 0) {
	printf("present %d\n", search); 
      }
    } else if (op != 'i' || op != 's') {
      // bad input
      printf("error\n"); 
    }
  } // end of while 
  return 0; 
}
