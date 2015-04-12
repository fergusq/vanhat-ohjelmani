import java.util.Scanner;

public class pelaaja {
	int sijaintix;
	int sijaintiy;
	int taulunKoko;
	
	Scanner syote = new Scanner(System.in);

	
	public pelaaja(int sijaintiy, int sijaintix, int taulunḰoko) {
		setSijaintiy(sijaintiy);
		setSijaintix(sijaintix);
		setTaulunKoko(taulunḰoko);
	}
	
	public void setSijaintix(int sijaintix) {
		this.sijaintix = sijaintix;
	}
	
	public void setSijaintiy(int sijaintiy) {
		this.sijaintiy = sijaintiy;
	}
	
	public int getSijaintiy() {
		return sijaintiy;
	}
	
	public int getSijaintix() {
		return sijaintix;
	}
	public void setTaulunKoko(int taulunKoko){
		this.taulunKoko = taulunKoko;
	}
	
	public int getTaulunKoko(){
		return taulunKoko;
	}
	
	public int[] StS() {
		int sijaintix = this.sijaintix;
		int sijaintiy = this.sijaintiy;
		int[] sijainti = {0, 0};
		
		int suunta;
		System.out.println("Suunta:");
		suunta = syote.nextInt();
		switch (suunta){
		case 6:
			if(sijaintix == taulunKoko - 1)
				break;
			sijaintix++;
			break;
		case 4:
			if(sijaintix == 0)
				break;
			sijaintix--;
			break;
		case 8:
			if(sijaintiy == 0)
				break;
			sijaintiy--;
			break;
		case 2:
			if(sijaintiy == taulunKoko - 1)
				break;
			sijaintiy++;
			break;
		default:
			System.out.println("Syötä: 2 = alas, 8 = ylös, 4 = vasemmalle, 6 = oikealle. " + suunta);
		}
		sijainti[0] = sijaintix;
		sijainti[1] = sijaintiy;
		return sijainti;
		
	}
}
