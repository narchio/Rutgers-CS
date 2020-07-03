package Pieces;
import Chess.Color;
import Chess.Type;

import java.util.ArrayList;
import java.util.List;

import Chess.Cell;

/**
 * The class Piece is a general representation of a chess piece.
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public abstract class Piece {
	/**
	 * Identifies the color of the chess piece.
	 */
	private Color color; 
	/**
	 * Identifies the type of the chess piece.
	 */
	private Type pieceType;
	/**
	 * Identifies the number of times the chess piece has been moved.
	 */
	private int numMoves;
	/**
	 * Identifies the legal moves for a chess piece.
	 */
	private List<Cell> legalMoves;
	/**
	 * Creates instance of Piece with the specified color and type.
	 * @param color The color of the chess piece
	 * @param type The type of the chess piece
	 */
	public Piece(Color color, Type type) {
		this.color = color;
		this.pieceType = type;
	}
	/**
	 * Returns the color of a chess piece.
	 * @return Enum variable containing the color of the chess piece.
	 */
	public Color getColor() {
		return this.color;
	}
	/**
	 * Assigns a color to a chess piece.
	 * @param color Enum variable containing color to assign to chess piece.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * Returns the list of legal moves for a chess piece.
	 * @return List of cells representing the legal moves for a chess piece.
	 */
	public List<Cell> getLegalMoves(){
		return this.legalMoves;
	}
	/**
	 * Generates and assigns the legal moves for a chess piece based on its location on the board.
	 * @param board The chess board.
	 * @param start The location of the chess piece.
	 */
	public void updateLegalMoves(Cell[][] board, Cell start) {
		this.legalMoves = this.generateLegalMoves(board, start);
	}
	/**
	 * Increments the number of moves made by a chess piece.
	 */
	public void updateNumMoves() {
		this.numMoves++;
	}
	/**
	 * Returns the number of moves made by a chess piece.
	 * @return Number of moves made by a chess piece.
	 */
	public int getNumMoves() {
		return this.numMoves;
	}
	/**
	 * Returns the type of a chess piece.
	 * @return Type of a chess piece.
	 */
	public Type getPieceType() {
		return this.pieceType;
	}
	/**
	 * Assigns a type to a chess piece
	 * @param type The specified type to assign to a chess piece.
	 */
	public void setPieceType(Type type) {
		this.pieceType = type;
	}
	/**
	 * Returns a string representation of a chess piece, including both its color and type.
	 */
	public String toString() {
		String result = this.color == Color.BLACK ? "b" : "w";
		switch(this.pieceType) {
		case ROOK:
			result += "R";
			break;
		case KNIGHT:
			result += "N";
			break;
		case BISHOP:
			result += "B";
			break;
		case QUEEN:
			result += "Q";
			break;
		case KING:
			result += "K";
			break;
		case PAWN:
			result += "p";
			break;
		}
		return result;
	}
	/**
	 * Determines whether user input is valid and moves a chess piece to its desired location.
	 * @param board The chess board.
	 * @param input User input from the console.
	 * @return True if the move was successful, false otherwise.
	 */
	public abstract boolean move(Cell[][] board, String input);
	/**
	 * Generates the legal moves for a chess piece at a specified location.
	 * @param board The chess board.
	 * @param start The starting cell of the chess piece.
	 * @return List of cells representing the legal moves for the piece.
	 */
	public abstract ArrayList<Cell> generateLegalMoves(Cell[][] board, Cell start);
}
