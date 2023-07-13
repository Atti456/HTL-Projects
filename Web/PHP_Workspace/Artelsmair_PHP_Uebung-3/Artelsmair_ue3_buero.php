<link href="CSS.css" type="text/css" rel="stylesheet"/>

<?php
//Artelsmair 11.03.2019


const mwst = 0.20;
const eur = ",-- €";


$bez_tafel = "Tafel";
$bez_stuhl = "Bürostuhl";
$bez_uhr = "Uhr";
$bez_pctisch = "Computerrisch";

$preis_tafel = 5879;
$preis_stuhl = 289;
$preis_uhr = 29;
$preis_pctisch = 999;

$anzahl_tafel= 1;
$anzahl_stuhl= 31;
$anzahl_uhr= 1;   
$anzahl_pctisch = 30;


$ipreis_tafel_brutto=  $preis_tafel*mwst+$preis_tafel;
$ipreis_stuhl_brutto=  $preis_stuhl*mwst+$preis_stuhl;
$ipreis_uhr_brutto= $preis_uhr*mwst+$preis_uhr;
$ipreis_pctisch_brutto= $preis_pctisch*mwst+$preis_pctisch;
$iges_preis_tafel=$ipreis_tafel_brutto* $anzahl_tafel;
$iges_preis_stuhl=$ipreis_stuhl_brutto*$anzahl_stuhl;
$iges_preis_uhr=  $ipreis_uhr_brutto;   
$iges_preis_pctisch = $ipreis_pctisch_brutto*$anzahl_pctisch;

$preis_tafel_brutto=  intval($ipreis_tafel_brutto);
$preis_stuhl_brutto=  intval($ipreis_stuhl_brutto);
$preis_uhr_brutto= intval($ipreis_uhr_brutto);
$preis_pctisch_brutto= intval($ipreis_pctisch_brutto);
$ges_preis_tafel=intval($iges_preis_tafel);
$ges_preis_stuhl=intval($iges_preis_stuhl);
$ges_preis_uhr=  intval($iges_preis_uhr); 
$ges_preis_pctisch = intval($iges_preis_pctisch);


$netto_gesamt = ($preis_tafel + $preis_stuhl + $preis_uhr + $preis_pctisch);
$brutto_gesamt=  $preis_tafel_brutto+ $preis_stuhl_brutto+$preis_uhr_brutto+$preis_pctisch_brutto;


echo "<table>
        
        <tr><th>Variable</th><th>Bezeichnung</th><th>Variable</th><th>Preis(netto)</th><th>Preis(brutto)</th><th>Anzahl</th><th>Gesamt</th></tr>
        <tr><td>\$bez_tafel</td><td>$bez_tafel</td><td> \$preis_tafel</td><td>$preis_tafel".eur;echo "</td><td>$preis_tafel_brutto".eur;echo "</td><td>$anzahl_tafel</td><td>$ges_preis_tafel".eur;echo "</td></tr>
        <tr><td>\$bez_stuhl</td><td>$bez_stuhl</td><td> \$preis_stuhl</td><td>$preis_stuhl".eur;echo "</td><td> $preis_stuhl_brutto".eur;echo "</td><td>$anzahl_stuhl</td><td>$ges_preis_stuhl".eur;echo "</td></tr>
        <tr><td>\$bez_uhr</td><td>$bez_uhr</td><td>\$preis_uhr</td><td>$preis_uhr".eur;echo "</td><td>$preis_uhr_brutto".eur;echo "</td><td>$anzahl_uhr</td><td>$ges_preis_uhr".eur;echo "</td></tr>
        <tr><td> \$bez_tisch</td><td>$bez_pctisch</td><td>\$preis_pctisch</td><td>$preis_pctisch".eur;echo "</td><td>$preis_pctisch_brutto".eur;echo "</td><td>$anzahl_pctisch</td><td>$ges_preis_pctisch".eur;echo "</td></tr>
        <tr><td></td><td>Bezeichnung</td><td>Gesamt(Klasse netto)  </td><td>$netto_gesamt".eur;echo "</td><td>Preis(brutto)</td><td>Gesamt(Klasse brutto)  </td><td>$brutto_gesamt".eur;echo "</td></tr>
        
        </table> ";





?>
    
