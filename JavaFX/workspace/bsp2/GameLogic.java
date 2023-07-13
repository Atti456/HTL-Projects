

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;

public class GameLogic {

    private GridPane root;
    private ArrayList<MemoryButton> buttons;
    private final int max = 2;
    private int prevbuttonID = 0;
    private int run = 0;

    public GameLogic(GridPane root,int amount)
    {
        this.root=root;
        this.buttons=new ArrayList<>();
        init(amount);
    }

    public int getPrevbuttonID() {
        return prevbuttonID;
    }

    public void setPrevbuttonID(int prevbuttonID) {
        this.prevbuttonID = prevbuttonID;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }





    public void init(int amount)
    {
        for (int i = 0; i <amount ; i++) {

            MemoryButton button = new MemoryButton(i % amount / 2,this);
            button.setMinSize(100,100);
            button.setMaxSize(150,150);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED,button);
            buttons.add(button);

        }

        Collections.shuffle(buttons);

        int i=0;
        for (int y = 0; y < Math.sqrt(amount); y++) {

            for (int x = 0; x <Math.sqrt(amount) ; x++) {
                root.add(buttons.get(i),x,y);
                i++;
            }

        }




    }

    public void buttonClicked(MemoryButton button)
    {


            button.flip();


            System.out.println("Button :" + button.getIdentification() + "wurde gedrückt");

    }

}
