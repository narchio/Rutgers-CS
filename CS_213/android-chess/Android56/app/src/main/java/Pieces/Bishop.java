package Pieces;
import java.util.ArrayList;
import java.util.List;

import Chess.Cell;
import Chess.Color;
import Chess.Logic;
import Chess.Type;

public class Bishop extends Piece{

	public Bishop(Color color, Type type) {
		super(color, type);
	}

	@Override
	public boolean move(Cell[][] board, String input) {
		int startX = Character.getNumericValue(input.charAt(0));
		int startY = Character.getNumericValue(input.charAt(1));
		int endX = Character.getNumericValue(input.charAt(2));
		int endY = Character.getNumericValue(input.charAt(3));

		//Pawn can move diagonally for any number moves
		if(Math.abs(startX-endX) == Math.abs(startY-endY)) {
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

		System.out.println("Invalid move, try again");
		return false;
	}

	@Override
	public ArrayList<Cell> generateLegalMoves(Cell[][] board, int startX, int startY) {
		ArrayList<Cell> result = new ArrayList<Cell>();

		//check up-left diagonal
		for(int deltaX = 1, deltaY = 1; deltaX <= startX; deltaX++, deltaY++) {
			if(deltaY <= startY) {
				//check for empty piece
				if(board[startX-deltaX][startY-deltaY].getPiece() == null) {
					result.add(board[startX-deltaX][startY-deltaY]);
					continue;
				}
				//piece to capture
				else if(board[startX-deltaX][startY-deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					result.add(board[startX-deltaX][startY-deltaY]);
					break;
				}
				//friendly
				else {
					break;
				}
			}
		}

		//check up-right diagonal
		for(int deltaX = 1, deltaY = 1; deltaX <= startX; deltaX++, deltaY++) {
			if(deltaY < Math.abs(board[0].length-startY)) {
				//check for empty piece
				if(board[startX-deltaX][startY+deltaY].getPiece() == null) {
					result.add(board[startX-deltaX][startY+deltaY]);
					continue;
				}
				//piece to capture
				else if(board[startX-deltaX][startY+deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					result.add(board[startX-deltaX][startY+deltaY]);
					break;
				}
				//friendly
				else {
					break;
				}
			}
		}

		//check down-left diagonal
		for(int deltaX = 1, deltaY = 1; deltaX < Math.abs(board.length-startX); deltaX++, deltaY++) {
			if(deltaY <= startY) {
				//check for empty piece
				if(board[startX+deltaX][startY-deltaY].getPiece() == null) {
					result.add(board[startX+deltaX][startY-deltaY]);
					continue;
				}
				//piece to capture
				else if(board[startX+deltaX][startY-deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					result.add(board[startX+deltaX][startY-deltaY]);
					break;
				}
				//friendly
				else {
					break;
				}
			}
		}

		//check down-right diagonal
		for(int deltaX = 1, deltaY = 1; deltaX < Math.abs(board.length-startX); deltaX++, deltaY++) {
			if(deltaY < Math.abs(board[0].length-startY)) {
				//check for empty piece
				if(board[startX+deltaX][startY+deltaY].getPiece() == null) {
					result.add(board[startX+deltaX][startY+deltaY]);
					continue;
				}
				//piece to capture
				else if(board[startX+deltaX][startY+deltaY].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					result.add(board[startX+deltaX][startY+deltaY]);
					break;
				}
				//friendly
				else {
					break;
				}
			}
		}

		return result;
	}
}
