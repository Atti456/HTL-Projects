#include <stdio.h>

//Author: Artelsmair Alexander
//Date: 15.07.2018
//Description: Ein einfaches Programm zur Preisberechnung.

void draw_line(void);

int main() {

    float fEinstandspreis = 12.25;
    int iStueckzahl = 10;
    long lArtikelnummer = 12345678;
    float fMwSt = 0;
    float fGesamteinstPreis = 0;
    float fVerkaufspreis = 0;


    fGesamteinstPreis = fEinstandspreis*iStueckzahl;
    fMwSt = fGesamteinstPreis*0.2;
    fVerkaufspreis = fGesamteinstPreis + fMwSt;


    printf("Verkaufspreisberechnung\n");
    draw_line();
    printf("Artikelnummer: %d EUR\n",lArtikelnummer);
    printf("Stückzahl: %d EUR\n",iStueckzahl);
    printf("Einstandspreis: %2.2f EUR\n",fEinstandspreis);
    draw_line();
    printf("Gesamt-Einstandspreis: %2.2f EUR\n",fGesamteinstPreis);
    printf("Mehrwertssteuer (+): %2.2f EUR\n",fMwSt);
    printf("Verkaufspreis: %2.2f EUR\n",fVerkaufspreis);
    draw_line();
    return 0;
}

void draw_line()
{
    printf("------------------------------------------\n");
}