package cubegame.item;

import java.io.IOException;

import cubegame.CubeState;
import cubegame.CubeType;
import cubegame.Human;
import cubegame.Texture;
import cubegame.TextureLoader;
import cubegame.inventory.ItemStack;
import cubegame.map.CubeMap;

public class Item {
	public int itemID;
	public int shiftedIndex = 0;
	public Texture tex;
	
	public Item(int id, Texture t){
		itemID = id;
		shiftedIndex = id+256;
		this.tex = t;
		if (itemList[id] == null) {
			itemList[id] = this;
		} else {
			System.out.println("SOJV @ " + id);
		}
	}
	
	public Item(int id) {
		shiftedIndex = id;
		itemID = -1;
	}

	public void onUse(Human user){		
	}
	
	public boolean onDig(int x, int y, int z, CubeMap m, ItemStack s){
		return false;		
	}
	
	public boolean onPlace(int x, int y, int z, CubeMap m, ItemStack s){
		return false;		
	}
	
	public boolean onPlace(int x, int y, int z, Human h, CubeState t, ItemStack s){
		return false;		
	}
	
	public static Item getItemFromId(int id){
		if (id < 256) {
			return new BlockItem(id);
		}
		id -= 256;
		return itemList[id];
	}
	
	private static TextureLoader t = new TextureLoader();
	
	public static Item pickaxe;
	public static Item shovel;
	public static Item axe;
	public static Item diamondDrill;
	public static Item firstAid;
	public static Item wand;
	public static Item[] itemList = new Item[255];
	
	static{
		try {
			pickaxe = new ToolItem(0, 10, t.getTexture("res/textures/tex4000/hakku.png"), CubeType.materialStone);
			shovel = new ToolItem(1, 5, t.getTexture("res/textures/tex4000/lapio.png"), CubeType.materialDirt);
			axe = new ToolItem(2, 5, t.getTexture("res/textures/tex4000/kirves.png"), CubeType.materialWood);
			diamondDrill = new ToolItem(3, 5, t.getTexture("res/textures/tex4000/timanttipora.png"), CubeType.materialGlass);
			firstAid = new FoodItem(4, 1, t.getTexture("res/textures/tex4000/ensiapu.png"));
			wand = new WandItem(5, t.getTexture("res/textures/tex4000/taikasauva.png"));
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
}
