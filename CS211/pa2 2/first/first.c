#include<stdio.h>
#include<stdlib.h>

// for checking purposes, not for the actual output 
void printmatrix(double ** matrix, int rows, int cols) {
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      printf("%lf\t", matrix[i][j]);
    }
    printf("\n"); 
  }
  return; 
}


// function will multiply two matrices together that are compatible 
// and return the resulting matrix 

double** multiply(double** A, double** B, double** result, int rowsA, int colsA, int rowsB, int colsB) {
  double resultdata = 0; 
  
  // create result matrix
  //create resulting matrix (mxn and nxm, will yeild a mxm)
  result = (double**) malloc(rowsA * sizeof(double*)); 
  for (int i = 0; i < rowsA; i++) { 
    result[i] = (double*) malloc(colsB * sizeof(double)); // columns in result
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
  for (i = 0; i < rowsA; i++) { //itterate through the rows of the first
    for (j = 0; j < colsB; j++) { // and the cols of the second (mxn*nxm = mxm)
      for (n = 0; n < rowsB; n++) { // now by the dimensions of the inner
	double a = A[i][n]; 
	double b = B[n][j]; 
	resultdata = resultdata + a*b;
      } 
      result[i][j] = resultdata; 
      resultdata = 0; 
    }
  }

  return result;

}


// function will return the transpose of the current matrix
// will take in the current matrix, its number of rows and columns

double** transpose(double** current, int rows, int cols) {

  // allocate space for the result (rows and cols reversed)
  double** result = (double**) malloc(cols* sizeof(double*)); 
  for (int i = 0; i < cols; i++) {
    result[i] = (double*) malloc(rows * sizeof(double)); 
  }
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      result[i][j] = current[j][i]; 
    }
  }

  // rows and cols are reversed for result
  return result; 
}

// this will divide a row by a constant
// takes in the current matrix, the targeted row to be divided by 
// the columns of the matrix (length) and the number to divide by
double** divrowbyconstant (double** matrix, int targetrow, int cols, double div){
  double resultdata; 
  for (int i = 0; i < cols; i++) {
    resultdata = matrix[targetrow][i]; 
    resultdata = resultdata / div; 
    matrix[targetrow][i] = resultdata; 
  }
  return matrix; 
}

// takes in current matrix and multiplies an entire row by a number 
// takes in the target row, the columns (length) and the number to mult by
double** multiplybyconstant (double** matrix, int targetrow, int cols, double mult){
  double resultdata; 
  for (int i = 0; i < cols; i++) {
    resultdata = matrix[targetrow][i]; 
    resultdata = resultdata * mult; 
    matrix[targetrow][i] = resultdata; 
  }
  return matrix; 
}

// this will subtract a row from another row
//takes in the current matrix, the top row and bot row, num of columns
double** sub (double** matrix, int toprow, int botrow, int cols){
  double resultdata; 
  for (int i = 0; i < cols; i++) {
    double top = matrix[toprow][i]; 
    double bot = matrix[botrow][i]; 
    resultdata = bot - top; 
    matrix[botrow][i] = resultdata; 
  }
  return matrix; 
}

// will calculate the inverse by augmenting with the Identity matrix
double** inverse(double** matrix, int rows, int cols) {
  // create the new identity matrix
  double** identity = (double**) malloc(rows * sizeof(double*)); 
  for (int i = 0; i < rows; i++) {
    identity[i] = (double*) malloc(cols * sizeof(double)); 
  }

  // fill its rows/cols with the values of the identity matrix
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      if (i == j) {
	identity[i][j] = 1; 
      }
    }
  }
  
  // Now calculate the inverse
  // replicate all steps for the identity matrix
  int pivot = 0; 
  int toprow; 
  int botrow; 
  // first, traverse down the matrix and convert into pivot cols  
  while (pivot < rows) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
	// if pivot
	if (i == j) {
	  double div = matrix[i][j]; 
	  matrix = divrowbyconstant(matrix, i, cols, div);

	  // replicate same for identity matrix
	  identity = divrowbyconstant(identity, i, cols, div);	  
	  
	  // now itterate through the first row to see if there are 
	  // any more constants under the pivot that are not 0
	  for (int k = i+1; k < rows; k++) {
	    if (matrix[k][pivot] == matrix[i][pivot]) {
	      toprow = i; 
	      botrow = k; 

	      matrix = sub(matrix, toprow, botrow, cols);

	      // repliate same for identity matrix
	      identity = sub(identity, toprow, botrow, cols);
	    } else {
	      toprow = i; 
	      botrow = k; 
	      // assign mult
	      double mult = matrix[k][pivot];

	      matrix = multiplybyconstant(matrix, toprow, cols, mult); 

	      // replicate same for identity
	      identity = multiplybyconstant(identity, toprow, cols, mult); 
	      
	      matrix = sub(matrix, toprow, botrow, cols); 
	      
	      // replicate same for identity
	      identity = sub(identity, toprow, botrow, cols); 

	      // now need to divide by same we mulitplied by
	      div = mult; 
	      matrix = divrowbyconstant(matrix, toprow, cols, div); 

	      // same for identity
	      identity = divrowbyconstant(identity, toprow, cols, div);
	      
	    } 
	  } // end of inner k for
	  // increment the pivot 
	  pivot = pivot + 1; 
	}
      } // end of inner for loop  
    } // end of outer for loop
  } // end of while   

  //pivot will be adjusted to pivot-1 because it is at an out of bounds index
  pivot = pivot-1; 

  // Now go back through and take out 0's above pivots (also no -0) 
  while (pivot >= 0) {
    for (int i = rows; i >= 0; i--) {
      for (int j = cols-1; j >= 0; j--){
	// if a pivot 
	if (pivot == j) {
	  // we want to ensure there are no 0's above the pivot      
	  for (int k = pivot-1; k >= 0; k--) {
	    if (matrix[k][pivot] == matrix[pivot][pivot]) {
	      toprow = pivot; 
	      botrow = k; 
	      // now subtract 
	      matrix = sub(matrix, toprow, botrow, cols); 

	      // same for identity
	      identity = sub(identity, toprow, botrow, cols); 
	      
	    } else {
	      toprow = pivot; 
	      botrow = k; 
	      double mult = matrix[k][pivot]; 
	      
	      // mulitply 
	      matrix = multiplybyconstant(matrix, toprow, cols, mult); 
	      
	      // same for identity
	      identity = multiplybyconstant(identity, toprow, cols, mult); 
	      
	      // and subtract the bot row from the top row
	      matrix = sub(matrix, toprow, botrow, cols); 
	      
	      // same for identity
	      identity = sub(identity, toprow, botrow, cols); 

	      // now divide to get the bottom row back to normal
	      double div = mult; 
	      matrix = divrowbyconstant(matrix, toprow, cols, div); 

	      // identity div 
	      identity = divrowbyconstant(identity, toprow, cols, div); 
	    } 
	  }
	}
	pivot = pivot-1; 
      } // end of inner for
    } // end of outer for
  }// end of while 
  // return augmented identity matrix
  return identity; 
}

int main(int argc, char** argv) {
  FILE *fp; 
  fp = fopen(argv[1], "r"); 
  int K; 
  int N; 
  fscanf(fp, "%d\n", &K); 
  fscanf(fp, "%d\n", &N);

  // allocate for the matrix X of (N x (K+1))
  double** X = (double**) malloc(N * sizeof(double*)); 
  for (int i = 0; i < N; i++) {
    X[i] = (double*) malloc((K+1) * sizeof(double));
  }
  // allocate for W matrix of (K+1) x 1
  double** W = (double**) malloc((K+1) * sizeof(double*)); 
  for (int i = 0; i < (K+1); i++) {
    W[i] = (double*) malloc(1 * sizeof(double)); 
  }
  // allocate for Y matrix of N x 1
  double** Y = (double**) malloc(N * sizeof(double*)); 
  for (int i = 0; i < N; i++) {
    Y[i] = (double*) malloc(1 * sizeof(double)); 
  }

   // now, fill the 1st col of X with 1.0s
  for (int i = 0; i < N; i++) {
    X[i][0] = 1.0; 
  }
  // Now, fill X with rest of the values and fill Y with (K+1) col values  
  for (int i = 0; i < N; i++) {
    for (int j = 1; j < (K+1); j++) {
	fscanf(fp, "%lf,", &X[i][j]);
    }
     fscanf(fp, "%lf\n", &Y[i][0]);
  }
  // First, calculate XT*X
  // allocate for the matrix step 1 of ((K+1) x (K+1))
  double** step1 = (double**) malloc((K+1) * sizeof(double*)); 
  for (int i = 0; i < (K+1); i++) {
    step1[i] = (double*) malloc((K+1) * sizeof(double));
  }
  // allocate for the matrix XT of ((K+1) x N)
  double** XT = (double**) malloc((K+1) * sizeof(double*)); 
  for (int i = 0; i < (K+1); i++) {
    XT[i] = (double*) malloc(N * sizeof(double));
  }
  XT = transpose(X, N, (K+1)); 
  // step1 is the result of XT*X 
  step1 = multiply(XT, X, step1, (K+1), N, N, (K+1)); 

  // Second, calculate the inverse of step1
  step1 = inverse(step1, (K+1), (K+1)); 

  // Third, step1 * XT so ((K+1) x (K+1) * (K+1) x N) = (K+1) x N
  double** step3 = (double**) malloc((K+1) * sizeof(double*)); 
  for (int i = 0; i < (K+1); i++) {
    step3[i] = (double*) malloc(N * sizeof(double));
  }
  step3 = multiply(step1, XT, step3, (K+1), (K+1), (K+1), N); 

  // Fourth, we calculate step3 * Y to get the W (K+1 x 1)
  W = multiply(step3, Y, W,(K+1), N, N , 1); 

  // Scan the second file
  // Now calculate the testY = W * testX 
  FILE *fp2; 
  fp2 = fopen(argv[2], "r"); 
  
  int M = 0; 
  fscanf(fp2, "%d\n", &M);
  //printf("%d M", M); 

  double houseprice = W[0][0];
  double val = 0; 

  for (int i = 0; i < M; i++) {
    //reset houseprice to the first of W
    houseprice = W[0][0]; 
    for (int j = 1; j < K; j++) { 
      fscanf(fp2, "%lf,",&val );
      val = val * W[j][0]; 
      houseprice = houseprice + val; 
    }
    fscanf(fp2, "%lf\n", &val); 
    val = val * W[K][0]; 
    houseprice = houseprice + val; 
    printf("%0.0lf\n", houseprice); 
  }

  return 0; 
}
