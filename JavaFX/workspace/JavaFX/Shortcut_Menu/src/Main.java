

import javafx.application.Application;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.*;



public class Main extends Application
{





    @Override
    public void start(Stage primaryStage) throws Exception {

        HBox root = new HBox();
        root.setPrefSize(340,70);
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);

        Button button1 = new Button();
        button1.setPrefSize(64, 64);

        Button button2 = new Button();
        button2.setPrefSize(64, 64);

        Button button3 = new Button();
        button3.setPrefSize(64, 64);

        Button button4 = new Button();
        button4.setPrefSize(64, 64);

        Button button5 = new Button();
        button5.setPrefSize(64, 64);


        button1.getStyleClass().add("button1");
        button2.getStyleClass().add("button2");
        button3.getStyleClass().add("button3");
        button4.getStyleClass().add("button4");
        button5.getStyleClass().add("button5");




        root.getChildren().add(button1);
        root.getChildren().add(button2);
        root.getChildren().add(button3);
        root.getChildren().add(button4);
        root.getChildren().add(button5);


        //"https://www.youtube.com/?gl=AT&hl=de"
        //"https://mail.google.com/mail/u/0/#inbox"
        //"https://lichess.org/"
        //"https://www.reddit.com/"



        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                getHostServices().showDocument("https://www.youtube.com/?gl=AT&hl=de");

            }
        });

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                getHostServices().showDocument("https://mail.google.com/mail/u/0/#inbox");

            }
        });

        button3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                getHostServices().showDocument("https://lichess.org/");

            }
        });

        button4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                getHostServices().showDocument("https://www.reddit.com/");

            }
        });

        button5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
                Platform.exit();
            }
        });




        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.setY(20);
        primaryStage.setX(Screen.getPrimary().getVisualBounds().getWidth()/2-170);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
