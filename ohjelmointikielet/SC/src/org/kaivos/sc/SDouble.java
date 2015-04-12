package org.kaivos.sc;

public class SDouble implements SValue {

	private double value;
	
	public SDouble(double i) {
		value = i;
	}
	
	public void setValue(double i) {
		value = i;
	}
	
	@Override
	public String toString() {
		return value+"";
	}
	
	@Override
	public int toInt() {
		return (int) Math.floor(value);
	}

	@Override
	public double toDouble() {
		return value;
	}
	
	@Override
	public boolean toBoolean() {
		return value != 0;
	}

	public static SValue toSValue(double d) {
		if (d-((int)Math.floor(d)) > 0) {
			return new SDouble(d);
		} else return new SInteger((int)Math.floor(d));
	}
	
}