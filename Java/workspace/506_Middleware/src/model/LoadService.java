package model;

import common.Album;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Collection;

public class LoadService extends Service<Collection<Album>> {

    private SongManagement model;


    public LoadService() {

    }

    @Override
    protected Task<Collection<Album>> createTask() {
        return new Task<Collection<Album>>() {
            @Override
            protected Collection<Album> call() throws Exception {

                Collection<Album> albums = model.getAllAlbums();

                return albums;
            }
        };
    }


    public void setModel(SongManagement model) {
        this.model = model;
    }

}
