package chess.board;
import chess.pieces.*;

/**
 * The Square class has several attributes and operations related to a square on an 8x8 chess board.
 * @author Kev Sharma | Dhruti Shah
 */
public class Square {
	
	/**
	 * The row of this square is on.
	 */
	private int row;
	/**
	 * The column this square is on.
	 */
	private int column;
	/**
	 * boolean value, true if this square is white, false if this square is black.
	 */
	private boolean isWhiteSquare;
	/**
	 * The piece, if any, that is on this square.
	 */
	private Piece occupying; 
	
	/**
	 * @param row Location of the Row of the square.
	 * @param column Location of the Column of the square.
	 * @param occupying The piece, if any, that is on this square.
	 */
	public Square(int row, int column, Piece occupying) {
		this.row = row;
		this.column = column;
		
		// determine if the square is black or white using math
		isWhiteSquare = (row + column) % 2 == 1 ? true : false;
		occupying = null;
		
	}
	
	/**
	 * @return Returns the row this square is on.
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * @return Returns the column this square is on.
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * @param p A object with a reference type of Piece
	 */
	public void setOccupyingPieceOnThisSquare(Piece p) {
		occupying = p;
		
		if(occupying!=null)
			occupying.setLocation(this);
	}
	
	/**
	 * @return Returns the piece on this square, if any.
	 */
	public Piece getOccupyingPieceOnThisSquare() {
		return occupying;
	}
	
	
	/**
	 * @return If there is a piece on this square, then it returns that piece's toString() return value.
	 * 		If the square is not occupied, then it returns the color of that square.
	 */
	public String toString() {
		if(occupying == null)
			return (isWhiteSquare ? "  " : "##");
		
		return occupying.toString();
	}
	
	/**
	 * @return Returns true if object passed is a square with the same attributes as the square this method is invoked upon.
	 */
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Square))
			return false;
		
		Square temp = (Square)(o);
		if(getRow() == temp.getRow() && getColumn() == temp.getColumn())
			return true;
		
		return false;
	}

}
