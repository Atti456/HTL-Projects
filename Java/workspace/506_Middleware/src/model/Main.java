package model;

import controller.Controller;
import controller.EditController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    private Controller controller;
    SongManagement songManagement;
    private EditController editController;
    private Stage newStage;


    /*Protokoll:

        Befehl(String); DataType(String); Object(DataType)
        InsertAlbum; Album, a

        Antwort(String); DataType(String); Rückgabewert(DataType)
        InsertAlbum:; int; 0
        getAlbums; Col<Album>; col

        q   -   quits the connection
     */


    /*Fehlercode

    0   -   erfolgreich
    1   -   Connection to Server Error
    2   -   SQLE
    3   -   IOE
    4   -   NPE

     */


    @Override
    public void start(Stage primaryStage) throws Exception{
        songManagement = new SongManagement(this);
        songManagement.init();
        FXMLLoader loader=new FXMLLoader(Main.class.getResource("../view/sample.fxml"));
        Parent root = loader.load();
        controller=loader.getController();
        controller.setModel(songManagement);
        songManagement.setController(controller);

        primaryStage.setTitle("SongManagement");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public void openEditWindow(String method, String object) throws IOException {
        FXMLLoader loader=new FXMLLoader(Main.class.getResource("../view/edit.fxml"));
        Parent newScene = loader.load();
        editController=loader.getController();
        editController.setModel(songManagement);

        newStage = new Stage();

        newStage.setTitle(method+object);
        newStage.setScene(new Scene(newScene));
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.show();

        songManagement.setNewStage(newStage);
        editController.init(method);
    }
    //TODO: Task-Service; fehlerbehandlung, bindings,



}
