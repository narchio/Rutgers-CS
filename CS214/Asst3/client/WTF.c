#include "WTF.h"

void launch_client(int argc, char** argv){
    //file descriptor for client socket 
    int csocketfd; 
    //struct for server 
    struct sockaddr_in server; 
    bzero(&server, sizeof(server));

    //create socket and check that creation was successful 
    csocketfd = socket(AF_INET, SOCK_STREAM, 0); 
    if(csocketfd < 0){
        perror("Failed to create client-side socket. Exiting...\n"); 
        exit(EXIT_FAILURE);
    }
    else{
        printf("Client-side socket created!\n");
    }
    //get IP and PORT from config file 
    char* IP = get_IP_from_config_file(); 
    char* PORT = get_PORT_from_config_file(); 
    //assign IP and PORT to connect to
    server.sin_family = AF_INET; 
    if(IP){
        server.sin_addr.s_addr = inet_addr(IP);
    }
    if(PORT){
        server.sin_port = htons(atoi(PORT));
    }

    //attempt to connect to server and retry every 3 seconds until it does connect
    int connected = 0; 
    while(connected == 0){
        if(connect(csocketfd, (struct sockaddr*)&server, sizeof(server)) != 0){
            perror("Failed to connect to server. Trying again in 3 seconds...\n"); 
            sleep(3);
        }
        else{
            printf("Client has connected to the server!\n"); 
            connected = 1;
        }
    }
    client_command_manager(argc, argv, csocketfd);
}

void client_command_manager(int argc, char** argv, int socketfd){
   
    //Build command from argc and argv 
    char* command = build_command(argc, argv); 
    //send the command size
    send_string_size(socketfd, strlen(command)); 
    //send the command 
    send_string(command, strlen(command), socketfd);
    
    //create
    if(strncmp(command+6, "create", 6) == 0){
        //recieve the newly created project from the server and untar it 
        recieve_tar_file(socketfd);
        printf("Successfully created project %s!\n", get_projectname_from_command(command));
    }
    //destroy
    else if(strncmp(command+6, "destroy", 7) == 0){
      printf("Successfully deleted\n"); 
    }
    //add
    else if(strncmp(command+6, "add", 3) == 0){
        //get the current version of the project 
        int currentVersion = get_string_size(socketfd);
        //check if file has already been added 
        int inManifest = check_if_already_added(get_projectname_from_command(command), get_filepath_from_command(command), currentVersion); 
        if(inManifest){
	  printf("already in manifest\n"); 
        }
        else{
            //add the file to the .Manifest
            add_command(get_projectname_from_command(command), get_filepath_from_command(command), currentVersion);
        }
        
    }
    //remove
    else if(strncmp(command+6, "remove", 6) == 0){
        //get the current version of the project 
        int currentVersion = get_string_size(socketfd);
        //remove the file from the .Manifest
        remove_command(get_projectname_from_command(command), get_filepath_from_command(command), currentVersion);
    }
    //current version
    else if(strncmp(command+6, "currentversion", 14) == 0){
        //recieve the server's .Manifest and untar it 
        recieve_tar_file(socketfd);
        //print out the filepath's and the file versions 
        display_current_version(".Manifest"); 
        //remove the .Manifest file from the client directory
        remove(".Manifest");
        printf("Successfully displayed currentversion!\n");
    }
    // commit
    else if (strncmp(command+6, "commit", 6) == 0) {
      // get curr version of the project
      int currentVersion = get_string_size(socketfd); 
      // get server's .Manifest
      recieve_tar_file(socketfd); 
      printf("recieved .Manifest\n");
      // build struct from it
      ManifestFile* serverManifest = create_manifest_struct_from_file(".Manifest", 0); 
      // remove .Manifest from client directory
      remove(".Manifest"); 
      // now call the commit
      commit_command(get_projectname_from_command(command), currentVersion, socketfd, serverManifest); 
      printf("Sucessfully sent .Commit!\n"); 
    }
    //checkout
    else if(strncmp(command+6, "checkout", 8) == 0){
        //recieve the project and untar it 
        recieve_tar_file(socketfd);
        //get the current version of the project 
        int currentVersion = get_string_size(socketfd);
        //create string with the current version not to delete
        char* versionToKeep = (char*) malloc((strlen("version") + get_digits_in_version_number(currentVersion)+1)*sizeof(char));
        if(!versionToKeep){
            perror("Failed to allocate memory during checkout. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        char vn[get_digits_in_version_number(currentVersion)+1];
        sprintf(vn, "%d", currentVersion);
        strcpy(versionToKeep, "version"); 
        strcat(versionToKeep, vn);
        //go into the project directory 
        chdir(get_projectname_from_command(command)); 
        //get the cwd
        char cwd[128]; 
        getcwd(cwd, sizeof(cwd)); 
        //open current directory and delete all version folder that are not the current version 
        DIR* dir = opendir(cwd); 
        if(!dir){
            perror("Failed to open directory during removal. Exiting...\n"); 
            exit(EXIT_FAILURE);
        }
        struct dirent* entry; 
        //iterate through the version folders
        while(entry = readdir(dir)){
            //skip these
            if(strcmp(entry->d_name, ".") == 0 || strcmp(entry->d_name, "..") == 0){
                continue; 
            }
            //delete directory if its not the current version
            else if(strcmp(entry->d_name, versionToKeep) != 0){
                delete_directory(entry->d_name);
            }
        }
    }
}



int main(int argc, char** argv){
    //client entered configure
    if(argc == 4 && strcmp(argv[1], "configure") == 0){
        configure(argv[2], argv[3]); 
    }
    else{
        launch_client(argc, argv);
    }
}
