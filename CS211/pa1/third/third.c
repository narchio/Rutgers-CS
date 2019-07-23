#include<stdio.h>
#include<stdlib.h>

// nodes of chained LL in HT
struct node{
  int val; 
  struct node* next; 
}; 

// the HT
struct hashtable{
  int size; 
  struct node** row; 
};

// Creates new hashtable 
struct hashtable* create(int size) {
  //initialize hashtable 
  struct hashtable* master = (struct hashtable*) malloc (sizeof(struct hashtable)); 
  master->size = size; 
  master->row = (struct node**) malloc(sizeof(struct node*)*size); 
 
  // now initialize each entry to NULL
  for (int i = 0; i<size; i++) {
    master->row[i] = NULL; // NULL @ default
  }
  return master; 
}

// populates the node with a value 
struct node* populateNode(struct node* root, int val) {
  struct node* new = (struct node*) malloc (sizeof(struct node)); 
  new->val = val; 
  new->next = root;
  // adds to the front so new front is new variable, so return new
  return new; 
}


// inserts the new node into the hashtable.. either T is there or F is not 
char insert(struct hashtable* master, struct node* insertnode) {
  // our key for the  hashfunction
  int key; 
  
  // hashfunction
  if (insertnode->val >= 0) {
    key = insertnode->val % master->size; 
  } else if (insertnode->val < 0) {
    // multiply by negative one so that there is no index out of bounds exception
    key = ((-1)* insertnode->val) % master->size; 
  }

  // Now check the master->row[key] to see if there are values or not
  if (master->row[key] == NULL) {
    master->row[key] = insertnode; // it is a blank bucket, so insert the node 
    return 'T'; 
  } else if (master->row[key]->val == insertnode->val) {
    // if the bucket equals the inserting node, then it is a duplicate
    return 'F'; 
  }
  
  struct node* ptr = NULL; 
  struct node* prev = NULL; 

  // itterate through the bucket that does contain a value
  for (ptr = master->row[key]; ptr != NULL; ptr = ptr->next) {
    // if it is a duplicate
    if (ptr->val == insertnode->val) {
      return 'F'; 
    }
    prev = ptr; 
  }
  // Now, since it has passed all the checks and is at the end of the bucket,
  // add the insertnode to the back of the LL
  prev->next = insertnode; 
  return 'T'; 
}

// searches for the selected target in the hashtable.. either T, is there or F is not 
char search(struct hashtable* master, int target) {
  int key; 
  if (target >= 0) {
    key = target % master->size; 
  } else if (target < 0) {
    key = (target * (-1)) % master->size; 
  }
  // if absent from the table
  if (master->row[key] == NULL) {
    return 'F'; 
  } // if the target is present and the buckets are empty 
  else if (master->row[key]->val == target) {
    return 'T'; 
  }

  // create a pointer to traverse the chained buckets
  struct node* ptr; 
  for(ptr = master->row[key]; ptr != NULL; ptr = ptr->next) {
    // if ptr->val is ever equal to the target, it is present
    if (ptr->val == target) {
      return 'T'; 
    }
  }
  // if it passes all the checks and is still not found, then return false as it is absent 
  return 'F'; 
}

int main(int argc, char** argv) {
  // create the master hashtable 
  struct hashtable* table = create(10000); 
  
  FILE *fp; 
  fp = fopen(argv[1], "r"); 

  if(fp == NULL) {
    printf("error\n"); 
    return 0; 
  }
  // variables and pointers that will be used within the while loop
  char op; 
  int value; 
  char ins; 
  char sear; 
  struct node* headptr = NULL; 
  // struct node* headptr = (struct node*) malloc(sizeof(struct node)); 
  
  while (fscanf(fp, "%c\t%d\n", &op, &value) == 2) {
    
    // if it is an insert
    if (op == 'i') { 
      //start headptr at NULL each time so we can create a new node each time there is an i
      headptr = NULL; 
      headptr = populateNode(headptr, value); 
      ins = insert(table, headptr); 
      // now check for the outcome of the insert
      if (ins == 'T') {
	printf("inserted\n"); 
      } else if (ins == 'F') {
	printf("duplicate\n"); 
      }
    } // if it is a search
    else if (op == 's') {
      sear = search(table, value); 
      // now check outcome of the search
      if (sear == 'T') {
	printf("present\n"); 
      } else if (sear == 'F') {
	printf("absent\n"); 
      }
    }
  }
  // return because it is an int main method
  return 0; 
}
