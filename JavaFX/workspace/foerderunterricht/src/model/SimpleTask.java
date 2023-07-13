package model;

import controller.Controller;
import javafx.concurrent.Task;

import java.io.*;
import java.util.stream.Stream;

public class SimpleTask extends Task<Long> {

    File from;
    File to;

    public SimpleTask(File from, File to) {
        this.from = from;
        this.to = to;
    }

    @Override
    protected Long call() throws Exception {
        long fileSize = from.length();
        long bytesCopied=0;
        int data = -1;

        updateMessage("Ready");
      try (InputStream in = new FileInputStream(from);
            OutputStream ou = new FileOutputStream(to))
        {
            updateMessage("Copy....");
            while ((data= in.read())!=-1){
                ou.write(data);
                bytesCopied++;
                updateProgress(bytesCopied, fileSize);
                updateMessage("Bytes copied: " + bytesCopied);
                System.out.println("Bytes copied: " + bytesCopied);
                Thread.sleep(10);
            }

        }
      updateMessage("Finish");
        return fileSize;
    }
}
