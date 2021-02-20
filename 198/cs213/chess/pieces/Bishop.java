package chess.pieces;

import java.util.ArrayList;
import chess.board.*;

/**
 * The Bishop class extends the Piece class and implements the abstract method getValids from the piece class.
 * @author Kev Sharma | Dhruti Shah
 */
public class Bishop extends Piece {
	
	/**
	 * Returns a Bishop object. A call to super handles initialization.
	 * @param isWhite True if the color of this piece is white. False if the color of this piece is black.
	 * @param location Square object which has, among others, properties like row and column that this piece is on.
	 */
	public Bishop(boolean isWhite, Square location) {
		super(isWhite, location);
	}
	
	/**
	 * @return Returns wB if the piece is a white bishop, bB if the piece is a black bishop
	 */
	public String toString() {
		return this.isWhite() ? "wB" : "bB";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Square> getValids(Board currentBoard) {
		ArrayList<Square> moves = new ArrayList<>();
		Square[][] board = currentBoard.getBoard();
		Square src = this.getlocation();
		
		/*
		 * Add all on both diagonals
		 * Note that if a similar colored piece is encountered, then we stop and do not add.
		 * If a different colored piece is encountered, then we add and stop.
		 */
		
		int r = src.getRow()	+1;
		int c = src.getColumn()	+1;
		while(r<8 && c<8) {
			Piece temp = board[r][c].getOccupyingPieceOnThisSquare();
			if(temp != null) {
				// if the piece occupying is same color, then break;
				// else add that and then break;
				if(temp.isWhite() == this.isWhite())
					break;
				
				moves.add(board[r][c]);
				break;
			}

			moves.add(board[r][c]);
			++r; ++c;
		}
		
		r = src.getRow()	-1;
		c = src.getColumn()	-1;
		while(r>=0 && c>=0) {
			Piece temp = board[r][c].getOccupyingPieceOnThisSquare();
			if(temp != null) {
				// if the piece occupying is same color, then break;
				// else add that and then break;
				if(temp.isWhite() == this.isWhite())
					break;
				
				moves.add(board[r][c]);
				break;
			}

			moves.add(board[r][c]);
			--r; --c;
		}
		

		r = src.getRow()	+1;
		c = src.getColumn() -1;
		while(r<8 && c>=0) {
			Piece temp = board[r][c].getOccupyingPieceOnThisSquare();
			if(temp != null) {
				// if the piece occupying is same color, then break;
				// else add that and then break;
				if(temp.isWhite() == this.isWhite())
					break;
				
				moves.add(board[r][c]);
				break;
			}

			moves.add(board[r][c]);
			++r; --c;
		}
		

		r = src.getRow()	-1;
		c = src.getColumn()	+1;
		while(r>=0 && c<8) {
			Piece temp = board[r][c].getOccupyingPieceOnThisSquare();
			if(temp != null) {
				// if the piece occupying is same color, then break;
				// else add that and then break;
				if(temp.isWhite() == this.isWhite())
					break;
				
				moves.add(board[r][c]);
				break;
			}

			moves.add(board[r][c]);
			--r; ++c;
		}
		
		
		// subtract those moves which attack their own piece
		// subtract those moves which have same position as src
		
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
}

