#include "mymalloc.h" 

void* mymalloc(size_t requestSize, char* file, int line){
	//check that request size is not <= 0
	if(requestSize <= 0){
		printf("Invalid allocation request size of %d bytes in %s on line %d. Unable to allocate memory!\n", requestSize, file, line); 
		return NULL;
	}
	
	//check that request is not too large
	else if(requestSize > (4096-sizeof(MetaData))){
		printf("Allocation request size of %d bytes in %s on line %d is too large. Unable to allocate memory!\n", requestSize, file, line); 
		return NULL;
	}
	
	//check for first call of malloc and init memory accordingly  
	int initialized = (((MetaData*) memory)->magicNumber == 65535); 
	if(initialized == 0){
		MetaData* head = (MetaData*) memory; 
		head->allocationSize = 4096-sizeof(MetaData); 
		head->inUse = '0';
		head->magicNumber = 65535; 
		head->next = NULL; 
	}
	
	MetaData* ptr; 
	MetaData* newEntry; 
	
	for(ptr = (MetaData*)memory; ptr != NULL; ptr = ptr->next){
		//metadata entry reflects space that is too small or already in use 
		if(ptr->allocationSize < requestSize || ptr->inUse == '1'){
			continue; //go to the next entry
		}
		
		//there is enough memory for the allocation call, but not enough to store the next metadata entry for remaining memory 
		else if(ptr->allocationSize < (requestSize + sizeof(MetaData)) && ptr->inUse == '0'){
			ptr->inUse = '1'; 
			MetaData* p = (MetaData*) memory; 
	
			return (void*)(((char*)ptr) + sizeof(MetaData)); 
		}
		
		//there is enough memory for allocation call and next metadata entry
		//memory needs to be split 
		else if(ptr->allocationSize > (requestSize + sizeof(MetaData)) && ptr->inUse == '0'){
			//create new metadata entry to reflect remaining free memory 
			newEntry = (MetaData*)(((char*)ptr) + sizeof(MetaData) + requestSize); 
			newEntry->allocationSize = ptr->allocationSize - sizeof(MetaData) - requestSize; 
			newEntry->inUse = '0'; 
			newEntry->magicNumber = 65535; 
			newEntry->next = ptr->next; 
			
			//update metadata entry to reflect allocation call 
			ptr->allocationSize = requestSize; 
			ptr->inUse = '1'; 
			ptr->next = newEntry; 
			
			return (void*)(((char*)ptr) + sizeof(MetaData));
		}	
	}
	
	//no metadata entries can accomodate the allocation call
	printf("Unable to allocate %d bytes of memory in %s on line %d!\n", requestSize, file, line); 
	return NULL; 
}

void myfree(void* ptr, char* file, int line){
	//metadata entry associated with pointer to chunk of memory 
	MetaData* entry = (MetaData*)(((char*)ptr) - sizeof(MetaData)); 
	
	//checking if pointer is null
	if(ptr == NULL){
		printf("Attempted to free a NULL pointer. Unable to free a NULL pointer in %s on line %d!\n", file, line); 
		return; 
	}
	
	//checking if pointer is within confines of memory i.e. it was allocated by mymalloc
	else if(((char*)ptr) < memory || ((char*)ptr) > memory+4096){
		printf("Attempted to free a NULL pointer. Unable to free a NULL pointer in %s on line %d!\n", file, line); 
		return; 
	}
	
	//check if pointer was already freed 
	else if(entry->inUse == '0'){
		printf("Pointer was already freed. Unable to free pointer in %s on line %d!\n", file, line); 
		return;
	}
	
	//check if pointer passed to free points to beginning of memory chunk : this is free(ptr + 2) sort of cases
	else if(entry->magicNumber != 65535){
		printf("Pointer does not point to beginning of allocation space. Unable to free pointer in %s on line %d!\n", file, line); 
		return;
	}
	
	//need to free pointer and then check if adjacent free memory exists and needs to be merged 
	else {
		entry->inUse = '0'; 
		MetaData* prev = NULL; 
		MetaData* curr = (MetaData*) memory; 
		while(curr != NULL && curr != entry){
			prev = curr; 
			curr = curr->next; 
		}
		
		int prevNull = (prev == NULL); 
		int currNull = (curr == NULL); 
		int nextNull = (curr->next == NULL); 
		
		//3 adjacent chunks of memory need to be merged 
		if(!prevNull && !currNull && !nextNull && prev->inUse == '0' && curr->inUse == '0' && curr->next->inUse == '0'){
			prev->allocationSize += sizeof(MetaData) + curr->allocationSize + sizeof(MetaData) + curr->next->allocationSize; 
			prev->next = curr->next->next; 
		}
		
		//prev and curr need to be merged 
		else if(!prevNull && !currNull && !nextNull && prev->inUse == '0' && curr->inUse == '0' && curr->next->inUse == '1'){
			prev->allocationSize += sizeof(MetaData) + curr->allocationSize; 
			prev->next = curr->next; 
		}
		
		//curr and curr->next need to be merged 
		else if(!prevNull && !currNull && !nextNull && prev->inUse == '1' && curr->inUse == '0' && curr->next->inUse == '0'){
			curr->allocationSize += sizeof(MetaData) + curr->next->allocationSize; 
			curr->next = curr->next->next; 
        }
        // curr and curr->next need to be merged, while the previous is NULL
        else if (prevNull && !currNull && !nextNull && curr->inUse == '0' && curr->next->inUse == '0') {
            curr->allocationSize += sizeof(MetaData) + curr->next->allocationSize;
            curr->next = curr->next->next;
            //printf("fourth condition \n");
        }
        //prev and curr need to be merged, while the curr->next is NULL
        else if (!prevNull && !currNull && nextNull && prev->inUse == '0' && curr->inUse == '0') {
            prev->allocationSize += sizeof(MetaData) + curr->allocationSize;
            prev->next = curr->next;
        }
	}
}
