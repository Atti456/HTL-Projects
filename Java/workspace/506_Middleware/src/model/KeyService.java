package model;

import common.Album;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class KeyService extends Service<Collection<Album>> {

    private SongManagement model;
    private Collection<Album> albums;
    private ObservableList albumNamesOBList;
    @FXML
    private ListView<?> AlbumView;

    public KeyService() {

    }

    @Override
    protected Task<Collection<Album>> createTask() {
        return new Task<Collection<Album>>() {
            @Override
            protected Collection<Album> call() throws Exception {

                List<String> albumNames = new LinkedList<>();

                albums = model.getAllAlbums();
                for (Album a:albums) {
                    albumNames.add(a.getAlbumtitle());
                }

                albumNamesOBList=FXCollections.observableArrayList(albumNames);

                AlbumView.setItems(albumNamesOBList);
                return albums;
            }
        };
    }


    public void setModel(SongManagement model) {
        this.model = model;
    }

    public Collection<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Collection<Album> albums) {
        this.albums = albums;
    }

    public void setAlbumView(ListView<?> albumView) {
        AlbumView = albumView;
    }
}
