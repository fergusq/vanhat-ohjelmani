package org.kaivos.sc;

public class SInteger implements SValue {

	private int value;
	
	public SInteger(int i) {
		value = i;
	}
	
	public void setValue(int i) {
		value = i;
	}
	
	@Override
	public String toString() {
		return value+"";
	}
	
	@Override
	public int toInt() {
		return value;
	}

	@Override
	public double toDouble() {
		return value;
	}
	
	@Override
	public boolean toBoolean() {
		return value != 0;
	}

}
