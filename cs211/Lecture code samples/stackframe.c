#include <stdio.h>

int h(int x)
{
    return 4 / x;
}

int g(int x)
{
    return h(x - 20);
}

int f(int x)
{
    return g(2 * x);
}

int main(int argc, char* argv[])
{
    int x = 10;
    f(x);
}
