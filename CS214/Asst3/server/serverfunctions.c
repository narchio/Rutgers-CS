#include "WTFserver.h"

/*THREAD NODE FUNCTIONS*/
ThreadNode* create_thread_node(){
    //malloc for space to store ThreadNode* 
    ThreadNode* tn = (ThreadNode*) malloc(1*sizeof(ThreadNode)); 
    //check that malloc call was successful 
    if(!tn){
        perror("Failed to allocate memory while creating ThreadNode*. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //leave thread field alone, set next to NULL 
    tn->next = NULL; 
    //return newly created ThreadNode 
    return tn;
}
void insert_thread_node(ThreadNode** list, ThreadNode* node){
    //check that node is not NULL 
    if(!node){
        perror("Pointer passed to insert into list is NULL. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //list is empty
    else if((*list) == NULL){
        (*list) = node; 
        return;
    }
    //pointer to traverse linked list
    ThreadNode* ptr = (*list); 
    //loop until last element in linked list
    while(ptr->next != NULL){
        ptr = ptr->next;
    }
    //set ptr->next to new node 
    ptr->next = node;

}



/*PROJECT NODE FUNCTIONS*/
ProjectNode* create_project_node(char* projectName){
    //malloc for space
    ProjectNode* node = (ProjectNode*) malloc(1*sizeof(ProjectNode)); 
    //check for good malloc call
    if(!node){
        perror("Failed to allocate memory while creating a ProjectNode. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //set projectname
    node->projectName = (char*) malloc((strlen(projectName)+1)*sizeof(char)); 
    strcpy(node->projectName, projectName);
    node->currentVersionNum = 1; 
    pthread_mutex_init(&node->mutex, NULL);
    node->next = NULL; 
    return node;
}
int get_project_current_version_number(ProjectNode* list, char* projectName){
    //check that projectName is not NULL
    if(!projectName){
        perror("Project name passed is NULL. Cannot find current project verion. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //pointer to go through linked list
    ProjectNode* ptr; 
    for(ptr = list; ptr != NULL; ptr = ptr->next){
        if(strcmp(projectName, ptr->projectName) == 0){
            return ptr->currentVersionNum;
        }
    }
}
void insert_project_node(ProjectNode** list, ProjectNode* node){
    //check that node is not NULL 
    if(!node){
        perror("Pointer passed to insert into list is NULL. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //list is empty
    else if((*list) == NULL){
        (*list) = node; 
        return;
    }
    //pointer to traverse linked list
    ProjectNode* ptr = (*list); 
    //loop until last element in linked list
    while(ptr->next != NULL){
        ptr = ptr->next;
    }
    //set ptr->next to new node 
    ptr->next = node;
}

void update_project_version_number(ProjectNode** list, char* projectName){
    //check that projectName is not NULL
    if(!projectName){
        perror("Project name passed is NULL. Cannot find current project verion. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //pointer to go through linked list
    ProjectNode* ptr; 
    for(ptr = (*list); ptr != NULL; ptr = ptr->next){
        if(strcmp(projectName, ptr->projectName) == 0){
            ptr->currentVersionNum += 1;
            break;
        }
    }
    return;
}



/*HELPER FUNCTIONS*/
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
        } //check if current directory entry is a file or a directory
        else if(directEntry->d_type == DT_DIR){
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
    remove(name);
}



/*Tar functions*/
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
    remove(tarfile);
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



/*COMMANDS*/
void create_command(char* projectname){
    //create the project folder 
    int result = mkdir(projectname, 0777);
    //check if projectfolder was made successfully 
    if(result != 0){
        perror("Failed to create project folder. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //createpath to projectname/version1 
    int v1PathLength = strlen(projectname) + 1 + strlen("version1") + 1; 
    //malloc for path
    char* v1Path = (char*) malloc(v1PathLength*sizeof(char)); 
    //check for good malloc call
    if(!v1Path){
        perror("Failed to allocate memory while creating new project. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //copy in projectname/version1 
    strcpy(v1Path, projectname); 
    strcat(v1Path, "/"); 
    strcat(v1Path, "version1"); 
    //make the directory 
    int result2 = mkdir(v1Path, 0777);
    if(result2 != 0){
        perror("Failed to create version folder when creating new project. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //create path to projectname/version1/.Manifest
    int finalPathLength = strlen(projectname) + 1 + strlen("version1") + 1 + strlen(".Manifest") + 1;
    //malloc for the path
    char* finalPath = (char*) malloc(finalPathLength*sizeof(char)); 
    //check for good malloc call
    if(!finalPath){
        perror("Failed to allocate memory while creating new project. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //copy in projectname/version1/.Manifest
    strcpy(finalPath, projectname); 
    strcat(finalPath, "/"); 
    strcat(finalPath, "version1"); 
    strcat(finalPath, "/"); 
    strcat(finalPath, ".Manifest");
    //create .Manifest file
    FILE* fp = fopen(finalPath, "ab+"); 
    if(!fp){
        perror("Failed to create .Manifest file. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    fputs("1\n", fp);
    fclose(fp);
    printf("Successfully created %s!\n", projectname);
    //create ProjectNode and insert it into the list
    ProjectNode* proj = create_project_node(projectname);
    insert_project_node(&listOfProjects, proj);
}

void currentversion_command(char* projectname, int fd){
    //get current version for project
    int currentVersion = get_project_current_version_number(listOfProjects, projectname); 
    //build path to manifest file 
    int manifestPathLength = strlen(projectname) + 1 + strlen("version") + get_digits_in_version_number(currentVersion) + 1;
    //malloc for path
    char* manifestPath = (char*) malloc(manifestPathLength*sizeof(char)); 
    //check for good malloc call 
    if(!manifestPath){
        perror("Failed to allocate memory while fetching current version. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    //get version # as string 
    char vn[get_digits_in_version_number(currentVersion)+1];
    sprintf(vn, "%d", currentVersion);
    //copy in projectname/version#/
    strcpy(manifestPath, projectname); 
    strcat(manifestPath, "/"); 
    strcat(manifestPath, "version"); 
    strcat(manifestPath, vn); 
    //change directory 
    chdir(manifestPath);
    //tar manifest file and send 
    char* tarfile = tar(".Manifest"); 
    send_tar_file(tarfile, get_file_size(tarfile), fd);
}

