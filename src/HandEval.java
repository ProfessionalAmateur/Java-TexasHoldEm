import java.util.Arrays;

public class HandEval {	
	private Card[] availableCards = new Card[7];
	private String handResult = new String();
	private short[] rankCounter = new short[13];
	private short[] suitCounter = new short[4];
	
	
	private int cardCounter = 0;

	
	// Constructors
	public HandEval(){		
	}
	
	public HandEval(Board board, Player player) throws HoldemArrayException{
		// populate with player cards			
		try{
		for (int i=0;i<player.holeCardsSize();i++){
			this.addCard(player.getCard(i));
		}
		
		//populate with board cards
		for (int i=0;i<board.boardSize();i++){
			this.addCard(board.getBoardCard(i));
		}	
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e); 
		}
	}
	
	//methods
	protected void addCard(Card card) throws HoldemArrayException{
		try{
			availableCards[cardCounter++] = card;
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e); 
		}		
	}
	
	protected Card getCard(int i){
		return availableCards[i];
	}
	
	protected int numCards(){
		return availableCards.length;
	}
	
	protected void sortByRank(){
		Arrays.sort(availableCards, new rankComparator());
	}
	
	protected void sortBySuit(){
		Arrays.sort(availableCards, new suitComparator());
	}
	
	protected void sortBySuitThenRank(){
		Arrays.sort(availableCards, new suitComparator());
		Arrays.sort(availableCards, new rankComparator());
	}
	
	protected void sortByRankThenSuit(){
		Arrays.sort(availableCards, new rankComparator());
		Arrays.sort(availableCards, new suitComparator());
	}
	
	/******************************************************
	 * A very procedural way to check for the rank of a hand
	 * I want to trim this down and will probably use someone
	 * elses library but for now it works
	 * @return String value of a hand
	 * @throws HoldemArrayException
	 */
	protected String nameHand() throws HoldemArrayException{
		//String handResult = new String();
		//short[] rankCounter = new short[13];
		//short[] suitCounter = new short[4];
		
		// initializations
		for (int i=0;i<rankCounter.length;i++){
			rankCounter[i] =0;
		}
		
		for (int i=4;i<suitCounter.length;i++){
			suitCounter[i] = 0;
		}
		

		// Loop through sorted cards and total ranks
		for(int i=0; i<availableCards.length;i++){
			rankCounter[ availableCards[i].getRank() ]++;
			suitCounter[ availableCards[i].getSuit() ]++;
		}
		
		//sort cards for evaluation
		this.sortByRankThenSuit();
		
		// hands are already sorted by rank and suit for royal and straight flush checks.		
		// check for royal flush
		handResult = evaluateRoyal(rankCounter, suitCounter);
		
		// check for straight flush
		if (handResult == null || handResult.length() == 0){
			handResult = evaluateStraightFlush(rankCounter, suitCounter);
		}
		
		// check for four of a kind
		if (handResult == null || handResult.length() == 0){
			handResult = evaluateFourOfAKind(rankCounter);
		}
		
		// check for full house
		if (handResult == null || handResult.length() == 0){
			handResult = evaluateFullHouse(rankCounter);
		}
		
		// check for flush
		if (handResult == null || handResult.length() == 0){
			handResult = evaluateFlush(rankCounter, suitCounter);
		}
		
		// check for straight
		if (handResult == null || handResult.length() == 0){
			// re-sort by rank, up to this point we had sorted by rank and suit
			// but a straight is suit independent.
			this.sortByRank();
			handResult = evaluateStraight(rankCounter);
		}
		
		// check for three of a kind
		if (handResult == null || handResult.length() == 0){
			handResult = evaluateThreeOfAKind(rankCounter);
		}
		
		// check for two pair
		if (handResult == null || handResult.length() == 0){
			handResult = evaluateTwoPair(rankCounter);
		}
		
		// check for one pair
		if (handResult == null || handResult.length() == 0){
			handResult = evaluateOnePair(rankCounter);
		}
		
		// check for highCard
		if (handResult == null || handResult.length() == 0){
			handResult = evaluateHighCard(rankCounter);
		}	
		
		return handResult;
	}
	
	private String evaluateRoyal(short[] rankCounter, short[] suitCounter) throws HoldemArrayException{
		String result = "";
		
		try{
			// Check for Royal Flush (10 - Ace of the same suit).
			// check if there are 5 of one suit, if not royal is impossible		
			if ((rankCounter[8] >= 1 &&       /* 10 */
					rankCounter[9] >= 1 &&   /* Jack */
					rankCounter[10] >= 1 &&  /* Queen */
					rankCounter[11] >= 1 &&  /* King */
					rankCounter[12] >= 1)    /* Ace */
					&& (suitCounter[0] > 4 || suitCounter[1] > 4 ||
							suitCounter[2] > 4 || suitCounter[3] > 4)){
				
				// min. requirements for a royal flush have been met, 
				// now loop through records for an ace and check subsequent cards. 
				// Loop through the aces first since they are the first card to 
				// appear in the sorted array of 7 cards. 
				royalSearch:
					for (int i=6;i>3;i--){
						// Check if first card is the ace.
						// Ace must be in position 0, 1 or 2
						if (availableCards[i].getRank() == 0){
							// because the ace could be the first card in the array
							// but the remaining 4 cards could start at position 1,
							// 2 or 3 loop through checking each possibility.
							for (int j=1;j<4-i;j++){
								if ((availableCards[i-j].getRank() == 11 && 
										availableCards[i-j-1].getRank() == 10 &&
										availableCards[i-j-2].getRank() == 9 &&
										availableCards[i-j-3].getRank() == 8) 
										&&
										(availableCards[i].getSuit() == availableCards[i-j].getSuit() &&
										availableCards[i].getSuit() == availableCards[i-j-1].getSuit() &&
										availableCards[i].getSuit() == availableCards[i-j-2].getSuit() &&
										availableCards[i].getSuit() == availableCards[i-j-3].getSuit())){
											// Found royal flush, break and return.
											result = "Royal Flush!! Suit: " + Card.suitAsString(availableCards[i].getSuit());
											break royalSearch;				
								}
							}
						}				
					}
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
		return result;
	}
	
	// Straight flush is 5 consecutive cards of the same suit.
	private String evaluateStraightFlush(short[] rankCounter, short[] suitCounter) throws HoldemArrayException{
		String result = "";
		boolean straightFlush = true;
		
		try{
			if (suitCounter[0] > 4 || suitCounter[1] > 4 ||
					suitCounter[2] > 4 || suitCounter[3] > 4){
				
				// min. requirements for a straight flush have been met.
				// Loop through available cards looking for 5 consecutive cards of the same suit,
				// start in reverse to get the highest value straight flush
				straightFlushSearch:
					for (int i=availableCards.length-1;i>3;i--){
						for (int j=0; j<5; j++){
							 if ((availableCards[i].getRank()-j != availableCards[i-j].getRank())
							    ||
							    (availableCards[i].getSuit() != availableCards[i-j].getSuit())){
							 	straightFlush = false;
							 	continue straightFlushSearch;
							 }
							 if((j==4) && straightFlush){
							 	result = "Straight Flush!! " + Card.rankAsString(availableCards[i].getRank()) + " high of " + Card.suitAsString(availableCards[i].getSuit());
								break straightFlushSearch;
							 } 							
						}
					}
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
		return result;
	}
	
	// Four of a kind is 4 cards with the same rank: 2-2-2-2, 3-3-3-3, etc...
	private String evaluateFourOfAKind(short[] rankCounter) throws HoldemArrayException{
		String result = "";
		boolean fourOfKind = false;
		String kickerValue = "";
		
		try{
			for (int i=0;i<rankCounter.length;i++){
				if (rankCounter[i] == 4){
					result = "Four of a Kind, " + Card.rankAsString(i) +"'s";
					fourOfKind = true;
				}
				// get kicker
				if(((rankCounter[i] > 0) && (rankCounter[i] < 4))){
					kickerValue = Card.rankAsString(i);
				}
			}
			if(fourOfKind == true){
				result = result + " with a " + kickerValue + " kicker";
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
		return result;
	}
	
	// Full house is having 3 of a kind of one rank, and two of a kind of 
	// a second rank.  EX: J-J-J-3-3
	private String evaluateFullHouse(short[] rankCounter) throws HoldemArrayException{
		String result = "";
		short threeOfKindRank = -1;
		short twoOfKindRank = -1;
		
		// FLAG MUST SET TO CHECK FOR Ace's first when hand is AAA3337
		
		try{
			for (int i=rankCounter.length-1;i>=0;i--){
				if ((threeOfKindRank < 0) || (twoOfKindRank < 0)){
					if ((rankCounter[i]) > 2 && threeOfKindRank < 0){
						threeOfKindRank = (short) (i);
					}
					else if ((rankCounter[i]) > 1){
						twoOfKindRank = (short)(i);
					}
				}
				else
				{
					break;
				}
			}
			
			if ((threeOfKindRank >= 0) && (twoOfKindRank >= 0)){
				result = "Full House: " + Card.rankAsString(threeOfKindRank) + "'s full of " + Card.rankAsString(twoOfKindRank) + "'s";
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
		
		return result;
	}
	
	// Flush is 5 cards of the same suit.
	private String evaluateFlush(short[] rankCounter, short[] suitCounter) throws HoldemArrayException{
		String result = "";
		boolean flush = true;
		
		try{
			// verify at least 1 suit has 5 cards or more.
			if (suitCounter[0] > 4 || suitCounter[1] > 4 ||
					suitCounter[2] > 4 || suitCounter[3] > 4){
				
				flushSearch:
					for (int i=availableCards.length-1;i>3;i--){
						for (short j=0; j<5; j++){
							if(availableCards[i].getSuit() != availableCards[i-j].getSuit()){
								flush = false;
								continue flushSearch;
							}
							if((j==4) && flush){
								result = "Flush!! " + Card.rankAsString(availableCards[i].getRank()) + " high of " + Card.suitAsString(availableCards[i].getSuit());
								break flushSearch;
							 } 
						}
				}			
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}		
		
		return result;
	}
	
	// Straight is 5 consecutive cards, regardless of suit.
	private String evaluateStraight(short[] rankCounter) throws HoldemArrayException{
		String result = "";
		
		try{
			// loop through rank array to check for 5 consecutive
			// index with a value greater than zero
			for (int i=rankCounter.length;i>4;i--){
				if ((rankCounter[i-1] > 0) &&
						(rankCounter[i-2] > 0) &&
						(rankCounter[i-3] > 0) &&
						(rankCounter[i-4] > 0) &&
						(rankCounter[i-5] > 0)){
							result = "Straight " + Card.rankAsString(i-1) + " high";
							break;
				}
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
		return result;
	}
	
	// Three of a kind is 3 cards of the same rank.
	private String evaluateThreeOfAKind(short[] rankCounter) throws HoldemArrayException{
		String result = "";
		String kickerValue = "";
		
		try{
			// loop through rank array to check for 5 consecutive
			// index with a value greater than zero
			for (int i=rankCounter.length-1;i>=0;i--){
				if (rankCounter[i] > 2){
							result = "Three of a Kind: " + Card.rankAsString(i) + "'s";							
				}
				if((kickerValue.length() == 0) && ((result.length() == 0 && rankCounter[i] < 2) || ((result.length() > 0) && rankCounter[i] > 0))){
					kickerValue = Card.rankAsString(i);
				}
				if (result.length() > 0 && kickerValue.length() > 0){
					result = result + " with a " + kickerValue + " kicker";
					break;
				}
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
		return result;
	}
	
	// Two pair is having 2 cards of the same rank, and two
	// different cards of the same rank.  EX: 3-3-7-7-A
	private String evaluateTwoPair(short[] rankCounter) throws HoldemArrayException{
		String result = "";
		String kickerValue = "";
		short firstPairRank = -1;
		short secondPairRank = -1;
		
		try{
			for (int i=rankCounter.length-1;i>=0;i--){
				if ((firstPairRank < 0) || (secondPairRank < 0)  || (kickerValue.length() == 0)){				
					if (((rankCounter[i]) > 1) && (firstPairRank < 0)){
						firstPairRank = (short) (i);					
					}
					else if ((rankCounter[i]) > 1){
						secondPairRank = (short)(i);
					}
					
					if( (kickerValue.length() == 0) && ((firstPairRank >= 0 && secondPairRank >= 0 && rankCounter[i] > 0 && firstPairRank != (short)i && secondPairRank != (short)i) || ((rankCounter[i]) == 1))){
						kickerValue = Card.rankAsString(i);
					}
				}
				else
				{
					// two pair found, break loop.
					break;
				}
			}
			
			// populate output
			if ((firstPairRank >= 0) && (secondPairRank >= 0)){
				result = "Two Pair: " + Card.rankAsString(firstPairRank) + "'s and " + Card.rankAsString(secondPairRank) + "'s" + " with a " + kickerValue + " kicker";						
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
		
		return result;
	}
	
	// One is is two cards of the same rank.
	private String evaluateOnePair(short[] rankCounter) throws HoldemArrayException{
		String result = "";
		String kickerValue = "";
		
		try{
			for (int i=rankCounter.length-1;i>=0;i--){
				if((rankCounter[i]) > 1){
					result = "One Pair: " + Card.rankAsString(i) + "'s";
				}
				if(kickerValue.length() ==0){
					if((rankCounter[i]) == 1){
						kickerValue = Card.rankAsString(i);
					}
				}
				if (result.length() > 0 && kickerValue.length() > 0){
					result = result + " with a " + kickerValue + " kicker";
					break;
				}
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
		return result;
	}
	
	// high card is the highest card out of the 7 possible cards to be used.
	private String evaluateHighCard(short[] rankCounter) throws HoldemArrayException{
		String result = "";
		String kickerValue = "";
		
		try{
			for (int i=rankCounter.length-1;i>=0;i--){
				if((rankCounter[i]) > 0 && result.length() == 0){
					result = "High Card: " + Card.rankAsString(i);
				    continue;
				}
				if(result.length() > 0 && kickerValue.length() == 0 && rankCounter[i] == 1){
					kickerValue = Card.rankAsString(i);
				}
				if (result.length() > 0 && kickerValue.length() > 0){
					result = result + " with a " + kickerValue + " kicker";
					break;
				}
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
		return result;
	}
}
