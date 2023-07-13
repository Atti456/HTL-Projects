package com.example.battleships_solitaire.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.battleships_solitaire.activities.GameActivity;
import com.example.battleships_solitaire.Model.LevelCollection;
import com.example.battleships_solitaire.Model.Model;
import com.example.battleships_solitaire.R;
//import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {

    public Model model;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDifficulty;
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;


        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtDifficulty = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            txtHeader = v.findViewById(R.id.DifficultyLine);
        }
    }


    public LevelAdapter() {

    }



    @Override
    public LevelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final LevelCollection levelCollection = model.list.get(position);
        String name = levelCollection.getName();
        String difficulty = "Difficulty: " + levelCollection.getDifficulty();

        holder.txtHeader.setText(name);
        holder.txtDifficulty.setText(difficulty);

        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("model", model);
                intent.putExtra("gamemode", levelCollection.getDifficulty().toString().toLowerCase());
                context.startActivity(intent);
            }
        });


        holder.txtFooter.setText(String.format("%s / %s", levelCollection.getProgression(), levelCollection.getMAXLEVELS()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return model.getList().size();
    }

}
