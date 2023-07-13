package model;

import common.Album;
import common.AlbumDAOInterface;
import common.Song;
import common.SongDAOInterface;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.Collection;

public class RemoteAlbumDAO implements AlbumDAOInterface, AutoCloseable{


    private String tableName;
    private int spaltenanzahl;
    private Statement stmt;
    private int counter;
    Connection con;
    private SongDAOInterface songDAO;


    
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    public void setSongDAO(SongDAOInterface songDAO) {
        this.songDAO = songDAO;
    }

    @Override
    public int deleteAlbum(Album a){
        int err=0;
        String str="";

        try {
            oos.writeUTF("deleteAlbum");
            oos.writeUTF("Album");
            oos.writeObject(a);
            oos.flush();

            str = ois.readUTF();
            System.out.print("Antwort von: "+ str);
            System.out.print(": ");
            str = ois.readUTF();
            err = ois.read();

            System.out.print(err);
        } catch (Exception e) {
            err=1;
        }

        return err;
    }

    @Override
    public Album deleteSong(Song s) {

        int err=0;
        Album a = null;
        String str="";

        try {
            oos.writeUTF("deleteSongFromAlbum");
            oos.writeUTF("Song");
            oos.writeObject(s);
            oos.flush();

            str = ois.readUTF();
            System.out.print("Antwort von: "+ str);
            System.out.print(": ");
            str = ois.readUTF();
            a = (Album) ois.readObject();
            System.out.print(str);
            System.out.print(a);

        } catch (Exception e) {
            err=1;
            e.printStackTrace();
        }

        return a;
    }

    @Override
    public int rollback() {
        int err=0;
        try {
            oos.writeUTF("rollback");
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            err=1;
        }
        return err;
    }


    public RemoteAlbumDAO(Socket serverSocket) throws SQLException, IOException {
        this.tableName = "albums";
        this.spaltenanzahl = 3;
        counter = 0;

    }


    @Override
    public int insertAlbum(Album a){
        int err=0;
        String str="";

        try {
            oos.writeUTF("insertAlbum");
            oos.writeUTF("Album");
            oos.writeObject(a);
            oos.flush();

            str = ois.readUTF();
            System.out.print("Antwort von: "+ str);
            System.out.print(": ");
            str = ois.readUTF();
            err = ois.read();

            System.out.print(err);
        } catch (Exception e) {
            err=1;
        }

        return err;
    }

    @Override
    public int addSongToAlbum(Album a, Song s) {
        int err=0;

        String str="";

        try {
            oos.writeUTF("addSongToAlbum");
            oos.writeUTF("Album");
            oos.writeObject(a);
            //oos.flush();

            oos.writeUTF("Song");
            oos.writeObject(s);
            oos.flush();

            str = ois.readUTF();
            System.out.print("Antwort von: "+ str);
            System.out.print(": ");
            str = ois.readUTF();
            err = ois.read();

            System.out.print(err);
        } catch (Exception e) {
            err=1;
        }

        return err;
    }

    @Override
    public Album getAlbum(String albumtitle) throws SQLException, IOException {
        int err=0;
        Album a = null;
        String str="";

        try {
            oos.writeUTF("getAlbum");
            oos.writeUTF("String");
            oos.writeUTF(albumtitle);
            oos.flush();

            str = ois.readUTF();
            System.out.print("Antwort von: "+ str);
            System.out.print(": ");
            str = ois.readUTF();
            a = (Album) ois.readObject();
            System.out.print(str);
            System.out.print(a);

        } catch (Exception e) {
            err=1;
            e.printStackTrace();
        }

        return a;
    }

    @Override
    public Collection<Album> getAllAlbums(){

        int err=0;
        Collection<Album> a = null;
        String str="";

        try {
            oos.writeUTF("getAllAlbums");
            oos.flush();

            str = ois.readUTF();
            System.out.print("Antwort von: "+ str);
            System.out.print(": ");
            str = ois.readUTF();
            a = (Collection<Album>) ois.readObject();
            System.out.print(str);
            System.out.print(a);

        } catch (Exception e) {
            err=1;
            e.printStackTrace();
        }

        return a;
    }

    @Override
    public int removeSongFromAlbum(Album a, Song s) throws SQLException {

        int err=0;

        String str="";

        try {
            oos.writeUTF("removeSongFromAlbum");
            oos.writeUTF("Album");
            oos.writeObject(a);
            oos.flush();

            oos.writeUTF("Song");
            oos.writeObject(s);
            oos.flush();

            str = ois.readUTF();
            System.out.print("Antwort von: "+ str);
            System.out.print(": ");
            str = ois.readUTF();
            err = ois.read();

            System.out.print(err);
        } catch (Exception e) {
            err=1;
        }

        return err;
    }

    @Override
    public void close() throws Exception {
        //oos.close();
        //ois.close();
    }

    @Override
    public void setStreams(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
    }

    @Override
    public int commit() {
        int err=0;
        try {
            oos.writeUTF("commit");
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            err=1;
        }
        return err;
    }
}
