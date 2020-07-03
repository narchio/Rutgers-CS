package Pieces;
import java.util.ArrayList;
import java.util.List;

import Chess.Cell;
import Chess.Color;
import Chess.Logic;
import Chess.Type;

public class Rook extends Piece{

	private boolean moved;

	public Rook(Color color, Type type) {
		super(color, type);
	}

	@Override
	public boolean move(Cell[][] board, String input) {
		int startX = Character.getNumericValue(input.charAt(0));
		int startY = Character.getNumericValue(input.charAt(1));
		int endX = Character.getNumericValue(input.charAt(2));
		int endY = Character.getNumericValue(input.charAt(3));

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

	@Override
	public ArrayList<Cell> generateLegalMoves(Cell[][] board, int startX, int startY) {
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
		for(int delta = 1; delta < Math.abs(board.length-startX); delta++) {
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
		for(int delta = 1; delta < Math.abs(board[0].length-startY); delta++) {
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
