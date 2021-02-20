#include <stdio.h>
#include <string.h>

typedef struct Plane
{
    char flightNum[7];
    int altitude;
    int longitude;
    int latitude;
    double airSpeed;
} Plane;

int main(int argc, char* argv[])
{
    Plane p1;
    p1.altitude = 30000;
    // p1.flightNum = "UAL196";
    p1.longitude = 90;
    p1.latitude = 23;
    strcpy(p1.flightNum, "UAL196");
    p1.airSpeed = 500.0;

    printf("%s\n", p1.flightNum);
    printf("%d\n", p1.longitude);
    printf("%d\n", p1.latitude);

    Plane p2 = p1;

    printf("%s\n", p2.flightNum);
    printf("%d\n", p2.longitude);
    printf("%d\n", p2.latitude);

    Plane p3 = { "UAL123", 10000, 123.5, 42.3, 300.0 };
}
