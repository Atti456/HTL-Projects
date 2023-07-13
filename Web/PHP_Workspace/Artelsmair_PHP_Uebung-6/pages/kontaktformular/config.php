<?php

$empfaenger = "alexander.artelsmair@gmail.com";  // Bitte tragen Sie hier Ihre E-Mail Adresse ein. (zwischen den Anführungszeichen)

$ihrname = "Alexander Artelsmair";  // Bitte tragen Sie hier Ihren Namen ein. (zwischen den Anführungszeichen) Dieser erscheint als Absender in der Danke Mail.

$cfg['DATENSCHUTZ_ERKLAERUNG'] = 0;  //  0 = Ohne Datenschutzerklärung    1 = Mit Datenschutzerklärung

$datenschutzerklaerung = "datenschutz.php";  //  Pfad zur Datenschutzerklärung. "datenschutz.php" kann durch einen Link/URL ersetzt werden. (muss mit "http://www." anfangen!)

    

// Spamschutz - Einstellungen //

$cfg['Sicherheitscode'] = 0;  //  0 = Ohne Sicherheitscode   1 = Mit Sicherheitscode

$cfg['Sicherheitsfrage'] = 0;  //  0 = Ohne Sicherheitsfrage   1 = Mit Sicherheitsfrage

$cfg['Honeypot'] = 0;  //  0 = Ohne Honeypot   1 = Mit Honeypot 
	
$cfg['Zeitsperre'] = 0;  //  Mindest-Anzahl der Sekunden zwischen Anzeigen und Senden des Formulars  	0 = Ohne Zeitsperre
	
$cfg['Klick-Check'] = 0;  //  0 = Ohne Klick-Check   1 = Mit Klick-Check

$cfg['Links'] = 100;  //  Anzahl der maximal erlaubten Links (0 = keine Links erlaubt)
	
$cfg['Badwordfilter'] = 'sex%, pussy%, porn%, %.ru, %.ru/%';  //  Begriffe für den Badword-Filter   0 oder leer = Ohne Badwordfilter

// Funktionsweise des Badwordfilters:
// badword = matcht wenn das Badword als ganzes Wort enthalten ist   
// badword% = matcht wenn das Badword enthalten ist UND wenn ein Wort mit dem Badword beginnt   
// %badword = matcht wenn das Badword enthalten ist UND wenn ein Wort mit dem Badword endet   
// &badword% = matcht wenn das Badword enthalten ist UND wenn ein Wort das Badword enthält
	
$cfg['Badwordfields'] = 'name, email, nachricht';  //   Die Namen der Felder, die bei dem Badword-Check geprüft werden sollen - Groß- und Kleinschreibung beachten!
	



// Weitere Einstellungen //

$cfg['Kopie_senden'] = 2;    // 0 = keine Kopie senden   1 = Kopie nur bei Zustimmung senden   2 = immer eine Kopie senden (ungefragt)

$cfg['HTML5_FEHLERMELDUNGEN'] = 0;  //  0 = Ohne HTML5 Fehlermeldungen    1 = Mit HTML5 Fehlermeldungen




// Die SMTP Funktion kann im nachfolgenden Abschnitt aktiviert werden. Wichtig: Auf Ihrem Webserver muss mind. PHP 5.5 oder höher installiert sein! Die aktuelle PHP Version können Sie prüfen, indem Sie die Datei phpinfo.php im Browser aufrufen. //

$smtp = array();

$smtp['enabled'] = 0; // Soll das Kontaktformular E-Mails über einen SMTP Server versenden? Ja = 1, Nein = 0

$smtp['host'] = 'smtp.example.de'; // Der Host, unter welchem der SMTP Server erreichbar ist. (bspw. smtp.gmail.com)
   
$smtp['user'] = ''; // Der Benutzername, mit welchem Sie sich bei Ihrem SMTP Server authentifizieren. (kann u.U. die oben genannte E-Mail Adresse sein!)

$smtp['password'] = ''; // Das Passwort, mit welchem Sie sich bei Ihrem SMTP Server authentifizieren.

$smtp['encrpytion'] = 'tls'; // Die Art der Verschlüsselung, die bei der Verbindung mit Ihrem SMTP Server verwendet wird: '', 'ssl' oder 'tls'

$smtp['port'] = 587; // Der TCP Port, unter welchem Ihr SMTP Server erreichbar ist.

$smtp['debug'] = 0; // Das Debuglevel (0 - 4)
    
    
    
    
    
// Maximale Zeichenlänge der Felder definieren //

$zeichenlaenge_name = "50";  // Maximale Zeichen - Feld "Name" (zwischen den Anführungszeichen)

$zeichenlaenge_email = "50"; // Maximale Zeichen - Feld "E-Mail" (zwischen den Anführungszeichen)

?> 