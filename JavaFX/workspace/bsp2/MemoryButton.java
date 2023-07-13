

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MemoryButton extends Button implements EventHandler<MouseEvent> {

    private int id;
    private boolean visible;
    private GameLogic logic;
    private ImageView img;
    private ImageView back;

    public MemoryButton(int identification,GameLogic logic)
    {
        this.id=identification;
        this.visible=false;
        this.logic=logic;
        this.img = new ImageView("./images/" + id + ".png");
        this.back = new ImageView("./images/background.png");
        this.setGraphic(back);

        setText(" ");
    }


    public int getIdentification() {
        return id;
    }

    public void flip()
    {
        visible= !visible;

        if (visible)
        {
            this.setGraphic(img);
        }
        else{
            this.setGraphic(back);
        }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemoryButton)) return false;

        MemoryButton that = (MemoryButton) o;

        if (getIdentification() != that.getIdentification()) return false;
        return isVisible() == that.isVisible();
    }


    @Override
    public void handle(MouseEvent event) {
        logic.buttonClicked(this);
    }
}

