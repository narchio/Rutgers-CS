#include<stdio.h>
#include<stdlib.h>
#include<string.h>


int main(int argc, char** argv) {

  
  // if there aren't 2 arguments, then there is an error
  if (argc < 2) {
    printf("error, not enough arguments\n"); 
    return 0; 
  }

  char* word;  
  char* begin; 
  char* end; 
  char* result;  
  int len; 

  for (int i = 1; i < argc; i++) {

    // set length and current word ptrs
    len = strlen(argv[i]); 
    word = (char*) malloc(len+1); 
    word = argv[i]; 

    // if first letter is a vowel 
    if (argv[i][0] == 'a' || argv[i][0] == 'e' || argv[i][0] == 'i'
	|| argv[i][0] == 'o' ||  argv[i][0] == 'u' || argv[i][0] == 'A' || 
	argv[i][0] == 'E' || argv[i][0] == 'I' || argv[i][0] == 'O' || 
	argv[i][0] == 'U') {

      // allocate for the result word + 4 
      result = (char*) malloc((len+1)+3); 

      // copy the result word
      result = strcpy(result, word); 
      
      // add the 'yay' to end of the vowel beginning word
      result[len] = 'y'; 
      result[len+1] = 'a'; 
      result[len+2] = 'y'; 
      result[len+3] = '\0'; 

      // now print result
      printf("%s ", result); 

    }  // if first letter is a consonant
    else {

      int count = 0;
      int j = 0; 
     
      //printf("got into the else if"); 
       
      // while the first letter is not a vowel
      while (argv[i][j] != 'a' && argv[i][j] != 'e' && argv[i][j] != 'i'
	&& argv[i][j] != 'o' &&  argv[i][j] != 'u' && argv[i][j] != 'A' && 
	argv[i][j] != 'E' && argv[i][j] != 'I' && argv[i][j] != 'O' && 
	argv[i][j] != 'U') {
	count++; 
	j++; 
      } // end of while

      //printf("got into the while loop"); 
      
      // the string of before the vowel
      begin  = (char*) malloc(count+1); 
      // now load temp with the part of word before the vowel
      int i; 
      for (i = 0; i < count; i++) {
	begin[i] = word[i]; 
      }
      //printf(" begin is : %s\n", begin); 


      int second = len-count; 
      end = (char*) malloc(second+1); 
      // now add the second part of word to the ptr string 
      for (int k = 0; k < second+1; k++) {
	end[k] = word[i]; 
	i++; 
      }
      //printf("end is : %s\n", end);
      
      //now add part of word before the vowel to the end of the result string +3
      //char* ans = (char*) malloc((len+1) + 2);       
      result  = (char*) malloc((len+1) + 2); 
      
      // copy second part first 
      result = strcpy(result, end); 

      // then copy first part to the end
      result = strcat(result, begin); 

      result[len] = 'a'; 
      result[len+1] = 'y'; 
      result[len+2] = '\0'; 

      // now print result
      printf("%s ", result);
    }

    // print the result at the end
    //printf("%s ", result); 
  }

  return 0; 
}
