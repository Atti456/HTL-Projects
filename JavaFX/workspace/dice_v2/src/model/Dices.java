package model;

import javafx.beans.property.SimpleIntegerProperty;

public class Dices {

    private SimpleIntegerProperty[] diceArray;

    public Dices(int dices)
    {
        diceArray=new SimpleIntegerProperty[dices];
        for (int i = 0; i < diceArray.length; i++) {
            diceArray[i]=new SimpleIntegerProperty(0);
        }
    }

    public SimpleIntegerProperty[] getDiceArray() {
        return diceArray;
    }

    public SimpleIntegerProperty getDiceProperty(int i)
    {
        return diceArray[i];
    }

    public int getDice(int i)
    {
       return diceArray[i].get();
    }

    public void setDice(int i, int value)
    {
        diceArray[i].set(value);
    }

    public void clearDices()
    {
        for (SimpleIntegerProperty simpleIntegerProperty : diceArray) {
            simpleIntegerProperty.set(0);
        }
    }
}
