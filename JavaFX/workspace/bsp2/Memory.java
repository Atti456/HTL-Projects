

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Memory extends Application {
    private GameLogic logic;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene ;


        primaryStage.setTitle("Memory");

        GridPane root =new GridPane();
        logic=new GameLogic(root,16);



        scene=new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
