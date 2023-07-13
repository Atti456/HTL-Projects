package com.example.battleships_solitaire.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.battleships_solitaire.Model.LevelCollection;
import com.example.battleships_solitaire.Model.Model;
import com.example.battleships_solitaire.R;
import com.example.battleships_solitaire.adapter.LevelAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.logging.Level;

public class LevelSelectionActivity extends AppCompatActivity {
    private Button playButton;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Model model;
    LevelCollection levelCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        LevelCollection einfach1 = new LevelCollection("Einfach1", LevelCollection.Difficulty.einfach);
        LevelCollection mittel1 = new LevelCollection("Mittel1", LevelCollection.Difficulty.mittel);
        LevelCollection schwierig1 = new LevelCollection("Schwierig1", LevelCollection.Difficulty.schwierig);

        model = new Model();
        model.addToList(einfach1);
        model.addToList(mittel1);
        model.addToList(schwierig1);

        readGameProgression();


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // define an adapter
        mAdapter = new LevelAdapter();
        Context context = (Context) this;
        ((LevelAdapter) mAdapter).setContext(context);
        ((LevelAdapter) mAdapter).model = model;
        recyclerView.setAdapter(mAdapter);


        /*
        playButton = findViewById(R.id.playButton);
        TextView einfach = findViewById(R.id.textViewEinfach);
        TextView mittel = findViewById(R.id.textViewMittel);
        TextView schwierig = findViewById(R.id.textViewSchwierig);

        einfach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                intent.putExtra("gamemode", "einfach");
                view.getContext().startActivity(intent);
            }
        });
        mittel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                intent.putExtra("gamemode", "mittel");
                view.getContext().startActivity(intent);
            }
        });
        schwierig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                intent.putExtra("gamemode", "schwierig");
                view.getContext().startActivity(intent);
            }
        });
        */

    }

    public void deleteProgression(View view) {
        Toast.makeText(this, "Löschung aller Spielfortschritte . . . ", Toast.LENGTH_LONG).show();
        File file = new File(getExternalFilesDir(null), "GameProgression.txt");
        String line;
        String[] elems;

        try(PrintWriter br = new PrintWriter(new FileWriter(file))) {
            for (int i = 0; i < model.list.size(); i++) {
                br.write("0");
                br.write(";");
                br.flush();
            }
            System.out.println("write Game Progression");
            ((LevelAdapter) mAdapter).notifyDataSetChanged();
        }catch (Exception e){
            System.out.println("Could not write Game Progression");
        }
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
        Log.e("LevelSelectionActivity", "Error due to: ", e.getCause());
        e.printStackTrace();
    }
}
