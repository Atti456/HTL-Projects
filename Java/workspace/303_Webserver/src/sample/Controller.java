package sample;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


public class Controller implements Initializable {

    @FXML
    protected TextArea textArea;

    @FXML
    private TextField textField;

    @FXML
    private Button SendButton;



    WebserverClientService service;
    private Main model;

    @FXML
    void sendText(ActionEvent event) {

        service.setLine(textField.getText());

        if (service.getState() == Worker.State.READY){
            service.start();
        }
        else {
            service.restart();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Verbinde zu localhost auf Port 8800");
        //Verbindungsaufbau zu Server

        System.out.println("? ");
        textArea.appendText("? "  + "\n");

        service = new WebserverClientService();

    }

}
