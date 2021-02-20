#include <stdio.h>
#include <stdlib.h>
#include "arraylist.h"

int cmp(const void *a, const void *b)
{
    int x = *(int*)a;
    int y = *(int*)b;
    if (x < y) return -1;
    if (x == y) return 0;
    return 1;
}

int main(int argc, char **argv)
{
    int d, i;
    arraylist_t A;
    al_init(&A, 2);

    while (1 == scanf("%d", &d)) {
	al_append(&A, d);
    }

    qsort(A.data, A.used, sizeof(int), cmp);

    for (i = 0; i < A.used; ++i) {
	printf("%d\n", A.data[i]);
    }

    al_destroy(&A);

    return EXIT_SUCCESS;
}
