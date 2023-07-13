package com.example.battleships_solitaire.Model;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.battleships_solitaire.R;

public class CellGroupFragmentHorizontalIndex extends Fragment {
    private int groupId;
    private View view;

    public CellGroupFragmentHorizontalIndex() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cell_group_horizontal_index, container, false);


        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3};

        return view;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
