package model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;

public class KeyService extends Service {

    File file;
    int length;

    public KeyService() {

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    protected Task createTask() {
        return new KeyTask(file, length);
    }
}
