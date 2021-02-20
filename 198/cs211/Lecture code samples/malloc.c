#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[])
{
/*
    int* p = NULL; // malloc(...)
    if (p == NULL)
        return 1;

    int x = p[0];
*/

    int* p2 = malloc(5 * sizeof(int));
    p2[0] = 42;
    p2[1] = 100;
    p2[2] = 17;
    p2[3] = 99;
    p2[4] = 1024;

    for (int i = 0; i < 5; i++)
        printf("%d ", p2[i]);
    printf("\n");

    free(p2);

    int** arr2d = malloc(3 * sizeof(int*));
    for (int i = 0; i < 2; i++)
        arr2d[i] = malloc(3 * sizeof(int));

    for (int i = 0; i < 2; i++)
        for (int j = 0; j < 2; j++)
            arr2d[i][j] = i + j;

    for (int i = 0; i < 2; i++)
        free(arr2d[i]);
    free(arr2d);
}
