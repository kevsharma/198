#include <stdio.h>
#include <string.h>

int main(int argc, char* argv[])
{
    char* s1 = "hello";
    char s2[6];
    char* s3 = "abc";
    char* s4 = "def";
    char s5[10];

    strcpy(s2, s1);
    if (strcmp(s1, s2) == 0)
        printf("same\n");
    else
        printf("different\n");

    printf("%zu\n", strlen(s1));

    printf("%c\n", s1[1]);

    printf("%d\n", strcmp(s3, s4));
    printf("%d\n", strcmp(s4, s3));

    s5[0] = 'h';
    s5[1] = 'i';
    s5[2] = '\0';
    printf("%s\n", s5);
}
