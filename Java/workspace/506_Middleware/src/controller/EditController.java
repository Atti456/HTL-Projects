package controller;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import common.Album;
import common.Song;
import model.SongManagement;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class EditController implements Initializable{

    private SongManagement model;
    private Object o;
    private String object;

    private String newPath;
    private String oldSongTitle;
    private String oldAlbumTitle;
    private int err; //Error-ID


    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private Label label4;

    @FXML
    private Label label5;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField field4;

    @FXML
    private TextField field3;

    @FXML
    private TextField field2;

    @FXML
    private TextField field1;
    private String method;
    private FileChooser fileChooser;

    private File otpFile = new File("test.txt");

    @FXML
    void closeWindow(ActionEvent event) {
        model.closeEditWindow("", "");
        newPath = "";
    }

    @FXML
    void saveChanges(ActionEvent event) {

        if ((field1.getText().equals(oldAlbumTitle)) || (field1.getText().equals(oldSongTitle))){

        }else
        err = check(field1.getText());


        if ((object.equals("Album")) && (err == 0)){
            Album a = new Album();

            a.setAlbumtitle(field1.getText());
            a.setArtist(field2.getText());
            a.setLabel(field3.getText());
            a.setCover(newPath);

            if (method.equals("Edit")) {
                model.editAlbum(oldAlbumTitle, a);
            }else{
                err = model.insertAlbum(a);
                if (err == 3){
                    showErrorMessage("Probleme mit der Datei(Cover). Überprüfen Sie ob die Datei existiert.");
                }
            }

        }else if ((object.equals("Song")) && (err == 0)){
            Song s = new Song();

            s.setSongtitle(field1.getText());
            s.setArtist(field2.getText());
            s.setYear(field3.getText());
            s.setGenre(field4.getText());

            if (method.equals("Edit")) {
                model.editSong(oldSongTitle, s);
            }else{
                err = model.insertSong(s);
            }

        }else if (err == 1){
            showErrorMessage("Object with specified name already exists");
        }else if (err == 2){
            showErrorMessage("Error loading Data");
        }else{
            showErrorMessage("undefined Error");
        }
        model.closeEditWindow("save", object);
    }

    private int check(String s) {
        int error = 0;
        try {

            List<Album> albumList = new LinkedList<>();
            List<Song> songList = new LinkedList<>();
            albumList = (List<Album>) model.getAllAlbums();

            Album album = new Album();

            for (Album a : albumList) {
                songList = a.getSongs();
                System.out.println(songList);
                for (Song song : songList) {
                    if (song.getSongtitle().equals(s)) {
                       error = 1;
                    }
                }
            }
        }catch (SQLException | IOException e){
            error = 2;
        }
        return error;
    }

    @FXML
    void sellectCover(ActionEvent event) {
        // generate FileChooser Dialog
        fileChooser = new FileChooser();

        // get current Stage from an GUI Element - in this case use the TextField "fileNameTextField"
        Stage stage = (Stage) field4.getScene().getWindow();

        // Title for FileChooser Dialog
        fileChooser.setTitle("Select new Cover");
        // set Filter for Files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        // show the FileChooser Dialog and get the chosen File as "File-Object"
        otpFile  = fileChooser.showOpenDialog(stage);

        // sets the Textfield with the
        field4.setText(otpFile .getName());
        newPath = otpFile.getPath();
    }

    public void setModel(SongManagement model) {
        this.model = model;
    }

    public SongManagement getModel() {
        return model;
    }



    public void showErrorMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR, s);
        alert.show();

    }

    public void init(String method) {

        o = model.getEditData();
        object = model.getEditState();
        this.method = method;

        if (object.equals("Album") && method.equals("Edit")){
            Album a = (Album)o;


            Image image = null;
            InputStream is = null;
            try {
                is = new FileInputStream(a.getCover());
                newPath=a.getCover();
            } catch (FileNotFoundException e) {
                showErrorMessage("File not found");
            }
            image = new Image(is);




            label1.setText("Albumname");
            label2.setText("Artist");
            label3.setText("Label");
            label4.setText("Path to Cover");
            label5.setText("Cover");

            oldAlbumTitle = a.getAlbumtitle();
            field1.setText(a.getAlbumtitle());
            field2.setText(a.getArtist());
            field3.setText(a.getLabel());
            field4.setText(a.getCover());
            imageView.setImage(image);

        }else if (object.equals("Song") && method.equals("Edit")){
            Song s = (Song)o;

            label1.setText("Songname");
            label2.setText("Artist");
            label3.setText("Year");
            label4.setText("Genre");

            oldSongTitle = s.getSongtitle();
            field1.setText(s.getSongtitle());
            field2.setText(s.getArtist());
            field3.setText(s.getYear());
            field4.setText(s.getGenre());

            editButton.setDisable(true); //Button ausschalten, da nur bei Album benötigt.
        }else if (object.equals("Song")){
            label1.setText("Songname");
            label2.setText("Artist");
            label3.setText("Year");
            label4.setText("Genre");

            editButton.setDisable(true); //Button ausschalten, da nur bei Album benötigt.

        }else if (object.equals("Album")){
            label1.setText("Albumname");
            label2.setText("Artist");
            label3.setText("Label");
            label4.setText("Path to Cover");
            label5.setText("Cover");
        }else {
            showErrorMessage("undefined Error");
        }
    }


    @FXML
    private Button acceptButton;
    @FXML
    private Button editButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding bp;
        bp = field1.textProperty().isEmpty().or(field2.textProperty().isEmpty()).or(field3.textProperty().isEmpty()).or(field4.textProperty().isEmpty());
        acceptButton.disableProperty().bind(bp);

    }
}
