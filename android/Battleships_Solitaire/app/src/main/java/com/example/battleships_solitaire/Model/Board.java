package com.example.battleships_solitaire.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

public class Board
{
    private int[][] current_grid_array;
    private int[][] defiened_grid_array;
    private boolean arrayIsFull;
    //0 = empty
    //1 = water
    //2 = ship

    private int[] horizontalIndex;
    private int[] verticalIndex;

    CellGroupFragmentVerticalIndex cellGroupFragmentVerticalIndex;
    CellGroupFragmentHorizontalIndex cellGroupFragmentHorizontalIndex;

    private int[][] array;
    private int gridsize;


    public Board() {
        current_grid_array = new int[][]{  {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        defiened_grid_array = new int[][]{  {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1}
        };
        array = new int[][]{  {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };


        //Test Data:
        verticalIndex = new int[]{1,2,3,1,2,3};
        horizontalIndex = new int[]{1,2,3,1,2,3};
    }


    public int[][] getCurrent_grid_array() {
        return current_grid_array;
    }

    public void setCurrent_grid_array(int[][] current_grid_array) {
        this.current_grid_array = current_grid_array;
    }

    public int[] getHorizontalIndex() {
        return horizontalIndex;
    }

    public void setHorizontalIndex(int[] horizontalIndex) {
        this.horizontalIndex = horizontalIndex;
    }

    public int[] getVerticalIndex() {
        return verticalIndex;
    }

    public void setVerticalIndex(int[] verticalIndex) {
        this.verticalIndex = verticalIndex;
    }




    public Exception loadPresetArray(int lenght, char indicator, InputStream inputStream, int progress){
        Exception ex = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {

            for (int i = 0; i < progress; i++) {
                bufferedReader.readLine();
                bufferedReader.readLine();
                bufferedReader.readLine();
            }

            String line = bufferedReader.readLine();
            String[] strings = line.split(String.valueOf(indicator));
            for (int j = 0; j < lenght; j++) {
                verticalIndex[j] = Integer.parseInt(strings[j]);
            }

            line = bufferedReader.readLine();
            strings = line.split(String.valueOf(indicator));
            for (int j = 0; j < lenght; j++) {
                horizontalIndex[j] = Integer.parseInt(strings[j]);
            }

            bufferedReader.close();
        } catch (Exception e) {
            ex = e;
        }
        return ex;
    }

    public void generateLevels(int size, String path){

        String gamemode = "einfach";
        String newPath = "";
        newPath = path + gamemode + ".txt";

        File file = new File(newPath);
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            System.out.println("Level Generation failed");
            e.printStackTrace();
        }

        for (int idd = 1; idd <= 30; idd++) {

            int shiptiles = 0;
            int[][] array = new int[size][size];
            int random = 0;
            int schiffe = 0;

            if (size == 6) {
                shiptiles = 8;
            }

            Random r = new Random();

            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array.length; j++) {
                    random = r.nextInt(4);
                    System.out.println("random: " + random);
                    if (random == 3) {
                        array[i][j] = 2;
                    } else array[i][j] = 1;
                }
            }

            printArray();


            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    if (array[i][j] == 2) {
                        schiffe = (schiffe + array[i][j]);
                    }
                }
                //verticalIndexTextViews[i].setText(String.valueOf(schiffe / 2));
                verticalIndex[i] = (schiffe / 2);

                System.out.println("vertical: " + verticalIndex[i]);

                schiffe = 0;
            }

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    if (array[j][i] == 2) {
                        schiffe = (schiffe + array[j][i]);
                    }
                }
                //horizontalIndexTextViews[i].setText(String.valueOf(schiffe / 2));
                horizontalIndex[i] = (schiffe / 2);
                schiffe = 0;

                System.out.println("horizontal: " + horizontalIndex[i]);

            }

            defiened_grid_array = array;

            if (!path.equals("")) {
                //safeToFile(path);
            }


            //////////////////////////////////



            if (!path.equals("")) {
                try {
                    for (int i : verticalIndex) {
                        br.write(i + ";");
                        br.flush();

                    }
                    br.newLine();
                    br.flush();

                    for (int i : horizontalIndex) {
                        br.write(i + ";");
                        br.flush();
                    }
                    //br.newLine();

                    br.newLine();
                    br.newLine();
                    br.flush();
                } catch (Exception e) {
                    System.out.println("Level Generation Failed");
                }

                System.out.println("Wrote to file: " + newPath);

            }
        }
    }


    /** boolean representing whethere a specific square is empty or not*/
    private boolean emptySquare = true;

    public void populateBoard(int gridsize, int lengthOfShip)
    {
        this.gridsize = gridsize;

        /** instance used for randomly placing the ships*/
        Random random = new Random();

        /** used to tell the loop whether to keep trying to place the ship or not*/
        boolean cont = false;
        int counter = 0;

        /** used to designate whether the ship should go backwards or forwards*/
        boolean orientation = false;
        /** used to designate whether the ship should be placed horizontally or vertically*/
        boolean direction;

        /**integer representing the potential x value of the ship position*/
        int x;
        /**integer representing the potential y value of the ship position*/
        int y;


        /** Array of Integers used for storing the locations of the ships*/
        //int array[][] = new int[gridsize][gridsize];

        int length = lengthOfShip;

        while (!cont)
        {
            emptySquare = true;

            //Zufällige Generierung der Ausrichtung
            orientation = random.nextBoolean();
            direction = random.nextBoolean();

            //Sucht sich einen zufälligen Punkt im 'Koordinatensystem' des Rätsels
            x = random.nextInt(gridsize);
            y = random.nextInt(gridsize);

            //vertical
            if (orientation)
            {
                //placed to the right
                if (direction)
                {
                    //both points are one the board
                    if ((y + length) <= (gridsize-1))
                    {
                        for (int i = y; i < y + length; i++)
                        {
                            //Felder der Schiffe sind belegt
                            if (array[x][i] != 0)
                            {
                                emptySquare = false;
                            }
                            checkOccupied(x, i);
                        }

                        //ship can be placed here
                        if (emptySquare)
                        {
                            for (int i = y; i < y + length; i++)
                            {
                                //array[x][i] = length;
                                array[x][i] = 2;

                                surroundShipsWaterX(x, i);
                            }
                            return;
                        }else{
                            direction = false;
                            emptySquare = true;
                        }
                    }
                }
                //placed to the left
                if (!direction)
                {
                    if ((y - length) >= 0)
                    {
                        for (int i = y; i > y - length; i--)
                        {
                            //square is already occupied
                            if (array[x][i] != 0)
                            {
                                emptySquare = false;
                            }
                            checkOccupied(x, i);
                        }

                        //ship can be placed here
                        if (emptySquare)
                        {
                            for (int i = y; i > y - length; i--)
                            {
                                //array[x][i] = length;
                                array[x][i] = 2;

                                surroundShipsWaterX(x, i);
                            }
                            return;
                        }else{
                            orientation = false;
                            emptySquare = true;
                        }
                    }
                }
            }

            //horizontal
            if (!orientation)
            {
                //placed upward
                if (!direction)
                {
                    //both points are one the board
                    if ((x - length) >= 0)
                    {
                        for (int i = x; i > x - length; i--)
                        {
                            //square is already occupied
                            if (array[i][y] != 0)
                            {
                                emptySquare = false;
                            }
                            checkOccupiedY(y, i);
                        }

                        //ship can be placed here
                        if (emptySquare)
                        {
                            for (int i = x; i > x - length; i--)
                            {
                                //array[i][y] = length;
                                array[i][y] = 2;

                                surroundShipsWaterY(y, i);
                            }
                            return;
                        }
                        else{
                            direction = true;
                            emptySquare = true;
                        }
                    }
                }

                //placed downward
                if (direction)
                {
                    if ((x + length) <= (gridsize-1))
                    {
                        for (int i = x; i < x + length; i++)
                        {
                            //square is already occupied
                            if (array[i][y] != 0)
                            {
                                emptySquare = false;
                            }

                            checkOccupiedY(y, i);
                        }

                        //ship can be placed here
                        if (emptySquare)
                        {
                            for (int i = x; i < x + length; i++)
                            {
                                //array[i][y] = length;
                                array[i][y] = 2;
                                surroundShipsWaterY(y, i);
                            }
                            return;
                        }
                        else {  //Wenn nach 10.maligem 'Probieren das Schiffsteil nicht platziert werden konnte wird es verworfen. Die Funktion endet und es wird mit dem nächsten Schiffsteil weitergemacht
                            if (counter == 30){
                                cont = true;
                            }
                            counter++;
                            emptySquare = true;
                        }
                    }
                }
            }
        }
    }

    private void surroundShipsWaterY(int x, int i) {
        //Felder um die Schiffe herum werden zuu Wasser
        if (x+1 < gridsize){
            array[x+1][i] =1;
        }
        if (x-1 >= 0) {
            array[x - 1][i] = 1;
        }
        if (i+1 < gridsize) {
            array[x][i + 1] = 1;
        }
        if (i-1 >= 0) {
            array[x][i - 1] = 1;
        }
    }
    private void surroundShipsWaterX(int i, int y) {
        //Felder um die Schiffe herum werden zuu Wasser
        if (i+1 < gridsize){
            array[i+1][y] =1;
        }
        if (i-1 >= 0) {
            array[i - 1][y] = 1;
        }
        if (y+1 < gridsize) {
            array[i][y + 1] = 1;
        }
        if (y-1 >= 0) {
            array[i][y - 1] = 1;
        }
    }

    private void checkOccupied(int x, int i) {
        //Felder um die Schiffe herum sind belegt
        if (x+1 < gridsize) {
            if (array[x + 1][i] != 0) {
                emptySquare = false;
            }
        }
        if (x-1 >= 0) {
            if (array[x - 1][i] != 0) {
                emptySquare = false;
            }
        }
        if (i+1 < gridsize) {
            if (array[x][i + 1] != 0) {
                emptySquare = false;
            }
        }
        if (i-1 >= 0) {
            if (array[x][i - 1] != 0) {
                emptySquare = false;
            }
        }
    }
    private void checkOccupiedY(int y, int i) {
        //Felder um die Schiffe herum sind belegt
        if (i+1 < gridsize) {
            if (array[i + 1][y] != 0) {
                emptySquare = false;
            }
        }
        if (i-1 >= 0) {
            if (array[i - 1][y] != 0) {
                emptySquare = false;
            }
        }
        if (y+1 < gridsize) {
            if (array[i][y + 1] != 0) {
                emptySquare = false;
            }
        }
        if (y-1 >= 0) {
            if (array[i][y - 1] != 0) {
                emptySquare = false;
            }
        }
    }


    public void generateBoard(int size, String path){


        int shiptiles = 0;
        int random = 0;
        int schiffe = 0;
        //array = new int[size][size];
        /*
           if (size == 6) {
               shiptiles = 8;
           }
           Random r = new Random();
           for (int row = 0; row < array.length; row++) {
               for (int column = 0; column < array.length; column++) {
                   random = r.nextInt(4);
                   System.out.println("random: " + random);
                   if (random == 3) {
                       array[row][column] = 2;
                   } else array[row][column] = 1;
               }
           }
           */
        populateBoard(size, 3);
        populateBoard(size, 3);
        populateBoard(size, 2);
        populateBoard(size, 2);
        populateBoard(size, 1);
        populateBoard(size, 1);
        /*
           for (int row = 0; row < array.length; row++) {
               for (int column = 0; column < array.length; column++) {
                   random = r.nextInt(size); //Erstelle nummer zwischen 0 und size-1. damit können wir dann den bereich 1-size abdecken.
                   System.out.println("random: " + random);
                   if (random == 3) {
                       array[row][column] = 2;
                   } else array[row][column] = 1;
               }
           }
           */
        printArray();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (array[i][j] == 2) {
                    schiffe = (schiffe + array[i][j]);
                }
            }
            //verticalIndexTextViews[i].setText(String.valueOf(schiffe / 2));
            verticalIndex[i] = (schiffe / 2);
            System.out.println("vertical: " + verticalIndex[i]);
            schiffe = 0;
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (array[j][i] == 2) {
                    schiffe = (schiffe + array[j][i]);
                }
            }
            //horizontalIndexTextViews[i].setText(String.valueOf(schiffe / 2));
            horizontalIndex[i] = (schiffe / 2);
            schiffe = 0;
            System.out.println("horizontal: " + horizontalIndex[i]);
        }
        //defiened_grid_array = array;
        if (!path.equals("")) {
            //safeToFile(path);
        }
    }

    private void safeToFile(String path) {
        String gamemode = "";
        int idd = 0;
        String newPath = "";

            newPath = path + gamemode + idd + ".txt";
            File file = new File(newPath);

            try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
                for (int i : verticalIndex) {
                    br.write(i + ";");
                    br.flush();

                }
                br.newLine();
                br.flush();

                for (int i : horizontalIndex) {
                    br.write(i + ";");
                    br.flush();
                }
                //br.newLine();

            } catch (Exception e) {

            }

            System.out.println("Wrote to file: " + newPath);

    }

    public int[][] emptyBoard(int[][] arr) {

        return arr;
    }

    public int[][] fillBoardWater(int[][] arr) {

        return arr;
    }

    /*
    boolean arraysEqual = Arrays.deepEquals(current_grid_array, defiened_grid_array);
        arraysEqual = true;
     */

    public String checkBoard() {
        String str = " ";
        boolean horizontal = checkHorizontal();
        boolean vertical = checkVertical();
        System.out.println("checking: " + horizontal + vertical);

        if (horizontal && vertical) {
            str = "Correct!";
        } else {
            str = "Incorrect!";
        }
        return str;
    }


    private boolean checkVertical() {
        int schiffe = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (current_grid_array[i][j] == 2) {
                    schiffe = (schiffe + current_grid_array[i][j]);
                }
                System.out.println("checking: " + j + i);
            }
            if ((schiffe / 2) != (verticalIndex[i])){
                return false;
            }
            schiffe=0;

        }
        return true;
    }

    private boolean checkHorizontal() {
        int schiffe = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if(current_grid_array[j][i] == 2) {
                    schiffe = (schiffe + current_grid_array[j][i]);

                }
                System.out.println("checking: " + j + i);
            }
            if ((schiffe / 2) != (horizontalIndex[i])){
                return false;
            }
            schiffe=0;
        }
        return true;
    }

    public void setNumber(int row, int column, int num) {
        current_grid_array[row][column]=num;
    }

    public boolean checkArrayFull() {
        for (int i = 0; i < current_grid_array.length; i++) {
            for (int j = 0; j < current_grid_array.length; j++) {
                if (current_grid_array[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }


    public String printArray(){
        String str = "";
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < current_grid_array.length; i++) {
            for (int j = 0; j < current_grid_array[i].length; j++) {
                if (j == 0) {
                    temp.append("\n");
                }

                int currentNumber = current_grid_array[i][j];
                if (currentNumber == 0) {
                    temp.append("-");
                } else {
                    temp.append(currentNumber);
                }

                if (j != (current_grid_array[i].length-1)) {
                    temp.append(" ");
                }
            }
        }
        return temp.toString();
    }

    public int getNumber(int row, int column) {
        return current_grid_array[row][column];
    }


    public void refresh() {
        array = new int[][]{  {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
    }
}
