package controller;

import common.Album;
import common.Song;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class Controller implements Initializable {

    private SongManagement model;

    private ObservableList albumNamesOBList;
    private ObservableList albumSongsOBList;
    private ObservableList songsOBList;

    private int albumItemsCounter=1;
    private int songsItemsCounter=1;
    private int albumSongsItemsCounter=1;
    private List<Song> songs = new LinkedList<>();
    private ChangeListener changeListener;
    private int err;
    private KeyService service;
    private Collection<Album> acol;
    private LoadService loadService;


    public SongManagement getModel() {
        return model;
    }

    public void setModel(SongManagement model) {
        this.model = model;
        service.setModel(model);
    }

    @FXML
    private ListView<?> AlbumView;

    @FXML
    private Button prevAlbumButton;

    @FXML
    private Button nextAlbumButton;

    @FXML
    private ListView<?> AlbumSongsView;

    @FXML
    private Button prevAlbumSongButton;

    @FXML
    private Button nextAlbumSongButton;

    @FXML
    private Label AlbumNameLabel;

    @FXML
    private ImageView ImageViewCover;

    @FXML
    private ListView<?> SongsView;

    @FXML
    private Button prevSongButton;

    @FXML
    private Button nextSongButton;

    public void showErrorMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR, s);
        alert.show();
    }

    public Optional<ButtonType> showConfirmMessage(String message, String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.setContentText(context);

        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    @FXML
    void generateTestData(ActionEvent event) {
        try {
            Optional<ButtonType> result = showConfirmMessage("Generating Test-Data deletes all data!", "Are you sure you want to continue?");
            if (result.get() == ButtonType.OK){
                model.generateTestData();
            } else {
                //don't generate Test-Data
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Could not generate Testdata");
        }
    }

    @FXML
    void refresh(ActionEvent event) {

        Collection<Song> scol;

        songs.clear();

        try {

            AlbumView.getSelectionModel().selectedItemProperty().removeListener(changeListener);

            List<Song> albumSongs;



            try {

                //get Albums
                if (loadService.getState() == Worker.State.READY) {
                    loadService.start();
                } else {
                    loadService.restart();
                }

                loadService.setOnSucceeded(event1 -> {
                    acol = loadService.getValue();
                    List<String> albumNames = new LinkedList<>();


                    //Do not do when trying the service
                    try {
                        acol = model.getAllAlbums();
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }





                    for (Album a:acol) {
                        albumNames.add(a.getAlbumtitle());
                    }
                    albumNamesOBList = FXCollections.observableArrayList(albumNames);
                    AlbumView.setItems(albumNamesOBList);
                });

            }catch (Exception e){
                e.printStackTrace();
            }




            //get Album songs
            try {
                albumSongs = model.getAlbum(AlbumView.getSelectionModel().getSelectedItem().toString()).getSongs();
                albumSongsOBList = FXCollections.observableArrayList(albumSongs);
                AlbumSongsView.setItems(albumSongsOBList);
            }catch (Exception e){

            }



            //get Songs
            scol = model.getAllSongs();
            songs.addAll(scol);
            songsOBList = FXCollections.observableArrayList(songs);
            SongsView.setItems(songsOBList);



            albumItemsCounter = 1;
            songsItemsCounter = 1;
            albumSongsItemsCounter =1;

            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    AlbumView.getSelectionModel().select(albumItemsCounter);
                    AlbumView.getFocusModel().focus(albumItemsCounter);

                    SongsView.getSelectionModel().select(songsItemsCounter);
                    SongsView.getFocusModel().focus(songsItemsCounter);

                    AlbumSongsView.getSelectionModel().select(albumSongsItemsCounter);
                    AlbumSongsView.getFocusModel().focus(albumSongsItemsCounter);
                }
            });

            AlbumView.getSelectionModel().selectedItemProperty().addListener(changeListener);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not initialize");
            showErrorMessage("Could not refresh");
        }
    }

    @FXML
    void addSongToAlbum(ActionEvent event) {
        Album a; Song s;
        try {
            a = model.getAlbum(AlbumView.getSelectionModel().getSelectedItem().toString());
            s = songs.get(SongsView.getSelectionModel().getSelectedIndex());
            err = model.addSongToAlbum(a,s);
            refresh(null);

        } catch (IOException | SQLException e) {
            e.printStackTrace();

            showErrorMessage("Can not add Song to Album!");
        }
    }

    @FXML
    void createAlbum(ActionEvent event) {
        try {
            model.openEditWindow("Add new", "Album");
        } catch (IOException e) {
            showErrorMessage("Can not open Edit-Window!");
        }
    }

    @FXML
    void createSong(ActionEvent event) {
        try {
            model.openEditWindow("Add new", "Song");
        } catch (IOException e) {
            showErrorMessage("Can not open Edit-Window!");
        }

    }

    @FXML
    void deleteAlbum(ActionEvent event) {
        Album a = null;
        try {
            a = model.getAlbum(AlbumView.getSelectionModel().getSelectedItem().toString());
            model.deleteAlbum(a);
            refresh(null);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showErrorMessage("Can not delete Album!");
        }

    }

    @FXML
    void deleteSong(ActionEvent event) {
        Song s = null;
        try {
            s = songs.get(SongsView.getSelectionModel().getSelectedIndex());
            model.deleteSong(s);
            refresh(null);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showErrorMessage("Can not delete Song!");
        }
    }

    @FXML
    void deleteSongFromAlbum(ActionEvent event) {
        Album a; Song s;
        try {
            a = model.getAlbum(AlbumView.getSelectionModel().getSelectedItem().toString());
            s = a.getSongs().get(AlbumSongsView.getSelectionModel().getSelectedIndex());
            err = model.deleteSongFromAlbum(a,s);
            refresh(null);
        } catch (IOException | SQLException e) {
            e.printStackTrace();

            showErrorMessage("Can not delete Song from Album!");
        }catch (IndexOutOfBoundsException e){
            showErrorMessage("Select song to delete");
        }

    }

    @FXML
    void editAlbum(ActionEvent event) {
        try {
            Album a = model.getAlbum(AlbumView.getSelectionModel().getSelectedItem().toString());
            model.setEditData(a);
            model.openEditWindow("Edit", "Album");
        } catch (IOException | SQLException e) {
            showErrorMessage("Can not open Edit-Window!");
            e.printStackTrace();
        }
    }

    @FXML
    void editSong(ActionEvent event) {
        try {
            Song a = songs.get(SongsView.getSelectionModel().getSelectedIndex());
            model.setEditData(a);
            model.openEditWindow("Edit", "Song");
        } catch (IOException e) {
            showErrorMessage("Can not open Edit-Window!");
        }
    }




    @FXML
    void selectNextAlbum(ActionEvent event) {
        if (albumItemsCounter < AlbumView.getItems().size()-1){
            albumItemsCounter++;
        }

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                AlbumView.getSelectionModel().select(albumItemsCounter);
                AlbumView.getFocusModel().focus(albumItemsCounter);

            }
        });
    }

    @FXML
    void selectPreviousAlbum(ActionEvent event) {

        if (albumItemsCounter > 0){
            albumItemsCounter--;
        }

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                AlbumView.getSelectionModel().select(albumItemsCounter);
                AlbumView.getFocusModel().focus(albumItemsCounter);

            }
        });
    }

    @FXML
    void selectPreviousSong(ActionEvent event) {
        if (songsItemsCounter > 0){
            songsItemsCounter--;
        }

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                SongsView.getSelectionModel().select(songsItemsCounter);
                SongsView.getFocusModel().focus(songsItemsCounter);

            }
        });
    }

    @FXML
    void selectNextSong(ActionEvent event) {
        if (songsItemsCounter < songsOBList.size()-1){
            songsItemsCounter++;
        }

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                SongsView.getSelectionModel().select(songsItemsCounter);
                SongsView.getFocusModel().focus(songsItemsCounter);

            }
        });
    }

    @FXML
    void selectPreviousAlbumSong(ActionEvent event) {
        if (albumSongsItemsCounter > 0){
            albumSongsItemsCounter--;
        }

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                AlbumSongsView.getSelectionModel().select(albumSongsItemsCounter);
                AlbumSongsView.getFocusModel().focus(albumSongsItemsCounter);

            }
        });
    }

    @FXML
    void selectNextAlbumSong(ActionEvent event) {
        if (albumSongsItemsCounter < albumSongsOBList.size()-1){
            albumSongsItemsCounter++;
        }

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                AlbumSongsView.getSelectionModel().select(albumSongsItemsCounter);
                AlbumSongsView.getFocusModel().focus(albumSongsItemsCounter);

            }
        });
    }


    @FXML
    void saveDataToDatabase(ActionEvent event) {
        try {
            model.saveData();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Could not save Data");
        }
    }



    @FXML
    void load(ActionEvent event) {
        Collection<Album> acol;
        Collection<Song> scol;

        try {
            List<String> albumNames = new LinkedList<>();
            System.out.println("load");
            //get Songs
            scol = model.getAllSongs();
            songs.addAll(scol);





            //get Albums
            service.start();

            /*
            acol=model.getAllAlbums();
            for (Album a:acol) {
                albumNames.add(a.getAlbumtitle());
            }
            albumNamesOBList = FXCollections.observableArrayList(albumNames);
            AlbumView.setItems(albumNamesOBList);

             */


            songsOBList = FXCollections.observableArrayList(songs);
            SongsView.setItems(songsOBList);


            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    AlbumView.getSelectionModel().select(albumItemsCounter);
                    AlbumView.getFocusModel().focus(albumItemsCounter);

                    SongsView.getSelectionModel().select(songsItemsCounter);
                    SongsView.getFocusModel().focus(songsItemsCounter);

                    AlbumSongsView.getSelectionModel().select(albumSongsItemsCounter);
                    AlbumSongsView.getFocusModel().focus(albumSongsItemsCounter);

                    connect.setDisable(true);
                }
            });

            //Changelistener damit die zum Album gehöhrigen Daten angezeigt werden können
            changeListener = (observable, oldValue, newValue) -> {
                List<Song> albumSongs1 = new LinkedList<>();

                Album a = null;
                try {
                    a = model.getAlbum((String) newValue);
                } catch (IOException e) {
                    showErrorMessage("IOException");
                } catch (SQLException e) {
                    showErrorMessage("SQLException");
                }
                try {
                    albumSongs1 = a.getSongs();
                }catch (NullPointerException e){
                    showErrorMessage("You need to refresh!");
                }

                albumSongsOBList = FXCollections.observableArrayList(albumSongs1);

                Image image = null;
                InputStream is = null;
                try {
                    is = new FileInputStream(a.getCover());
                } catch (FileNotFoundException e) {
                    showErrorMessage("File not found");
                }
                image = new Image(is);

                AlbumNameLabel.setText((String) newValue);
                ImageViewCover.setImage(image);
                AlbumSongsView.setItems(albumSongsOBList);
            };

            AlbumView.getSelectionModel().selectedItemProperty().addListener(changeListener);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not initialize");
            showErrorMessage("Could not initialize");
        }
    }

    @FXML
    private MenuItem connect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new KeyService();
        service.setAlbumView(AlbumView);

        loadService = new LoadService();


        service.setOnSucceeded(event -> {
            System.out.println("Succeeded with Value: " + service.getValue());


        });

        service.setOnFailed(event -> {
            System.out.println("Failed with Value: " + service.getValue());
            service.getException().printStackTrace();
        });


        /*
        deleteAlbum.disableProperty().bind(AlbumView.focusedProperty());
        editAlbum.disableProperty().bind(AlbumView.focusedProperty());
        addSongToAlbum.disableProperty().bind(AlbumView.focusedProperty().and(SongsView.focusedProperty()));
        deleteSong.disableProperty().bind(SongsView.focusedProperty());
        editSong.disableProperty().bind(SongsView.focusedProperty());
        deleteSongFromAlbum.disableProperty().bind(AlbumView.focusedProperty().and(SongsView.focusedProperty()));


         */

    }


    public void setAcol(Collection<Album> acol) {
        this.acol = acol;
    }

    @FXML
    private Button deleteAlbum;

    @FXML
    private Button deleteSongFromAlbum;

    @FXML
    private Button editSong;

    @FXML
    private Button deleteSong;

    @FXML
    private Button addSongToAlbum;

    @FXML
    private Button editAlbum;
}
