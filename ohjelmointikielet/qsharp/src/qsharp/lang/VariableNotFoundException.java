package qsharp.lang;

public class VariableNotFoundException extends QSharpException {

	public static final String CODE = "0x000000002";

	public VariableNotFoundException(String why) {
		super(why);
		code = CODE;
	}

	public String getReason() {
		String s = super.getReason();
		
		return "VariableNotFoundException ("+code+") Reason: " + s;
		
	}
	
}
