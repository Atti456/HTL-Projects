package controller;

import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.KeyService;

import java.io.File;

public class Controller {

    @FXML
    private Label message;

    @FXML
    private Label percent;

    @FXML
    private Button selectbutton;

    @FXML
    private Button startbutton;

    @FXML
    private TextField selectFile;

    @FXML
    private ProgressBar pb;

    @FXML
    private Spinner<Integer> keylength;

    private FileChooser fileChooser;

    private File otpFile = new File("test.txt");

    private KeyService service;


    @FXML
    public void initialize() {

        service = new KeyService();


        keylength.setEditable(true);
        keylength.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 10000));

        startbutton.disableProperty().bind(keylength.valueProperty().isNull().or(selectFile.textProperty().isEmpty()));

        pb.progressProperty().bind(service.progressProperty());
        message.textProperty().bind(service.messageProperty());

        percent.textProperty().bind(service.progressProperty().multiply(100).asString("%.2f%%"));



        service.setOnSucceeded(event -> {
            selectFile.clear();
        });

    }

    @FXML
    void selectFile(ActionEvent event) {
        // generate FileChooser Dialog
        fileChooser = new FileChooser();

        // get current Stage from an GUI Element - in this case use the TextField "fileNameTextField"
        Stage stage = (Stage) selectFile.getScene().getWindow();

        // Title for FileChooser Dialog
        fileChooser.setTitle("Save OTP Key");
        // set Filter for Files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        // show the FileChooser Dialog and get the chosen File as "File-Object"
        otpFile  = fileChooser.showOpenDialog(stage);

        // sets the Textfield with the
        selectFile.setText(otpFile .getName());


    }

    @FXML
    void start(ActionEvent event) {



        if (service.getState() == Worker.State.READY){
            service.setLength(keylength.getValue());
            service.setFile(otpFile);



            service.start();
        }
        else {
            service.restart();
        }



    }

}
