package middleware;

import common.Album;
import common.AlbumDAOInterface;
import common.Song;
import common.SongDAOInterface;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {


    // Client Connection via Socket Class
    private Socket connect;
    private static Connection con;
    private static int port;

    public static void main(String[] args) throws SQLException, IOException {

        try {
            con = loadProps();
        } catch (SQLException | IOException e) {
            e.printStackTrace();

            //fehlermeldung wenn props nicht geladen werden können
        }

        AlbumDAOInterface albumDAO;
        SongDAOInterface songDAO;
        System.out.println("Starting Server");


        albumDAO = new AlbumDAO(con);
        songDAO = new SongDAO(con);
        albumDAO.setSongDAO(songDAO);
        ExecutorService executorService = Executors.newCachedThreadPool();



        System.out.println("-----------------------------");
        System.out.println("Initializing DB");
        System.out.println("-----------------------------");


        System.out.println(albumDAO.getAllAlbums());

        Album piano = new Album("piano2", "keine Ahnung", "piano", "cover4.jpg");
        Song gangnam = new Song("Gangnam Style", "Mr. Was geht Sie das an", "1234", "Wos was I");
        Album a1;
        Album a2;
        int err;

        err = songDAO.insertSong(gangnam);  System.out.println(err);
        err = albumDAO.insertAlbum(piano);  System.out.println(err);


        a1 = albumDAO.getAlbum("piano2");   System.out.println(a1.toString());

        err = albumDAO.addSongToAlbum(piano, gangnam);  System.out.println(err);

        a2 = albumDAO.getAlbum("piano2");   System.out.println(a2.toString());

        int errr = albumDAO.removeSongFromAlbum(piano, gangnam);    System.out.println(errr);

        a2 = albumDAO.getAlbum("piano2");   System.out.println(a2.toString());




        //Aufbau zu Client
        try (ServerSocket serverSocket = new ServerSocket(port)){

            while (true) {
                Socket clientConnection = serverSocket.accept();
                if (clientConnection != null){
                    System.out.println("Verbindung hergestellt");
                    System.out.println(clientConnection.getLocalSocketAddress());
                    //Kommunikation mit Client übernimmt der ClientHandler in einem neuen Thread.
                    executorService.execute(new ClientHandler(clientConnection, con));
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static Connection loadProps() throws SQLException, IOException {
        Properties prop = new Properties(); // Properties laden
        try (FileInputStream in = new FileInputStream("Artelsmair.properties")) {
            prop.load(in);
        }
        String driver = prop.getProperty("driver");
        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String pwd = prop.getProperty("pwd");
        port = Integer.parseInt(prop.getProperty("port"));


        return DriverManager.getConnection(url, user, pwd);
    }

}
