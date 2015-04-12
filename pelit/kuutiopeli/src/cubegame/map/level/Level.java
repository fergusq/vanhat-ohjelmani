package cubegame.map.level;

import cubegame.CubeState;
import cubegame.map.CubeMap;

public abstract class Level extends CubeMap {

	public Level(int size) {
		super(size);
		
	}
	
	public abstract void init(CubeState mng);
	
	public abstract void update(int delta, CubeState mng);
	
	public abstract void render(int delta, CubeState mng);
	
	public abstract boolean isComplete(int delta, CubeState mng);

}
