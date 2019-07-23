//First.c pointers
#include<stdio.h>
#include<stdlib.h>

void swap(int *array){ //swaps variables (ptrs) using a temp 
  int temp = *array; 
  *array = *(array+1); 
  *array = temp; 
  //return; 
}

int main(int argc, char** argv) {

  FILE *fp; 
  int size = 0;
  //int b = 0;
  int i = 0; 
 
  // take the first argument or the title of the test file
  fp = fopen(argv[1], "r"); 

  //null check/empty check
  if (fp == NULL) {
    return 0; 
  }

// Scan the test file to determine size of array
  fscanf(fp, "%d\n", &size); 
  //printf("The size is: %d\n", size); 
   //  here is the array of correct size, put the numnbers from the file into an array
  int *array;
  array = (int *) malloc(sizeof(int) * size);
  int *result; 
  result = (int *) malloc(sizeof(int) * size); 
  
  //fp = fopen(argv[1], "r"); 
  
  // load array
  for (int l = 0; l < size; l++) {
    fscanf(fp,"%d\t", &array[l]); 
    // printf("%d\t", array[l]); 
  }
  
  // now sort array
  for (i = 0; i < size; i++) {
    for (int j = 0; j < size; j++) {
      if (array[i] < array[j]) { 
	int temp = array[i]; 
	array[i] = array[j]; 
	array[j] = temp;  
      }
    }
  }

  // first, load even in 
  int ev = 0;
  for (i = 0; i < size; i++) {
    if (array[i] % 2 == 0) { 
      int temp = array[i]; 
      array[i] = result[ev]; 
      result[ev] = temp; 
      ev = ev+1; 
    } else if (array[i] == 0) {
      int temp = array[i]; 
      array[i] = result[ev]; 
      result[ev] = temp; 
      ev = ev+1;
    }
  }
  int od = ev; 
  // now load odd in
  for (i = 0; i < size; i++) {
    if (array[i] % 2 != 0) {
      int temp = array[i]; 
      array[i] = result[od]; 
      result[od] = temp; 
      od = od+1;
    }
  }

  /* // FOR TESTING PURPOSES
  // here is the sorted array up until the last element (we need tabs btw elements)
  printf("Array result sorted: \n"); 
  for (i = 0; i < size-1; i++) {
    printf("%d\t", array[i]); 
  }

  // now print the last number 
  printf("%d", array[size-1]); 
  */

  // here is the result evenly sorted array
  //printf("Array result sorted: \n"); 
  for (i = 0; i < size-1; i++) {
    printf("%d\t", result[i]); 
  }

  // now print the last number 
  printf("%d", result[size-1]); 

  //free(array); 
	   
  
  return 0; 
}
