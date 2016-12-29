package rainbowvis;

public class NumberRangeException extends RainbowException {
	
	private static final long serialVersionUID = 4165381497766700805L;
	
	private double minNum;
	private double maxNum;	
	
	public NumberRangeException (double minNumber, double maxNumber) {
		super();
		minNum = minNumber;
		maxNum = maxNumber;
	}
	
	public String getMessage() {
		return "maxNumber (" + maxNum + ") is not greater than minNumber (" + minNum + ")";
	}

}
