package model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;

public class KeySpinner{
    private File tofile;
    private int keyvalue;



    public KeySpinner() {
    }

    public File getTofile() {
        return tofile;
    }

    public void setTofile(File tofile) {
        this.tofile = tofile;
    }


    public int getKeyvalue() {
        return keyvalue;
    }

    public void setKeyvalue(int keyvalue) {
        this.keyvalue = keyvalue;
    }
}
