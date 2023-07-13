/*
 * Copyright (c) 2020. Alexander Artelsmair, Markus Pechhacker
 */

package com.developer.MusterMerken;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Leaderboard extends Fragment {

    public Leaderboard() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_leaderboard, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.PREF_NAME,0);
        ((TextView) rootView.findViewById(R.id.easylead)).append(""+Constants.winStreak);

        return rootView;
    }

}
