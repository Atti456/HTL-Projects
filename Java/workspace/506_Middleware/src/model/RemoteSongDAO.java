package model;

import common.AlbumDAOInterface;
import common.Song;
import common.SongDAOInterface;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.Collection;

public class RemoteSongDAO implements SongDAOInterface, AutoCloseable {

    private PreparedStatement ps_deleteTuple;
    private String tableName;
    private int spaltenanzahl;
    private PreparedStatement ps_selectTuple;
    private PreparedStatement ps_insertInto;
    private Statement stmt;
    DatabaseMetaData data;
    private AlbumDAOInterface albums;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;




    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getSpaltenanzahl() {
        return spaltenanzahl;
    }

    public void setSpaltenanzahl(int spaltenanzahl) {
        this.spaltenanzahl = spaltenanzahl;
    }


    public RemoteSongDAO(Socket serverSocket) throws IOException, SQLException {
        this.tableName = "songs";
        this.spaltenanzahl = 4;

        albums = new RemoteAlbumDAO(serverSocket);
    }

    @Override
    public int editTuple(String oldSongTitle, Song s) throws SQLException {

        int err=0;

        String str="";

        try {
            oos.writeUTF("editSong");
            oos.writeUTF("String");
            oos.writeUTF(oldSongTitle);

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
    public int insertSong(Song s){

        int err=0;

        String str="";

        try {
            oos.writeUTF("insertSong");

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



    public void loadCSV(String path) throws IOException, SQLException {
        //loads the users and the corresponding passwords
        String str;
        String[] elems;
        int linecount = 1;


        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            str = br.readLine();
            while ((str = br.readLine())!= null)
            {
                linecount++;
                elems = str.split(";");

                try {

                if (data.supportsBatchUpdates()){
                    ps_insertInto.setString(1, elems[0]);
                    ps_insertInto.setString(2,elems[1]);
                    ps_insertInto.setString(3,elems[2]);
                    ps_insertInto.setString(4,elems[3]);
                    ps_insertInto.addBatch();
                }


                    ps_insertInto.executeBatch();
                } catch (SQLException | IndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("");
                    System.out.println("Could not load Elements with PrimaryKey: " + elems[0] + " || Element in " + path + " in line: " + linecount);
                }
            }
        }

    }

    @Override
    public Song getSong(String songname) throws SQLException {
        int err=0;
        Song s=null;
        String str="";

        try {
            oos.writeUTF("getSong");
            oos.writeUTF("String");
            oos.writeUTF(songname);
            oos.flush();

            str = ois.readUTF();
            System.out.print("Antwort von: "+ str);
            System.out.print(": ");
            str = ois.readUTF();
            s = (Song) ois.readObject();
            System.out.print(s);


        } catch (Exception e) {
            err=1;
        }


        return s;
    }


    @Override
    public Collection<Song> getAllSongs() throws SQLException, IOException {

        int err=0;
        Collection<Song> s=null;
        String str="";

        try {
            System.out.println("load");
            oos.writeUTF("getAllSongs");
            oos.flush();


            str = ois.readUTF();
            System.out.print("Antwort von: "+ str);
            System.out.print(": ");
            str = ois.readUTF();
            s = (Collection<Song>) ois.readObject();
            System.out.print(s);

            System.out.println("loadfinished");
        } catch (Exception e) {
            err=1;
            e.printStackTrace();
        }


        return s;
    }


    @Override
    public int deleteSong(Song s) throws SQLException {
        int err=0;

        String str="";

        try {
            oos.writeUTF("deleteSong");
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
    public AlbumDAOInterface getAlbums() {
        return albums;
    }

    @Override
    public void setStreams(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
    }
}
