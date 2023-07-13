package Address.view;

import java.io.File;

import Address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;


/**
 * Der Controller für das Root-Layout.
 * 
 * @author Artelsmair ALexander
 */
public class RootLayoutController {

    private MainApp mainApp;

    /**
     * Wird von der Hauptanwendung aufgerufen, einen Verweis auf sich selbst zu geben.
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Erstellt ein komplett neues komplett lehres Addressbuch
     */
    @FXML
    private void handleNew() {
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);
    }

    /**
     * Öffnet den Explorer, damit der User eine Datei aussuchen kann die geldaen werden soll.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Setzt einen Filter der nur Dateien it der Endung xml anzeigt.
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadPersonDataFromFile(file);
        }
    }

    /**
     * Speichert die Datei, die gerade geöffnet ist. Wenn es keine gibt
      * wird der Dialog "Speichern unter" angezeigt.
     */
    @FXML
    private void handleSave() {
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null) {
            mainApp.savePersonDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Öffnet den Explorer um die DAtei zu speichern.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

     // Setzt einen Filter der nur Dateien it der Endung xml anzeigt.
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Überprüft nochmal ob die DAtei auch die richtige Endung hat.
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePersonDataToFile(file);
        }
    }

   
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("AddressApp");
        alert.setHeaderText("About");
        alert.setContentText("Author: Artelsmair ALexander");

        alert.showAndWait();
    }

    /**
     * Schließt die Anwendung
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    /**
     * Öffnet das Fenster für die Geburtstagsstatistik
     */
    @FXML
    private void handleShowBirthdayStatistics() {
      mainApp.showBirthdayStatistics();
    }
}
