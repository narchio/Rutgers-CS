#include "WTFserver.h"

void launch_server(int port){
    //set thread list to NULL to start 
    listOfThreads = NULL;
    //set project list to NULL 
    listOfProjects = NULL;
    //file descriptor for server's listening socket and new socket
    int lsocketfd, nsocketfd; 
    //struct for server 
    struct sockaddr_in server; 
    bzero(&server, sizeof(server)); 
    //struct for client
    struct sockaddr_in client; 
    bzero(&client, sizeof(client));
    
    //create socket and verify that it was made successfully 
    lsocketfd = socket(AF_INET, SOCK_STREAM, 0); 
    if(lsocketfd < 0){
        perror("Failed to create server-side listening socket. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    else{
        printf("Server-side listening socket created!\n");
    }
    //set socket options to be able to reuse the port 
    int sockopt = 1;
    if(setsockopt(lsocketfd, SOL_SOCKET, SO_REUSEADDR, &sockopt, sizeof(int)) < 0){
        perror("Failed to set socket options to reuse port. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    else{
        printf("Socket options to reuse port set!\n");
    }
    //assign IP and PORT to server 
    server.sin_family = AF_INET; 
    server.sin_addr.s_addr = htonl(INADDR_ANY); 
    server.sin_port = htons(port); 
    //bind listening socket and check that binding was successful 
    if((bind(lsocketfd, (struct sockaddr*)&server, sizeof(server))) != 0) { 
        perror("Failed to bind server-side listening socket. Exiting...\n"); 
        exit(EXIT_FAILURE);
    } 
    else{
        printf("Server-side listening socket binded!\n");
    }
    //call listen on the server 
    if((listen(lsocketfd, 20)) != 0){
        perror("Listening failed. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    else{
        printf("Server is now listening!\n"); 
    }

    //loop infinitely to accept connections 
    while(1){
        signal(SIGINT, signal_handler);
        //get size of client 
        int clientSize = sizeof(client); 
        //accept new client connection and check that connection is successful 
        nsocketfd = accept(lsocketfd, (struct sockaddr*)&client, &clientSize); 
        if(nsocketfd < 0){
            perror("Failed to accept client connection. Exiting...\n"); 
            exit(EXIT_FAILURE); 
        }
        else{
            printf("New client connection accepted!\n");
        }
        //create new thread node 
        ThreadNode* node = create_thread_node(); 
        //create a new thread 
        if(pthread_create((&(node->thread)), NULL, (void*)&connection_handler, (void*)&nsocketfd) != 0){
            perror("Failed to create new thread. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        else{
            printf("New thread created!\n"); 
            //insert threadnode into list 
            insert_thread_node(&listOfThreads, node);
        }
    }
}

void connection_handler(void* params){
    //get socketfd from argument 
    int fd = *((int*)params); 
    //read in command size from client 
    int commandSize = get_string_size(fd);
    //read in command from server
    char* command = read_string(fd, commandSize);
    
    //do things based on the command 
    //create
    if(strncmp(command+6, "create", 6) == 0){
        char* projectname = (strchr(command+6, ' ')+1);
        create_command(projectname);
        char* tarfile = tar(projectname);
        send_tar_file(tarfile, get_file_size(tarfile), fd);
    }
    //add
    else if(strncmp(command+6, "add", 3) == 0){
        //send current version number of project
        send_string_size(fd, get_project_current_version_number(listOfProjects, get_projectname_from_command(command)));
    }
    //destroy
    else if(strncmp(command+6, "destroy", 7) == 0){
        int commandLength = strlen("rm -r ") + strlen(get_projectname_from_command(command)) + 1; 
        char* comm = (char*) malloc(commandLength*sizeof(char)); 
        if(!comm){
            perror("Failed to allocate memory during directory removal. Exiting...\n"); 
            exit(EXIT_FAILURE); 
        }
        strcpy(comm, "rm -r "); 
        strcat(comm, get_projectname_from_command(command)); 
        system(comm); 
        printf("Successfully deleted project!\n");
    }
    //remove
    else if(strncmp(command+6, "remove", 6) == 0){
        //send current version number of project
        send_string_size(fd, get_project_current_version_number(listOfProjects, get_projectname_from_command(command)));
    }
    //current version 
    else if(strncmp(command+6, "currentversion", 14) == 0){
        currentversion_command(get_projectname_from_command(command), fd);
    }
    //checkout
    else if(strncmp(command+6, "checkout", 8) == 0){
        char* tarfile = tar(get_projectname_from_command(command));
        send_tar_file(tarfile, get_file_size(tarfile), fd);
        send_string_size(fd, get_project_current_version_number(listOfProjects, get_projectname_from_command(command)));
    }
    // commit 
    else if (strncmp(command+6, "commit", 6) == 0) {
      
      int currentVersion = get_project_current_version_number(listOfProjects, get_projectname_from_command(command)); 
      // send current version to client
      send_string_size(fd, currentVersion); 
      // get the path for the current directory
      DIR* directory = opendir(get_projectname_from_command(command));
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
      int pathLength = strlen(get_projectname_from_command(command)) + 1 + strlen(item->d_name); 
      char* pathToManifest = (char*) malloc(sizeof(char) * (pathLength+1)); 
      if (!pathToManifest) {
	perror("Failed to allocate memory during commit. Exiting..\n"); 
	exit(EXIT_FAILURE); 
      }
      // get version number as a string 
      //char vn[get_digits_in_version_number(currentVersion)+1]; 
      //sprintf(vn, "%d", currentVersion); 
      // copy in path
      strcpy(pathToManifest, get_projectname_from_command(command)); 
      strcat(pathToManifest, "/"); 
      //strcat(pathToManifest, "version"); 
      //strcat(pathToManifest, vn); 
      strcat(pathToManifest, item->d_name); 
      chdir(pathToManifest); 
      // send manifest over
      //printf("pathToManifest = %s\n", pathToManifest); 
      char* tarfile = tar(".Manifest"); 
      send_tar_file(tarfile, get_file_size(tarfile), fd); 
      // now commit
      // recieve the .Commit from the client 
      recieve_tar_file(fd);
    }
} 

void signal_handler(int sig){
    if(sig == SIGINT){
        printf("Control-C has been entered. Exiting...\n"); 
        exit(EXIT_SUCCESS);
    }
}
int main(int argc, char** argv){
    if(argc == 2){
        printf("Preparing to launch server...\n"); 
        launch_server(atoi(argv[1])); 
    }
    else{
        perror("Invalid amount of parameters passed to server executable. Please enter ./WTFserver <PORT> to launch server.\n"); 
        exit(EXIT_FAILURE);
    }
}

