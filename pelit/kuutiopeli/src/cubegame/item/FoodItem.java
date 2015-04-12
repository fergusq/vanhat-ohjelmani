package cubegame.item;

import cubegame.Human;
import cubegame.Texture;

public class FoodItem extends Item {

	private int food;

	public FoodItem(int id, int food, Texture t) {
		super(id, t);
		this.food = food;
	}

	public void onUse(Human user){
		user.health += food;
	}
	
}
