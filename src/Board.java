public class Board {
	private Card[] board = new Card[5];
	private Card[] burnCards = new Card[3];
	
	private int boardIndex = 0;
	private int burnIndex = 0;
	
	//constructor
	public Board(){
	}
	
	//methods
	protected void setBoardCard(Card card) throws HoldemArrayException{
		try{
			this.board[boardIndex++] = card;
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e); 
		}		
	}
	
	protected Card getBoardCard(int cardNum){
		return this.board[cardNum];
	}
	
	protected void setBurnCard(Card card) throws HoldemArrayException{
		try{
			this.burnCards[burnIndex++] = card;
		}catch (ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e); 
		}		
	}
	
	protected Card getBurnCard(int cardNum){
		return this.burnCards[cardNum];
	}
	
	protected int boardSize(){
		return board.length;
	}
	
	protected void printBoard(){
		System.out.println("The board contains the following cards:");
		for(int i =0; i<board.length;i++){
			System.out.println(i+1 + ": " + getBoardCard(i).printCard());
		}
		System.out.println("\n");
	}
	
	protected void printBurnCards(){
		System.out.println("The burn cards are:");
		for(int i =0; i<burnCards.length;i++){
			System.out.println(i+1 + ": " + getBurnCard(i).printCard());
		}
		System.out.println("\n");
	}

}
