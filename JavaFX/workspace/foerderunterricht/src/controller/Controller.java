package controller;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.SimpleService;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private SimpleService simpleService;
    @FXML
    private TextField copyFrom;

    @FXML
    private TextField copyTo;

    @FXML
    private Button copyFromSelect;

    @FXML
    private Button copyToSelect;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private Button copybutton;

    private FileChooser fileChooser;


    @FXML
    void onActionCopyButton(ActionEvent event) {

        simpleService.setTo(new File(copyTo.getText()));

        if(simpleService.getState() == Worker.State.READY) {
            simpleService.start();
        }else{
            simpleService.restart();}
    }

    @FXML
    void onActionCopyFromSelect(ActionEvent event) {
        fileChooser = new FileChooser();

        Stage stage = (Stage) copyFrom.getScene().getWindow();

        fileChooser.setTitle("Copy from File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        copyFrom.setText(selectedFile.getName());
        simpleService.setFrom(selectedFile);
    }

    @FXML
    void onActionCopyToSelect(ActionEvent event) {
        fileChooser = new FileChooser();

        Stage stage = (Stage) copyTo.getScene().getWindow();

        fileChooser.setTitle("Copy to");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        copyTo.setText(selectedFile.getName());
        simpleService.setTo(selectedFile);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        simpleService = new SimpleService();

        copybutton.disableProperty().bind(copyFrom.textProperty().isEmpty().or(copyTo.textProperty().isEmpty()));

        progressbar.progressProperty().bind(simpleService.progressProperty());

    }
}
