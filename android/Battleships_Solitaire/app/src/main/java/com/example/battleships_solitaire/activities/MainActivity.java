package com.example.battleships_solitaire.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;



import com.example.battleships_solitaire.R;
import com.google.android.gms.appinvite.AppInviteInvitation;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity{
    private static final int REQUEST_INVITE = 1;
    private Button playButton;GameActivity gameActivity;

    private static final String TAG = "MainActivity";

    // ...


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);

        playButton = findViewById(R.id.playButton);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);


                TextView pop = popupView.findViewById(R.id.pop);
                String str = "Schiffe unterschiedlicher Größe (wie im Bild oberhalb) sind in dem Raster versteckt. Um das Rätsel zu lösen, müssen alle Felder richtig ausgefüllt sein. \n" +
                        "Die Zahlen geben an, wie viele „Bootsquadrate“ sich in jeder Zeile oder Spalte befinden: Schiffe können sich nicht berühren.";
                pop.setText(str);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 380);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

    }

    public void playEndless(View view){
        Intent intent = new Intent(view.getContext(), GameActivity.class);
        intent.putExtra("gamemode", "endless");
        view.getContext().startActivity(intent);
    }
    public void playLevel(View view){
        Intent intent = new Intent(view.getContext(), LevelSelectionActivity.class);
        view.getContext().startActivity(intent);
    }

    public void share(View view){
        onInviteClicked();
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                //.setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                //.setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                //.setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }


    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        TextView pop = findViewById(R.id.pop);
        String str = "Schiffe unterschiedlicher Größe (oben auf dem Puzzle abgebildet) sind in dem Raster versteckt. Um das Rätsel zu lösen, füllen alle Felder richtig ausgefüllt sein. \n" +
                "Die Zahlen geben an, wie viele „Bootsquadrate“ sich in jeder Zeile oder Spalte befinden: Schiffe können sich nicht berühren.";
        pop.setText(str);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
