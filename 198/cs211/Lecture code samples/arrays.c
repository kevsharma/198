#include <stdio.h>

void printArray(int* arr, int n)
{
    for (int i = 0; i < n; i++)
        printf("%d ", arr[i]);
    printf("\n");
}

int main(int argc, char* argv[])
{
    int numbers[] = {3, 7, 11, 4, -2};

    printf("%zu\n", sizeof(numbers)/sizeof(numbers[0]));
    printArray(numbers, 5);
}
