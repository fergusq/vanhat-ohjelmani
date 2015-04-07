public class Matotaulu {
	String ru1;
	String ru2;
	String ru3;
	String ru4;
	String tauluq;
	String[][] Taulukko;
	int taulunKoko;
	int madonKoko;
	
	public Matotaulu(int madonKoko, int taulunKoko, int[] y, int[] x){
		setTaulunKoko(taulunKoko);
		
		setMadonKoko(madonKoko);
		
		Taulukko = new String[taulunKoko][taulunKoko];
		
		for(int i = 0; i < taulunKoko; i++){
			for (int t = 0; t < taulunKoko; t++) {
					Taulukko[i][t] = ".";
			}
		}
		Taulukko[y[0]][x[0]] = "+";
		for(int i = 1; i < madonKoko; i++){
			Taulukko[y[i]][x[i]] = "#";
		}
	}
	public Matotaulu(int madonKoko, int taulunKoko, int[] y, int[] x, int[] rY, int[] rX, SijainninLuokka[] rL){
		setTaulunKoko(taulunKoko);
		
		setMadonKoko(madonKoko);
		
		Taulukko = new String[taulunKoko][taulunKoko];
		
		for(int i = 0; i < taulunKoko; i++){
			for (int t = 0; t < taulunKoko; t++) {
					Taulukko[i][t] = ".";
			}
		}
		
		for(int i = 0; i < rY.length; i++){
			switch (rL[i]) {
			case o:
				Taulukko[rY[i]][rX[i]] = "o";
				break;
			case c:
				Taulukko[rY[i]][rX[i]] = "c";
				break;
			case ö:
				Taulukko[rY[i]][rX[i]] = "ö";
				break;
			case ja:
				Taulukko[rY[i]][rX[i]] = "&";
				break;
			default:
				break;
			}
			
			
		}
		
		Taulukko[y[0]][x[0]] = "+";
		for(int i = 1; i < madonKoko; i++){
			Taulukko[y[i]][x[i]] = "#";
		}
	}
	
	public void setTaulunKoko(int taulunKoko){
		this.taulunKoko = taulunKoko;
	}
	
	public int getTaulunKoko(){
		return taulunKoko;
	}
	
	public void setMadonKoko(int madonKoko){
		this.madonKoko = madonKoko;
	}
	
	public int getMadonKoko(){
		return madonKoko;
	}
	
	public String toString() {
		String tiedot = "";
		int i;
		int t;
		for(i = 0; i < taulunKoko; i++){
			for (t = 0; t < taulunKoko; t++) {
					tiedot = tiedot + Taulukko[i][t];
			}
			tiedot = tiedot + "\n";
		}
		return tiedot;
	}
}
