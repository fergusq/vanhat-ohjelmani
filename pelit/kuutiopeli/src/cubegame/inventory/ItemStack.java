package cubegame.inventory;

import cubegame.item.Item;

public class ItemStack {

	private Item item;
	private int stackSize;
	
	public ItemStack(Item item, int stackSize) {
		super();
		this.item = item;
		this.stackSize = stackSize;
	}
	
	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public int getStackSize() {
		return stackSize;
	}
	public void setStackSize(int stackSize) {
		if (stackSize < 0) stackSize = 0;
		this.stackSize = stackSize;
	}
	
}
