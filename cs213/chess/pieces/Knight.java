package chess.pieces;

import java.util.ArrayList;
import chess.board.*;

/**
 * The Knight class extends the Piece class and implements the abstract method getValids from the piece class.
 * @author Kev Sharma | Dhruti Shah
 */
public class Knight extends Piece {

	/**
	 * Returns a Knight object. A call to super handles initialization.
	 * @param isWhite True if the color of this piece is white. False if the color of this piece is black.
	 * @param location Square object which has, among others, properties like row and column that this piece is on.
	 */
	public Knight(boolean isWhite, Square location) {
		super(isWhite, location);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Square> getValids(Board currentBoard) {
		
		ArrayList<Square> moves = new ArrayList<>();
		Square[][] board = currentBoard.board;
		Square src = getlocation(); // source square of the knight
		
		// add all of the possible moves (no leaping for bishops/rooks)
		int r = src.getRow();
		int c = src.getColumn();
		
		if(r+1 < 8) {
			if(c+2 < 8)
				moves.add(board[r+1][c+2]);
			
			if(c-2 >= 0)
				moves.add(board[r+1][c-2]);
		}
		
		if(r-1 >=0) {
			if(c+2 < 8)
				moves.add(board[r-1][c+2]);
			
			if(c-2 >= 0)
				moves.add(board[r-1][c-2]);
		}
		
		if(r+2 <8) {
			if(c+1 < 8)
				moves.add(board[r+2][c+1]);
			if(c-1 >=0)
				moves.add(board[r+2][c-1]);
		}
		
		if(r-2>=0) {
			if(c+1 < 8)
				moves.add(board[r-2][c+1]);
			if(c-1 >=0)
				moves.add(board[r-2][c-1]);
		}
		

		// subtract those moves which attack their own piece
		// subtract those moves which have the same r,c as src		
		
		ArrayList<Square> validMoves = new ArrayList<>();
		for(Square s : moves)
		{
			r = s.getRow();
			c = s.getColumn();
			
			if(r==src.getRow() && c==src.getColumn())
				continue;
			
			if(attacksOwnPiece(src, s))
				continue;
			
			// Otherwise add to valid.
			validMoves.add(s);
		}

		return validMoves;
	}
	
	/**
	 * @return Returns wN if Piece is a white knight, bN if the Piece is a black knight.
	 */
	public String toString() {
		return this.isWhite() ? "wN" : "bN";
	}

}
