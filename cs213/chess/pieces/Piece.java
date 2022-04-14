package chess.pieces;
import java.util.ArrayList;
import chess.board.*;

/**
 * This abstract class has properties which generalize to all Chess pieces.
 * @author Kev Sharma | Dhruti Shah
 */
public abstract class Piece{
	
	/**
	 * boolean value stores true if this piece is white, false if black.
	 */
	public boolean isWhite;
	
	/**
	 * boolean value stores true if this piece has runtime type of King, false otherwise.
	 */
	public boolean isKing;
	
	/**
	 * boolean value stores true if this piece has runtime type of pawn, false otherwise.
	 */
	public boolean isPawn;
	
	/**
	 * boolean value stores true if this piece has moved once before.
	 */
	public boolean hasMoved;
	
	/**
	 * Stores the square which this piece is occupying.
	 */
	Square location;
	
	/**
	 * @param isWhite Pass true if the piece is white, false if piece is black.
	 * @param location Pass the square this piece is occupying.
	 */
	public Piece(boolean isWhite, Square location) {
		this.isWhite = isWhite;
		this.location = location;
		
		isKing = false;
		isPawn = false;
		hasMoved = false;
	}
	
	/**
	 * @return Returns true if the piece is white or false if the piece is black.
	 */
	public boolean isWhite() {
		return isWhite;
	}
	
	/**
	 * @return Returns the location of the square this piece is occupying.
	 */
	public Square getlocation() {
		return location;
	}
	
	/**
	 * @param s This square's location now gets set to where it has moved.
	 */
	public void setLocation(Square s) {
		location = s;
	}
	
	/**
	 * @return Returns true if this piece is a King of either color.
	 */
	public boolean isKing() {
		return isKing;
	}
	
	/**
	 * @return Returns whether this piece is a pawn
	 */
	public boolean isPawn() {
		return isPawn;
	}
	
	/**
	 * @return Returns true if this piece has moved once already.
	 * This is important for the pawn, king, and rook pieces. 
	 */
	public boolean hasMoved() {
		return hasMoved;
	}
	
	/**
	 * @param currentBoard Pass the state of the board as it is now.
	 * @return Returns an array list of square objects denoting destinations that this piece can move to.
	 * This array list CONTAINS those moves which, if executed, cause a check to that piece's own king.
	 * This method is invoked upon the pieces of the other color to determine if they have a path to this king (hence threaten this king).
	 */
	public abstract ArrayList<Square> getValids(Board currentBoard);
	
	/**
	 * @param currentBoard Pass the state of the board as it is now.
	 * @return Returns an array list of square objects denoting valid and LEGAL moves.
	 * The returned ArrayList of square objects does not contain those moves which, if executed, cause a check to that piece's own king.
	 */
	public ArrayList<Square> getValidMoves(Board currentBoard){
		
		// Call getValids to obtain all places this piece can move to (even if it causes a check to this piece's king).
		ArrayList<Square> validMoves = this.getValids(currentBoard);
		ArrayList<Square> legalMoves = new ArrayList<>();
		/*
		 * Filter out those moves from validMoves which cause a check to this piece's king.
		 * We then return all legal moves of this piece.
		 */
		for(int i=0; i<validMoves.size(); i++) {
			
			Square dest = validMoves.get(i);
			
			// If the king is in check after this move is made, then this move is not legal
			if(!putsOwnKingInCheck(this.getlocation(), dest, currentBoard)) {
				legalMoves.add(dest);
			}
		}

		
		return legalMoves;	
	}
	

	/**
	 * @param src The square this piece is on.
	 * @param destination The square this piece intends on moving to.
	 * @return Returns true if this move in invalid (by attacking its own piece), false otherwise.
	 */
	public boolean attacksOwnPiece(Square src, Square destination) {
		// Does the piece at the source, moving to the square at the destination capture a piece of its own color?
		Piece sourcePiece = src.getOccupyingPieceOnThisSquare();
		Piece destinationPiece = destination.getOccupyingPieceOnThisSquare();
		
		// Prevents null pointer exception.
		if(destinationPiece == null)
			return false;
		
		boolean sourcePieceColor = sourcePiece.isWhite();
		boolean destinationPieceColor = destinationPiece.isWhite();
		
		// if they are the same, this piece is trying to capture a piece of its own color.
		// this means that it is true that this piece is attacksOwnPiece
		if(sourcePieceColor == destinationPieceColor)
			return true;
		
		return false;
	}

	/**
	 * @param src The square this piece is on.
	 * @param destination The square this piece wants to move to.
	 * @param currentBoard The board situation
	 * @return Returns true, if this move causes a check to this piece's own king.
	 */
	public boolean putsOwnKingInCheck(Square src, Square destination, Board currentBoard) {
		
		Square[][] board = currentBoard.board;
		
		// this was a quick fix for a problem that needs bigger fix
		if(src.getOccupyingPieceOnThisSquare() == null)
			return false;
		
		/// If the move we're looking at is a king itself, this process is easy
		if(src.getOccupyingPieceOnThisSquare().isKing()) {
			// if this piece is itself the king and moving it causes it to stay in check, then return true;
			Piece temp = destination.getOccupyingPieceOnThisSquare();
			
			destination.setOccupyingPieceOnThisSquare(src.getOccupyingPieceOnThisSquare()); // destination now holds the king
			src.setOccupyingPieceOnThisSquare(null);
			
			
			King k = (King)destination.getOccupyingPieceOnThisSquare();

			if(k!=null && k.isInCheck(currentBoard)) {
				//System.out.println("we came here");
				// reverse the move we just made and return true;
				src.setOccupyingPieceOnThisSquare(destination.getOccupyingPieceOnThisSquare());
				destination.setOccupyingPieceOnThisSquare(temp);
				return true;
			}
			
			// if its not in check then we still need to ensure that before returning false, we switch the positions back
			src.setOccupyingPieceOnThisSquare(destination.getOccupyingPieceOnThisSquare());
			destination.setOccupyingPieceOnThisSquare(temp);
			return false;
		}
		
		
		// Otherwise find this color's piece's king.
		Piece king = new King(this.isWhite(), null);
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece p = board[i][j].getOccupyingPieceOnThisSquare();
				if(p == null)
					continue;
				// if the piece is king & piece color matches king color
				if(p.isKing() && (p.isWhite() == this.isWhite())) {
					king = new King(this.isWhite(), board[i][j]); // finds the king
					break;
				}
			}
		}
		
		// Determine whether after making this move, our king is in check. If it is, then we know this move is illegal.
		boolean putsKingInCheck = false;
		
		// do the move
		Piece temp = destination.getOccupyingPieceOnThisSquare();
		destination.setOccupyingPieceOnThisSquare(src.getOccupyingPieceOnThisSquare());
		src.setOccupyingPieceOnThisSquare(null);
		// if it puts the king in check then that move is invalid
		
		King k = (King)king;
		// If any of the other colored piece's valids have a destination matching this king.
		if(k.isInCheck(currentBoard)) {
			//System.out.println("we came here");
			putsKingInCheck = true;
		}
		
		// reverse the move we just made.
		src.setOccupyingPieceOnThisSquare(destination.getOccupyingPieceOnThisSquare());
		destination.setOccupyingPieceOnThisSquare(temp);
		
		return putsKingInCheck;
	}

	/**
	 * @param opponent The color of the opposing king.
	 * @param currentBoard The board as it is now.
	 * @return Returns true, if the other king is in check.
	 */
	public boolean putsOtherKingInCheck(boolean opponent, Board currentBoard) {
		Piece king = new King(opponent, null);
		Square[][] board = currentBoard.board;
		
		// Find opposing colored king.
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece p = board[i][j].getOccupyingPieceOnThisSquare();
				if(p == null)
					continue;
				// if the piece is king & piece color matches king color
				if(p.isKing() && (p.isWhite() == opponent)) {
					king = new King(opponent, board[i][j]); // finds the king
					break;
				}
			}
		}
		
		// Check whether the opposing colored king is in check.
		return ((King)king).isInCheck(currentBoard);
	}
	
	/**
	 * @param opponent The color of the opposing king.
	 * @param currentBoard The board as it is now.
	 * @return Returns true, if the other colored pieces have no valid moves left. Hence the opposing king is checkmated.
	 */
	public boolean putsOtherKingInCheckmate(boolean opponent, Board currentBoard) {
		Piece king = null;
		Square[][] board = currentBoard.board;
		
		// Find opposing colored king.
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece p = board[i][j].getOccupyingPieceOnThisSquare();
				if(p == null)
					continue;
				// if the piece is king & piece color matches king color
				if(p.isKing() && (p.isWhite() == opponent)) {
					king = new King(opponent, board[i][j]); // finds the king
					break;
				}
			}
		}
		
		if(((King)king).isInCheck(currentBoard) == false) {
			return false;
		}
		
		// defendable = union of set of valid moves for all opponent colored pieces, if none exist then we are assured of checkmate
		ArrayList<Square> defendable = new ArrayList<>();
		for(Square[] s : board) {
			for(Square cell : s) {
				if(cell.getOccupyingPieceOnThisSquare() == null) {
					continue;
				}
				else {
					Piece p = cell.getOccupyingPieceOnThisSquare();
					if(p==null)
						continue;
					if(p.isWhite() == opponent) { // if the piece belongs to the same color as the king we are checking for checkmate 
						defendable.addAll(p.getValidMoves(currentBoard)); // add all the moves for that piece given they don't create check for the king
					}
				}
			}
		}

		if(defendable.size() == 0)
			return true;
		
		//for(Square n : defendable) {
		//	System.out.println("defendable has: " + n.getRow() + " ," + n.getColumn()); // this is here to ensure that we programmed everything correctly
		//}  // if checkmate isn't working as intended, uncomment above line to see all possible moves
		
		return false;
	}

	/**
	 * If the opposing player hasn't captured that en passantable pawn, then we are assured they no longer can.
	 * @param currentBoard Takes in the current board and on this players turn ensures that all pawn 2 step moves they did in prior move are now no longer en passantable.
	 */
	public void setThisColorsPawnsToNoLongerBeEnPassantable(Board currentBoard) {
		// If black is making a move, then we know that none of black's previously enpassantable pawns can now be en passantable
		for(Square[] s : currentBoard.board) {
			for(Square cell : s) {
				Piece p = cell.getOccupyingPieceOnThisSquare();
				if(p == null)
					continue;
				
				// Piece has to be pawn and has to have same color as this piece.
				if(p.isWhite() == this.isWhite() && p.isPawn()) {
					((Pawn)p).enpassantable = false;
				}
			}
		}
	}
	
	
	/**
	 * @param src The square this piece is on.
	 * @param destination The square this piece wishes to move to.
	 * @param currentBoard Pass the state of the board as it is now.
	 * @return Returns 0 if move is legal and went through.
	 * Returns 1 if the move is legal and causes check to opponent
	 * Returns 2 if the move is legal and causes checkmate to opponent
	 * Returns -1 if the move was illegal and didn't go through.
	 */
	public int move(Square src, Square destination, Board currentBoard){
		// Most important method.
		if(src.getOccupyingPieceOnThisSquare() == null)
			return -1;

		ArrayList<Square> validDestinations = src.getOccupyingPieceOnThisSquare().getValidMoves(currentBoard);

		// Check whether destination square exists in the list of legal moves (where this piece can move to).
		for(Square s : validDestinations){
			if(destination.equals(s)) {
				
				
				// Following is only if the piece is a king and if the destination move implies its a castle move.
				if(src.getOccupyingPieceOnThisSquare().isKing()) {
					boolean isCastle = Math.abs(src.getColumn() - destination.getColumn()) == 2 ? true : false;
					
					if(isCastle) {

						// proceed moving king to destination, rook moves one right of destination.getCol
						
						Piece rook = null;
						
						if(src.getColumn() > destination.getColumn()) {
							// queen side // rook moves one left of destination.get Col
							
							// make sure interior moves are valid (they aren't incurring check)
							boolean foundInterior = false;
							for(Square interior : validDestinations) {
								if(currentBoard.board[this.getlocation().getRow()][3].equals(interior))
									foundInterior = true;
							}
							
							if(foundInterior == false)
								return -1;
							
							src.getOccupyingPieceOnThisSquare().hasMoved = true;
							destination.setOccupyingPieceOnThisSquare(src.getOccupyingPieceOnThisSquare());
							src.setOccupyingPieceOnThisSquare(null);
							
							// move the rook 
							rook = currentBoard.board[this.getlocation().getRow()][0].getOccupyingPieceOnThisSquare();
							rook.hasMoved = true;
							currentBoard.board[this.getlocation().getRow()][3].setOccupyingPieceOnThisSquare(rook);
							currentBoard.board[this.getlocation().getRow()][0].setOccupyingPieceOnThisSquare(null);
						}
						else if(src.getColumn() < destination.getColumn()){
							// king side // rook comes one to the left of king
							
							// make sure interior moves are valid (they aren't incurring check)
							boolean foundInterior = false;
							for(Square interior : validDestinations) {
								if(currentBoard.board[this.getlocation().getRow()][5].equals(interior))
									foundInterior = true;
							}
							
							if(foundInterior == false)
								return -1;
							
							src.getOccupyingPieceOnThisSquare().hasMoved = true;
							destination.setOccupyingPieceOnThisSquare(src.getOccupyingPieceOnThisSquare());
							src.setOccupyingPieceOnThisSquare(null);
							
							rook = currentBoard.board[this.getlocation().getRow()][7].getOccupyingPieceOnThisSquare();
							rook.hasMoved = true;
							currentBoard.board[this.getlocation().getRow()][5].setOccupyingPieceOnThisSquare(rook);
							currentBoard.board[this.getlocation().getRow()][7].setOccupyingPieceOnThisSquare(null);	
						}
						
						boolean checksOpponent = putsOtherKingInCheck(!rook.isWhite(), currentBoard);
						boolean checkmatesOpponent = putsOtherKingInCheckmate(!rook.isWhite(), currentBoard);
						
						if(checkmatesOpponent) {
							this.setThisColorsPawnsToNoLongerBeEnPassantable(currentBoard);
							return 2;
						}
						
						if(checksOpponent) {
							this.setThisColorsPawnsToNoLongerBeEnPassantable(currentBoard);
							return 1;
						}

						
						return 0;
					}
				}
				
				// proceed moving the piece on src to destination
				Piece p = src.getOccupyingPieceOnThisSquare();
				
				src.getOccupyingPieceOnThisSquare().hasMoved = true;
				
				destination.setOccupyingPieceOnThisSquare(src.getOccupyingPieceOnThisSquare());
				src.setOccupyingPieceOnThisSquare(null);
				
				
				// After this move, check for check or checkmate
				// !p.isWhite() passes in the opposite color
				boolean checksOpponent = putsOtherKingInCheck(!p.isWhite(), currentBoard);
				boolean checkmatesOpponent = putsOtherKingInCheckmate(!p.isWhite(), currentBoard);
				
				if(checkmatesOpponent) {// check + no moves = checkmate
					this.setThisColorsPawnsToNoLongerBeEnPassantable(currentBoard);
					return 2;
				}
				
				if(checksOpponent) {
					this.setThisColorsPawnsToNoLongerBeEnPassantable(currentBoard);
					return 1;
				}
				
				this.setThisColorsPawnsToNoLongerBeEnPassantable(currentBoard);
				return 0;
			}
		}
		
		return -1;
	}
}








