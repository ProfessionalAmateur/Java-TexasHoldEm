import java.util.Random;

public class Deck{
	private Card[] cards = new Card[52];
	Random random = new Random();
	int dealCounter = 0;

	//Constructor
	public Deck(){
		int i = 0;
		for (short j=0; j<4; j++){
			for (short k=0; k<13;k++){
				cards[i++] = new Card(k, j);	
			}
		}
	}
	
	// Print entire deck in order
	protected void printDeck(){
		for(int i=0; i<cards.length;i++){
			System.out.println(i+1 + ": " + cards[i].printCard());
		}
		System.out.println("\n");
	}
	
	// Find card in deck in a linear fashion
	// Use this method if deck is shuffled/random
	protected int findCard(Card card){
		for (int i=0;i<52;i++){
			if (Card.sameCard(cards[i], card)){
				return i;
			}
		}
		return -1;
	}
	
	//return specified card from deck
	protected Card getCard(int cardNum){
		return cards[cardNum];
	}
	
	protected Card dealCard(){
		return cards[dealCounter++];
	}
	
	protected void shuffle() throws HoldemArrayException{
		int length = cards.length;
		
		try{
		//random.nextInt();
		for (int i=0;i<length;i++){
			int change = i + random.nextInt(length-i);
			swapCards(i, change);
		}
		}catch(ArrayIndexOutOfBoundsException e){
			throw new HoldemArrayException(e);
		}
	}
	
	protected void cutDeck(){
		Card[] tempDeck = new Card[52];
		int cutNum = random.nextInt(52);
		
		try{
			System.arraycopy(this.cards, (this.cards.length-cutNum), tempDeck, 0, cutNum);
			System.arraycopy(this.cards, 0, tempDeck, cutNum, (this.cards.length-cutNum));
		}catch(IndexOutOfBoundsException e){
			throw new IndexOutOfBoundsException();
		}catch (ArrayStoreException e){
			throw new ArrayStoreException();
		}catch (NullPointerException e){
			throw new NullPointerException();
		}
		
		this.cards = tempDeck;
	}
	
	// Swap cards in array to 'shuffle' the deck.
	private void swapCards(int i, int change){		
		Card temp = cards[i];
		cards[i] = cards[change];
		cards[change] = temp;
	}
}
