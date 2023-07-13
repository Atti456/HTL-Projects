package com.example.battleships_solitaire.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.battleships_solitaire.activities.GameActivity;
import com.example.battleships_solitaire.activities.MainActivity;

public class LevelDialog extends DialogFragment {
    private String gameState = "";
    private String gamemode = "";

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

        if (gameState.equals("Correct!")){
            builder.setMessage(gameState)
                    .setPositiveButton("Play next Level", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent (getContext(), GameActivity.class);
                            intent.putExtra("gamemode", gamemode);
                            getContext().startActivity(intent);
                        }
                    })
                    .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent (getContext(), MainActivity.class);
                            getContext().startActivity(intent);
                        }
                    });
        }else {
            builder.setMessage(gameState)
                    .setPositiveButton("Replay Level", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getContext(), GameActivity.class);
                            intent.putExtra("gamemode", gamemode);
                            getContext().startActivity(intent);
                        }
                    })
                    .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            getContext().startActivity(intent);
                        }
                    });
        }

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }
}
