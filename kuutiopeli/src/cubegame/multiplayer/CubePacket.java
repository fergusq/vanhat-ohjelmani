package cubegame.multiplayer;

import java.io.Serializable;

import cubegame.CubeType;

public class CubePacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private float X;
	private float Y;
	private float Z;
	
	public CubePacket(float x, float y, float z, CubeType type) {
		super();
		X = x;
		Y = y;
		Z = z;
		this.type = type;
	}

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

	public CubeType getType() {
		return type;
	}

	public void setType(CubeType type) {
		this.type = type;
	}

	private CubeType type;

}
