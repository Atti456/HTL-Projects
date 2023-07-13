package model;

import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

public class KeySpinnerTask extends Task<Long> {
    private KeySpinner spinner;

    public KeySpinnerTask(KeySpinner spinner) {
        this.spinner = spinner;
    }

    @Override
    protected Long call() throws Exception {
        Random random = new Random();
        try(FileOutputStream ou = new FileOutputStream(spinner.getTofile())) {
            updateMessage("Generating Key No:30");
            for (int i = 0; i < spinner.getKeyvalue(); i++) {
                char b = (char) (random.nextInt(25 + 1) + 65);
                updateProgress(i+1,spinner.getKeyvalue());
                ou.write(b);
                Thread.sleep(30);
            }
        }
        updateMessage("Finished");
        return null;
    }
}
