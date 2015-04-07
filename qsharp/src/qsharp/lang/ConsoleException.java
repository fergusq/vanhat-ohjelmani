package qsharp.lang;

public class ConsoleException extends QSharpException {

	public static final String CODE = "0x000000001";

	public ConsoleException(String why) {
		super(why);
		code = CODE;
	}

	public String getReason() {
		String s = super.getReason();
		
		return "ConsoleException ("+code+") Reason: " + s;
		
	}
	
}
