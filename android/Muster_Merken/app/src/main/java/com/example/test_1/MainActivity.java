package com.example.test_1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int size = 5;
        double difficulty = 0;
        GridView view = null,view3,view4,view5;


        List<GridItem> image_details = generateMuster(size, difficulty);
        List<GridItem> merke = image_details;



        view3 = (GridView) findViewById(R.id.gridView);
        view4 = (GridView) findViewById(R.id.gridView4);
        view5 = (GridView) findViewById(R.id.gridView5);


        view3.setVisibility(View.INVISIBLE);
        view4.setVisibility(View.INVISIBLE);
        view5.setVisibility(View.INVISIBLE);

        if (size == 3) {
            view3.setVisibility(View.VISIBLE);
            view = view3;
        }else if (size == 4) {
            view4.setVisibility(View.VISIBLE);
            view = view4;
        }else if (size == 5){
            view5.setVisibility(View.VISIBLE);
            view = view5;
        }

        final GridView gridView = view;


        //gridView.setAdapter(new CustomGridAdapter(this, merke));





        /*
        final int sizee = gridView.getChildCount();
        for(int i = 0; i < sizee; i++) {
            ViewGroup gridChild = (ViewGroup) gridView.getChildAt(i);

            if (arr[i]) {
                gridChild.setBackgroundColor(Color.YELLOW);
            }
        }
        try {
            wait(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/


        gridView.setAdapter(new CustomGridAdapter(this, image_details));





        // When the user clicks on the GridItem
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                GridItem GridItem = (GridItem) o;


                v.setBackgroundColor(Color.YELLOW);


            }
        });
    }

    private List<GridItem> generateMuster(double size, double difficulty) {

        List<GridItem> list = new ArrayList<GridItem>();
        Random r;
        double d;

        for (int i = 0; i < (size*size); i++) {
            GridItem gi = generateGridItme(i);


            list.add(gi);
        }

        return list;
    }

    private GridItem generateGridItme(int i) {
        return new GridItem(" ", " ", 5);
    }



}
