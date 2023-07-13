#include <stdio.h>

//Author: Artelsmair Alexander
//Date: 16.07.2018
//Description: Ein einfaches Programm zur Berechnung des Bezugspreises einer Ware.

void draw_line(void);

void draw_line()
{
    printf("---------------------------------\n");
}

int main() {


    float fPreis = 0;
    int iStueck = 0;
    float fRabatt = 0;
    float fSkonto = 0;
    float fVersandkosten = 0;

    float fListenpreis = 0;
    float fRabattAbzug = 0;
    float fZieleinkaufspreis = 0;
    float fSkontoAbzug = 0;
    float fBareinkaufspreis = 0;
    float fBezugspreis = 0;

    draw_line();
    printf("Preis/Stueck (in EUR): \n");
    scanf("%f",&fPreis);
    fflush(stdin);
    printf("Stueck: \n");
    scanf("%d",&iStueck);
    fflush(stdin);
    printf("Rabatt (in %%): \n");
    scanf("%f",&fRabatt);
    fflush(stdin);
    printf("Skonto (in %%): \n");
    scanf("%f",&fSkonto);
    fflush(stdin);
    printf("Versandkosten (in EUR): \n");
    scanf("%f",&fVersandkosten);
    fflush(stdin);
    draw_line();


    fListenpreis = fPreis*iStueck;
    fRabattAbzug = fListenpreis*fRabatt*0.01;
    fZieleinkaufspreis = fListenpreis-fRabattAbzug;
    fSkontoAbzug = fZieleinkaufspreis*fSkonto*0.01;
    fBareinkaufspreis = fZieleinkaufspreis-fSkontoAbzug;
    fBezugspreis = fBareinkaufspreis+fVersandkosten;


    printf("Listenpreis: %2.2f EUR \n",fListenpreis);
    printf("- Rabatt %2.0f %% %2.2f EUR \n",fRabatt,fRabattAbzug);
    printf("= Zieleinkaufspreis: %2.2f EUR \n",fZieleinkaufspreis);
    printf("- Skonto (%2.0f %%) %2.2f EUR \n",fSkonto,fSkontoAbzug);
    printf("= Bareinkaufspreis %2.2f EUR \n",fBareinkaufspreis);
    printf("+ Versnadkosten %2.2f EUR \n",fVersandkosten);
    draw_line();
    printf("= Bezugspreis (Einstandspreis) %2.2f EUR): \n",fBezugspreis);
    return 0;
}

