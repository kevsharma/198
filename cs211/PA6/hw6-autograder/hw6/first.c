#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

struct node {
    unsigned long long tag;
    struct node* next; 
};
typedef struct node Node;


int main(int argc, char* argv[])
{
    /* This program has 3 parts: (1) Reading input, (2) Initializing Cache and Relevant information, and (3) Simulating Cache */

    /* Part 1 */
    if(argc < 6){
        printf("error\n");
        return 0;
    }
    /*  0          1          2             3              4            5 */
    /* ./first <cache size> <assoc> <replace policy> <block size> <trace file> */

    int cacheSize = atoi(argv[1]);
    int blockSize = atoi(argv[4]);

    // Ensure cacheSize and blockSize are >=1 & a power of two.
    if(cacheSize < 1 || blockSize < 1){
        printf("error\n");
        return 0;
    }

    int testCache = cacheSize;
    while(testCache != 1){
        if(testCache % 2 != 0){ // 18 mod 2 = 0. 18 div 2 = 9. 9 mod 2 !=0 thus not a power of 2.
            printf("error\n");
            return 0;
        }
        testCache = testCache / 2;
    }

    int testBlock = blockSize;
    while(testBlock != 1){
        if(testBlock % 2 != 0){
            printf("error\n");
            return 0;
        }
        testBlock = testBlock / 2;
    }

    // Check for associativity.
    char* associativity = argv[2];
    if(!(strcmp(associativity, "direct")==0 || strcmp(associativity, "assoc")==0 || strncmp(associativity, "assoc:",6)==0)){
        printf("error\n");
        return 0;
    }

    char* replacePolicy = argv[3];
    if(!(strcmp(replacePolicy, "lru")==0 || strcmp(replacePolicy, "fifo")==0)){
        printf("error 40");
        return 0;
    }
    
    
    FILE *fp = fopen(argv[5], "r");
    if(fp == NULL || replacePolicy == NULL || associativity == NULL){
        printf("error\n");
        return 0;
    }

    /************************************************************************************************************************/


    /* Part 2 */
    unsigned long long numSets, numLines, numLinesPerSet;
    numSets = numLines = numLinesPerSet = 0;
    
    numLines = cacheSize / blockSize;
    // Populate numSets and numSetsPerLine according to associativity:
    if(strcmp(associativity, "direct") == 0){
        // 1 Line per Set : E = 1;
        numSets = numLines;
        numLinesPerSet = 1;
    }
    else if(strcmp(associativity, "assoc") == 0){
        // 1 Set : S = 1;
        numSets = 1;
        numLinesPerSet = numLines;
    }
    else{
        sscanf(associativity, "assoc:%llx", &numLinesPerSet);
        numSets = numLines / numLinesPerSet;
    }

    int numBlockBits = log2(blockSize);
    int numSetBits = log2(numSets);

    // Initialize 2d Array of LinkedLists
    Node** cache = (Node **) malloc(numSets * sizeof(Node *));
    for(int i=0; i<numSets; i++){
        *(cache + i) = NULL; // *(cache+i) = cache[i] sets the ith pointer to a Node to NULL
    }

    /************************************************************************************************************************/


    /* Part 3 */
    int memoryReads, memoryWrites, cacheHits, cacheMisses;
    memoryReads = memoryWrites = cacheHits = cacheMisses = 0;

    
    /* These are used to store input data */
    unsigned long long pc, address, setvalue, tagvalue; 
    char operation;

    while(1){
        int n = fscanf(fp, "%llx: %c %llx", &pc, &operation, &address);
        if(n != 3){
            break;
        }

        // Populate tagvalue and setvalue appropriately.
        setvalue = (address >> numBlockBits) & ((1 << numSetBits) -1);
        tagvalue = address >> (numSetBits + numBlockBits);
    
        if(strcmp(replacePolicy, "lru") == 0){
            Node* thisNode = (Node *) malloc(sizeof(Node));
            thisNode->tag = tagvalue;
            thisNode->next = NULL;

            // If nothing stored in that set, then automatically add.
            if(*(cache+setvalue) == NULL){
                ++cacheMisses;
                ++memoryReads;
                if(operation == 'W'){
                    ++memoryWrites;
                }

                *(cache + setvalue) = thisNode;
                continue;
            }

            // Set is not empty.
            Node* head = *(cache + setvalue);
            Node* ptr; Node* prev;
            ptr = prev = head;
            // Check whether thisNode exists in the set. If it does, increment cacheHit and move that node to the front.
            int found = 0;

            while(ptr!= NULL){
                if(ptr->tag == tagvalue){
                    ++cacheHits;
                    if(operation == 'W'){
                        ++memoryWrites;
                    }

                    if(ptr!=prev){
                        prev->next = ptr->next;
                        ptr->next = head;
                        *(cache + setvalue) = ptr;
                    }
                    
                    found = 1;
                    break;
                }

                prev = ptr;
                ptr = ptr->next;
            }

            // If we did find it, we have no use for thisNode anymore.
            if(found==1){
                free(thisNode);
            }

            // If it is not found, add it to the front and trim to ensure # nodes = numLinesPerSet
            if(found == 0){
                ++cacheMisses;
                ++memoryReads;
                if(operation == 'W'){
                    ++memoryWrites;
                }

                thisNode->next = head;
                *(cache + setvalue) = thisNode;

                // Trim
                unsigned long long count = 0;
                prev = ptr = thisNode;
                while(1){
                    if(count == numLinesPerSet){
                        prev->next = NULL;
                        if(ptr!=NULL){
                            free(ptr);
                        }
                        break;
                    }

                    if(ptr == NULL){
                        break;
                    } 
                    
                    ++count;
                    prev = ptr;
                    ptr = ptr->next;
                }
            }
        }

        else{ // FIFO
            Node* thisNode = (Node *) malloc(sizeof(Node));
            thisNode->tag = tagvalue;
            thisNode->next = NULL;

            // If nothing stored in that set, then automatically add.
            if(*(cache+setvalue) == NULL){
                ++cacheMisses;
                ++memoryReads;
                if(operation == 'W'){
                    ++memoryWrites;
                }

                *(cache + setvalue) = thisNode;
                continue;
            }

            // Set is not empty.
            Node* head = *(cache + setvalue);
            Node* ptr; Node* prev;
            ptr = prev = head;
            // Check whether thisNode exists in the set. If it does, increment cacheHit and move that node to the front.
            int found = 0;

            while(ptr!= NULL){
                if(ptr->tag == tagvalue){
                    ++cacheHits;
                    if(operation == 'W'){
                        ++memoryWrites;
                    }

                    found = 1;
                    break;
                }

                prev = ptr;
                ptr = ptr->next;
            }

            // If we did find it, we have no use for thisNode anymore.
            if(found==1){
                free(thisNode);
            }

            if(found == 0){
                ++memoryReads;
                ++cacheMisses;
                if(operation == 'W'){
                    ++memoryWrites;
                }
                // Add it to the back. Keep track of how many times you moved ptr.
                // If that count equals limit, then remove the front.
                unsigned long long count = 0;
                ptr = prev =  head;
                while(ptr != NULL){
                    ++count;
                    prev = ptr;
                    ptr = ptr->next;
                }

                prev->next = thisNode;

                if(count == numLinesPerSet){
                    *(cache + setvalue) = head->next;
                    free(head);
                }
            }
        }
    }
    
    /************************************************************************************************************************/

    /* Freeing memory, then printing results.*/
    fclose(fp);
    for(int i=0; i<numSets; i++){
        // Free each node in every LinkedList.
        Node* head = *(cache + i);
        Node* temp;
        while(head!= NULL){
            temp = head;
            head = head->next;
            free(temp);
        }
        head = temp = NULL;
    }
    free(cache);
    
    printf("Memory reads: %d\n", memoryReads);
    printf("Memory writes: %d\n", memoryWrites);
    printf("Cache hits: %d\n", cacheHits);
    printf("Cache misses: %d\n", cacheMisses);
    
    return 0;
}