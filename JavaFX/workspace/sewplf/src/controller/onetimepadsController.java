package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import model.KeySpinner;
import model.KeySpinnerService;

import java.io.File;

public class onetimepadsController {

    @FXML
    private Spinner<Integer> keylengthfield;

    @FXML
    private TextField filenametextField;

    @FXML
    private Button choosefrom;

    @FXML
    private Button generateButton;

    @FXML
    private Label statustext;

    @FXML
    private Label statusprozent;

    @FXML
    private ProgressBar progressbar;

    private FileChooser fileChooser;
    private File otpFile = null;
    private KeySpinner spinner;
    private KeySpinnerService spinnerService;


    @FXML
    public void initialize() {
        spinner = new KeySpinner();
        spinnerService = new KeySpinnerService(spinner);
        keylengthfield.setEditable(true);
        keylengthfield.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 10000));
        generateButton.disableProperty().bind(filenametextField.textProperty().isEmpty().or(keylengthfield.valueProperty().isNull()));
        progressbar.progressProperty().bind(spinnerService.progressProperty());
        statustext.textProperty().bind(spinnerService.messageProperty());

       // statusprozent.setText(String.format("%s%%",(progressbar.progressProperty().getValue()+1)*100));

    }

    @FXML
    void filechooser(ActionEvent event) {
        // generate FileChooser Dialog
        fileChooser = new FileChooser();

        // get current Stage from an GUI Element - in this case use the TextField "fileNameTextField"
        Stage stage = (Stage) choosefrom.getScene().getWindow();

        // Title for FileChooser Dialog
        fileChooser.setTitle("Save OTP Key");
        // set Filter for Files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        // show the FileChooser Dialog and get the chosen File as "File-Object"
        otpFile  = fileChooser.showSaveDialog(stage);
        // sets the Textfield with the
        filenametextField.setText(otpFile.getName());
    }

    @FXML
    void generate(ActionEvent event) {

        spinner.setKeyvalue(keylengthfield.getValue());
        spinner.setTofile(otpFile);
        statusprozent.textProperty().bind((progressbar.progressProperty()).multiply(100).asString("%.0f%%"));
       if(spinnerService.getState() == Worker.State.READY){
           spinnerService.start();
       }else{
           spinnerService.restart();
       }
    }


}
