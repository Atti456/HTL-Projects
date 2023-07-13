package controller;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import model.DiceService;
import model.Dices;

public class DiceController {

    @FXML
    private Rectangle six;

    @FXML
    private Rectangle one;

    @FXML
    private Button start;

    @FXML
    private Rectangle three;

    @FXML
    private Rectangle two;

    @FXML
    private Label sixPercentage;

    @FXML
    private TextField delay;

    @FXML
    private Label fivePercentage;

    @FXML
    private Rectangle four;

    @FXML
    private Label fourPercentage;

    @FXML
    private Label threePercentage;

    @FXML
    private Label twoPercentage;

    @FXML
    private Rectangle five;

    @FXML
    private Label onePercentage;

    private DiceService service;
    private Dices dices;

    @FXML
    private void initialize()
    {
        // Initialisierung
        dices=new Dices(6);
        service = new DiceService(0, dices);

        //Bindings
        start.disableProperty().bind(delay.textProperty().isEmpty());

        one.widthProperty().bind(dices.getDiceProperty(0));
        two.widthProperty().bind(dices.getDiceProperty(1));
        three.widthProperty().bind(dices.getDiceProperty(2));
        four.widthProperty().bind(dices.getDiceProperty(3));
        five.widthProperty().bind(dices.getDiceProperty(4));
        six.widthProperty().bind(dices.getDiceProperty(5));

        service.setOnSucceeded(e->{
            onePercentage.setText(String.format("%.2f%%",(float) dices.getDice(0)/1000*100));
            twoPercentage.setText(String.format("%.2f%%",(float) dices.getDice(1)/1000*100));
            threePercentage.setText(String.format("%.2f%%",(float) dices.getDice(2)/1000*100));
            fourPercentage.setText(String.format("%.2f%%",(float) dices.getDice(3)/1000*100));
            fivePercentage.setText(String.format("%.2f%%",(float) dices.getDice(4)/1000*100));
            sixPercentage.setText(String.format("%.2f%%",(float) dices.getDice(5)/1000*100));


        });
    }

    @FXML
    void onStart(ActionEvent event)
    {
        service.setDelay(Integer.parseInt(delay.getText()));

        dices.clearDices();






        onePercentage.setText("");
        twoPercentage.setText("");
        threePercentage.setText("");
        fourPercentage.setText("");
        fivePercentage.setText("");
        sixPercentage.setText("");


        if (service.getState() == Worker.State.READY)
        service.start();
        else
            service.restart();
    }

}