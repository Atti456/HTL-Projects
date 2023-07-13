package com.example.battleships_solitaire.Model;

import java.io.Serializable;

public class LevelCollection implements Serializable {
    private String name;
    private Difficulty difficulty;
    private int progression;
    private final int MAXLEVELS = 30;


    public LevelCollection(String name, Difficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
        this.progression = 0;
    }

    public static enum Difficulty{
        einfach, mittel, schwierig
    }

    public int getMAXLEVELS() {
        return MAXLEVELS;
    }

    public int getProgression() {
        return progression;
    }

    public void setProgression(int progression) {
        this.progression = progression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
