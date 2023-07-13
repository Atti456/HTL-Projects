package model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DiceService extends Service<Integer>{
    private int delay;
    private Dices dices;

    public DiceService(int delay, Dices dices) {
        this.delay = delay;
        this.dices = dices;
    }

    @Override
    protected Task<Integer> createTask() {
        return new DiceTask(delay,dices);
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
