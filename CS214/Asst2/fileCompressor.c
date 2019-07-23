#include "filecompressor.h"

int main(int argc, char** argv){

    //build codebook 
    if(argc == 3 && strcmp("-b", argv[1]) == 0 && strcmp("-R", argv[2]) != 0){ 
      int r = 0; 
      AVLNode* root = NULL; 
      root = tokenize_file(argv[2], root); 
      build_codebook(root, argv[2], r);
    }
    //compress
    else if(argc == 4 && strcmp("-c", argv[1]) == 0 && strcmp("-R", argv[2]) != 0){
      int r = 0; 
        compress_file(argv[3], argv[2]); 
    }
    //decompress 
    else if(argc == 4 && strcmp("-d", argv[1]) == 0 && strcmp("-R", argv[2]) != 0){
      decompress_file(argv[3], argv[2]);
    }
    else if ((argc == 4) && (strcmp(argv[1], "-R") == 0 || strcmp(argv[2], "-R") == 0) && (strcmp(argv[1], "-b") == 0 || strcmp(argv[2], "-b") == 0)) {
      int r = 1;
      AVLNode* root = NULL;
      recursive_index_files(argv[3], &root, r);
      build_codebook(root, argv[3], r);
    }
    //recursively 
    else if(argc == 5){
        //build
        if((strcmp(argv[1], "-R") == 0 || strcmp(argv[2], "-R") == 0) && (strcmp(argv[1], "-b") == 0 || strcmp(argv[2], "-b") == 0)){
          int r = 1;  
	  AVLNode* root = NULL; 
	  recursive_index_files(argv[3], &root, r); 
	  build_codebook(root, argv[3], r);
        }
        //compress
        else if((strcmp(argv[1], "-R") == 0 || strcmp(argv[2], "-R") == 0) && (strcmp(argv[1], "-c") == 0 || strcmp(argv[2], "-c") == 0)){
            recursive_compress(argv[3], argv[4]); 
        }
        //decompress 
        else if((strcmp(argv[1], "-R") == 0 || strcmp(argv[2], "-R") == 0) && (strcmp(argv[1], "-d") == 0 || strcmp(argv[2], "-d") == 0)){
            recursive_decompress(argv[3], argv[4]);
        }
        else{
            printf("Unable to perform operation. Please check input parameters!\n"); 
            return 0;
        }
    }
    
    
}

void recursive_decompress(char* path, char* codebook){
    DIR* directory = opendir(path); 
    if(!directory){
        printf("Unable to open directory given by %s\n", path); 
        return;
    }
    struct dirent* item; 
    while((item = readdir(directory))){
        //ignore current and parent directories 
        if(strcmp(item->d_name, ".") == 0 || strcmp(item->d_name, "..") == 0){
            continue; 
        }
        //its a regular file -> insert frequencies into avl tree 
        else if(item->d_type == DT_REG){
	  // build string of path that we want                                                                   
          int lenFile = strlen(item->d_name);
	  int lenPath = strlen(path);
          char* finalString = (char*) malloc(sizeof(char) * (lenFile+lenPath+2));
          // path is our current string, we want path to be added to a string, which will then be added with the item->d_name string
	  int i;
	  for (i = 0; i < lenPath; i++) {
	    char c = path[i];
	    finalString[i] = c;
	  }
	  finalString[lenPath] = '/';
	  int d = 0;
	  ++i;
	  while (i < (lenPath+lenFile+1)) {
	    char cc = item->d_name[d];
	    finalString[i] = cc;
	    ++i;
	    ++d;
	  }
	  i = 0;
	  d = 0;
	  // do other copy now                                                                                   
	  finalString[(lenPath+lenFile+1)] = '\0';
	  // now decompress 
	  decompress_file(codebook, finalString);
        }
        //its a directory -> make recursive call 
        else if(item->d_type == DT_DIR){
            char* newpath = create_path(path, item->d_name); 
            recursive_decompress(newpath, codebook);
        }
    }
}

void recursive_compress(char* path, char* codebook){
    DIR* directory = opendir(path); 
    if(!directory){
        printf("Unable to open directory given by %s\n", path); 
        return;
    }
    struct dirent* item; 
    while((item = readdir(directory))){
        //ignore current and parent directories 
        if(strcmp(item->d_name, ".") == 0 || strcmp(item->d_name, "..") == 0){
            continue; 
        }
        //its a regular file -> insert frequencies into avl tree 
        else if(item->d_type == DT_REG){
	  // build string of path that we want                                                                                                   
          int lenFile = strlen(item->d_name);
          int lenPath = strlen(path);
          char* finalString = (char*) malloc(sizeof(char) * (lenFile+lenPath+2));
          // path is our current string, we want path to be added to a string, which will then be added with the item->d_name string 
          int i;
          for (i = 0; i < lenPath; i++) {
            char c = path[i];
            finalString[i] = c;
          }
          finalString[lenPath] = '/';
          int d = 0;
          ++i;
          while (i < (lenPath+lenFile+1)) {
            char cc = item->d_name[d];
            finalString[i] = cc;
            ++i;
            ++d;
          }
          i = 0;
          d = 0;
          // do other copy now            
          finalString[(lenPath+lenFile+1)] = '\0';
	  // now compress
	  compress_file(codebook, finalString);
        }
        //its a directory -> make recursive call 
        else if(item->d_type == DT_DIR){
            char* newpath = create_path(path, item->d_name); 
            recursive_compress(newpath, codebook);
        }
    }

}

void recursive_index_files(char* path, AVLNode** freqs, int recur){
    DIR* directory = opendir(path); 
    if(!directory){
        printf("Unable to open directory given by %s\n", path); 
        return;
    }
    struct dirent* item; 
    while((item = readdir(directory))){
        //ignore current and parent directories 
        if(strcmp(item->d_name, ".") == 0 || strcmp(item->d_name, "..") == 0){
            continue; 
        }
        //its a regular file -> insert frequencies into avl tree 
        else if((item->d_type == DT_REG) && (recur == 1)){
            // build string of path that we want
            int lenFile = strlen(item->d_name);
            int lenPath = strlen(path);
            char* finalString = (char*) malloc(sizeof(char) * (lenFile+lenPath+2));
            // path is our current string, we want path to be added to a string, which will then be added with th item->d_name string
            int i;
            for (i = 0; i < lenPath; i++) {
                char c = path[i];
                finalString[i] = c;
            }
            finalString[lenPath] = '/';
            int d = 0;
            ++i;
            while (i < (lenPath+lenFile+1)) {
                char cc = item->d_name[d];
                finalString[i] = cc;
                ++i;
                ++d;
            }
            i = 0;
            d = 0;
            // do other copy now
            finalString[(lenPath+lenFile+1)] = '\0';
            (*freqs) = tokenize_file(finalString, (*freqs));
        }
        else if (item->d_type == DT_REG) {
            (*freqs) = tokenize_file(item->d_name, (*freqs));
        }
        //its a directory -> make recursive call
        else if(item->d_type == DT_DIR){
            char* newpath = create_path(path, item->d_name); 
	    recursive_index_files(newpath, freqs, recur);
        }
    }
}

void build_codebook(AVLNode* root, char* path, int r){
    // get the size of the AVL
    int avlSize = get_avl_size(root);
    // now create the minheap
    hnode** minheap = initializeheap(avlSize);
    // put the avl to an array, to be transfered to the minheap now
    AVLNode** arr = (AVLNode**) malloc(avlSize*sizeof(AVLNode*));
    int i = 0;
    while(i < avlSize){
        arr[i] = (AVLNode*) malloc(1*sizeof(AVLNode));
        ++i;
    }
    int index = 0;
    avl_to_array(root, &arr, &index);
    // finally, array --> minheap
    array_to_minheap(arr, &minheap, avlSize);
    // now create the huffman tree
    bnode* bnodetree = (bnode*) malloc(sizeof(bnode));
    bnodetree = createHuffmanTree(minheap);
    // calculates huffman code for each node in tree                                                             
    int* heaparray = (int*) malloc(sizeof(int) * avlSize);
    calculateHuffmanCode(bnodetree, heaparray, 0);
    // creates HUFFMAN CODEBOOK
    // create string for the codebook
   
    if (r == 1) {
      int lenPath = strlen(path); 
      char* huff = (char*) malloc(sizeof(char) * 16); 
      strcpy(huff, "HuffmanCodebook"); 
      int lenHuff = strlen(huff); 
      char* codebookString = (char*) malloc(sizeof(char) * (lenPath+lenHuff+2));
      // now do the swapping 
      int j;
      for (j = 0; j < lenPath; j++) {
	char c = path[j];
	codebookString[j] = c;
      }
     
      codebookString[lenPath] = '/';
      int d = 0;
      ++j;
     
      while (j < (lenPath+lenHuff+1)) {
	char cc = huff[d];
	codebookString[j] = cc;
	++j;
	++d;
      }
      j = 0;
      d = 0;
      codebookString[(lenPath+lenHuff+1)] = '\0'; 
      // now call createCodebook
      createCodebook(bnodetree, codebookString);
      // now call createCodebook
    } else if (r == 0) {
      createCodebook(bnodetree, "HuffmanCodebook");
    }
}

//takes current path and appends name of directory/file to end for recursive calls and returns new path
char* create_path(char* currentPath, char* name){
    //calculate new path length
    int newPathLength = strlen(currentPath) + strlen(name) + 2;
    char* newPath = (char*) malloc(newPathLength*sizeof(char)); 
    if(!newPath){
        printf("Unable to allocate memory to create new path!\n"); 
        return NULL; 
    }
    //copy old path
    strcpy(newPath, currentPath); 
    //add '/'
    strcat(newPath, "/"); 
    //add file name
    strcat(newPath, name);
    return newPath;
}

//escape control characters so they can be printed to HuffmanCodebook easily 
char* escape_control_code(char c){
    char* result = (char*) malloc(4*sizeof(char)); 
    switch(c){
        case '\a':
            strcpy(result, "\\a"); 
            break;
        case '\b':
            strcpy(result, "\\b"); 
            break;
        case '\f':
            strcpy(result, "\\f"); 
            break;
        case '\n':
            strcpy(result, "\\n"); 
            break;
        case '\r':
            strcpy(result, "\\r"); 
            break;
        case '\t':
            strcpy(result, "\\t"); 
            break;
        case '\v':
            strcpy(result, "\\v"); 
            break;
        default: 
            break;
    }
    return result;
}

//tokenizes file given current path, and file name 
AVLNode* tokenize_file(char* filename, AVLNode* root){
  int fd = open(filename, O_RDONLY); 
    if(fd == -1){
        printf("Unable to open file to tokenize!\n"); 
        return NULL; 
    }
    //get size of file
    int fileSize = lseek(fd, (off_t)0, SEEK_END); 
    lseek(fd, (off_t)0, SEEK_SET); 
    //copy file into string for tokenizing purposes 
    char* fileContents = (char*) malloc((fileSize+1)*sizeof(char)); 
    if(!fileContents){
        printf("Unable to allocate memory to store file contents!\n"); 
        return NULL;
    }
    char currentChar;
    int i = 0; 
    while(read(fd, &currentChar, 1) == 1){
        fileContents[i] = currentChar; 
        ++i;
    }
    fileContents[fileSize] = '\0';
    //tokenize file and insert into avl tree 
    int index = 0; 
    int tokenLength = 0; 
    while(index < fileSize){
        //current character is not a space and not a control character -> go to next character 
        if(fileContents[index] != ' ' && iscntrl(fileContents[index]) == 0){
            ++index; 
            ++tokenLength;
        }
        //current character is a space 
        else if(fileContents[index] == ' '){
            //token length is > 0 -> insert token, insert space, reset token length, go to next character 
            if(tokenLength > 0){
                char* token = (char*) malloc((tokenLength+1)*sizeof(char)); 
                strncpy(token, fileContents+index-tokenLength, tokenLength); 
                root = insert_avl_node(root, token); 
                root = insert_avl_node(root, " "); 
                tokenLength = 0; 
                ++index;
            }
            //token length is 0 -> insert space, go to next character 
            else
            {
                root = insert_avl_node(root, " "); 
                ++index;
            }
        }
        //current character is a control character
        else if(iscntrl(fileContents[index]) > 0){
            //token length is > 0 -> insert token, escape control character, insert control character, reset token length, go to next character 
            if(tokenLength > 0){
                char* token = (char*) malloc((tokenLength+1)*sizeof(char)); 
                strncpy(token, fileContents+index-tokenLength, tokenLength); 
                root = insert_avl_node(root, token); 
                root = insert_avl_node(root, escape_control_code(fileContents[index])); 
                tokenLength = 0; 
                ++index;
            }
            //token length is 0 -> escape control character, insert control character, go to next character 
            else{
                root = insert_avl_node(root, escape_control_code(fileContents[index])); 
                ++index;
            }
        }
    }
    //reach end of file and still have to insert last token 
    if(tokenLength > 0){
        char* token = (char*) malloc((tokenLength+1)*sizeof(char)); 
        strncpy(token, fileContents+index-tokenLength, tokenLength); 
        root = insert_avl_node(root, token); 
        tokenLength = 0;
    }
    close(fd); 
    free(fileContents);
    return root; 
}

//prints the contents of the heap
void print_heap(hnode** heap, int size){
    int i = 0; 
    while(i < size){
        printf("%s:%d\n", heap[i]->word, heap[i]->data); 
        ++i;
    }
}

//traverses the AVL tree and copies the nodes into an array 
void avl_to_array(AVLNode* root, AVLNode*** arr, int* index){
    if(!root) return;
    (*arr)[(*index)++] = root; 
    avl_to_array(root->left, arr, index);
    avl_to_array(root->right, arr, index);
}

//iterates through the array and inserts the frequencies and tokens into the minheap
void array_to_minheap(AVLNode** arr, hnode*** heap, int avlSize){
    int i = 0; 
    while(i < avlSize){
        (*heap) = inserthnode((*heap), createhnode(arr[i]->freq, arr[i]->token)); 
        ++i;
    }
}

void parse_original_file(char* filename, bnode** hcbarray){
     int fd = open(filename, O_RDONLY); 
    if(fd == -1){
        printf("Unable to open file to tokenize!\n");  
    }
    //get size of file
    int fileSize = lseek(fd, (off_t)0, SEEK_END); 
    lseek(fd, (off_t)0, SEEK_SET); 
    //copy file into string for tokenizing purposes 
    char* fileContents = (char*) malloc((fileSize+1)*sizeof(char)); 
    if(!fileContents){
        printf("Unable to allocate memory to store file contents!\n");
    }
    char currentChar;
    int i = 0; 
    while(read(fd, &currentChar, 1) == 1){
        fileContents[i] = currentChar; 
        ++i;
    }
    fileContents[fileSize] = '\0';
    //tokenize file and insert into avl tree 
    int index = 0; 
    int tokenLength = 0; 
    while(index < fileSize){
        //current character is not a space and not a control character -> go to next character 
        if(fileContents[index] != ' ' && iscntrl(fileContents[index]) == 0){
            ++index; 
            ++tokenLength;
        }
        //current character is a space 
        else if(fileContents[index] == ' '){
            //token length is > 0 -> insert token, insert space, reset token length, go to next character 
            if(tokenLength > 0){
                char* token = (char*) malloc((tokenLength+1)*sizeof(char)); 
                strncpy(token, fileContents+index-tokenLength, tokenLength); 
                encoder(hcbarray, token, filename); 
                encoder(hcbarray, " ", filename);
                tokenLength = 0; 
                ++index;
            }
            //token length is 0 -> insert space, go to next character 
            else
            {
                encoder(hcbarray, " ", filename); 
                ++index;
            }
        }
        //current character is a control character
        else if(iscntrl(fileContents[index]) > 0){
            //token length is > 0 -> insert token, escape control character, insert control character, reset token length, go to next character 
            if(tokenLength > 0){
                char* token = (char*) malloc((tokenLength+1)*sizeof(char)); 
                strncpy(token, fileContents+index-tokenLength, tokenLength); 
                encoder(hcbarray, token, filename);
                encoder(hcbarray, escape_control_code(fileContents[index]), filename); 
                tokenLength = 0; 
                ++index;
            }
            //token length is 0 -> escape control character, insert control character, go to next character 
            else{
                encoder(hcbarray, escape_control_code(fileContents[index]), filename);
                ++index;
            }
        }
    }
    //reach end of file and still have to insert last token 
    if(tokenLength > 0){
        char* token = (char*) malloc((tokenLength+1)*sizeof(char)); 
        strncpy(token, fileContents+index-tokenLength, tokenLength); 
        encoder(hcbarray, token, filename);
        tokenLength = 0;
    }
    close(fd); 
    free(fileContents);
}

void compress_file(char* codebookFilename, char* filename){
    int fd = open(codebookFilename, O_RDONLY);
    if(fd == -1){
        printf("Unable to open file during conversion!\n");
        return;
    }
    int fileSize = lseek(fd, 0, SEEK_END);
    lseek(fd, 0, SEEK_SET);
    char* file = (char*) malloc((fileSize+1)*sizeof(char));
    file[fileSize] = '\0';
    int i = 0;
    char c;
    while(read(fd, &c, 1) == 1){
        file[i] = c;
        ++i;
    }
    bnode** node = hcb_to_array(codebookFilename);
    parse_original_file(filename, node);
}

void decompress_file(char* codebook, char* compressedFile){
    int fd = open(codebook, O_RDONLY); 
    if(fd == -1){
        printf("Unable to open file during decompression!\n"); 
        return;
    }
    int fileSize = lseek(fd, 0, SEEK_END);
    lseek(fd, 0, SEEK_SET); 

    char* file = (char*) malloc((fileSize+1)*sizeof(char)); 
    file[fileSize] = '\0';
    int i = 0;
    char c;
    while(read(fd, &c, 1) == 1){
        file[i] = c;
        ++i;
    }
    bnode** arr = hcb_to_array(codebook); 

    // other file 
    int fd2 = open(compressedFile, O_RDONLY);
    if(fd2 == -1){
      printf("Unable to open file during decompression!\n");
      return;
    }
    int fileSize2 = lseek(fd2, 0, SEEK_END);
    lseek(fd2, 0, SEEK_SET);

    char* file2 = (char*) malloc((fileSize2+1)*sizeof(char));
    file2[fileSize2] = '\0';
    int i2 = 0;
    char c2;
    while(read(fd2, &c2, 1) == 1){
      file2[i2] = c2;
      ++i2;
    }
    
    // decode it
    decoder(arr, file2, compressedFile);
}
