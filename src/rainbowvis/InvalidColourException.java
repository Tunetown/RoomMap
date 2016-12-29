package rainbowvis;

public class InvalidColourException extends RainbowException {

	private static final long serialVersionUID = 5801441252925805756L;
	
	private String nonColor;
	
	public InvalidColourException(String nonColour){
		super();
		nonColor = nonColour;
	}
	
	public String getMessage() {
		return nonColor + " is not a valid colour.";
	}
	
}
