#include <stdio.h>

typedef struct Foo
{
    struct Foo* otherFoo;
    int x;
    int y;
} Foo;

typedef float feet;
typedef float meters;

int main(int argc, char* argv[])
{
    feet f = 6.0;
    meters m = 2.0;
    int length = f + m;

    Foo foo;
    
}
