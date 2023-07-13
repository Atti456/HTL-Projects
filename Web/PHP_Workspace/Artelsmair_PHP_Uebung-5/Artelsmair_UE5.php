<!-- Artelsmair
13.5.2019 -->

<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Rechner</title>
</head>
<body>
<table>
    <form method="get">
        <tr>
            <td><label>Zahl 1</label></td>
            <td><input type="text" id="zahl1" name="Zahl1"></td>
        </tr>
        <tr>
            <td><label>Operation </label></td>
            <td>
                <select name="operator" size="1">
                    <option value="+">+</option>
                    <option value="-">-</option>
                    <option value="*">*</option>
                    <option value="/">/</option>
                </select>
            </td>
        </tr>
        <tr>
            <td><label>Zahl 2</label></td>
            <td><input type="text" id="zahl2" name="Zahl2"></td>
        </tr>
        <tr>
            <td><button>=</button></td>
        </tr>
        <tr>
            <td><label>Resultat </label></td>
            <td><output style="border: 1px solid black; margin-left: 3px; padding:3px">
                    <?php calculate()?>
                </output></td>
        </tr>
        <tr>
            <td><button onclick="window.location.reload()">Löschen</button></td>
        </tr>
    </form>
</table>

</body>
</html>







<?php

function calculate(){
    
    if(isset($_GET['Zahl1']) && $_GET['Zahl1']!=NULL && $_GET['Zahl2']!=NULL &&isset($_GET['Zahl2'])) {
        if ($_GET['operator'] == '+') {
            echo $_GET['Zahl1'] + $_GET['Zahl2'];
        } else if ($_GET['operator'] == '-') {
            echo $_GET['Zahl1'] - $_GET['Zahl2'];
        } else if ($_GET['operator'] == '*') {
            echo $_GET['Zahl1'] * $_GET['Zahl2'];
        } else if ($_GET['operator'] == '/' && $_GET['Zahl2']!=0) {
            echo $_GET['Zahl1'] / $_GET['Zahl2'];
        }
    }
}

?>