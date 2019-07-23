#include<stdio.h>
#include<stdlib.h>

struct node {
  int val; 
  struct node *next; 
}; 

// SIZE of LL (easiest so I did it first) 
int sizeLL(struct node* head) {
  struct node* ptr = head;
  int size = 0; 
  if (head == NULL) {
    return 0; 
  }
  for (ptr = head; ptr != NULL; ptr = ptr->next) {
    size = size+1; 
  }
  return size; 
}

// INSERTS the node to the front of the list (similar to what we did in class, however I did not want to do a void method because I find it easier to return a value when I implement the code in the main method
struct node* insertNode(struct node* head, int val) {
  struct node* insertNode = NULL;
  insertNode = (struct node*) malloc(sizeof(struct node)); 
  
  // now populate the inserted node with values and insert to front of list
  insertNode->val = val; 
  insertNode->next = head; 
  
  // now return the new head
  return insertNode; 
}
// DELETES  the node at val
struct node* deleteNode(struct node* head, int val) {
  // if the head is null to start
  if (head == NULL) {
    return head; 
  }
  
  // if it is the first element
  if (head->val == val) {
    if (head->next == NULL) {
      head = head->next; 
      return head; // which will be NULL, here
      }
      head = head->next; 
  } 
  
  struct node* ptr = head->next; 
  struct node* prev = head;

  // if it is not the first element
  for (ptr = head->next; ptr != NULL; ptr = ptr->next) {
    // if the pointer is equal to the value that is called to be deleted
    if (ptr->val == val) {
      // set the previous' next to the pointer's next, thus severing its
      // connection to the LL
      prev->next = ptr->next; 
    }
    // increment prev, and ptr will as well
    prev = ptr;
  }
  return head; 
}

// SORTs the linked list
struct node* sort(struct node* head) {
  // ptr will traverse list
  // prev will stay 1 node behind ptr
  //headptr will stay on the head or the value that needs to be sorted into the LL
  struct node* ptr = head->next;
  struct node* prev = head; 
  struct node* headptr = head; 

  // begin looping through LL
  for (headptr = head; headptr != NULL; headptr = headptr->next) {
    // bringing the pointers back to the start to check for any outliers
    ptr = headptr->next; 
    prev = headptr; 
    for (ptr = headptr->next; ptr != NULL; ptr = ptr->next) {
      if (prev->val > ptr->val) {
	// now, give the smaller value to previous
	prev = ptr; 
      } // else we continue and ptr is incremented 
    }
    // make the value of the head equal to the value of the headptr and swap
    int headval = headptr->val; 
    headptr->val = prev->val; 
    prev->val = headval; 
  }
  // return head; 
  return head; 
}


// will take care of the DUPLICATEs by deleting if there is one
struct node* isDuplicateNode(struct node* head) {
  struct node* ptr = head->next; 
  struct node* headptr = NULL;  
  struct node* prev = head; 

  for (headptr = head; headptr != NULL; headptr = headptr->next) {
    // keep ptr 1 in front of headptr
    ptr = headptr->next; 
    prev = headptr; 
    while (ptr != NULL) {
      if (headptr->val == ptr->val) {
	// move the previous' next to the pointer's next to eliminate the duplicate
	prev->next = ptr->next;
	  }
      //Now, simply increment the pointers 
      prev = ptr; 
      ptr = ptr->next; 
    } 
  }
  // the list will not be sorted correct, so will have to call the sort method
  return head; 
}

int main(int argc, char** argv) {
  struct node* head = NULL;

  FILE *fp; 
  int value; 
  char op; 
  fp = fopen(argv[1],"r"); 
  
  // create the ptr for future reference 
  struct node* ptr = NULL; 

  if (fp == NULL) {
    printf("error"); 
    return 0; 
  } 
  while (fscanf(fp,"%c\t %d\n", &op, &value) == 2) {
    if (op == 'i') {
      // insert the node, then check for duplicate 
      head = insertNode(head, value);
      head = isDuplicateNode(head);  
    } else if (op == 'd') {
      // delete the node
      head = deleteNode(head, value); 
    }
  }
  int size = 0; 
  ptr = head; 
  size = sizeLL(ptr); 
  if (size == 0) {
    printf("%d", size); 
    return 0; 
  }
  // ensure the final product has no duplicates
  head = isDuplicateNode(head);
  
  // ensure it has no duplicates
  head = sort(head); 

  // close the file down
  fclose(fp); 
  
  // find size
  //int size;   
  ptr = head; 
  size = sizeLL(ptr); 
  // the file exists, but is empty
  if (size == 0) {
    // still have to print size like in the test case
    printf("%d", size); 
    // and then return to finish program
    return 0; 
  }
  
  // print size
  printf("%d\n", size); 

  // print LL 
  for (ptr = head; ptr->next != NULL; ptr = ptr->next) {
    printf("%d\t", ptr->val); 
  }
  // print last node
  printf("%d", ptr->val); 
  
  return 0; 
}
