package com.example.battleships_solitaire.Model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model implements Serializable {
    public List<LevelCollection> list;

    public Model() {
        list = new ArrayList<>();
    }

    public List<LevelCollection> getList() {
        return list;
    }

    public void setList(List<LevelCollection> list) {
        this.list = list;
    }

    public void addToList(LevelCollection b){
        list.add(b);
    }
    public void deleteFromList(LevelCollection b){
        list.remove(b);
    }

}
