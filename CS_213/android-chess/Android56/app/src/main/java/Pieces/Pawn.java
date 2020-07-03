package Pieces;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.example.android56.ChessBoard;

import java.util.ArrayList;
import java.util.List;

import Chess.Cell;
import Chess.Color;
import Chess.Logic;
import Chess.Type;


public class Pawn extends Piece{

	private Activity activity;

	public Pawn(Color color, Type type) {
		this(color, type, null);
	}

	public Pawn(Color color, Type type, Activity activity) {
		super(color, type);
		this.activity = activity;
	}

	@Override
	public boolean move(Cell[][] board, String input) {
		int startX = Character.getNumericValue(input.charAt(0));
		int startY = Character.getNumericValue(input.charAt(1));
		int endX = Character.getNumericValue(input.charAt(2));
		int endY = Character.getNumericValue(input.charAt(3));

		//pawn is in its starting position
		if((startX == 1 && board[startX][startY].getPiece().getColor() == Color.BLACK) || (startX == 6 && board[startX][startY].getPiece().getColor() == Color.WHITE)) {
			//pawn is moving directly forward
			if((Math.abs(startX-endX) == 1 || Math.abs(startX-endX) == 2) && Math.abs(startY-endY) == 0) {
				//spot is not already occupied
				if(board[endX][endY].getPiece() == null) {
					//move the piece
					board[endX][endY].setPiece(board[startX][startY].getPiece());
					//set the initial spot to empty
					board[startX][startY].setPiece(null);
					this.updateNumMoves();
					return true;
				}
				//spot is already occupied --> invalid move
				else {
					System.out.println("Invalid move, try again");
					return false;
				}
			}
			//pawn is attacking
			else if(Math.abs(startX-endX) == 1 && Math.abs(startY-endY) == 1) {
				//check that there is a piece to capture
				if(board[endX][endY].getPiece() != null && board[endX][endY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//move the piece
					board[endX][endY].setPiece(board[startX][startY].getPiece());
					//set the initial spot to empty
					board[startX][startY].setPiece(null);
					this.updateNumMoves();
					return true;
				}
				//there is no piece to capture
				else {
					System.out.println("Invalid move, try again");
					return false;
				}
			}
		}
		//pawn is not in the starting position
		else {
			//pawn is moving directly forward
			if(Math.abs(startX-endX) == 1 && Math.abs(startY-endY) == 0) {
				//spot is not already occupied
				if(board[endX][endY].getPiece() == null) {
					//move the piece
					board[endX][endY].setPiece(board[startX][startY].getPiece());
					//set the initial spot to empty
					board[startX][startY].setPiece(null);
					Chess.Color pieceColor = board[endX][endY].getPiece().getColor();
					//check for promotion
					if(endX == 0 || endX == 7) {
						AlertDialog.Builder alert = new AlertDialog.Builder(activity);
						alert.setTitle("Choose pawn promotion");
						String[] options = {"ROOK", "KNIGHT", "BISHOP", "QUEEN"};
						alert.setItems(options, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch(which) {
									case 0:
										board[endX][endY].setPiece(new Rook(pieceColor, Type.ROOK));
										break;
									case 1:
										board[endX][endY].setPiece(new Knight(pieceColor, Type.KNIGHT));
										break;
									case 2:
										board[endX][endY].setPiece(new Bishop(pieceColor, Type.BISHOP));
										break;
									case 3:
										board[endX][endY].setPiece(new Queen(pieceColor, Type.QUEEN));
										break;
								}
								dialog.dismiss();
							}
						});
						alert.show();
					}
					Log.println(Log.ASSERT, "CHANGE", "-->"+board[endX][endY].getPiece().getPieceType());
					this.updateNumMoves();
					return true;
				}
				//spot is already occupied
				else {
					System.out.println("Invalid move, try again");
					return false;
				}
			}
			//pawn is attacking
			else if(Math.abs(startX-endX) == 1 && Math.abs(startY-endY) == 1) {
				//check that there is a piece to capture
				if(board[endX][endY].getPiece() != null && board[endX][endY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//move the piece
					board[endX][endY].setPiece(board[startX][startY].getPiece());
					//set the initial spot to empty
					board[startX][startY].setPiece(null);
					//check for promotion
					Chess.Color pieceColor = board[endX][endY].getPiece().getColor();
					if(endX == 0 || endX == 7) {
						AlertDialog.Builder alert = new AlertDialog.Builder(activity);
						alert.setTitle("Choose pawn promotion");
						String[] options = {"ROOK", "KNIGHT", "BISHOP", "QUEEN"};
						alert.setItems(options, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch(which) {
									case 0:
										board[endX][endY].setPiece(new Rook(pieceColor, Type.ROOK));
										break;
									case 1:
										board[endX][endY].setPiece(new Knight(pieceColor, Type.KNIGHT));
										break;
									case 2:
										board[endX][endY].setPiece(new Bishop(pieceColor, Type.BISHOP));
										break;
									case 3:
										board[endX][endY].setPiece(new Queen(pieceColor, Type.QUEEN));
										break;
								}
								dialog.dismiss();
							}
						});
						alert.show();
					}
					Log.println(Log.ASSERT, "CHANGE", "-->"+board[endX][endY].getPiece().getPieceType());
					this.updateNumMoves();
					return true;
				}
				//enpassant capture
				else if( (Logic.isInBounds(startY+1) && board[startX][startY+1].getPiece() != null) || (Logic.isInBounds(startY-1) && board[startX][startY-1].getPiece() != null)) {
					//get the most recent move from the game
					String lastMove = ChessBoard.getMoves().get(ChessBoard.getMoves().size()-1);
					int lmStartX = Character.getNumericValue(lastMove.charAt(0));
					int lmStartY = Character.getNumericValue(lastMove.charAt(1));
					int lmEndX = Character.getNumericValue(lastMove.charAt(2));
					int lmEndY = Character.getNumericValue(lastMove.charAt(3));
					if((lmStartX == 1 || lmStartX == 6) && (lmEndX == startX) && (lmEndY == startY+1 || lmEndY == startY-1)) {
						//check to the left
						if(board[startX][startY+1].getPiece() != null && board[startX][startY+1].getPiece().getColor() != this.getColor() && board[startX][startY+1].getPiece().getPieceType() == Type.PAWN && board[startX][startY+1].getPiece().getNumMoves() == 1) {
							//move the pawn
							board[endX][endY].setPiece(board[startX][startY].getPiece());
							//set the initial spot to blank
							board[startX][startY].setPiece(null);
							//set the capture square to blank
							board[startX][startY+1].setPiece(null);
							board[endX][endY].getPiece().updateNumMoves();
							return true;
						}
						else if(board[startX][startY-1].getPiece() != null && board[startX][startY-1].getPiece().getColor() != this.getColor() && board[startX][startY-1].getPiece().getPieceType() == Type.PAWN && board[startX][startY-1].getPiece().getNumMoves() == 1) {
							//move the pawn
							board[endX][endY].setPiece(board[startX][startY].getPiece());
							//set the initial spot to blank
							board[startX][startY].setPiece(null);
							//set the capture square to blank
							board[startX][startY-1].setPiece(null);
							board[endX][endY].getPiece().updateNumMoves();
							return true;
						}
					}
				}
				//there is no piece to capture
				else {
					System.out.println("Invalid move, try again");
					return false;
				}
			}
		}
		System.out.println("Invalid move, try again");
		return false;
	}

	//@Override --> had a 'Cell start' passed into the method, replaced with passing in the start (X & Y)
	public ArrayList<Cell> generateLegalMoves(Cell[][] board, int startX, int startY) {
		ArrayList<Cell> result = new ArrayList<Cell>();

		//black pawn in start
		if(startX == 1 && board[startX][startY].getPiece().getColor() == Color.BLACK) {
			//move one space forward
			if(board[startX+1][startY].getPiece() == null) {
				result.add(board[startX+1][startY]);
			}
			//two spots forward
			if(board[startX+2][startY].getPiece() == null) {
				result.add(board[startX+2][startY]);
			}
			//attacking
			if(startY-1 >= 0 && board[startX+1][startY-1].getPiece() != null && board[startX+1][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+1][startY-1]);
			}
			if(startY+1 < board[0].length && board[startX+1][startY+1].getPiece() != null && board[startX+1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+1][startY+1]);
			}
		}
		//white pawn in start
		if(startX == 6 && board[startX][startY].getPiece().getColor() == Color.WHITE) {
			//move one space forward
			if(board[startX-1][startY].getPiece() == null) {
				result.add(board[startX-1][startY]);
			}
			//two spots forward
			if(board[startX-2][startY].getPiece() == null) {
				result.add(board[startX-2][startY]);
			}
			//attacking
			if(startY-1 >= 0 && board[startX-1][startY-1].getPiece() != null && board[startX-1][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-1][startY-1]);
			}
			if(startY+1 < board[0].length && board[startX-1][startY+1].getPiece() != null && board[startX-1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-1][startY+1]);
			}
		}
		//black pawn not in start
		if(startX != 1 && board[startX][startY].getPiece().getColor() == Color.BLACK) {
			//move one space forward
			if(startX+1 < board.length && board[startX+1][startY].getPiece() == null) {
				result.add(board[startX+1][startY]);
			}
			//attacking
			if(startX+1 < board.length && startY-1 >= 0 && board[startX+1][startY-1].getPiece() != null && board[startX+1][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+1][startY-1]);
			}
			if(startX+1 < board.length && startY+1 < board[0].length && board[startX+1][startY+1].getPiece() != null && board[startX+1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+1][startY+1]);
			}
			//enpassant
			if(startY-1 >= 0 && startX+1 < board.length && board[startX][startY-1].getPiece() != null) {
				//check if the piece is pawn
				if(board[startX][startY-1].getPiece().getPieceType() == Type.PAWN && board[startX][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//get last move
					if(ChessBoard.getMoves().size() > 0) {
						String lastMove = ChessBoard.getMoves().get(ChessBoard.getMoves().size()-1);
						int lmStartX = Character.getNumericValue(lastMove.charAt(0));
						int lmEndX = Character.getNumericValue(lastMove.charAt(2));
						if(lmStartX == 6 && lmEndX == startX && board[startX][startY-1].getPiece().getNumMoves() == 1) {
							result.add(board[startX+1][startY-1]);
						}
					}
				}
			}
			if(startY+1 < board[0].length && startX+1 < board.length && board[startX][startY+1].getPiece() != null) {
				//check if the piece is pawn
				if(board[startX][startY+1].getPiece().getPieceType() == Type.PAWN && board[startX][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//get last move
					if(ChessBoard.getMoves().size() > 0) {
						String lastMove = ChessBoard.getMoves().get(ChessBoard.getMoves().size()-1);
						int lmStartX = Character.getNumericValue(lastMove.charAt(0));
						int lmEndX = Character.getNumericValue(lastMove.charAt(2));
						if(lmStartX == 6 && lmEndX == startX && board[startX][startY+1].getPiece().getNumMoves() == 1) {
							result.add(board[startX+1][startY+1]);
						}
					}
				}
			}
		}
		//white pawn not in start
		if(startX != 6 && board[startX][startY].getPiece().getColor() == Color.WHITE) {
			//move one space forward
			if(startX-1 >= 0 && board[startX-1][startY].getPiece() == null) {
				result.add(board[startX-1][startY]);
			}
			//attacking
			if(startX-1 >= 0 && startY-1 >= 0 && board[startX-1][startY-1].getPiece() != null && board[startX-1][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-1][startY-1]);
			}
			if(startX-1 >= 0 && startY+1 < board[0].length && board[startX-1][startY+1].getPiece() != null && board[startX-1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-1][startY+1]);
			}
			//enpassant
			if(startY-1 >= 0 && startX-1 >= 0 && board[startX][startY-1].getPiece() != null) {
				//check if the piece is pawn
				if(board[startX][startY-1].getPiece().getPieceType() == Type.PAWN && board[startX][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//get last move
					if(ChessBoard.getMoves().size() > 0) {
						String lastMove = ChessBoard.getMoves().get(ChessBoard.getMoves().size()-1);
						int lmStartX = Character.getNumericValue(lastMove.charAt(0));
						int lmEndX = Character.getNumericValue(lastMove.charAt(2));
						if(lmStartX == 1 && lmEndX == startX && board[startX][startY-1].getPiece().getNumMoves() == 1) {
							result.add(board[startX-1][startY-1]);
						}
					}
				}
			}
			if(startY+1 < board[0].length && startX-1 >= 0 && board[startX][startY+1].getPiece() != null) {
				//check if the piece is pawn
				if(board[startX][startY+1].getPiece().getPieceType() == Type.PAWN && board[startX][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//get last move
					if(ChessBoard.getMoves().size() > 0) {
						String lastMove = ChessBoard.getMoves().get(ChessBoard.getMoves().size()-1);
						int lmStartX = Character.getNumericValue(lastMove.charAt(0));
						int lmEndX = Character.getNumericValue(lastMove.charAt(2));
						if(lmStartX == 1 && lmEndX == startX && board[startX][startY+1].getPiece().getNumMoves() == 1) {
							result.add(board[startX-1][startY+1]);
						}
					}
				}
			}
		}
		return result;
	}
}
