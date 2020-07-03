package com.example.android56;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Chess.ChessGame;

public class PlaythroughActivity extends AppCompatActivity {

    private ChessGame game;
    private Button nextMove;
    private Button previousMove;
    private ChessBoard cb;
    private int index;

    public void displayGameResult(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cb.getContext());
        builder.setTitle("Game Result");
        switch(result) {
            case "white":
                builder.setMessage("White wins!");
                break;
            case "black":
                builder.setMessage("Black wins!");
                break;
            case "draw":
                builder.setMessage("There is no winner. Draw!");
                break;
        }
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog resultAlert = builder.create();
        resultAlert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playthrough);

        this.game = (ChessGame) getIntent().getSerializableExtra("SelectedGame");
        this.nextMove = (Button) findViewById(R.id.nextMove);
        this.previousMove = (Button) findViewById(R.id.previousMove);
        this.cb = (ChessBoard) findViewById(R.id.playthroughChessBoard);
        this.index = 0;
        cb.initBoard();

        nextMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index < game.getMoves().size()) {
                    int startX = Character.getNumericValue(game.getMoves().get(index).charAt(0));
                    int startY = Character.getNumericValue(game.getMoves().get(index).charAt(1));
                    int endX = Character.getNumericValue(game.getMoves().get(index).charAt(2));
                    int endY = Character.getNumericValue(game.getMoves().get(index).charAt(3));

                    ChessBoard.getBoard()[endX][endY].setPiece(ChessBoard.getBoard()[startX][startY].getPiece());
                    ChessBoard.getBoard()[startX][startY].setPiece(null);

                    index++;

                    cb.invalidate();
                }
                else{
                    displayGameResult(game.getWinner());
                }
            }
        });

        previousMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index-1 >= 0) {
                    index--;
                    int startX = Character.getNumericValue(game.getMoves().get(index).charAt(0));
                    int startY = Character.getNumericValue(game.getMoves().get(index).charAt(1));
                    int endX = Character.getNumericValue(game.getMoves().get(index).charAt(2));
                    int endY = Character.getNumericValue(game.getMoves().get(index).charAt(3));

                    ChessBoard.getBoard()[startX][startY].setPiece(ChessBoard.getBoard()[endX][endY].getPiece());
                    ChessBoard.getBoard()[endX][endY].setPiece(null);

                    cb.invalidate();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(cb.getContext());
                    builder.setTitle("Beginning of Game");
                    builder.setMessage("There are no previous moves. You are at the beginning of the game. Please click next move!");
                    builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog invalidPreviousMove = builder.create();
                    invalidPreviousMove.show();
                }
            }
        });
    }
}
