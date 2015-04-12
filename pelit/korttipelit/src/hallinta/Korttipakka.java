package hallinta;

import java.util.ArrayList;

/**
 * Korttipakka, jossa on 52-korttia. Jokerit valinnanvaraiset. Käyttää hyödykseen Kortti-luokkaa.
 * 
 * @author iikka
 *
 */
public class Korttipakka {
	ArrayList<Kortti> pakka = new ArrayList<Kortti>();
	
	/**
	 * Tavallinen korttipakka, jossa ässä on arvoltaan 14
	 */
	public Korttipakka (){
		for (int i = 1; i < 2; i++){
			for (int t = 2; t < 15; t++) {
				pakka.add(new Kortti(t, "pata"));
			}
			for (int t = 2; t < 15; t++) {
				pakka.add(new Kortti(t, "risti"));
			}
			for (int t = 2; t < 15; t++) {
				pakka.add(new Kortti(t, "ruutu"));
			}
			for (int t = 2; t < 15; t++) {
				pakka.add(new Kortti(t, "hertta"));
			}
			
		}
	}
	
	/**
	 * Tavallinen korttipakka, josta on poistettu kortit 2-5. Sopii mm. marjapussiin
	 * Ässä on 14
	 * 
	 * @param montako Jos kortit poistetaan
	 */
	public Korttipakka (boolean poistaKortit) {
		for (int i = 1; i < 2; i++){
			for (int t = 6; t < 15; t++) {
				pakka.add(new Kortti(t, "pata"));
			}
			for (int t = 6; t < 15; t++) {
				pakka.add(new Kortti(t, "risti"));
			}
			for (int t = 6; t < 15; t++) {
				pakka.add(new Kortti(t, "ruutu"));
			}
			for (int t = 6; t < 15; t++) {
				pakka.add(new Kortti(t, "hertta"));
			}
			
		}
	}
	
	/**
	 * Tavallinen korttipakka, jossa ässä on arvoltaan 1
	 * 
	 * @param pakkojenMäärä Pakkojen lukumäärä + 1.
	 */
	public Korttipakka (int pakkojenMäärä){
		for (int i = 1; i < pakkojenMäärä; i++){
			for (int t = 1; t < 14; t++) {
				pakka.add(new Kortti(t, "pata"));
			}
			for (int t = 1; t < 14; t++) {
				pakka.add(new Kortti(t, "risti"));
			}
			for (int t = 1; t < 14; t++) {
				pakka.add(new Kortti(t, "ruutu"));
			}
			for (int t = 1; t < 14; t++) {
				pakka.add(new Kortti(t, "hertta"));
			}
			
		}
	}
	
	/**
	 * @param pakkojenMäärä Pakkojen lukumäärä + 1.
	 * @param jokerit Jokerien lukumäärä.
	 */
	public Korttipakka (int pakkojenMäärä, int jokerit){
		for (int i = 1; i < pakkojenMäärä; i++){
			for (int t = 1; t < 14; t++) {
				pakka.add(new Kortti(t, "pata"));
			}
			for (int t = 1; t < 14; t++) {
				pakka.add(new Kortti(t, "risti"));
			}
			for (int t = 1; t < 14; t++) {
				pakka.add(new Kortti(t, "ruutu"));
			}
			for (int t = 1; t < 14; t++) {
				pakka.add(new Kortti(t, "hertta"));
			}
			for (int t = 1; t < jokerit + 1; t++) {
				pakka.add(new Kortti(0, "jokeri"));
			}
		}
	}
	
	/**
	 * Sekoittaa pakan, vaihtamalla kahden satunnaisen kortin paikkaa 52 kertaa.
	 */
	public void sekoita() {
		for(int i = 0; i < 52; i++) {
			double aInt = Math.random() * 100; //Arpoo kortin a paikan.
			double bInt = Math.random() * 100; //Arpoo kortin b paikan.
			if (aInt > 52) aInt = aInt - 48; //Tarkistaa, löytyykö kortti 52-kortin pakasta. Panee sen löytymään. 
			if (bInt > 52) bInt = bInt - 48;
			Kortti a = pakka.get((int)aInt); //Kirjoittaa korttiin a arvotun kortin tiedot.
			while(a.equals(null)){
				aInt = Math.random() * 100;
				a = pakka.get((int)aInt);
			}
			Kortti b = pakka.get((int)bInt); //Kirjoittaa korttiin b arvotun kortin tiedot.
			while(b.equals(null)){
				bInt = Math.random() * 100;
				b = pakka.get((int)bInt);
			}
			if(aInt != bInt){ //Tarkistaa, että a ei ole b.
				pakka.set((int)aInt, b); //Vaihtaa korttien paikat pakassa.
				pakka.set((int)bInt, a);
			}
		}
		
	}
	
	/**
	 * Palauttaa ensimmäisen kortin ja poistaa sen pakasta.
	 * 
	 * @return Pakan ensimmäinen kortti.
	 */
	public Kortti jaaKortti(){
		Kortti a = pakka.get(0); //Ottaa kortin...
		pakka.remove(0); //...ja poistaa sen pakasta.
		return a;
	}
}