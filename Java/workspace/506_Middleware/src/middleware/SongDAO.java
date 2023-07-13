package middleware;

import common.AlbumDAOInterface;
import common.Song;
import common.SongDAOInterface;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SongDAO implements SongDAOInterface, AutoCloseable {

    private PreparedStatement ps_deleteTuple;
    private String tableName;
    private int spaltenanzahl;
    private PreparedStatement ps_selectTuple;
    private PreparedStatement ps_insertInto;
    private Statement stmt;
    DatabaseMetaData data;


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


    public SongDAO(Connection con) throws SQLException {
        this.tableName = "songs";
        this.spaltenanzahl = 4;

        ps_selectTuple = con.prepareStatement("SELECT * FROM Songs WHERE ? = songtitle");
        ps_deleteTuple = con.prepareStatement("UPDATE Songs SET artist = ?, year = ?, genre = ? WHERE songtitle = ?");
        ps_insertInto = con.prepareStatement("INSERT INTO Songs VALUES (?, ?, ?, ?)");
        ps_deleteTuple = con.prepareStatement("Delete FROM Songs where songtitle = ?");
        stmt = con.createStatement();

        data = con.getMetaData();
    }

    @Override
    public int editTuple(String oldSongTitle, Song s)  {
        int err=0;
        try {
        ps_deleteTuple.setString(1,s.getArtist());
        ps_deleteTuple.setString(2,s.getYear());
        ps_deleteTuple.setString(3,s.getGenre());
        ps_deleteTuple.setString(4,oldSongTitle);


            ps_insertInto.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            err=2;
        }

        return err;
    }


    @Override
    public int insertSong(Song s){

        int err = 0;

        try {
        ps_insertInto.setString(1,s.getSongtitle());
        ps_insertInto.setString(2,s.getArtist());
        ps_insertInto.setString(3,s.getYear());
        ps_insertInto.setString(4,s.getGenre());
        } catch (SQLException e) {
            err=2;
        }

        try {
            ps_insertInto.execute();
        } catch (SQLException e) {
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
        ps_selectTuple.setString(1, songname);

        Song s = new Song();

        try (ResultSet rs = ps_selectTuple.executeQuery();) {

            while (rs.next()) {
                s.setSongtitle(rs.getString(1));
                s.setArtist(rs.getString(2));
                s.setGenre(rs.getString(3));
                s.setYear(rs.getString(4));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }


    @Override
    public Collection<Song> getAllSongs() throws SQLException, IOException {

        List<Song> collection = new ArrayList<>();

        try (ResultSet rs = stmt.executeQuery("select * from Songs")) {
            while (rs.next()) {
                collection.add(new Song(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        }
        return collection;
    }


    @Override
    public int deleteSong(Song s) {
        int err=0;
        try {
            ps_deleteTuple.setString(1, s.getSongtitle());
            ps_deleteTuple.execute();
        }catch (Exception e){
            err = 1;
        }
        return err;
    }

    @Override
    public AlbumDAOInterface getAlbums() {
        return null;
    }

    @Override
    public void setStreams(ObjectOutputStream oos, ObjectInputStream ois) {

    }

    @Override
    public void close() throws Exception {
        ps_selectTuple.close();
        ps_insertInto.close();
        stmt.close();
    }
}
