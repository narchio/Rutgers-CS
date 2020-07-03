package Pieces;
import Chess.Color;
import Chess.Type;

import java.util.ArrayList;
import java.util.List;

import Chess.Cell;


public abstract class Piece {
	
	private Color color; 
	private Type pieceType;
	private int numMoves;
	private List<Cell> legalMoves;
	
	public Piece(Color color, Type type) {
		this.color = color;
		this.pieceType = type;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public List<Cell> getLegalMoves(){
		return this.legalMoves;
	}
	
	public void updateLegalMoves(Cell[][] board, int startX, int startY) {
		this.legalMoves = this.generateLegalMoves(board, startX, startY);
	}
	
	public void updateNumMoves() {
		this.numMoves++;
	}
	
	public int getNumMoves() {
		return this.numMoves;
	}
	
	public Type getPieceType() {
		return this.pieceType;
	}
	
	public void setPieceType(Type type) {
		this.pieceType = type;
	}
	
	public abstract boolean move(Cell[][] board, String input);
	
	public abstract ArrayList<Cell> generateLegalMoves(Cell[][] board, int startX, int startY);
}
