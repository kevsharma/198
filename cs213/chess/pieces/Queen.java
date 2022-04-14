package chess.pieces;

import java.util.ArrayList;
import chess.board.*;

/**
 * The Queen class extends the Piece class and implements the abstract method getValids from the piece class.
 * @author Kev Sharma | Dhruti Shah
 */
public class Queen extends Piece{

	/**
	 * Returns a Queen object. A call to super handles initialization.
	 * @param isWhite True if the color of this piece is white. False if the color of this piece is black.
	 * @param location Square object which has, among others, properties like row and column that this piece is on.
	 */
	public Queen(boolean isWhite, Square location) {
		super(isWhite, location);
	}

	/**
	 * @return Returns wQ if Piece is a white queen, bQ if the Piece is a black queen.
	 */
	public String toString() {
		return this.isWhite() ? "wQ" : "bQ";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Square> getValids(Board currentBoard){
		// Set of valid moves for a Queen is the union of the set of valid moves for a rook and a bishop.
			boolean queenColor = this.isWhite();
			
			Piece bishop = new Bishop(queenColor, this.getlocation());
			Piece rook = new Rook(queenColor, this.getlocation());
			
			ArrayList<Square> validQueenMoves = new ArrayList<>();
			
			validQueenMoves.addAll(bishop.getValids(currentBoard));
			validQueenMoves.addAll(rook.getValids(currentBoard));
			
			return validQueenMoves;
	}
}