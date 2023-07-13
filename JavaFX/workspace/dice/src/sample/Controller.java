package sample;

import javafx.concurrent.Task;

import java.awt.*;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Rectangle;

public class Controller{

    @FXML
    private Rectangle pb1;

    @FXML
    private ProgressBar pb2;

    @FXML
    private ProgressBar pb3;

    @FXML
    private ProgressBar pb4;

    @FXML
    private ProgressBar pb5;

    @FXML
    private ProgressBar pb6;

    @FXML
    private Button start;


    @FXML
    void startcount(ActionEvent event) {
        new Thread(task).start();
    }



    Random r = new Random();
    int num = 0;

    Task<Integer>task = new Task<Integer>() {
        @Override protected Integer call() throws Exception {
            int iterations;
            for (iterations = 0; iterations < 600; iterations++) {
                if (isCancelled()) {
                    break;
                }
                System.out.println("Iteration " + iterations);
                num = (r.nextInt(6)+1);
                System.out.println(num);

                if (num == 1)
                {
                    pb1.widthProperty().add(1);
                }
            }
            return iterations;
        }
    };
}
