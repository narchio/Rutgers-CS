package com.example.android56;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import Chess.Cell;
import Chess.Logic;
import Chess.Serialize;
import Pieces.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Chess.Color;
import Chess.Type;

public class PlayActivity extends AppCompatActivity {

    private static List<String> moves;
    private static List<String> executedMoves;
    private ChessBoard cb;
    private Button undoButton;
    private Button aiButton;
    private Button drawButton;
    private Button resignButton;
    private TextView textView;


    public static List<String> getMoves() {
        return moves;
    }
    public static List<String> getExecutedMoves() {return executedMoves; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        moves = new ArrayList<String>();
        executedMoves = new ArrayList<String>();
        cb = (ChessBoard) findViewById(R.id.chessBoard);
        undoButton = (Button) findViewById(R.id.undoButton);
        aiButton = (Button) findViewById(R.id.aiButton);
        drawButton = (Button) findViewById(R.id.drawButton);
        resignButton = (Button) findViewById(R.id.resignButton);
        textView = (TextView) findViewById(R.id.playerMoveTextView);

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.getBooleanVar("gameOver")){
                    return;
                }
                if(executedMoves.size() > 0){
                    //get the last move
                    String lastMove = executedMoves.get(executedMoves.size()-1);
                    int fromX = Character.getNumericValue(lastMove.charAt(0));
                    int fromY = Character.getNumericValue(lastMove.charAt(1));
                    int toX = Character.getNumericValue(lastMove.charAt(2));
                    int toY = Character.getNumericValue(lastMove.charAt(3));
                    //undo the last move
                    if(ChessBoard.getBoard()[fromX][fromY].getPiece() == null && ChessBoard.getBoard()[toX][toY].getPiece() != null) {
                        ChessBoard.getBoard()[fromX][fromY].setPiece(ChessBoard.getBoard()[toX][toY].getPiece());
                        ChessBoard.getBoard()[toX][toY].setPiece(null);
                        if (cb.getBooleanVar("white")) {
                            cb.setBooleanVar("white", false);
                            cb.setBooleanVar("black", true);
                            cb.getTextView().setText(R.string.blackMove);
                        } else {
                            cb.setBooleanVar("white", true);
                            cb.setBooleanVar("black", false);
                            cb.getTextView().setText(R.string.whiteMove);
                        }
                        cb.invalidate();
                    }
                    else{
                        cb.invalidMove();
                    }
                }
                else{
                    cb.invalidMove();
                }
            }
        });

        aiButton.setOnClickListener(new View.OnClickListener() {
            Random random = new Random();
            @Override
            public void onClick(View v) {
                if(cb.getBooleanVar("gameOver")){
                    return;
                }
                if(cb.getBooleanVar("white")){
                    // get list of cells that have a white piece
                    List<Cell> whitePieces = cb.getPieces(Color.WHITE);
                    //generate a random cell
                    Cell randomCell = null;
                    while (true) {
                        randomCell = whitePieces.get(random.nextInt(whitePieces.size()));
                        randomCell.getPiece().updateLegalMoves(ChessBoard.getBoard(), randomCell.getX(), randomCell.getY());
                        if (randomCell.getPiece().getLegalMoves().size() > 0) {
                            break;
                        }
                    }
                    //generate a random move
                    Cell randomMoveCell = randomCell.getPiece().getLegalMoves().get(random.nextInt(randomCell.getPiece().getLegalMoves().size()));
                    String fromCell = ""+randomCell.getX()+randomCell.getY();
                    String toCell = ""+randomMoveCell.getX()+randomMoveCell.getY();
                    moves.add(fromCell);
                    moves.add(toCell);
                    cb.handleMoves();
                }
                else{
                    // get list of cells that have a white piece
                    List<Cell> blackPieces = cb.getPieces(Color.BLACK);
                    //generate a random cell
                    Cell randomCell = null;
                    while(true) {
                        randomCell = blackPieces.get(random.nextInt(blackPieces.size()));
                        randomCell.getPiece().updateLegalMoves(ChessBoard.getBoard(), randomCell.getX(), randomCell.getY());
                        if(randomCell.getPiece().getLegalMoves().size() > 0) {
                            break;
                        }
                    }
                    //generate a random move
                    Cell randomMoveCell = randomCell.getPiece().getLegalMoves().get(random.nextInt(randomCell.getPiece().getLegalMoves().size()));
                    String fromCell = ""+randomCell.getX()+randomCell.getY();
                    String toCell = ""+randomMoveCell.getX()+randomMoveCell.getY();
                    moves.add(fromCell);
                    moves.add(toCell);
                    cb.handleMoves();
                }
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.getBooleanVar("gameOver")){
                    return;
                }
                if(cb.getBooleanVar("white")){
                    cb.setBooleanVar("white", false);
                    cb.setBooleanVar("black", true);
                    cb.getTextView().setText(R.string.blackMove);
                    AlertDialog.Builder builder = new AlertDialog.Builder(cb.getContext());
                    builder.setTitle("Draw Offer");
                    builder.setMessage("White is offering to draw. Do you accept black?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            cb.setBooleanVar("gameOver", true);
                            cb.getTextView().setText(null);
                            cb.draw();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog drawOffer = builder.create();
                    drawOffer.show();
                }
                else{
                    cb.setBooleanVar("white", true);
                    cb.setBooleanVar("black", false);
                    cb.getTextView().setText(R.string.whiteMove);
                    AlertDialog.Builder builder = new AlertDialog.Builder(cb.getContext());
                    builder.setTitle("Draw Offer");
                    builder.setMessage("Black is offering to draw. Do you accept white?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            cb.setBooleanVar("gameOver", true);
                            cb.getTextView().setText(null);
                            cb.draw();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog drawOffer = builder.create();
                    drawOffer.show();
                }
            }
        });

        resignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.getBooleanVar("gameOver")){
                    return;
                }
                //whites turn
                if(cb.getBooleanVar("white") == true){
                    //set the game over
                    cb.setBooleanVar("gameOver", true);
                    cb.getTextView().setText(null);
                    cb.gameOver(false);
                }
                //blacks turn
                else{
                    //set the game over
                    cb.setBooleanVar("gameOver", true);
                    cb.getTextView().setText(null);
                    cb.gameOver(true);
                }
            }
        });

        cb.setTextView(textView);
        cb.chessDriver();
    }

    @Override
    public void onPause() {
        super.onPause();
        Serialize.write(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Serialize.write(this);
    }
}
