package Pieces;
import java.util.ArrayList;
import java.util.List;

import Chess.Cell;
import Chess.Color;
import Chess.Logic;
import Chess.Type;
import Chess.Chess;

/**
 * The class Pawn represents a pawn chess piece
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class Pawn extends Piece{
	/**
	 * Creates an instance of Pawn with the specified color and type PAWN.
	 * @param color The color of the pawn.
	 * @param type Type.PAWN
	 */
	public Pawn(Color color, Type type) {
		super(color, type);
	}
	/**
	 * Determines whether user input is valid for a pawn chess piece and moves the piece to the desired location.
	 * @param board The chess board.
	 * @param input User input from console.
	 * @return True if the move was successful, false otherwise. 
	 */
	@Override
	public boolean move(Cell[][] board, String input) {
		int startX = Logic.ROW_MAP.get(Integer.parseInt(input.substring(1, 2)));
		int startY = Logic.COLUMN_MAP.get(input.charAt(0));
		int endX = Logic.ROW_MAP.get(Integer.parseInt(input.substring(4, 5)));
		int endY = Logic.COLUMN_MAP.get(input.charAt(3));
		
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
					//check for promotion 
					if(endX == 0 || endX == 7) {
						//check whether promotion was specified 
						if(Logic.isPawnPromotion(input)) {
							switch(input.charAt(6)) {
							case 'R': 
								board[endX][endY].getPiece().setPieceType(Type.ROOK);
								break;
							case 'N':
								board[endX][endY].getPiece().setPieceType(Type.KNIGHT);
								break;
							case 'B': 
								board[endX][endY].getPiece().setPieceType(Type.BISHOP);
								break;
							case 'Q':
								board[endX][endY].getPiece().setPieceType(Type.QUEEN);
								break;
							}
						}
						//promotion is not specified --> default promotion to queen
						else {
							board[endX][endY].getPiece().setPieceType(Type.QUEEN);
						}
					}
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
					if(endX == 0 || endX == 7) {
						//check whether promotion was specified 
						if(Logic.isPawnPromotion(input)) {
							switch(input.charAt(6)) {
							case 'R': 
								board[endX][endY].getPiece().setPieceType(Type.ROOK);
								break;
							case 'N':
								board[endX][endY].getPiece().setPieceType(Type.KNIGHT);
								break;
							case 'B': 
								board[endX][endY].getPiece().setPieceType(Type.BISHOP);
								break;
							case 'Q':
								board[endX][endY].getPiece().setPieceType(Type.QUEEN);
								break;
							}
						}
						//promotion is not specified --> default promotion to queen
						else {
							board[endX][endY].getPiece().setPieceType(Type.QUEEN);
						}
					}
					this.updateNumMoves();
					return true;
				}
				//enpassant capture 
				else if(board[startX][startY+1].getPiece() != null || board[startX][startY-1].getPiece() != null) {
					//get the most recent move from the game
					String lastMove = Chess.moves.get(Chess.moves.size()-1);
					int lmStartX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(1, 2)));
					int lmStartY = Logic.COLUMN_MAP.get(lastMove.charAt(0));
					int lmEndX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(4, 5)));
					int lmEndY = Logic.COLUMN_MAP.get(lastMove.charAt(3));
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
	/**
	 * Generates a list of cells that represents the legal moves for a pawn.
	 * @param board The Chess board.
	 * @param start The cell containing the pawn.
	 * @return List of cells containing the legal moves.
	 */
	@Override
	public ArrayList<Cell> generateLegalMoves(Cell[][] board, Cell start) {
		int startX = start.getX();
		int startY = start.getY();
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
			if(startY+1 < board[0].length-1 && board[startX+1][startY+1].getPiece() != null && board[startX+1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
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
			if(startY+1 < board[0].length-1 && board[startX-1][startY+1].getPiece() != null && board[startX-1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-1][startY+1]);
			}
		}
		//black pawn not in start 
		if(startX != 1 && board[startX][startY].getPiece().getColor() == Color.BLACK) {
			//move one space forward 
			if(startX+1 < board.length-1 && board[startX+1][startY].getPiece() == null) {
				result.add(board[startX+1][startY]);
			}
			//attacking
			if(startX+1 < board.length-1 && startY-1 >= 0 && board[startX+1][startY-1].getPiece() != null && board[startX+1][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+1][startY-1]);
			}
			if(startX+1 < board.length-1 && startY+1 < board[0].length-1 && board[startX+1][startY+1].getPiece() != null && board[startX+1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+1][startY+1]);
			}
			//enpassant 
			if(startY-1 >= 0 && startX+1 < board.length-1 && board[startX][startY-1].getPiece() != null) {
				//check if the piece is pawn 
				if(board[startX][startY-1].getPiece().getPieceType() == Type.PAWN && board[startX][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//get last move
					if(Chess.moves.size() > 0) {
						String lastMove = Chess.moves.get(Chess.moves.size()-1);
						int lmStartX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(1, 2)));
						int lmEndX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(4, 5)));
						if(lmStartX == 6 && lmEndX == startX && board[startX][startY-1].getPiece().getNumMoves() == 1) {
							result.add(board[startX+1][startY-1]);
						}
					}
				}
			}
			if(startY+1 < board[0].length-1 && startX+1 < board.length-1 && board[startX][startY+1].getPiece() != null) {
				//check if the piece is pawn 
				if(board[startX][startY+1].getPiece().getPieceType() == Type.PAWN && board[startX][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//get last move
					if(Chess.moves.size() > 0) {
						String lastMove = Chess.moves.get(Chess.moves.size()-1);
						int lmStartX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(1, 2)));
						int lmEndX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(4, 5)));
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
			if(startX-1 >= 0 && startY+1 < board[0].length-1 && board[startX-1][startY+1].getPiece() != null && board[startX-1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-1][startY+1]);
			}
			//enpassant 
			if(startY-1 >= 0 && startX-1 >= 0 && board[startX][startY-1].getPiece() != null) {
				//check if the piece is pawn 
				if(board[startX][startY-1].getPiece().getPieceType() == Type.PAWN && board[startX][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//get last move
					if(Chess.moves.size() > 0) {
						String lastMove = Chess.moves.get(Chess.moves.size()-1);
						int lmStartX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(1, 2)));
						int lmEndX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(4, 5)));
						if(lmStartX == 1 && lmEndX == startX && board[startX][startY-1].getPiece().getNumMoves() == 1) {
							result.add(board[startX-1][startY-1]);
						}
					}
				}
			}
			if(startY+1 < board[0].length-1 && startX-1 >= 0 && board[startX][startY+1].getPiece() != null) {
				//check if the piece is pawn 
				if(board[startX][startY+1].getPiece().getPieceType() == Type.PAWN && board[startX][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					//get last move
					if(Chess.moves.size() > 0) {
						String lastMove = Chess.moves.get(Chess.moves.size()-1);
						int lmStartX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(1, 2)));
						int lmEndX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(4, 5)));
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
