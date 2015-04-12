package qls.core;

import qls.syntaxtree.StatementExpression;

public class QslCmd extends QslVariable {
	private StatementExpression[] cmd;

	/**
     * Get a variable value
     * @return StticValue
     */
    public StatementExpression[] getCmd() {
        return cmd;
    }
    
    /**
     * Set a value to variable
     * @param variableValue
     */
    public void setCmd(StatementExpression[] variableValue) {
        this.cmd = variableValue;
    }
}
