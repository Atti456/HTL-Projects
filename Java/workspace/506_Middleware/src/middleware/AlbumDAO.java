package middleware;

import common.Album;
import common.AlbumDAOInterface;
import common.Song;
import common.SongDAOInterface;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AlbumDAO implements AlbumDAOInterface, AutoCloseable {

    private PreparedStatement ps_removeSongFromAlbum;
    private PreparedStatement ps_deleteSongs;
    private PreparedStatement ps_deleteAlbumFromAlbum_songs;
    private PreparedStatement ps_deleteTuple;
    private PreparedStatement ps_selectSong;
    private String tableName;
    private int spaltenanzahl;
    private PreparedStatement ps_selectTuple;
    private PreparedStatement ps_insertInto;
    private PreparedStatement ps_insertIntoAlbumSongs;
    private Statement stmt;
    private int counter;
    Connection con;
    private SongDAOInterface songDAO;


    public void setSongDAO(SongDAOInterface songDAO) {
        this.songDAO = songDAO;
    }

    @Override
    public int deleteAlbum(Album a) {
        int err=0;
        try {
        //Delete Album Entry in Album_songs
        ps_deleteAlbumFromAlbum_songs.setString(1, a.getAlbumtitle());
        ps_deleteAlbumFromAlbum_songs.execute();

        //Delete Album Entry in Albums
        ps_deleteTuple.setString(1, a.getAlbumtitle());
        ps_deleteTuple.execute();
        } catch (Exception e) {
            e.printStackTrace();
            err = 1;
        }
        return err;
    }

    @Override
    public Album deleteSong(Song s) throws SQLException, IOException {


        List<Album> albumList = new LinkedList<>();
        List<Song> songList = new LinkedList<>();
        albumList = (List<Album>) getAllAlbums();

        Album album = new Album();

        for (Album a:albumList) {
            songList = a.getSongs();
            System.out.println(songList);
            for (Song song:songList) {
                if (song.getSongtitle().equals(s.getSongtitle())){
                    a.removeSongFromAlbum(s);
                    album=a;
                }
            }
        }

        ps_deleteSongs.setString(1, s.getSongtitle());
        ps_deleteSongs.execute();

        return album;
    }


    @Override
    public void setStreams(ObjectOutputStream oos, ObjectInputStream ois) {
    }

    @Override
    public int commit() {
        return 0;
    }


    public AlbumDAO(Connection con) throws SQLException {
        this.tableName = "albums";
        this.spaltenanzahl = 3;
        this.con = con;

        ps_deleteTuple = con.prepareStatement("Delete FROM Albums where albumtitle = ?");
        ps_deleteAlbumFromAlbum_songs = con.prepareStatement("Delete FROM Album_songs where albumtitle = ?");
        ps_deleteSongs = con.prepareStatement("Delete FROM Album_songs where songtitle = ?");
        ps_removeSongFromAlbum = con.prepareStatement("DELETE From Album_songs where albumtitle = ? AND songtitle = ?");

        ps_selectTuple = con.prepareStatement("SELECT * FROM Albums WHERE ? = albumtitle");
        ps_selectSong = con.prepareStatement("SELECT Songtitle FROM Album_songs WHERE ? = albumtitle");
        ps_insertIntoAlbumSongs = con.prepareStatement("INSERT INTO Album_songs VALUES (?, ?, ?)");
        ps_insertInto = con.prepareStatement("INSERT INTO Albums VALUES (?, ?, ?, ?)");

        stmt = con.createStatement();
        counter = 0;
    }


    @Override
    public int rollback() {
        return 0;
    }

    @Override
    public int insertAlbum(Album a){
        int err=0;
        try {
        ps_insertInto.setString(1,a.getAlbumtitle());
        ps_insertInto.setString(2,a.getArtist());
        ps_insertInto.setString(3,a.getLabel());
            FileInputStream fis = new FileInputStream(a.getCover());
            ps_insertInto.setBinaryStream(4,fis);
        } catch (SQLException e) {
            err = 2;
        } catch (FileNotFoundException e) {
            err = 3;
        }

        try {
            ps_insertInto.execute();
        } catch (SQLException e) {
            err=1;
        }

        return err;
    }

    @Override
    public int addSongToAlbum(Album a, Song s) {
        int err=0;

        try{
            a.addSong(s);


        ps_insertIntoAlbumSongs.setString(1,a.getAlbumtitle());
        ps_insertIntoAlbumSongs.setString(2,s.getSongtitle());
        ps_insertIntoAlbumSongs.setInt(3,(a.getIndex(s) + 1));
        } catch (SQLException e) {
            err=2;
        }

        try {
            ps_insertIntoAlbumSongs.execute();
        } catch (SQLException e) {
            err=1;
        }

        return err;
    }


    //SELECT * FROM ?
    public void printData() throws SQLException{
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

        System.out.println("");
        System.out.println("Print Data of " + tableName);
        while (rs.next()) {
            for (int i = 1; i < (spaltenanzahl+1); i++) {
                System.out.print(rs.getString(i));
                System.out.print("\t|\t");
            }
            System.out.println("");
        }
    }


    //SELECT * FROM ? WHERE ? = ?
    public void printAlbum(String albumtitle) throws SQLException {
        ps_selectTuple.setString(1, albumtitle);
        ResultSet rs = ps_selectTuple.executeQuery();

        System.out.println("");
        System.out.println("Print Data of " + tableName);
        while (rs.next()) {
            for (int i = 1; i < (spaltenanzahl+1); i++) {
                System.out.print(rs.getString(i));
                System.out.print("\t|\t");
            }
            System.out.println("");
        }
    }


    @Override
    public Album getAlbum(String albumtitle) throws SQLException, IOException {
        ps_selectTuple.setString(1, albumtitle);
        Album newA = null;
        SongDAO sd = new SongDAO(con);

        String newPath = "./image-output/newbild"+counter+".png";
        counter++;


        try(ResultSet rs = ps_selectTuple.executeQuery()){
            while (rs.next()) {

                newA = new Album(rs.getString(1), rs.getString(2), rs.getString(3));

                try (InputStream is = rs.getBinaryStream(4);
                     FileOutputStream fos = new FileOutputStream(newPath))
                {
                    fos.write(is.readAllBytes());
                    newA.setCover(newPath);
                }

            }
        }

        ps_selectSong.setString(1, albumtitle);


        try(ResultSet rese = ps_selectSong.executeQuery()) {
            while (rese.next()) {
                newA.addSong(songDAO.getSong(rese.getString(1)));
            }
        }

        return newA;
    }

    @Override
    public Collection<Album> getAllAlbums() throws SQLException, IOException {

        List<Album> collection = new ArrayList<>();
        String newPath = "./image-output/newbild"+counter+".png";


        try(ResultSet rs = stmt.executeQuery("select * from Albums")){
            while (rs.next()) {

                try (InputStream is = rs.getBinaryStream(4);
                     FileOutputStream fos = new FileOutputStream(newPath))
                {
                    fos.write(is.readAllBytes());

                }
                collection.add(new Album(rs.getString(1), rs.getString(2), rs.getString(3), newPath));
                counter++;
                newPath = "./image-output/newbild"+counter+".png";
            }
        }



        for (int i = 0; i < collection.size(); i++) {

            ps_selectSong.setString(1, collection.get(i).getAlbumtitle());

            try(ResultSet rese = ps_selectSong.executeQuery()) {
                while (rese.next()) {

                    collection.get(i).addSong(songDAO.getSong(rese.getString(1)));

                }
            }
        }

        return collection;
    }

    @Override
    public int removeSongFromAlbum(Album a, Song s) throws SQLException {

        int err = 0;

        err = a.removeSongFromAlbum(s);
        try {
            ps_removeSongFromAlbum.setString(1,a.getAlbumtitle());
            ps_removeSongFromAlbum.setString(2,s.getSongtitle());
            ps_removeSongFromAlbum.execute();
        }catch (Exception e){
            err = 2;
        }

        return err;
    }


    @Override
    public void close() throws Exception {
        ps_selectTuple.close();
        ps_selectSong.close();
        ps_insertIntoAlbumSongs.close();
        ps_insertInto.close();
        stmt.close();
    }


}
