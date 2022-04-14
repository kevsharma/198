#include <stdio.h>

int main(int argc, char* argv[])
{
    int x = 42;
    int* p = &x;
    int a[100];

    printf("%p\n", p);
    printf("%p\n", &p);
    printf("%p\n", a);
    printf("%p\n", &a);
}
