package model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class KeySpinnerService extends Service<Long> {

    private KeySpinner spinner;

    public KeySpinnerService(KeySpinner spinner) {
        this.spinner = spinner;
    }

    @Override
    protected Task<Long> createTask() {
        return new KeySpinnerTask(spinner);
    }
}
