#include<stdio.h>
#include<stdlib.h>
#include<string.h>

int main(int argc, char** argv) {
  
  // no file to read, so get output
  int len;  
  
  // if there arent 2 arguments, then there is an error with the program
  if (argc < 2) {
    printf("error, not enough arguments\n"); 
    return 0; 
  }

  // must start counter (i) at 1 because it is the second argument of the line 
  int i = 1; 
  while (i < argc) {
    
    // get the length of the string from the inputed word 
    len = strlen(argv[i]); 
    
    // argv[i][len] would be '\0' so we have to take len-1 to get the last char
    printf("%c", argv[i][len-1]);

    // increment i 
    i = i + 1; 
  } 
  return 0; 
}

