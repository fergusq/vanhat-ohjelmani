package qsharp.lang;

public class ArithmeticException extends QSharpException {

	public static final String CODE = "0x000000003";
	
	public ArithmeticException(String why) {
		super(why);
		code = CODE;
	}

	public String getReason() {
		String s = super.getReason();
		
		return "ArithmeticException ("+code+") Reason: " + s;
		
	}

}
