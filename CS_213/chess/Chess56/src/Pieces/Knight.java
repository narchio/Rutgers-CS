package Pieces;
import java.util.ArrayList;
import java.util.List;

import Chess.Cell;
import Chess.Color;
import Chess.Type;
import Chess.Logic;

/**
 * The class Knight represents a knight chess piece.
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class Knight extends Piece{
	/**
	 * Creates an instance of Knight with the specified color and type KNIGHT
	 * @param color The color of the knight
	 * @param type Type.KNIGHT
	 */
	public Knight(Color color, Type type) {
		super(color, type);
	}
	/**
	 * Determines whether user input is valid for a knight chess piece and moves the piece to the desired location.
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
		
		// error check on bounds (just to be sure) 
		if (endX < 0 || endY < 0) { System.out.println("Invalid move, try again"); return false;}
		
		// Case 1: Up 1, left 2 
		if ((Math.abs(startY-endY) == 2 && Math.abs(startX-endX) == 1) && (startY-2 == endY)) {
			// if there is not a piece in that location, move to it  
			if (board[endX][endY].getPiece() == null) {
				// set the knight to the new place and remove the current piece
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				this.updateNumMoves();
				return true;
				
			} // there is a piece of a different color, that is not the KING, take the piece 
			else if (board[endX][endY].getPiece() != null && board[endX][endY].getPiece().getColor() != board[startX][startY].getPiece().getColor() && board[endX][endY].getPiece().getPieceType() != Type.KING){
				// set the knight to the new place and remove the current piece
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				this.updateNumMoves();
				return true; 
			} else {
				System.out.println("Invalid move, try again");
				return false;
			}
		} 
		// Case 2: Up 1, right 2 
		else if ((Math.abs(startY-endY) == 2 && Math.abs(startX-endX) == 1) && (startY+2 == endY)) {
			// if there is not a piece in that location, move to it  
			if (board[endX][endY].getPiece() == null) {
				// set the knight to the new place and remove the current piece
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				this.updateNumMoves(); 
				return true;

			} // there is a piece of a different color, that is not the KING, take the piece 
			else if (board[endX][endY].getPiece() != null && board[endX][endY].getPiece().getColor() != board[startX][startY].getPiece().getColor() && board[endX][endY].getPiece().getPieceType() != Type.KING){
				// set the knight to the new place and remove the current piece
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				this.updateNumMoves(); 
				return true; 
			} else {
				System.out.println("Invalid move, try again");
				return false;
			}
		}
		// Case 3: Up 2, left 1
		else if ((Math.abs(startY-endY) == 1 && Math.abs(startX-endX) == 2) && (startY-1 == endY)) {
			// if there is not a piece in that location, move to it  
			if (board[endX][endY].getPiece() == null) {
				// set the knight to the new place and remove the current piece
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				this.updateNumMoves();
				return true;

			} // there is a piece of a different color, that is not the KING, take the piece 
			else if (board[endX][endY].getPiece() != null && board[endX][endY].getPiece().getColor() != board[startX][startY].getPiece().getColor() && board[endX][endY].getPiece().getPieceType() != Type.KING){
				// set the knight to the new place and remove the current piece
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				this.updateNumMoves();
				return true; 
			} else {
				System.out.println("Invalid move, try again");
				return false;
			}
		}
		// Case 4: Up 2, right 1 
		else if ((Math.abs(startY-endY) == 1 && Math.abs(startX-endX) == 2) && (startY+1 == endY)) {
			// if there is not a piece in that location, move to it  
			if (board[endX][endY].getPiece() == null) {
				// set the knight to the new place and remove the current piece
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				this.updateNumMoves(); 
				return true;

			} // there is a piece of a different color, that is not the KING, take the piece 
			else if (board[endX][endY].getPiece() != null && board[endX][endY].getPiece().getColor() != board[startX][startY].getPiece().getColor() && board[endX][endY].getPiece().getPieceType() != Type.KING){
				// set the knight to the new place and remove the current piece
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				System.out.println("Case 4: moving to " + endX + " " + endY); 
				return true; 
			} else {
				this.updateNumMoves();
				return false;
			}
		} 
		// if not a correct move, return false
		System.out.println("Invalid move, try again");
		return false;
	}
	/**
	 * Generates a list of cells that represents the legal moves for a knight.
	 * @param board The Chess board.
	 * @param start The cell containing the knight.
	 * @return List of cells containing the legal moves.
	 */
	@Override
	public ArrayList<Cell> generateLegalMoves(Cell[][] board, Cell start) {
		int startX = start.getX();
		int startY = start.getY();
		ArrayList<Cell> result = new ArrayList<Cell>();
		
		//check moving up
		if(startX-2 >= 0 && startY-1 >= 0) {
			//spot is empty 
			if(board[startX-2][startY-1].getPiece() == null) {
				result.add(board[startX-2][startY-1]);
			}
			//there is a piece to capture
			else if(board[startX-2][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-2][startY-1]);
			}
		}
		if(startX-2 >= 0 && startY+1 < board.length-1) {
			//spot is empty 
			if(board[startX-2][startY+1].getPiece() == null) {
				result.add(board[startX-2][startY+1]);
			}
			//there is a piece to capture
			else if(board[startX-2][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-2][startY+1]);
			}
		}
		
		//check moving down
		if(startX+2 < board.length-1 && startY-1 >= 0) {
			//spot is empty 
			if(board[startX+2][startY-1].getPiece() == null) {
				result.add(board[startX+2][startY-1]);
			}
			//there is a piece to capture
			else if(board[startX+2][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+2][startY-1]);
			}
		}
		if(startX+2 < board.length-1 && startY+1 < board.length-1) {
			//spot is empty 
			if(board[startX+2][startY+1].getPiece() == null) {
				result.add(board[startX+2][startY+1]);
			}
			//there is a piece to capture
			else if(board[startX+2][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+2][startY+1]);
			}
		}
		
		//check moving left 
		if(startX-1 >= 0 && startY-2 >= 0) {
			//spot is empty 
			if(board[startX-1][startY-2].getPiece() == null) {
				result.add(board[startX-1][startY-2]);
			}
			//there is a piece to capture
			else if(board[startX-1][startY-2].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-1][startY-2]);
			}
		}
		if(startX+1 < board.length-1 && startY-2 >= 0) {
			//spot is empty 
			if(board[startX+1][startY-2].getPiece() == null) {
				result.add(board[startX+1][startY-2]);
			}
			//there is a piece to capture
			else if(board[startX+1][startY-2].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+1][startY-2]);
			}
		}
		
		//check moving right 
		if(startX-1 >= 0 && startY+2 < board.length-1) {
			//spot is empty 
			if(board[startX-1][startY+2].getPiece() == null) {
				result.add(board[startX-1][startY+2]);
			}
			//there is a piece to capture
			else if(board[startX-1][startY+2].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-1][startY+2]);
			}
		}	
		if(startX+1 < board.length-1 && startY+2 < board.length-1) {
			//spot is empty 
			if(board[startX+1][startY+2].getPiece() == null) {
				result.add(board[startX+1][startY+2]);
			}
			//there is a piece to capture
			else if(board[startX+1][startY+2].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+1][startY+2]);
			}
		}
		
		return result;
		
	}

}
