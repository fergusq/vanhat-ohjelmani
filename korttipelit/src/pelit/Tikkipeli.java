package pelit;

import hallinta.Kortti;
import hallinta.Korttipakka;
import hallinta.Käsi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Tikkipeli {

	public int pelaajia = 4;
	public ArrayList<Käsi> pelaajat;
	public Korttipakka pakka = new Korttipakka();
	public int pelaaja;
	public boolean vinkit;
	private Scanner sc = new Scanner(System.in);
	
	@Override
	protected void finalize() throws Throwable {
		sc.close();
		super.finalize();
	}
	
	public Tikkipeli(int pelaajat) {
		pakka.sekoita();
		pelaajia = pelaajat;
		this.pelaajat = new ArrayList<Käsi>();
		for (int i = 0; i < pelaajia; i++) {
			Käsi k = new Käsi();
			k.otaKortti(pakka.jaaKortti());
			k.otaKortti(pakka.jaaKortti());
			k.otaKortti(pakka.jaaKortti());
			k.otaKortti(pakka.jaaKortti());
			k.otaKortti(pakka.jaaKortti());
			this.pelaajat.add(k);
		}
		Random r = new Random();
		pelaaja = r.nextInt(pelaajia);
	}
	
	public int kierros(int aloittaja) {
		Käsi pöytä = new Käsi();
		int arvo = 0; /* Suurin pöytään laitettu kortti */
		int voittaja = 0; /* Suurimman kortin laittaja */
		Kortti aloitus;
		
		if (aloittaja == pelaaja) {
			Käsi k = pelaajat.get(pelaaja);
			print("Aloitat!");
			print("Kätesi: ");
			for (int i = 0; i < k.getKortit().size(); i++) {
				print(i + ": " + k.getKortit().get(i));
			}
			print("Minkä kortin lyöt?");
			int valinta = Integer.parseInt(lue());
			aloitus = pelaajat.get(aloittaja).jaaKortti(valinta);
			pöytä.otaKortti(aloitus);
			arvo = aloitus.getArvo(); // Voittajan kortin arvo
			voittaja = aloittaja;
			print(aloittaja + " (Aloittaja): " + aloitus);
		} else {
			aloitus = pelaajat.get(aloittaja).jaaKortti(pelaajat.get(aloittaja).suurin());
			pöytä.otaKortti(aloitus);
			arvo = aloitus.getArvo(); // Voittajan kortin arvo
			voittaja = aloittaja;
			print("Pelaaja " + aloittaja + " (Aloittaja): " + aloitus);
		}
		
		
		
		for (int i = (aloittaja != pelaajat.size()-1 ? aloittaja+1 : 0); i != aloittaja; i=( i<pelaajat.size()-1 ? i+1 : 0)) {
			if (i == aloittaja) continue;
			Käsi k = pelaajat.get(i);
			int sijainti = k.suurinSamaaMaata(aloitus.getMaa()); // Tekoäly JA vinkit: Joko suurempi samaa maata, muulloin pienin kortti
			if (sijainti == -1) {
				sijainti = k.pienin();
			} else {
				if (k.getKortit().get(sijainti).getArvo() < arvo) {
					sijainti = k.pieninSamaaMaata(aloitus.getMaa());
				}
			}
			if (i == pelaaja) { // Pelaaja
				print("Kätesi: ");
				for (int l = 0; l < k.getKortit().size(); l++) {
					if (k.getKortit().get(l).getMaa().equals(aloitus.getMaa())) 
						print(l + ": " + k.getKortit().get(l) + (l == sijainti ? (vinkit?"	<<<":"	<") : "	<"));
					else
						print(l + ": " + k.getKortit().get(l) + (l == sijainti ? (vinkit?"	<<<":"") : ""));
				}
				print("Minkä kortin lyöt?");
				sijainti = Integer.parseInt(lue());
			}
			Kortti kortti = k.jaaKortti(sijainti);
			pöytä.otaKortti(kortti);
			if (arvo < kortti.getArvo() && kortti.getMaa().equals(aloitus.getMaa())) {
				arvo = kortti.getArvo(); // Jos arvo on suurempi, se pannaan muistiin
				voittaja = i;
			}
			print("Pelaaja " + i + ": " + kortti);
		}
		return voittaja;
	}
	
	public static void print(String c) {
		System.out.println(c);
	}
	
	public String lue() {
		
		String s = sc.next();
		return s;
	}
	
	public static void odota() {
		try {
			System.in.read();
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Tikkipeli peli = new Tikkipeli(6);
		print("Olet pelaaja " + peli.pelaaja);
		print("Paina enteriä aloittaaksesi...");
		odota();
		int v = peli.kierros(0);
		print("Voittaja on: " + v);
		print("Paina enteriä...");
		odota();
		v = peli.kierros(v);
		print("Voittaja on: " + v);
		print("Paina enteriä...");
		odota();
		v = peli.kierros(v);
		print("Voittaja on: " + v);
		print("Paina enteriä...");
		odota();
		v = peli.kierros(v);
		print("Voittaja on: " + v);
		print("Paina enteriä...");
		odota();
		v = peli.kierros(v);
		print("Voittaja on " + v + ", joka voitti koko pelin.");
	}
}
