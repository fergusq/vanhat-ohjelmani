package cubegame.map.level;

import java.util.ArrayList;

import cubegame.CubeState;
import cubegame.Entity;
import cubegame.map.BasicMapGenerator;
import cubegame.npc.Spirit;
import cubegame.render.BoxRenderer;

public class EntitiedLevel extends Level {

	public ArrayList<Entity> entities = new ArrayList<Entity>();

	public EntitiedLevel(int size) {
		super(size);
		
	}

	@Override
	public void init(CubeState mng) {
		BasicMapGenerator gen = new BasicMapGenerator();
		gen.generateLevel(this);
		setCubeType(0, 8, 0, 1);
		setCubeType(1, 8, 0, 1);
		setCubeType(1, 8, 1, 1);
		setCubeType(0, 8, 1, 1);
		setCubeType(-1, 8, 0, 1);
		setCubeType(-1, 8, 1, 1);
		setCubeType(0, 8, -1, 1);
		setCubeType(1, 8, -1, 1);
		setCubeType(-1, 8, -1, 1);
	}

	@Override
	public boolean isComplete(int delta, CubeState mng) {

		return false;
	}

	@Override
	public void render(int delta, CubeState mng) {
		if (!mng.isMultiplayer && !entities.isEmpty()){
			for (Entity e : entities){
				if (e instanceof Spirit){
					Spirit s = (Spirit) e;
					BoxRenderer.renderBiped(-s.getPositionX(), -s.getPositionY(), -s.getPositionZ(), 
						s.getRotationY(), s.getPitch(), s.legRot);
					s.render();
				}
				
			}
		}
	}

	@Override
	public void update(int delta, CubeState mng) {
		if (!mng.isMultiplayer) {
			for (Entity e : entities) {
				if (e instanceof Spirit) {
					Spirit s = (Spirit) e;
					s.moveUpdate(mng, delta);
				}
			}

		}

	}

	public void removeEntities(ArrayList<Entity> removeList) {
		entities.removeAll(removeList);
	}
	
	public void addEntities(ArrayList<Entity> addList) {
		entities.addAll(addList);
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
		
	}
	public void removeEntity(Entity entity) {
		entities.remove(entity);
		
	}

}
