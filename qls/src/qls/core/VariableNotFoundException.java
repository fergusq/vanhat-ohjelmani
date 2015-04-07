package qls.core;

public class VariableNotFoundException extends QslException {

	public VariableNotFoundException(String message) {
		super(message);
		this.type = noVariable;
		this.code = "0x000000001";
	}

}
