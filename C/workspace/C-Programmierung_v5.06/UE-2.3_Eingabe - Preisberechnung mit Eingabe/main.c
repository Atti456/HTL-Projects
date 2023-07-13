#include <stdio.h>

//Author: Artelsmair Alexander
//Date: 16.07.2018
//Description: Ein einfaches Programm zur Preisberechnung mit Benutzereingabe.

void draw_line(void);

int main() {

    float fEinstandspreis = 12.25;
    int iStueckzahl = 10;
    long lArtikelnummer = 12345678;
    float fMwSt = 0;
    float fGesamteinstPreis = 0;
    float fVerkaufspreis = 0;


    printf("Verkaufspreisberechnung\n");
    draw_line();
    printf("Bitte Artikelnummer eingeben: ");
    scanf("%d",&lArtikelnummer);
    fflush(stdin);
    printf("Bitte Stueckzahl eingeben: ");
    scanf("%d",&iStueckzahl);
    fflush(stdin);
    printf("Bitte Einstandspreis eingeben: ");
    scanf("%f",&fEinstandspreis);
    draw_line();


    fGesamteinstPreis = fEinstandspreis*iStueckzahl;
    fMwSt = fGesamteinstPreis*0.2;
    fVerkaufspreis = fGesamteinstPreis + fMwSt;


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