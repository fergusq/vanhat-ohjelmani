package hallinta;

/**
 * Pelikortti.
 * 
 * @author iikka
 *
 */
public class Kortti {
	int arvo;
	String maa;
	
	
	
	public Kortti(int arvo, String maa){
		setArvo(arvo);
		setMaa(maa);
	}
	
	public void setArvo(int arvo){
		this.arvo = arvo;
	}
	
	public int getArvo() {
		return arvo;
	}
	
	public void setMaa(String maa){
		this.maa = maa;
	}
	
	public String getMaa() {
		return maa;
	}
	
	public String toString(){
		String arvo = this.arvo + "";
		if (arvo.equals("11")) arvo = "J (11)";
		if (arvo.equals("12")) arvo = "Q (12)";
		if (arvo.equals("13")) arvo = "K (13)";
		if (arvo.equals("14")) arvo = "A (14)";
		String maa = this.maa.substring(1);
		maa = this.maa.substring(0, 1).toUpperCase() + maa;
		return maa + " " + arvo;
	}
}