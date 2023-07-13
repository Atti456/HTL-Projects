package common;

import java.io.Serializable;

public class Song implements Serializable {

    private String songtitle;
    private String artist;
    private String year;
    private String genre;

    public Song(String songtitle, String artist, String year, String genre) {
        this.songtitle = songtitle;
        this.artist = artist;
        this.year = year;
        this.genre = genre;
    }

    public Song() {
    }

    public String getSongtitle() {
        return songtitle;
    }

    public void setSongtitle(String songtitle) {
        this.songtitle = songtitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    @Override
    public String toString() {
        return "Song{" +
                "songtitle='" + songtitle + '\'' +
                ", artist='" + artist + '\'' +
                ", year='" + year + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
