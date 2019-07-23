#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<string.h>

////////////// GLOBAL VARIABLES ///////////////
int n; // # of inputs
int op; // # of outputs 
int rows; // # of rows
//struct gate* head; // the first gate in the LL of gates
//int** master; 
//////////////////////////////////////////////

struct var {
  int index; 
  char* name;  
  struct var* next; 
  //int* array; 
  int val; 
}; 
// this can be used for both part 1 and 2 for sorting 
struct gate {
  int type; // And == 1, NOT == 2, OR == 3, XOR == 4, NOR == 5, NAND == 6
  struct var** inputs; 
  struct var** outputs; 
  struct var** nums; // for the multiplexer
  int sizein; 
  int sizeout; 
  struct gate* next; 
}; 
// mallocs and initializes to 0
int** initialize(int rows, int cols) {
  // allocate space
  int**  matrix = (int**) malloc(rows * sizeof(int*)); 
  for (int i = 0; i < rows; i++) {
    matrix[i] = (int*) malloc(cols * sizeof(int)); 
  }
  // initialize resulting matrix to 0
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      matrix[i][j] = 0; 
    } 
  } 
  return matrix; 
}

// populates input
int* populateinput(int** master, int rows, int index) {
  int* temp = (int*) malloc(rows*sizeof(int)); 
  for (int i = 0; i < rows; i++) {
    temp[i] = master[i][index]; 
  }
  return temp; 
}

void printint(int* master) {
  for (int i = 0; i < n; i++) {
    printf("%d ", master[i]); 
  }
  //printf("\n"); 
}
void printres(int** master, int rows, int cols) {
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      printf("%d ", master[i][j]);  
    }
    //printf("\n"); 
  }
}
/////////// Gate methods //////////////

// will take 2 integer inputs and AND them
int AND (int input1, int input2) {
  if ((input1 == 1) && (input2 == 1)) {
    return 1; // they are both 1
  }
  return 0; // else, they are not both 1
}

// flip the bit 
int NOT(int input) {
  if (input == 0) {
    return 1; // the opposite
  } 
  return 0; // else the input is 1 and you return 0
}

// if either input is 1, return true
int OR (int input1, int input2) {
  if ((input1 == 1) || (input2 == 1)) {
    return 1; // if either is 1
  } 
  return 0; // else theyre both 0 
}

// if input1 and input2 are different return 1, else return 0
int XOR (int input1, int input2) {
  if ((input1 == 1) && (input2 == 1)) {
    return 0; // same
  } else if ((input1 == 0) && (input2 == 0)) {
    return 0; // same
  }
  return 1; // they are different 
}

// A compliment dot B compliment, is NOR
int NOR (int input1, int input2) {
  return AND(NOT(input1), NOT(input2)); 
}

// // A compliment plus B compliment, is NAND
int NAND (int input1, int input2) {
  return OR(NOT(input1), NOT(input2)); 
}
///////////////////////////////////////////////////
/////////////////// Matrix  methods ///////////////
// Empty to Binary, rows are 2^inp
int* toBinary (int* binary, int rows, int inputs, int i) {
    // this variable will be the correct indeces of the columns
    int count = inputs-1; 
    int k = 0; // correct index to place in 
    // itterate through the columns minus 1
    while (count >= 0) {
      int sq = (int) pow(2, count); 
      int div = i / sq; 
      // mod the index divided by 2^count by 2 to yeild a 0 or a 1 for Binary 
      int res = div % 2; 
      // now place the correct value in the current matrix and decrement count
      binary[k] = res; 
      count = count-1; 
      k++; 
  }
  return binary; 
}
// Convert Binary to Gray code
int* toGraycode (int* binary, int* result, int rows, int inputs, int i) {
  // itterate through the array and do XOR opperations to get gray code
    // this variable will act as the correct number of indeces of columns
    int count = inputs-1; 
    while (count >= 0) {
      if (count == 0) {
	result[0] = binary[0]; 
      } else {
	int right = binary[count]; 
	int left = binary[count-1]; 
	int res; 
	res = XOR(left, right); 
	// update with the XOR'd value
	result[count] = res; 
      }
      // decrement count
      count = count-1; 
    }
  return result; 
}
////////////////// Now, LL  methods////////////////////////

// creates a temp var
struct var* createtemp(struct var* temp, char* name, int rows) {
  //printf("create temp\n"); 
  temp->name = name; 
  temp->next = NULL; 
  temp->val = 0; 
  return temp; 
}

// for a LL of variables (add to back)
void addtemp(struct var* head, struct var* node) {
  struct var* ptr = head; 
  while (ptr != NULL) {
    if (ptr->next == NULL) {
      ptr->next = node; 
      break;
    }
    ptr = ptr->next;
  }
}
 
// for the GATE of variables (add to back)
void addgate(struct gate* head, struct gate* node) {
  struct gate* ptr = head; 
  while (ptr != NULL) {
    if (ptr->next == NULL) {
      ptr->next = node; 
      break;
    }
    ptr = ptr->next; 
  }
}

// traverse Temps
struct var* traversetemps(struct var* head, char* target) {
  struct var* nullptr = NULL; 
  struct var* ptr = head; 
    while (ptr != NULL) {
      if (strcmp(ptr->name, target) == 0) {
	return ptr; 
      }
      ptr = ptr->next; 
    }
    //printf("the NULL WILL PRINT\n"); 
  return nullptr; 
}

////////////////// End of LL methods////////////////////////

////// This will solve the 1-6 operations (not including NOT)
int solve6(struct gate* gate) {
  // CHECK if a temp is in the inputs array or the outputs array (inputvar and outputvar arrays, not the gate arrays) 
  // now solve the gates
  if (gate->type == 1) {
    int input1 = gate->inputs[0]->val; 
    //printf("ANDinput1 %d: ", input1); 
    int input2 = gate->inputs[1]->val; 
    //printf("ANDinput2 %d: ", input2); 
    int result = AND(input1, input2); 
    gate->outputs[0]->val = result; 
  } else if (gate->type == 2) {
    int input1 = gate->inputs[0]->val; 
    //printf("NOTinput1 %d: ", input1);
    int result = NOT(input1); 
    gate->outputs[0]->val = result; 
  } else if (gate->type == 3) {
    int input1 = gate->inputs[0]->val; 
    //printf("ORinput1 %d: ", input1); 
    int input2 = gate->inputs[1]->val; 
    //printf("ORinput2 %d: ", input2); 
    int result = OR(input1, input2);
    gate->outputs[0]->val = result; 
  } else if (gate->type == 4) {
    int input1 = gate->inputs[0]->val;  
    int input2 = gate->inputs[1]->val; 
    int result = XOR(input1, input2); 
      gate->outputs[0]->val = result; 
  } else if (gate->type == 5) {  
    int input1 = gate->inputs[0]->val;  
    int input2 = gate->inputs[1]->val; 
    int result = NOR(input1, input2); 
    gate->outputs[0]->val = result; 
  } else if (gate->type == 6) {
    int input1 = gate->inputs[0]->val;  
    int input2 = gate->inputs[1]->val; 
    int result = NAND(input1, input2);
    gate->outputs[0]->val = result; 
  }
  //printf("%d int\n", gate->outputs[0]->val); 
  return gate->outputs[0]->val; // because they only make 1 output 
}

////////////////////////

int main(int argc, char** argv) {
  
  FILE *fp; 
  fp = fopen(argv[1], "r"); 

  n = 0; // n = # of inputs
  fscanf(fp,"INPUTVAR %d", &n); 
  //printf("n : %d\n", n); 
  
  // make the rows
  rows = (int) pow(2, n); 
  //printf("rows : %d\n", rows); 

  // NEED to create an array of inputs here
  struct var** inputs = (struct var**) malloc(sizeof(struct var*)*n); 
  for (int i = 0; i < n; i++) {
    inputs[i] = (struct var*) malloc(sizeof(struct var)); 
  }  
  // now, scan for the inputs and store them in this array
  char* varname = (char*) malloc(sizeof(char) * 16); 
  for (int i = 0; i < n; i++) {
    fscanf(fp,"%s", varname);
    // here, NEED to insert it into the array of inputs and populate it with correct values
    struct var* var = (struct var*) malloc(sizeof(struct var));   
    var->name = (char*) malloc(sizeof(char)*(strlen(varname)+1)); 
    strcpy(var->name, varname); 
    var->val = 0; 
    inputs[i] = var;  
    //printf("inputs[i] = %s\n", inputs[i]->name); 
  }
  fscanf(fp,"\n");  // CONFIRMED the inputs are loaded with the correct values

  // SAME for the Outputs //
  op = 0; // op = # of outputs
  fscanf(fp,"OUTPUTVAR %d", &op); 
  // NEED to create an array of outputs here
  struct var** outputs = (struct var**) malloc(sizeof(struct var*)*op); 
  for (int i = 0; i < op; i++) {
    outputs[i] = (struct var*) malloc(sizeof(struct var)); 
  }  
  
  // insert into array of outputs
  char* outname = (char*) malloc(sizeof(char) * 16); 
  for (int i = 0; i < op; i++) { 
    fscanf(fp,"%s", outname);
    // here, NEED to insert it into the array of outputs 
    struct var* outvar = (struct var*) malloc(sizeof(struct var)); 
    outvar->name = (char*) malloc(sizeof(char)*(strlen(outname)+1)); 
    strcpy(outvar->name, outname); 
    outvar->val = 0; 
    outputs[i] = outvar; 
  }
  fscanf(fp,"\n"); 
  
  // now to scan in the gates (not including Multiplexers or Decoders
  char* type = (char*) malloc(sizeof(char) * 16); 
  char* input1 = (char*) malloc(sizeof(char) * 16); 
  char* input2 = (char*) malloc(sizeof(char) * 16);  
  char* output1 = (char*) malloc(sizeof(char) * 16); 
  //int count = 0;
  for (int l = 0; l < rows; l++) {
    struct var* temphead = NULL;  
    char* line = (char*) malloc(sizeof(char) * 16); 
    //////////////////////////////// MAIN WHILE LOOP ////////////////////////
    //int count = 0; 
    int* arr = (int*) malloc(sizeof(int) * rows); 
    int* res = (int*) malloc(sizeof(int)* rows); 
    arr = toBinary(arr, rows, n, l); 
    res = toGraycode(arr, res, rows, n, l); 
    printint(res); // take away the \n, will go at end when output is added
    for (int i = 0; i < n; i++) {
      inputs[i]->val = res[i];
      //printf("res[i] %d\n", res[i]); 
    }
    struct gate* gate; 
    while (fgets(line, 1000, fp)) {
      
      // will load in each line
      type = strtok(line, " "); 
      if (type[0] == 'N' && type[1] == 'O') { // if it is a not gate
	input1 = strtok(NULL, " "); 
	output1 = strtok(NULL, "\n"); 
      } else {
	input1 = strtok(NULL, " "); 
	input2 = strtok(NULL, " "); 
	output1 = strtok(NULL, "\n"); 
      } 
      /* printf("type: = %s\n", type); 
      printf("input1: = %s\n", input1); 
      printf("input2: = %s\n", input2); 
      printf("output1: = %s\n", output1); */ 
      
      // find correct input index
      int input1index = 0; 
      int input2index = 0; 
      for (int i = 0; i < n; i++) {
	if (strcmp(input1, inputs[i]->name) == 0) {
	  input1index = i; 
	  //printf("INDEX1 val: %s\n", inputs[i]->name); 
	} else if (strcmp(input2, inputs[i]->name) == 0) {
	  input2index = i; 
	  //printf("INDEX2 val: %s\n", inputs[i]->name); 
	} 
      }
      //printf("finished the corrct input index\n"); 
      
      // if NOT gate
      //struct gate* gate; 
      if (type[0] == 'N' && type[1] == 'O') {
	gate = (struct gate*) malloc(sizeof(struct gate)); 
	gate->inputs = (struct var**) malloc(sizeof(struct var*)*1); 
	gate->inputs[0] = (struct var*) malloc(sizeof(struct var)); 
	gate->inputs[0] = inputs[input1index]; 
	//printf("input: %s\n", inputs[input1index]->name); 
	gate->outputs = (struct var**) malloc(sizeof(struct var*)*1); 
	for (int i = 0; i < 1; i++) {
	  gate->outputs[i] = (struct var*) malloc(sizeof(struct var)); 
	  gate->outputs[i] = outputs[i]; 
	  //printf("i = %d\n", i); 
	}
      } else { // not a NOT gate
	//printf("in the else\n"); 
	gate = (struct gate*) malloc(sizeof(struct gate)); 
	gate->inputs = (struct var**) malloc(sizeof(struct var*)*2); 
	// input1
	//printf("before input1\n input1index %d\n", input1index);
	gate->inputs[0] = (struct var*) malloc(sizeof(struct var)); 
	gate->inputs[0] = inputs[input1index]; 
	//printf("gate->inputs[i] %s\n", gate->inputs[0]->name); 
	// input2
	//printf("before input2\n"); 
	gate->inputs[1] = (struct var*) malloc(sizeof(struct var)); 
	gate->inputs[1] = inputs[input2index]; 
	//printf("gate->inputs[i] %s\n", gate->inputs[1]->name); 
	gate->outputs = (struct var**) malloc(sizeof(struct var*)*1); 
	for (int i = 0; i < 1; i++) {
	  gate->outputs[i] = (struct var*) malloc(sizeof(struct var)); 
	  gate->outputs[i] = outputs[i]; 
	}
      }
      //printf("created gates and added temps successfully, now to check temps\n");

      // now get type
      if (type[0] == 'A') {
	gate->type = 1; // AND
      } else if (type[0] == 'N' && type[2] == 'T') {
	gate->type = 2; // NOT
      } else if (type[0] == 'O') {
	gate->type = 3; // OR
      } else if (type[0] == 'X') {
	gate->type = 4; // XOR
      } else if (type[0] == 'N' && type[2] == 'R') {
	gate->type = 5; // NOR
      } else if (type[0] == 'N' && type[2] == 'N') {
	gate->type = 6; 
      }
      
      
      // check if an output is a temp, if it is, insert it into the temp LL 
      // check if an output is a temp and replace accordingly
      // NEED THIS OR SEARCH IN TEMP2 WILL NOT WORK SO ADD IT IN 
      
      if (strstr(output1, "OUT") == NULL) { // if it is a temp and not an OUT
	// create temp node and add to LL if not already there
	//printf("in output\n");
	struct var* outtemp = (struct var*) malloc(sizeof(struct var)); 
	//struct var* ifnull = (struct var*) malloc(sizeof(struct var)); 
	// update the outtemp with the temp from the LL if not NULL
	outtemp->name = output1; 
	outtemp->val = 0; 
	outtemp->next = NULL; 
	if (temphead == NULL) {
	  temphead = outtemp; 
	  addtemp(temphead, outtemp); 
	  gate->outputs[0] = outtemp; 
	} else {
	  outtemp = traversetemps(temphead, output1); 
	  gate->outputs[0] = outtemp; 
	}
	//printf("gate->outputs[0]->name %s\n", gate->outputs[0]->name); 
      }
      //printf("output is good\n"); 
      
      // check if an input is a temp and replace accordingly 
      // if the correct input index is a temp
      if (strstr(input1, "temp") != NULL) {
	// create temp node and add to LL if not already there
	//printf("in input1\n"); 
	struct var* temp = (struct var*) malloc(sizeof(struct var));
	//struct var* ifnull = (struct var*) malloc(sizeof(struct var)); 
	// update the temp with the var from the LL 
	temp->name = input1; 
	temp->val = 0; 
	temp->next = NULL; 
	if (temphead == NULL) {
	  temphead = temp; 
	  addtemp(temphead, temp); 
	  gate->inputs[0] = temp; 
	  input1index = 0; 
	  //printf("INDEX1ifnull val: %s\n", gate->inputs[0]->name);
	} else {
	  temp = traversetemps(temphead, input1); 
	  gate->inputs[0] = temp; 
	  input1index = 0; 
	  //printf("INDEX1temp val: %s\n", gate->inputs[0]->name); 
	}
      } else if (strstr(input2, "temp") != NULL) {
	// create temp node and add to LL if not already there
	struct var* temp2 = (struct var*) malloc(sizeof(struct var));
	//struct var* ifnull = (struct var*) malloc(sizeof(struct var)); 
	// create a temp with the var from the LL
	temp2->name = input2; 
	temp2->val = 0; 
	temp2->next = NULL; 
	if (temphead == NULL) {
	  temphead = temp2; 
	  addtemp(temphead, temp2); 
	  gate->inputs[1] = temp2; 
	  input1index = 1; 
	  //printf("INDEX2ifnull val: %s\n", gate->inputs[1]->name);
	} else {
	  temp2 = traversetemps(temphead, input2); 
	  gate->inputs[1] = temp2; 
	  input1index = 1; 
	  //printf("INDEX2temp val: %s\n", gate->inputs[1]->name); 
	}
      }
      
      //printf("inputs are good\n"); 
      //printf("after temp check\n"); 
      // then add the gate to the gateLL -> but do we have to? just solve gate
      
      // solve the gate 
      solve6(gate); 
      //printf("SOLVE output = %d\n", gate->outputs[0]->val); 
      
      // update the tempLL if the output of the gate is a temp, else if the output of it is an OUT, then update the outputs var** 
      if (strstr(output1, "OUT") == NULL) { // it is a temp
	struct var* ptr = (struct var*) malloc(sizeof(struct var)); 
	ptr = traversetemps(temphead, output1); 
	//printf("after the ptr\n"); 
	// if this temp is in the LL (has to be becasuse we inserted it before)
	ptr->val = gate->outputs[0]->val; 
	//printf("gate->output[0]->val = %d\n", ptr->val);
      } else { // if it is the OUT
	outputs[0]->val = gate->outputs[0]->val; 
	//printf("gate->output[0]->val = %d\n", gate->outputs[0]->val); 
      } 
      
      //printf("%d\n", gate->outputs[0]->val);  
      //printf("\n");  
    } // end of while loop
    printf("%d\n", gate->outputs[0]->val);  
    rewind(fp); 
    fgets(line, 1000, fp);
    fgets(line, 1000, fp); 
  }
  //printf("escaped the while loop\n"); 
  
  
  return 0; 
}
