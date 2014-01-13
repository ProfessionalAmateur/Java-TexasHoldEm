import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.SpecialK.SpecialKEval.SevenEval;

public class TexasHoldEm {
	
	// HashMap for SpecialK Lookup
	private static HashMap<Card, Integer> specialKLookup = new HashMap<Card, Integer>();
    static SevenEval eval = new SevenEval();

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// variables
		Deck holdemDeck = new Deck();
		int numPlayers = 0;
		Board board = new Board();		
		
		// initializations
		populateSpecialK();    
	    
		
		// Get number of players from user
		do {
			try{
			numPlayers = getNumberOfPlayers();
			}catch (HoldemUserException e){
				System.err.println(e);
			}catch (HoldemIOException ioe){
				System.err.println(ioe);
				System.exit(1);
			}
		} while (numPlayers==0);
		
		Player[] player = new Player[numPlayers];
		
		// Pretty much the only exception to plan for with the remainder of the 
		// program is an array out of bounds exception.  I'll process through
		// the rest of the 'main' processing and perform one catch
		// at the end for this.		
		try{		
			/* 3 shuffles just like in real life. */
			for(int i=0;i<3;i++){
				holdemDeck.shuffle();
			}
			
			// Cut Deck
			holdemDeck.cutDeck();
			
			// Initialize players
			for (int i=0;i<numPlayers;i++){
				player[i] = new Player();
			}
			
			//====================================================================
			// Main processing
			//====================================================================
			
			// Deal hole cards to players
			for (int i=0;i<2;i++){
				for (int j=0;j<numPlayers;j++){
					player[j].setCard(holdemDeck.dealCard());
				}
			}
			
			//------------------------
			// Start dealing board
			//------------------------
			
			// Burn one card before flop
			board.setBurnCard(holdemDeck.dealCard());
			
			// deal flop
			for (int i=0; i<3;i++){
				board.setBoardCard(holdemDeck.dealCard());
			}
		    
		    // Burn one card before turn
			board.setBurnCard(holdemDeck.dealCard());
			
			// deal turn
			board.setBoardCard(holdemDeck.dealCard());
			
			// Burn one card before river
			board.setBurnCard(holdemDeck.dealCard());
			
			// deal river
			board.setBoardCard(holdemDeck.dealCard());
			
			//------------------------
			// end dealing board
			//------------------------
		}catch (HoldemArrayException e){
			System.err.println(e);
			System.exit(1);
		}catch(Exception e){
			// this is the catch all for other thrown errors. (ex: System.arraycopy)
			System.err.println(e);
			System.exit(1);
		}
			
		System.out.println("The hand is complete...\n");
		
		// print deck
		holdemDeck.printDeck();
		
		//print board
		board.printBoard();
		
		// print player cards
		System.out.println("The player cards are the following:\n");
		for (int i=0;i<numPlayers;i++){
			player[i].printPlayerCards(i);
		}
		
		// print burn cards
		board.printBurnCards();		
		
		//------------------------
		// Begin hand comparison
		//------------------------
		
		for (int i=0;i<numPlayers;i++){			
			HandEval handToEval = new HandEval(board, player[i]);
			
			// Set hand name for Player
			player[i].setHandName(handToEval.nameHand());
			
			// Get hand rank using specialK library.
			int[] specialKInput = new int[7];
			for(int k=0;k<specialKInput.length;k++){
				specialKInput[k] = specialKLookup.get(handToEval.getCard(k));
			}			
			// Set the integer value of the hand rank.  This will be compared later to determine the hand winner.
			player[i].setHandRank(eval.getRankOf(specialKInput[0],specialKInput[1],specialKInput[2],specialKInput[3],specialKInput[4],specialKInput[5],specialKInput[6]));
			
			System.out.println("Player " + (i+1) + " " + player[i].getHandName());
			System.out.println(player[i].getHandRank());
		}
	}
	
	protected static int getNumberOfPlayers() throws Exception{
		int intPlayers = 0;
		String userInput = "";
		
		// Get number of players from user.
		System.out.println("Enter number of players (1-9):");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try { 
	           userInput = br.readLine(); 
	        } catch (IOException ioe) {
	        	throw new HoldemIOException(ioe);
	        }
	        
	    // convert user input to an integer
    	try {
    	   intPlayers = Integer.parseInt(userInput);
    		} catch (NumberFormatException nfe) {
               throw new HoldemUserException("Error: Input provided is not a valid Integer!");
            }
    		
    	if ((intPlayers<1) || (intPlayers>9)){
    		throw new HoldemUserException("Error: Number of players must be an integer between 1 and 9");
    	}
		
		return intPlayers;
	}
	
	protected static void populateSpecialK(){
		/* Build SpecialK Lookup HashMap.
         * Ace of Spades = 0
         * Ace of Hearts = 1
         * Ace of Diamonds = 2
         * Ace of Clubs = 3
         * ...
         * Two of Clubs = 51
         */
		Integer specialKCounter = 0;
        for(int i=12;i>=0;i--){
                for (int j=0;j<4;j++){
                        specialKLookup.put(new Card((short)i, (short)j), specialKCounter++);
                }
        }		
	}
}
