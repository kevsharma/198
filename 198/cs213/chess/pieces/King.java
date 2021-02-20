package chess.pieces;

import java.util.ArrayList;
import chess.board.*;

/**
 * The King class extends the Piece class and implements the abstract method getValids from the piece class.
 * @author Kev Sharma | Dhruti Shah
 */
public class King extends Piece {

	/**
	 * Returns a King object. A call to super handles initialization. Super's isKing attribute is set to true to reflect this Piece's instance as being a king.
	 * @param isWhite True if the color of this piece is white. False if the color of this piece is black.
	 * @param location Square object which has, among others, properties like row and column that this piece is on.
	 */
	public King(boolean isWhite, Square location) {
		super(isWhite, location);
		isKing = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Square> getValids(Board currentBoard) {

		ArrayList<Square> moves = new ArrayList<>();
		Square[][] board = currentBoard.getBoard();
		Square src = this.getlocation();
		
		// add all possible moves for king. 
		// Check whether any move is altering src position.
		int r = src.getRow();
		int c = src.getColumn();
		
		if(r-1 >=0) {
			if(c+1 < 8)
				moves.add(board[r-1][c+1]);
			
			if(c-1 >=0)
				moves.add(board[r-1][c-1]);
			
			moves.add(board[r-1][c]);
		}
		
		if(c+1 < 8)
			moves.add(board[r][c+1]);
		if(c-1 >=0)
			moves.add(board[r][c-1]);
		
		if(r+1 < 8) {
			
			if(c-1 >=0)
				moves.add(board[r+1][c-1]);
			
			moves.add(board[r+1][c]);
			
			if(c+1 < 8)
				moves.add(board[r+1][c+1]);
		}
		
		
		// Castling moves
		if(this.hasMoved() == false) {
			int thisKingrow = this.getlocation().getRow();
			Square rook1square = board[thisKingrow][7];
			Square rook2square = board[thisKingrow][0];
			
			if(rook1square.getOccupyingPieceOnThisSquare() != null) { // there is a rook here
				if(rook1square.getOccupyingPieceOnThisSquare().hasMoved() == false) // rook on h hasn't moved.
				{
					// king side
					if(board[thisKingrow][5].getOccupyingPieceOnThisSquare()==null && board[thisKingrow][6].getOccupyingPieceOnThisSquare()==null)
					{// nothing is in the middle
						moves.add(board[thisKingrow][5]);
						moves.add(board[thisKingrow][6]);
					}
				}
			}
			if(rook2square.getOccupyingPieceOnThisSquare() != null) { // there is a rook here
				if(rook2square.getOccupyingPieceOnThisSquare().hasMoved() == false) // rook on a hasn't moved
				{
					// queen side has 3 to check for
					if((board[thisKingrow][3].getOccupyingPieceOnThisSquare()==null) && ((board[thisKingrow][2].getOccupyingPieceOnThisSquare()==null) && (board[thisKingrow][1].getOccupyingPieceOnThisSquare()==null)))
					{// nothing is in the middle
						moves.add(board[thisKingrow][3]);
						moves.add(board[thisKingrow][2]);
					}
				}
			}
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
	 * This method checks whether any of this king's opposing colored pieces are targetting it.
	 * @param currentBoard Pass the state of the board as it is now.
	 * @return Returns true if this king is in check.
	 */
	public boolean isInCheck(Board currentBoard) {		
		// see whether any of the opposing color's have a destination of this piece's location
		
		Square thisKingsLocation = this.getlocation();
		ArrayList<Square> threats = new ArrayList<>();
		
		// Aggregate all of opponents valids (NOT validMOVES just valids)
		for(Square[] s : currentBoard.getBoard()) {
			for(Square cell : s) {
				if(cell.getOccupyingPieceOnThisSquare() == null) {
					continue;
				}
				else {
					Piece p = cell.getOccupyingPieceOnThisSquare();
					if(p==null)
						continue;
					
					if(p.isWhite() == !this.isWhite()) {
						// Aggregate the valids of the other color's pieces
						threats.addAll(p.getValids(currentBoard));
					}
				}
			}
		}
		
		for(Square destination : threats) {
			if(destination.equals(thisKingsLocation))
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * @return Returns wK if the Piece is a white king, bK if the Piece is a black king.
	 */
	public String toString() {
		return this.isWhite() ? "wK" : "bK";
	}
}