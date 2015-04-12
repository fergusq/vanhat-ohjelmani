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

public class matopeliV extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private TextArea JLLauta = null;
	private int suunta;
	private Mato pelaaja1 = null;  //  @jve:decl-index=0:
	private Matotaulu taulu1 = null;  //  @jve:decl-index=0:
	private static int ALOITUSPITUUS = 4;
	private boolean intro = false;
	private int pituus = 0;
	private int taulunKoko = 10;
	private int siirrot = 0;
	private int lisapisteet;
	private Timer timer1 = null;  //  @jve:decl-index=0:visual-constraint="526,157"
	private boolean peto = false;
	
	private int[] rY = {5, 3, 7, 3, 8};
	private int[] rX = {2, 7, 5, 4, 6};
	
	private SijainninLuokka[] rL = {SijainninLuokka.o, SijainninLuokka.o,
			SijainninLuokka.o, SijainninLuokka.c, SijainninLuokka.c};
	
	private static String mato_intro = " __  __    _  _____ ___ \n"+
	"|  \\/  |  / \\|_   _/ _ \\\n"+
	"| |\\/| | / _ \\ | || | | |       Versio 2.0\n"+
	"| |  | |/ ___ \\| || |_| |     Ohjelmoinut IH\n"+
	"|_|  |_/_/   \\_\\_| \\___/\n"+
	"  ____  _____ _     ___ \n"+
	" |  _ \\| ____| |   |_ _|      ###....###.....\n"+
	" | |_) |  _| | |    | |       ..####.#.#####+\n"+
	" |  __/| |___| |___ | |       .....###.......\n"+
	" |_|   |_____|_____|___|    Paina 5 jatkaaksesi.";
	
	private static String peto_intro = "  ____  _____ _____ ___  \n"+
	" |  _ \\| ____|_   _/ _ \\\n"+
	" | |_) |  _|   | || | | |       Versio 2.0\n"+
	" |  __/| |___  | || |_| |     Ohjelmoinut IH\n"+
	" |_|   |_____| |_| \\___/\n"+
	"  ____  _____ _     ___ \n"+
	" |  _ \\| ____| |   |_ _|      .###+.o......#.\n"+
	" | |_) |  _| | |    | |       .#.....o.###.#.\n"+
	" |  __/| |___| |___ | |       .#########.###.\n"+
	" |_|   |_____|_____|___|    Paina 5 jatkaaksesi.";
	
	private void viesti(String v) {
		JLLauta.setText(taulu1.toString()+
				"Aika (siirtoina): " + siirrot + ". Matosi pituus: " + pituus +
				". Pisteet: " + (pituus + lisapisteet)/ALOITUSPITUUS + ".\n" + v);
	}
	
	public class liikutaMatoa extends TimerTask{

		@Override
		public void run() {
			if (!pelaaja1.onkoSolmussa() && peto == false){
				pelaaja1.liikuSuuntaan(suunta);
				taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix());
				viesti("");
				siirrot += 1;
				if ((siirrot % 3) == 0){
					pelaaja1.korotaPituus();
					pituus += 1;
				}
			}
			else if(!pelaaja1.onkoSolmussa() && peto == true){
				pelaaja1.liikuSuuntaan(suunta);
				taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix(), rY, rX, rL);
				viesti("  ------------------ Petopeli ------------------");
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
						viesti("Sait lisäpisteen!");
						break;
					case ja:
						viesti("Törmäsit! Menetit lisäpisteen!");
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
	public matopeliV(String[] args) {
		super();
		initialize();
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
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("/home/iikka/workspace/peli2/bin/Kuvat/mato.png"));
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle("Matopeli");
		this.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				if (e.getKeyChar() == '5' && intro == false){
					suunta = 2;
					timer1 = new Timer(true);
					timer1.scheduleAtFixedRate(new liikutaMatoa(), (long)1000, (long)1000);
					intro = true;
				}
				else if(intro){
					//System.out.println("keyReleased()\ne.toString() = " + e.toString() + "\ne.getKeyChar() = " + e.getKeyChar());
					if (!pelaaja1.onkoSolmussa()){
						Character chrSuunta = new Character(e.getKeyChar());
						suunta = e.getKeyCode();
						siirrot += 1;
						int s = pelaaja1.liikuSuuntaan(suunta);
						taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix());
						if (s == -1) viesti("Liiku nuolinäppäimillä.");
						JLLauta.setText(taulu1.toString()+
								"Aika (siirtoina): " + siirrot + ". Matosi pituus: " + pituus + ". Pisteet: " + (pituus)/ALOITUSPITUUS + ".");
						if ((siirrot % 5) == 0){
							pelaaja1.korotaPituus();
							pituus += 1;
						}
					}
					else JLLauta.setText("Törmäsit! Tuloksesi: " + (pituus + lisapisteet)/ALOITUSPITUUS);
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
			JLLauta.setText(mato_intro);
			JLLauta.setMinimumSize(new Dimension(437, 157));
			JLLauta.setEditable(false);
			JLLauta.setBackground(Color.white);
			JLLauta.setFont(new Font("Courier New", Font.PLAIN, 12));
			JLLauta.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					if (e.getKeyChar() == '5' && intro == false){
						suunta = 2;
						timer1 = new Timer(true);
						timer1.scheduleAtFixedRate(new liikutaMatoa(), 1000, 1000);
						intro = true;
					}
					else if(intro){
						//System.out.println("keyReleased()\ne.toString() = " + e.toString() + "\ne.getKeyChar() = " + e.getKeyChar() + "\ne.getKeyCode() = " + e.getKeyCode());
						if (!pelaaja1.onkoSolmussa()){
							Character chrSuunta = new Character(e.getKeyChar());
							suunta = e.getKeyCode();
							siirrot += 1;
							int s = pelaaja1.liikuSuuntaan(suunta);
							taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix());
							if (s == -1) viesti("Liiku nuolinäppäimillä.");
							else JLLauta.setText(taulu1.toString()+
									"Aika (siirtoina): " + siirrot + ". Matosi pituus: " + pituus + ". Pisteet: " + (pituus + lisapisteet)/ALOITUSPITUUS + ".");
							if (!peto){
								taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix());
								if (s != -1) JLLauta.setText(taulu1.toString()+
										"Aika (siirtoina): " + siirrot + ". Matosi pituus: " + pituus + ". Pisteet: " + (pituus + lisapisteet)/ALOITUSPITUUS + ".");
					
								if ((siirrot % 5) == 0){
									pelaaja1.korotaPituus();
									pituus += 1;
								}
								if (s == -1) viesti("Liiku nuolinäppäimillä.");
							}
							else{
								taulu1 = new Matotaulu(pituus, taulunKoko, pelaaja1.getSijaintiy(), pelaaja1.getSijaintix(), rY, rX, rL);
								if (s != -1) JLLauta.setText(taulu1.toString()+
										"Aika (siirtoina): " + siirrot + ". Matosi pituus: " + pituus + ". Pisteet: " + (pituus + lisapisteet)/ALOITUSPITUUS + ".");
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
										JLLauta.setText(taulu1.toString()+
												"Aika (siirtoina): " + siirrot + ". Matosi pituus: " + pituus +
												". Pisteet: " + (pituus + lisapisteet)/ALOITUSPITUUS + ".\nSait lisäpisteen!");
										break;
									case ja:
										JLLauta.setText(taulu1.toString()+
												"Aika (siirtoina): " + siirrot + ". Matosi pituus: " + pituus +
												". Pisteet: " + (pituus + lisapisteet)/ALOITUSPITUUS + ".\nTörmäsit! Menetit lisäpisteen!");
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
								if (s == -1) viesti("Liiku nuolinäppäimillä.");
							}
							
						}
						else JLLauta.setText("Törmäsit! Tuloksesi: " + (pituus + lisapisteet)/ALOITUSPITUUS);
					}
					else if(e.getKeyChar() == ' '){
						timer1.cancel();
						JLLauta.setText(mato_intro);
						peto = false;
						
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
						siirrot = 0;
						lisapisteet = 0;
					}
					else if(e.getKeyChar() == 'e'){
						JLLauta.setText(peto_intro);
						peto = true;
						for(int i = 0; i < rY.length; i++){
							Random noppa = new Random();
							rY[i] = noppa.nextInt(10);
							rX[i] = noppa.nextInt(10);
						}
					}
					else if(e.getKeyChar() == 'h'){
						if (timer1 != null) timer1.cancel();
						JLLauta.setText("Matopeli - ohjeet\n" +
										"Matopeli on yksinkertainen peli,\n" +
										"jossa on tarkoitus ohjata matoa niin,\n" +
										"että mato ei törmää häntäänsä.\n" +
										"ohjaus toimii numeronäppäimillä.\n\n" +
										"Petopeli - ohjeet\n" +
										"Petopeli on matopelin lisäosa,\n" +
										"jossa mato kasvaa syömällä ruokaa.\n" +
										"o - mato kasvaa yhden ruudun. c - kaksi ruutua.\n" +
										"ö - lisäpiste. & - seinä, mato menettää lisäpisteen.\n" +
										"Pisteiden lasku: pisteet = (pituus + lisapisteet) / 4");
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
		    matopeliV ikkuna;
			
		    ikkuna = new matopeliV(args);
		    ikkuna.setVisible(true);
		  }


}  //  @jve:decl-index=0:visual-constraint="10,10"
