package Chess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

/**
 * The class Logic contains methods used during a game of chess such as mapping file-rank to array indices, identifying player input, checking for check and checkmate, etc...
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class Logic {
	/**
	 * Mapping of letters to integer column indices for the chess board.
	 */
	public static final HashMap<Character, Integer> COLUMN_MAP = new HashMap<Character, Integer>(){{
		put('a', 0);
		put('b', 1);
		put('c', 2);
		put('d', 3);
		put('e', 4);
		put('f', 5);
		put('g', 6);
		put('h', 7);
	}};
	/**
	 * Mapping of numbers to integer row indices for the chess board.
	 */
	public static final HashMap<Integer, Integer> ROW_MAP = new HashMap<Integer, Integer>(){{
		put(8, 0);
		put(7, 1);
		put(6, 2);
		put(5, 3);
		put(4, 4);
		put(3, 5);
		put(2, 6);
		put(1, 7);
	}};
	/**
	 * Returns the cell instance of a chess board that contains the black king.
	 * @param board The chess board containing the black king.
	 * @return Cell containing the black king.
	 */
	public static Cell getBlackKing(Cell[][] board) {
		for(int i = 0; i < board.length-1; i++) {
			for(int j = 0; j < board[0].length-1; j++) {
				if(board[i][j].getPiece() != null && board[i][j].getPiece().getPieceType() == Type.KING && board[i][j].getPiece().getColor() == Color.BLACK) {
					return board[i][j];
				}
			}
		}
		return null;
	}
	/**
	 * Returns the cell instance of a chess board that contains the white king.
	 * @param board The chess board containing the white king.
	 * @return Cell containing the white king.
	 */
	public static Cell getWhiteKing(Cell[][] board) {
		for(int i = 0; i < board.length-1; i++) {
			for(int j = 0; j < board[0].length-1; j++) {
				if(board[i][j].getPiece() != null && board[i][j].getPiece().getPieceType() == Type.KING && board[i][j].getPiece().getColor() == Color.WHITE) {
					return board[i][j];
				}
			}
		}
		return null;
	}
	/**
	 * Determines whether user input is a normal move.
	 * @param input The text inputted by the user.
	 * @return True if input is a normal move, false otherwise.
	 */
	public static boolean isNormalMove(String input) {
		return input.matches("[a-h][1-8] [a-h][1-8]");
	}
	/**
	 * Determines whether user input is a specified pawn promotion move.
	 * @param input The text inputted by the user.
	 * @return True if input is a specified pawn promotion move, false otherwise.
	 */
	public static boolean isPawnPromotion(String input) {
		return input.matches("[a-h][1-8] [a-h][1-8] [RBNQ]");
	}
	/**
	 * Determines whether user input is a resignation move.
	 * @param input The text inputted by the user.
	 * @return True if input is a resignation move, false otherwise.
	 */
	public static boolean isResignation(String input) {
		return input.equals("resign");
	}
	/**
	 * Determines whether user input is a draw offer.
	 * @param input The text inputted by the user.
	 * @return True if input is a draw offer, false otherwise.
	 */
	public static boolean isDrawOffer(String input) {
		return input.contains("draw?");
	}
	/**
	 * Determines whether user input is a draw acceptance.
	 * @param input The text inputted by the user.
	 * @return True if input is a draw offer, false otherwise.
	 */
	public static boolean isDrawAcceptance(String input) {
		return input.equals("draw");
	}
	/**
	 * Determines whether an integer index is valid and in bounds in respect to the chess board.
	 * @param n The index to be checked.
	 * @return True if index is valid and in bounds, false otherwise.
	 */
	public static boolean isInBounds(int n) {
		return ((n >= 0) && (n <= 7)) ? true: false; 
	}
	/**
	 * Determines whether user input is a castling move.
	 * @param board The chess board.
	 * @param input THe text inputted by the user.
	 * @return True if input is a castling move, false otherwise.
	 */
	public static boolean isCastling(Cell[][] board, String input) {
		int startX = Logic.ROW_MAP.get(Integer.parseInt(input.substring(1, 2)));
		int startY = Logic.COLUMN_MAP.get(input.charAt(0));
		int endX = Logic.ROW_MAP.get(Integer.parseInt(input.substring(4, 5)));
		int endY = Logic.COLUMN_MAP.get(input.charAt(3));
		
		//check if king is in starting position 
		if(startX == 0 && startY == 4 && board[startX][startY].getPiece() != null && board[startX][startY].getPiece().getPieceType() == Type.KING && board[startX][startY].getPiece().getNumMoves() == 0) {
			//check to make sure rook is available 
			if(board[0][0].getPiece() != null && board[0][0].getPiece().getPieceType() == Type.ROOK && board[0][0].getPiece().getColor() == board[startX][startY].getPiece().getColor() && board[0][0].getPiece().getNumMoves() == 0) {
				//check to make sure move is two spaces
				if(Math.abs(startX-endX) == 0 && Math.abs(startY-endY) == 2) {
					return true;
				}
			}
			if(board[0][7].getPiece() != null && board[0][7].getPiece().getPieceType() == Type.ROOK && board[0][7].getPiece().getColor() == board[startX][startY].getPiece().getColor() && board[0][7].getPiece().getNumMoves() == 0) {
				//check to make sure move is two spaces
				if(Math.abs(startX-endX) == 0 && Math.abs(startY-endY) == 2) {
					return true;
				}
			}
		}
		if(startX == 7 && startY == 4 && board[startX][startY].getPiece() != null && board[startX][startY].getPiece().getPieceType() == Type.KING && board[startX][startY].getPiece().getNumMoves() == 0) {
			//check to make sure rook is available 
			if(board[7][0].getPiece() != null && board[7][0].getPiece().getPieceType() == Type.ROOK && board[7][0].getPiece().getColor() == board[startX][startY].getPiece().getColor() && board[7][0].getPiece().getNumMoves() == 0) {
				//check to make sure move is two spaces
				if(Math.abs(startX-endX) == 0 && Math.abs(startY-endY) == 2) {
					return true;
				}
			}
			if(board[7][7].getPiece() != null && board[7][7].getPiece().getPieceType() == Type.ROOK && board[7][7].getPiece().getColor() == board[startX][startY].getPiece().getColor() && board[7][7].getPiece().getNumMoves() == 0) {
				//check to make sure move is two spaces
				if(Math.abs(startX-endX) == 0 && Math.abs(startY-endY) == 2) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Determines whether user input is an enpessant move.
	 * @param board The chess board.
	 * @param input The text inputted by the user.
	 * @return True if input is an enpessant move, false otherwise.
	 */
	public static boolean isEnpassant(Cell[][] board, String input) {
		int startX = Logic.ROW_MAP.get(Integer.parseInt(input.substring(1, 2)));
		int startY = Logic.COLUMN_MAP.get(input.charAt(0));
		int endX = Logic.ROW_MAP.get(Integer.parseInt(input.substring(4, 5)));
		int endY = Logic.COLUMN_MAP.get(input.charAt(3));
		
		if(Chess.moves.size() > 0) {
			String lastMove = Chess.moves.get(Chess.moves.size()-1);
			int lmStartX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(1, 2)));
			int lmStartY = Logic.COLUMN_MAP.get(lastMove.charAt(0));
			int lmEndX = Logic.ROW_MAP.get(Integer.parseInt(lastMove.substring(4, 5)));
			int lmEndY = Logic.COLUMN_MAP.get(lastMove.charAt(3));
			
			//make sure starting piece is a pawn
			if(board[startX][startY].getPiece().getPieceType() == Type.PAWN) {
				//check if there is a pawn to the left or right 
				if(startY-1 >= 0 && board[startX][startY-1].getPiece() != null && board[startX][startY-1].getPiece().getPieceType() == Type.PAWN && board[startX][startY-1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					if(lmEndY == startY-1) {
						return true;
					}
				}
				if(startY+1 < board.length-1 && board[startX][startY+1].getPiece() != null && board[startX][startY+1].getPiece().getPieceType() == Type.PAWN && board[startX][startY+1].getPiece().getColor() != board[startX][startY].getPiece().getColor()) {
					if(lmEndY == startY+1) {
						return true;
					}
				}
			}
		}
		
		return false;
		
	}
	/**
	 * Determines whether there is a clear path between two cells
	 * @param board The chess board.
	 * @param startX The x-coordinate of the starting cell.
	 * @param startY The y-coordinate of the starting cell.
	 * @param endX The x-coordinate of the ending cell.
	 * @param endY The y-coordinate of the ending cell.
	 * @return True if there is a clear path, false otherwise.
	 */
	public static boolean clearPath(Cell[][] board, int startX, int startY, int endX, int endY) {
		boolean clearPath = true;
		//case 1: moving horizontally
		if(Math.abs(startX-endX) == 0 && Math.abs(startY-endY) != 0) {
			int direction = endY > startY ? 1 : -1;
			for(int i = 1; i < Math.abs(startY-endY); i++) {
				if(board[startX][startY+(i*direction)].getPiece() == null) {
					continue;
				}
				else {
					clearPath = false;
					break;
				}
			}
		}
		//case 2: moving vertically 
		else if(Math.abs(startX-endX) != 0 && Math.abs(startY-endY) == 0) {
			int direction = endX > startX ? 1 : -1;
			for(int i = 1; i < Math.abs(startX-endX); i++) {
				if(board[startX+(i*direction)][startY].getPiece() == null) {
					continue;
				}
				else {
					clearPath = false;
					break;
				}
			}
		}
		//case 3: moving diagonally 
		else if(Math.abs(startX-endX) == Math.abs(startY-endY)) {
			int hDirection = endY > startY ? 1 : -1;
			int vDirection = endX > startX ? 1 : -1;
			for(int i = 1; i < Math.abs(startY-endY); i++) {
				if(board[startX+(i*vDirection)][startY+(i*hDirection)].getPiece() == null) {
					continue;
				}
				else {
					clearPath = false;
					break;
				}
			}
		}
		return clearPath;
	}
	/**
	 * Returns the list of cells in the path from the piece checking the king to the king itself.
	 * @param board The chess board.
	 * @param startX The x-coordinate of the king.
	 * @param startY The y-coordinate of the king.
	 * @param endX The x-coordinate of the checking cell.
	 * @param endY The y-coordinate of the checking cell.
	 * @return List of cells.
	 */
	public static ArrayList<Cell> getCheckingPath(Cell[][] board, int startX, int startY, int endX, int endY){
		ArrayList<Cell> path = new ArrayList<Cell>();
		
		if(board[endX][endY].getPiece().getPieceType() == Type.KNIGHT) {
			path.add(board[endX][endY]);
			return path;
		}
		
		//case 1: moving horizontally
		if(Math.abs(startX-endX) == 0 && Math.abs(startY-endY) != 0) {
			int direction = endY > startY ? 1 : -1;
			for(int i = 1; i < Math.abs(startY-endY); i++) {
				if(board[startX][startY+(i*direction)].getPiece() == null) {
					path.add(board[startX][startY+(i*direction)]);
				}
				else {
					
					break;
				}
			}
		}
		
		
		//case 2: moving vertically 
		else if(Math.abs(startX-endX) != 0 && Math.abs(startY-endY) == 0) {
			int direction = endX > startX ? 1 : -1;
			for(int i = 1; i < Math.abs(startX-endX); i++) {
				if(board[startX+(i*direction)][startY].getPiece() == null) {
					path.add(board[startX+(i*direction)][startY]);
				}
				else {
					break;
				}
			}
		}
		//case 3: moving diagonally 
		else if(Math.abs(startX-endX) == Math.abs(startY-endY)) {
			int hDirection = endY > startY ? 1 : -1;
			int vDirection = endX > startX ? 1 : -1;
			for(int i = 1; i < Math.abs(startY-endY); i++) {
				if(board[startX+(i*vDirection)][startY+(i*hDirection)].getPiece() == null) {
					path.add(board[startX+(i*vDirection)][startY+(i*hDirection)]);
				}
				else {
					break;
				}
			}
		}
		path.add(board[endX][endY]);
		return path;
	}
	/**
	 * Determines whether a king is being checked by the opponent.
	 * @param board The chess board.
	 * @param x The x-coordinate of the king.
	 * @param y The y-coordinate of the king.
	 * @return True if the king is being checked, false otherwise.
	 */
	public static boolean isChecked(Cell[][] board, int x, int y) {
		
		//check up 
		for(int i = 1; i <= x; i++) {
			//there is a piece in the cell
			if(board[x-i][y].getPiece() != null) {
				//check if friendly piece
				if(board[x-i][y].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					//check if rook/queen
					if(board[x-i][y].getPiece().getPieceType() == Type.ROOK || board[x-i][y].getPiece().getPieceType() == Type.QUEEN) {
						return true;
					}
					else {
						break;
					}
				}
			}
			//empty spot
			else {
				continue;
			}
		}
		
		//check down 
		for(int i = 1; i < Math.abs(board.length-1-x); i++) {
			//there is a piece in the cell
			if(board[x+i][y].getPiece() != null) {
				//check if friendly piece
				if(board[x+i][y].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					//check if rook/queen
					if(board[x+i][y].getPiece().getPieceType() == Type.ROOK || board[x+i][y].getPiece().getPieceType() == Type.QUEEN) {
						return true;
					}
					else {
						break;
					}
				}
			}
			//empty spot
			else {
				continue;
			}
		}
		
		//check left 
		for(int i = 1; i <= y; i++) {
			//there is a piece in the cell
			if(board[x][y-i].getPiece() != null) {
				//check if friendly piece
				if(board[x][y-i].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					//check if rook/queen
					if(board[x][y-i].getPiece().getPieceType() == Type.ROOK || board[x][y-i].getPiece().getPieceType() == Type.QUEEN) {
						return true;
					}
					else {
						break;
					}
				}
			}
			//empty spot
			else {
				continue;
			}
		}
		
		//check right 
		for(int i = 1; i < Math.abs(board[0].length-1-y); i++) {
			//there is a piece in the cell
			if(board[x][y+i].getPiece() != null) {
				//check if friendly piece
				if(board[x][y+i].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					//check if rook/queen
					if(board[x][y+i].getPiece().getPieceType() == Type.ROOK || board[x][y+i].getPiece().getPieceType() == Type.QUEEN) {
						return true;
					}
					else {
						break;
					}
				}
			}
			//empty spot
			else {
				continue;
			}
		}
		
		//check up-left 
		int xCopy = x-1, yCopy = y-1;
		while(xCopy >= 0 && yCopy >= 0) {
			//there is a piece in the cell
			if(board[xCopy][yCopy].getPiece() != null) {
				//check if friendly piece
				if(board[xCopy][yCopy].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					if(board[xCopy][yCopy].getPiece().getPieceType() == Type.BISHOP || board[xCopy][yCopy].getPiece().getPieceType() == Type.QUEEN) {
						return true;
					}
					else {
						break;
					}
				}
			}
			else {
				xCopy--;
				yCopy--;
				continue;
			}
		}
		
		//check up-right
		xCopy = x-1;
		yCopy = y+1;
		while(xCopy >= 0 && yCopy < board[0].length-1) {
			//there is a piece in the cell
			if(board[xCopy][yCopy].getPiece() != null) {
				//check if friendly piece
				if(board[xCopy][yCopy].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					if(board[xCopy][yCopy].getPiece().getPieceType() == Type.BISHOP || board[xCopy][yCopy].getPiece().getPieceType() == Type.QUEEN) {
						return true;
					}
					else {
						break;
					}
				}
			}
			else {
				xCopy--;
				yCopy++;
				continue;
			}
		}
		
		//check down-left
		xCopy = x+1;
		yCopy = y-1;
		while(xCopy < board.length-1 && yCopy >= 0) {
			//there is a piece in the cell
			if(board[xCopy][yCopy].getPiece() != null) {
				//check if friendly piece
				if(board[xCopy][yCopy].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					if(board[xCopy][yCopy].getPiece().getPieceType() == Type.BISHOP || board[xCopy][yCopy].getPiece().getPieceType() == Type.QUEEN) {
						return true;
					}
					else {
						break;
					}
				}
			}
			else {
				xCopy++;
				yCopy--;
				continue;
			}
		}
		
		//check down-right 
		xCopy = x+1; 
		yCopy = y+1;
		while(xCopy < board.length-1 && yCopy < board.length-1) {
			//there is a piece in the cell
			if(board[xCopy][yCopy].getPiece() != null) {
				//check if friendly piece
				if(board[xCopy][yCopy].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					if(board[xCopy][yCopy].getPiece().getPieceType() == Type.BISHOP || board[xCopy][yCopy].getPiece().getPieceType() == Type.QUEEN) {
						return true;
					}
					else {
						break;
					}
				}
			}
			else {
				xCopy++;
				yCopy++;
				continue;
			}
		}
		
		//check that no pawns can take the king
		if(board[x][y].getPiece().getColor() == Color.WHITE) {
			//check the two directly above 
			if((board[x-1][y-1].getPiece() != null && board[x-1][y-1].getPiece().getColor() == Color.BLACK && board[x-1][y-1].getPiece().getPieceType() == Type.PAWN) || (board[x-1][y+1].getPiece() != null && board[x-1][y+1].getPiece().getColor() == Color.BLACK && board[x-1][y+1].getPiece().getPieceType() == Type.PAWN)) {
				return true;
			}
		}
		if(board[x][y].getPiece().getColor() == Color.BLACK) {
			//check the two directly above 
			if((board[x+1][y-1].getPiece() != null && board[x+1][y-1].getPiece().getColor() == Color.WHITE && board[x+1][y-1].getPiece().getPieceType() == Type.PAWN) || (board[x+1][y+1].getPiece() != null && board[x+1][y+1].getPiece().getColor() == Color.WHITE && board[x+1][y+1].getPiece().getPieceType() == Type.PAWN)) {
				return true;
			}
		}
		
		//check if knight can take it 
		if(x-2 >= 0 && x-2 < board.length-1 && y-1 >= 0 && y-1 < board.length-1) {
			if(board[x-2][y-1].getPiece() != null && board[x-2][y-1].getPiece().getPieceType() == Type.KNIGHT && board[x-2][y-1].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return true;
			}
		}
		if(x-2 >= 0 && x-2 < board.length-1 && y+1 >= 0 && y+1 < board.length-1) {
			if(board[x-2][y+1].getPiece() != null && board[x-2][y+1].getPiece().getPieceType() == Type.KNIGHT && board[x-2][y+1].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return true;
			}
		}		
		if(x-1 >= 0 && x-1 < board.length-1 && y-2 >= 0 && y-2 < board.length-1) {
			if(board[x-1][y-2].getPiece() != null && board[x-1][y-2].getPiece().getPieceType() == Type.KNIGHT && board[x-1][y-2].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return true;
			}
		}
		if(x-1 >= 0 && x-1 < board.length-1 && y+2 >= 0 && y+2 < board.length-1) {
			if(board[x-1][y+2].getPiece() != null && board[x-1][y+2].getPiece().getPieceType() == Type.KNIGHT && board[x-1][y+2].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return true;
			}
		}
		if(x+1 >= 0 && x+1 < board.length-1 && y-2 >= 0 && y-2 < board.length-1) {
			if(board[x+1][y-2].getPiece() != null && board[x+1][y-2].getPiece().getPieceType() == Type.KNIGHT && board[x+1][y-2].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return true;
			}
		}
		if(x+1 >= 0 && x+1 < board.length-1 && y+2 >= 0 && y+2 < board.length-1) {
			if(board[x+1][y+2].getPiece() != null && board[x+1][y+2].getPiece().getPieceType() == Type.KNIGHT && board[x+1][y+2].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return true;
			}
		}
		if(x+2 >= 0 && x+2 < board.length-1 && y-1 >= 0 && y-1 < board.length-1) {
			if(board[x+2][y-1].getPiece() != null && board[x+2][y-1].getPiece().getPieceType() == Type.KNIGHT && board[x+2][y-1].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return true;
			}
		}
		if(x+2 >= 0 && x+2 < board.length-1 && y+1 >= 0 && y+1 < board.length-1) {
			if(board[x+2][y+1].getPiece() != null && board[x+2][y+1].getPiece().getPieceType() == Type.KNIGHT && board[x+2][y+1].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Returns the cell that is putting a king in check.
	 * @param board The chess board.
	 * @param x The x-coordinate of the king.
	 * @param y The y-coordinate of the king.
	 * @return Cell that is checking a king.
	 */
	public static Cell getCheckingCell(Cell[][] board, int x, int y) {
		
		//check up 
		for(int i = 1; i <= x; i++) {
			//there is a piece in the cell
			if(board[x-i][y].getPiece() != null) {
				//check if friendly piece
				if(board[x-i][y].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					//check if rook/queen
					if(board[x-i][y].getPiece().getPieceType() == Type.ROOK || board[x-i][y].getPiece().getPieceType() == Type.QUEEN) {
						return board[x-i][y];
					}
					else {
						break;
					}
				}
			}
			//empty spot
			else {
				continue;
			}
		}
		
		//check down 
		for(int i = 1; i < Math.abs(board.length-1-x); i++) {
			//there is a piece in the cell
			if(board[x+i][y].getPiece() != null) {
				//check if friendly piece
				if(board[x+i][y].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					//check if rook/queen
					if(board[x+i][y].getPiece().getPieceType() == Type.ROOK || board[x+i][y].getPiece().getPieceType() == Type.QUEEN) {
						return board[x+i][y];
					}
					else {
						break;
					}
				}
			}
			//empty spot
			else {
				continue;
			}
		}
		
		//check left 
		for(int i = 1; i <= y; i++) {
			//there is a piece in the cell
			if(board[x][y-i].getPiece() != null) {
				//check if friendly piece
				if(board[x][y-i].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					//check if rook/queen
					if(board[x][y-i].getPiece().getPieceType() == Type.ROOK || board[x][y-i].getPiece().getPieceType() == Type.QUEEN) {
						return board[x][y-i];
					}
					else {
						break;
					}
				}
			}
			//empty spot
			else {
				continue;
			}
		}
		
		//check right 
		for(int i = 1; i < Math.abs(board[0].length-1-y); i++) {
			//there is a piece in the cell
			if(board[x][y+i].getPiece() != null) {
				//check if friendly piece
				if(board[x][y+i].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					//check if rook/queen
					if(board[x][y+i].getPiece().getPieceType() == Type.ROOK || board[x][y+i].getPiece().getPieceType() == Type.QUEEN) {
						return board[x][y+i];
					}
					else {
						break;
					}
				}
			}
			//empty spot
			else {
				continue;
			}
		}
		
		//check up-left 
		int xCopy = x-1, yCopy = y-1;
		while(xCopy >= 0 && yCopy >= 0) {
			//there is a piece in the cell
			if(board[xCopy][yCopy].getPiece() != null) {
				//check if friendly piece
				if(board[xCopy][yCopy].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					if(board[xCopy][yCopy].getPiece().getPieceType() == Type.BISHOP || board[xCopy][yCopy].getPiece().getPieceType() == Type.QUEEN) {
						return board[xCopy][yCopy];
					}
					else {
						break;
					}
				}
			}
			else {
				xCopy--;
				yCopy--;
				continue;
			}
		}
		
		//check up-right
		xCopy = x-1;
		yCopy = y+1;
		while(xCopy >= 0 && yCopy < board[0].length-1) {
			//there is a piece in the cell
			if(board[xCopy][yCopy].getPiece() != null) {
				//check if friendly piece
				if(board[xCopy][yCopy].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					if(board[xCopy][yCopy].getPiece().getPieceType() == Type.BISHOP || board[xCopy][yCopy].getPiece().getPieceType() == Type.QUEEN) {
						return board[xCopy][yCopy];
					}
					else {
						break;
					}
				}
			}
			else {
				xCopy--;
				yCopy++;
				continue;
			}
		}
		
		//check down-left
		xCopy = x+1;
		yCopy = y-1;
		while(xCopy < board.length-1 && yCopy >= 0) {
			//there is a piece in the cell
			if(board[xCopy][yCopy].getPiece() != null) {
				//check if friendly piece
				if(board[xCopy][yCopy].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					if(board[xCopy][yCopy].getPiece().getPieceType() == Type.BISHOP || board[xCopy][yCopy].getPiece().getPieceType() == Type.QUEEN) {
						return board[xCopy][yCopy];
					}
					else {
						break;
					}
				}
			}
			else {
				xCopy++;
				yCopy--;
				continue;
			}
		}
		
		//check down-right 
		xCopy = x+1; 
		yCopy = y+1;
		while(xCopy < board.length-1 && yCopy < board.length-1) {
			//there is a piece in the cell
			if(board[xCopy][yCopy].getPiece() != null) {
				//check if friendly piece
				if(board[xCopy][yCopy].getPiece().getColor() == board[x][y].getPiece().getColor()) {
					break;
				}
				else {
					if(board[xCopy][yCopy].getPiece().getPieceType() == Type.BISHOP || board[xCopy][yCopy].getPiece().getPieceType() == Type.QUEEN) {
						return board[xCopy][yCopy];
					}
					else {
						break;
					}
				}
			}
			else {
				xCopy++;
				yCopy++;
				continue;
			}
		}
		
		//check that no pawns can take the king
		if(board[x][y].getPiece().getColor() == Color.WHITE) {
			//check the two directly above 
			if((board[x-1][y-1].getPiece() != null && board[x-1][y-1].getPiece().getColor() == Color.BLACK && board[x-1][y-1].getPiece().getPieceType() == Type.PAWN) || (board[x-1][y+1].getPiece() != null && board[x-1][y+1].getPiece().getColor() == Color.BLACK && board[x-1][y+1].getPiece().getPieceType() == Type.PAWN)) {
				return board[x-1][y-1];
			}
		}
		if(board[x][y].getPiece().getColor() == Color.BLACK) {
			//check the two directly above 
			if((board[x+1][y-1].getPiece() != null && board[x+1][y-1].getPiece().getColor() == Color.WHITE && board[x+1][y-1].getPiece().getPieceType() == Type.PAWN) || (board[x+1][y+1].getPiece() != null && board[x+1][y+1].getPiece().getColor() == Color.WHITE && board[x+1][y+1].getPiece().getPieceType() == Type.PAWN)) {
				return board[x+1][y-1];
			}
		}
		
		//check if knight can take it 
		if(x-2 >= 0 && x-2 < board.length-1 && y-1 >= 0 && y-1 < board.length-1) {
			if(board[x-2][y-1].getPiece() != null && board[x-2][y-1].getPiece().getPieceType() == Type.KNIGHT && board[x-2][y-1].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return board[x-2][y-1];
			}
		}
		if(x-2 >= 0 && x-2 < board.length-1 && y+1 >= 0 && y+1 < board.length-1) {
			if(board[x-2][y+1].getPiece() != null && board[x-2][y+1].getPiece().getPieceType() == Type.KNIGHT && board[x-2][y+1].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return board[x-2][y+1];
			}
		}		
		if(x-1 >= 0 && x-1 < board.length-1 && y-2 >= 0 && y-2 < board.length-1) {
			if(board[x-1][y-2].getPiece() != null && board[x-1][y-2].getPiece().getPieceType() == Type.KNIGHT && board[x-1][y-2].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return board[x-1][y-2];
			}
		}
		if(x-1 >= 0 && x-1 < board.length-1 && y+2 >= 0 && y+2 < board.length-1) {
			if(board[x-1][y+2].getPiece() != null && board[x-1][y+2].getPiece().getPieceType() == Type.KNIGHT && board[x-1][y+2].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return board[x-1][y+2];
			}
		}
		if(x+1 >= 0 && x+1 < board.length-1 && y-2 >= 0 && y-2 < board.length-1) {
			if(board[x+1][y-2].getPiece() != null && board[x+1][y-2].getPiece().getPieceType() == Type.KNIGHT && board[x+1][y-2].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return board[x+1][y-2];
			}
		}
		if(x+1 >= 0 && x+1 < board.length-1 && y+2 >= 0 && y+2 < board.length-1) {
			if(board[x+1][y+2].getPiece() != null && board[x+1][y+2].getPiece().getPieceType() == Type.KNIGHT && board[x+1][y+2].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return board[x+1][y+2];
			}
		}
		if(x+2 >= 0 && x+2 < board.length-1 && y-1 >= 0 && y-1 < board.length-1) {
			if(board[x+2][y-1].getPiece() != null && board[x+2][y-1].getPiece().getPieceType() == Type.KNIGHT && board[x+2][y-1].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return board[x+2][y-1];
			}
		}
		if(x+2 >= 0 && x+2 < board.length-1 && y+1 >= 0 && y+1 < board.length-1) {
			if(board[x+2][y+1].getPiece() != null && board[x+2][y+1].getPiece().getPieceType() == Type.KNIGHT && board[x+2][y+1].getPiece().getColor() != board[x][y].getPiece().getColor()) {
				return board[x+2][y+1];
			}
		}
		return null;
	}
	/**
	 * Generates a list of legal moves for all pieces of a specified color.
	 * @param board The chess board.
	 * @param color The color of pieces to generate moves for.
	 * @return List of cells of the legal moves.
	 */
	public static ArrayList<Cell> generateAllLegalMoves(Cell[][] board, Color color){
		ArrayList<Cell> moves = new ArrayList<Cell>();
		for(Cell[] row: board) {
			for(Cell cell: row) {
				if(cell.getPiece() != null && cell.getPiece().getColor() == color) {
					cell.getPiece().updateLegalMoves(board, cell);
					moves.addAll(cell.getPiece().getLegalMoves());
				}
			}
		}
		return moves;
	}
	/**
	 * Determines whether a king is in check-mate.
	 * @param board The chess board.
	 * @param x The x-coordinate of the king.
	 * @param y The y-coordinate of the king.
	 * @return True if the king is in checkmate, false otherwise.
	 */
	public static boolean checkMate(Cell[][] board, int x, int y) {
		//generate the moves
		ArrayList<Cell> kingMoves = board[x][y].getPiece().generateLegalMoves(board, board[x][y]);
		if(kingMoves.size() > 0) {
			return false;
		}
		//get location where king is being checked from 
		Cell checkingCell = Logic.getCheckingCell(board, x, y);
		//get the path to the checked cell 
		ArrayList<Cell> checkingPath = Logic.getCheckingPath(board, x, y, checkingCell.getX(), checkingCell.getY());
		//get the color of the king being checked
		Color kingColor = board[x][y].getPiece().getColor();
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j].getPiece() != null && board[i][j].getPiece().getColor() == kingColor) {
					ArrayList<Cell> legalMoves = board[i][j].getPiece().generateLegalMoves(board, board[i][j]);
					for(Cell cell: checkingPath) {
						if(legalMoves.contains(cell)) {
							Cell[][] boardCopy = Arrays.stream(board).map(Cell[]::clone).toArray(Cell[][]::new);
							//move the piece 
							boardCopy[cell.getX()][cell.getY()].setPiece(board[i][j].getPiece());
							boardCopy[i][j].setPiece(null);
							//check if still checked 
							if(!Logic.isChecked(boardCopy, x, y)) {
								return false;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
}
