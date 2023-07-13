/*
 * Copyright (c) 2020. Alexander Artelsmair, Markus Pechhacker
 */

package com.developer.MusterMerken.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.developer.MusterMerken.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

public class EasyLevelAdapter extends RecyclerView.Adapter<EasyLevelAdapter.ViewHolder> {

    private ArrayList<Integer> cardFront;
    private boolean flipped=false;


    public EasyLevelAdapter(ArrayList<Integer> cardFront) {
        this.cardFront = cardFront;
    }

    @Override
    public EasyLevelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        view.setMinimumWidth(parent.getMeasuredWidth() / 3);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!flipped) {
            holder.cardFr.setImageResource(cardFront.get(position));
            holder.easyFlipView.flipTheView();
        }else holder.easyFlipView.flipTheView();
    }

    @Override
    public int getItemCount() {
        return cardFront.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView cardFr;
        private EasyFlipView easyFlipView;


        public ViewHolder(View itemView) {
            super(itemView);
            cardFr = itemView.findViewById(R.id.cardfront);
            easyFlipView=itemView.findViewById(R.id.flippingcard);
        }
    }
}
