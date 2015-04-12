package cubegame.npc.model;

import geometry.Box;

public class HumanoidModel extends Model {

	public HumanoidModel(){
		addHexahedron(0f, 0.5f, 0f, 0.5f, 1.5f, 0.5f);
		addHexahedron(0f, 2f, 0f, 0.5f, 0.5f, 0.5f);
	}
	
	@Override
	public void render() {
		for (int i = 0; i < modelMap.size(); i++) {

			if (modelMap.get(i) instanceof Box) {

				Box b = modelMap.get(i);
				
				drawFace(b.getTop());
			}
		}
	}

}
