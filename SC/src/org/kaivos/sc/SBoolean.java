package org.kaivos.sc;

public class SBoolean implements SValue {

	private boolean value;
	
	public SBoolean(boolean i) {
		value = i;
	}
	
	public void setValue(boolean i) {
		value = i;
	}
	
	@Override
	public String toString() {
		return value+"";
	}
	
	@Override
	public int toInt() {
		return value ? 1:0;
	}

	@Override
	public double toDouble() {
		return value ? 1:0;
	}
	
	@Override
	public boolean toBoolean() {
		return value;
	}

}
