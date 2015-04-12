package cubegame.map;

import cubegame.Cube;

public enum MapLayer {
	AIR(0, 128),
	GRASS(Cube.GRASS, 64),
	SEALEVEL(Cube.WATER, 64),
	STONE(Cube.STONE_2, 32);
	
	private int level;
	private int type;

	private MapLayer(int type, int level){
		this.level = level;
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
