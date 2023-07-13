package com.example.battleships_solitaire.activities;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.battleships_solitaire.Model.Board;
import com.example.battleships_solitaire.Model.CellGroupFragment;
import com.example.battleships_solitaire.Model.Model;
import com.example.battleships_solitaire.OnFragmentInteractionListener;
import com.example.battleships_solitaire.R;
import com.example.battleships_solitaire.dialogs.GameDialog;
import com.example.battleships_solitaire.dialogs.LevelDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GameActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private ImageView clickedCell;
    private int clickedGroup;
    private int clickedCellId;
    private int number = 1;
    private boolean switchState;
    private Button checkBoardButton;
    private GameDialog gameDialog;
    private LevelDialog levelDialog;



    private Board board;



    private TextView[] verticalIndexTextViews;
    private TextView[] horizontalIndexTextViews;

    private ArrayList<ImageView> gridElements;
    private int[] cellGroupFragments;
    private CellGroupFragment[] cellGroups = new CellGroupFragment[4];
    private ImageView[][] grid_array = new ImageView[6][6];

    //private int model.list.get(1).getProgression() = 0;
    //private int model.list.get(0).getProgression() = 0;
    //private int model.list.get(2).getProgression() = 0;
    private String gamemode;
    private Model model;


    private static final String TAG = "GameActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

            gamemode = getIntent().getExtras().getString("gamemode");
            model = (Model) getIntent().getExtras().getSerializable("model");

            board = new Board();
            gameDialog = new GameDialog();
            levelDialog = new LevelDialog();

            checkBoardButton = findViewById(R.id.button);
            Switch sw = (Switch) findViewById(R.id.switch1);
            switchState = sw.isChecked();
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        switchState = isChecked;
                    } else {
                        switchState = isChecked;
                    }
                }
            });


            cellGroupFragments = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3,
                    R.id.cellGroupFragment4};
            for (int i = 1; i < 5; i++) {
                CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[i - 1]);
                thisCellGroupFragment.setGroupId(i);
                cellGroups[i - 1] = thisCellGroupFragment;
                //gridElements.addAll(thisCellGroupFragment.getImageViews());
            }


            if (gamemode.equals("endless")) {
                playEndless();
            } else {
                playLevel();
            }
        }catch (Exception e){
            errorMsg(e);
        }
    }


    private void playLevel() {
        //readGameProgression();
        System.out.println(model.list.get(0).getProgression() + "," + model.list.get(1).getProgression() + "," + model.list.get(2).getProgression());
        loadLevel();
    }


    private void loadLevel() {
        InputStream inputStream = null;
        int progress = 0;

        if (gamemode.equals("einfach")){
            inputStream = getResources().openRawResource(R.raw.einfach);
            progress = model.list.get(0).getProgression();
        }else if (gamemode.equals("mittel")){
            inputStream = getResources().openRawResource(R.raw.mittel);
            progress = model.list.get(1).getProgression();
        }else if (gamemode.equals("schwierig")){
            inputStream = getResources().openRawResource(R.raw.schwierig);
            progress = model.list.get(2).getProgression();
        }
        board.loadPresetArray(6,';',inputStream, progress);

        loadNumbers(board.getVerticalIndex(), board.getHorizontalIndex());
    }

    private void readGameProgression() {
        File file = new File(getExternalFilesDir(null), "GameProgression.txt");
        String line;
        String[] elems;

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            //line = br.readLine();
            line = br.readLine();
            elems = line.split(";");
            for (int i = 0; i < elems.length; i++) {
                model.list.get(i).setProgression(Integer.parseInt(elems[i]));
                System.out.println(model.list.get(i).getProgression());
            }

            System.out.println("read Game Progression");

        }catch (Exception e){
            errorMsg(e);
            System.out.println("Could not read Game Progression");
        }
    }

    private void errorMsg(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        Log.e(TAG, "Error due to: ", e.getCause());
        e.printStackTrace();
    }

    private void writeGameProgression() {
        File file = new File(getExternalFilesDir(null), "GameProgression.txt");
        String line;
        String[] elems;

        try(PrintWriter br = new PrintWriter(new FileWriter(file))) {
            for (int i = 0; i < model.list.size(); i++) {
                br.write(String.valueOf(model.list.get(i).getProgression()));
                br.write(";");
                br.flush();
            }
            System.out.println("write Game Progression");
        }catch (Exception e){
            errorMsg(e);
            System.out.println("Could not write Game Progression");
        }
    }

    private void playEndless() {
        Toast.makeText(this, "Generating Board", Toast.LENGTH_LONG).show();
        String str = "";
        //board.refresh();
        board.generateBoard(6,str);
        loadNumbers(board.getVerticalIndex(), board.getHorizontalIndex());
    }

    public void checkBoard(View view) {
        try {
            String result = board.checkBoard();
            gameDialog.setGameState(result);

            levelDialog.setGameState(result);
            levelDialog.setGamemode(gamemode);

            if (result.equals("Correct!")){
                switch (gamemode) {
                    case "einfach":
                        int pEasy = model.list.get(0).getProgression();
                        pEasy++;
                        model.list.get(0).setProgression(pEasy);
                        writeGameProgression();
                        levelDialog.show(getSupportFragmentManager(), result);
                        break;
                    case "mittel":
                        int pMedium = model.list.get(1).getProgression();
                        pMedium++;
                        model.list.get(1).setProgression(pMedium);
                        writeGameProgression();
                        levelDialog.show(getSupportFragmentManager(), result);
                        break;
                    case "schwierig":
                        int pHard = model.list.get(2).getProgression();
                        pHard++;
                        model.list.get(2).setProgression(pHard);
                        writeGameProgression();
                        levelDialog.show(getSupportFragmentManager(), result);
                        break;
                    case "endless":
                        gameDialog.show(getSupportFragmentManager(), result);
                        break;
                }
            }else {
                switch (gamemode) {
                    case "einfach":

                        levelDialog.show(getSupportFragmentManager(), result);
                        break;
                    case "mittel":

                        levelDialog.show(getSupportFragmentManager(), result);
                        break;
                    case "schwierig":

                        levelDialog.show(getSupportFragmentManager(), result);
                        break;
                    case "endless":
                        gameDialog.show(getSupportFragmentManager(), result);
                        break;
                }
            }


            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            errorMsg(e);
        }
    }


    public void loadNumbers(int[] verticalIndex, int[] horizontalIndex) {
        View verticalIndex1 = findViewById(R.id.cellGroupFragmentVerticalIndex);
        View verticalIndex2 = findViewById(R.id.cellGroupFragmentVerticalIndex2);
        View horizontalIndex1 = findViewById(R.id.cellGroupFragmentHorizontalIndex);
        View horizontalIndex2 = findViewById(R.id.cellGroupFragmentHorizontalIndex1);

        verticalIndexTextViews = new TextView[]{verticalIndex1.findViewById(R.id.textView1), verticalIndex1.findViewById(R.id.textView2), verticalIndex1.findViewById(R.id.textView3),
                verticalIndex2.findViewById(R.id.textView1), verticalIndex2.findViewById(R.id.textView2), verticalIndex2.findViewById(R.id.textView3)};
        for (int i = 0; i < verticalIndexTextViews.length; i++) {
            verticalIndexTextViews[i].setText(String.valueOf(verticalIndex[i]));
        }

        horizontalIndexTextViews = new TextView[]{horizontalIndex1.findViewById(R.id.textView1), horizontalIndex1.findViewById(R.id.textView2), horizontalIndex1.findViewById(R.id.textView3),
                horizontalIndex2.findViewById(R.id.textView1), horizontalIndex2.findViewById(R.id.textView2), horizontalIndex2.findViewById(R.id.textView3)};
        for (int i = 0; i < horizontalIndexTextViews.length; i++) {
            horizontalIndexTextViews[i].setText(String.valueOf(horizontalIndex[i]));
        }
    }


    @Override
    public void onFragmentInteraction(int groupId, int cellId, View view) {
        try {
            clickedCell = (ImageView) view;
            clickedGroup = groupId;
            clickedCellId = cellId;
            System.out.println("Clicked group " + groupId + ", cell " + cellId);
            Drawable water = getDrawable(R.drawable.table_border_cell_water);

            System.out.println(cellGroups[0].getImageViews()[0][0]);

            for (int i = 0; i < board.getCurrent_grid_array().length; i++) {
                for (int j = 0; j < board.getCurrent_grid_array().length; j++) {
                    if (i < 3 && j < 3) {
                        grid_array[i][j] = cellGroups[0].getImageViews()[i][j];
                    }
                    if (i < 3 && j > 2) {
                        grid_array[i][j] = cellGroups[1].getImageViews()[i][j - 3];
                    }
                    if (i > 2 && j <= 2) {
                        grid_array[i][j] = cellGroups[2].getImageViews()[i - 3][j];
                    }
                    if (i > 2 && j > 2) {
                        grid_array[i][j] = cellGroups[3].getImageViews()[i - 3][j - 3];
                    }
                }
            }


            int row = ((clickedGroup - 1) / 2) * 3 + (clickedCellId / 3);
            int column = ((clickedGroup - 1) % 2) * 3 + ((clickedCellId) % 3);

            if (switchState) {
                clickedCell.setImageResource(R.drawable.single_piece);
                board.setNumber(row, column, 2);

                //optional
                clickedCell.setBackground(water);
            } else {
                clickedCell.setImageResource(R.drawable.table_border_cell_water);
                board.setNumber(row, column, 1);
            }

            //System.out.println(board.printArray());

            if (board.checkArrayFull()) {
                checkBoardButton.setVisibility(View.VISIBLE);
            } else {
                checkBoardButton.setVisibility(View.INVISIBLE);
            }

            checkPieceOrientation();
            showError2(clickedGroup, clickedCell);
        }catch (Exception e){
            errorMsg(e);
        }
    }

    public void checkPieceOrientation() {
        for (int i = 0; i < board.getCurrent_grid_array().length; i++) { //Jede Zeile/Reihe
            for (int j = 0; j < board.getCurrent_grid_array().length; j++) { //Jede Spalte
                if ((board.getCurrent_grid_array()[i][j] == 2) && j <= 4) { //Wenn Schifsteil an XY UND Y != letzte Spalte -> XY = links UND XY+1 = rechts
                    if (board.getCurrent_grid_array()[i][j+1] == 2){
                        grid_array[i][j].setImageResource(R.drawable.left_piece_v2);
                        grid_array[i][j+1].setImageResource(R.drawable.right_piece);
                        if (j > 0){
                            if (board.getCurrent_grid_array()[i][j-1] == 2){    //Wenn links Schiffsteil -> XY zu mitte und linkes zu links
                                grid_array[i][j].setImageResource(R.drawable.center_piece);
                                //grid_array[i][j-1].setImageResource(R.drawable.left_piece_v2);
                            }
                        }
                        if (j < 4){
                            if (board.getCurrent_grid_array()[i][j+2] == 2){
                                grid_array[i][j+2].setImageResource(R.drawable.right_piece);
                            }
                        }
                    }
                }
            }
        }
        for (int j = 0; j < board.getCurrent_grid_array().length; j++) {
            for (int i = 0; i < board.getCurrent_grid_array().length; i++) {
                if ((board.getCurrent_grid_array()[i][j] == 2) && i <= 4) {
                    if (board.getCurrent_grid_array()[i+1][j] == 2){
                        grid_array[i][j].setImageResource(R.drawable.top_piece_v2);
                        grid_array[i+1][j].setImageResource(R.drawable.bottom_piece_v2);
                        if (i > 0) {
                            if (board.getCurrent_grid_array()[i - 1][j] == 2) {
                                grid_array[i][j].setImageResource(R.drawable.center_piece);
                                //grid_array[i-1][j].setImageResource(R.drawable.top_piece_v2);
                            }
                        }
                        if (i < 4) {
                            if (board.getCurrent_grid_array()[i + 2][j] == 2) {
                                grid_array[i + 2][j].setImageResource(R.drawable.bottom_piece_v2);
                            }
                        }
                    }
                }
            }
        }
    }

    private void showError2(int clickedGroup, ImageView clickedCell) {
        int row = ((clickedGroup-1)/2)*3 + (clickedCellId/3);
        int column = ((clickedGroup-1)%2)*3 + ((clickedCellId)%3);
        int schiffe = 0;
        int preset_ships = 0;



        for (int j = 0; j < 6; j++) {
            if (board.getNumber(row, j) == 2) {
                schiffe++;
            }
            System.out.println("checking: row:" + row + "collumn: " + j);
        }
        preset_ships = Integer.parseInt(verticalIndexTextViews[row].getText().toString());
        System.out.println("Defined Ships: " + preset_ships + "at row: " + row);

        if ((schiffe) > (preset_ships)){

            verticalIndexTextViews[row].setBackground(getDrawable(R.drawable.table_border_cell_wrong));
        }else{
            verticalIndexTextViews[row].setBackground(getDrawable(R.drawable.table_border_cell_white));

        }
        schiffe=0;


        for (int j = 0; j < 6; j++) {
            if (board.getNumber(j, column) == 2) {
                schiffe++;
            }
            System.out.println("checking: row:" + j + "collumn: " + column);
        }
        preset_ships = Integer.parseInt(horizontalIndexTextViews[column].getText().toString());
        System.out.println("Defined Ships: " + preset_ships + "at row: " + column);

        if ((schiffe) > (preset_ships)){
            horizontalIndexTextViews[column].setBackground(getDrawable(R.drawable.table_border_cell_wrong));
        }else{
            horizontalIndexTextViews[column].setBackground(getDrawable(R.drawable.table_border_cell_white));
        }
        schiffe=0;
        preset_ships = 0;

    }


}
