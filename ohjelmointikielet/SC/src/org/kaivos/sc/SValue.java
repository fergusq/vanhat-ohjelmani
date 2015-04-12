package org.kaivos.sc;

public interface SValue {
	public String toString();
	
	/**
	 * 
	 * @return an int representation of the object
	 */
	public int toInt();
	
	/**
	 * 
	 * @return a double representation of the object
	 */
	public double toDouble();
	
	/**
	 * 
	 * @return a double representation of the object
	 */
	public boolean toBoolean();
}
