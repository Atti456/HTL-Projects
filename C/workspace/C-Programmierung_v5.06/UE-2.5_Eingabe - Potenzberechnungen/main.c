#include <stdio.h>
#include <math.h>

//Author: Artelsmair Alexander
//Date: 16.07.2018
//Description: Ein einfaches Programm zur Berechnung der Potenzen und der Quadratwurzel einer eingegebenen Zahl.

void draw_line(void);

void draw_line()
{
    printf("---------------------------------\n");
}

int main() {

    float fZahl = 0;
    float fP2 = 0;
    float fP3 = 0;
    float fsqrt = 0;

    printf("Berechnung der Potenzen und der Quadratwurzel einer eingegebenen Zahl\n");
    draw_line();
    printf("Eingegebene Zahl: ");
    scanf("%f",&fZahl);
    fflush(stdin);
    draw_line();


    fP2 = powf(fZahl,2);
    fP3 = powf(fZahl,3);
    fsqrt = sqrtf(fZahl);


    printf("Zweier-Potenz: %2.2f\n",fP2);
    printf("Dreier-Potenz: %2.2f\n",fP3);
    printf("Quadratwurzel: %2.2f\n",fsqrt);
    draw_line();

    return 0;
}