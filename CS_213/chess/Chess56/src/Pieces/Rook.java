package Pieces;
import java.util.ArrayList;
import java.util.List;

import Chess.Cell;
import Chess.Color;
import Chess.Logic;
import Chess.Type;

/**
 * The class Rook represents a rook chess piece.
 * @author Nicolas Carchio
 * @author Adam Romano
 *
 */
public class Rook extends Piece{
	/**
	 * Identifies whether a rook has been moved at all during a chess game.
	 */
	private boolean moved;
	/**
	 * Creates an instance of Rook with the specified color and type ROOK.
	 * @param color The color of the rook.
	 * @param type Type.ROOK
	 */
	public Rook(Color color, Type type) {
		super(color, type);
	}
	/**
	 * Determines whether user input is valid for a king chess piece and moves the piece to the desired location.
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
		
		// Case 1: Move up or down (y is same, x inc. or dec.) 
		if (Math.abs(startY - endY) == 0) {
			// if there is a clear path
			if (Logic.clearPath(board, startX, startY, endX, endY)) {
				// make the move, even if there is already a piece there
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				this.updateNumMoves();
				return true; 
			} else {
				System.out.println("Invalid move, try again");
				return false;
			}
		}
		// Case 2: Move left or right (x is same, y inc. or dec.) 
		else if (Math.abs(startX - endX) == 0) {
			if (Logic.clearPath(board, startX, startY, endX, endY)) {
				// make the move, even if there is already a piece there
				board[endX][endY].setPiece(board[startX][startY].getPiece()); 
				board[startX][startY].setPiece(null); 
				this.updateNumMoves();
				return true;
			} else {
				System.out.println("Invalid move, try again");
				return false;
			}
		}
		// invalid move
		System.out.println("Invalid move, try again");
		return false;
	}
	/**
	 * Generates a list of cells that represents the legal moves for a rook.
	 * @param board The Chess board.
	 * @param start The cell containing the rook.
	 * @return List of cells containing the legal moves.
	 */
	@Override
	public ArrayList<Cell> generateLegalMoves(Cell[][] board, Cell start) {
		int startX = start.getX();
		int startY = start.getY();
		ArrayList<Cell> result = new ArrayList<Cell>();
		//check legal moves up
		for(int delta = 1; delta <= startX; delta++) {
			//check if square is empty 
			if(board[startX-delta][startY].getPiece() == null) {
				result.add(board[startX-delta][startY]);
				continue;
			}
			//there is a piece to capture
			else if(board[startX-delta][startY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-delta][startY]);
				break;
			}
			else {
				break;
			}
		}
		//check legal moves down 
		for(int delta = 1; delta < Math.abs(board.length-1-startX); delta++) {
			//check if square is empty 
			if(board[startX+delta][startY].getPiece() == null) {
				result.add(board[startX+delta][startY]);
				continue;
			}
			//there is a piece to capture
			else if(board[startX+delta][startY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+delta][startY]);
				break;
			}
			else {
				break;
			}
		}
		//check legal moves left 
		for(int delta = 1; delta <= startY; delta++) {
			//check if square is empty 
			if(board[startX][startY-delta].getPiece() == null) {
				result.add(board[startX][startY-delta]);
				continue;
			}
			//there is a piece to capture
			else if(board[startX][startY-delta].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX][startY-delta]);
				break;
			}
			else {
				break;
			}
		}
		//check legal moves right 
		for(int delta = 1; delta < Math.abs(board[0].length-1-startY); delta++) {
			//check if square is empty 
			if(board[startX][startY+delta].getPiece() == null) {
				result.add(board[startX][startY+delta]);
				continue;
			}
			//there is a piece to capture
			else if(board[startX][startY+delta].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX][startY+delta]);
				break;
			}
			else {
				break;
			}
		}
		return result;
	}
}
