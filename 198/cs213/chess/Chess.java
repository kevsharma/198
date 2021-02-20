package chess;
import chess.board.*;
import chess.pieces.*;
import java.util.Scanner;

/**
 * This class, when run, allows two users to play a chess game.
 * @author Kev Sharma | Dhruti Shah
 */
public class Chess {
	
	/**
	 * boolean value representing whether it is the player who has white pieces to make the move.
	 */
	static boolean whitesTurn = true;
	/**
	 * boolean value representing whether a draw has been offered by the opposing player.
	 */
	static boolean drawOffered = false;

	/**
	 * The main method allows 2 players to play a chess game on the terminal.
	 * @param args Unused in this method.
	 */
	public static void main(String[] args) {
		
		Board game = new Board();
		Scanner s = new Scanner(System.in);
		
		while(true) {
			if(whitesTurn) {
				System.out.println(game);
				System.out.print("White's move: ");
				String temp = s.nextLine();
				System.out.println();
				int x = performMove(temp, game);
				
				while(x==-1) {
					System.out.println("Illegal move, try again");
					System.out.print("\nWhite's move: ");
					temp = s.nextLine();
					System.out.println();
					x = performMove(temp, game);

				}
				if(x==0) {
					whitesTurn = !whitesTurn;
				}
				else if(x==1) {
					System.out.println("Check\n");
					whitesTurn = !whitesTurn;
				}
				else if(x==2) {
					System.out.println(game);
					System.out.println("Checkmate");
					System.out.println("White wins\n");
					break;
				}
				
				else if(x==4) {
					System.out.println("Black wins\n");
					break;
				}
				
				// They must accept a draw on the very next turn
				else if(x==3) {
					System.out.println("draw");
					break;
				} else if(drawOffered) {drawOffered = false;}
			}
			else {
				System.out.println(game);
				System.out.print("Black's move: ");
				String temp = s.nextLine();
				System.out.println();
				int x = performMove(temp, game);
				
				while(x==-1) {
					System.out.println("Illegal move, try again");
					System.out.print("\nBlack's move: ");
					temp = s.nextLine();
					System.out.println();
					x = performMove(temp, game);
				}
				if(x==0) {
					whitesTurn = !whitesTurn;
				}
				else if(x==1) {
					System.out.println("Check\n");
					whitesTurn = !whitesTurn;
				}
				else if(x==2) {
					System.out.println(game);
					System.out.println("Checkmate");
					System.out.println("Black wins");
					break;
				}
				
				else if(x==4) {
					System.out.println("White wins\n");
					break;
				}
				
				else if(x==3) {
					System.out.println("draw");
					break;
				}else if(drawOffered) {drawOffered = false;}
			}
		}
		
		s.close();
	}
	
	/**
	 * @param s The string input for the move.
	 * @param game The current state of the Board.
	 * @return	Returns -1 if the move is illegal (or poorly formatted) and was not performed
	 * Returns 0 if the move was performed and no check or checkmate to other king
	 * Returns 1 if the move was performed and causes a check to other king
	 * Returns 2 if the move was performed and causes checkmate to other king
	 * Returns 3 if the player accepts a draw
	 * Returns 4 if the player resigns
	 */
	public static int performMove(String s, Board game) {

		String input[] = s.split(" ");
		// Ensure valid input and pass to chess.pieces appropriately.
		/*
		 * Input should look like follows:
		 * 1)	f2 f4 
		 * 2)	f7 f8 *      {* = N,Q,R,B}
		 * 3)	f2 f4 draw?
		 * 4)	draw
		 * 5)	resign
		 * 6) 	f7 f8 N draw?
		 */
		
		// Case 4 and 5
		if(input.length == 1) {
			if(input[0].equals("draw") && drawOffered) {
				return 3;
			}
			
			if(input[0].equals("resign"))
				return 4;
			
			if(input[0].equals("draw?")) {
				drawOffered = true;
				return 0; // Fangda Han's forum states that if no valid moves for this player, then the player can ask for a draw.
			}
		}
		// Case 1 and 2 (but only for queen)
		else if(input.length == 2) {
			String source = input[0];
			String destination = input[1];
			
			if(source.length() > 2 || destination.length() > 2)
				return -1;
			
			int f1 = (int)source.charAt(0) -97; // col
			int r1 = (int)source.charAt(1) -49;  // row

			int f2 = (int)destination.charAt(0) -97; // col
			int r2 = (int)destination.charAt(1) -49; // row
			
			if(r1 < 0 || r1 > 7 || r2 < 0 || r2 > 7 || f1 < 0 || f1 > 7 || f2 < 0 || f2 > 7)
				return -1;
			
			Square src = game.board[r1][f1];
			Square dest = game.board[r2][f2];
			Piece p = src.getOccupyingPieceOnThisSquare();
			
			// ensure that they're not moving an invalid piece
			if(p==null || p.isWhite()!=whitesTurn)
				return -1;


			// promotion?
			if(p.isPawn() && (dest.getRow()==7 || dest.getRow()==0))
				return ((Pawn)p).promotion(src, dest, game, 'Q');
			
			// regular move.
			return p.move(src, dest, game);
		}
		// Case 2 and 3
		else if(input.length == 3) {
			String source = input[0];
			String destination = input[1];
			String third = input[2];
			
			// Check for proper source and destination
			if(source.length() > 2 || destination.length() > 2)
				return -1;
			
			int f1 = (int)source.charAt(0) -97; // col
			int r1 = (int)source.charAt(1) -49;  // row

			int f2 = (int)destination.charAt(0) -97; // col
			int r2 = (int)destination.charAt(1) -49; // row
			
			if(r1 < 0 || r1 > 7 || r2 < 0 || r2 > 7 || f1 < 0 || f1 > 7 || f2 < 0 || f2 > 7)
				return -1;
			
			Square src = game.board[r1][f1];
			Square dest = game.board[r2][f2];
			Piece p = src.getOccupyingPieceOnThisSquare();
			
			// ensure that they're not moving an invalid piece
			if(p==null || p.isWhite()!=whitesTurn)
				return -1;

			// Either promote or draw, if neither executed then illegal formatted.
			if(third.equals("draw?")) {
				drawOffered = true;
				return p.move(src, dest, game);
			}
			
			if(third.equals("N") ||third.equals("B") ||third.equals("Q") ||third.equals("R")) {
				if(p.isPawn() && (dest.getRow()==7 || dest.getRow()==0)) {
					return ((Pawn)p).promotion(src, dest, game, third.charAt(0));
				}		
			}
			
		}
		else if(input.length == 4) {
			String source = input[0];
			String destination = input[1];
			String promote = input[2];
			String drawoffer = input[3];
			
			// Check for proper source and destination
			if(source.length() > 2 || destination.length() > 2)
				return -1;
			
			int f1 = (int)source.charAt(0) -97; // col
			int r1 = (int)source.charAt(1) -49;  // row

			int f2 = (int)destination.charAt(0) -97; // col
			int r2 = (int)destination.charAt(1) -49; // row
			
			if(r1 < 0 || r1 > 7 || r2 < 0 || r2 > 7 || f1 < 0 || f1 > 7 || f2 < 0 || f2 > 7)
				return -1;
			
			Square src = game.board[r1][f1];
			Square dest = game.board[r2][f2];
			Piece p = src.getOccupyingPieceOnThisSquare();
			
			// ensure that they're not moving an invalid piece
			if(p==null || p.isWhite()!=whitesTurn)
				return -1;

			// Check for proper promote
			if(!(promote.equals("N") ||promote.equals("B") ||promote.equals("Q") ||promote.equals("R")))
				return -1;
				
			if(!drawoffer.equals("draw?"))
				return -1;
			
			/*
			 * Otherwise, everything is properly formatted and we execute our move.
			 * We know that if they input 4 arguments, then its a promotion. Also we know that if we reach here then we can call p.move
			 */
			if(p.isPawn() && (dest.getRow()==7 || dest.getRow()==0)) {
				drawOffered = true;				
				return ((Pawn)p).promotion(src, dest, game, promote.charAt(0));
			}
		}
		
		// Neither case worked.
		return -1;
	}
}


