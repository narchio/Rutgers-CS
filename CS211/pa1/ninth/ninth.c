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

// Used same methods/functions from Eighth because they follow same logic
/*
 Now, here is the delete method, this method takes in the root of the tree and 
the integer value that is attempting to be deleted. The method will return a T if it can be deleted
and is deleted successfully and a F if it is a failure to delete (if it isn't in the tree

*/


// need a minright to complete the delete correctly
struct node* minright(struct node* ptr) {
  struct node* temp; 
  temp = ptr; 
  
  while (temp->left != NULL) {
    temp = temp->left; 
  }
  return temp; 
}


struct node* deleteanode(struct node* root, int val) {
  
  // check the list before 
  int search = searchforanode(root, val);
  if (search == 0) {
    printf("fail\n"); 
    return root; 
  }

  
  if (root == NULL) {
    return root; 
  }

  // if it is greater than, go down the right subtree
  if (val > root->val) {
    root->right = deleteanode(root->right, val); 
  } // if it is less than, go down the right subtree
  else if (val < root->val) {
    root->left = deleteanode(root->left, val); 
  } else {
    
      struct node* temp; 
    
    // check left child, if NULL delete 
    if (root->left == NULL) {
      temp = root->right; 
      printf("success\n"); 
      free(root); 
      return temp; 
    } // check right child, if NULL delete
    else if (root->right == NULL) {
      temp = root->left; 
      printf("success\n"); 
      free(root); 
      return temp; 
    } 
    // if 2 children
    // find the minimum on the right child of them
    temp = minright(root->right); 
    // update the value 
    root->val = temp->val; 
    // now delete on the right child with the new value
    root->right = deleteanode(root->right, temp->val); 

  }

  return root; 

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
      /*
      if (root == NULL) {
	root->height = 0; 
      }
      */

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
    } else if (op == 'd') {
      root = deleteanode(root,val); 
      if (root == NULL)
          root = malloc(sizeof(struct node));
      /*
      // search before deletion occurs
      search = searchforanode(root, val); 
      
      // if absent, then deletion cannot occur
      if (search == 0) {
	printf("fail\n"); 
      } 
      */
      // delete the node
      //root = deleteanode(root, parent, val); 
      //root = deleteanode(root, val); 
      
      // check if deletion occured
      //search = searchforanode(root, val); 
      
      // absent so the deletion occured successfully
      // if (search == 0) {
      //	printf("success\n"); 
      // }
    } else if (op != 'd' || op != 's' || op != 'i') {
      // bad input
      printf("error\n"); 
    }
  } // end of while 
  return 0; 
}
