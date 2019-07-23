#include<stdio.h>
#include<stdlib.h>

void printmatrix(int ** matrix, int rows, int cols) {
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      printf("%d\t", matrix[i][j]);
    }
    printf("\n"); 
  }
  return; 
}


// multiplies the two matrices and returns the resulting matrix, only works for valid matrices. 
// the rows will be the greatest rows from the two matrices in the main and the columns will be found in the same way in main
int** multiply(int** A, int** B, int** result, int rowsA, int colsA, int rowsB, int colsB) {
  int resultdata = 0; 
  
  // create result matrix
  //create resulting matrix (mxn and nxm, will yeild a mxm)
  //int resultMemAlloc = rows * cols; 
  result = (int**) malloc(rowsA * sizeof(int*)); 
  for (int i = 0; i < rowsA; i++) { 
    result[i] = (int*) malloc(colsB * sizeof(int)); // columns in result
  }
 
  int i; 
  int j; 
  
  // initialize resulting matrix to 0
  for (int i = 0; i < rowsA; i++) {
    for (int j = 0; j < colsB; j++) {
      result[i][j] = 0; 
    } 
  }
  
 // already initialized and filled so just multiply
  int n; 
  for (i = 0; i < rowsA; i++) { //rowsB-1
    for (j = 0; j < colsB; j++) { // colsB
      for (n = 0; n < rowsB; n++) {
	int a = A[i][n]; 
	int b = B[n][j]; 
	resultdata = resultdata + a*b;
	//printf("inner for a: %d, b: %d\n", a, b); 
	//	printf("resultdata is: %d\n", resultdata);
	//result[i][j] = result[i][j] + resultdata; 
	//	printf("result is: %d\n", result[i][j]); 
      } 
      result[i][j] = resultdata; 
      resultdata = 0; 
    }
  }

  return result; 
}

// main method
int main(int argc, char** argv) {
  FILE *fp; 
  fp = fopen(argv[1], "r"); 
 
  // INITIALIZE A 
  // scan file for rowsA and colsA
  int rowsA; 
  int colsA; 
  fscanf(fp, "%d\t %d\n", &rowsA, &colsA);  
  // now allocate for rows and columns
  int** A = (int**) malloc(rowsA * sizeof(int*)); // rows in A
  for (int i = 0; i < rowsA; i++) { 
    A[i] = (int*) malloc(colsA * sizeof(int)); // columns in A
  }
  // for to initialize A matrix
  int num;  
  // now initialize A 
  for (int i = 0; i < rowsA; i++) {
    for (int j = 0; j < colsA; j++) { ///////////////////updating from rowsA
      fscanf(fp, "%d\t", &num); // scan the line
      A[i][j] = num; // initialize 
    }
    // now go to the next line
    fscanf(fp, "\n"); 
  }

  //printmatrix(A, rowsA, colsA); 


  // INITIALIZE B 
  // scan file for rowsB and colsB 
  int rowsB; 
  int colsB; 
  fscanf(fp, "%d\t %d\n", &rowsB, &colsB); 

  // printf("colsA %d\n", colsA); 
  // printf("rowsB %d\n", rowsB); 
  
  // check for bad matrix 
  if (colsA != rowsB) {
    printf("bad-matrices"); 
    return 0; 
  }
 
  // now allocate for rows and columns
  int** B = (int**) malloc(rowsB * sizeof(int*)); // rows in B
  for (int i = 0; i < rowsB; i++) { 
    B[i] = (int*) malloc(colsB * sizeof(int)); // columns in B
  }

  // for to initialize B matrix
  int num2;  
  // now initialize B 
  for (int i = 0; i < rowsB; i++) {
    for (int j = 0; j < colsB; j++) {
      fscanf(fp, "%d\t", &num2); // scan the line
      B[i][j] = num2; // initialize 
    }
    // now go to the next line
    fscanf(fp, "\n"); 
  }

  // close scanner
  fclose(fp); 

  //printmatrix(B, rowsB, colsB); 
  
  // create a resulting matrix
  int ** result; 

  // relies on either colsA or rowsB so multiply and populate result
  for (int i = 0; i < colsA; i++) {
    result = multiply(A, B, result, rowsA, colsA, rowsB, colsB);  
  }

  //printf("got past multiplying A(result)\n"); 
  // now print the new A*B matrix (result)
  printmatrix(result, rowsA, colsB); 

  return 0; 
}
