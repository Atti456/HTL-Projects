package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Collection;

public interface SongDAOInterface {
    int editTuple(String oldSongTitle, Song s) throws SQLException;

    int insertSong(Song s);

    Song getSong(String songname) throws SQLException;

    Collection<Song> getAllSongs() throws SQLException, IOException;

    int deleteSong(Song s) throws SQLException;

    AlbumDAOInterface getAlbums();

    void setStreams(ObjectOutputStream oos, ObjectInputStream ois);
}
