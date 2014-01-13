
public class HoldemArrayException extends HoldemException {
	ArrayIndexOutOfBoundsException err = new ArrayIndexOutOfBoundsException();
	
	//constructor
	public HoldemArrayException(ArrayIndexOutOfBoundsException error){
		err = error;
	}
	
	// methods
	public String getMessage(){
		return err.toString();
	}
}
