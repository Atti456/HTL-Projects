package sample;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class WebserverClientService extends Service {
    private String line;


    public WebserverClientService(String line) {
        this.line = line;

    }

    public WebserverClientService() {

    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    protected Task createTask() {

        return new WebserverClientTask(line);
    }


}
