package middleware;

import common.Album;
import common.AlbumDAOInterface;
import common.Song;
import common.SongDAOInterface;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class ClientHandler implements Runnable {
    protected Socket clientSocket;
    protected AlbumDAOInterface albumDAO;
    protected SongDAOInterface songDAO;
    protected Connection con;
    private Statement stmt;

    public ClientHandler(Socket clientSocket, Connection con) throws IOException {
        this.clientSocket = clientSocket;
        this.con = con;
    }


    public int rollback() {
        int err = 0;
        try {
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
            err = 2;
        }
        return err;
    }

    public int commit() {
        int err=0;
        try {
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
            err=2;
        }
        return (err);
    }

    public int dropTables() {
        int err=0;

        try {
            stmt.executeUpdate("DROP TABLE Album_songs");
            stmt.executeUpdate("DROP TABLE Albums");
            stmt.executeUpdate("DROP TABLE songs");
        } catch (SQLException e) {
            e.printStackTrace();
            err=2;
        }

        return err;
    }

    public int generateTestData(){
        int err=0;

        try {

        //Zuerst tabellen leeren
        stmt.executeUpdate("truncate table Album_songs");
        stmt.executeUpdate("truncate table albums");
        stmt.executeUpdate("truncate table songs");

        //Songs
        Song s1 = new Song("Despacito", "Simpson", "1234", "Pop");
        Song s2 = new Song("Despacito 2", "Simpson", "1235", "Pop");
        Song s3 = new Song("Harlem Shake", "Mr. Was geht Sie das an", "1234", "Wos was I");

        songDAO.insertSong(s1);
        songDAO.insertSong(s2);
        songDAO.insertSong(s3);


        //Albums
        Album a1 = new Album("despacitos", "keine Ahnung", "lolxd", "cover1.jpg");
        Album a2 = new Album("minecraft music", "keine Ahnung", "lolxd", "cover2.jpg");
        Album a3 = new Album("piano", "keine Ahnung", "piano", "cover3.jpg");

        albumDAO.insertAlbum(a1);
        albumDAO.insertAlbum(a2);
        albumDAO.insertAlbum(a3);


        //Album_Songs
        albumDAO.addSongToAlbum(a1, songDAO.getSong("Despacito"));
        albumDAO.addSongToAlbum(a1, songDAO.getSong("Despacito 2"));
        albumDAO.addSongToAlbum(a3, songDAO.getSong("Despacito"));
        } catch (SQLException e) {
            e.printStackTrace();
            err=2;
        }

        //con.commit();
        return err;
    }



    @Override
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String str = "";
            int err = 0;

            try {
                albumDAO = new AlbumDAO(con);
                songDAO = new SongDAO(con);
                albumDAO.setSongDAO(songDAO);
                stmt = con.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
                err=2;
            }
            oos.flush();

            //Tabellen erzeugen
            System.out.println("Creating Tables");
            try {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS songs ( songtitle VARCHAR(50) NOT NULL, artist VARCHAR(50), year VARCHAR(50), genre VARCHAR(50), PRIMARY KEY (songtitle)); ");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS albums (albumtitle VARCHAR(50) NOT NULL, artist VARCHAR(50), label VARCHAR(20), cover BLOB, PRIMARY KEY (albumtitle));");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Album_songs ( albumtitle VARCHAR(30) NOT NULL, songtitle VARCHAR(30), tracknumber INT, PRIMARY KEY (albumtitle, songtitle), FOREIGN KEY (albumtitle) REFERENCES Albums(albumtitle), FOREIGN KEY (songtitle) REFERENCES Songs(songtitle)); ");
                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                err=2;
            }


            String socketAddress = clientSocket.getLocalSocketAddress().toString();
            System.out.println("Verbindung zu " + socketAddress + " aufgebaut");

            while (!str.equals("q")) {
                str = ois.readUTF();
                System.out.println("Empfange: " + str);
                Album a=null;
                Song s=null;
                String string="";


                switch (str) {
                    case "deleteAlbum":
                        string =ois.readUTF();
                        a = (Album) ois.readObject();
                        System.out.println("Empfange: "+a);
                        try {
                            err = albumDAO.deleteAlbum(a);
                        } catch (SQLException e) {
                            err=2;
                        }
                        oos.writeUTF("deleteAlbum");
                        oos.writeUTF("int");
                        oos.write(err);
                        oos.flush();
                        break;
                    case "deleteSongFromAlbum":
                        string =ois.readUTF();
                        s = (Song) ois.readObject();
                        System.out.println("Empfange: "+s);
                        try {
                            a = albumDAO.deleteSong(s);
                        } catch (SQLException e) {
                            err=2;
                        }
                        oos.writeUTF("deleteSongFromAlbum");

                        oos.writeUTF("Album");
                        oos.writeObject(a);
                        oos.flush();
                        break;

                    case "rollback":

                        rollback();
                        break;

                    case "commit":

                        commit();
                        break;

                    case "dropTables":

                        dropTables();
                        break;

                    case "generateTestData":

                        generateTestData();
                        break;

                    case "insertAlbum":

                        string =ois.readUTF();
                        a = (Album) ois.readObject();
                        System.out.println("Empfange: "+a);
                        err = albumDAO.insertAlbum(a);
                        oos.writeUTF("insertAlbum");
                        oos.writeUTF("int");
                        oos.write(err);
                        oos.flush();
                        break;

                    case "addSongToAlbum":

                        string =ois.readUTF();
                        a = (Album) ois.readObject();
                        System.out.println("Empfange: "+a);
                        string =ois.readUTF();
                        s = (Song) ois.readObject();
                        System.out.println("Empfange: "+s);
                        err = albumDAO.addSongToAlbum(a,s);
                        oos.writeUTF("addSongToAlbum");
                        oos.writeUTF("int");
                        oos.write(err);
                        oos.flush();
                        break;

                    case "removeSongFromAlbum":


                        string =ois.readUTF();
                        a = (Album) ois.readObject();
                        System.out.println("Empfange: "+a);
                        string =ois.readUTF();
                        s = (Song) ois.readObject();
                        System.out.println("Empfange: "+s);
                        try {
                            err = albumDAO.removeSongFromAlbum(a,s);
                        } catch (SQLException e) {
                            err=2;
                        }
                        oos.writeUTF("removeSongFromAlbum");
                        oos.writeUTF("int");
                        oos.write(err);
                        System.out.println(err);
                        oos.flush();
                        break;

                    case "getAlbum":


                        string =ois.readUTF();
                        string =ois.readUTF();
                        System.out.println("Empfange: "+string);
                        try {
                            a = albumDAO.getAlbum(string);
                        } catch (SQLException e) {
                            err=2;
                        }
                        oos.writeUTF("getAlbum");
                        oos.writeUTF("Album");
                        oos.writeObject(a);
                        oos.flush();
                        break;

                    case "getAllAlbums":


                        Collection<Album> col= null;
                        try {
                            col = albumDAO.getAllAlbums();
                        } catch (SQLException e) {
                            err=2;
                        }
                        oos.writeUTF("getAllAlbums");
                        oos.writeUTF("Collection<Album>");
                        oos.writeObject(col);
                        oos.flush();
                        break;

                    case "getSong":


                        string =ois.readUTF();
                        string =ois.readUTF();
                        System.out.println("Empfange: "+string);
                        try {
                            s = songDAO.getSong(string);
                        } catch (SQLException e) {
                            err=2;
                        }
                        oos.writeUTF("getSong");
                        oos.writeUTF("Song");

                        oos.writeObject(s);
                        oos.flush();
                        break;

                    case "getAllSongs":

                        Collection<Song> coll= null;
                        try {
                            coll = songDAO.getAllSongs();
                        } catch (SQLException e) {
                            err=2;
                        }
                        oos.writeUTF("getAllSongs");
                        oos.writeUTF("Collection<Song>");

                        oos.writeObject(coll);
                        oos.flush();
                        break;

                    case "deleteSong":
                        string =ois.readUTF();
                        s = (Song) ois.readObject();
                        System.out.println("Empfange: "+s);
                        try {
                            err = songDAO.deleteSong(s);
                        } catch (SQLException e) {
                            err=2;
                        }
                        oos.writeUTF("deleteSong");
                        oos.writeUTF("int");
                        oos.flush();
                        oos.write(err);
                        oos.flush();
                        break;

                    case "insertSong":

                        string =ois.readUTF();
                        s = (Song) ois.readObject();
                        System.out.println("Empfange: "+s);
                        err = songDAO.insertSong(s);
                        oos.writeUTF("insertSong");
                        oos.writeUTF("int");
                        System.out.println(err);
                        oos.write(err);
                        oos.flush();
                        break;

                    case "editSong":

                        string =ois.readUTF();
                        string =ois.readUTF();

                        str =ois.readUTF();
                        s = (Song) ois.readObject();
                        System.out.println("Empfange: "+s);
                        try {
                            err = songDAO.editTuple(string, s);
                        } catch (SQLException e) {
                            err=2;
                        }

                        oos.writeUTF("editSong");
                        oos.writeUTF("int");
                        oos.write(err);
                        oos.flush();
                        break;

                    default:
                        System.out.println("Kein adequater Befehl");
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            rollback();
        } finally {
            try {
                clientSocket.close();

                System.out.println("Verbindung geschlossen");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
