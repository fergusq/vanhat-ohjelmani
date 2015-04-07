package cubegame.item;

import cubegame.CubeType;
import cubegame.Texture;
import cubegame.inventory.ItemStack;
import cubegame.map.CubeMap;

public class ToolItem extends Item {
	public int strength;
	public int material;
	
	public ToolItem(int id, int str, Texture texture, int material) {
		super(id, texture);
		this.strength = str;
		this.material = material;
	}

	public boolean onDig(int x, int y, int z, CubeMap m, ItemStack s){
		if (CubeType.getTypeFromNumber(m.getCubeType(x, y, z)).getMaterial() == this.material){
			return true;
		}
		return false;
	}
}
