#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<string.h>

/////GLOBAL variables (master caches and cachecounter answer) //////
struct block** nomaster; 
struct cachecounter answer; 
struct block** premaster; 
int rank; 
int lru; 
//////////////////////////////////////////////////////////////////

struct block {
  unsigned long long int tag; 
  int valid; 
  int rank; 
  int lru; 
}; 

struct cachecounter {
  int A; // how many blocks can be in a given set 
  int noread; 
  int nowrite; 
  int preread; 
  int prewrite; 
  int nohit; 
  int nomiss; 
  int prehit; 
  int premiss; 
}; 

// counts the current items in a set
int count(struct block** master, int setindex, int A) {
  // loop through and count
  int count = 0; 
  for (int i = 0; i < A; i++) {
    if (master[setindex][i].tag != 0) {
      count++; // if there is a value, else do nothing 
    }
  }
  //printf("count is: %d\n", count); 
  return count; 
}

// This method prints the values of the answer structure 
void printanswer() {
  printf("no-prefetch\n"); 
  printf("Memory reads: %d\n", answer.noread); 
  printf("Memory writes: %d\n", answer.nowrite); 
  printf("Cache hits: %d\n", answer.nohit); 
  printf("Cache misses: %d\n", answer.nomiss); 
  
  printf("with-prefetch\n"); 
  printf("Memory reads: %d\n", answer.preread); 
  printf("Memory writes: %d\n", answer.prewrite); 
  printf("Cache hits: %d\n", answer.prehit); 
  printf("Cache misses: %d\n", answer.premiss);
  
}

// initializes the cache (using a Hash Table model)
struct block** create(struct block** master, int amountofsets, int A) {
  master = (struct block**) malloc(sizeof(struct block*) * amountofsets); 
  // allocate for the space
  for (int i = 0; i < amountofsets; i++) {
    master[i] = (struct block*) malloc(sizeof(struct block) * A); 
  }
  // initialize all fields to 0
  for (int i = 0; i < amountofsets; i++) {
    for (int j = 0; j < A; j++) {
      master[i][j].tag = 0; 
      master[i][j].valid = 0;
      master[i][j].rank = 0; 
      master[i][j].lru = 0; 
    }
  }
  return master; 
}

// this will enqueue an item into the set, setindex will function as the key in a HT 
void insert(struct block** master, unsigned long long int tag, int setindex, int A) {
  // search set and input it 
  for (int i = 0; i < A; i++) {
    if (master[setindex][i].tag == 0) {
      master[setindex][i].rank = rank; 
      master[setindex][i].tag = tag;
      master[setindex][i].valid = 1; 
      master[setindex][i].lru = lru; 
    }
  }
}

// removes an entry
void removeblock(struct block** master, int setindex, int A, char method) {
  // this is going to itterate through the blocks in the current set
  if (method == 'f') {
    int lowestrank = 0; 
    for (int i = 0; i < A; i++) {
      if (master[setindex][i].rank < master[setindex][lowestrank].rank) {
	lowestrank = i; 
      }
    }
    //printf("lowest rank: %d of rank: %d\n", lowestrank, master[setindex][lowestrank].rank); 
    master[setindex][lowestrank].valid = 0;
    master[setindex][lowestrank].rank = 0;
    master[setindex][lowestrank].tag = 0;
    master[setindex][lowestrank].lru = 0; 
  } else if (method == 'l') {
    int lowestlru = 0; 
    for (int i = 0; i < A; i++) {
      if (master[setindex][i].lru < master[setindex][lowestlru].lru) {
	lowestlru = i; 
      }
    }
    //printf("lru: %d\n", lru); 
    master[setindex][lowestlru].valid = 0;
    master[setindex][lowestlru].lru = 0;
    master[setindex][lowestlru].tag = 0;
    master[setindex][lowestlru].rank = 0; 
  }
}

// this will search the set for a specific tag (1 for a hit and 0 for a miss)
int search(struct block** master, unsigned long long int tag, int setindex, int A) {
  // search for the tag, 1 if found 0 if not
  for (int i = 0; i < A; i++) {
    if (master[setindex][i].valid == 1) {
      if (master[setindex][i].tag == tag) {
	// increment least recently used
	lru++; 
	master[setindex][i].lru = lru; 
	//lru++; 
	return 1; // tag found
      }
    } // else if is NULL and we continue until end of set
  }
  // tag not found
  return 0; 
}

////////////// This is no prefetch ///////////////

// first do the no-prefetch ones
void noDirect(int setindex, unsigned long long int tag, char readorwrite, int A) {
  // if hit (a set and block contains the current tag)
  if (search(nomaster, tag, setindex, A) == 1) {
    //printf("in the search is 1\n"); 
    if (readorwrite == 'W') {
      answer.nowrite = answer.nowrite + 1; 
    }
    // then increment the hit counter
    answer.nohit = answer.nohit + 1;
    lru++; 
  } else if (search(nomaster, tag, setindex, A) == 0){ // else if not, it is a miss
    // remove if full
    //answer.nomiss = answer.nomiss + 1;
    if (count(nomaster, setindex, A) == A) { // the super set is full
      // the set is full 
      //removeblock(nomaster, setindex, A); // removes the 1st in the queue
      nomaster[setindex][0].valid = 0;
      nomaster[setindex][0].rank = 0;
      nomaster[setindex][0].tag = 0; 
      nomaster[setindex][0].lru = 0; 
      // then insert
      insert(nomaster, tag, setindex, A); 
      lru++; 
      // on a no-pre miss, if W, increment read and write, if R increment read
      if (readorwrite == 'W') {
	answer.nowrite = answer.nowrite + 1;  
      }
      // increment miss counter
      answer.nomiss = answer.nomiss + 1; 
      answer.noread = answer.noread+1;
    } else { // insert the tag as a block into the super set, with all the other blocks in it
      insert(nomaster, tag, setindex, A); 
      lru++; 
      // on a no-pre miss, if W, increment read and write, if R increment read
      if (readorwrite == 'W') {
	answer.nowrite = answer.nowrite + 1; 
      }
      // increment miss counter
      answer.nomiss = answer.nomiss + 1; 
      answer.noread = answer.noread+1;
    }
  }
}
// if the associativity is just assoc, increment hit or miss and use FIFO
void noAssoc(int setindex, unsigned long long int tag, char readorwrite, int A, char method) {
// if hit (a set and block contains the current tag)
  if (search(nomaster, tag, setindex, A) == 1) {
    //printf("in the search is 1\n"); 
    if (readorwrite == 'W') {
      answer.nowrite = answer.nowrite + 1; 
    }
    // then increment the hit counter
    answer.nohit = answer.nohit + 1;
    lru++; 
  } else { // else if not, it is a miss
    // FIFO algo here ****
    if (count(nomaster, setindex, A) == A) { // the super set is full
      // the set is full 
      removeblock(nomaster, setindex, A, method); // removes the 1st in the queue
      // then insert
      lru++; 
      insert(nomaster, tag, setindex, A); 
      // on a write miss, increment write and read
      if (readorwrite == 'W') {
	answer.nowrite = answer.nowrite + 1; 
      }
      // increment counter for read and miss
      answer.nomiss = answer.nomiss + 1; 
      answer.noread = answer.noread + 1; 
    } else { // insert the tag as a block into the super set, with all the other blocks in it
      lru++; 
      insert(nomaster, tag, setindex, A); 
      // on a no-pre miss, if W, increment read and write, if R increment read
      if (readorwrite == 'W') {
	answer.nowrite = answer.nowrite + 1; 
	}
      // increment miss counter
      answer.nomiss = answer.nomiss + 1; 
      answer.noread = answer.noread + 1; 
    }
  }
}
// if the associativity is assoc:n, then increment hit or miss and use FIFO for sets of n blocks

void noAssocn(int setindex, unsigned long long int tag, char readorwrite, int A, char method) {
  // if hit (a set and block contains the current tag)
  if (search(nomaster, tag, setindex, A) == 1) {
    //printf("in the search is 1\n"); 
    if (readorwrite == 'W') {
      answer.nowrite = answer.nowrite + 1; 
    }
    // then increment the hit counter
    answer.nohit = answer.nohit + 1;
    lru++; 
  } else { // else if not, it is a miss
    // FIFO algo here ****
    if (count(nomaster, setindex, A) == A) { // the super set is full
      // the set is full 
      removeblock(nomaster, setindex, A, method); // removes the 1st in the queue
      // then insert
      lru++; 
      insert(nomaster, tag, setindex, A); 
      //lru++; 
      // on a write miss, increment write and read
      if (readorwrite == 'W') {
	answer.nowrite = answer.nowrite + 1; 
      }
      // increment counter for read and miss
      answer.nomiss = answer.nomiss + 1; 
      answer.noread = answer.noread + 1; 
    } else { // insert the tag as a block into the super set, with all the other blocks in it
      lru++; 
      insert(nomaster, tag, setindex, A); 
      //lru++; 
      // on a no-pre miss, if W, increment read and write, if R increment read
      if (readorwrite == 'W') {
	answer.nowrite = answer.nowrite + 1; 
	}
      // increment miss counter
      answer.nomiss = answer.nomiss + 1; 
      answer.noread = answer.noread + 1; 
    }
  }
}

//////// HERE will be the PREfetch methods /////////////


// first do the no-prefetch ones
void preDirect (int setindex, int presetindex, unsigned long long int tag, unsigned long long int preTag, char readorwrite, int A) {
  // if hit (a set and block contains the current tag)
  if (search(premaster, tag, setindex, A) == 1) {
    // then increment the hit counter
    //answer.prehit = answer.prehit + 1;
    if (readorwrite == 'W') {
      answer.prewrite = answer.prewrite + 1; 
    }
    answer.prehit = answer.prehit + 1; 
    lru++; 
  } else { // else if not, it is a miss
    // FIFO algo here ****
    if (count(premaster, setindex, A) == A) { // the super set is full
      // the set is full 
      premaster[setindex][0].valid = 0; 
      premaster[setindex][0].rank = 0;
      premaster[setindex][0].tag = 0;
      premaster[setindex][0].lru = 0; 
      // insert
      insert(premaster, tag, setindex, A); 
      lru++; 
      // increment read counter
      answer.preread = answer.preread + 1; 
    } else { // insert the tag as a block into the super set, with all the other blocks in it
      // insert
      insert(premaster, tag, setindex, A); 
      lru++; 
      // increment read counter
      answer.preread = answer.preread + 1; 
    } 
     ////// if the prefetched is not in the cache already//////
    if (search(premaster, preTag, presetindex, A) == 0) {
      if (count(premaster, presetindex, A) == A) {
	// the next set is full
	premaster[presetindex][0].valid = 0; 
	premaster[presetindex][0].rank = 0;
	premaster[presetindex][0].tag = 0;
	// insert
	insert(premaster, preTag, presetindex, A); 
	lru++; 
	answer.preread = answer.preread + 1; 
      } else {
	// insert
	insert(premaster, preTag, presetindex, A); 
	lru++; 
	// increment read counter
	answer.preread = answer.preread + 1; 
      }
    } //
    // on a pre miss, if W, increment read and write, if R increment read
    answer.premiss = answer.premiss + 1; 
    if (readorwrite == 'W') {
      answer.prewrite = answer.prewrite + 1;  
      //answer.preread = answer.preread + 1; 
    } 
  } 
}
// if the associativity is just assoc, increment hit or miss and use FIFO
void preAssoc(int setindex, int presetindex, unsigned long long int tag, unsigned long long int preTag, char readorwrite, int A, char method) {
  // if hit (a set and block contains the current tag)
  if (search(premaster, tag, setindex, A) == 1) {
    // then increment the hit counter
    if (readorwrite == 'W') {
      answer.prewrite = answer.prewrite + 1; 
    }
    // increment hit
    answer.prehit = answer.prehit + 1; 
    lru++; 
  } else { // else if not, it is a miss
    // FIFO algo here ****
    if (count(premaster, setindex, A) == A) { // the super set is full
      // the set is full 
      removeblock(premaster, setindex, A, method); 
      // then insert
      insert(premaster, tag, setindex, A); 
      lru++; 
      // increment read counter
      answer.preread = answer.preread + 1; 
    } else { // insert the tag as a block into the super set, with all the other blocks in it
      insert(premaster, tag, setindex, A); 
      lru++; 
      // increment read counter
      answer.preread = answer.preread + 1; 
    }
    ////// if the prefetched is not in the cache already//////
      if (search(premaster, preTag, presetindex, A) == 0) {
	if (count(premaster, presetindex, A) == A) { //pre
	  // the next set is full
	  removeblock(premaster, presetindex, A, method); 
	  // then insert
	  rank++;
	  insert(premaster, preTag, presetindex, A); 
	  lru++; 
	  // increment read counter
	  answer.preread = answer.preread + 1; 
	} else {
	  rank++; 
	  insert(premaster, preTag, presetindex, A);
	  lru++; 
	  // increment read counter
	  answer.preread = answer.preread + 1; 
	}
      }// else { ///
      //	answer.preread = answer.preread+1; 
      //}
    // on a pre miss, if W, increment read and write, if R increment read
    answer.premiss = answer.premiss + 1; 
    if (readorwrite == 'W') {
      answer.prewrite = answer.prewrite + 1; 
      //answer.preread = answer.preread+1; 
    } 
  } 
}
// if the associativity is assoc:n, then increment hit or miss and use FIFO for sets of n blocks
void preAssocn(int setindex, int presetindex, unsigned long long int tag, unsigned long long int preTag, char readorwrite, int A, char method) {
 // if hit (a set and block contains the current tag)
  if (search(premaster, tag, setindex, A) == 1) {
    // then increment the hit counter
    if (readorwrite == 'W') {
      answer.prewrite = answer.prewrite + 1; 
    }
    // increment hit
    answer.prehit = answer.prehit + 1; 
    lru++; 
  } else { // else if not, it is a miss
    // FIFO algo here ****
    if (count(premaster, setindex, A) == A) { // the super set is full
      // the set is full 
      removeblock(premaster, setindex, A, method); 
      // then insert 
      lru++; 
      insert(premaster, tag, setindex, A); 
      // increment
      answer.preread = answer.preread + 1; 
    } else { // insert the tag as a block into the super set, with all the other blocks in it
      lru++; 
      insert(premaster, tag, setindex, A); 
      //increment
      answer.preread = answer.preread + 1; 
    }
    ////// if the prefetched is not in the cache already//////
    if (search(premaster, preTag, presetindex, A) == 0) {
      if (count(premaster, presetindex, A) == A) {
	// the next set is full
	removeblock(premaster, presetindex, A, method); 
	// then insert
	rank++; 
	lru++; 
	insert(premaster, preTag, presetindex, A); 
	// increment
	answer.preread = answer.preread + 1; 
      } else {
	rank++; 
	lru++; 
	insert(premaster, preTag, presetindex, A); 
	// increment
	answer.preread = answer.preread + 1; 
      }
      //// below is the extra part from the search (below bracket and whatever)
    }/*else {
      lru++; 
    }
     */
    // on a pre miss, if W, increment read and write, if R increment read
    answer.premiss = answer.premiss + 1; 
    if (readorwrite == 'W') {
      answer.prewrite = answer.prewrite + 1; 
      //answer.preread = answer.preread+1; 
    } 
  } 
}

// int is power of 2
int powof2(int num) {
  
  if ((num & (num-1)) == 0) {
    // is a power of two 
    return 1; 
  }
  // not a power of two 
  return 0; 
}


// this will run our cache
int main(int argc, char** argv) {

  // check for any input errors (also have to check for powers of 2, so can't input 42
  if (argc < 5) {
    printf("error\n"); 
    return 0; 
  }
  // put each argument to its variable
  int cachesize = atoi(argv[1]);
  if (powof2(cachesize) == 0) {
    printf("error\n"); 
    return 0;
  }
  int lenA = strlen(argv[2]); 
  //char* associativity = (char*) malloc(lenA+1);
  //associativity = argv[2]; 
  // use this when extra credit is ready 
  
  //int lenB = strlen(argv[3]); 
  //char* policy = (char*) malloc(lenB+1);
  //policy = argv[3]; 
  char method = argv[3][0]; 
  //printf("method: %c\n", method); 
  // blocksize
  int B  = atoi(argv[4]); 
  if (powof2(B) == 0) {
    printf("error\n"); 
    return 0; 
  }
  // argv[5] is the tracefile 
  int A = 0; // associativity or blocks per set
  int S = 0; // number of sets 


  if (argv[2][0] == 'd') {
    A = 1; 
    S = cachesize/B; 
  } else if (argv[2][5] != ':') { 
    A = cachesize/B; 
    S = 1; 
  } else {  // if assoc:n, need to find the n 
    char* num; 
    int digitcount = 0; 
    for(int i = 6; i < lenA; i++) {
      // if it is a digit 
      digitcount++; 
    }
    // now create the string of the associativity n  
    num = (char*) malloc(digitcount+1); 
    for (int i = 0; i < lenA-6; i++) {
      // if it is a digit 
      num[i] = argv[2][i+6]; 
    }
    // now convert the associativity n into an integer 
    A = atoi(num); 
    if (powof2(A) == 0) {
      printf("error\n"); 
      return 0;
    }
    int temp = A*B; 
    S  = cachesize/temp;
  }
 
  // create a tag for the current address we are on (no-prefetch)
  // find block offset bits, call log2 method 
  //double blockoffset = logb2(B);
  //int blockoffset = (int)logb2(B);
  int blockoffset = log2(B); 
  // find set index bits, call log2 method again
  //double setindex = logb2(S)
  //int setindexbits = (int)logb2(S);  
  int setindexbits = log2(S); 
  //double seti = 0; 

  // create the sets
  nomaster = create(nomaster, S, A); 
  premaster = create(premaster, S, A); 
  
    
  FILE *fp;
  fp = fopen(argv[5], "r"); 
    
  char readorwrite; 
  unsigned long long int address; 
  unsigned long long int programcounter; 
  //int rank = 0; 
  rank = 0; 
  lru = 0; 
  // now itterate through the tracefile (loop through until #eof)
  while (fscanf(fp, "%llx: %c %llx\n", &programcounter, &readorwrite, &address) == 3) { // fix this statement, c will be the R or W, the first will be the PC which we dont care about, and the last is our address that we need, potentially stored in an int but has the address to it 

    // first, calculate the actual setindex
    int setindex = ((address >> blockoffset) & ((1 << setindexbits)-1));
    int presetindex = (((address+B) >> blockoffset) & ((1 << setindexbits)-1));

    //printf("setindex: %d, presetindex: %d\n", setindex, presetindex); 

    // then, create the tag:
    unsigned long long int tag = address >> (blockoffset + setindexbits); 

    // create a tag for the address + 4 for a prefetch 
    // now, the preTag will be the rightshifted of the original address by the (blockoffset + setindex)
    unsigned long long int preTag = (address+B) >> (blockoffset + setindexbits); 
    // now check for direct, assoc, assoc:n to do the prefetch or noprefetch methods on them
    
    if (argv[2][0] == 'd') {
      // no prefetch 
      noDirect(setindex, tag, readorwrite, A); 
      // prefetch 
      preDirect(setindex, presetindex, tag, preTag, readorwrite, A); 
    } else if (argv[2][5] != ':') { 
      // no prefetch 
      noAssoc(setindex, tag, readorwrite, A, method); 
      // prefetch
      preAssoc(setindex, presetindex, tag, preTag, readorwrite, A, method); 
    } else { // it is assoc:n 
      // no prefetch 
      noAssocn(setindex, tag, readorwrite, A, method); 
      // prefetch
      preAssocn(setindex, presetindex, tag, preTag, readorwrite, A, method); 
    }
      
    rank++;
    
    lru++; 
    
  } // end of while 
  // print answer
  //printanswer(); 
  //printm(S, A); 
  printanswer(); 
  return 0; 
}
