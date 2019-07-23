#ifndef _WTF_h 
#define _WTF_h 

//include libraries 
#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 
#include <unistd.h>
#include <netdb.h> 
#include <netinet/in.h> 
#include <sys/socket.h> 
#include <sys/types.h> 
#include <pthread.h>
#include <ctype.h>
#include <signal.h>
#include <dirent.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <openssl/sha.h>


typedef struct _ManifestFileEntry ManifestFileEntry; 
struct _ManifestFileEntry{
  int fileVersion; 
  int seenByServer; 
  char* filePath; 
  char* hash;
  ManifestFileEntry* next;
};

typedef struct _ManifestFile ManifestFile; 
struct _ManifestFile{
  int version; 
  ManifestFileEntry* entries;
};


//Manifest file functions
ManifestFileEntry* create_manifestfile_entry(int fileVersion, int seenByServer, char* filePath, char* hash); 
ManifestFile* create_manifestfile(int version); 
void insert_manifestfile_entry(ManifestFileEntry** entries, ManifestFileEntry* entry);
int get_fileversion_from_entry(char* entry); 
int seen_by_server(char* entry); 
char* get_filepath_from_entry(char* entry); 
char* get_hash_from_entry(char* entry); 
ManifestFile* create_manifest_struct_from_file(char* manifest, int liveHashes);


//Configure functions 
int check_configure(char* path);
void configure(char* IP, char* PORT);
char* get_IP_from_config_file();
char* get_PORT_from_config_file();

//Client functions
void launch_client(int argc, char** argv);
void client_command_manager(int argc, char** argv, int socketfd);


//Helper functions
char* build_command(int argc, char** argv);
void send_string_size(int fd, int size);
void send_string(char* string, int size, int fd);
char* read_string(int fd, int size);
int get_file_size(char* filename);
int get_string_size(int fd);
char* get_hash(char* filename);
int get_digits_in_version_number(int version);
char* get_projectname_from_command(char* command);
char* get_filepath_from_command(char* command);
void delete_line_from_manifest(char* projectname, char* filepath, int currentVersion);
void display_current_version(char* manifestfile);
void delete_directory(char* name);
int check_if_already_added(char* projectname, char* filepath, int currentVersion);

//Tar functions
char* tar(char* filename);
void untar(char* tarfile);
void send_tar_file(char* tarfile, int size, int fd);
void recieve_tar_file(int fd);

//Client commands 
void add_command(char* projectname, char* filepath, int currentVersion);
void remove_command(char* projectname, char* filepath, int currentVersion);
void commit_command(char* projectname, int currV, int socketfd, ManifestFile* serverManifest); 

#endif 
