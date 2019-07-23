#include<stdio.h>
#include<stdlib.h>

// sums a single row
int sumofrow(int** matrix, int row, int nxn) {
  int sumrow = 0; 
  for (int i = 0; i < nxn; i++) {
    sumrow = sumrow + matrix[row][i]; 
  }
  return sumrow; 
}

// sums a single column 
int sumofcolumn(int** matrix, int col, int nxn) {
  int sumcol = 0; 
  for (int i = 0; i < nxn; i++) {
    sumcol = sumcol + matrix[i][col]; 
  }
  return sumcol; 
}

// sums the left -> right diagonal (i am referring to this as diagonal 1) 
int sumdiagonal1(int** matrix, int nxn) {
  int diagonal1 = 0; 
  int j; 
  // begin loop
  for (int i = 0; i < nxn; i++) {
    j = i; 
    diagonal1 = diagonal1 + matrix[i][j]; 
  }
  return diagonal1; 
}

// sums the right -> left diagonal (i am reffering to this as diagonal 2)
int sumdiagonal2(int** matrix, int nxn) {
  int diagonal2 = 0; 
  int j = nxn-1; 
  // begin loop
  for (int i = 0; i < nxn; i++) {
    diagonal2 = diagonal2 + matrix[i][j]; 
    j = j - 1; 
  }
  return diagonal2; 
}

// returns 'T' if there is a duplicate or 'F' if there is no duplicate found
char containsduplicatenumber(int** matrix, int nxn) {
  for (int i = 0; i < nxn; i++) {
    for (int j = 0; j < nxn; j++) {
      for (int n = i+1; n < nxn; n++) {
	for (int m = 0; m < nxn; m++) {
	  // if there is a duplicate value, return true
	  if (matrix[i][j] == matrix[n][m]) {
	    return 'T'; 
	  }
	}
      }
    }
  }
  // there is no duplicate value
  return 'F'; 
}

int main(int argc, char** argv) {

  FILE *fp; 
  fp = fopen(argv[1], "r"); 

  // the rows/columns of the square
  int nxn;  
  fscanf(fp, "%d\n", &nxn); 
  
  // create and allocate space for the matrix
  int** matrix = (int**) malloc(nxn * sizeof(int*)); 
  for (int i = 0; i < nxn; i++) {
    matrix[i] = (int*) malloc(nxn * sizeof(int)); 
  }

  // initialize it with 0 values
  for (int i = 0; i < nxn; i++) {
    for (int j = 0; j < nxn; j++) {
      matrix[i][j] = 0; 
    }
  }

  // now populate the matrix with correct, scanned values
  int fill; 
  for (int i = 0; i < nxn; i++) {
    for (int j = 0; j < nxn; j++) {
      fscanf(fp, "%d\t", &fill); 
      matrix[i][j] = fill; 
    }
    fscanf(fp, "\n"); 
  }
  /*
  /////////// print matrix
  for (int i = 0; i < nxn; i++) {
    for (int j = 0; j < nxn; j++) {
      printf("%d\t", matrix[i][j]);  
    }
    printf("\n"); 
  }
  ///////////
  */



  // now it is populated, check for duplicates
  char dup = containsduplicatenumber(matrix, nxn); 
  if (dup == 'T') {
    // then there is a duplicate
    printf("not-magic"); 
    return 0; 
  }
  
  // now, determine if matrix with no duplicates is magic
  for (int i = 0; i < nxn; i++) {
   
    int sumrow = sumofrow(matrix, i, nxn); 
    // printf("sumrow %d", sumrow); 
    int sumcol = sumofcolumn(matrix, i, nxn); 
    //printf("sumcol %d", sumcol);
    int diagonal1 = sumdiagonal1(matrix, nxn); 
    // printf("diagonal1 %d", diagonal1);
    int diagonal2 = sumdiagonal2(matrix, nxn); 
    // printf("diagonal2 %d", diagonal2);
   
    // now check if the sums of rows, colums and diags are equal
    if (sumrow != sumcol) {
      printf("not-magic"); 
      return 0; 
    } else if (sumrow != diagonal1) {
      printf("not-magic"); 
      return 0; 
    } else if (sumrow != diagonal2) {
      printf("not-magic"); 
      return 0; 
    }
  }
  // else, it is good and it is a magic square 
 
  fclose(fp); 
  printf("magic"); 
  return 0; 
}
