package sunmaker.alphastorm;

public class Entity {

	public Entity() {
		x = 0;
		y = 0;
		velX = x;
		velY = y;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.velX = x;
		this.velY = y;
	}
	
	public float x;
	public float y;
	
	public float velX;
	public float velY;
	
}
