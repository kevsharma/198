package chess.pieces;

import java.util.ArrayList;
import chess.board.*;

/**
 * The Pawn class extends Piece and implements the abstract method getValids. It also overrides the move method from Piece to ensure proper handling of en passante situations.
 * @author Kev Sharma | Dhruti Shah
 */
public class Pawn extends Piece {
	
	/**
	 * boolean value representing whether an opposing pawn can perform en passante on this pawn.
	 */
	public boolean enpassantable;
	
	/**
	 * Returns a Pawn object. A call to super handles initialization. isPawn and enpassantable values are initialized for this Pawn appropriately.
	 * @param isWhite True if the color of this piece is white. False if the color of this piece is black.
	 * @param location Square object which has, among others, properties like row and column that this piece is on.
	 */
	public Pawn(boolean isWhite, Square location) {
		super(isWhite, location);
		isPawn = true;
		enpassantable = false;
	}
	
	/**
	 * @return Returns wP if the piece is a white pawn, bP if the piece is a black pawn.
	 */
	public String toString() {
		return this.isWhite() ? "wp" : "bp";
	}
	
	/**
	 * Performs a pawn move and then promotes the pawn based on parameter passed.
	 * @param src The square this pawn is on.
	 * @param destination The square this pawn wants to move to.
	 * @param currentBoard Pass the board as it is now (to reflect the state of the game).
	 * @param c Indicates what type of piece they wish to promote the pawn to. 
	 * @return Returns -1 if the move is illegal. Returns 0 if the move went through and promotion happened. Returns 1 if the move went through and caused 
	 * check to the other king. Returns 2 if the move went through and causes checkmate to the other king.
	 */
	public int promotion(Square src, Square destination, Board currentBoard, char c) {
		int result = this.move(src, destination, currentBoard);
		if(result == -1)
			return -1;
		
		// Prmote the piece
		if(c=='N') {
			destination.setOccupyingPieceOnThisSquare(new Knight(this.isWhite(), destination));
		}
		else if(c=='R') {
			destination.setOccupyingPieceOnThisSquare(new Rook(this.isWhite(), destination));
		}
		else if(c=='B') {
			destination.setOccupyingPieceOnThisSquare(new Bishop(this.isWhite(), destination));
		}
		else if(c=='Q'){
			destination.setOccupyingPieceOnThisSquare(new Queen(this.isWhite(), destination));
		}
		
		// Return whether this promotion checks or mates the other king
		Piece p = destination.getOccupyingPieceOnThisSquare();
		
		boolean checksOpponent = putsOtherKingInCheck(!p.isWhite(), currentBoard);
		boolean checkmatesOpponent = putsOtherKingInCheckmate(!p.isWhite(), currentBoard);
		
		if(checkmatesOpponent) {// check + no moves = checkmate
			return 2;
		}
		
		if(checksOpponent) {
			return 1;
		}
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public int move(Square src, Square destination, Board currentBoard){
		// Most important method.
		if(src.getOccupyingPieceOnThisSquare() == null)
			return -1;

		ArrayList<Square> validDestinations = src.getOccupyingPieceOnThisSquare().getValidMoves(currentBoard);
		Square[][] board = currentBoard.board;

		// Check whether destination square exists in the list of legal moves (where this piece can move to).
		for(Square s : validDestinations) {
			if(destination.equals(s)) {
				
				// proceed moving the piece on src to destination
				Piece p = src.getOccupyingPieceOnThisSquare();
				
				src.getOccupyingPieceOnThisSquare().hasMoved = true;
				
				// The stuff inside the braces is enpassant
				if(destination.getOccupyingPieceOnThisSquare()==null && destination.getColumn()!= src.getColumn()) {
					// we know this move is an enpassant move and don't have to worry about going out of bounds
					if(p.isWhite()) {
						// white left and right enpassant move
						if(board[destination.getRow()-1][destination.getColumn()].getOccupyingPieceOnThisSquare()!=null) {
							board[destination.getRow()-1][destination.getColumn()].setOccupyingPieceOnThisSquare(null);
						}
					}
					
					if(p.isWhite()==false) {
						// black left and black enpassant move
						if(board[destination.getRow()+1][destination.getColumn()].getOccupyingPieceOnThisSquare()!=null) {
							board[destination.getRow()+1][destination.getColumn()].setOccupyingPieceOnThisSquare(null);
						}
					}
					
				}
				
				destination.setOccupyingPieceOnThisSquare(src.getOccupyingPieceOnThisSquare());
				src.setOccupyingPieceOnThisSquare(null);
				
				
				// After this move, check for check or checkmate
				// !p.isWhite() passes in the opposite color
				boolean checksOpponent = putsOtherKingInCheck(!p.isWhite(), currentBoard);
				boolean checkmatesOpponent = putsOtherKingInCheckmate(!p.isWhite(), currentBoard);
				
				if(checkmatesOpponent) {// check + no moves = checkmate
					this.setThisColorsPawnsToNoLongerBeEnPassantable(currentBoard);
					if(Math.abs(destination.getRow() - src.getRow()) == 2) {
						this.enpassantable = true; // same as ((Pawn)(dest.getOccupyingPieceOnThisSquare())).enpassantable = true;
					}
					return 2;
				}
				
				if(checksOpponent) {
					this.setThisColorsPawnsToNoLongerBeEnPassantable(currentBoard);
					if(Math.abs(destination.getRow() - src.getRow()) == 2) {
						this.enpassantable = true; // same as ((Pawn)(dest.getOccupyingPieceOnThisSquare())).enpassantable = true;
					}
					return 1;
				}
				
				this.setThisColorsPawnsToNoLongerBeEnPassantable(currentBoard);
				if(Math.abs(destination.getRow() - src.getRow()) == 2) {
					this.enpassantable = true; // same as ((Pawn)(dest.getOccupyingPieceOnThisSquare())).enpassantable = true;
				}
				return 0;
			}
		}
		
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Square> getValids(Board currentBoard) {
		ArrayList<Square> moves = new ArrayList<>();
		Square[][] board = currentBoard.getBoard();
		Square src = this.getlocation();
		
		// add all of the possible moves (no leaping for bishops/rooks), (+-1, +-2) (+-2, +-1)
		
		int r = src.getRow();
		int c = src.getColumn();
		
		// if the piece this one intends on going to (when it goes forward) contains a piece of the opposing color, don't let it.
		
		if(this.isWhite() && this.hasMoved() == false) { // starting place
			if(board[r+2][c].getOccupyingPieceOnThisSquare() == null)
				moves.add(board[r+2][c]);
		}
		else if(!this.isWhite() && this.hasMoved() == false) { // starting place
			if(board[r-2][c].getOccupyingPieceOnThisSquare() == null)
				moves.add(board[r-2][c]);
		}
		
		// white pawn moving up
		if(this.isWhite()) {
			if(r+1 < 8) {
				if(board[r+1][c].getOccupyingPieceOnThisSquare() == null) // regular up move
					moves.add(board[r+1][c]);
				
				if(c+1 < 8) // white capture
					if(board[r+1][c+1].getOccupyingPieceOnThisSquare()!=null){
						moves.add(board[r+1][c+1]);
					}
				
				if(c-1 >= 0) // white capture
					if(board[r+1][c-1].getOccupyingPieceOnThisSquare()!=null) {
						moves.add(board[r+1][c-1]);
					}
			}
		}
		
		
		// black pawn moving down
		if(this.isWhite() == false) {
			if(r-1 >= 0) {
				if(board[r-1][c].getOccupyingPieceOnThisSquare() == null) // regular down move
				moves.add(board[r-1][c]);
				
				if(c+1 < 8) // black capture
					if(board[r-1][c+1].getOccupyingPieceOnThisSquare()!=null){
						moves.add(board[r-1][c+1]);
					}
				
				if(c-1 >= 0) // black capture
					if(board[r-1][c-1].getOccupyingPieceOnThisSquare()!=null) {
						moves.add(board[r-1][c-1]);
					}
			}
		}
		
		// en passante where white pawn caps black's enpassantable pawn
		if(this.isWhite() && this.getlocation().getRow()==4) {
			// Left 
			if((this.getlocation().getColumn() - 1) >= 0) {
				Piece left = board[4][this.getlocation().getColumn() -1].getOccupyingPieceOnThisSquare();
				if(left!=null && (left.isPawn() && left.isWhite() == false && ((Pawn)left).enpassantable)) {
					moves.add(board[5][this.getlocation().getColumn()-1]);
				}
			}
			// Right
			if((this.getlocation().getColumn()+1) < 8) {
				Piece right = board[4][this.getlocation().getColumn() +1].getOccupyingPieceOnThisSquare();
				if(right !=null && (right.isPawn() && right.isWhite() == false && ((Pawn)right).enpassantable)) {
					moves.add(board[5][this.getlocation().getColumn()+1]);
				}
			}
			
		}
		
		// en passante where black pawn caps white's enpassantable pawn
		if(this.isWhite() == false && this.getlocation().getRow()==3) {
			// Left 
			if((this.getlocation().getColumn() - 1) >= 0) {
				Piece left = board[3][this.getlocation().getColumn() -1].getOccupyingPieceOnThisSquare();
				if(left!=null && (left.isPawn() && left.isWhite() && ((Pawn)left).enpassantable)) {
					moves.add(board[2][this.getlocation().getColumn()-1]);
				}
			}
			// Right
			if((this.getlocation().getColumn()+1) < 8) {
				Piece right = board[3][this.getlocation().getColumn() +1].getOccupyingPieceOnThisSquare();
				if(right !=null && (right.isPawn() && right.isWhite() && ((Pawn)right).enpassantable)) {
					moves.add(board[2][this.getlocation().getColumn()+1]);
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
}
