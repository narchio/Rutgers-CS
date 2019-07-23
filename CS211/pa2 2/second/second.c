#include<stdio.h>
#include<stdlib.h>

// this method prints the matrix
void printmatrix(int ** matrix, int rows, int cols) {
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      printf("%d\t", matrix[i][j]);
    }
    printf("\n"); 
  }
  return; 
}

// will check if there is a number already in the rows or columns of the matrix
int isinroworcol (int** matrix, int row, int col, int target) {
  // always itterate through row, then col so the single row first
  for (int j = 0; j < 9; j++) {
    if (matrix[row][j] == target) { 
      return 1; // is in row 
    }
  }
  // now check cols 
  for (int i = 0; i < 9; i++) {
    if (matrix[i][col] == target) {
      return 1; // is in col
    }
  }
  // else, they are not in either rows or columns, so return 0 
  // to say that the number was not found in either
  return 0; 
}

// will check if target is in a 3x3 grid already
int isin3x3grid (int** matrix, int row, int col, int target) {
  // to get the beginning row and col, we need to take the current row and mod it by 3, this will ensure that we begin on the first row and col of the correct 3x3 grid, since there are 9 possible grids on a 9x9 matrix
  int beginrow = row - (row%3); 
  int begincol = col - (col%3); 

  // now itterate through the correct grid
  for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
      // adding begrow/col will bring us to the correct current row/col value
      int currentrow = i + beginrow; 
      int currentcol = j + begincol; 
      // now check if this equals the target
      if (matrix[currentrow][currentcol] == target) {
	return 1; // is in 3x3 grid 
      }
    }
  }
  // else, it is not in the grid
  return 0; // not in the 3x3 grid 
}
// duplicate in resulting board
int checkdup(int** board, int row, int col, int rows, int cols) { 
  // itterate through matrix, check if there is a row that has a dup in it
  for (int i = 0; i < rows; i++) {
    for (int j = 1; j < cols; j++) {
      if ((board[row][i] == board[row][j]) && (i != j)) {
	// if it is a dup, return 0 
	return 0; 
      } 
    }
  }
  // itterate through the matrix and check if there is a col with a dup in it
  for (int i = 0; i < rows; i++) {
    for (int j = 1; j < cols; j++) {
      if ((board[i][col] == board[j][col]) && (i != j)) {
	// if it is a dup, return 0
	return 0; 
      } 
    }
  }
 // if it got through all the checks, then the board is good 
  return 1; 
}
// checks duplicate in a 3x3 grid
int checkdup3x3(int** board, int row, int col, int rows, int cols) {

  // itterate through the 3x3 grid, if there is a dup within it 
  int beginrow = row - (row%3); 
  int begincol = col - (col%3); 

  // now itterate through the correct grid
  for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
      // adding begrow/col will bring us to the correct current row/col value
      int currentrow = i + beginrow; 
      int currentcol = j + begincol; 
      // now check if this equals the target
      if (checkdup(board, currentrow, currentcol, 3, 3) == 0) {
	return 0; 
      } 
    }
  }
  // else, no dups
  return 1; 
}
// will return true if there is an unfilled (0) in a row or col to see if finished or not. Will also function as a base case in the actual algorithm solve
int unfilled(int** matrix, int row, int col) {
  // itterate through board
  for (int i = 0; i < row; i++) {
    for (int j = 0; j < col; j++) {
      // check if there is an unfilled entry still a 0
      if (matrix[i][j] == 0) {
	return 1; // unfilled
      }
    }
  }
  return 0; // all are filled
}

// this method will solve the sudoku, combining all of the methods that I have 
// created previously. It will take in the matrix alone as the rows and cols 
//willvbe itterated through. 
int solve (int** matrix) {
  int digit = 0; 
  // double loop to itterate through the 9x9 matrix or board
  for (int i = 0; i < 9; i++) {
    for (int j = 0; j < 9; j++) {
      // 0 is for an empty '-' value 
      if (matrix[i][j] == 0) {
	// start the digit at 1
	digit = 1; 
	// loop through the 9 possible digits 
	while (digit <= 9) {
	  // if the current digit is an acceptable move
	  if ((isinroworcol(matrix, i, j, digit) == 0) && 
	      (isin3x3grid(matrix, i, j, digit) == 0)) {
	    // assigns digit
	    matrix[i][j] = digit;
	    // now recurse back, if it is 1 then that was the last empty filled 
	    if (solve(matrix) == 1) {
	      // return solved 
	      return 1; 
	      // else, we have to change back to an empty
	    } else {
	      // reset to empty
	      matrix[i][j] = 0;  
	    } 
	  }
	  // increment the digit before end of while loop, if it was not found
	  // to be the correct input 
	  digit = digit + 1; 
	}
	// if it is none of these, it is unsolvable 
	return 0; 
      }
    }
  }
  // else, we have completed it and it is solved
  return 1; 
}

// Any puzzle with a single solution (not requiring guessing) has a min of 17 spots already filled in.. thus if there are less than 17 squares filled in from the start, then it requires guesses and does not have a unique solution 
int requiresguess(int** board) {
  // itterate through the board
  int count = 0; 
  for (int i = 0; i < 9; i++) {
    for (int j = 0; j < 9; j++) {
      // if it is not a 0, increment count 
      if (board[i][j] != 0) {
	count = count + 1; 
      } 
      // else, continue
    }
  }
  return count; 
}

// main, will run the functions created above
int main (int argc, char** argv) {

  FILE *fp; 
  fp = fopen(argv[1], "r"); 

  int rows = 9; 
  int cols = 9; 
  // allocate  and malloc for matrix
  int** board = (int**) malloc(rows * sizeof(int*)); 
  for (int i = 0; i < rows; i++) {
    board[i] = (int*) malloc(cols * sizeof(int)); 
  }
  // initialize it to all 0s
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      board[i][j] = 0; 
    }
  }
  // scan for the '-' and put a -1 in that position of the 9x9 board (i,j)
  // scan for a number and place that into the i,j
  int val; 
  char c; 
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      if (fscanf(fp, "%d", &val) == 1) {
	board[i][j] = val; 
      } else if (fscanf(fp, "%c", &c) == 1) {
	board[i][j] = 0; 
      }
      fscanf(fp,"\t"); 
    }
    fscanf(fp,"\n"); 
   }
  
  // check sudoku to see if it requires a guess or not
  // if requires guess, print no sol
  if (requiresguess(board) <= 16) {
      printf("no-solution"); 
      return 0; 
  } 

  // now that it is a valid sudoku, do the sudoku 
  if (solve(board) == 1) {
    // check for any duplicates 
    int i = 0; 
    for (i = 0; i < 9; i++) {
      if (checkdup(board, i, i, rows, cols) == 0) {
	printf("no-solution"); 
	return 0; 
      } else if (checkdup3x3(board, i, i, rows, cols) == 0) {
	printf("no-solution"); 
	return 0; 
      }
    }
    if (i == 9) {
      printmatrix(board, rows, cols); 
      return 0; 
    }
  } else {
    printf("no-solution");
    return 0; 
  }
  return 0; 
}
