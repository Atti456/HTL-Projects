package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Collection;

public interface AlbumDAOInterface {
    int rollback();

    int insertAlbum(Album a);

    int addSongToAlbum(Album a, Song s);

    Album getAlbum(String albumtitle) throws SQLException, IOException;

    Collection<Album> getAllAlbums() throws SQLException, IOException;

    int removeSongFromAlbum(Album piano, Song gangnam) throws SQLException;

    void setSongDAO(SongDAOInterface songTable);

    int deleteAlbum(Album a) throws SQLException;

    Album deleteSong(Song s) throws SQLException, IOException;

    void setStreams(ObjectOutputStream oos, ObjectInputStream ois);


    int commit();
}
