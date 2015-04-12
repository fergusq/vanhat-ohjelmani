package cubegame.item;

import cubegame.Cube;
import cubegame.CubeState;
import cubegame.CubeType;
import cubegame.Human;
import cubegame.Texture;
import cubegame.inventory.HotbarInventory;
import cubegame.inventory.ItemStack;
import cubegame.map.CubeMap;

public class WandItem extends Item {

	public WandItem(int id, Texture t) {
		super(id, t);
	}

	@Override
	public boolean onPlace(int x, int y, int z, Human h, CubeState t, ItemStack s) {
		t.addCube(x, y, z, CubeType.getTypeFromNumber(h.spellId));
		return true;
	}	
	
	public static final HotbarInventory items;
	
	static {
		items = new HotbarInventory();
		items.setItem(0, Cube.STONE_1);
		items.setItem(1, Cube.STONE_2);
		items.setItem(2, Cube.WOOD);
		items.setItem(3, Cube.DIRT);
		items.setItem(4, Cube.GLASS);
		items.setItem(5, Cube.LIGHT);
		items.setItem(6, Cube.WATER);
		items.setItem(7, Cube.LAVA);
	}
	
}
