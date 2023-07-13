package model;

import javafx.concurrent.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

public class KeyTask extends Task<Integer> {

    File file;
    int length;

    public KeyTask(File file, int length) {
        this.file = file;
        this.length = length;
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
    protected Integer call() throws Exception {

        int iterations = 0;
        Random r = new Random();
        int i;

        try (OutputStream os = new FileOutputStream(file)){
            for (iterations = 0; iterations < length; iterations++) {
                i = r.nextInt('Z'+1-'A')+'A';
                os.write((char)i);
                updateProgress(iterations+1, length);
                updateMessage("Generating Key No: " + iterations*length/100);

                Thread.sleep(200);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        updateMessage("Finished");



        return iterations;
    }
}
