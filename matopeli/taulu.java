
public class taulu {
	String ru1;
	String ru2;
	String ru3;
	String ru4;
	String tauluq;
	String[][] Taulukko;
	int taulunKoko;
	
	public taulu(String pelaajanSijainti, int taulunKoko, int y, int x){
		if(pelaajanSijainti == "ru1"){
			ru1 = "+";
		}
		else
			ru1 = "#";
		if(pelaajanSijainti == "ru2"){
			ru2 = "+";
		}
		else
			ru2 = "#";
		if(pelaajanSijainti == "ru3"){
			ru3 = "+";
		}
		else
			ru3 = "#";
		if(pelaajanSijainti == "ru4"){
			ru4 = "+";
		}
		else
			ru4 = "#";
		
		setTaulunKoko(taulunKoko);
		
		Taulukko = new String[taulunKoko][taulunKoko];
		
		int i;
		int t;
		for(i = 0; i < taulunKoko; i++){
			for (t = 0; t < taulunKoko; t++) {
				if ((i != y) || (t != x)) {
					Taulukko[i][t] = "#";
				}
				else
					Taulukko[i][t] = "+";
			}
		}
			
	}
	
	public void setTaulunKoko(int taulunKoko){
		this.taulunKoko = taulunKoko;
	}
	
	public int getTaulunKoko(){
		return taulunKoko;
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
