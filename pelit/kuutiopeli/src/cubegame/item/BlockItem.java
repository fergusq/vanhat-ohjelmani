package cubegame.item;


import cubegame.CubeState;
import cubegame.CubeType;
import cubegame.Human;
import cubegame.inventory.ItemStack;

public class BlockItem extends Item {

	public BlockItem(int id) {
		super(id);
	}
	
	@Override
	public boolean onPlace(int x, int y, int z, Human h, CubeState t, ItemStack s) {
		//Mouse.setGrabbed(false);
		t.addCube(x, y, z, CubeType.getTypeFromNumber(shiftedIndex));
		s.setStackSize(s.getStackSize()-1);
		if (s.getStackSize() == 0) s.setItem(new Item(0));
		return true;
	}	

}
