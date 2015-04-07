package cubegame.multiplayer;

import java.io.Serializable;

public class HumanMPPacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private float X;
	private float Y;
	private float Z;
	
	private String name;

	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
	}

	public float getZ() {
		return Z;
	}

	public void setZ(float z) {
		Z = z;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HumanMPPacket(float x, float y, float z, String name) {
		super();
		X = x;
		Y = y;
		Z = z;
		this.name = name;
	}

}
