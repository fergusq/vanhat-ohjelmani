package hallinta;

import java.util.ArrayList;

/**
 * Pelaajan käsi. Sisältää metodit pelaajan käden korttien säilömiseen.
 * 
 * @author iikka
 *
 */
public class Käsi {
	private ArrayList<Kortti> kortit = new ArrayList<Kortti>();
	
	public ArrayList<Kortti> getKortit(){
		return kortit;
	}
	
	/**
	 * Ottaa kortin ja lisää sen kortit-muuttujaan.
	 * 
	 * @param a Lisättävä kortti.
	 */
	public void otaKortti(Kortti a){
		kortit.add(a);
	}
	
	/**
	 * Ottaa kortin kortit-muuttujasta.
	 * 
	 * @param a Lisättävä kortti.
	 */
	public Kortti jaaKortti(int mones){
		return kortit.remove(mones);
	}
	
	public int pieninSamaaMaata(String maa) {
		int vastaus = 0;
		int arvo = 14;
		boolean onkoMaata = false;
		for (int i = 0; i < kortit.size(); i++) {
			Kortti k = kortit.get(i);
			if (k.maa.equals(maa)) {
				if (k.arvo < arvo) {
					arvo = k.arvo;
					vastaus = i;
					onkoMaata = true;
				}
			}
		}
		return onkoMaata ? vastaus : -1;
	}
	
	public int suurinSamaaMaata(String maa) {
		int vastaus = 0;
		int arvo = 0;
		boolean onkoMaata = false;
		for (int i = 0; i < kortit.size(); i++) {
			Kortti k = kortit.get(i);
			if (k.maa.equals(maa)) {
				if (k.arvo > arvo) {
					arvo = k.arvo;
					vastaus = i;
					onkoMaata = true;
				}
			}
		}
		return onkoMaata ? vastaus : -1;
	}
	
	public int pienin() {
		int vastaus = 0;
		int arvo = 14;
		for (int i = 0; i < kortit.size(); i++) {
			Kortti k = kortit.get(i);
			if (k.arvo < arvo) {
				arvo = k.arvo;
				vastaus = i;
			}
		}
		return vastaus;
	}
	
	public int suurin() {
		int vastaus = 0;
		int arvo = 0;
		for (int i = 0; i < kortit.size(); i++) {
			Kortti k = kortit.get(i);
			if (k.arvo > arvo) {
				arvo = k.arvo;
				vastaus = i;
			}
		}
		return vastaus;
	}
}
