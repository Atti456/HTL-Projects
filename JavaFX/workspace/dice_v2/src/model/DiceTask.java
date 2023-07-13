package model;

import javafx.concurrent.Task;

import java.util.Random;

public class DiceTask extends Task<Integer>{
    private int delay;
    private Dices dices;

    public DiceTask(int delay, Dices dices)
    {
        this.dices = dices;
        this.delay= delay;

    }
    @Override
    protected Integer call() throws Exception {
        Random r = new Random();
        int i=0, rand=0;

        for (i = 0; i < 1000; i++) {
            rand=r.nextInt(6);

            dices.setDice(rand, dices.getDice(rand)+1);

            Thread.sleep(delay);
        }

        return null;
    }
}
