package qls.core;

public class QslException {
	
	private String msg;
	protected String type;
	protected String code;
	
	public QslException(String message) {
		this.msg = message;
		this.type = exception;
		this.code = "0x000000000";
	}
	
	public void send() throws Exception {
		throw new LanguageException(
				"Unhandled exception! (" + code + ")\n" +
				this.msg);
	}
	
	protected static String exception = "Exception";
	protected static String noVariable = "VariableNotFoundException";
}
