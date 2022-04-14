#include <stdio.h>

int main(int argc, char* argv[])
{
    int x = 1;
    char* p = (char*) &x;

    if (*p == 0)
        printf("Big endian\n");
    else
        printf("Little endian\n");
}
