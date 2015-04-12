
public class Solu {
	SoluTyypit tyyppi = null;
	Vaihe vaihe;
	int x = 0;
	int y = 0;
	
	public Solu(SoluTyypit tyyppi, int x, int y) {
		super();
		this.tyyppi = tyyppi;
		if (tyyppi == SoluTyypit.pelto){
			vaihe = Vaihe.Hoito1;
		}
		else vaihe = Vaihe.Alku;
		this.x = x;
		this.y = y;
	}
	public Solu(SoluTyypit tyyppi, Vaihe Vaihe, int x, int y) {
		super();
		this.tyyppi = tyyppi;
		this.vaihe = Vaihe;
		this.x = x;
		this.y = y;
	}
	
	public int korjaa(Työkalu väline){
		int saalis = 0;
		switch (tyyppi){
		case pelto:
			switch (väline){
			case sirppi:
				if (vaihe == Vaihe.Hoito1){vaihe = Vaihe.Hoito2; break;}
				if (vaihe == Vaihe.Hoito2){vaihe = Vaihe.Hoito3; break;}
				if (vaihe == Vaihe.Hoito3){vaihe = Vaihe.Korjuu; break;}
				vaihe = Vaihe.Alku;
				tyyppi = SoluTyypit.aro;
				return 100;
			case viikate:
				if (vaihe == Vaihe.Hoito1){vaihe = Vaihe.Hoito2; break;}
				if (vaihe == Vaihe.Hoito2){vaihe = Vaihe.Hoito3; break;}
				if (vaihe == Vaihe.Hoito3){vaihe = Vaihe.Korjuu; break;}
				vaihe = Vaihe.Alku;
				tyyppi = SoluTyypit.aro;
				return 300;
			case miekka:
				if (vaihe == Vaihe.Hoito1){vaihe = Vaihe.Hoito2; break;}
				if (vaihe == Vaihe.Hoito2){vaihe = Vaihe.Hoito3; break;}
				if (vaihe == Vaihe.Hoito3) break;
				vaihe = Vaihe.Alku;
				tyyppi = SoluTyypit.aro;
				return 50;
			case kirves:
				return 0;
			case saha:
				return 0;
			}
		case metsä:
			switch (väline){
			case sirppi:
				return 0;
			case viikate:
				return 0;
			case miekka:
				switch (vaihe){
				case Alku:
					vaihe = Vaihe.Avohakkuu;
					break;
				case Avohakkuu:
					vaihe = Vaihe.MaaperänMuokkaus;
					break;
				case TaimikonPerkaus:
					vaihe = Vaihe.TaimikonHoito;
					break;
				case TaimikonHoito:
					vaihe = Vaihe.EnsimmäinenHarvennus;
				case EnsimmäinenHarvennus:
					vaihe = Vaihe.ToinenHarvennus;
					return 5;
				case ToinenHarvennus:
					vaihe = Vaihe.Päätehakkuu;
					return 10;
				case Päätehakkuu:
					vaihe = Vaihe.Alku;
					tyyppi = SoluTyypit.aro;
					return 50;
				}
			case kirves:
				switch (vaihe){
				case Alku:
					vaihe = Vaihe.Avohakkuu;
					break;
				case Avohakkuu:
					vaihe = Vaihe.MaaperänMuokkaus;
					break;
				case TaimikonPerkaus:
					vaihe = Vaihe.TaimikonHoito;
					break;
				case TaimikonHoito:
					vaihe = Vaihe.EnsimmäinenHarvennus;
				case EnsimmäinenHarvennus:
					vaihe = Vaihe.ToinenHarvennus;
					return 50;
				case ToinenHarvennus:
					vaihe = Vaihe.Päätehakkuu;
					return 100;
				case Päätehakkuu:
					vaihe = Vaihe.Alku;
					tyyppi = SoluTyypit.aro;
					return 400;
				}
			case saha:
				switch (vaihe){
				case Alku:
					vaihe = Vaihe.Avohakkuu;
					break;
				case Avohakkuu:
					vaihe = Vaihe.MaaperänMuokkaus;
					break;
				case TaimikonPerkaus:
					vaihe = Vaihe.TaimikonHoito;
					break;
				case TaimikonHoito:
					vaihe = Vaihe.EnsimmäinenHarvennus;
				case EnsimmäinenHarvennus:
					vaihe = Vaihe.ToinenHarvennus;
					return 70;
				case ToinenHarvennus:
					vaihe = Vaihe.Päätehakkuu;
					return 150;
				case Päätehakkuu:
					vaihe = Vaihe.Alku;
					tyyppi = SoluTyypit.aro;
					return 500;
				}
			case kuokka:
				switch (vaihe){
				case MaaperänMuokkaus:
					vaihe = Vaihe.TaimikonPerkaus;
					break;
				}
			}
		case aro:
			switch (väline){
			case sirppi:
				return 0;
			case viikate:
				return 0;
			case miekka:
				return 0;
			case kirves:
				return 0;
			case saha:
				return 0;
			case kuokka:
				switch (vaihe){
				case Alku:
					vaihe = Vaihe.Kyntö;
					return -5;
				case Kyntö:
					vaihe = Vaihe.Kylvö;
					break;
				case Kylvö:
					vaihe = Vaihe.Hoito1;
					tyyppi = SoluTyypit.pelto;
					break;
				}
			}
		case tuli:
			switch (väline){
			case sirppi:
				switch (vaihe){
				case PalavaPelto:
					return 10;
				case PalavaMetsä:
					return 15;
				}
				break;
			case viikate:
				switch (vaihe){
				case PalavaPelto:
					return 20;
				case PalavaMetsä:
					return 25;
				}
				break;
			case miekka:
				return 0;
			case kirves:
				return 0;
			case saha:
				return 0;
			}
		}
		
		
		return saalis;
	}
	
	public static boolean voikoHarventaa(Vaihe vaihe, int vuosi) {
		switch (vaihe) {
		case Alku: // -> Avohakkuu
			return true;
		case Avohakkuu: // -> Maaperän muokkaus
			return true;
		case MaaperänMuokkaus: // -> istutus
			return true;
		case TaimikonPerkaus: // -> taimikon hoito
			return true;
		case TaimikonHoito: // -> taimikon harvennus
			if (1 <= vuosi && vuosi <= 5) return true;
			break;
			// taimikon harvennus -> 1. harvennus
		case EnsimmäinenHarvennus: // -> 2. harvennus
			if (14 <= vuosi && vuosi <= 16) return true;
			break;
		case ToinenHarvennus: // -> Päätehakkuu
			if (29 <= vuosi && vuosi <= 31) return true;
			break;
		case Päätehakkuu: // -> Maaperän muokkaus
			if (59 <= vuosi && vuosi <= 61) return true;
			break;
		default:
			break;
		}
		return true;
	}
	
	public boolean polta(){
		if (tyyppi == SoluTyypit.aro || tyyppi == SoluTyypit.tuli){
			return false;
		}
		else {tyyppi = SoluTyypit.tuli; return true;}
	}
}
