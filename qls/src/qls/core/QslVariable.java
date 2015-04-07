package qls.core;

public class QslVariable  extends QslObject{
	private String variableName = null;
	       private QslValue variableValue = null;
	       
	       /**
	        * Get a variable name
	        * @return String
	        */
	       public String getVariableName() {
	           return variableName;
	       }
	       
	       /**
	        * Set a name for variable
	        * @param variableName
	        */
	       public void setVariableName(String variableName) {
	           this.variableName = variableName;
	       }
	       
	       /**
	        * Get a variable value
	        * @return StticValue
	        */
	       public QslValue getVariableValue() {
	           return variableValue;
	       }
	       
	       /**
	        * Set a value to variable
	        * @param variableValue
	        */
	       public void setVariableValue(QslValue variableValue) {
	           this.variableValue = variableValue;
	       }
}
