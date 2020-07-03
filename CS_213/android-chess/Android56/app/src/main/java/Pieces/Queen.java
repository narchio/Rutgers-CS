package Pieces;
import java.util.ArrayList;
import java.util.List;

import Chess.Cell;
import Chess.Color;
import Chess.Logic;
import Chess.Type;

public class Queen extends Piece{

	public Queen(Color color, Type type) {
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
		// Case 3: Diagonal move
		else if(Math.abs(startX-endX) == Math.abs(startY-endY)) {
			//check if there is a clear path
			if(Logic.clearPath(board, startX, startY, endX, endY)) {
				//move the piece
				board[endX][endY].setPiece(board[startX][startY].getPiece());
				//change initial spot to empty
				board[startX][startY].setPiece(null);
				this.updateNumMoves();
				return true;
			}
			else {
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

		//check spaces moving up
		for(int deltaX = 1; deltaX <= startX; deltaX++) {
			//square is empty
			if(board[startX-deltaX][startY].getPiece() == null) {
				result.add(board[startX-deltaX][startY]);
				continue;
			}
			//there is a piece to capture
			else if(board[startX-deltaX][startY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX-deltaX][startY]);
				break;
			}
			//friendly blocking piece
			else {
				break;
			}
		}

		//check spaces moving down
		for(int deltaX = 1; deltaX < Math.abs(board.length-startX); deltaX++) {
			//square is empty
			if(board[startX+deltaX][startY].getPiece() == null) {
				result.add(board[startX+deltaX][startY]);
				continue;
			}
			//there is a piece to capture
			else if(board[startX+deltaX][startY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX+deltaX][startY]);
				break;
			}
			//friendly blocking piece
			else {
				break;
			}
		}

		//check spaces moving left
		for(int deltaY = 1; deltaY <= startY; deltaY++) {
			//square is empty
			if(board[startX][startY-deltaY].getPiece() == null) {
				result.add(board[startX][startY-deltaY]);
				continue;
			}
			//there is a piece to capture
			else if(board[startX][startY-deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX][startY-deltaY]);
				break;
			}
			//friendly blocking piece
			else {
				break;
			}
		}

		//check spaces moving right
		for(int deltaY = 1; deltaY < Math.abs(board[0].length-startY); deltaY++) {
			//square is empty
			if(board[startX][startY+deltaY].getPiece() == null) {
				result.add(board[startX][startY+deltaY]);
				continue;
			}
			//there is a piece to capture
			else if(board[startX][startY+deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
				result.add(board[startX][startY+deltaY]);
				break;
			}
			//friendly blocking piece
			else {
				break;
			}
		}

		//check up-left diagonal
		for(int deltaX = 1, deltaY = 1; deltaX <= startX; deltaX++, deltaY++) {
			if(deltaY <= startY) {
				//square is empty
				if(board[startX-deltaX][startY-deltaY].getPiece() == null) {
					result.add(board[startX-deltaX][startY-deltaY]);
					continue;
				}
				//there is a piece to capture
				else if(board[startX-deltaX][startY-deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					result.add(board[startX-deltaX][startY-deltaY]);
					break;
				}
				//friendly blocking piece
				else {
					break;
				}
			}
		}

		//check up-right diagonal
		for(int deltaX = 1, deltaY = 1; deltaX <= startX; deltaX++, deltaY++) {
			if(deltaY < Math.abs(board[0].length-startY)) {
				//square is empty
				if(board[startX-deltaX][startY+deltaY].getPiece() == null) {
					result.add(board[startX-deltaX][startY+deltaY]);
					continue;
				}
				//there is a piece to capture
				else if(board[startX-deltaX][startY+deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					result.add(board[startX-deltaX][startY+deltaY]);
					break;
				}
				//friendly blocking piece
				else {
					break;
				}
			}
		}

		//check down-left diagonal
		for(int deltaX = 1, deltaY = 1; deltaX < Math.abs(board.length-startX); deltaX++, deltaY++) {
			if(deltaY <= startY) {
				//square is empty
				if(board[startX+deltaX][startY-deltaY].getPiece() == null) {
					result.add(board[startX+deltaX][startY-deltaY]);
					continue;
				}
				//there is a piece to capture
				else if(board[startX+deltaX][startY-deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					result.add(board[startX+deltaX][startY-deltaY]);
					break;
				}
				//friendly blocking piece
				else {
					break;
				}
			}
		}

		//check for down-right diagonal
		for(int deltaX = 1, deltaY = 1; deltaX < Math.abs(board.length-startX); deltaX++, deltaY++) {
			if(deltaY < Math.abs(board[0].length-startY)) {
				//square is empty
				if(board[startX+deltaX][startY+deltaY].getPiece() == null) {
					result.add(board[startX+deltaX][startY+deltaY]);
					continue;
				}
				//there is a piece to capture
				else if(board[startX+deltaX][startY+deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					result.add(board[startX+deltaX][startY+deltaY]);
					break;
				}
				//friendly blocking piece
				else {
					break;
				}
			}
		}
		return result;
	}
}
