package com.example.battleships_solitaire.Model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.battleships_solitaire.OnFragmentInteractionListener;
import com.example.battleships_solitaire.R;

public class CellGroupFragment extends Fragment {
    private int groupId;
    private View view;
    private int[][] textViews;
    private OnFragmentInteractionListener fragmentListener;
    private ImageView[][] imageViews;

    public CellGroupFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int[][] getTextViews() {
        return textViews;
    }

    public ImageView[][] getImageViews() {
        return imageViews;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageViews = new ImageView[3][3];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cell_group_old, container, false);


        textViews = new int[][]{
                {R.id.textView1, R.id.textView2, R.id.textView3},
                {R.id.textView4, R.id.textView5, R.id.textView6},
                {R.id.textView7, R.id.textView8, R.id.textView9}
        };
        for (int k = 0; k<3; k++) {
            for (int i = 0; i <3; i++) {
                ImageView textView = view.findViewById(textViews[k][i]);
                imageViews[k][i] = textView;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragmentListener.onFragmentInteraction(groupId, Integer.parseInt(view.getTag().toString()), view);
                    }
                });
            }

        }
        return view;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (OnFragmentInteractionListener) context;     //Context must implement FragmentListener
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
