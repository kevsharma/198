#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int get(unsigned short x, unsigned short n){
    x = x & (1 << n);
    
    if(x^0){
        return 1;
    }
    return 0;
}

// returns 1 if true, 0 if false; RECURSIVE
int isPalindrome(unsigned short x, unsigned short n){
    if(n==8){
        // all units checked (base case n = 8)
        printf("Is-Palindrome\n");
        return 1;
    } 

    if(get(x,n) ^ get(x, 15-n)){
        printf("Not-Palindrome\n");
        return 0;
    }
    
    return isPalindrome(x, n+1);
}

int main(int argc, char* argv[])
{
    if(argc < 2){
        printf("Error: number not given.");
        return 0;
    }

    unsigned short x = (unsigned short)(atoi(argv[1]));
    isPalindrome(x, 0);
}
