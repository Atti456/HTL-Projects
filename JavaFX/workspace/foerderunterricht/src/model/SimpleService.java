package model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;

import java.io.File;

public class SimpleService extends Service<Long> {

    File from;
    File to;

    public SimpleService() {
    }

    public SimpleService(File from, File to) {
        this.from = from;
        this.to = to;
    }

    public File getFrom() {
        return from;
    }

    public void setFrom(File from) {
        this.from = from;
    }

    public File getTo() {
        return to;
    }

    public void setTo(File to) {
        this.to = to;
    }

    @Override
    protected Task<Long> createTask() {
        return new SimpleTask(from, to);
    }


}
