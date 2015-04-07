import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JFrame;

import sunmaker.ecreep.MsgBox;
import sunmaker.ecreep.MsgBox.MsgType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Peli extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panel = null;
	Kartta peli = new Kartta();  //  @jve:decl-index=0:
	ArrayList<BufferedImage> kuvat = new ArrayList<BufferedImage>();  //  @jve:decl-index=0:
	private int omaisuus = 10;
	private Työkalu väline = Työkalu.sirppi;  //  @jve:decl-index=0:
	private ArrayList<Teko> teot = new ArrayList<Teko>();  //  @jve:decl-index=0:
	private int siirrot = 11;
	private int työläiset = 1;
	private int vuodet = 0;
	private int nälkäVuodet = 0;
	private Random r = new Random();
	private int nollausVuosi = 75+r.nextInt(10);
	private int vaihe = 0;
	private int palkkaKerroin = 1;
	
	class ImgPanel
	extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1639152708263036763L;

		public ImgPanel(){
			super();
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			Peli.this.addKeyListener(this);
			Peli.this.addWindowListener(new WindowListener() {
				
				@Override
				public void windowOpened(WindowEvent e) {
				}
				
				@Override
				public void windowIconified(WindowEvent e) {
				}
				
				@Override
				public void windowDeiconified(WindowEvent e) {
				}
				
				@Override
				public void windowDeactivated(WindowEvent e) {
				}
				
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
				
				@Override
				public void windowClosed(WindowEvent e) {
					System.exit(0);
					
				}
				
				@Override
				public void windowActivated(WindowEvent arg0) {
				}
			});
		}
		
		public void paint(Graphics g){
			//Peli.this.initialize();
			for (int i = 0; i < 20; i++){
				for (int j = 0; j < 14; j++){
				try{
					g.drawImage(haeKuva(i, j), i * 50, j * 50, this);
				}
				catch(NullPointerException ex){
					ex.printStackTrace();
				}
				}
			}
			repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			if (siirrot > 0){
				teot.add(new Teko(e.getX()/50, e.getY()/50, väline, vuodet)); //Lisätään teko listaan
				siirrot--;
			}
			//omaisuus += peli.haeSolu(e.getX()/50, e.getY()/50).korjaa(väline);
			
			if ((vuodet >= 0 && vuodet <= 8) || (vuodet >= 10 && vuodet <= 15) || (vuodet >= 30 && vuodet <= 40) || (vuodet >= 70 && vuodet <= 80)){
				Peli.this.setTitle("Sijainti: " + (e.getX()/50) + ", " + (e.getY()/50) + ", " + peli.haeSolu(e.getX()/50, e.getY()/50).tyyppi + ", " + peli.haeSolu(e.getX()/50, e.getY()/50).vaihe + " Raha: " + omaisuus + " Väline: " + väline + " Siirrot: " + (siirrot) + " Vuodet: " + vuodet + " Metsänkorjuu!!!");
			}
			else Peli.this.setTitle("Sijainti: " + (e.getX()/50) + ", " + (e.getY()/50) + ", " + peli.haeSolu(e.getX()/50, e.getY()/50).tyyppi + ", " + peli.haeSolu(e.getX()/50, e.getY()/50).vaihe + " Raha: " + omaisuus + " Väline: " + väline + " Siirrot: " + (siirrot) + " Vuodet: " + vuodet);
			//repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if ((vuodet >= 0 && vuodet <= 8) || (vuodet >= 10 && vuodet <= 15) || (vuodet >= 30 && vuodet <= 40) || (vuodet >= 70 && vuodet <= 80)){
				Peli.this.setTitle("Sijainti: " + (e.getX()/50) + ", " + (e.getY()/50) + ", " + peli.haeSolu(e.getX()/50, e.getY()/50).tyyppi + ", " + peli.haeSolu(e.getX()/50, e.getY()/50).vaihe + " Raha: " + omaisuus + " Väline: " + väline + " Siirrot: " + (siirrot) + " Vuodet: " + vuodet + " Metsänkorjuu!!!");
			}
			else Peli.this.setTitle("Sijainti: " + (e.getX()/50) + ", " + (e.getY()/50) + ", " + peli.haeSolu(e.getX()/50, e.getY()/50).tyyppi + ", " + peli.haeSolu(e.getX()/50, e.getY()/50).vaihe + " Raha: " + omaisuus + " Väline: " + väline + " Siirrot: " + (siirrot) + " Vuodet: " + vuodet);

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			switch (e.getKeyCode()){
			case KeyEvent.VK_1:    
				väline = Työkalu.sirppi;
				break;
			case KeyEvent.VK_2:    
				väline = Työkalu.viikate;
				break;
			case KeyEvent.VK_3:    
				väline = Työkalu.miekka;
				break;
			case KeyEvent.VK_4:    
				väline = Työkalu.kirves;
				break;
			case KeyEvent.VK_5:    
				väline = Työkalu.saha;
				break;
			case KeyEvent.VK_6:    
				väline = Työkalu.kuokka;
				break;
			case KeyEvent.VK_7:    
				väline = Työkalu.tulukset;
				break;
			case KeyEvent.VK_ENTER:
				peli = new Kartta();
				break;
			case KeyEvent.VK_SPACE:    
				ArrayList<Teko> varmistus = new ArrayList<Teko>(); //Tehdyt teot
				
				for (int i = 0; i < teot.size(); i++){
					varmistus.add(0, teot.get(i)); //Lisää teon tehtyjen tekojen listaan
					boolean v = true;
					for (int t = 1; t < varmistus.size(); t++){
						try{
							if (varmistus.get(t) == teot.get(i) && //Metsää ei voi hakata kahta kertaa
									peli.haeSolu(teot.get(i).getX(), teot.get(i).getY()).tyyppi == SoluTyypit.metsä && 
									teot.get(i).getVäline() != Työkalu.tulukset){
								v = false; //Puinti kielletään
								break;
							}
						}
						catch (NullPointerException ex){
							System.err.println(ex.getMessage());
						}
					}
					
					if (v) omaisuus += peli.tee(teot.get(i)); //Jos v on tosi, solu puidaan ja omaisuus pannaan tilille.
				}
				teot.clear(); // Nollataan teot
				varmistus.clear();
				siirrot = 5*työläiset;
				vuodet++; // Lisätään vuosiin 1
				ilmoitukset();
				break;
			case KeyEvent.VK_T:
				MsgType t = MsgBox.quest("Työläisiä: " + työläiset + 
						"\nPalkkaa lisää työläisiä?", "Maatalous", MsgType.YesNo);
				if (t == MsgType.YES) {
					String s = MsgBox.input("Kuinka monta? (Pääoma: " + omaisuus + " markkaa)"
							, "Maatalous", new String[]{"10", "20", "30", "50", "60", "80"});
					int määrä = Integer.parseInt(s);
					int hinta = määrä*5;
					if (omaisuus > hinta) {
						MsgType vastaus2 = MsgBox.quest("Omaisuutesi nyt: " + omaisuus + 
								" markkaa\nHinta: " + hinta +
								"\nErotus: " + (omaisuus - hinta), "Maatalous", MsgType.YesNo);
						if (vastaus2 == MsgType.YES) {
							työläiset += määrä;
							omaisuus -= hinta;
							MsgBox.msg("Sinulla on nyt " + työläiset + " kpl. työläisiä.", "Maatalous");
						}
					} else {
						MsgBox.warn("Rahasi eivät riitä!", "Maatalous");
					}
				}
				break;
			}
			
				
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	/**
	 * This is the default constructor
	 */
	public Peli() {
		super();
		initialize();
	}

	public void ilmoitukset() {
		if (vuodet == nollausVuosi) {
			MsgBox.msg("Joudumme suruksemme ilmoittaa, että\n" +
					"rakastettu maanviljelijämme \n" +
					"on kuollut. Hänen lapsensa ottavat työn."
					, "Maatalous");
			
			MsgBox.msg("Vuodet nollataan.", "Maatalous");
			vuodet = 0;
			
			int perintö = r.nextInt(2)+1;
			
			MsgBox.msg("Omaisuudesta otetaan \n" +
					perintö+"/3 perinnöksi muille kuin lapsille.\n"
					, "Maatalous");
			
			omaisuus -= (omaisuus / 3 * perintö);
			
			int eronneet = r.nextInt(3)+1;
			
			MsgBox.msg(eronneet + "/5 työläisistä sanoutui irti\n" +
					"uuden esimiehen vuoksi.", "Maatalous");
			
			työläiset -= (työläiset/5)*eronneet;
			
			nollausVuosi = 75+r.nextInt(10);
		}
		
		if (omaisuus > 5) {
			omaisuus -= 5; // Ruokakulut, yms
		} else {
			MsgBox.warn("Kuolet kohta nälkään!\nJoudut ohittamaan tämän vuoden!", "Maatalous");
			vuodet++;
			nälkäVuodet++;
			if (nälkäVuodet == 3) {
				MsgBox.msg("Joudut anomaan apua.", "Maatalous");
				String s = MsgBox.input("Keneltä anot apua?", "Maatalous", new String[]{
						"Maatalousministeriö","Maanviljelijöiden liitto","Maanviljelijöiden Asian Yhdistys"});
				
				int apu = r.nextInt(30);
				
				MsgBox.msg(s+" myönsi sinulle "+apu+" markkaa.", "Maatalous");
				omaisuus += apu;
				nälkäVuodet = 0;
			}
			ilmoitukset();
		}
		
		int palkat = (työläiset * 2) * (palkkaKerroin / 2 + 1);
		
		if (omaisuus > palkat) {
			omaisuus -= palkat; // Palkat
		} else {
			MsgBox.warn("Sinulla ei ole rahaa maksaa palkkoja!\nVoit maksaa vain osalle.", "Maatalous");
			int eronneet = (palkat - omaisuus) / 2;
			MsgBox.msg(eronneet + " sanoutui irti\n" +
					"huonon palkan vuoksi.", "Maatalous");
			työläiset -= eronneet;
		}
		
		int verot = omaisuus/(omaisuus/2);
		
		if (omaisuus > verot) {
			omaisuus -= verot; // Verot
		} else {
			MsgBox.warn("Sinulla ei ole rahaa maksaa veroja!\nVoit maksaa vain osan.", "Maatalous");
			int osa = (verot - omaisuus) / 2;
			omaisuus -= osa;
		}
		
		if (omaisuus > 10000 && vaihe < 2) {
			MsgBox.msg("Maanviljelijöiden liitto arvioi sinut\n" +
					"Kohtuu Hyväksi Maanviljelijäksi\n\n" +
					"ja lahjoittaa sinulle 500 markkaa.", "Maatalous");
			omaisuus += 500;
			vaihe = 2;
		}
		
		if (omaisuus > 50000 && vaihe < 3) {
			MsgBox.msg("Maanviljelijöiden liitto arvioi sinut\n" +
					"Melko Hyväksi Maanviljelijäksi\n\n" +
					"ja lahjoittaa sinulle 1 000 markkaa.", "Maatalous");
			omaisuus += 1000;
			vaihe = 3;
		}
		
		if (omaisuus > 100000 && vaihe < 4) {
			MsgBox.msg("Maanviljelijöiden liitto arvioi sinut\n" +
					"Todella Hyväksi Maanviljelijäksi\n\n" +
					"ja lahjoittaa sinulle 5 000 markkaa.", "Maatalous");
			omaisuus += 5000;
			vaihe = 3;
			MsgType t = MsgBox.quest("Puintityöläiset vaativat lisää palkkaa,\n" +
					"koska sinun omaisuutesi on kasvanut.\n" +
					"Suostutko palkankorotukseen?", "Maatalous", MsgType.YesNo);
			if (t == MsgType.YES) {
				MsgBox.msg("Puintityöläiset kiittävät.", "Maatalous");
				palkkaKerroin++;
			}
			if (t == MsgType.NO) {
				int eronneet = työläiset / 9;
				MsgBox.msg(eronneet + " puintityöläistä sanoutui irti\n" +
						"huonon palkan vuoksi.", "Maatalous");
			}
			vaihe = 4;
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1000, 1500);
		/*kuvat = new ArrayList<BufferedImage>();
		for (int i = 0; i < 20; i++){
			for (int t = 0; t < 14; t++){
				switch(peli.haeSolu(i, t).tyyppi){
				case aro:
					try {
						File url;
						if (haeTeko(i, t) == null){
							url = new File("/home/iikka/workspace/maatalous/bin/nurmikko1.png");
						}
						else
							url = new File("/home/iikka/workspace/maatalous/bin/nurmikkoV.png");
			             kuvat.add(ImageIO.read(url));
			         } catch (IOException e) {
			        	 e.printStackTrace();
			         }
			         catch (IllegalArgumentException ex) {
			        	 ex.printStackTrace();
			         }
					break;
				case metsä:
					try {
						File url;
						if (haeTeko(i, t) == null){
							url = new File("/home/iikka/workspace/maatalous/bin/puu2.png");
						}
						else
							url = new File("/home/iikka/workspace/maatalous/bin/puuV.png");
			             kuvat.add(ImageIO.read(url));
			         } catch (IOException e) {
			        	 e.printStackTrace();
			         }
			         catch (IllegalArgumentException ex) {
			        	 ex.printStackTrace();
			         }
					break;
				case pelto:
					try {
						File url;
						if (haeTeko(i, t) == null){
							url = new File("/home/iikka/workspace/maatalous/bin/vilja1.png");
						}
						else
							url = new File("/home/iikka/workspace/maatalous/bin/vilja1.png");
			             kuvat.add(ImageIO.read(url));
			         } catch (IOException e) {
			        	 e.printStackTrace();
			         }
			         catch (IllegalArgumentException ex) {
			        	 ex.printStackTrace();
			         }
					break;
				case tuli:
					try {
			             File url;
			             if (peli.haeSolu(i, t).Vaihe == vaihe.PalavaMetsä){
			            	 url = new File("/home/iikka/workspace/maatalous/bin/palavaMetsä.gif");
			             }
			             else
			            	 url = new File("/home/iikka/workspace/maatalous/bin/viljaT.png");
			             kuvat.add(ImageIO.read(url));
			         } catch (IOException e) {
			        	 e.printStackTrace();
			         }
			         catch (IllegalArgumentException ex) {
			        	 ex.printStackTrace();
			         }
					break;
				}
				
			}
		}*/
		this.setContentPane(getPanel());
		
	}

	private BufferedImage haeKuva(int x, int y) {
		switch(peli.haeSolu(x, y).tyyppi){
		case aro:
			try {
				if (haeTeko(x, y) == null && peli.haeSolu(x, y).vaihe == Vaihe.Alku){
					return aro;
				}
				else if (peli.haeSolu(x, y).vaihe == Vaihe.Alku) {
					return aroKyntö;
				}
				else if (haeTeko(x, y) == null && peli.haeSolu(x, y).vaihe == Vaihe.Kyntö) {
					return kyntö;
				}
				else if (peli.haeSolu(x, y).vaihe == Vaihe.Kyntö) {
					return kyntöKylvö;
				}
				else if (haeTeko(x, y) == null && peli.haeSolu(x, y).vaihe == Vaihe.Kylvö) {
					return kylvö;
				}
				else if (peli.haeSolu(x, y).vaihe == Vaihe.Kylvö) {
					return kylvöKasvu;
				}
			} catch (IllegalArgumentException ex) {
	        	 ex.printStackTrace();
	         }
			break;
		case metsä:
			try {
				if (peli.haeSolu(x, y).vaihe == Vaihe.Alku){
					return metsä;
				} else if (peli.haeSolu(x, y).vaihe == Vaihe.TaimikonHoito){
					return taimi;
				} else if(peli.haeSolu(x, y).vaihe == Vaihe.TaimikonPerkaus){
					return taimi;
				} else if(peli.haeSolu(x, y).vaihe == Vaihe.ToinenHarvennus){
					return puu;
				} else if(peli.haeSolu(x, y).vaihe == Vaihe.Päätehakkuu){
					return puu;
				} else if(peli.haeSolu(x, y).vaihe == Vaihe.MaaperänMuokkaus){
					return maanmuokkaus;
				} else {
					return metsä;
				}
	         }
	         catch (IllegalArgumentException ex) {
	        	 ex.printStackTrace();
	         }
			break;
		case pelto:
			try {
				if (haeTeko(x, y) == null && peli.haeSolu(x, y).vaihe == Vaihe.Hoito1){
					return pelto1;
				}
				else if (peli.haeSolu(x, y).vaihe == Vaihe.Hoito1) {
					return pelto1Korjuu;
				}
				else if (haeTeko(x, y) == null && peli.haeSolu(x, y).vaihe == Vaihe.Hoito2) {
					return pelto2;
				}
				else if (peli.haeSolu(x, y).vaihe == Vaihe.Hoito2) {
					return pelto2Korjuu;
				}
				else if (haeTeko(x, y) == null && peli.haeSolu(x, y).vaihe == Vaihe.Hoito3) {
					return pelto2;
				}
				else if (peli.haeSolu(x, y).vaihe == Vaihe.Hoito3) {
					return pelto2Korjuu;
				}
				else if (haeTeko(x, y) == null && peli.haeSolu(x, y).vaihe == Vaihe.Korjuu) {
					return pelto2;
				}
				else if (peli.haeSolu(x, y).vaihe == Vaihe.Korjuu) {
					return pelto2Korjuu;
				}
			}catch (IllegalArgumentException ex) {
	        	 ex.printStackTrace();
	         }
			break;
		case tuli:
			try {
	             if (peli.haeSolu(x, y).vaihe == Vaihe.PalavaMetsä){
	            	 return (tuli);
	             }
	             else
	            	 return tuli2;
			}catch (IllegalArgumentException ex) {
	        	 ex.printStackTrace();
	         }
			break;
		}
		return null;
	}
	
	static BufferedImage aro;  //  @jve:decl-index=0:
	static BufferedImage aroKyntö;
	static BufferedImage metsä;
	static BufferedImage puu;
	static BufferedImage taimi;
	static BufferedImage maanmuokkaus;
	static BufferedImage pelto1;
	static BufferedImage pelto1Korjuu;
	static BufferedImage pelto2;
	static BufferedImage pelto2Korjuu;
	static BufferedImage kyntö;
	static BufferedImage kyntöKylvö;
	static BufferedImage kylvö;
	static BufferedImage kylvöKasvu;
	static BufferedImage tuli;
	static BufferedImage tuli2;
	
	static {
		try {
			aro = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/nurmikko1.png"));
			aroKyntö = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/nurmikkoKyntö.png"));
			metsä = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/puu2.png"));
			puu = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/puu3.png"));
			taimi = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/taimi.png"));
			maanmuokkaus = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/maanmuokkaus.png"));
			pelto1 = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/vilja1.png"));
			pelto1Korjuu = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/vilja1Korjuu.png"));
			pelto2 = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/vilja2.png"));
			pelto2Korjuu = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/vilja2Korjuu.png"));
			kyntö = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/kyntö.png"));
			kyntöKylvö = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/kyntöKylvö.png"));
			kylvö = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/kylvö.png"));
			kylvöKasvu = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/kylvöKasvu.png"));
			tuli = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/palavaMetsä.gif"));
			tuli2 = ImageIO.read(new File("/home/iikka/workspace/maatalous/bin/palavaPelto.gif"));
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 */
	private JPanel getPanel() {
		if (panel == null) {
			panel = new ImgPanel();
			panel.setLayout(new BorderLayout());
			//panel.add(b);
			/*for (int i = 0; i < kuvat.size(); i++){
				
				Graphics g = panel.getGraphics();
				try{
					g.drawImage(kuvat.get(i), peli.solut.get(i).x * 50, peli.solut.get(i).y * 50, this);
				}
				catch(NullPointerException ex){
					ex.printStackTrace();
				}
			}*/
		}
		return panel;
	}

	public Teko haeTeko(int x, int y){
		Teko teko = null;
		for (int i = 0; i < teot.size(); i++){
			if (teot.get(i).getX() == x && teot.get(i).getY() == y){
				teko = teot.get(i);
				break;
			}
		}
		return teko;
	}
	
	public static void main(String[] args){
		Peli peli = new Peli();
		peli.setVisible(true);
	}
	
}
