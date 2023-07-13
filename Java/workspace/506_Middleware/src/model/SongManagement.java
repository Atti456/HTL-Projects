package model;

import common.Album;
import common.AlbumDAOInterface;
import common.Song;
import common.SongDAOInterface;
import controller.Controller;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class SongManagement {

    private SongDAOInterface songTable;
    private AlbumDAOInterface albums;
    private Connection con;
    private Stage newStage;

    public Main main;

    private Object editObject;
    private String editState;
    private String object;
    private Statement stmt;
    private int err;
    private Controller controller;

    private String address;
    private int port;
    private Socket serverSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;



    public SongManagement(Main main) {
        this.main = main;
    }


    private int loadProps() throws IOException {
        Properties prop = new Properties(); // Properties laden
        try (FileInputStream in = new FileInputStream("Artelsmair.properties")) {
            prop.load(in);
        }
        String driver = prop.getProperty("driver");
        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String pwd = prop.getProperty("pwd");
        address = prop.getProperty("address");
        port = Integer.parseInt(prop.getProperty("port"));

        System.out.println(address + port);

        return 0;
    }

    public int generateTestData() {
        String str;
        try {
            oos.writeUTF("generateTestData");
            oos.flush();

        }catch (IOException e) {
            err=3;
        }

        //con.commit();
        return err;
    }

    private int dropTables() {
        int err=0;

        String str;
        try {
            oos.writeUTF("dropTables");
            oos.flush();


        }catch (IOException e) {
            err=3;
        }

        //con.commit();
        return err;
    }

    public void printMetaData(DatabaseMetaData data) throws SQLException {
        System.out.println("URL: " + data.getURL());
        System.out.println("Name des Users: " + data.getUserName());
        System.out.println("Database-Product Name: " + data.getDatabaseProductName());
        System.out.println("Version: " + data.getDatabaseMajorVersion() + "." + data.getDatabaseMinorVersion());
        System.out.println("Name des JDBC Treibers: " + data.getDriverName());
        System.out.println("Treiber-Version: " + data.getDriverVersion());
        System.out.println("Unterstützt Batch: " + data.supportsBatchUpdates());
    }

    public void init() throws SQLException {

        int error = 0;

        try {
            try {
                loadProps();
            }catch (IOException e){
                error = 3;
                System.out.println("Could not load Properties");
            }

            try{
                serverSocket = new Socket(address, port);
            }catch (Exception e){
                e.printStackTrace();
                error=1;
            }


            try {
                if (serverSocket != null) {
                    songTable = new RemoteSongDAO(serverSocket);
                    albums = songTable.getAlbums();

                    oos = new ObjectOutputStream(serverSocket.getOutputStream());
                    oos.flush();
                    ois = new ObjectInputStream(serverSocket.getInputStream());

                    songTable.setStreams(oos, ois);
                    albums.setStreams(oos, ois);

                }else {
                    System.out.println("null");
                    error=1;
                }
            }catch (Exception e){
                e.printStackTrace();
                error=1;
            }

            try {
                //dropTables();
            }catch (Exception e){
                error = 2;
            }

        }catch (Exception e){
            e.printStackTrace();
            albums.rollback();

        }

    }



    public Collection<Album> getAllAlbums() throws IOException, SQLException {
        return albums.getAllAlbums();
    }

    public Collection<Song> getAllSongs() throws IOException, SQLException {
        return songTable.getAllSongs();
    }

    public Album getAlbum(String name) throws IOException, SQLException {
        return albums.getAlbum(name);
    }

    public void openEditWindow(String method, String object) throws IOException {
        editState = object;
        main.openEditWindow(method, object);


    }

    public void closeEditWindow(String type, String object) {
        if (type.equals("save")){
            this.object = object;
        }
        newStage.close();
        editState = "Closed";
    }

    public void setNewStage(Stage stage) {
        this.newStage = stage;
    }

    public void setEditData(Object o) {
        editObject = o;
    }

    public Object getEditData() {
        return editObject;
    }

    public Song getSong(String songName) throws SQLException {
        return songTable.getSong(songName);
    }

    public String getEditState() {
        return editState;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int insertSong(Song s) {
        int err = songTable.insertSong(s);

        return err;
    }

    public int editSong(String oldSongTitle, Song s) {
        try {
            Album a;
            Song song = songTable.getSong(oldSongTitle);
            a = albums.deleteSong(song);
            songTable.deleteSong(song);
            songTable.insertSong(s);
            System.out.println(a);
            System.out.println(s);
            if(a != null){
                albums.addSongToAlbum(a,s);
            }


        } catch (SQLException | IOException e) {
            err = 1;

        }
        return err;

    }

    public int editAlbum(String oldAlbumTitle, Album a) {
        try {
            Album album = albums.getAlbum(oldAlbumTitle);
            List<Song> oldSongs = album.getSongs();
            albums.deleteAlbum(album);
            albums.insertAlbum(a);
            System.out.println(a);
            System.out.println(oldSongs);
            if(oldSongs != null){
                for (Song s:oldSongs) {
                    albums.addSongToAlbum(a,s);
                }
            }


        } catch (SQLException | IOException e) {
            err = 1;

        }
        return err;
    }

    public int insertAlbum(Album a) {
        int err = albums.insertAlbum(a);
        return err;
    }

    public int deleteAlbum(Album a) throws SQLException {
        int err;
        err=albums.deleteAlbum(a);
        return err;
    }

    public int deleteSong(Song s) throws SQLException, IOException {
        int err;
        albums.deleteSong(s);
        err = songTable.deleteSong(s);
        System.out.println(err);
        return err;
    }

    public int deleteSongFromAlbum(Album a, Song s) throws SQLException {
        int err = albums.removeSongFromAlbum(a,s);
        return err;
    }

    public int addSongToAlbum(Album a, Song s) {
        int err = albums.addSongToAlbum(a,s);
        a.addSong(s);
        return err;
    }

    public void saveData() throws SQLException {
        albums.commit();

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }
}
