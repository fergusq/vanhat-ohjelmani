import java.util.ArrayList;
import java.util.Random;
public class Kartta {
	//ArrayList<solu> solut = new ArrayList<solu>();
	Solu[][] soluKartta = new Solu[20][14];
	
	public Kartta(){
		lisääSolu(new Solu(SoluTyypit.metsä, 0, 0));
		/*lisääSolu(new solu(soluT.aro, 0, 1));
		lisääSolu(new solu(soluT.pelto, 0, 2));
		lisääSolu(new solu(soluT.pelto, 0, 3));
		lisääSolu(new solu(soluT.metsä, 0, 4));
		lisääSolu(new solu(soluT.pelto, 0, 5));
		lisääSolu(new solu(soluT.aro, 0, 6));
		lisääSolu(new solu(soluT.aro, 0, 7));
		lisääSolu(new solu(soluT.aro, 0, 8));
		lisääSolu(new solu(soluT.metsä, 0, 9));
		lisääSolu(new solu(soluT.metsä, 0, 10));
		lisääSolu(new solu(soluT.metsä, 0, 11));
		lisääSolu(new solu(soluT.aro, 0, 12));
		lisääSolu(new solu(soluT.aro, 0, 13));
		lisääSolu(new solu(soluT.aro, 0, 14));
		lisääSolu(new solu(soluT.aro, 0, 15));
		lisääSolu(new solu(soluT.pelto, 0, 16));
		lisääSolu(new solu(soluT.pelto, 0, 17));
		lisääSolu(new solu(soluT.aro, 0, 18));
		lisääSolu(new solu(soluT.pelto, 0, 19));
		lisääSolu(new solu(soluT.aro, 0, 20));
		lisääSolu(new solu(soluT.aro, 0, 21));
		lisääSolu(new solu(soluT.pelto, 0, 22));
		lisääSolu(new solu(soluT.aro, 0, 23));
		lisääSolu(new solu(soluT.aro, 0, 24));
		lisääSolu(new solu(soluT.metsä, 0, 25));
		lisääSolu(new solu(soluT.metsä, 0, 26));
		lisääSolu(new solu(soluT.metsä, 0, 27));
		lisääSolu(new solu(soluT.aro, 0, 28));
		lisääSolu(new solu(soluT.aro, 0, 29));*/
		
		for (int i = 0; i < 20; i++){
			for (int t = 0; t < 14; t++){
				Random noppa = new Random();
				int sama = noppa.nextInt(11);
				int toinen = noppa.nextInt(11);
				int arpa = noppa.nextInt(11);
				if (i == 0 && t == 0){
					
				}
				else if (i == 0){
					switch(arpa){
					case 0:
						lisääSolu(new Solu(SoluTyypit.aro, i, t));
						break;
					case 1:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 2:
						lisääSolu(new Solu(SoluTyypit.pelto, Vaihe.Hoito1, i, t));
						break;
					case 3:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 4:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 5:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 6:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 7:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 8:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 9:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 10:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					}
				}
				else if (sama < 5)
					lisääSolu(new Solu(haeSolu(i-1, t).tyyppi, i, t));
				else if (toinen < 5 && t != 0)
					lisääSolu(new Solu(haeSolu(i, t-1).tyyppi, i, t));
				else
					switch(arpa){
					case 0:
						lisääSolu(new Solu(SoluTyypit.aro, i, t));
						break;
					case 1:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 2:
						lisääSolu(new Solu(SoluTyypit.pelto, Vaihe.Hoito1, i, t));
						break;
					case 3:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 4:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 5:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 6:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 7:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 8:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 9:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					case 10:
						lisääSolu(new Solu(SoluTyypit.metsä, i, t));
						break;
					}
			}
		}
	}
	
	public void lisääSolu(Solu s) {
		soluKartta[s.x][s.y] = s;
	}
	
	public Solu haeSolu(int x, int y){
		Solu s = soluKartta[x][y];
		/*for (int i = 0; i < solut.size(); i++){
			if (solut.get(i).x == x && solut.get(i).y == y){
				Solu = solut.get(i);
				break;
			}
		}*/
		return s;
	}
	
	public int tee(Teko teko){
		int saalis = 0;
		
		if (teko.getVäline() == Työkalu.tulukset){
			haeSolu(teko.getX(), teko.getY()).polta();
		}
		else{ 
			if (haeSolu(teko.getX(), teko.getY()).tyyppi != SoluTyypit.metsä)
				saalis = haeSolu(teko.getX(), teko.getY()).korjaa(teko.getVäline());
			else {
				if (Solu.voikoHarventaa(haeSolu(teko.getX(), teko.getY()).vaihe, teko.vuosi)){
					saalis = haeSolu(teko.getX(), teko.getY()).korjaa(teko.getVäline());
				}
			}
		}
		
		return saalis;
	}
}
