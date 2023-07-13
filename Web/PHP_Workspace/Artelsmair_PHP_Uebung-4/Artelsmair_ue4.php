<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
  <link rel="stylesheet" type="text/css" href="CSS2.css" media="screen" />
</head>
<body>
<table id="table" >
    <tr>
        <th></th>
        <th>Datum<a href="Artelsmair_ue4.php?sort=dau">↓</a> <a href="Artelsmair_ue4.php?sort=dab">↑</a></th>
        <th>Band <a href="Artelsmair_ue4.php?sort=bau">↓</a> <a href="Artelsmair_ue4.php?sort=bab">↑</a></th>
        <th>Ort<a href="Artelsmair_ue4.php?sort=oau">↓</a> <a href="Artelsmair_ue4.php?sort=oab">↑</a></th>
    </tr>
<?php


function sortarray($key,$ascdesc){
    global $termin;
    foreach($termin as $index){
        $sortvalue[] = $index[$key];
    }

    if (strcmp($ascdesc,"asc")) {
        array_multisort($sortvalue,SORT_ASC,$termin);
    } else if (strcmp($ascdesc,"desc")) {
        array_multisort($sortvalue,SORT_DESC,$termin);
    }

    return 0;
}

    $termin = array(
            array("termin" => "08.12.2018", "band" => "Warcraft ending", "ort" => "Wels"),
            array("termin" => "11.03.2019","band" => "Monster legends comming","ort" => "Wien"),
            array("termin" => "28.06.2019","band" => "Starcraft forever","ort" => "Linz"),
            array("termin" => "28.06.2020","band" => "Doom arising","ort" => "Graz"),
            );
    $i = 0;

// xau = x soll aufsteigend - xab= soll absteigend
if(isset($_GET['sort'])){
    switch($_GET['sort']){
        case "dau": sortarray("termin","asc");
            break;
        case "dab": sortarray("termin","desc");
            break;
        case "bau": sortarray("band","asc");
            break;
        case "bab": sortarray("band","desc");
            break;
        case "oau": sortarray("ort","asc");
            break;
        case "oab": sortarray("ort","desc");
            break;
        default : echo "";
            break;
    };
}


foreach($termin as $index) {
    echo "<tr>";
    $i++;
    echo  "<td>" . $i . "</td>";
    foreach ($index as $value) {
        echo  "<td>" . $value . "</td>";
    }
    echo  "</tr>";
}
?>


</table>
</body>
</html>
