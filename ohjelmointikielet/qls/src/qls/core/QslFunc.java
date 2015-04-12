package qls.core;

import qls.syntaxtree.ReturnStatement;

public class QslFunc extends QslCmd {

	private ReturnStatement ret;
	
	public QslFunc(ReturnStatement r) {
		super();
		setRet(r);
	}

	public void setRet(ReturnStatement ret) {
		this.ret = ret;
	}

	public ReturnStatement getRet() {
		return ret;
	}

}
