/*
 * Copyright (c) 2020. Alexander Artelsmair, Markus Pechhacker
 */

package com.developer.MusterMerken;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.MusterMerken.Adapters.EasyLevelAdapter;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class EasyLevel extends Fragment {

    private RecyclerView EasyLevelRecyclerView;
    public ArrayList<Integer> cards;
    public int CARDS[] = {
            R.drawable.card1,
            R.drawable.card2,
            R.drawable.card1,
            R.drawable.card2,
            R.drawable.card1,
            R.drawable.card2,
            R.drawable.card1,
            R.drawable.card2,
            R.drawable.card1,
            R.drawable.card2,
            R.drawable.card1,
            R.drawable.card2

            //TO-Do: Replay-Button
    };
    EasyFlipView flippedCard;
    public long RemainingTime;
    public boolean isPaused, isCancelled;
    Bundle b;
    private SharedPreferences pref;
    int count;
    List<Integer> falseArray = new LinkedList();
    List<Integer> correctArray = new LinkedList();
    EasyLevelAdapter ela;



    public EasyLevel() {
        // Required empty public constructor
    }

    public void generateMuster(int cards[], int n) {
        Random random = new Random();
        int r = 1;
        int f =0;
        int t= 0;
        for (int i = 0; i < n; i++) {

            r = random.nextInt(n - i);

            int temp = cards[r];
            cards[r] = cards[i];
            cards[i] = temp;

            if (cards[i] == R.drawable.card2 && f<=6) {
                falseArray.add(i);
                f++;

            }
            if (cards[i] == R.drawable.card1 && t<=6) {
                correctArray.add(i);
                t++;

            }
        }

    }

    public void fragmentTransaction(Bundle b) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        final Result r = new Result();
        r.setArguments(b);
        transaction.replace(R.id.layoutFragment, r);
        transaction.commit();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_easy_level, container, false);

        EasyLevelRecyclerView = rootView.findViewById(R.id.easylevelview);
        b = new Bundle();
        b.putInt("level", Constants.LEVEL_EASY);
        pref = getActivity().getSharedPreferences(Constants.PREF_NAME, 0);

        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        EasyLevelRecyclerView.setLayoutManager(lm);

        cards = new ArrayList<>();


        generateMuster(CARDS, Constants.EASY_NO_OF_CARDS);   // double generateMuster
        for (int card : CARDS) {
            cards.add(card);
        }

        ela = new EasyLevelAdapter(cards);
        EasyLevelRecyclerView.setAdapter(ela);

        isPaused = false;
        isCancelled = false;

        final CountDownTimer countDownTimer = new CountDownTimer(Constants.playtime, Constants.TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isPaused || isCancelled) {
                    cancel();
                } else {
                    ((TextView) rootView.findViewById(R.id.easylevelcounter)).setText("Time : " + millisUntilFinished / Constants.TIMER_INTERVAL);
                    RemainingTime = millisUntilFinished;
                    if (count == Constants.EASY_NO_OF_CARDS) {

                        cancel();
                        this.onFinish();
                    }
                }
            }

            @Override
            public void onFinish() {

                //TODO: ela.onBindViewHolder();    //flip the cards back when finished
            }
        };


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        //Quithandler
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    isPaused = true;
                    AlertDialog.Builder pause = new AlertDialog.Builder(getContext());
                    pause.setTitle("Game paused");
                    pause.setMessage("Do you want to quit ?");
                    pause.setCancelable(false);
                    pause.setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isPaused = false;
                            new CountDownTimer(RemainingTime, Constants.TIMER_INTERVAL) {
                                int time;

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    if (isPaused || isCancelled) {
                                        cancel();
                                    } else {
                                        ((TextView) rootView.findViewById(R.id.easylevelcounter)).setText("Time : " + millisUntilFinished / Constants.TIMER_INTERVAL);
                                        RemainingTime = millisUntilFinished;
                                        if (count == Constants.EASY_NO_OF_CARDS) {
                                            b.putString("Data", "win");
                                            time = (int) ((Constants.playtime - millisUntilFinished) / Constants.TIMER_INTERVAL);
                                            b.putInt("Time", time);
                                            cancel();
                                            this.onFinish();
                                        }
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    if (count < Constants.EASY_NO_OF_CARDS) {
                                        b.putString("Data", "lost");
                                        b.putInt("Time", (int) (Constants.playtime / Constants.TIMER_INTERVAL));
                                    }
                                    fragmentTransaction(b);
                                }
                            }.start();
                        }
                    });
                    pause.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isCancelled = true;
                            getFragmentManager().popBackStack();
                        }
                    });
                    pause.show();
                    return true;
                }
                return false;
            }
        });


        countDownTimer.start();


        EasyLevelRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {

                if (RemainingTime > 0)
                    countDownTimer.cancel();

                final View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {

                    final int position = rv.getChildAdapterPosition(child);


                    //Check if Card is a member of the pattern. If not, the game is lost
                    for (int i = 0; i < falseArray.size(); i++) {
                        if (position == falseArray.get(i)) {
                            b.putString("Data", "lost");
                            b.putInt("Time", (int) (Constants.playtime / Constants.TIMER_INTERVAL));
                            fragmentTransaction(b);
                            Constants.playtime = 5000;
                        }


                    }
                    for (int i = 0; i < correctArray.size(); i++) {     //remove card if correct.
                        if (position == correctArray.get(i)) {
                            correctArray.remove(i);
                        }
                    }
                }

                checkForWin();

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        return rootView;
    }

    private void checkForWin() {
        if (correctArray.isEmpty()) {
            b.putString("Data", "win");
            b.putInt("Time", (int) Constants.playtime);
            fragmentTransaction(b);
            Constants.playtime -= 250;    //reduce the time the pattern is shown everytime the player wins by 0.25 seconds
            Constants.winStreak++;

            if (Constants.playtime < 1000) {
                //TODO: make level more complicated
            }
        }

    }


}


