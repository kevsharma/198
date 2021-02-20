#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char* makeCaps(char* s)
{
    char* newString = malloc(strlen(s) + 1);
    // ...
    return newString;
}

int main(int argc, char* argv[])
{
    char* s1 = "hello";
    char* s2 = makeCaps(s1);
    printf("%s\n", s2);
    free(s2);
}
