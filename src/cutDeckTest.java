import java.util.Random;


public class cutDeckTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random random = new Random();
		int cutNum = random.nextInt(10);
		char[] charArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
		char[] cutArray = new char[10];
		for (int k=0;k<cutArray.length;k++){
			cutArray[k] = '-';
		}
		
		System.out.println("Cut Number: " + cutNum + "\n");
		
		System.out.println("Char Array before cut:");
		for (int i=0;i<charArray.length;i++){
			System.out.println(charArray[i]);
		} 
		
		/*for (int i=0;i<cutNum;i++){
			cutArray[i] = charArray[10-cutNum+i];			
		}
		for (int j=0;j<10-cutNum;j++){
			cutArray[j+cutNum] = charArray[j];			
		}*/
		
		System.arraycopy(charArray, (charArray.length-cutNum), cutArray, 0, cutNum);
		System.arraycopy(charArray, 0, cutArray, cutNum, (charArray.length-cutNum));
		
		System.out.println("Cut Array after cut:");
		for (int i=0;i<cutArray.length;i++){
			System.out.println(cutArray[i]);
		}
	}

}
