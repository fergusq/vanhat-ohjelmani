package sunmaker.alphastorm;

public class GameMap {
	
	public GameMap() {
		height = 8;
		width = 32;
		data = new int[height][width];
	}
	
	public GameMap(int x, int y) {
		height = x;
		width = y;
		data = new int[height][width];
	}
	
	public int getNode(int x, int y) {
		if (y < 0 || y >= height) return 0;
		if (x < 0 || x >= width) return 0;
		return data[y][x];
	}
	
	public void setNode(int x, int y, int t) {
		if (y < 0 || y >= height) return;
		if (x < 0 || x >= width) return;
		data[y][x] = t;
	}
	
	public int width;
	public int height;
	
	private int[][] data;

	public void clear() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				data[y][x] = 0;
			}
		}
		
	}
}
