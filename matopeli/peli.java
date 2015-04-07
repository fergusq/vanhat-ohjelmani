import java.io.IOException;
import java.util.Scanner;


public class peli {

	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public static void main(String[] args) {
		Scanner scann = new Scanner(System.in);
		
		System.out.println("Neliö:");
		int taulunKoko = (int) scann.nextDouble();
		String sijainti = "ru1";
		pelaaja pelaaja1 = new pelaaja(0, 0, taulunKoko);
		taulu taulu1 = new taulu("", taulunKoko ,0, 0);
		while(sijainti != "ru4"){
			try {
			taulu1 = new taulu("", taulunKoko ,pelaaja1.getSijaintiy(), pelaaja1.getSijaintix());
			System.out.println(taulu1.toString());
			int[] sijaintiq = pelaaja1.StS();
			pelaaja1.setSijaintix(sijaintiq[0]);
			pelaaja1.setSijaintiy(sijaintiq[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
