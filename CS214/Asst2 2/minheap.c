#include "minheap.h"

//--------------------------------------------------------// 
//////////////////// minHeap functions /////////////////////
//--------------------------------------------------------//

//initialize heap
hnode** initializeheap(int capacity){
    hnode** minheap = (hnode**) malloc(capacity*sizeof(hnode*));
    int i;
    for (i = 0; i < capacity; i++) {
        minheap[i] = (hnode*) malloc(sizeof(hnode) * 1);
    }
    return minheap;
}

//create heapnode 
hnode* createhnode(int freq, char* token){
    hnode* node = (hnode*) malloc(1*sizeof(hnode)); 
    if(!node){
      printf("Unable to allocate memory to create heap node!\n"); 
      return NULL; 
    }
    node->data = freq; 
    node->bnode = NULL; 
    node->word = (char*) malloc((strlen(token)+1)*sizeof(char)); 
    if(!(node->word)){
      printf("Unable to allocate memory to store token in heap node!\n"); 
      return NULL;
    }
    strcpy(node->word, token);
    return node;
}

// deletes the heapnode and sifts down
hnode** deletehnode(hnode** array, int index) {
  //int heapsize = array->heapsize;
  if (minheapsize == 0) {
    printf("Error, attempted to delete from empty heap\n");
    return array;
  } else if (minheapsize == 1) {
    minheapsize--; 
    return array;
  } else {
    hnode* last = array[minheapsize-1];
    array[index] = last;
    minheapsize--; 
    siftDown(&array);
    return array;
  }
}

// inserts a heapnode and sifts up
//hnode** inserthnode (hnode** array, hnode* insertnode, int index) {
hnode** inserthnode ( hnode** array, hnode* insertnode) {
    if (minheapsize == 0) {
        array[minheapsize] = insertnode;
        ++minheapsize;
    } else {
        array[minheapsize] = insertnode;
        ++minheapsize;
    }
    //array[minheapsize] = insertnode;
    //++minheapsize;
    siftUp(&array);
    return array;
}

// this is called after the node has been inserted, sifting up to find the correct spot for the inserted node
void siftUp (hnode*** array) {
    int k = 0;
    k = minheapsize - 1;
    int p= (k-1) /2;
    // loop through indices from heapsize-1 to 0
    while (k > 0)    {
        p = (k-1) / 2; // p is the parent index
        //printf(" before p = %d\t, before k = %d\n", p, k);
        if ((*array)[k]->data < (*array)[p]->data) { // swap if current k is less than the parent p
            // update new min
            int tempData = (*array)[p]->data;
            (*array)[p]->data = (*array)[k]->data;
            (*array)[k]->data = tempData;
            // now swap the words
            char* tempWord = (*array)[p]->word;
            (*array)[p]->word = (*array)[k]->word;
            (*array)[k]->word = tempWord;
            // swap bnodes
            bnode* tempBnode = (*array)[p]->bnode;
            (*array)[p]->bnode = (*array)[k]->bnode;
            (*array)[k]->bnode = tempBnode;
            // increment k
            k = p;
        } else {
            break;
        }
    } // end of while
}


// this is called after the delete, to siftDown in order to find correct spot for the new root
void siftDown (hnode*** array) {
  int k = 0;
    int left = (2 * k) + 1;
    //while (left < (*array)->heapsize) {      
      while (left < minheapsize) {
        int min = left;
        int right = (2 * k) + 2;
        //if (right < (*array)->heapsize) { // if there is a right child
          if (right < minheapsize) {
              if ((*array)[right]->data < (*array)[left]->data) {
                min = right;
              }
          }
          if ((*array)[k]->data > (*array)[min]->data) {
              // update new min
              int tempData = (*array)[k]->data;
              (*array)[k]->data = (*array)[min]->data;
              (*array)[min]->data = tempData;
              // now swap the words
              char* tempWord = (*array)[k]->word;
              (*array)[k]->word = (*array)[min]->word;
              (*array)[min]->word = tempWord;
              // now swap the bnodes
              bnode* tempBnode = (*array)[k]->bnode;
              (*array)[k]->bnode = (*array)[min]->bnode;
              (*array)[min]->bnode = tempBnode;
              // now increment k and left
              k = min;
              left = (2 * k) + 1;
          } else {
              break;
          }
      }
}

//--------------------------------------------------------//                                                  
//////////////////// bnode functions /////////////////////                                                   
//--------------------------------------------------------//          

// inserts the bnode and returns the root
bnode* insertBnode (hnode** hnode1, hnode** hnode2) {
  bnode* left = (bnode*) malloc(sizeof(bnode));
  bnode* right = (bnode*) malloc(sizeof(bnode));
  bnode* root = (bnode*) malloc(sizeof(bnode));

  //printf("in the insert method\n"); 
  if ((*hnode1)->bnode == NULL) {
    left->data = (*hnode1)->data; 
    left->word = (*hnode1)->word; 
    left->huffmanCode = NULL;
  } else {
    left = (*hnode1)->bnode;
    left->data = (*hnode1)->data;
    left->word = (*hnode1)->word;
    left->huffmanCode = NULL;
  }
  // populate right node with hnode2's data
  if ((*hnode2)->bnode == NULL) {
    right->data = (*hnode2)->data; 
    right->word = (*hnode2)->word;
    right->huffmanCode = NULL;
  } else {
    right = (*hnode2)->bnode;
    right->data = (*hnode2)->data;
    right->word = (*hnode2)->word;
    right->huffmanCode = NULL;
  }
  // populate root with the data from left and right
  root->data = left->data + right->data;
  root->left = left;
  root->right = right;

  root->word = NULL; 
  root->huffmanCode = NULL; 
  //printf("root->data = %d\n", root->data); 
  return root;
}


// create huffman, takes in a minheap and returns the huffman tree
bnode* createHuffmanTree(hnode** array) {
  while (minheapsize > 1) {
    // take two lowest values
    hnode* first = (hnode*) malloc(sizeof(hnode)); 
    first = array[0];
    array = deletehnode(array, 0);
    
    hnode* second = (hnode*) malloc(sizeof(hnode)); 
    second = array[0];
    array = deletehnode(array, 0);
    
    // how create a bnode
    bnode* current = (bnode*) malloc(sizeof(bnode));
    current = insertBnode(&first, &second);
    // now create an hnode that will be inserted into the minheap, with the current in it
    hnode* toinsert = (hnode*) malloc(sizeof(hnode));
    toinsert->bnode = current;
    toinsert->data = current->data;
    toinsert->word = current->word;
    // now insert it into the array
    array = inserthnode (array, toinsert);
  }
  // now return the root
  bnode* root = (bnode*) malloc(sizeof(bnode));
  root = array[minheapsize-1]->bnode; // should be 0
  return root;
}

// calculates the huffman code of a bnode tree and returns void
// the huffman code will be a string that is generated from an array of 0s and 1s
void calculateHuffmanCode (bnode* root, int* codes, int index) {
  // if the left subtree is not NULL
  if (root->left != NULL) {
    codes[index] = 0; 
    calculateHuffmanCode (root->left, codes, index + 1);  
  } // if the right subtree is not NULL 
  if (root->right != NULL) {
    codes[index] = 1; 
    calculateHuffmanCode(root->right, codes, index + 1); 
  } // if it is a leaf node (a node with a token 
  if (root->left == NULL && root->right == NULL) {
    // create a string of the codes values 
    int i; 
    char* encodedString = (char*) malloc(sizeof(char) * (index+1)); 
    i = 0; 
    while (i < index) {
      char c = codes[i] + '0'; 
      encodedString[i] = c; 
      ++i; 
    }
    encodedString[index] = '\0'; 
    // now assign it to the node
    root->huffmanCode = encodedString; 
  }
}

/////////////////////////////////////////////////////
///////////// Create Codebook Function //////////////
/////////////////////////////////////////////////////   
/*
void createCodebook() {


}
*/

// don't know what to return so just doing void
// takes in a bnode* and a file called codebook 
void createCodebook (bnode* root, char* codebook) {
  // traverse the left subtree
  if (root->left != NULL) {
    createCodebook(root->left, codebook); 
  }
  // traverse the right subtree
  if (root->right != NULL) {
    createCodebook(root->right, codebook); 
  }
  // if it is a leaf node, left and right are null
  if (root->left == NULL && root->right == NULL) {
    // fd is our file pointer (output of the open) 
    int fd = open (codebook, O_CREAT| O_APPEND | O_RDWR, 0777); 
    if (fd == -1) {
      printf("error in opening the HuffmanCodebook\n"); 
      return; 
    }
    // now write the proper outputs to the CODEBOOK
    int code = write (fd, root->huffmanCode, strlen(root->huffmanCode)); 
    if (code == -1) {
      printf("error writing %s\n", root->huffmanCode); 
    }
    // now the tab
    int tab = write (fd, "\t", strlen("\t")); 
    if (tab == -1) {
      printf("error writing %s\n", "\\t"); 
    }
    // now the word
    int str = write (fd, root->word, strlen(root->word));
    if (str == -1) {
      printf("error writing %s\n", root->word);
    }
    // now the new line
    int newline = write (fd, "\n", strlen("\n"));
    if (newline == -1) {
      printf("error writing %s\n", "\\n");
    }
    // close file to finish
    close(fd); 
  }
}

// traverses huff tree and returns the node that matches the token 
bnode* traverseHuffTree (bnode* root, char* token) {
  // create a temp ptr
  bnode* ptr; 
  // start by checking the root
  if (root != NULL) {
    char* word = root->word;
    // if the lengths are equal 
    if ((root->word != NULL) && (strlen(word) == strlen(token))) { // if they are equal 
      int b; 
      for (b = 0; b < strlen(token); b++) {
	char wb = word[b]; 
	char tb = token[b]; 
	if (wb == tb) {
	  continue; 
	} else {
	  break; 
	}
      } 
      // they are equal, so return 
      if (b == strlen(token)) {
	return root; 
      } 
    } else {
      // check left subtree, if null check right
      ptr = traverseHuffTree(root->left, token);
      if (ptr == NULL) {
	ptr = traverseHuffTree(root->right, token); 
      }
      return ptr; 
    }
  } else {
    // this means the root is null and we havent found it yet
    return NULL; 
  }
}


// (endocer) takes in a token that was read from CODEBOOK and finds node in the huffman tree -> huffcode
// this function takes the root and 2 strings (the current token and the filename) 
//-> filename will need to be converted to .hcz) 

void encoder (bnode** root, char* token, char* filename) {
  // create the filename
  char* newfilename = NULL; 
  int filelen = strlen(filename)+5;
  newfilename = (char*) malloc(sizeof(char) * (strlen(filename)+5));
  int b = 0; 
  for (b = 0; b < strlen(filename); b++) {
    char a = filename[b]; 
    newfilename[b] = a; 
  }
  
  newfilename[b] = '.';
  newfilename[b+1] = 'h';
  newfilename[b+2] = 'c';
  newfilename[b+3] = 'z';
  newfilename[b+4] = '\0';
  b = 0; 
  
  // now search through the tree for the token 
  char* enc = get_encoding_from_token(token, root);

  int fd = open (newfilename, O_CREAT | O_APPEND | O_RDWR, 0777); 
  if (fd == -1) {
    printf("Error in the open during encoding\n");
    return;
  }
  // now write the code into the compressed file 
  
  int writeInt = write (fd, enc, strlen(enc)); 
  if (writeInt == -1) {
    printf("Error in the write during encoding\n"); 
    return; 
  }
}

// (decoder) reads from the .hcz file and puts it into a STRING 
// this function takes the root and 2 strings (the file and the filename) and writes to the original file

void decoder (bnode** root, char* file, char* filename) {
    // create the filename
  
  char* decompressedName; // without the .hcz
    int filelen = strlen(filename)-4;
    decompressedName = (char*) malloc(sizeof(char) * (filelen));
    int b;
    for (b = 0; b < filelen; b++) {
        char a = filename[b];
        decompressedName[b] = a;
    }
    decompressedName[filelen] = '\0';
  
    // loop until end of file int d;
    int d = 0;
    char* huffcode = "";
    int hcounter = 1;
   
    
    while (d < strlen(file)) {
        char c = file[d];

        if (c == '1') {
            if (hcounter == 0) {
                char* huffcode = (char*) malloc(sizeof(char)*2);
                huffcode[0] = c;
                huffcode[1] = '\0';
                ++hcounter;
                //printf("Code1: %s\n", huffcode);
                char* token = get_token_from_encoding(huffcode, root);

                if(token){
                    char cd = redo_control_codes(token);
                    char* tobewritten = (char*) malloc(sizeof(char) * 2);
                    tobewritten[0] = cd;
                    tobewritten[1] = '\0';
                    if (cd) {
		        int fd = open (decompressedName, O_CREAT | O_APPEND | O_RDWR, 0777);
                        if (fd == -1) {
			  printf("Error in the open during decoding\n");
			  return;
                        }
                        int writeInt = write (fd, tobewritten, strlen(tobewritten));
                        if (writeInt == -1) {
                            printf("Error in the write during decoding\n");
                            return;
                        }
                        huffcode = "";
                        hcounter = 1;
                    } // end of c
                    else {
		     
                        int fd = open (decompressedName, O_CREAT | O_APPEND | O_RDWR, 0777);
                        if (fd == -1) {
                            printf("Error in the open during decoding\n");
                            return;
                        }
                        int writeInt = write (fd, token, strlen(token));
                        if (writeInt == -1) {
                            printf("Error in the write during decoding\n");
                            return;
                        }
                        huffcode = "";
                        hcounter = 1;
                    } // end of c
                } // end of token
            } else {
                char* temp = (char*) malloc(sizeof(char) * hcounter);
                strcpy (temp, huffcode);
                huffcode = (char*) malloc(sizeof(char)*(hcounter+1));
                strcpy( huffcode, temp);
                huffcode[hcounter-1] = '1';
                huffcode[hcounter] = '\0';
                ++hcounter;
               
                char* token = get_token_from_encoding(huffcode, root);
	
                if(token){
                    char cd = redo_control_codes(token);
                    char* tobewritten = (char*) malloc(sizeof(char) * 2);
                    tobewritten[0] = cd;
                    tobewritten[1] = '\0';
                    if (cd) {
                        int fd = open (decompressedName, O_CREAT | O_APPEND | O_RDWR, 0777);
                        if (fd == -1) {
                            printf("Error in the open during decoding\n");
                            return;
                        }
                        int writeInt = write (fd, tobewritten, strlen(tobewritten));
                        if (writeInt == -1) {
                            printf("Error in the write during decoding\n");
                            return;
                        }
                        huffcode = "";
                        hcounter =1;
                    } // if not c
                    else {
                        int fd = open (decompressedName, O_CREAT | O_APPEND | O_RDWR, 0777);
                        if (fd == -1) {
                            printf("Error in the open during decoding\n");
                            return;
                        }
                        int writeInt = write (fd, token, strlen(token));
                        if (writeInt == -1) {
                            printf("Error in the write during decoding\n");
                            return;
                        }
                        huffcode = "";
                        hcounter = 1;
                    } // end of c
		} //end of token
            }
        } else if (c == '0'){
                if (hcounter == 0) {
                    char* huffcode = (char*) malloc(sizeof(char)*2);
                    huffcode[0] = c;
                    huffcode[1] = '\0';
                    ++hcounter;
                    //printf("Code3: %s\n", huffcode);
                    char* token = get_token_from_encoding(huffcode, root);
		
                    if(token){
                        char cd = redo_control_codes(token);
                        char* tobewritten = (char*) malloc(sizeof(char) * 2);
                        tobewritten[0] = cd;
                        tobewritten[1] = '\0';
                        if (cd) {
                            int fd = open (decompressedName, O_CREAT | O_APPEND | O_RDWR, 0777);
                            if (fd == -1) {
                                printf("Error in the open during decoding\n");
                                return;
                            }
                            int writeInt = write (fd, tobewritten, strlen(tobewritten));
                            if (writeInt == -1) {
                                printf("Error in the write during decoding\n");
                                return;
                            }
                            huffcode = "";
                            hcounter = 1;
                        }
                        else {
                            int fd = open (decompressedName, O_CREAT | O_APPEND | O_RDWR, 0777);
                            if (fd == -1) {
                                printf("Error in the open during decoding\n");
                                return;
                            }
                            int writeInt = write (fd, token, strlen(token));
                            if (writeInt == -1) {
                                printf("Error in the write during decoding\n");
                                return;
                            }
                            huffcode = "";
                            hcounter = 1;
                        } // end of c
                    }// end token
                } else {
		  char* temp = (char*) malloc(sizeof(char) * hcounter);
                    strcpy (temp, huffcode);
                    huffcode = (char*) malloc(sizeof(char)*(hcounter+1));
                    strcpy( huffcode, temp);
                    huffcode[hcounter-1] = '0';
                    huffcode[hcounter] = '\0';
                    ++hcounter;
                    //printf("Code4: %s\n", huffcode);
                    char* token = get_token_from_encoding(huffcode, root);
		
                    if(token){
                        char cd = redo_control_codes(token);
                        char* tobewritten = (char*) malloc(sizeof(char) * 2);
                        tobewritten[0] = cd;
                        tobewritten[1] = '\0';
                        if (cd) {
                            int fd = open (decompressedName, O_CREAT | O_APPEND | O_RDWR, 0777);
                            if (fd == -1) {
                                printf("Error in the open during decoding\n");
                                return;
                            }
                            int writeInt = write (fd, tobewritten, strlen(tobewritten));
                            if (writeInt == -1) {
                                printf("Error in the write during decoding\n");
                                return;
                            }
                            huffcode = "";
                            hcounter = 1;
                        }
                        else {
                            int fd = open (decompressedName, O_CREAT | O_APPEND | O_RDWR, 0777);
                            if (fd == -1) {
                                printf("Error in the open during decoding\n");
                                return;
                            }
                            int writeInt = write (fd, token, strlen(token));
                            if (writeInt == -1) {
                                printf("Error in the write during decoding\n");
                                return;
                            }
                            huffcode = "";
                            hcounter = 1;
                        } // end of c
                    }// end of token
                }// end of little else
	} // --- big if
	// increment
	++d;
    } // while
}

// bnode insert for the tokenizer of the huffman codebook                
bnode* huffToBnodeInsert (bnode* root, bnode* insertnode) {
  insertnode->left = NULL; 
  insertnode->right = NULL; 
  // ptr to the root
  //bnode* ptr = root; 
  // if root == NULL                                                             
  if (root == NULL) { 
    root = insertnode; 
    return root;
  } else if (root->left == NULL) {
    root->left = insertnode; 
    return root;  
  } else if (root->right == NULL) {
    root->right = insertnode; 
    return root; 
  } 
  
  // if root isn't null, check the left, else check the right                    
  if (root->left != NULL) {
    root->left = huffToBnodeInsert (root->left, insertnode);
  } else if (root->right != NULL) {
      root->right = huffToBnodeInsert (root->right, insertnode);
  }
  return root; 
}

// parses the string of the file and inserts into a bnode tree              
bnode** hcb_to_array(char* filename){
  int fd = open(filename, O_RDONLY);
  if(fd == -1){
    printf("Unable to open file during conversion!\n");
  }
  int fileSize = lseek(fd, 0, SEEK_END);
  lseek(fd, 0, SEEK_SET);

  char* file = (char*) malloc((fileSize+1)*sizeof(char));
  file[fileSize] = '\0';
  int i = 0;
  char c;
  while(read(fd, &c, 1) == 1){
    file[i] = c;
    //printf("%c", c);
    ++i;
  }

  bnode** array = (bnode**) malloc(fileSize*sizeof(bnode*)); 
  int j = 0; 
  while(j < fileSize){
    array[j] = (bnode*) malloc(1*sizeof(bnode)); 
    ++j;
  }

  //parse string                                                     
  char* huffCode;
  char* token;
  int index = 0;
  int wordcount = 0;
  int wordstart = 0;
  int arrIndex = 0; 

  // while the index isnt larger than the file length                       
  while (index < fileSize) {
    // if we see a tab, create the huffCode word and set wordcount to 0        
    if (file[index] == '\t') {
      huffCode = (char*) malloc(sizeof(char) * (wordcount+1));
      int b;
      for (b = 0; b < wordcount; b++) {
        huffCode[b] = file[wordstart];
        ++wordstart;
      }
      huffCode[wordcount] = '\0';
      
      // increment counters                                               
      wordcount = 0;
      ++index;
    } // if we see a new line, create the token word and set wordcount to 0, then insert 
    else if (file[index] == '\n') {
      token = (char*) malloc(sizeof(char) * (wordcount+1));
      int b;
      for (b = 0; b < wordcount; b++) {
        token[b] = file[wordstart];
        ++wordstart;
      }
      token[wordcount] = '\0'; 
      // now, insert into the bnode tree                               
      bnode* insertnode = (bnode*) malloc(sizeof(bnode));
      insertnode->huffmanCode = huffCode;
      insertnode->word = token;
      // now insert it                                                                      
      array[arrIndex] = insertnode; 
      ++arrIndex;
      ++hcbarraysize;
      // increment counters                                                                 
      wordcount = 0;
      ++index;
    } // else increment the word counter and the index one                                  
    else {
      ++wordcount;
      ++index;
      if (wordcount == 1) {
        wordstart = index-1;
      }
    }
  } // end of while                                                                       
  return array;
}

char* get_encoding_from_token(char* token, bnode** arr){
  int i = 0; 
  while(i < hcbarraysize){
    if(strcmp(token, arr[i]->word) == 0){
      return arr[i]->huffmanCode;
    }
    else{
      ++i;
    }
  }
  //printf("Cannot find huffman code for token : %s\n", token);
  return NULL;
}

char* get_token_from_encoding(char* encoding, bnode** arr){
  int i = 0; 
  while(i < hcbarraysize){
    if (arr[i]->word == NULL) {
      return NULL; 
    }
    if(strcmp(encoding, arr[i]->huffmanCode) == 0){
      return arr[i]->word;
    }
    else{
      ++i;
    }
  }
  //printf("Unable to find token for given encoding: %s\n", encoding);
  return NULL;
}


// undo escape control characters so they can be printed to HuffmanCodebook easily 
char redo_control_codes(char* token){
    char c;
    if (strcmp(token, "\\a") == 0) {
        c = '\a';
    } else if (strcmp(token, "\\b") == 0) {
        c = '\b';
    } else if (strcmp(token, "\\f") == 0) {
        c = '\f';
    } else if (strcmp(token, "\\n") == 0) {
        c = '\n';
    } else if (strcmp(token, "\\r") == 0) {
        c = '\r';
    } else if (strcmp(token, "\\t") == 0) {
        c = '\t';
    } else if (strcmp(token, "\\v") == 0) {
        c = '\v';
    }
    return c;
}
    
    























