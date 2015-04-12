package cubegame.physics;

import cubegame.CubeType;

public class HalfPhysicCube extends PhysicCube {

	public HalfPhysicCube(float x, float y, float z) {
		super(x, y, z);
	}

	public HalfPhysicCube(float x, float y, float z, boolean isSelectFrame) {
		super(x, y, z, isSelectFrame);
	}

	public HalfPhysicCube(float x, float y, float z, CubeType type) {
		super(x, y, z, type);
	}

	public HalfPhysicCube(float x, float y, float z, int type, float size) {
		super(x, y, z, type, size);
	}
	
	

}
