package sunmaker.alphastorm;

public class Node {

	public int x;
	public int y;
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static int EMPTY = 0;
	public static int STONE = 1;
	public static int POINT = 2;
	public static int FLYPOINT = 3;
	public static int FIRE = 4;
	
}
