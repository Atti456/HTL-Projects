package common;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Album implements Serializable {

    private String albumtitle;
    private String artist;
    private String label;
    private List<Song> songs;
    private String cover;


    public Album(String albumtitle, String artist, String label) {
        this.albumtitle = albumtitle;
        this.artist = artist;
        this.label = label;
        songs = new LinkedList<>();

    }

    public Album(String albumtitle, String artist, String label, String cover) {
        this.albumtitle = albumtitle;
        this.artist = artist;
        this.label = label;
        songs = new LinkedList<>();
        this.cover = cover;
    }

    public Album() {
        songs = new LinkedList<>();
    }

    public String getAlbumtitle() {
        return albumtitle;
    }

    public void setAlbumtitle(String albumtitle) {
        this.albumtitle = albumtitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void addSong(Song s){
        songs.add(s);

    }

    public int getIndex(Song s){
        return songs.indexOf(s);
    }



    public int removeSongFromAlbum(Song s){

        int err = 0;
        try {
            songs.remove(s);
        }catch (Exception e){
            err = 1;
        }
        return err;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumtitle='" + albumtitle + '\'' +
                ", artist='" + artist + '\'' +
                ", label='" + label + '\'' +
                ", songs=" + songs +
                '}';
    }
}
