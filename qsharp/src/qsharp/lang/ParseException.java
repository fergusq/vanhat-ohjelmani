package qsharp.lang;

public class ParseException extends QSharpException {

	public static final String CODE = "0x000000004";
	
	public ParseException(String why) {
		super(why);
		code = CODE;
	}

	public String getReason() {
		String s = super.getReason();
		
		return "ParseException ("+code+") Reason: " + s;
		
	}
	
}
