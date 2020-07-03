package com.example.android56;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Chess.*;
import Pieces.*;


public class ChessBoard extends View {

    private TextView textView;
    private Paint paint;
    private int squareDimension = (int) (Resources.getSystem().getDisplayMetrics().widthPixels / 8.0);
    private static Cell[][] board;
    private static List<String> moves = new ArrayList<String>();

    // booleans for the game logic
    private boolean white;
    private boolean black;
    private boolean whiteCheck;
    private boolean blackCheck;
    private boolean gameOver;

    public ChessBoard(Context context) {
        this(context, null);
    }

    public ChessBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChessBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        paint.setStrokeWidth(4);
        board = initBoard();
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
    public void invalidMove() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Invalid Move");
        builder.setMessage("The action you have attempted is invalid. Please try again!");
        builder.setNegativeButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (white) {
                    textView.setText("White Move");
                } else {
                    textView.setText("Black Move");
                }
                dialog.dismiss();
            }
        });
        AlertDialog invalidMove = builder.create();
        invalidMove.show();
    }
    public void gameOver(boolean whiteWins) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Congratulations");
        if(whiteWins == true) {
            builder.setMessage("Game over. White wins!");
        }
        else{
            builder.setMessage("Game over. Black wins!");
        }
        builder.setNegativeButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(whiteWins){
                    storeGameOffer("white");
                }
                else{
                    storeGameOffer("black");
                }

            }
        });
        AlertDialog gameOver = builder.create();
        gameOver.show();

    }
    public void check(boolean whiteCheck) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Check");
        if(whiteCheck == true) {
            builder.setMessage("White is currently in check!");
        }
        else{
            builder.setMessage("Black is currently in check!");
        }
        builder.setNegativeButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog check = builder.create();
        check.show();
    }
    public void draw() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Draw");
        builder.setMessage("Game over. There is no winner. Draw!");
        builder.setNegativeButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                storeGameOffer("draw");
            }
        });
        AlertDialog draw = builder.create();
        draw.show();
    }
    public void storeGameOffer(String winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        EditText gameTitleInput = new EditText(getContext());
        builder.setTitle("Playback");
        builder.setView(gameTitleInput);
        builder.setMessage("Would you like to store the game for future playback? If so, please provide a game title!");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gameTitle = gameTitleInput.getText().toString().trim();
                ChessGame game = new ChessGame(gameTitle, winner);
                MainActivity.getSavedGames().add(game);
                Serialize.write(getContext());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(intent);
            }
        });
        AlertDialog storeGame = builder.create();
        storeGame.show();
    }
    public void handleMoves() {
        if(this.gameOver == true){
            return;
        }
        String input = "";
        boolean isWhite = false;
        if(PlayActivity.getMoves().size() >= 2 && PlayActivity.getMoves().size() % 2 == 0) {
            String from = PlayActivity.getMoves().get(PlayActivity.getMoves().size() - 2);
            String to = PlayActivity.getMoves().get(PlayActivity.getMoves().size() - 1);

            int startX = Character.getNumericValue(from.charAt(0));
            int startY = Character.getNumericValue(from.charAt(1));
            int endX = Character.getNumericValue(to.charAt(0));
            int endY = Character.getNumericValue(to.charAt(1));

            // generate all legal moves for the current move that has been made
            boolean validMove = false;
            if(board[startX][startY].getPiece() != null) {
                ArrayList<Cell> legalMoves = board[startX][startY].getPiece().generateLegalMoves(board, startX, startY);
                for (Cell c : legalMoves) {
                    // if valid move, add the full move
                    if (c.getX() == endX && c.getY() == endY) {
                        validMove = true;
                        input = "" + startX + startY + endX + endY;
                        PlayActivity.getExecutedMoves().add(input);
                        // check which color the start coordinates are on
                        if (board[startX][startY].getPiece().getColor() == Color.WHITE) {
                            isWhite = true;
                        } else {
                            isWhite = false;
                        }
                        break;
                    }
                }
            }
            // After the loop, valid move is still false then it is invalid
            if (validMove == false) {
                invalidMove();
                return;
            }
            // game logic here
            if (gameOver == false) {
                if (white && isWhite) {
                    System.out.println();
                    System.out.print("White's move: ");
                    //textView.setText(R.string.whiteMove);
                    System.out.println();

                    //check if white is resigning
                    if (Logic.isResignation(input)) {
                        System.out.println();
                        System.out.println("Black wins");
                        System.out.println();
                        gameOver = true;
                        gameOver(false);
                        return;
                    }
                    //check if white is accepting a draw offer
                    else if (Logic.isDrawAcceptance(input)) {
                        if (moves.size() > 0) {
                            if (Logic.isDrawOffer(moves.get(moves.size() - 1))) {
                                System.out.println();
                                System.out.println("Draw");
                                System.out.println();
                                gameOver = true;
                                return;
                            }
                        }
                    } else {
                        if (whiteCheck) {
                            boolean hasMoved = false;
                            ArrayList<Cell> whiteMoves = Logic.generateAllLegalMoves(board, Color.WHITE);
                            //check to see if inputted move is in the list of possible moves
                            for (Cell move : whiteMoves) {
                                if (move.getX() == endX && move.getY() == endY) {
                                    //copy the board
                                    Cell[][] boardCopy = copyBoard(board);

                                    if (boardCopy[startX][startY].getPiece().move(boardCopy, input)) {
                                        if (Logic.isChecked(boardCopy, Logic.getWhiteKing(boardCopy).getX(), Logic.getWhiteKing(boardCopy).getY())) {
                                            System.out.println();
                                            System.out.println("Invalid move, try again");
                                            invalidMove();
                                            System.out.println();
                                        } else {
                                            //Chess.printBoard(board);
                                            board[startX][startY].getPiece().move(board, input);
                                            whiteCheck = false;
                                            white = false;
                                            black = true;
                                            hasMoved = true;
                                            break;
                                        }

                                    } else {
                                        System.out.println();
                                        System.out.println("Invalid move, try again");
                                        invalidMove();
                                        System.out.println();
                                    }
                                }
                            }
                            if (!hasMoved) {
                                System.out.println();
                                System.out.println("Invalid move, try again");
                                invalidMove();
                                System.out.println();
                            }

                        } else if (board[startX][startY].getPiece().move(board, input)) {
                            //Chess.printBoard(board);
                            moves.add(input);
                            white = false;
                            black = true;
                            invalidate();
                            if (Logic.isChecked(board, Logic.getBlackKing(board).getX(), Logic.getBlackKing(board).getY())) {
                                if (Logic.checkMate(board, Logic.getBlackKing(board).getX(), Logic.getBlackKing(board).getY())) {
                                    System.out.println();
                                    System.out.println("White Wins");
                                    System.out.println();
                                    gameOver = true;
                                    gameOver(true);
                                    return;
                                } else {
                                    blackCheck = true;
                                    System.out.println();
                                    System.out.println("Check");
                                    check(false);
                                    System.out.println();
                                }
                            }
                        }
                    }
                    textView.setText(R.string.blackMove);
                } // If it is black's move..
                else if (black && !isWhite) {
                    System.out.println();
                    System.out.print("Black's move: ");
//                    textView.setText(R.string.blackMove);
                    System.out.println();

                    //check if white is resigning
                    if (Logic.isResignation(input)) {
                        System.out.println();
                        System.out.println("White wins");
                        System.out.println();
                        gameOver = true;
                        gameOver(true);
                        return;
                    }
                    //check if white is accepting a draw offer
                    else if (Logic.isDrawAcceptance(input)) {
                        if (moves.size() > 0) {
                            if (Logic.isDrawOffer(moves.get(moves.size() - 1))) {
                                System.out.println();
                                System.out.println("Draw");
                                System.out.println();
                                gameOver = true;
                                return;
                            }
                        }
                    } else {
                        if (blackCheck) {
                            boolean hasMoved = false;
                            ArrayList<Cell> blackMoves = Logic.generateAllLegalMoves(board, Color.BLACK);
                            //check to see if inputted move is in the list of possible moves
                            for (Cell move : blackMoves) {
                                if (move.getX() == endX && move.getY() == endY) {
                                    //copy the board
                                    Cell[][] boardCopy = copyBoard(board);

                                    if (boardCopy[startX][startY].getPiece().move(boardCopy, input)) {
                                        if (Logic.isChecked(boardCopy, Logic.getBlackKing(boardCopy).getX(), Logic.getBlackKing(boardCopy).getY())) {
                                            System.out.println();
                                            System.out.println("Invalid move, try again");
                                            invalidMove();
                                            System.out.println();
                                        } else {
                                            //Chess.printBoard(board);
                                            board[startX][startY].getPiece().move(board, input);
                                            blackCheck = false;
                                            white = true;
                                            black = false;
                                            hasMoved = true;
                                            break;
                                        }
                                    } else {
                                        System.out.println();
                                        System.out.println("Invalid move, try again");
                                        invalidMove();
                                        System.out.println();
                                    }
                                }
                            }
                            if (!hasMoved) {
                                System.out.println();
                                System.out.println("Invalid move, try again");
                                invalidMove();
                                System.out.println();
                            }
                        } else if (board[startX][startY].getPiece().move(board, input)) {
                            //Chess.printBoard(board);
                            moves.add(input);
                            white = true;
                            black = false;
                            invalidate();
                            if (Logic.isChecked(board, Logic.getWhiteKing(board).getX(), Logic.getWhiteKing(board).getY())) {
                                if (Logic.checkMate(board, Logic.getWhiteKing(board).getX(), Logic.getWhiteKing(board).getY())) {
                                    System.out.println();
                                    System.out.println("Black Wins");
                                    System.out.println();
                                    gameOver = true;
                                    gameOver(false);
                                    return;
                                } else {
                                    whiteCheck = true;
                                    System.out.println();
                                    System.out.println("Check");
                                    check(true);
                                    System.out.println();
                                }
                            }
                        }
                    }
                    textView.setText(R.string.whiteMove);
                }
                // it is an invalid move
                else {
                    invalidMove();
                }
            }
        }
        // redraw board
        invalidate();
    }
    public void chessDriver() { board = initBoard(); }
    public void setBooleanVar(String name, boolean value) {
        switch(name){
            case "white":
                this.white = value;
                break;
            case "black":
                this.black = value;
                break;
            case "whiteCheck":
                this.whiteCheck = value;
                break;
            case "blackCheck":
                this.blackCheck = value;
                break;
            case "gameOver":
                this.gameOver = value;
                break;
        }
    }

    public static Cell[][] getBoard() { return board; }
    public static List<String> getMoves() { return moves; }
    public static Cell[][] copyBoard(Cell[][] board){
        Cell[][] copy = new Cell[8][8];
        // now copy board contents
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                Cell temp = new Cell(board[i][j].getColor(), board[i][j].getX(), board[i][j].getY(), board[i][j].getRect().left, board[i][j].getRect().top, board[i][j].getRect().right, board[i][j].getRect().bottom);
                temp.setPiece(board[i][j].getPiece());
                copy[i][j] = temp;
            }
        }
        return copy;
    }

    public Cell[][] initBoard() {
        Cell[][] board = new Cell[8][8];
        //initialize the cells
        for(int j = 0; j < board.length; j++) {
            for(int i = 0; i < board[0].length; i++) {
                if((i % 2) == (j % 2)) {
                    board[j][i] = new Cell(Chess.Color.LIGHT_BROWN, j, i,  i * squareDimension, j * squareDimension, (i + 1) * squareDimension, (j + 1) * squareDimension);
                }
                else {
                    board[j][i] = new Cell(Chess.Color.DARK_BROWN, j, i, i * squareDimension, j * squareDimension, (i + 1) * squareDimension, (j + 1) * squareDimension);
                }
            }
        }
        //initialize the pieces
        board[0][0].setPiece(new Rook(Chess.Color.BLACK, Type.ROOK));
        board[0][1].setPiece(new Knight(Chess.Color.BLACK, Type.KNIGHT));
        board[0][2].setPiece(new Bishop(Chess.Color.BLACK, Type.BISHOP));
        board[0][3].setPiece(new Queen(Chess.Color.BLACK, Type.QUEEN));
        board[0][4].setPiece(new King(Chess.Color.BLACK, Type.KING));
        board[0][5].setPiece(new Bishop(Chess.Color.BLACK, Type.BISHOP));
        board[0][6].setPiece(new Knight(Chess.Color.BLACK, Type.KNIGHT));
        board[0][7].setPiece(new Rook(Chess.Color.BLACK, Type.ROOK));

        board[7][0].setPiece(new Rook(Chess.Color.WHITE, Type.ROOK));
        board[7][1].setPiece(new Knight(Chess.Color.WHITE, Type.KNIGHT));
        board[7][2].setPiece(new Bishop(Chess.Color.WHITE, Type.BISHOP));
        board[7][3].setPiece(new Queen(Chess.Color.WHITE, Type.QUEEN));
        board[7][4].setPiece(new King(Chess.Color.WHITE, Type.KING));
        board[7][5].setPiece(new Bishop(Chess.Color.WHITE, Type.BISHOP));
        board[7][6].setPiece(new Knight(Chess.Color.WHITE, Type.KNIGHT));
        board[7][7].setPiece(new Rook(Chess.Color.WHITE, Type.ROOK));

        for(int i = 0; i < board.length; i++) {
            board[1][i].setPiece(new Pawn(Chess.Color.BLACK, Type.PAWN, (Activity) this.getContext()));
            board[6][i].setPiece(new Pawn(Chess.Color.WHITE, Type.PAWN, (Activity) this.getContext()));
        }

        // initialize game values
        this.white = true;
        this.black = false;
        this.whiteCheck = false;
        this.blackCheck = false;
        this.gameOver = false;

        return board;
    }
    public Bitmap pieceToBitmap(int column, Chess.Color color) {
        Bitmap bitmap;
        switch (column) {
            case 0:
                bitmap = (color == Chess.Color.BLACK) ? BitmapFactory.decodeResource(getContext().getResources(), R.drawable.black_rook) : BitmapFactory.decodeResource(getContext().getResources(), R.drawable.white_rook);
                break;
            case 1:
                bitmap = (color == Chess.Color.BLACK) ? BitmapFactory.decodeResource(getContext().getResources(), R.drawable.black_knight) : BitmapFactory.decodeResource(getContext().getResources(), R.drawable.white_knight);
                break;
            case 2:
                bitmap = (color == Chess.Color.BLACK) ? BitmapFactory.decodeResource(getContext().getResources(), R.drawable.black_bishop) : BitmapFactory.decodeResource(getContext().getResources(), R.drawable.white_bishop);
                break;
            case 3:
                bitmap = (color == Chess.Color.BLACK) ? BitmapFactory.decodeResource(getContext().getResources(), R.drawable.black_queen) : BitmapFactory.decodeResource(getContext().getResources(), R.drawable.white_queen);
                break;
            case 4:
                bitmap = (color == Chess.Color.BLACK) ? BitmapFactory.decodeResource(getContext().getResources(), R.drawable.black_king) : BitmapFactory.decodeResource(getContext().getResources(), R.drawable.white_king);
                break;
            case 5:
                bitmap = (color == Chess.Color.BLACK) ? BitmapFactory.decodeResource(getContext().getResources(), R.drawable.black_bishop) : BitmapFactory.decodeResource(getContext().getResources(), R.drawable.white_bishop);
                break;
            case 6:
                bitmap = (color == Chess.Color.BLACK) ? BitmapFactory.decodeResource(getContext().getResources(), R.drawable.black_knight) : BitmapFactory.decodeResource(getContext().getResources(), R.drawable.white_knight);
                break;
            case 7:
                bitmap = (color == Chess.Color.BLACK) ? BitmapFactory.decodeResource(getContext().getResources(), R.drawable.black_rook) : BitmapFactory.decodeResource(getContext().getResources(), R.drawable.white_rook);
                break;
            default:
                bitmap = null;
                break;
        }
        return bitmap;
    }
    public TextView getTextView() {return this.textView; }
    public List<Cell> getPieces(Color color) {
        List<Cell> pieces = new ArrayList<Cell>();
        for(Cell[] row: board) {
            for(Cell cell: row) {
                if(cell.getPiece() != null && cell.getPiece().getColor() == color){
                    pieces.add(cell);
                }
            }
        }
        return pieces;
    }

    public int xCoordinateToColumn(float xCoordinate) {
        int result = -1;
        if(xCoordinate >= 0 && xCoordinate <= squareDimension) {
            result = 0;
        }
        else if(xCoordinate > squareDimension && xCoordinate <= 2*squareDimension) {
            result = 1;
        }
        else if(xCoordinate > 2*squareDimension && xCoordinate <= 3*squareDimension) {
            result = 2;
        }
        else if(xCoordinate > 3*squareDimension && xCoordinate <= 4*squareDimension) {
            result = 3;
        }
        else if(xCoordinate > 4*squareDimension && xCoordinate <= 5*squareDimension) {
            result = 4;
        }
        else if(xCoordinate > 5*squareDimension && xCoordinate <= 6*squareDimension) {
            result = 5;
        }
        else if(xCoordinate > 6*squareDimension && xCoordinate <= 7*squareDimension) {
            result = 6;
        }
        else if(xCoordinate > 7*squareDimension && xCoordinate <= 8*squareDimension) {
            result = 7;
        }
        return result;
    }
    public int yCoordinateToRow(float yCoordinate) {
        return xCoordinateToColumn(yCoordinate);
    }

    public boolean getBooleanVar(String name){
        boolean result = false;
        switch(name){
            case "white":
                result = this.white;
                break;
            case "black":
                result = this.black;
                break;
            case "whiteCheck":
                result = this.whiteCheck;
                break;
            case "blackCheck":
                result = this.blackCheck;
                break;
            case "gameOver":
                result = this.gameOver;
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            float xCoordinate = event.getX();
            float yCoordinate = event.getY();
            int row = yCoordinateToRow(yCoordinate);
            int col = xCoordinateToColumn(xCoordinate);
            String s = ""+row+col;
            PlayActivity.getMoves().add(s);
            handleMoves();
            return true;
        }
       return false;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[0].length; j++) {
                board[i][j].drawCell(canvas, getContext());
            }
        }
    }


}