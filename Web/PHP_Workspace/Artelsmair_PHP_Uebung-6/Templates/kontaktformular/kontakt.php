<?php
session_start();
error_reporting(E_ERROR | E_PARSE);
date_default_timezone_set('Europe/Berlin');
require_once("AntiSpam.php");
$q = AntiSpam::getRandomQuestion();
header('Content-type: text/html; charset=utf-8');


#########################################################################
#	Kontaktformular.com         					                                #
#	http://www.kontaktformular.com        						                    #
#	All rights by KnotheMedia.de                                    			#
#-----------------------------------------------------------------------#
#	I-Net: http://www.knothemedia.de                            					#
#########################################################################
// Der Copyrighthinweis darf NICHT entfernt werden!


  $script_root = substr(__FILE__, 0,
                        strrpos(__FILE__,
                                DIRECTORY_SEPARATOR)
                       ).DIRECTORY_SEPARATOR;

require_once $script_root.'upload.php';

$remote = getenv("REMOTE_ADDR");

function encrypt($string, $key) {
	$result = '';
	for($i=0; $i<strlen($string); $i++) {
	   $char = substr($string, $i, 1);
	   $keychar = substr($key, ($i % strlen($key))-1, 1);
	   $char = chr(ord($char)+ord($keychar));
	   $result.=$char;
	}
	return base64_encode($result);
}

@require('config.php');
require_once("AntiSpam.php");
include("PHPMailer/Secureimage.php");
// form-data should be deleted
if ($_POST['delete']){
	unset($_POST);
}

// form has been sent
if ($_POST["kf-km"]) {

	// clean data
	$name		= stripslashes($_POST["name"]);
	$email		= stripslashes($_POST["email"]);
	$betreff		= "E-Mail über Kontaktformular";
	$nachricht  = stripslashes($_POST["nachricht"]);
	if($cfg['DATENSCHUTZ_ERKLAERUNG']) { $datenschutz = stripslashes($_POST["datenschutz"]); }
	if($cfg['Sicherheitscode']){
		$sicherheits_eingabe = encrypt($_POST["sicherheitscode"], "8h384ls94");
		$sicherheits_eingabe = str_replace("=", "", $sicherheits_eingabe);
	}

	$date = date("d.m.Y | H:i");
	$ip = $_SERVER['REMOTE_ADDR'];
	$UserAgent = $_SERVER["HTTP_USER_AGENT"];
	$host = getHostByAddr($remote);


	// formcheck	
	if(!$name) {
		$fehler['name'] = "<span class='errormsg' style='color:red;font-size:.75rem;'>Geben Sie bitte Ihren <strong>Namen</strong> ein.</span>";
	}
	
	if (!preg_match("/^[0-9a-zA-ZÄÜÖ_.-]+@[0-9a-z.-]+\.[a-z]{2,6}$/", $email)) {
		$fehler['email'] = "<span class='errormsg' style='color:red;font-size:.75rem;'>Geben Sie bitte Ihre <strong>E-Mail-Adresse</strong> ein.</span>";
	}
	
	
	
	if(!$nachricht) {
		$fehler['nachricht'] = "<span class='errormsg' style='color:red;font-size:.75rem;'>Geben Sie bitte eine <strong>Nachricht</strong> ein.</span>";
	}
	
	
	
	// -------------------- SPAMPROTECTION ERROR MESSAGES START ----------------------
	if($cfg['Sicherheitscode'] && $sicherheits_eingabe != $_SESSION['captcha_spam']){
		unset($_SESSION['captcha_spam']);
		$fehler['captcha'] = "<span class='errormsg' style='color:red;font-size:.75rem;'>Der <strong>Sicherheitscode</strong> wurde falsch eingegeben.</span>";
	} 
		

  if($cfg["Sicherheitsfrage"]){
	$answer = AntiSpam::getAnswerById(intval($_POST["q_id"]));
	if($_POST["q"] != $answer){
		$fehler['q_id12'] = "<span class='errormsg' style='color:red;font-size:.75rem;'>Bitte die <strong>Sicherheitsfrage</strong> richtig beantworten.</span>";
	}
  }



	if($cfg['Honeypot'] && (!isset($_POST["mail"]) || ''!=$_POST["mail"])){
		$fehler['Honeypot'] = "<span class='errormsg' style='display: block; color:red;font-size:.75rem;'>Es besteht Spamverdacht. Bitte überprüfen Sie Ihre Angaben.</span>";
	}
	
	if($cfg['Zeitsperre'] && (!isset($_POST["chkspmtm"]) || ''==$_POST["chkspmtm"] || '0'==$_POST["chkspmtm"] || (time() - (int) $_POST["chkspmtm"]) < (int) $cfg['Zeitsperre'])){
		$fehler['Zeitsperre'] = "<span class='errormsg' style='display: block; color:red;font-size:.75rem;'>Bitte warten Sie einige Sekunden, bevor Sie das Formular erneut absenden.</span>";
	}
	
	if($cfg['Klick-Check'] && (!isset($_POST["chkspmkc"]) || 'chkspmhm'!=$_POST["chkspmkc"])){
		$fehler['Klick-Check'] = "<span class='errormsg' style='display: block; color:red;font-size:.75rem;'>Sie müssen den Senden-Button mit der Maus anklicken, um das Formular senden zu können.</span>";
	}
	
	if($cfg['Links'] < preg_match_all('#http(s?)\:\/\/#is', $nachricht, $irrelevantMatches)){
		$fehler['Links'] = "<span class='errormsg' style='display: block; color:red;font-size:.75rem;'>Ihre Nachricht darf ".(0==$cfg['Links'] ? 
																																'keine Links' : 
																																(1==$cfg['Links'] ? 
																																	'nur einen Link' : 
																																	'maximal '.$cfg['Links'].' Links'
																																)
																															)." enthalten.</span>";
	}
	
	if(''!=$cfg['Badwordfilter'] && 0!==$cfg['Badwordfilter'] && '0'!=$cfg['Badwordfilter']){
		$badwords = explode(',', $cfg['Badwordfilter']);			// the configured badwords
		$badwordFields = explode(',', $cfg['Badwordfields']);		// the configured fields to check for badwords
		$badwordMatches = array();									// the badwords that have been found in the fields
		
		if(0<count($badwordFields)){
			foreach($badwords as $badword){
				$badword = trim($badword);												// remove whitespaces from badword
				$badwordMatch = str_replace('%', '', $badword);							// take human readable badword for error-message
				$badword = addcslashes($badword, '.:/');								// make ., : and / preg_match-valid
				if('%'!=substr($badword, 0, 1)){ $badword = '\\b'.$badword; }			// if word mustn't have chars before > add word boundary at the beginning of the word
				if('%'!=substr($badword, -1, 1)){ $badword = $badword.'\\b'; }			// if word mustn't have chars after > add word boundary at the end of the word
				$badword = str_replace('%', '', $badword);								// if word is allowed in the middle > remove all % so it is also allowed in the middle in preg_match 
				foreach($badwordFields as $badwordField){
					if(preg_match('#'.$badword.'#is', $_POST[trim($badwordField)]) && !in_array($badwordMatch, $badwordMatches)){
						$badwordMatches[] = $badwordMatch;
					}
				}
			}		
			
			if(0<count($badwordMatches)){
				$fehler['Badwordfilter'] = "<span class='errormsg' style='display: block; color:red;font-size:.75rem;'>Folgende Begriffe sind nicht erlaubt: ".implode(', ', $badwordMatches)."</span>";
			}
		}		
	}
  // -------------------- SPAMPROTECTION ERROR MESSAGES ENDE ----------------------
  
  
	if($cfg['DATENSCHUTZ_ERKLAERUNG'] && isset($datenschutz) && $datenschutz == ""){ 
		$fehler['datenschutz'] = "<span class='errormsg' style='color:red;font-size:.75rem;'>Sie müssen die <strong>Datenschutzerklärung</strong> akzeptieren.</span><br />";
	}

	// there are NO errors > upload-check
    if (!isset($fehler) || count($fehler) == 0) {
      $error             = false;
      $errorMessage      = '';
      $uploadErrors      = array();
      $uploadedFiles     = array();
      $totalUploadSize   = 0;

      if ($cfg['UPLOAD_ACTIVE'] && in_array($_SERVER['REMOTE_ADDR'], $cfg['BLACKLIST_IP']) === true) {
          $error = true;
          $fehler['upload'] = "<span class='errormsg'>Sie haben keine Erlaubnis Dateien hochzuladen.</span>";
      }

      if (!$error) {
          for ($i=0; $i < $cfg['NUM_ATTACHMENT_FIELDS']; $i++) {
              if ($_FILES['f']['error'][$i] == UPLOAD_ERR_NO_FILE) {
                  continue;
              }

              $extension = explode('.', $_FILES['f']['name'][$i]);
              $extension = strtolower($extension[count($extension)-1]);
              $totalUploadSize += $_FILES['f']['size'][$i];

              if ($_FILES['f']['error'][$i] != UPLOAD_ERR_OK) {
                  $uploadErrors[$j]['name'] = $_FILES['f']['name'][$i];
                  switch ($_FILES['f']['error'][$i]) {
                      case UPLOAD_ERR_INI_SIZE :
                          $uploadErrors[$j]['error'] = 'Die Datei ist zu groß (PHP-Ini Direktive).';
                      break;
                      case UPLOAD_ERR_FORM_SIZE :
                          $uploadErrors[$j]['error'] = 'Die Datei ist zu groß (MAX_FILE_SIZE in HTML-Formular).';
                      break;
                      case UPLOAD_ERR_PARTIAL :
						  if ($cfg['UPLOAD_ACTIVE']) {
                          	  $uploadErrors[$j]['error'] = 'Die Datei wurde nur teilweise hochgeladen.';
						  } else {
							  $uploadErrors[$j]['error'] = 'Die Datei wurde nur teilweise versendet.';
					  	  }
                      break;
                      case UPLOAD_ERR_NO_TMP_DIR :
                          $uploadErrors[$j]['error'] = 'Es wurde kein temporärer Ordner gefunden.';
                      break;
                      case UPLOAD_ERR_CANT_WRITE :
                          $uploadErrors[$j]['error'] = 'Fehler beim Speichern der Datei.';
                      break;
                      case UPLOAD_ERR_EXTENSION  :
                          $uploadErrors[$j]['error'] = 'Unbekannter Fehler durch eine Erweiterung.';
                      break;
                      default :
						  if ($cfg['UPLOAD_ACTIVE']) {
                          	  $uploadErrors[$j]['error'] = 'Unbekannter Fehler beim Hochladen.';
						  } else {
							  $uploadErrors[$j]['error'] = 'Unbekannter Fehler beim Versenden des Email-Attachments.';
						  }
                  }

                  $j++;
                  $error = true;
              }
              else if ($totalUploadSize > $cfg['MAX_ATTACHMENT_SIZE']*1024) {
                  $uploadErrors[$j]['name'] = $_FILES['f']['name'][$i];
                  $uploadErrors[$j]['error'] = 'Maximaler Upload erreicht ('.$cfg['MAX_ATTACHMENT_SIZE'].' KB).';
                  $j++;
                  $error = true;
              }
              else if ($_FILES['f']['size'][$i] > $cfg['MAX_FILE_SIZE']*1024) {
                  $uploadErrors[$j]['name'] = $_FILES['f']['name'][$i];
                  $uploadErrors[$j]['error'] = 'Die Datei ist zu groß (max. '.$cfg['MAX_FILE_SIZE'].' KB).';
                  $j++;
                  $error = true;
              }
              else if (!empty($cfg['BLACKLIST_EXT']) && strpos($cfg['BLACKLIST_EXT'], $extension) !== false) {
                  $uploadErrors[$j]['name'] = $_FILES['f']['name'][$i];
                  $uploadErrors[$j]['error'] = 'Die Dateiendung ist nicht erlaubt.';
                  $j++;
                  $error = true;
              }
              else if (preg_match("=^[\\:*?<>|/]+$=", $_FILES['f']['name'][$i])) {
                  $uploadErrors[$j]['name'] = $_FILES['f']['name'][$i];
                  $uploadErrors[$j]['error'] = 'Ungültige Zeichen im Dateinamen (\/:*?<>|).';
                  $j++;
                  $error = true;
              }
              else if ($cfg['UPLOAD_ACTIVE'] && file_exists($cfg['UPLOAD_FOLDER'].'/'.$_FILES['f']['name'][$i])) {
                  $uploadErrors[$j]['name'] = $_FILES['f']['name'][$i];
                  $uploadErrors[$j]['error'] = 'Die Datei existiert bereits.';
                  $j++;
                  $error = true;
              }
              else {
				  if ($cfg['UPLOAD_ACTIVE']) {
                     move_uploaded_file($_FILES['f']['tmp_name'][$i], $cfg['UPLOAD_FOLDER'].'/'.$_FILES['f']['name'][$i]);
				  }
                  $uploadedFiles[$_FILES['f']['tmp_name'][$i]] = $_FILES['f']['name'][$i];
              }
          }
      }

      if ($error) {
          $errorMessage = 'Es sind folgende Fehler beim Versenden des Kontaktformulars aufgetreten:'."\n";
          if (count($uploadErrors) > 0) {
              foreach ($uploadErrors as $err) {
                  $tmp .= '<strong>'.$err['name']."</strong><br/>\n- ".$err['error']."<br/><br/>\n";
              }
              $tmp = "<br/><br/>\n".$tmp;
          }
          $errorMessage .= $tmp.'';
          $fehler['upload'] = $errorMessage;
      }
	}


	// there are NO errors > send mail
   if (!isset($fehler))
   {
		// ------------------------------------------------------------
		// -------------------- send mail to admin --------------------
		// ------------------------------------------------------------

		// ---- create mail-message for admin
	 $mailcontent  = "Folgendes wurde am ". $date ." Uhr per Formular geschickt:\n" . "-------------------------------------------------------------------------\n\n";
   $mailcontent .= "Name: " . $name . "\n";
   $mailcontent .= "E-Mail: " . $email . "\n\n";
   
   $mailcontent .= "Nachricht:\n" . $_POST['nachricht'] = preg_replace("/\r\r|\r\n|\n\r|\n\n/","\n",$_POST['nachricht']) . "\n\n";
		if(count($uploadedFiles) > 0){
			if($cfg['UPLOAD_ACTIVE']){
				$mailcontent .= 'Es wurden folgende Dateien hochgeladen:'."\n";
				foreach ($uploadedFiles as $filename) {
					$mailcontent .= ' - '.$cfg['DOWNLOAD_URL'].'/'.$cfg['UPLOAD_FOLDER'].'/'.$filename."\n";
				}
			} else {
				$mailcontent .= 'Es wurden folgende Dateien als Attachment angehängt:'."\n";
				foreach ($uploadedFiles as $filename) {
					$mailcontent .= ' - '.$filename."\n";
				}
			}
		}
		if($cfg['DATENSCHUTZ_ERKLAERUNG']) { $mailcontent .= "\n\nDatenschutz: " . $datenschutz . " \n"; }
    $mailcontent .= "\n\nIP Adresse: " . $ip . "\n";
		$mailcontent = strip_tags ($mailcontent);

		// ---- get attachments for admin
		$attachments = array();
		if(!$cfg['UPLOAD_ACTIVE'] && count($uploadedFiles) > 0){
			foreach($uploadedFiles as $tempFilename => $filename) {
				$attachments[$filename] = file_get_contents($tempFilename);
			}
		}

		$success = false;

        // ---- send mail to admin
        if($smtp['enabled'] !== 0) {
            require_once __DIR__ . '/smtp.php';
            $success = SMTP::send(
                $smtp['host'],
                $smtp['user'],
                $smtp['password'],
                $smtp['encryption'],
                $smtp['port'],
                $email,
                $ihrname,
                $empfaenger,
                $betreff,
                $mailcontent,
                $uploadedFiles,
                $cfg['UPLOAD_FOLDER'],
                $smtp['debug']
            );
        } else {
            $success = sendMyMail($email, $name, $empfaenger, $betreff, $mailcontent, $attachments);
        }

    	// ------------------------------------------------------------
    	// ------------------- send mail to customer ------------------
    	// ------------------------------------------------------------
    	if(
			$success && 
			(
				2==$cfg['Kopie_senden'] || 																// send copy always
				(1==$cfg['Kopie_senden'] && isset($_POST['mail-copy']) && 1==$_POST['mail-copy'])		// send copy only if customer want to
			)
		){

    		// ---- create mail-message for customer
			$mailcontent  = "Vielen Dank für Ihre E-Mail. Wir werden schnellstmöglich darauf antworten.\n\n";
			$mailcontent .= "Zusammenfassung: \n" .

  "-------------------------------------------------------------------------\n\n";

   $mailcontent .= "Name: " . $name . "\n";
   $mailcontent .= "E-Mail: " . $email . "\n\n";
   
   $mailcontent .= "Nachricht:\n" . str_replace("\r", "", $nachricht) . "\n\n";
    		if(count($uploadedFiles) > 0){
    			$mailcontent .= 'Sie haben folgende Dateien übertragen:'."\n";
    			foreach($uploadedFiles as $file){
    				$mailcontent .= ' - '.$file."\n";
    			}
    		}
    		$mailcontent = strip_tags ($mailcontent);

    		// ---- send mail to customer
            if($smtp['enabled'] !== 0) {
                SMTP::send(
                    $smtp['host'],
                    $smtp['user'],
                    $smtp['password'],
                    $smtp['encryption'],
                    $smtp['port'],
                    $empfaenger,
                    $ihrname,
                    $email,
                    "Ihre Anfrage",
                    $mailcontent,
                    array(),
                    $cfg['UPLOAD_FOLDER'],
                    $smtp['debug']
                );
            } else {
                $success = sendMyMail($empfaenger, $ihrname, $email, "Ihre Anfrage", $mailcontent);
            }
		}
		
		// redirect to success-page
		if($success){
			if($smtp['enabled'] === 0 || $smtp['debug'] === 0) {
    		    echo "<META HTTP-EQUIV=\"refresh\" content=\"0;URL=".$danke."\">";
            }

    		exit;
		}
	}
}

// clean post
foreach($_POST as $key => $value){
    $_POST[$key] = htmlentities($value, ENT_QUOTES, "UTF-8");
}
?>
<?php




function sendMyMail($fromMail, $fromName, $toMail, $subject, $content, $attachments=array()){

	$boundary = md5(uniqid(time()));
	$eol = PHP_EOL;

	// header
	$header = "From: =?UTF-8?B?".base64_encode(stripslashes($fromName))."?= <".$fromMail.">".$eol;
	$header .= "Reply-To: <".$fromMail.">".$eol;
	$header .= "MIME-Version: 1.0".$eol;
	if(is_array($attachments) && 0<count($attachments)){
		$header .= "Content-Type: multipart/mixed; boundary=\"".$boundary."\"";
	}
	else{
		$header .= "Content-type: text/plain; charset=utf-8";
	}


	// content with attachments
	if(is_array($attachments) && 0<count($attachments)){

		// content
		$message = "--".$boundary.$eol;
		$message .= "Content-type: text/plain; charset=utf-8".$eol;
		$message .= "Content-Transfer-Encoding: 8bit".$eol.$eol;
		$message .= $content.$eol;

		// attachments
		foreach($attachments as $filename=>$filecontent){
			$filecontent = chunk_split(base64_encode($filecontent));
			$message .= "--".$boundary.$eol;
			$message .= "Content-Type: application/octet-stream; name=\"".$filename."\"".$eol;
			$message .= "Content-Transfer-Encoding: base64".$eol;
			$message .= "Content-Disposition: attachment; filename=\"".$filename."\"".$eol.$eol;
			$message .= $filecontent.$eol;
		}
		$message .= "--".$boundary."--";
	}
	// content without attachments
	else{
		$message = $content;
	}

	// subject
	$subject = "=?UTF-8?B?".base64_encode($subject)."?=";

	// send mail
	return mail($toMail, $subject, $message, $header);
}

?>
<!DOCTYPE html>
<html lang="de-DE">
	<head>
		<meta charset="utf-8">
		<meta name="language" content="de"/>
		<meta name="description" content="kontaktformular.com"/>
		<meta name="revisit" content="After 7 days"/>
		<meta name="robots" content="INDEX,FOLLOW"/>
		<title>kontaktformular.com</title>

		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<!-- Stylesheet -->
<link href="style-kontaktformular.css" rel="stylesheet">

<link href='https://fonts.googleapis.com/css?family=Heebo:700' rel='stylesheet' type='text/css'>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script></head>





<body>

<div>
<form class="kontaktformular" action="<?php echo $_SERVER['PHP_SELF'];?>" method="post" enctype="multipart/form-data">

	<?php 
		if(
			$fehler["Honeypot"] != "" || 
			$fehler["Zeitsperre"] != "" ||
			$fehler['Klick-Check'] != "" ||
			$fehler['Links'] != "" ||
			$fehler['Badwordfilter'] != ""
		){
			?>
			<div class="row">
				<label class="col-sm-3 control-label"></label>
				<div class="col-sm-8">
					<?php if ($fehler["Honeypot"] != "") { echo $fehler["Honeypot"]; } ?>
					<?php if ($fehler["Zeitsperre"] != "") { echo $fehler["Zeitsperre"]; } ?>
					<?php if ($fehler["Klick-Check"] != "") { echo $fehler["Klick-Check"]; } ?>
					<?php if ($fehler["Links"] != "") { echo $fehler["Links"]; } ?>
					<?php if ($fehler["Badwordfilter"] != "") { echo $fehler["Badwordfilter"]; } ?>
				</div>
			</div>
			<?php
		}
	
	
	?>


  <div class="row">
    <label class="col-sm-3 control-label">Name: *</label>
    <div class="col-sm-2"><?php if ($fehler["name"] != "") { echo $fehler["name"]; } ?>
      <input type="text" name="name" class="field"  placeholder="Name" value="<?php echo $_POST[name]; ?>" maxlength="<?php echo $zeichenlaenge_name; ?>" <?php if($cfg['HTML5_FEHLERMELDUNGEN']) { ?> required <?php } ?> />
    </div>



     <label class="col-sm-4 control-label">E-Mail: *</label>
    <div class="col-sm-2"><?php if ($fehler["email"] != "") { echo $fehler["email"]; } ?>
      <input type="text" name="email" class="field" placeholder="E-Mail" value="<?php echo $_POST[email]; ?>" maxlength="<?php echo $zeichenlaenge_email; ?>" <?php if($cfg['HTML5_FEHLERMELDUNGEN']) { ?> required <?php } ?> />
    </div>
  </div>



 




    <div class="row">
    <label class="col-sm-3 control-label" style="align-self: center;">Nachricht: *</label>
    <div class="col-sm-8"><?php if ($fehler["nachricht"] != "") { echo $fehler["nachricht"]; } ?>
       <textarea name="nachricht" class="field" rows="5" placeholder="Nachricht" style="height:100%;width:100%;" <?php if($cfg['HTML5_FEHLERMELDUNGEN']) { ?> required <?php } ?>><?php echo $_POST[nachricht]; ?></textarea>
    </div>
  </div>




<?php
// -------------------- DATEIUPLOAD START ----------------------
	if(0<$cfg['NUM_ATTACHMENT_FIELDS']){
				echo ' <div class="row upload-row">';
				echo '<label class="col-sm-3 control-label">Dateiupload:</label>';
				  for ($i=0; $i < $cfg['NUM_ATTACHMENT_FIELDS']; $i++) {
				  echo '<div class="col-sm-8"><input style="height:30px;width:109%;" type="file" size=12 name="f[]"/></div>';
				  }
			   echo '</div>';
	}
// -------------------- DATEIUPLOAD ENDE ----------------------
?>






<?php
// -------------------- SPAMPROTECTION START ----------------------

if($cfg['Honeypot']){ ?>
	<div style="height: 2px; overflow: hidden;">
		<label style="margin-top: 10px;">Das nachfolgende Feld muss leer bleiben, damit die Nachricht gesendet wird!</label>
		<div style="margin-top: 10px;"><input type="email" name="mail" value="" /></div>
	</div>
<?php }

if($cfg['Zeitsperre']){ ?>
	<input type="hidden" name="chkspmtm" value="<?php echo time(); ?>" />
<?php }

if($cfg['Klick-Check']){ ?>
	<input type="hidden" name="chkspmkc" value="chkspmbt" />
<?php }


if($cfg['Sicherheitscode']) { ?>
  <div class="row">
    <label class="col-sm-3 control-label">Spamschutz:</label>
    <div class="col-sm-8">
   
<img src="captcha/captcha.php" alt="Sicherheitscode" title="kontaktformular.com-sicherheitscode" id="captcha" />
						<a href="javascript:void(0);" onclick="javascript:document.getElementById('captcha').src='captcha/captcha.php?'+Math.random();cursor:pointer;">
							<span class="captchareload"><img src="icon-kf.gif" alt="Sicherheitscode neu laden" title="Bild neu laden" /></span></a>
   
   
    </div>
  </div>
  
<div class="row" id="answer">
    <label class="col-sm-3 control-label">Eingabe: *</label>
    <div class="col-sm-8"><input placeholder="Bitte Sicherheitscode eingeben." type="text" name="sicherheitscode" maxlength="150"  class="field<?php if ($fehler["captcha"] != "") { echo ' errordesignfields'; } ?>"/><?php if ($fehler["captcha"] != "") { echo $fehler["captcha"]; } ?>
   
    </div>
  </div>
  

<?php }

if($cfg['Sicherheitsfrage']) { ?>
  

    <div class="row">
    <label class="col-sm-3 control-label">Spamschutz:</label>
    <div class="col-sm-8">
   <div style="width:90%;height:1.7em;"><?php echo $q[1]; ?></div> <input type="hidden" name="q_id" value="<?php echo $q[0]; ?>"/>
    </div>
  </div>

  <div class="row" id="answer">
    <label class="col-sm-3 control-label">Antwort: *</label>
    <div class="col-sm-8">
   <input placeholder="Bitte Aufgabe lösen." type="text" class="field<?php if ($fehler["q_id12"] != "") { echo ' errordesignfields'; } ?>" name="q"/><?php if ($fehler["q_id12"] != "") { echo $fehler["q_id12"]; } ?>
    </div>
  </div>
  
  

<?php } 

// -------------------- SPAMPROTECTION ENDE ----------------------
		{ ?>






	<?php }
		
		// -------------------- MAIL-COPY START ----------------------

		if(1==$cfg['Kopie_senden']) { ?>			
<div style="margin-top:10px;">
  <div class="row" id="mail-copy">
   <label for="inlineCheckbox11" class="col-sm-3 control-label">Kopie senden:</label>
   <label class="checkbox-inline">
    
  <input type="checkbox" id="inlineCheckbox11" name="mail-copy" value="1" <?php if (isset($_POST['mail-copy']) && $_POST['mail-copy']=='1') echo(' checked="checked" '); ?>> <span>Kopie der Nachricht per E-Mail senden</span>
</label>

</div></div>



	
<?php } 

		// -------------------- MAIL-COPY ENDE ----------------------
		
		
		// -------------------- DATAPROTECTION START ----------------------

if($cfg['DATENSCHUTZ_ERKLAERUNG']) { ?>
<div style="margin-top:10px;">
  <div class="row" id="privacy-security">
   <label for="inlineCheckbox11" class="col-sm-3 control-label">Datenschutz: *</label>
   <label class="checkbox-inline">
    <?php if ($fehler["datenschutz"] != "") { echo $fehler["datenschutz"]; } ?>
  <input type="checkbox" id="inlineCheckbox11" name="datenschutz" value="akzeptiert" <?php if ($_POST['datenschutz']=='akzeptiert') echo(' checked="checked" '); ?> <?php if($cfg['HTML5_FEHLERMELDUNGEN']) { ?> required <?php } ?>> <a href="<?php echo "$datenschutzerklaerung"; ?>" target="_blank">Bitte lesen und akzeptieren Sie die Datenschutzerkl&auml;rung.</a>
</label>

</div></div>
<?php } 

// -------------------- DATAPROTECTION ENDE ----------------------
 ?>
 
 


  <div class="row" id="send">
  <div class="col-sm-4 col-sm-offset-3">
      <br /><b>Hinweis:</b> Felder mit <span class="pflichtfeld">*</span> müssen ausgefüllt werden.
      <br />
      <br />
  <input type="submit" class="senden" name="kf-km" value="Anfrage senden" />
  
  <div>
  <!-- Dieser Copyrighthinweis darf NICHT entfernt werden. --><br /><br /><br /><a href="https://www.kontaktformular.com" title="kontaktformular.com" style="text-decoration: none;color:#000000;font-size:13px;" target="_blank">&copy; by kontaktformular.com - Alle Rechte vorbehalten.</a>
  </div>
  
  
  </div>
  </div>
  
  
  
<?php if($cfg['Klick-Check']){ ?>
	<script type="text/javascript">
		function chkspmkcfnk(){
			document.getElementsByName('chkspmkc')[0].value = 'chkspmhm';
		}
		document.getElementsByName('kf-km')[0].addEventListener('mouseenter', chkspmkcfnk);
		document.getElementsByName('kf-km')[0].addEventListener('touchstart', chkspmkcfnk);
	</script>
<?php } ?>

</form>
</div>
</body>
</html>
