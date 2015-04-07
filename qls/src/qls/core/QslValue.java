package qls.core;

public class QslValue extends QslObject{
	private Class type  = null;
	       private long value = 0;
	       private String valueS = null;
	       
	       /**
	        * Get a value class type
	        * @return Class
	        */
	       public Class getType() {
	           if ( type != null )
	               return type;
	           return long.class;
	       }
	       
	       /**
	        * Set value class type
	        * @param type
	        */
	       public void setType(Class type) {
	           this.type = type;
	       }
	       
	       /**
	        * Get value as long (remember this language can operate just integers,
	        * if you don't know it please read the artile of this tutorial)
	        * 
	        * @return long
	        */
	       public String getValue() {
	           return valueS != null ? valueS : value+"";
	       }
	       
	       /**
	        * Set value as long
	        * @param value
	        */
	       public void setValue(long value) {
	           this.value = value;
	       }
	       
	       /**
	        * Set value as string
	        * @param value
	        */
	       public void setValue(String value) {
	           this.valueS = value;
	       }
}
