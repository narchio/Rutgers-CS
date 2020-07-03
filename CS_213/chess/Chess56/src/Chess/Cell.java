package Chess;
import Pieces.Piece;

/**
 * The class Cell is a representation of a square on the chess board.
 * @author Nicolas Carchio
 * @author Adam Romano
 */

public class Cell {
	/**
	 * Identifies the color of a cell. A cell can be black or white.
	 */
	private Color color; 
	/**
	 * Identifies the chess piece currently occupying the cell. A blank cell is identified as having a piece that is null.
	 */
	private Piece piece;
	/**
	 * Identifies the content of a cell for the bottom row and right-most column.
	 * Content can be letters a-h or numbers 1-8. 
	 * Cells with non-null content are not valid spaces to move on the chess board. 
	 */
	private String content; 
	/**
	 * Identifies the x and y coordinates of a cell on the chess board.
	 */
	private int x, y;
	/**
	 * Constructs an instance of a Cell object with a color, x-coordinate and y-coordinate.
	 * @param color The color of the cell.
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 */
	public Cell(Color color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
	}
	/**
	 * Constructs an instance of a Cell object with content, x-coordinate and y-coordinate.
	 * @param content The content of the cell.
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 */
	public Cell(String content, int x, int y) {
		this.content = content;
		this.x = x;
		this.y = y;
	}
	/**
	 * Returns the x-coordinate of the cell.
	 * @return Integer containing the x-coordinate of the cell.
	 */
	public int getX() {
		return this.x;
	}
	/**
	 * Returns the y-coordinate of the cell.
	 * @return Integer containing the y-coordinate of the cell.
	 */
	public int getY() {
		return this.y;
	}
	/**
	 * Returns the color of the cell.
	 * @return Enum variable containing the color of the cell.
	 */
	public Color getColor() {
		return this.color;
	}
	/**
	 * Returns the piece currently occupying the cell.
	 * @return Piece instance containing the chess piece in the cell.
	 */
	public Piece getPiece() {
		return this.piece;
	}
	/**
	 * Assigns a chess-piece to occupy the cell.
	 * @param piece Chess piece that will occupy the cell.
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	/**
	 * Returns the type of chess piece occupying the cell.
	 * @return Enum variable containing the type of the piece occupying the cell.
	 */
	public Type getPieceType() {
		return this.piece.getPieceType();
	}
	/**
	 * Returns the content of the cell.
	 * @return String containing the content of the cell.
	 */
	public String getContent() {
		return this.content;
	}
	/**
	 * Assigns content to the cell.
	 * @param content Content to be assigned to the cell.
	 */
	public void setContent(String content) {
		this.content = content; 
	}
	/**
	 * Copies the contents of a separate cell.
	 * @param original Cell instance to copy.
	 */
	public void copyCell(Cell original) {
		this.x = original.getX(); 
		this.y = original.getY(); 
		this.color = original.getColor(); 
		this.piece = original.getPiece(); 
		this.content = original.getContent(); 
		 
	}
	/**
	 * Provides a string representation of the cell. 
	 */
	public String toString() {
		if(this.piece != null && this.content == null) {
			return this.piece.toString();
		}
		else if(this.piece == null && this.content == null) {
			return this.color == Color.BLACK ? "##" : "  ";
		}
		else {
			if(content.length() > 0) {
				char c = content.charAt(0); 
				if(Character.isAlphabetic(c)) {
					return " " + content;
				}
			}
			return content;
		}
	}
}
