package cubegame.inventory;

import cubegame.item.Item;

public class ItemContainer {
	ItemStack[] items;
	public ItemContainer(int size)
	{
		items = new ItemStack[size];
	}
	public ItemStack getItem(int index) {
		if (index > items.length || index < 0)
		{
			return null;
		}
		return items[index];
	}
	public void setItem(int index, int item){
		if (index > items.length || index < 0)
		{
			return;
		}
		items[index] = new ItemStack(Item.getItemFromId(item), 1);
	}
	public void setItem(int index, int item, int size){
		if (index > items.length || index < 0)
		{
			return;
		}
		items[index] = new ItemStack(Item.getItemFromId(item), size);
	}
	public void setItem(int index, ItemStack i) {
		if (index > items.length || index < 0)
		{
			return;
		}
		items[index] = i;
	}
}
