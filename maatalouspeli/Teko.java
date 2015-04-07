
public class Teko {
	private int x;
	private int y;
	private Työkalu väline;
	int vuosi;
	
	public Teko(int x, int y, Työkalu väline, int vuosi) {
		super();
		this.x = x;
		this.y = y;
		this.väline = väline;
		this.vuosi = vuosi;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Työkalu getVäline() {
		return väline;
	}

	public void setVäline(Työkalu väline) {
		this.väline = väline;
	}
	
	
}
