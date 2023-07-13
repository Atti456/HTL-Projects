#include <stdio.h>

//Author: Artelsmair Alexander
//Date: 15.07.2018
//Description: Ein einfaches Programm zur Berechnung des Volumens, Mantelflaeche und der Oberflaeche eines geraden Kreiszylinders.

const float pi = 3.1415926536F;
void draw_line(void);

int main() {

    float fRadius = 0;
    float fHoehe = 0;
    float fVolumen = 0;
    float fOberflaeche = 0;
    float fMantelflaeche = 0;

    printf("Berechnung der Oberflaeche eines geraden Kreiszylinders.\n");
    draw_line();
    printf("Eingabe Radius: \n");

    scanf("%f",&fRadius);
    fflush(stdin);
    printf("Eingabe Hoehe: \n");

    scanf("%f",&fHoehe);
    fflush(stdin);
    draw_line();


    fVolumen = (fRadius*fRadius*pi*fHoehe);
    fMantelflaeche = (2*fRadius*pi*fHoehe);
    fOberflaeche = (2*fRadius*pi+2*fRadius*fRadius*pi*fHoehe);


    printf("Berechnete Werte\n");
    draw_line();
    printf("Volumen des Zylinders: %2.2f\n",fVolumen);
    printf("Mantelflaeche des Zylinders: %2.2f\n",fMantelflaeche);
    printf("Oberflaeche des Zylinders: %2.2f\n",fOberflaeche);
    draw_line();
    return 0;
}

void draw_line(){

    printf("------------------------------\n");
}