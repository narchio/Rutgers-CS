#include "mymalloc.h"
#include <stdio.h>
#include<time.h>
#include <sys/time.h>

// NEED 6 programs to test and clock outputs

// testA: malloc() 1 byte and immediately free it - do this 150 times
int testA () {
    
    int i = 0;
    // these two variables will be used for the gettimeofday function
    struct timeval beginning;
    struct timeval endofload;
    gettimeofday(&beginning, 0);
    
    for (i = 0; i < 150; i++) {
        char* mal = (char*) malloc(1);
        free(mal);
    }
    gettimeofday(&endofload, 0);
    int loadtime = ((endofload.tv_sec - beginning.tv_sec) * 1000000) + (endofload.tv_usec - beginning.tv_usec);
    return loadtime;
}

// testB: malloc() 1 byte, store the pointer in an array - do this 150 times.
// Once you've malloc()ed 50 byte chunks, then free() the 50 1 byte pointers one by one.
int testB () {
    // these two variables will be used for the gettimeofday function
    struct timeval beginning;
    struct timeval endofload;
    gettimeofday(&beginning, 0);
    
    char* arr[150];
    int i = 0;
    while (i <= 150) {
        arr[i] = (char*) malloc(sizeof(char)*1);
        if ((i % 50 == 0) && (i != 0)) {
            int k;
            if (i == 50) {
                k = i - 50;
            } else {
                k = i - 49;
            }
            while(k <= i) {
                if (arr[k] != NULL) {
                    free(arr[k]);
                }
                ++k;
            }
        }
        ++i;
    }
    gettimeofday(&endofload, 0);
    int loadtime = ((endofload.tv_sec - beginning.tv_sec) * 1000000) + (endofload.tv_usec - beginning.tv_usec);
    return loadtime;
}

//  testC: Randomly choose between a 1 byte malloc() or free()ing a 1 byte pointer
//   > do this until you have allocated 50 times
// - Keep track of each operation so that you eventually malloc() 50 bytes, in total
//   > if you have already allocated 50 times, disregard the random and just free() on each iteration
//  - Keep track of each operation so that you eventually free() all pointers
//    > don't allow a free() if you have no pointers to free()


int testC() {
    // these two variables will be used for the gettimeofday function
    struct timeval beginning;
    struct timeval endofload;
    gettimeofday(&beginning, 0);
    
    char* array[1000];
    int i = 0;
    for (i = 0; i < 1000; i++) {
        array[i] = NULL;
    }
    
    int malloced = 0;
    int allocated = 0;
    int freed = 0;
    int malCount = 0;
    int freeCount = 0;
    
    while (allocated < 50) {
        int ranNum = (rand() % 2); // 0 for malloc and 1 for free
        
        if (ranNum == 0) { // malloc
            if (array[malCount] == NULL) {
                array[malCount] = (char*) malloc(sizeof(char) * 1);
                ++malCount;
                ++allocated;
            }
        } else if (ranNum == 1) {
            if (array[freeCount] == NULL) {
                continue;
            } else {
                free(array[freeCount]);
                ++freeCount;
            }
        }
    }
    int j = freeCount;
    while (j < 1000) {
        if (array[j] != NULL) {
            free(array[j]);
        }
        ++j;
    }
    
    // now calculate the clock speed for 1 run
    gettimeofday(&endofload, 0);
    int loadtime = ((endofload.tv_sec - beginning.tv_sec) * 1000000) + (endofload.tv_usec - beginning.tv_usec);
    return loadtime;
}


//  testD: Randomly choose between a randomly-sized malloc() or free()ing a pointer – do this many times (see below)
// - Keep track of each malloc so that all mallocs do not exceed your total memory capacity
// - Keep track of each operation so that you eventually malloc() 50 times
// - Keep track of each operation so that you eventually free() all pointers
// - Choose a random allocation size between 1 and 64 bytes

long testD() {
    // these two variables will be used for the gettimeofday function
    struct timeval beginning;
    struct timeval endofload;
    gettimeofday(&beginning, 0);
    
    char* array[4096];
    int i = 0;
    for (i = 0; i < 4096; i++) {
        array[i] = NULL;
    }
    
    int byteCounter = 4096;
    int allocated = 0;
    int malCount = 0;
    int freeCount = 0;
    
    while (allocated < 50) {
        int ranNum = (rand() % 2); // 0 for malloc and 1 for free
        int ranBytes = ( rand() % (64 + 1 - 1) + 1);
        if (ranNum == 0) { // malloc
            if (array[malCount] == NULL) {
                array[malCount] = (char*) malloc(sizeof(char) * ranBytes);
                ++malCount;
                ++allocated;
                byteCounter -= ranBytes - sizeof(MetaData);
            } else {
                // end of list
                break;
            }
        } else if (ranNum == 1) {
            if (array[freeCount] == NULL) {
                continue;
            } else {
                free(array[freeCount]);
                ++freeCount;
            }
        }
    }
    int j = freeCount;
    while (j < 4096) {
        if (array[j] != NULL) {
            free(array[j]);
        }
        ++j;
    }
    
    // now calculate the clock speed for 1 run
    gettimeofday(&endofload, 0);
    long loadtime = ((long)(endofload.tv_sec - beginning.tv_sec) * 1000000) + (long)(endofload.tv_usec - beginning.tv_usec);
    return loadtime;
}


//testE,testF: Two more workloads of your choosing
// - Describe both workloads in your testplan.txt

//Test E: reverse free
int testE() {
    
    // these two variables will be used for the gettimeofday function
    struct timeval beginning;
    struct timeval endofload;
    gettimeofday(&beginning, 0);
    
    char* a[150];
    int s = 0;
    for (s = 0; s < 150; s++) {
        a[s] = (char*) malloc(1);
    }
    // now free ptrs in reverse order
    for (s = 149; s >= 0; s--) {
        free(a[s]);
    }
    // now calculate the clock speed for 1 run
    gettimeofday(&endofload, 0);
    int loadtime = ((endofload.tv_sec - beginning.tv_sec) * 1000000) + (endofload.tv_usec - beginning.tv_usec);
    return loadtime;
}

//Test F: malloc by 2's until capacity (backwards loading into the array), then free backwards
int testF() {
    
    // these two variables will be used for the gettimeofday function
    struct timeval beginning;
    struct timeval endofload;
    gettimeofday(&beginning, 0);
    
    char* a8[157];
    int v;
    for (v = 0; v < 157; v++) {
        a8[v] = (char*) malloc(sizeof(char)*2);
    }
    // free even in order
    for (v = 0; v < 157; v = v + 2) {
        if (a8[v] != NULL) {
            free(a8[v]);
        }
    }
    // free odd indexes backwards
    for (v = 155; v > 0; v = v - 2) {
        if (a8[v] != NULL) {
            free(a8[v]);
        }
    }
    // now calculate the clock speed for 1 run
    gettimeofday(&endofload, 0);
    int loadtime = ((endofload.tv_sec - beginning.tv_sec) * 1000000) + (endofload.tv_usec - beginning.tv_usec);
    return loadtime;
}

// now will test the runtimes/clockspeeds in main
int main(int argc, char** argv){
    
    int A;
    int B;
    int C;
    long D;
    int E;
    int F;
    int i;
    
    srand(time(NULL));
    // itterate through for 100 cycles
    for (i = 0; i < 100; i++) {
        A += testA();
        B += testB();
        C += testC();
        D += testD();
        E += testE();
        F += testF();
    }
    int meanA = A/100;
    int meanB = B/100;
    int meanC = C/100;
    long meanD = D/100;
    int meanE = E/100;
    int meanF = F/100;
    
    printf("The average of 100 workloads for test A is %d in microseconds\n", meanA);
    printf("The average of 100 workloads for test B is %d in microseconds\n", meanB);
    printf("The average of 100 workloads for test C is %d in microseconds\n", meanC);
    printf("The average of 100 workloads for test D is %ld in microseconds\n", meanD);
    printf("The average of 100 workloads for test E is %d in microseconds\n", meanE);
    printf("The average of 100 workloads for test F is %d in microseconds\n", meanF);
    
    return 0;
}
