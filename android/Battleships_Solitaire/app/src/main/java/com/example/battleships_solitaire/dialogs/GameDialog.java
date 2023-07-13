package com.example.battleships_solitaire.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.battleships_solitaire.activities.GameActivity;
import com.example.battleships_solitaire.activities.MainActivity;

public class GameDialog extends DialogFragment {
    private String gameState = "";

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(gameState)
                .setPositiveButton("Erneut Spielen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent (getContext(), GameActivity.class);
                        intent.putExtra("gamemode", "endless");
                        getContext().startActivity(intent);
                    }
                })
                .setNegativeButton("Hauptmenü", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent (getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
