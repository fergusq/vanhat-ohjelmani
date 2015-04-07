package qsharp.lang;

public class QSharpException {

	public static final String CODE = "0x000000000";
	
	private String reason;
	protected String code;
	
	
	public QSharpException(String why) {
		this.setReason(why);
		code = CODE;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getReason() {
		return reason;
	}
	
}
