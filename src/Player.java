
public class Player {
	private Card[] holeCards = new Card[2];
	private int holeCardIndex = 0;
	private int finalCardRank = -1;
	private String handName = "";
	
	//constructor
	public Player(){
	}
	
	public Player(Card card1, Card card2){
		holeCards[0] = card1;
		holeCards[1] = card2;
	}
	
	//methods
	protected void setCard(Card card) throws HoldemArrayException{
		try{
			holeCards[holeCardIndex++] = card;
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e); 
		}
	}
	
	protected Card getCard(int cardNum){
		return holeCards[cardNum];
	}
	
	protected void setHandRank(int i){
		this.finalCardRank = i;
	}
	
	protected int getHandRank(){
		return this.finalCardRank;
	}
	
	protected void setHandName(String s){
		this.handName = s;
	}
	
	protected String getHandName(){
		return this.handName;
	}
	
	protected int holeCardsSize(){
		return holeCards.length;
	}
	
	protected void printPlayerCards(int playerNumber){
		System.out.println("Player " + (playerNumber+1) + " hole cards:");
		for (int i=0;i<2;i++){
			System.out.println(holeCards[i].printCard());
		}
		System.out.println("\n");
	}
}
