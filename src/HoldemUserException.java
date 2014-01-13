
public class HoldemUserException extends HoldemException {
	String err = new String();
	
	//constructor
	public HoldemUserException(String error){
		err = error;
	}
	
	// methods
	public String getMessage(){
		return err;
	}

}
