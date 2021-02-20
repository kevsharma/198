#include <stdio.h>
#include <stdlib.h>
#include <string.h>


// returns 1 if odd, 0 if even.
unsigned short Parity(unsigned short x){
    unsigned short count;
    while(x ^ 0){ // if evaluated then x!=0 then true, if x==0, then false.
        // check if last digit is 1, if so increment count and shift x over.
        if((x & 1)^0) // if true, last digit is 1
            ++count;
        x = x >> 1;
    }
    
    // determine if count is even (if so return 1)
    return count & 1;
}

// prints numPairs of 1s
unsigned short numPairs(unsigned short x){
    unsigned short count = 0;
    while(x ^ 0){
        if((x & 1)^0){ // if last digit is 1
            x = x >> 1; // move one over.

            if(x ^ 0){ // x!=0 if evaluated, there is still a digit
                if((x&1)^0) // if new last digit is 1, then increment the count
                    ++count;
            } 
            else{
                return count;
            }
        }
        x = x >> 1;
    }
    
    // determine if count is even (if so return 1)
    return count;
}

int main(int argc, char* argv[])
{
    
    if(argc < 2){
        printf("Error: Number not given.");
        return 0;
    }

    unsigned short x = atoi(argv[1]);
    
    if(Parity(x)){ // value is not 0
        printf("Odd-Parity\t");
    }
    else{
        printf("Even-Parity\t");
    }
    
    printf("%d\n", numPairs(x));
    return 0;
}
