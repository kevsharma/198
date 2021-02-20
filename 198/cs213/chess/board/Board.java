package chess.board;

import chess.pieces.*;

/**
 * The board class represents an 8x8 chess board.
 * @author Kev Sharma | Dhruti Shah
 */
public class Board {
	
	/**
	 * An 8x8 array of Square objects designed to resemble a real life chess board.
	 */
	public Square[][] board;

	/**
	 * Returns a new Board object that has its squares resemble the beginning of a chess game.
	 */
	public Board() {
		board = new Square[8][8];
		
		// initialize board
		for(int row=0; row<8; row++)
			for(int col=0; col<8; col++) {
				board[row][col] = new Square(row, col, null);
			}
		
		// initialize pawns
		for(int col=0; col<8; col++) {
			board[1][col].setOccupyingPieceOnThisSquare(new Pawn(true, board[1][col]));
			board[6][col].setOccupyingPieceOnThisSquare(new Pawn(false, board[6][col]));
		}
				
		// initialize all the other pieces
		
		board[0][0].setOccupyingPieceOnThisSquare(new Rook(true, board[0][0]));
		board[0][1].setOccupyingPieceOnThisSquare(new Knight(true, board[0][1]));
		board[0][2].setOccupyingPieceOnThisSquare(new Bishop(true, board[0][2]));
		board[0][3].setOccupyingPieceOnThisSquare(new Queen(true, board[0][3]));
		board[0][4].setOccupyingPieceOnThisSquare(new King(true, board[0][4]));
		board[0][5].setOccupyingPieceOnThisSquare(new Bishop(true, board[0][5]));
		board[0][6].setOccupyingPieceOnThisSquare(new Knight(true, board[0][6]));
		board[0][7].setOccupyingPieceOnThisSquare(new Rook(true, board[0][7]));
		
		board[7][0].setOccupyingPieceOnThisSquare(new Rook(false, board[7][0]));
		board[7][1].setOccupyingPieceOnThisSquare(new Knight(false, board[7][1]));
		board[7][2].setOccupyingPieceOnThisSquare(new Bishop(false, board[7][2]));
		board[7][3].setOccupyingPieceOnThisSquare(new Queen(false, board[7][3]));
		board[7][4].setOccupyingPieceOnThisSquare(new King(false, board[7][4]));
		board[7][5].setOccupyingPieceOnThisSquare(new Bishop(false, board[7][5]));
		board[7][6].setOccupyingPieceOnThisSquare(new Knight(false, board[7][6]));
		board[7][7].setOccupyingPieceOnThisSquare(new Rook(false, board[7][7]));

	}
	
	/**
	 * @return Returns an 8x8 array of Square objects (board)
	 */
	public Square[][] getBoard(){
		return board;
	}
	
	/**
	 * @return Returns the asci UI representation of this board. This representation reflects the state of the chess game.
	 */
	@Override
	public String toString() {
		String asciUI = "";
		
		for(int i=board.length-1; i>=0; i--) {
			for(int j=0; j<board.length; j++) {
				asciUI += board[i][j].toString() + " ";
			}
			asciUI += (i+1) + "\n";
		}
		
		asciUI += " a  b  c  d  e  f  g  h\n";
		
		return asciUI;
	}
}
























