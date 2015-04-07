import java.awt.BorderLayout;
import java.awt.TextArea;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Toolkit;

public class HLSVEKP extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private TextArea JLLauta = null;
	private int suunta;
	private Mato pelaaja1 = null;
	private Matotaulu taulu1 = null;
	private static int ALOITUSPITUUS = 4;
	private boolean intro = false;
	private int pituus = 0;
	private int nälkä = 0;
	private int taulunKoko = 10;
	private int siirrot = 0;
	private int lisapisteet;
	private Timer timer1 = null;
	private Ruutu ruutu = Ruutu.intro;
	
	private int[] rY = {5, 3, 7, 3, 8};
	private int[] rX = {2, 7, 5, 4, 6};
	
	private SijainninLuokka[] rL = {SijainninLuokka.o, SijainninLuokka.o,
			SijainninLuokka.o, SijainninLuokka.c, SijainninLuokka.c};
	
	
	private static String hlsv_intro = "                         \n"+
	"                        \n"+
	"     Hän lähti sinne            Versio 2.0\n"+
	"         varkain,             Ohjelmoinut IH\n"+
	"  EIKÄ KOSKAAN PALANNUT \n"+
	"  ____  _____ _     ___ \n"+
	" |  _ \\| ____| |   |_ _|      ..###.o........\n"+
	" | |_) |  _| | |    | |       ....+..ö..&....\n"+
	" |  __/| |___| |___ | |       ..c............\n"+
	" |_|   |_____|_____|___|    Paina 5 jatkaaksesi.";
	
	private static String han_intro = " _   _  _  _\n" +
"| | | |(_)(_)_ __    \n" +
"| |_| |/ _` | '_ \\  \n" +
"|  _  | (_| | | | |  \n" +
"|_| |_|\\__,_|_| |_| \n";
	
	private static String lahti_intro = " _  _  _ _     _   _ \n" +
			"| |(_)(_| |__ | |_(_)\n" +
			"| |/ _` | '_ \\| __| |\n" +
			"| | (_| | | | | |_| |\n" +
			"|_|\\__,_|_| |_|\\__|_|\n";
	private static String sinne_intro = 
			"     _\n" +
			" ___(_)_ __  _ __   ___ \n" +
			"/ __| | '_ \\| '_ \\ / _ \\\n" +
			"\\__ \\ | | | | | | |  __/\n" +
			"|___/_|_| |_|_| |_|\\___|\n";
	
	private static String varkain_intro = "                 _         _      \n" +
"__   ____ _ _ __| | ____ _(_)_ __  \n" +
"\\ \\ / / _` | '__| |/ / _` | | '_ \\ \n" +
" \\ V / (_| | |  |   < (_| | | | | |\n" +
"  \\_/ \\__,_|_|  |_|\\_\\__,_|_|_| |_|\n";
	
	private void viesti(String v) {
		JLLauta.setText(taulu1.toString()+
				"Aika: " + siirrot + ". Nälkä: " + nälkä +
				". XP: " + (pituus + lisapisteet)/ALOITUSPITUUS + ".\n" + v);
	}
	
	public class liikutaMatoa extends TimerTask{

		@Override
		public void run() {
			if(!pelaaja1.onkoSolmussa()){
				pelaaja1.liikuSuuntaan(suunta);
				taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix(), rY, rX, rL);
				viesti("");
				siirrot += 1;
				Sijainti osuma = pelaaja1.onkoSolmussa(rY, rX, rL);
				if (osuma.solmussa()){
					switch (osuma.getLuokka()) {
					case o:
						pelaaja1.korotaPituus();
						pituus += 1;
						break;
					case c:
						pelaaja1.korotaPituus();
						pelaaja1.korotaPituus();
						pituus += 2;
						break;
					case ö:
						lisapisteet += 1;
						viesti("Sait kokemusta!");
						break;
					case ja:
						viesti("Ugh. Et halua törmätä tuhon uudestaan.");
						lisapisteet -= 1;
						break;
					default:
						break;
					}
					
					Random noppa = new Random();
					rY[osuma.getMones()] = noppa.nextInt(10);
					rX[osuma.getMones()] = noppa.nextInt(10);
					switch(noppa.nextInt(4)){
					case 0:
						rL[osuma.getMones()] = SijainninLuokka.o;
						break;
					case 1:
						rL[osuma.getMones()] = SijainninLuokka.c;
						break;
					case 2:
						rL[osuma.getMones()] = SijainninLuokka.ö;
						break;
					case 3:
						rL[osuma.getMones()] = SijainninLuokka.ja;
						break;
					}
				}
			}
			else{
				JLLauta.setText("Törmäsit! Tuloksesi: " + (pituus + lisapisteet)/ALOITUSPITUUS + "\n\nPaina välilyöntiä jatkaaksesi.");
				timer1.cancel();
				intro = false;
			}
		}

	}

	
	/**
	 * This is the default constructor
	 */
	public HLSVEKP(String[] args) {
		super();
		initialize();
		setVisible(true);
		System.out.println("Kiittäen,");
		for (int i = 0; i < 5; i++) {
			switch (i) {
			case 0:
				JLLauta.setText("\n\n\n" + han_intro);
				break;
			case 1:
				JLLauta.setText("\n\n\n" + lahti_intro);
				break;
			case 2:
				JLLauta.setText("\n\n\n" + sinne_intro);
				break;
			case 3:
				JLLauta.setText("\n\n\n" + varkain_intro);
				break;
			case 4:
				JLLauta.setText(hlsv_intro);
				break;
			default:
				break;
			}
			try {
				Thread.sleep(1000);
				System.out.println(satunnainenHenkilönNimi());
			} catch (InterruptedException e1) {e1.printStackTrace();
			}
		}
		System.out.println("Hän lähti sinne varkain,");
		System.out.println(" EIKÄ KOSKAAN PALANNUT");
		JLLauta.setText(hlsv_intro);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		/*try{
			if (argumentit[0].equals("--TaulunKoko")){
				if (argumentit.length < 1){
					taulunKoko = Integer.parseInt(argumentit[1]);
				}
				else {
					System.err.println("SYÖTTÖVIRHE: Valitsin --TaulunKoko vaatii argumentin!");
					System.exit(ABORT);
				}
			}
			else{
				taulunKoko = 10;
			}
		}
		catch(ArrayIndexOutOfBoundsException ex){
			
		}
		catch(NumberFormatException ex){
			System.err.println("SYÖTTÖVIRHE: Valitsin --TaulunKoko ei hyväksy argumenttia! Tarkista, että kirjoitit kokonaisluvun.");
		}*/
		taulunKoko = 10;
		pituus = 4;
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
		pelaaja1 = new Mato(y, x, taulunKoko, pituus);
		taulu1 = new Matotaulu(pituus, taulunKoko, y, x);
		this.setSize(419, 225);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("kuvat/mato.png"));
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle("Hän lähti sinne varkain");
		this.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				if (e.getKeyChar() == '5' && intro == false){
					suunta = 2;
					ruutu = Ruutu.kartta;
					viesti("");
				}
			}
		});
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.out.println("windowClosed()");
				System.out.println("Ikkuna suljettu. Aika (siirtoina): " + siirrot + ". Matosi pituus: " + pituus + ". Pisteet: " + (pituus + lisapisteet)/ALOITUSPITUUS);
				System.exit(EXIT_ON_CLOSE);
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			JLLauta = new TextArea();
			JLLauta.setText(hlsv_intro);
			JLLauta.setMinimumSize(new Dimension(437, 157));
			JLLauta.setEditable(false);
			JLLauta.setBackground(Color.white);
			JLLauta.setFont(new Font("Courier New", Font.PLAIN, 12));
			JLLauta.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					if (e.getKeyChar() == '5' && ruutu == Ruutu.intro){
						suunta = 2;
						timer1 = new Timer(true);
						//timer1.scheduleAtFixedRate(new liikutaMatoa(), 1000, 1000);
						ruutu = Ruutu.kartta;
					}
					else if(ruutu == Ruutu.kartta){
						//System.out.println("keyReleased()\ne.toString() = " + e.toString() + "\ne.getKeyChar() = " + e.getKeyChar() + "\ne.getKeyCode() = " + e.getKeyCode());
						if (!pelaaja1.onkoSolmussa()){
							suunta = e.getKeyCode();
							siirrot += 1;
							nälkä++;
							pelaaja1.liikuSuuntaan(suunta);
							if (nälkä > 50) lisapisteet--;
							//taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix());
							//viesti("");
							{
								taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix(), rY, rX, rL);
								viesti("");
								
								Sijainti osuma = pelaaja1.onkoSolmussa(rY, rX, rL);
								if (osuma.solmussa()){
									switch (osuma.getLuokka()) {
									case o:
										nälkä-=50;
										if (nälkä < 0) nälkä = 0;
										viesti("Söit hyvää " + partitiiviin(satunnainen(pikku_ruuat)) + "!");
										break;
									case c:
										nälkä-=100;
										if (nälkä < 0) nälkä = 0;
										viesti("Söit hyvää " + partitiiviin(satunnainen(isot_ruuat)) + "!");
										break;
									case ö:
										lisapisteet += 1;
										viesti("Löysit kirjan (" + satunnainenHenkilönNimi() + ", " + isolla(satunnainenNimi()) + ").");
										break;
									case ja:
										lisapisteet -= 1;
										if (lisapisteet < 0) lisapisteet = 0;
										viesti("Törmäsit... johonkin.");
										break;
									default:
										JLLauta.setText("Törmäsit! Tuloksesi: " + (pituus + lisapisteet)/ALOITUSPITUUS + "\n\nPaina välilyöntiä jatkaaksesi.");
										ruutu = Ruutu.intro;
										break;
									}
									Random noppa = new Random();
									rY[osuma.getMones()] = noppa.nextInt(10);
									rX[osuma.getMones()] = noppa.nextInt(10);
									switch(noppa.nextInt(5)){
									case 0:
										rL[osuma.getMones()] = SijainninLuokka.o;
										break;
									case 1:
										rL[osuma.getMones()] = SijainninLuokka.c;
										break;
									case 2:
										rL[osuma.getMones()] = SijainninLuokka.ö;
										break;
									case 3:
										rL[osuma.getMones()] = SijainninLuokka.ja;
										break;
									case 4:
										rL[osuma.getMones()] = SijainninLuokka.ö;
										break;
									}
									
								}
								if (pelaaja1.onkoSolmussa()) {
									JLLauta.setText("Törmäsit! Tuloksesi: " + (pituus + lisapisteet)/ALOITUSPITUUS + "\n\nPaina välilyöntiä jatkaaksesi.");
									ruutu = Ruutu.intro;
								}
							}
							
						}
						else {
							JLLauta.setText("Törmäsit! Tuloksesi: " + (pituus + lisapisteet)/ALOITUSPITUUS + "\n\nPaina välilyöntiä jatkaaksesi.");
							ruutu = Ruutu.intro;
						}
					}
					else if(e.getKeyChar() == ' '){
						ruutu = Ruutu.intro;
						//timer1.cancel();
						JLLauta.setText(hlsv_intro);
						
						taulunKoko = 10;
						pituus = 4;
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
						pelaaja1 = new Mato(y, x, taulunKoko, pituus);
						taulu1 = new Matotaulu(pituus, taulunKoko, y, x, rY, rX, rL);
						siirrot = 0;
						lisapisteet = 0;
						
						for(int i = 0; i < rY.length; i++){
							Random noppa = new Random();
							rY[i] = noppa.nextInt(10);
							rX[i] = noppa.nextInt(10);
						}
					}
					else if(e.getKeyChar() == 'h' || ruutu == Ruutu.ohjeet){
						ruutu = Ruutu.ohjeet;
						if (timer1 != null) timer1.cancel();
						JLLauta.setText("Hän lähti sinne varkain eikä koskaan palannut\n" +
										"---------------------------------------------\n" +
										"Tarkoituksena on kerätä mahdollisimman paljon\n" +
										"XP-pisteitä. Saat niitä kirjoista (ö).\n" +
										"Jos törmäät johonkin epämiellyttävään (&),\n" +
										"menetät XP-pisteitä. Jos sinulle tulee nälkä,\n" +
										"sinun tulee syödä. Ruokaa on kahdessa koossa:\n" +
										"Pieni ruoka (o) ottaa 50 nälkäpistettä.\n" +
										"Iso ruoka (c) ottaa 100 nälkäpistettä.\n" +
										"\n" +
										"Paina välilyöntiä jatkaaksesi.");
					}
				}
			});
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(JLLauta, BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	  public static void main(String[] args) {
		    HLSVEKP ikkuna;
			
		    ikkuna = new HLSVEKP(args);
		    ikkuna.setVisible(true);
		  }

	  private static String satunnainenNimi() {
		  String alku = satunnainen(nimet_tavut_alku);
		  String keski = satunnainen(nimet_tavut_keski);
		  String loppu = satunnainen(nimet_tavut_loppu);
		  if (alku.endsWith(keski.substring(0, 1))) {
			  keski = keski.substring(1);
		  }
		  if (keski.endsWith(loppu.substring(0, 1))) {
			  loppu = loppu.substring(1);
		  }
		  return alku + keski + loppu;
	  }
	  
	  private static String satunnainenHenkilönNimi() {
		  return satunnainenEtunimi() + " " + satunnainenSukunimi();
	  }
	  
	  private static String satunnainenEtunimi() {
		  String alku = satunnainen(etunimet_tavut_alku);
		  String keski = satunnainen(etunimet_tavut_keski);
		  String loppu = satunnainen(etunimet_tavut_loppu);
		  if (alku.endsWith(keski.substring(0, 1))) {
			  keski = keski.substring(1);
		  }
		  if (keski.endsWith(loppu.substring(0, 1))) {
			  loppu = loppu.substring(1);
		  }
		  return alku + keski + loppu;
	  }
	  
	  private static String satunnainenSukunimi() {
		  String alku = satunnainen(sukunimet_tavut_etuliite);
		  String keski = satunnainen(sukunimet_tavut_alku);
		  String loppu = satunnainen(sukunimet_tavut_loppu);
		  Random r = new Random();
		  if (r.nextInt(3) == 0) {
			  alku = satunnainen(sukunimet2_tavut_etuliite);
			  keski = satunnainen(sukunimet2_tavut_alku);
			  loppu = satunnainen(sukunimet2_tavut_loppu);
		  }
		  if (keski.endsWith(loppu.substring(0, 1))) {
			  loppu = loppu.substring(1);
		  }
		  return alku + keski + loppu;
	  }
	  
	  private static String satunnainen(String[] arr) {
		  Random noppa = new Random();
		  return arr[noppa.nextInt(arr.length)];
	  }
	  
	  public static String isolla(String sana) {
		  String etukirjain = sana.substring(0, 1);
		  sana = sana.substring(1);
		  
		  return etukirjain.toUpperCase() + sana;
	  }
	  
	  public static String genetiiviin(String sana) {
		  return sana + "n";
	  }
	  
	  public static String partitiiviin(String sana) {
		  if (sana.endsWith("ä") || sana.endsWith("ö")) return sana + "ä";
		  if (sana.endsWith("ni") && sana.substring(1, 2).equalsIgnoreCase("i")) return sana.substring(0, sana.length()-1) + "tä";
		  if (sana.endsWith("ni")) return sana + "ta";
		  return sana + "a";
	  }
	  
	  private static String[] ruuat = {"leipä", "keitto", "puuro", "sieni"};
	  private static String[] pikku_ruuat = {"leipä", "sieni"};
	  private static String[] isot_ruuat = {"keitto", "puuro"};
	  
	  private static String[] nimet_tavut_alku = {"si", "ma", "koi", "pu", "paa", "sa", "de"};
	  private static String[] nimet_tavut_keski = {"kma", "nki", "de", "puna", "pii", "sio", "mao"};
	  private static String[] nimet_tavut_loppu = {"ngo", "ngi", "nki", "nko", "um", "ix", "dor"};
	  
	  private static String[] sukunimet_tavut_etuliite = {"Mc", "O'", "ben ", "", ""};
	  private static String[] sukunimet_tavut_alku = {"Bran", "Folk", "Garri", "Good", "Jon", "Kir", "Neil", "Peter", "Roger", "Sam", 
		  "Star", "Wilkin", "William", "Long"};
	  private static String[] sukunimet_tavut_loppu = {"bern", "burn", "born", "fer", "fur", "sten", "stun", "ston", "for",
		  "sen", "son", "s", "sby", "er", "fire", "fyre", "ling", "lyng", "bottom"};
	  
	  private static String[] sukunimet2_tavut_etuliite = {"Mc", "O'", "ben ", "", ""};
	  private static String[] sukunimet2_tavut_alku = {"Ath", "Eccl", "Garr", "Inn", "Jam", };
	  private static String[] sukunimet2_tavut_loppu = {"e", "iburn", "eburn", "ibern", "ebern", "iborn", "eborn", "ibourn", "ebourn", "ibairn", "ebairn",
		  "ifur", "efur", "ifer", "efer", "ifor", "efor", "ifour", "efour", "ifair", "efairn",
		  "istun", "estun", "isten", "esten", "iston", "eston", "istoun", "estoun", "istain", "estain",
		  "isen", "esen", "iesen", "ison", "eson", "ieson", "eson", "isdottir", "esdottir", "isdottyr", "esdottyr", 
		  "is", "es", "es", "esby", "ifire", "efire", "ifyre", "efyre", "ling", "lyng"};
	  
	  private static String[] etunimet_tavut_alku = {"Pal", "Pin", "Kol", "San", "Sol", "Mar", "Es"};
	  private static String[] etunimet_tavut_keski = {"pin", "pil", "sol", "pat", "pit", "sil", "man"};
	  private static String[] etunimet_tavut_loppu = {"ine", "yne", "an", "un", "um", "ix", "dor"};

}
