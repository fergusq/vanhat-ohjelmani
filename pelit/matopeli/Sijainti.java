
public class Sijainti {
	private int y;
	private int x;
	private int mones;
	private boolean solmu;
	private SijainninLuokka luokka;
	
	public Sijainti(int y, int x, int mones, boolean solmu, SijainninLuokka luokka){
		this.y = y;
		this.x = x;
		this.mones = mones;
		this.solmu = solmu;
		this.luokka = luokka;
	}
	
	
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public int getX() {
		return x;
	}

	public void setMones(int mones) {
		this.mones = mones;
	}
	public int getMones() {
		return mones;
	}


	public void setSolmu(boolean solmu) {
		this.solmu = solmu;
	}
	public boolean solmussa() {
		return solmu;
	}
	
	public void setLuokka(SijainninLuokka luokka) {
		this.luokka = luokka;
	}
	public SijainninLuokka getLuokka() {
		return luokka;
	}
}
