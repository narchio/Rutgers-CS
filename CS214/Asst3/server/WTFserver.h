#ifndef _WTFserver_h 
#define _WTFserver_h 

//include necessary libraries 
#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 
#include <unistd.h>
#include <signal.h>
#include <netdb.h> 
#include <netinet/in.h> 
#include <sys/socket.h> 
#include <sys/types.h> 
#include <pthread.h>
#include <ctype.h>
#include <dirent.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <openssl/sha.h>

//define struct to store thread
typedef struct _ThreadNode ThreadNode; 
struct _ThreadNode{
    pthread_t thread; 
    ThreadNode* next; 
};

//ThreadNode functions 
ThreadNode* create_thread_node(); 
void insert_thread_node(ThreadNode** list, ThreadNode* node);

//global variable for ThreadNode* linked list 
ThreadNode* listOfThreads;

/*----------------------------------------------------------------------*/

//define struct for ProjectNode
typedef struct _Project ProjectNode; 
struct _Project{
    char* projectName;
    int currentVersionNum; 
    pthread_mutex_t mutex; 
    ProjectNode* next;
};

//global variable for ProjectNode* linked list 
ProjectNode* listOfProjects;

//ProjectNode functions 
ProjectNode* create_project_node(char* projectName);
int get_project_current_version_number(ProjectNode* list, char* projectName);
void insert_project_node(ProjectNode** list, ProjectNode* node);
void update_project_version_number(ProjectNode** list, char* projectName);

/*-----------------------------------------------------------------------*/

//Server functions 
void launch_server(int port);
void connection_handler(void* params); 
void signal_handler(int sig);

//Helper functions
int get_string_size(int fd);
char* read_string(int fd, int size);
void send_string_size(int fd, int size);
void send_string(char* string, int size, int fd);
int get_file_size(char* filename);
char* get_projectname_from_command(char* command);
char* get_filepath_from_command(char* command);
void copy_and_rename_file(char* origFilePath, char* newFilePath);
int get_digits_in_version_number(int version);
void delete_directory(char* name);
    

//Tar functions
char* tar(char* filename);
void untar(char* tarfile);
void send_tar_file(char* tarfile, int size, int fd);
void recieve_tar_file(int fd);

//Commands
void create_command(char* projectname);
void currentversion_command(char* projectname, int fd);


#endif 