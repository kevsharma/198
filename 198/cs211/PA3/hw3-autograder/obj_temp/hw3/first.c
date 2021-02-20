#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void set(unsigned short *ptr, unsigned short n, unsigned short v){
    if(v ^ 0){ // if true, then v and 0 are different. i.e v = 1
        // Setting a bit to 1 is easy.
        *ptr |= (1<<n);
        printf("%hu\n", *ptr);
        return;
    }
    
    /*
    *   // Setting a bit to 0, is same as clearing that bit.
    *   001100 = 12
    *   Set the third bit to 0, then we want: 000100
    *   let clear be: 1000, ~1000 = 0111
    *   clear = 110111
    * 
    *    001100 (*ptr)
    *   &  0111 (clear)
    *   --------
    *   000100 = 4 
    */
    unsigned short clear = ~(1<<n);
    *ptr = *ptr & clear;    
    printf("%hu\n", *ptr);
}

int get(unsigned short x, unsigned short n){
    x = x & (1 << n);
    
    if(x^0){
        return 1;
    }
    return 0;
}

void comp(unsigned short *ptr, unsigned short n){
    int valueOfnth = get(*ptr, n);
    if(valueOfnth ^ 0){ // if true, then valueOfnth and 0 are different.
        set(ptr, n, (unsigned short)0);
        return;
    } 
    set(ptr, n, (unsigned short)1);
}

int main(int argc, char* argv[])
{
    if(argc < 1){
        printf("Input file not provided.");
        return 0;
    }

	FILE *fp = fopen(argv[1], "r");
    if(fp==NULL){
        printf("File not found.\n");
        return 0;
    }
    
    unsigned short x;
    fscanf(fp, "%hu\n", &x);

    while(1)
    {
        char call[5];
        unsigned short n;
        unsigned short v;

        int matched = fscanf(fp, "%s\t%hu\t%hu\n", call, &n, &v);

        if(matched!=3)
            break;
        
        if(strncmp(call, "set", 5) == 0){
            set(&x, n, v);
        }
        else if(strncmp(call, "get", 5) == 0){
            printf("%d\n", get(x, n));
        }
        else if(strncmp(call, "comp", 5) == 0){
            comp(&x, n);
        }
    }
    fclose(fp);
}