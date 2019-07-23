#include "WTF.h"

/*CONFIGURE FUNCTIONS*/
int check_configure(char* path){
    //open directory pointed to by argument 
    DIR* directory = opendir(path); 
    //check if directory was opened successfully 
    if(!directory){
        perror("Failure opening directory while checking for configure call. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    
    //represents item within directory (file or subdirectory)
    struct dirent* item; 
    //loop through items in directory 
    while(item = readdir(directory)){
        //check if current item's name is .configure 
        if(strcmp(item->d_name, ".configure") == 0){
            return 1; 
        }
    }

    //.configure file not found -> configure command not called 
    return 0;
}

void configure(char* IP, char* PORT){
    printf("Preparing to configure IP address and PORT number...\n"); 
    //check to see if configure has been called already 
    if(check_configure(".") == 1){
        printf("Configure has already been called!\n"); 
        return;
    }
    //configure has not been called yet 
    else{
        //create file
        FILE* file = fopen(".configure", "a+");
        //write IP to file 
        int writeIP = fwrite(IP, sizeof(char), strlen(IP), file); 
        if(writeIP != strlen(IP)){
            perror("Error writing IP address to .configure file. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        //write new line to file 
        int writeNL = fwrite("\n", sizeof(char), 1, file); 
        if(writeNL != 1){
            perror("Error writing new line to .configure file. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        //write PORT number to file 
        int writePORT = fwrite(PORT, sizeof(char), strlen(PORT), file); 
        if(writePORT != strlen(PORT)){
            perror("Error writing PORT number to .configure file. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        printf("Successfully configure IP address and PORT number!\n");
        
        //close file 
        fclose(file);
    }
}

char* get_IP_from_config_file(){
    //open file 
    FILE* file = fopen(".configure", "r"); 
    //check that file was opened successfully 
    if(!file){
        perror("Unable to open .configure file while retrieving IP address. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //create string to hold IP
    char* line = (char*) malloc(64*sizeof(char)); 
    if(!line){
        perror("Failure allocating memory while retrieving IP address from .configure file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    fgets(line, 64, file);
    fclose(file);
    return line;
}

char* get_PORT_from_config_file(){
    //open file 
    FILE* file = fopen(".configure", "r"); 
    //check that file was opened successfully 
    if(!file){
        perror("Unable to open .configure file while retrieving PORT number. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //create string to hold PORT
    char* line = (char*) malloc(64*sizeof(char)); 
    if(!line){
        perror("Failure allocating memory while retrieving PORT number from .configure file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    fgets(line, 64, file);
    fgets(line, 64, file);
    fclose(file);
    return line;
}

/*MANIFEST FILE STRUCT FUNCTIONS*/
ManifestFileEntry* create_manifestfile_entry(int fileVersion, int seenByServer, char* filePath, char* hash){
    //malloc for space
    ManifestFileEntry* entry = (ManifestFileEntry*) malloc(1*sizeof(ManifestFileEntry));
    //check malloc call
    if(!entry){
        perror("Failed to allocate memory while creating ManifestFileEntry. Exiting...\n");
        exit(EXIT_FAILURE);
    }
    //set values
    entry->fileVersion = fileVersion;
    entry->seenByServer = seenByServer;
    entry->filePath = filePath;
    entry->hash = hash;
    entry->next = NULL;
    //return
    return entry;
}

ManifestFile* create_manifestfile(int version){
    //malloc for space
    ManifestFile* mf = (ManifestFile*) malloc(1*sizeof(ManifestFile));
    //check the malloc call
    if(!mf){
        perror("Failed to allocate memory while creating ManifestFile. Exiting...\n");
        exit(EXIT_FAILURE);
    }
    //set values
    mf->version = version;
    mf->entries = NULL;
    return mf;
}

void insert_manifestfile_entry(ManifestFileEntry** entries, ManifestFileEntry* entry){
    //check if list is NULL
    if((*entries) == NULL){
        (*entries) = entry;
        return;
    }
    //pointer to navigate
    ManifestFileEntry* ptr = (*entries);
    //loop until last entry in LL
    while(ptr->next != NULL){
        ptr = ptr->next;
    }
    //set ptr->next to new entry to add
    ptr->next = entry;
    return;
}


ManifestFile* create_manifest_struct_from_file(char* manifest, int liveHashes){
    //open manifest file
    FILE* mf = fopen(manifest, "r");
    //check that file was opened properly
    if(!mf){
        perror("Failed to allocate memory while creating ManifestFile* struct from .Manifest. Exiting...\n");
        exit(EXIT_FAILURE);
    }
    char line[1024];
    //bzero(line, sizeof(line));
    //get manifest file version
    fgets(line, sizeof(line), mf);
    //create manifest file struct
    ManifestFile* file = create_manifestfile((line[0] -'0'));
    //loop through rest of file and create entries
    while(fgets(line, sizeof(line), mf)){
        //get all aspects of entry in .Manifest
        int fileVersion = get_fileversion_from_entry(line);
        int seenByServer = seen_by_server(line);
        char* filePath = get_filepath_from_entry(line);
        char* hash;
        if(liveHashes == 1){
            hash = get_hash(filePath);
        }
        else{
            hash = get_hash_from_entry(line);
            //printf("hash = %s\n", hash);
        }
        //create entry struct
        ManifestFileEntry* entry = create_manifestfile_entry(fileVersion, seenByServer, filePath, hash);
        //printf("entry -> hash = %s\n", entry->hash);
        //insert entry struct
        insert_manifestfile_entry(&(file->entries), entry);
        //bzero(line, sizeof(line));
    }
    //return file struct
    return file;
}


int get_fileversion_from_entry(char* entry){
    char fileVersion = entry[0];
    int fVersion = fileVersion - '0';
    return fVersion;
}

int seen_by_server(char* entry){
    int sbs = !(entry[2] == '@');
    return sbs;
}

char* get_filepath_from_entry(char* entry){
    //check if there is a @ in the manifest entry
    int seenByServer = seen_by_server(entry);
    int startingIndex;
    if(seenByServer){
        //<fileversion> <filepath> <hash>
        startingIndex = 2;
    }
    else{
        //<fileversion> @ <filepath> <hash>
        startingIndex = 4;
    }
    //get length of filepath
    int length = 0;
    int i = startingIndex;
    while(i < strlen(entry)){
        if(entry[i] == ' '){
            break;
        }
        else{
            ++i;
            ++length;
        }
    }
    //malloc for filepath
    char* path = (char*) malloc((length+1)*sizeof(char));
    //chck for good malloc call
    if(!path){
        perror("Failed to allocate memory while attempting to extract filepath from .Manifest file entry\n");
        exit(EXIT_FAILURE);
    }
    //copy filepath
    strncpy(path, entry+startingIndex, length);
    //return path
    return path;
}

char* get_hash_from_entry(char* entry){
    int seenByServer = seen_by_server(entry);
    int filePathStartingIndex;
    //THERE IS NO @
    if(seenByServer){
        filePathStartingIndex = 2;
    }
    //THERE IS A @
    else{
        filePathStartingIndex = 4;
    }
    return (strchr(entry+filePathStartingIndex, ' ')+1);
}




/*HELPER FUNCTIONS*/
char* build_command(int argc, char** argv){
    int length; 
    char* command; 
    //3 arguments into input : ./WTF <command> <projectname>
    if(argc == 3){
        //get length of command string
        length = strlen(argv[0]) + strlen(argv[1]) + strlen(argv[2]) + 3; 
        //malloc for command string 
        command = (char*) malloc(length*sizeof(char)); 
        //check for good malloc call 
        if(!command){
            perror("Failed to allocate memory while building command from client. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        //copy in ./WTF
        strcpy(command, argv[0]); 
        //copy in space 
        strcat(command, " "); 
        //copy in <command>
        strcat(command, argv[1]); 
        //copy in space 
        strcat(command, " "); 
        //copy in project name 
        strcat(command, argv[2]); 
        //return command string 
        return command; 
    }
    //4 arguments into input : ./WTF <command> <projectname> <path>
    else if(argc == 4){
        //get length of command string 
        length = strlen(argv[0]) + strlen(argv[1]) + strlen(argv[2]) + strlen(argv[3]) + 4;
        //malloc for command string 
        command = (char*) malloc(length*sizeof(char));
        //check for good malloc call 
        if(!command){
            perror("Failed to allocate memory while building command from client. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        //copy in ./WTF
        strcpy(command, argv[0]); 
        //copy in space 
        strcat(command, " "); 
        //copy in <command>
        strcat(command, argv[1]); 
        //copy in space 
        strcat(command, " "); 
        //copy in project name 
        strcat(command, argv[2]);
        //copy in space
        strcat(command, " "); 
        //copy in path 
        strcat(command, argv[3]);
        //return command 
        return command;  
    }
    else{
        perror("Invalid amount of arguments given. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
}

void send_string_size(int fd, int size){
    //cast size as int32_t to ensure 4 bytes on every machine 
    int32_t stringSize = htonl((int32_t)size);
    //write the size over the socket 
    int sent = write(fd, &stringSize, sizeof(stringSize));
    //check for error 
    if(sent < 0){
        perror("Failed to send string size to server. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
}

void send_string(char* string, int size, int fd){
    //bytes left to send 
    int bytesLeft = size; 
    int bytesSent = 0; 
    //loop while there are still bytesLeft 
    do{
        bytesSent = write(fd, string, bytesLeft); 
        if(bytesSent < 0){
            perror("Failed to write string over socket to the server. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        else{
            //subtract bytes written from total 
            bytesLeft -= bytesSent; 
            //move pointer in string for writing
            string += bytesSent;
        }
    }while(bytesLeft > 0);
}

char* read_string(int fd, int size){
    //malloc to store string 
    char* string = (char*) malloc((size+1)*sizeof(char)); 
    //check for good malloc call 
    if(!string){
        perror("Failed to allocate memory while reading string from client. Exiting...\n");
        exit(EXIT_FAILURE);
    }
    //total bytes to read
    int bytesLeft = size; 
    int bytesRead = 0; 
    //loop while there are bytes left to read 
    do{
        bytesRead = read(fd, string, bytesLeft); 
        if(bytesRead < 0){
            perror("Failed to read string over socket from client. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        else{
            //subtract bytes read from total
            bytesLeft -= bytesRead; 
        }
    }while(bytesLeft > 0);
    string[size] = '\0';
    //return string 
    return string;
}

int get_file_size(char* filename){
    //open file
    int fd = open(filename, O_RDONLY); 
    //check if file was opened 
    if(fd < 0){
        perror("Failed to open file while getting file size. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    off_t size = lseek(fd, (off_t)0, SEEK_END); 
    lseek(fd, (off_t)0, SEEK_SET); 
    close(fd); 
    return ((int)size);
}

int get_string_size(int fd){
    int size = 0; 
    //read size into variable 
    int status = read(fd, &size, sizeof(size)); 
    //check for error
    if(status < 0){
        perror("Failed to get string size from client. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    return ntohl(size);
}

char* get_hash(char* filename){
    //int fd = open(path, O_CREAT | O_RDWR | O_APPEND, 0777);                   
    FILE* currentfile = fopen(filename, "rb");
    if(!currentfile){ 
        perror("File passed does not exist. Unable to calculate hash for file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    // set up variables to be used in hash process                              
    unsigned char hashcode[SHA256_DIGEST_LENGTH];
    char* output = (char*) malloc(sizeof(char) * 65);
    SHA256_CTX sha;
    // initialize sha struct                                                    
    SHA256_Init(&sha);
    // now createbuffer                                                         
    const int sizeOfBuffer = 32768;
    char* readbuffer = (char*) malloc(sizeof(char) * sizeOfBuffer);
    if(!readbuffer){
        perror("Failed to allocate memory while attempting to hash file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //now read in bytes                                                        
    int read = 0;
    while((read = fread(readbuffer, 1, sizeOfBuffer, currentfile))){
        SHA256_Update(&sha, readbuffer, read);
    }
    SHA256_Final(hashcode, &sha);
    //now hash the string                                                      
    int i = 0;
    while(i < SHA256_DIGEST_LENGTH){
        sprintf(output + (i*2), "%02x", hashcode[i]);
        ++i;
    }
    output[64] = 0;
    // close up shop before we finish                                           
    fclose(currentfile);
    free(readbuffer);
    return output;
}

int get_digits_in_version_number(int version){
    //total number of digits
    int total = 0;
    //loop while number is not 0
    while(version > 0){
        //move to left by one digit
        version /= 10; 
        //update total
        ++total;
    }
    return total;
}

char* get_projectname_from_command(char* command){
    //remove ./WTF
    char* startingAtCommand = strchr(command, ' ')+1; 
    //remove command 
    char* startingAtProj = strchr(startingAtCommand, ' ')+1;
    //count length of project
    int length = 0; 
    int i = 0; 
    while(i < strlen(startingAtProj)){
        if(startingAtProj[i] == '\0' || startingAtProj[i] == ' '){
            break;
        }
        else{
            ++i; 
            ++length;
        }
    }
    //malloc for proj name
    char* proj = (char*) malloc((length+1)*sizeof(char)); 
    //check for good malloc call
    if(!proj){
        perror("Failed to allocate memory while getting project name from command. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    int j = 0; 
    while(j < length){
        proj[j] = startingAtProj[j]; 
        ++j; 
    }
    proj[length] = '\0'; 
    return proj;
}

char* get_filepath_from_command(char* command){
    char* startingAtCommand = strchr(command, ' ')+1; 
    char* startingAtProjectname = strchr(startingAtCommand, ' ')+1;
    char* startingAtFilepath = strchr(startingAtProjectname, ' ')+1;
    return startingAtFilepath;
}

void delete_line_from_manifest(char* projectname, char* filepath, int currentVersion){
    //build path to .Manifest file 
    int manifestPathLength = strlen(projectname) + 1 + strlen("verion") + get_digits_in_version_number(currentVersion) + 1 + strlen(".Manifest") + 1; 
    //malloc for path to manifest file 
    char* manifestPath = (char*) malloc(manifestPathLength*sizeof(char)); 
    //check for good malloc call 
    if(!manifestPath){
        perror("Failed to allocate memory while adding file to .Manifest\n"); 
        exit(EXIT_FAILURE);
    }
    //get version # as string 
    char vn[get_digits_in_version_number(currentVersion)+1];
    sprintf(vn, "%d", currentVersion);
    //copy in projectname/version#/.Manifest
    strcpy(manifestPath, projectname); 
    strcat(manifestPath, "/"); 
    strcat(manifestPath, "version");
    strcat(manifestPath, vn);
    strcat(manifestPath, "/");
    strcat(manifestPath, ".Manifest");
    //build path to tempManifest 
    char* tempManifestPath = (char*) malloc(manifestPathLength*sizeof(char)); 
    //check for good malloc call 
    if(!tempManifestPath){
        perror("Failed to allocate memory while adding file to .Manifest\n"); 
        exit(EXIT_FAILURE);
    }
    //copy in projectname/version#/.Manifest
    strcpy(tempManifestPath, projectname); 
    strcat(tempManifestPath, "/"); 
    strcat(tempManifestPath, "version");
    strcat(tempManifestPath, vn);
    strcat(tempManifestPath, "/");
    strcat(tempManifestPath, ".tempManifest");
    //create temp manifest 
    FILE* tempFP = fopen(tempManifestPath, "wb");
    //check that tempManifest was created
    if(!tempFP){
        perror("Failed to create temporary .Manifest while removing file from .Manifest file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //open .Manifest
    FILE* manifestFP = fopen(manifestPath, "rb"); 
    //check that .Manifest was opened
    if(!manifestFP){
        perror("Failed to open .Manifest file while removing file from it. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //to store lines of .Manifest
    char line[1024]; 
    //loop through .Manifest and copy over everything that does not contain the filepath passed 
    while(fgets(line, sizeof(line), manifestFP)){
        if(strstr(line, filepath) == NULL){
            fputs(line, tempFP);
        }
    }
    //delete the old .Manifest
    if(remove(manifestPath) != 0){
        perror("Failed to remove old .Manifest file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //rename the temp manifest file
    if(rename(tempManifestPath, manifestPath) != 0){
        perror("Failed to rename temp .Manifest file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //close the file pointers
    fclose(tempFP); 
    fclose(manifestFP);
}

void display_current_version(char* manifestfile){
    //open manifest file
    FILE* fp = fopen(manifestfile, "r"); 
    //check to make sure it was opened
    if(!fp){
        perror("Failed to open .Manifest file while displaying its contents during currentversion. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //loop through .Manifest and output <path/filename>: <version#>
    char line[1024]; 
    //dont care about first line, so read it in and start looping from the second line
    int firstLine = 1;
    //loop starting from second line
    while(fgets(line, sizeof(line), fp)){
        if(firstLine){
            firstLine = 0;
            continue;
        }
        //get version number
        char version = line[0];
        int vers = version - '0';
        //get substring starting at file path
        char* startingAtFilePath = strchr(line, ' ')+1; 
        //count length of file path 
        int length = 0; 
        int i; 
        for(i = 0; i < strlen(startingAtFilePath); i++){
            if(startingAtFilePath[i] == ' '){
                break;
            }
            else{
                ++length;
            }
        }
        //malloc to store filepath
        char* filepath = (char*) malloc((length+1)*sizeof(char)); 
        //check for good malloc call
        if(!filepath){
            perror("Failed to allocate memory while attempting to output currentversion. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        //copy in file path 
        int j; 
        for(j = 0; j < length; j++){
            filepath[j] = startingAtFilePath[j];
        }
        filepath[length] = '\0';
        printf("%s: %d\n", filepath, vers);
    }
}

void delete_directory(char* name){
    //open the directory 
    DIR* directory = opendir(name); 
    //check that it was opened
    if(!directory){
        perror("Failed to open directory while attempting to remove it. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    struct dirent* directEntry; 
    //loop through entries in the directory 
    while((directEntry = readdir(directory)) != NULL){
        if(strcmp(directEntry->d_name, ".") == 0 || strcmp(directEntry->d_name, "..") == 0){
            continue;
        }
        //check if current directory entry is a file or a directory 
        if(directEntry->d_type == DT_DIR){
            //recurse into directory 
            printf("Recursing into %s\n", directEntry->d_name);
            //build new path 
            char* newPath = (char*) malloc((strlen(name)+1+strlen(directEntry->d_name)+1)*sizeof(char)); 
            if(!newPath){
                perror("Failed to allocate memory while removing directory. Exiting...\n"); 
                exit(EXIT_FAILURE);
            }
            strcpy(newPath, name); 
            strcat(newPath, "/"); 
            strcat(newPath, directEntry->d_name);
            delete_directory(newPath); 
        }
        //its a file so delete it 
        else{
            printf("Deleting %s\n", directEntry->d_name);
            remove(directEntry->d_name); 
        }
    }
    //remove the directory itself 
    rmdir(name);
}

int check_if_already_added(char* projectname, char* filepath, int currentVersion){
    // get the path for the current directory
    DIR* directory = opendir(projectname);
    if (!directory) { printf("Directory could not be found, exit\n"); exit(0); }
    struct dirent* item;
    while ((item = readdir(directory))) {
        if (strcmp(item->d_name, ".") == 0 || strcmp(item->d_name, "..") == 0) {
            continue;
        } else if (item->d_type == DT_DIR) {
	  // found version #
	  //printf("version = %s\n", item->d_name);
            break;
        }
    }
    // get currentVersion #
    int n;
    char num[5];
    bzero(num, sizeof(num)); 
    int m = 0;
    for (n = strlen(item->d_name)-1; n > 0; n--) {
      char c = item->d_name[n];
      //printf("char c = %c\n", c);
      if (isdigit(c) != 0) {
	num[m] = c;
	//printf("num[%d] = %c\n", m, num[m]);
	++m;
      } else {
	break;
      }
    }
    currentVersion = atoi(num);
    //build path to manifest file
    int pathLength = strlen(projectname) + 1 + strlen(item->d_name) + 1 + strlen(".Manifest") + 1; 
    //malloc for path 
    char* path = (char*) malloc(pathLength*sizeof(char)); 
    //check for good malloc call
    if(!path){
        perror("Failed to allocate memory while checking if file has already been added to the .Manifest. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //get version number as string
    //copy in projectname/version#/.Manifest 
    strcpy(path, projectname); 
    strcat(path, "/"); 
    strcat(path, item->d_name);
    strcat(path, "/"); 
    strcat(path, ".Manifest"); 
    //open .Manifest file
    FILE* manifestFile = fopen(path, "a+"); 
    //check that file was opened
    if(!manifestFile){
        perror("Failed to open .Manifest file while checking if file has already been added to it. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //loop through the entries and see if the filepath exists
    char line[1024]; 
    char newline[1024];
    char* newhash; 
    int vNumber; 
    char vNum[15]; 
    while(fgets(line, sizeof(line), manifestFile)){
        if(strstr(line, filepath) != NULL){
	  //filepath already in .Manifest 
	  printf("old line= %s\n", line); 
	  //char newline[1024]; 
	  bzero(newline, sizeof(newline)); 
	  newhash = get_hash(filepath); 
	  vNumber = get_fileversion_from_entry(line); 
	  //char vNum[15]; 
	  bzero(vNum, sizeof(vNum)); 
	  sprintf(vNum, "%d", vNumber); 
	  // remove old line
	  delete_line_from_manifest(projectname, filepath, currentVersion); 
	  // create newline
	  strcpy(newline, vNum); 
	  strcat(newline, " "); 
	  strcat(newline, filepath); 
	  strcat(newline, " "); 
	  strcat(newline, newhash);
	  strcat(newline, "\n"); 
	  // put newline
	  printf("newline = %s\n", newline); 
	  //fputs(newline, manifestFile); 
	  fclose(manifestFile); 
	  //return 1;
	  break; 
        }
    }
    FILE* manfile2 = fopen(path, "a+"); 
    while(fgets(line, sizeof(line), manfile2)){
      if (strstr(line, filepath) != NULL) {
	fputs(newline, manfile2);
	fclose(manfile2); 
	return 1; 
      }
    }
    //fclose(manifestFile); 
    //fclose(manfile2); 
    //file not already in .Manifest
    return 0;
}


/*TAR FUNCTIONS*/
char* tar(char* filename){
    //check that filename is not NULL
    if(!filename){
        perror("File to be tarred is NULL. Cannot tar file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //create string to hold tar command: tar -czvf <filename>.tgz <filename>
    int tarStringLength = strlen("tar -czvf ") + strlen(filename) + strlen(".tgz ") + strlen(filename) + 2;
    char* tarString = (char*) malloc(tarStringLength*sizeof(char)); 
    //check for good malloc call 
    if(!tarString){
        perror("Failed to allocate memory while attempting to tar file. Exiting...\n"); 
        exit(EXIT_FAILURE);    
    }
    //copy in "tar -czvf "
    strcpy(tarString, "tar -czvf "); 
    //copy in filename 
    strcat(tarString, filename); 
    //copy in .tgz
    strcat(tarString, ".tgz "); 
    //copy in filename
    strcat(tarString, filename);
    
    //system call on tarstring to execute tar command 
    system(tarString); 
    //return filename.tgz  
    char* tarfile = (char*) malloc((strlen(filename) + strlen(".tgz") + 1)*sizeof(char)); 
    //check for good malloc call
    if(!tarfile){
        perror("Failed to allocate memory while attempting to tar file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //copy in file name
    strcpy(tarfile, filename); 
    //copy in .tgz
    strcat(tarfile, ".tgz"); 
    printf("Successfully tarred file!\n");
    //return tarfile 
    return tarfile;
}

void untar(char* tarfile){
    //check that tarfile is not NULL 
    if(!tarfile){
        perror("File to untar is NULL. Cannot untar file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //create string to hold untar command 
    int untarStringLength = strlen("tar -zxvf ") + strlen(tarfile) + 1;
    char* untarString = (char*) malloc(untarStringLength*sizeof(char)); 
    //check for good malloc call 
    if(!untarString){
        perror("Failed to allocate memory while attempting to untar file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //copy in "tar -zxvf "
    strcpy(untarString, "tar -zxvf "); 
    //copy in tarfile name
    strcat(untarString, tarfile);
    //system call to untar file
    system(untarString);
    //remove tar file from directory 
    int removed = remove(tarfile);
    printf("Successfully untarred file!\n");
}

void send_tar_file(char* tarfile, int size, int fd){
    //send length of tarfile name
    send_string_size(fd, strlen(tarfile)); 
    //send tarfile name 
    send_string(tarfile, strlen(tarfile), fd); 
    //send size of tarfile 
    send_string_size(fd, size); 
    //malloc for string to hold bytes of tarfile 
    char* tarfileContents = (char*) malloc((size+1)*sizeof(char)); 
    //check for good malloc call
    if(!tarfileContents){
        perror("Failed to allocate memory while attempting to send tar file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //open the tar file 
    int ffd = open(tarfile, O_RDONLY); 
    //check that file was opened
    if(ffd < 0){
        perror("Failed to open tarfile while sending contents. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    char currentChar; 
    int index = 0; 
    while(read(ffd, &currentChar, 1) == 1){
        tarfileContents[index++] = currentChar;
    }
    close(ffd);
    //add null terminator to end of bytes string
    tarfileContents[index] = '\0';
    //send tarfile contents
    send_string(tarfileContents, size, fd);
    //remove tar file
    int removed = remove(tarfile);
}

void recieve_tar_file(int fd){
    //read in length of tarfile name
    int lengthOfTarfileName = get_string_size(fd);      
    //read in tarfile name
    char* tarfileName = read_string(fd, lengthOfTarfileName); 
    //read in size of tarfile 
    int tarfileSize = get_string_size(fd); 
    //malloc for string to store tarfile contents
    char* tarfileContents = read_string(fd, tarfileSize);
    //create file with filename 
    //printf("tarfileName %s \n", tarfileName); 
    int tf = open(tarfileName, O_CREAT | O_RDWR | O_APPEND, 0777); 
    //check that file was created
    if(tf < 0){
        perror("Failed to recreate tarfile. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //write tarfile contents into tarfile 
    send_string(tarfileContents, tarfileSize, tf); 
    //untar the tarfile 
    untar(tarfileName);
    close(tf);
    //remove the tar file 
    int removed = remove(tarfileName);
}

/*COMMAND FUNCTIONS*/
void add_command(char* projectname, char* filepath, int currentVersion){
    // get the path for the current directory
    DIR* directory = opendir(projectname);
    if (!directory) { printf("Directory could not be found, exit\n"); exit(0); }
    struct dirent* item;
    while ((item = readdir(directory))) {
        if (strcmp(item->d_name, ".") == 0 || strcmp(item->d_name, "..") == 0) {
            continue;
        } else if (item->d_type == DT_DIR) {
            // found version #
            //printf("version = %s\n", item->d_name);
            break;
        }
    }
    //get hash of the file
    char* hash = get_hash(filepath);
    //build path to .Manifest file 
    int manifestPathLength = strlen(projectname) + 1 + strlen(item->d_name) + 1 + strlen(".Manifest") + 1;
    //malloc for path to manifest file 
    char* manifestPath = (char*) malloc(manifestPathLength*sizeof(char)); 
    //check for good malloc call 
    if(!manifestPath){
        perror("Failed to allocate memory while adding file to .Manifest\n"); 
        exit(EXIT_FAILURE);
    }
    //get version # as string 
    //char vn[get_digits_in_version_number(currentVersion)+1];
    //sprintf(vn, "%d", currentVersion);
    //copy in projectname/version#/.Manifest
    strcpy(manifestPath, projectname); 
    strcat(manifestPath, "/"); 
    //strcat(manifestPath, "version");
    //strcat(manifestPath, vn);
    strcat(manifestPath, item->d_name);
    strcat(manifestPath, "/");
    strcat(manifestPath, ".Manifest");
    //create line to write to file 
    int lineLength = strlen("1 ") + strlen(filepath) + 1 + strlen(hash)+2;
    //malloc for new line
    char* entry = (char*) malloc(lineLength*sizeof(char)); 
    //check for good malloc call
    if(!entry){
        perror("Failed to allocate memory while adding file to .Manifest\n"); 
        exit(EXIT_FAILURE);
    }
    //copy in 1 @ <filepath> <hash>
    strcpy(entry, "1 "); 
    strcat(entry, filepath); 
    strcat(entry, " "); 
    strcat(entry, hash); 
    strcat(entry, "\n");
    //open manifest file 
    FILE* fp = fopen(manifestPath, "a"); 
    //check that file was opened
    if(!fp){
        perror("Failed to open .Manifest file while adding file to it. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //add line to end
    fputs(entry, fp);
    //close file
    fclose(fp); 
    printf("Successfully added file at %s to .Manifest file in %s!\n", filepath, projectname);
}

void remove_command(char* projectname, char* filepath, int currentVersion){
    delete_line_from_manifest(projectname, filepath, currentVersion);
    printf("Successfully removed %s from .Manifest in %s!\n", filepath, projectname);
}

void commit_command(char* projectname, int currV, int socketfd, ManifestFile* serverManifest) {
    // get the path for the current directory
    DIR* directory = opendir(projectname);
    if (!directory) { printf("Directory could not be found, exit\n"); exit(0); }
    struct dirent* item;
    while ((item = readdir(directory))) {
        if (strcmp(item->d_name, ".") == 0 || strcmp(item->d_name, "..") == 0) {
            continue;
        } else if (item->d_type == DT_DIR) {
            // found version #
            //printf("version commit = %s\n", item->d_name);
            break;
        }
    }
    int n;
    char num[5];
    bzero(num, sizeof(num)); 
    int m = 0;
    for (n = strlen(item->d_name)-1; n > 0; n--) {
      char c = item->d_name[n];
      //printf("char c = %c\n", c);
      if (isdigit(c) != 0) {
	  num[m] = c;
	  //printf("num[%d] = %c\n", m, num[m]);
	  ++m;
        } else {
            break;
        }
    }
    currV = atoi(num);
    
    // versionpath = <projectname>/<version(currentVersion)>
    int versionpathlen = strlen(projectname) + 1 + strlen("version") + get_digits_in_version_number(currV);
    char* versionpath = (char*) malloc(sizeof(char) * (versionpathlen + 1));
    //get version # as string
    char vn[get_digits_in_version_number(currV)+1];
    sprintf(vn, "%d", currV);
    strcpy(versionpath, projectname);
    strcat(versionpath, "/");
    strcat(versionpath, "version");
    strcat(versionpath, vn);
    
    // get the path for the commit file
    int commitlen = strlen(versionpath) + strlen("/") + strlen(".Commit");
    char* commitpath = (char*) malloc(sizeof(char) * (commitlen+1));
    strcpy(commitpath, versionpath);
    strcat(commitpath, "/");
    strcat(commitpath, ".Commit");
    
    // then, we take the client's .Manifest and convert it into manifest structs
    // first, need path for client manifest
    int cManlen = strlen(versionpath) + strlen("/") + strlen(".Manifest");
    char* cManpath = (char*) malloc(sizeof(char) * (cManlen+1));
    strcpy(cManpath, versionpath);
    strcat(cManpath, "/");
    strcat(cManpath, ".Manifest");
    // now convert the cMan to manifest structs
    int currentVersion;
    FILE* cMan = fopen(cManpath, "a+");
    if (!cMan) { perror("Failed to open .Manifest file (From client). Exiting...\n"); exit(EXIT_FAILURE);}
    fscanf(cMan, "%d\n", &currentVersion);
    
    // now make the server manifest file LL
    ManifestFile* clientManifest = create_manifestfile(currentVersion);
    
    // if the version numbers are different, we exit
    if (serverManifest->version != clientManifest->version) {
        printf("The server has version %d and the client has version %d, the versions are different and the user needs to update itself to the server's version first. Exiting.. \n", serverManifest->version, clientManifest->version);
        exit(EXIT_FAILURE);
    }
    
    // now loop to create entries
    char line[1024];
    //bzero(line, sizeof(line));
    while (fgets(line, sizeof(line), cMan)) {
        // entry for cMan
        //printf("line = %s\n", line);
        // live hash
        char* livehash = get_hash(get_filepath_from_entry(line));
        // now create the entry for the line with live hash
        ManifestFileEntry* currentEntry = create_manifestfile_entry(currentVersion, seen_by_server(line), get_filepath_from_entry(line), livehash);
        // insert it
        insert_manifestfile_entry(&(clientManifest->entries), currentEntry);
    }
    fclose(cMan);
    
    // then, compare versions of the client's manifest of that project to the servers
    ManifestFileEntry* clientptr = clientManifest->entries;
    ManifestFileEntry* serverptr = serverManifest->entries;
    ManifestFileEntry* headOfServer = serverManifest->entries;
    
    FILE* comfd = fopen(commitpath, "a+");
    if (!comfd) { perror("Commit file was not able to open, exiting now..\n"); exit(EXIT_FAILURE);}
    // now loop through server's and client's LL's
    while(clientptr != NULL) {
        // make sure it starts at top every time
        serverptr = headOfServer;
        while (serverptr != NULL) {
            // if the same file.. do comparissons
            if (strcmp(clientptr->filePath, serverptr->filePath) == 0) {
                //printf("same file:\n \tclient = %s\n \tserver = %s\n", clientptr->filePath, serverptr->filePath);
                // if client has a higher version # or the hashes are different
                if (strcmp(clientptr->hash, serverptr->hash) != 0) {
                    //printf("different hash: \n \tclient = %s\n \tserver = %s\n", clientptr->hash, serverptr->hash);
                    // this means client has newer version than server, and must write it out to the .Commit
                    char tocommit[1024];
                    sprintf(tocommit, "%c %d %s %s\n", 'o', (clientptr->fileVersion) + 1, clientptr->filePath, clientptr->hash);
                    fputs(tocommit, comfd);
                    break;
                } // client has same file but newer version
                else if (clientptr->fileVersion > serverptr->fileVersion) {
                    // this means client has newer version than server, and must write it out to the .Commit
                    char tocommit[1024];
                    sprintf(tocommit, "%c %d %s %s\n", 'o', (clientptr->fileVersion) + 1, clientptr->filePath, clientptr->hash);
                    fputs(tocommit, comfd);
                    break;
                }
                else if (clientptr->fileVersion < serverptr->fileVersion) {
                    // remove .Commit
                    int rm = remove(commitpath);
                    // print error message and exit
                    perror("Server version of the file is newer than clients, client must update.. Exiting.. \n");
                    exit(EXIT_FAILURE);
                }
                // if it doesn't satisfy any of these, then the file does not need to be changed, so break
                break;
            }
            // if it is the last entry, and still hasn't called break, it is in the c and not the s, so add
            if (serverptr->next == NULL) {
                // add
                char tocommit[1024];
                sprintf(tocommit, "%c %d %s %s\n", 'a' , (clientptr->fileVersion) + 1, clientptr->filePath, clientptr->hash);
                fputs(tocommit, comfd);
            }
            // increment
            serverptr = serverptr->next;
        }
        // increment
        clientptr = clientptr->next;
    }
    
    // now itterate through, looking for deletes
    clientptr = clientManifest->entries;
    serverptr = serverManifest->entries;
    ManifestFileEntry* headOfClient = clientManifest->entries;
    while (serverptr != NULL) {
        // set client ptr to the head
        clientptr = headOfClient;
        while (clientptr != NULL) {
            // if the files are the same
            if (strcmp(clientptr->filePath, serverptr->filePath) == 0) {
                // exit and continue
                break;
            }
            // if it hasn't called break and is the last entry, it is a delete
            if (clientptr->next == NULL) {
                // delete
                char tocommit[1024];
                sprintf(tocommit, "%c %d %s %s\n", 'd' , (serverptr->fileVersion) + 1, serverptr->filePath, serverptr->hash);
                fputs(tocommit, comfd);
            }
            // increment
            clientptr = clientptr->next;
        }
        // increment
        serverptr = serverptr->next;
    }
    fclose(comfd);
    
    // now make path to commit tarfile
    int pathToComlen = strlen(projectname) + 1 + strlen("version") + get_digits_in_version_number(currV);
    char* pathToCommit = (char*) malloc(sizeof(char) * (pathToComlen+1));
    strcpy(pathToCommit, projectname);
    strcat(pathToCommit, "/");
    strcat(pathToCommit, "version");
    strcat(pathToCommit, vn);
    chdir(pathToCommit);
    
    // check if commit is empty or not
    int fd = open(".Commit", O_RDONLY);
    off_t size = lseek(fd, (off_t)0, SEEK_END);
    int commitSize = (int)size;
    if (commitSize == 0) {
        printf("The repository is up to date and there is nothing to commit\n"); 
    }
    
    close(fd);
    // tar the commit
    char* tarfile = tar(".Commit");
    send_tar_file(tarfile, get_file_size(tarfile), socketfd);
    
    // at the end, we remove the server's manifest that was sent over
    //int removed = remove(".Commit");
}
