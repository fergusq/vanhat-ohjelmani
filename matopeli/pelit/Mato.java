package pelit;

import java.util.Scanner;

public class Mato {
	int sijaintix[];
	int sijaintiy[];
	int taulunKoko;
	int pituus;

	Scanner syote = new Scanner(System.in);

	
	public Mato(int sijaintiy[], int sijaintix[], int taulunḰoko, int pituus) {
		int[] y = sijaintiy;
		int[] x = sijaintix;
		setSijaintiy(y);
		setSijaintix(x);
		setTaulunKoko(taulunḰoko);
		setPituus(pituus);
	}
	
	public void setSijaintix(int[] sijaintix) {
		this.sijaintix = sijaintix;
	}
	
	public void setSijaintiy(int[] sijaintiy) {
		this.sijaintiy = sijaintiy;
	}
	
	public int[] getSijaintiy() {
		int[] y = new int[this.pituus];
		System.arraycopy(this.sijaintiy, 0, y, 0, getPituus());
		return y;
	}
	
	public int[] getSijaintix() {
		int[] x = new int[this.pituus];
		System.arraycopy(this.sijaintix, 0, x, 0, getPituus());
		return sijaintix;
	}
	public void setTaulunKoko(int taulunKoko){
		this.taulunKoko = taulunKoko;
	}
	
	public int getTaulunKoko(){
		return taulunKoko;
	}
	
	public int getPituus() {
		return pituus;
	}

	public void setPituus(int pituus) {
		this.pituus = pituus;
	}
	
	public void korotaPituus(){
		int[] x = new int[this.pituus + 1];
		System.arraycopy(getSijaintix(), 0, x, 0, getPituus());
		int[] y = new int[this.pituus + 1];
		System.arraycopy(getSijaintiy(), 0, y, 0, getPituus());
		
		this.pituus += 1;
		
		int[] sijaintiX = new int[this.pituus];
		int[] sijaintiY = new int[this.pituus];
		
		sijaintiY = y;
		sijaintiX = x;
		
		sijaintiX[getPituus()-1] = sijaintiX[getPituus()-2];
		sijaintiY[getPituus()-1] = sijaintiY[getPituus()-2];
		
		sijaintix = new int[this.pituus];
		sijaintiy = new int[this.pituus];
		
		System.arraycopy(sijaintiX, 0, this.sijaintix, 0, getPituus());
		System.arraycopy(sijaintiY, 0, this.sijaintiy, 0, getPituus());
	}
	
	public boolean onkoSolmussa(){
		boolean vastaus = false;
		
		int[] x = new int[this.pituus + 1];
		System.arraycopy(getSijaintix(), 0, x, 0, getPituus());
		int[] y = new int[this.pituus + 1];
		System.arraycopy(getSijaintiy(), 0, y, 0, getPituus());
		
		for(int i = 1; i < getPituus(); i++){
			if(y[i] == y[0] && x[i] == x[0]){
				vastaus = true;
				break;
			}
		}
		
		return vastaus;
	}
	
	public void StS() {
		int[] sijaintiX = new int[this.pituus];
		System.arraycopy(getSijaintix(), 0, sijaintiX, 0, getPituus());
		int[] sijaintiY = new int[this.pituus];
		System.arraycopy(getSijaintiy(), 0, sijaintiY, 0, getPituus());
		int[] x = new int[this.pituus];
		System.arraycopy(getSijaintix(), 0, x, 0, getPituus());
		int[] y = new int[this.pituus];
		System.arraycopy(getSijaintiy(), 0, y, 0, getPituus());
		//int pituus = this.pituus;
		
		boolean vaara = false;
		
		int suunta;
		System.out.print("Suunta: ");
		suunta = syote.nextInt();
		switch (suunta){
		case 6:
			if(sijaintiX[0] == taulunKoko - 1)
				break;
			sijaintiX[0]++;
			break;
		case 4:
			if(sijaintiX[0] == 0)
				break;
			sijaintiX[0]--;
			break;
		case 8:
			if(sijaintiY[0] == 0)
				break;
			sijaintiY[0]--;
			break;
		case 2:
			if(sijaintiY[0] == taulunKoko - 1)
				break;
			sijaintiY[0]++;
			break;
		default:
			System.out.println("Syötä: 2 = alas, 8 = ylös, 4 = vasemmalle, 6 = oikealle.");
			vaara = true;
		}
		if (vaara != true){
			for(int i = 0; i < pituus-1; i++){
				int X = x[i];
				int Y = y[i];
				sijaintiX[i+1] = X;
				sijaintiY[i+1] = Y;
			}
		}
		
		
		/*sijaintiX[1] = x[0];
		sijaintiX[2] = x[1];
		sijaintiX[3] = x[2];
		sijaintiY[1] = y[0];
		sijaintiY[2] = y[1];
		sijaintiY[3] = y[2];*/
		
		this.sijaintix = sijaintiX;
		this.sijaintiy = sijaintiY;
		
	}
	public void StS(int suunta) {
		int[] sijaintiX = new int[this.pituus];
		System.arraycopy(getSijaintix(), 0, sijaintiX, 0, getPituus());
		int[] sijaintiY = new int[this.pituus];
		System.arraycopy(getSijaintiy(), 0, sijaintiY, 0, getPituus());
		int[] x = new int[this.pituus];
		System.arraycopy(getSijaintix(), 0, x, 0, getPituus());
		int[] y = new int[this.pituus];
		System.arraycopy(getSijaintiy(), 0, y, 0, getPituus());
		//int pituus = this.pituus;
		
		boolean vaara = false;
		
		switch (suunta){
		case 6:
			if(sijaintiX[0] == taulunKoko - 1)
				break;
			sijaintiX[0]++;
			break;
		case 4:
			if(sijaintiX[0] == 0)
				break;
			sijaintiX[0]--;
			break;
		case 8:
			if(sijaintiY[0] == 0)
				break;
			sijaintiY[0]--;
			break;
		case 2:
			if(sijaintiY[0] == taulunKoko - 1)
				break;
			sijaintiY[0]++;
			break;
		default:
			System.out.println("Syötä: 2 = alas, 8 = ylös, 4 = vasemmalle, 6 = oikealle.");
			vaara = true;
		}
		if (vaara != true){
			for(int i = 0; i < pituus-1; i++){
				int X = x[i];
				int Y = y[i];
				sijaintiX[i+1] = X;
				sijaintiY[i+1] = Y;
			}
		}
		
		
		/*sijaintiX[1] = x[0];
		sijaintiX[2] = x[1];
		sijaintiX[3] = x[2];
		sijaintiY[1] = y[0];
		sijaintiY[2] = y[1];
		sijaintiY[3] = y[2];*/
		
		this.sijaintix = sijaintiX;
		this.sijaintiy = sijaintiY;
		
	}
}
