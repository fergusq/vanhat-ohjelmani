import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class matopeli {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public static void main(String[] args) {
		Scanner scann = new Scanner(System.in);
		PrintWriter kirjuri = new PrintWriter(System.console().writer());
		kirjuri.println(" __  __    _  _____ ___  ____  _____ _     ___ \n"+"|  \\/  |  / \\|_   _/ _ \\|  _ \\| ____| |   |_ _|\n"+"| |\\/| | / _ \\ | || | | | |_) |  _| | |    | | \n"+"| |  | |/ ___ \\| || |_| |  __/| |___| |___ | | \n"+"|_|  |_/_/   \\_\\_| \\___/|_|   |_____|_____|___|");
		kirjuri.flush();
		System.out.println("Syötä matosi pituus. Huomaa, että taulun neliö on 10. Madon pitää suurempi tai yhtäsuuri kuin 3.");
		System.out.print("Pituus: ");
		int pituus = 0;
		try{
			pituus = scann.nextInt();
		}
		catch (InputMismatchException ex){
			System.out.println("Madon pituuden tulee olla tasaluku, eikä se saa sisältää kirjaimia.");
		}
		int aloituspituus = pituus;
		System.out.println("Neliö: 10");
		int taulunKoko = 10;
		int[] y = new int[pituus];
		int[] x = new int[pituus];
		y[0] = 0;
		y[1] = 0;
		y[2] = 0;
		x[0] = 3;
		x[1] = 2;
		x[2] = 1;
		for(int i = 3; i < pituus; i++){
			y[i] = 0;
			x[i] = 0;
		}
		Mato pelaaja1 = new Mato(y, x, taulunKoko, pituus);
		Matotaulu taulu1 = new Matotaulu(pituus, taulunKoko, y, x);
		int i;
		for(i = 0; pelaaja1.onkoSolmussa() == false; i++){
			try {
			taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix());
			System.out.println(taulu1.toString());
			System.out.println("Aika (siirtoina): " + i + ". Matosi pituus: " + pituus + ". Pisteet: " + (i - 1)/aloituspituus);
			pelaaja1.StS();
			if ((i % 5) == 0){
				pelaaja1.korotaPituus();
				pituus += 1;
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (pelaaja1.onkoSolmussa() == true){
			System.out.println("Törmäsit! Tuloksesi: " + (i - 1)/aloituspituus);
		}
		kirjuri.close();
	}
	
	

}
