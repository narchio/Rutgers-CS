package Pieces;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Chess.Cell;
import Chess.Color;
import Chess.Logic;
import Chess.Type;

public class King extends Piece{

	public King(Color color, Type type) {
		super(color, type);
	}

	@Override
	public boolean move(Cell[][] board, String input) {
	int startX = Character.getNumericValue(input.charAt(0));
	int startY = Character.getNumericValue(input.charAt(1));
	int endX = Character.getNumericValue(input.charAt(2));
	int endY = Character.getNumericValue(input.charAt(3));

		//king can move one space in all 8 directions
		if((Math.abs(startX-endX) == 1 && Math.abs(startY-endY) == 0) || (Math.abs(startX-endX) == 0 && Math.abs(startY-endY) == 1) || (Math.abs(startX-endX) == 1 && Math.abs(startY-endY) == 1)) {
			//move the piece
			board[endX][endY].setPiece(board[startX][startY].getPiece());
			board[startX][startY].setPiece(null);
			//check if the piece is in checkmate
			if(Logic.isChecked(board, endX, endY) == true) {
				//move the piece back
				board[startX][startY].setPiece(board[endX][endY].getPiece());
				board[endX][endY].setPiece(null);
				System.out.println("Invalid move, try again");
				return false;
			}
			this.updateNumMoves();
			return true;
		}
		//castling move
		else if(Math.abs(startY-endY) == 2 && Math.abs(startX-endX) == 0) {
			if((startX != 0 && startY != 4) || (startX != 7 && startY != 4)) {
				System.out.println("Invalid move, try again");
				return false;
			}
			//check if the piece has moved already
			if(endX == 0 && endY == 2) {
				if(this.getNumMoves() != 0 || board[0][0].getPiece().getNumMoves() != 0) {
					System.out.println("Invalid move, try again");
					return false;
				}
			}
			if(endX == 0 && endY == 6) {
				if(this.getNumMoves() != 0 || board[0][7].getPiece().getNumMoves() != 0) {
					System.out.println("Invalid move, try again");
					return false;
				}
			}
			if(endX == 7 && endY == 2) {
				if(this.getNumMoves() != 0 || board[7][0].getPiece().getNumMoves() != 0) {
					System.out.println("Invalid move, try again");
					return false;
				}
			}
			if(endX == 7 && endY == 6) {
				if(this.getNumMoves() != 0 || board[7][7].getPiece().getNumMoves() != 0) {
					System.out.println("Invalid move, try again");
					return false;
				}
			}
			//check that the path is clear
			if(Logic.clearPath(board, startX, startY, endX, endY)) {
				//castling with right rook -> check that no spaces involve check
				if(startY < endY) {
					//move the piece over one
					board[startX][startY+1].setPiece(board[startX][startY].getPiece());
					board[startX][startY].setPiece(null);
					if(Logic.isChecked(board, startX, startY+1)) {
						board[startX][startY].setPiece(board[startX][startY+1].getPiece());
						board[startX][startY+1].setPiece(null);
						System.out.println("Invalid move, try again");
						return false;
					}
					//move the piece over again
					board[startX][startY+2].setPiece(board[startX][startY+1].getPiece());
					board[startX][startY+1].setPiece(null);
					if(Logic.isChecked(board, startX, startY+2)) {
						board[startX][startY].setPiece(board[startX][startY+2].getPiece());
						board[startX][startY+2].setPiece(null);
						System.out.println("Invalid move, try again");
						return false;
					}
					//move the rook
					if(board[startX][startY+2].getPiece().getColor() == Color.WHITE) {
						board[startX][startY+1].setPiece(board[7][7].getPiece());
						board[7][7].setPiece(null);
						board[startX][startY+1].getPiece().updateNumMoves();
						return true;
					}
					else {
						board[startX][startY+1].setPiece(board[0][7].getPiece());
						board[0][7].setPiece(null);
						board[startX][startY+1].getPiece().updateNumMoves();
						return true;
					}

				}
				//castling with left rook -> check that no spaces involve check
				else {
					//move the piece over one
					board[startX][startY-1].setPiece(board[startX][startY].getPiece());
					board[startX][startY].setPiece(null);
					if(Logic.isChecked(board, startX, startY-1)) {
						board[startX][startY].setPiece(board[startX][startY-1].getPiece());
						board[startX][startY-1].setPiece(null);
						System.out.println("Invalid move, try again");
						return false;
					}
					//move the piece again
					board[startX][startY-2].setPiece(board[startX][startY-1].getPiece());
					board[startX][startY-1].setPiece(null);
					if(Logic.isChecked(board, startX, startY-2)) {
						board[startX][startY].setPiece(board[startX][startY-2].getPiece());
						board[startX][startY-2].setPiece(null);
						System.out.println("Invalid move, try again");
						return false;
					}
					//move the rook
					if(board[startX][startY-2].getPiece().getColor() == Color.WHITE) {
						board[startX][startY-1].setPiece(board[7][0].getPiece());
						board[7][0].setPiece(null);
						board[startX][startY-1].getPiece().updateNumMoves();
						return true;
					}
					else {
						board[startX][startY-1].setPiece(board[0][0].getPiece());
						board[0][0].setPiece(null);
						board[startX][startY-1].getPiece().updateNumMoves();
						return true;
					}

				}
			}
			else {
				System.out.println("Invalid move, try again");
				return false;
			}
		}
		else {
			System.out.println("Invalid move, try again");
			return false;
		}
	}

	@Override
	public ArrayList<Cell> generateLegalMoves(Cell[][] board, int startX, int startY) {
		ArrayList<Cell> result = new ArrayList<Cell>();
		Cell temp = new Cell(Color.DARK_BROWN,0, 0, 0, 0, 0, 0);


		//check the surrounding eight spots
		//check up
		if(startX-1 >= 0) {
			temp.copyCell(board[startX-1][startY]);
			//check if the spot is free or if there is a piece to take
			if(board[startX-1][startY].getPiece() == null || (board[startX-1][startY].getPiece() != null && (board[startX-1][startY].getPiece().getColor() != board[startX][startY].getPiece().getColor()))) {
				//move the piece to check if it puts the king into check
				board[startX-1][startY].setPiece(board[startX][startY].getPiece());
				board[startX][startY].setPiece(null);
				if(!Logic.isChecked(board, startX-1, startY)) {
					result.add(board[startX-1][startY]);
				}
				//move the piece back
				board[startX][startY].setPiece(board[startX-1][startY].getPiece());
				if (temp.getPiece() == null) {
					board[startX-1][startY].setPiece(null);
				} else {
					board[startX-1][startY].setPiece(temp.getPiece());
				}
			}
		}
		//check down
		if(startX+1 < board.length) {
			temp.copyCell(board[startX+1][startY]);
			//check if the spot is free or if there is a piece to take
			if(board[startX+1][startY].getPiece() == null || (board[startX+1][startY].getPiece() != null && (board[startX+1][startY].getPiece().getColor() != board[startX][startY].getPiece().getColor()))) {
				//move the piece to check if it puts the king into check
				board[startX+1][startY].setPiece(board[startX][startY].getPiece());
				board[startX][startY].setPiece(null);
				if(!Logic.isChecked(board, startX+1, startY)) {
					result.add(board[startX+1][startY]);
				}
				//move the piece back
				board[startX][startY].setPiece(board[startX+1][startY].getPiece());
				if (temp.getPiece() == null) {
					board[startX+1][startY].setPiece(null);
				} else {
					board[startX+1][startY].setPiece(temp.getPiece());
				}
			}
		}
		//check left
		if(startY-1 >= 0) {
			temp.copyCell(board[startX][startY-1]);
			//check if the spot is free or if there is a piece to take
			if(board[startX][startY-1].getPiece() == null || (board[startX][startY-1].getPiece() != null && (board[startX][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()))) {
				//move the piece to check if it puts the king into check
				board[startX][startY-1].setPiece(board[startX][startY].getPiece());
				board[startX][startY].setPiece(null);
				if(!Logic.isChecked(board, startX, startY-1)) {
					result.add(board[startX][startY-1]);
				}
				//move the piece back
				board[startX][startY].setPiece(board[startX][startY-1].getPiece());
				if (temp.getPiece() == null) {
					board[startX][startY-1].setPiece(null);
				} else {
					board[startX][startY-1].setPiece(temp.getPiece());
				}
			}
		}
		//check right
		if(startY+1 < board[0].length) {
			temp.copyCell(board[startX][startY+1]);
			//check if the spot is free or if there is a piece to take
			if(board[startX][startY+1].getPiece() == null || (board[startX][startY+1].getPiece() != null && (board[startX][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()))) {
				//move the piece to check if it puts the king into check
				board[startX][startY+1].setPiece(board[startX][startY].getPiece());
				board[startX][startY].setPiece(null);
				if(!Logic.isChecked(board, startX, startY+1)) {
					result.add(board[startX][startY+1]);
				}
				//move the piece back
				board[startX][startY].setPiece(board[startX][startY+1].getPiece());
				if (temp.getPiece() == null) {
					board[startX][startY+1].setPiece(null);
				} else {
					board[startX][startY+1].setPiece(temp.getPiece());
				}
			}
		}
		//check up-left
		if(startX-1 >= 0 && startY-1 >= 0) {
			temp.copyCell(board[startX-1][startY-1]);
			//check if the spot is free or if there is a piece to take
			if(board[startX-1][startY-1].getPiece() == null || (board[startX-1][startY-1].getPiece() != null && (board[startX-1][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()))) {
				//move the piece to check if it puts the king into check
				board[startX-1][startY-1].setPiece(board[startX][startY].getPiece());
				board[startX][startY].setPiece(null);
				if(!Logic.isChecked(board, startX-1, startY-1)) {
					result.add(board[startX-1][startY-1]);
				}
				//move the piece back
				board[startX][startY].setPiece(board[startX-1][startY-1].getPiece());
				if (temp.getPiece() == null) {
					board[startX-1][startY-1].setPiece(null);
				} else {
					board[startX-1][startY-1].setPiece(temp.getPiece());
				}

			}
		}
		//check up-right
		if(startX-1 >= 0 && startY+1 < board[0].length) {
			temp.copyCell(board[startX-1][startY+1]);
			//check if the spot is free or if there is a piece to take
			if(board[startX-1][startY+1].getPiece() == null || (board[startX-1][startY+1].getPiece() != null && (board[startX-1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()))) {
				//move the piece to check if it puts the king into check
				board[startX-1][startY+1].setPiece(board[startX][startY].getPiece());
				board[startX][startY].setPiece(null);
				if(!Logic.isChecked(board, startX-1, startY+1)) {
					result.add(board[startX-1][startY+1]);
				}
				//move the piece back
				board[startX][startY].setPiece(board[startX-1][startY+1].getPiece());
				if (temp.getPiece() == null) {
					board[startX-1][startY+1].setPiece(null);
				} else {
					board[startX-1][startY+1].setPiece(temp.getPiece());
				}
			}
		}
		//check down-left
		if(startX+1 < board.length && startY-1 >= 0) {
			temp.copyCell(board[startX+1][startY-1]);
			//check if the spot is free or if there is a piece to take
			if(board[startX+1][startY-1].getPiece() == null || (board[startX+1][startY-1].getPiece() != null && (board[startX+1][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()))) {
				//move the piece to check if it puts the king into check
				board[startX+1][startY-1].setPiece(board[startX][startY].getPiece());
				board[startX][startY].setPiece(null);
				if(!Logic.isChecked(board, startX+1, startY-1)) {
					result.add(board[startX+1][startY-1]);
				}
				//move the piece back
				board[startX][startY].setPiece(board[startX+1][startY-1].getPiece());
				if (temp.getPiece() == null) {
					board[startX+1][startY-1].setPiece(null);
				} else {
					board[startX+1][startY-1].setPiece(temp.getPiece());
				}
			}
		}
		//check down-right
		if(startX+1 < board.length && startY+1 < board[0].length) {
			temp.copyCell(board[startX+1][startY+1]);
			//check if the spot is free or if there is a piece to take
			if(board[startX+1][startY+1].getPiece() == null || (board[startX+1][startY+1].getPiece() != null && (board[startX+1][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()))) {
				//move the piece to check if it puts the king into check
				board[startX+1][startY+1].setPiece(board[startX][startY].getPiece());
				board[startX][startY].setPiece(null);
				if(!Logic.isChecked(board, startX+1, startY+1)) {
					result.add(board[startX+1][startY+1]);
				}
				//move the piece back
				board[startX][startY].setPiece(board[startX+1][startY+1].getPiece());
				if (temp.getPiece() == null) {
					board[startX+1][startY+1].setPiece(null);
				} else {
					board[startX+1][startY+1].setPiece(temp.getPiece());
				}
			}
		}
		//check castling
		if((startX == 0 && startY == 4) && board[startX][startY].getPiece().getNumMoves() == 0) {
			//try castling to the left
			if(board[0][0].getPiece() != null && board[0][0].getPiece().getPieceType() == Type.ROOK && board[0][0].getPiece().getColor() == board[startX][startY].getPiece().getColor() && board[0][0].getPiece().getNumMoves() == 0) {
				//check if there is a clear path
				if(Logic.clearPath(board, startX, startY, 0, 0)) {
					temp.copyCell(board[startX][startY-1]);
					//check if castling puts the king into check at all
					board[startX][startY-1].setPiece(board[startX][startY].getPiece());
					board[startX][startY].setPiece(null);
					if(!Logic.isChecked(board, startX, startY-1)) {
						temp.copyCell(board[startX][startY-2]);
						board[startX][startY-2].setPiece(board[startX][startY-1].getPiece());
						board[startX][startY-1].setPiece(null);
						if(!Logic.isChecked(board, startX, startY-2)) {
							result.add(board[startX][startY-2]);
							//move the piece back
							board[startX][startY-1].setPiece(board[startX][startY-2].getPiece());
							if (temp.getPiece() == null) {
								board[startX][startY-2].setPiece(null);
							} else {
								board[startX][startY-2].setPiece(temp.getPiece());
							}
						}
					}
					board[startX][startY].setPiece(board[startX][startY-1].getPiece());
					if (temp.getPiece() == null) {
						board[startX][startY-1].setPiece(null);
					} else {
						board[startX][startY-1].setPiece(temp.getPiece());
					}
				}
			}
			//try castling to the right
			if(board[0][7].getPiece() != null && board[0][7].getPiece().getPieceType() == Type.ROOK && board[0][7].getPiece().getColor() == board[startX][startY].getPiece().getColor() && board[0][7].getPiece().getNumMoves() == 0) {
				//check if there is a clear path
				if(Logic.clearPath(board, startX, startY, 0, 7)) {
					temp.copyCell(board[startX][startY+1]);
					//check if castling puts the king into check at all
					board[startX][startY+1].setPiece(board[startX][startY].getPiece());
					board[startX][startY].setPiece(null);
					if(!Logic.isChecked(board, startX, startY+1)) {
						temp.copyCell(board[startX][startY+2]);
						board[startX][startY+2].setPiece(board[startX][startY+1].getPiece());
						board[startX][startY+1].setPiece(null);
						if(!Logic.isChecked(board, startX, startY+2)) {
							result.add(board[startX][startY+2]);
							//move the piece back
							board[startX][startY+1].setPiece(board[startX][startY+2].getPiece());
							if (temp.getPiece() == null) {
								board[startX][startY+2].setPiece(null);
							} else {
								board[startX][startY+2].setPiece(temp.getPiece());
							}
						}
					}
					board[startX][startY].setPiece(board[startX][startY+1].getPiece());
					if (temp.getPiece() == null) {
						board[startX][startY+1].setPiece(null);
					} else {
						board[startX][startY+1].setPiece(temp.getPiece());
					}
				}
			}
		}
		if((startX == 7 && startY == 4) && board[startX][startY].getPiece().getNumMoves() == 0) {
			//try castling to the left
			if(board[7][0].getPiece() != null && board[7][0].getPiece().getPieceType() == Type.ROOK && board[7][0].getPiece().getColor() == board[startX][startY].getPiece().getColor() && board[7][0].getPiece().getNumMoves() == 0) {
				//check if there is a clear path
				if(Logic.clearPath(board, startX, startY, 7, 0)) {
					//check if castling puts the king into check at all
					temp.copyCell(board[startX][startY-1]);
					board[startX][startY-1].setPiece(board[startX][startY].getPiece());
					board[startX][startY].setPiece(null);
					if(!Logic.isChecked(board, startX, startY-1)) {
						temp.copyCell(board[startX][startY-2]);
						board[startX][startY-2].setPiece(board[startX][startY-1].getPiece());
						board[startX][startY-1].setPiece(null);
						if(!Logic.isChecked(board, startX, startY-2)) {
							result.add(board[startX][startY-2]);
							//move the piece back
							board[startX][startY-1].setPiece(board[startX][startY-2].getPiece());
							if (temp.getPiece() == null) {
								board[startX][startY-2].setPiece(null);
							} else {
								board[startX][startY-2].setPiece(temp.getPiece());
							}
						}
					}
					board[startX][startY].setPiece(board[startX][startY-1].getPiece());
					if (temp.getPiece() == null) {
						board[startX][startY-1].setPiece(null);
					} else {
						board[startX][startY-1].setPiece(temp.getPiece());
					}
				}
			}
			//try castling to the right
			if(board[7][7].getPiece() != null && board[7][7].getPiece().getPieceType() == Type.ROOK && board[7][7].getPiece().getColor() == board[startX][startY].getPiece().getColor() && board[7][7].getPiece().getNumMoves() == 0) {
				//check if there is a clear path
				if(Logic.clearPath(board, startX, startY, 7, 7)) {
					//check if castling puts the king into check at all
					temp.copyCell(board[startX][startY+1]);
					board[startX][startY+1].setPiece(board[startX][startY].getPiece());
					board[startX][startY].setPiece(null);
					if(!Logic.isChecked(board, startX, startY+1)) {
						temp.copyCell(board[startX][startY+2]);
						board[startX][startY+2].setPiece(board[startX][startY+1].getPiece());
						board[startX][startY+1].setPiece(null);
						if(!Logic.isChecked(board, startX, startY+2)) {
							result.add(board[startX][startY+2]);
							//move the piece back
							board[startX][startY+1].setPiece(board[startX][startY+2].getPiece());
							if (temp.getPiece() == null) {
								board[startX][startY+2].setPiece(null);
							} else {
								board[startX][startY+2].setPiece(temp.getPiece());
							}
						}
					}
					board[startX][startY].setPiece(board[startX][startY+1].getPiece());
					if (temp.getPiece() == null) {
						board[startX][startY+1].setPiece(null);
					} else {
						board[startX][startY+1].setPiece(temp.getPiece());
					}
				}
			}
		}
		return result;
	}
}



