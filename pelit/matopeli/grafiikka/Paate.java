package grafiikka;

import java.awt.*;
import javax.swing.*;

public class Paate extends JFrame {
	private JLabel teksti;
	
	public Paate(String otsikko) {
		
	    // luodaan ikkuna
	    super(otsikko);

	    // luodaan komponentit
	    this.teksti = new JLabel("Paate V 0.1");
	    // otetaan selville ikkunaan liittyvä sisältöpaneeli
	    // ja määritellään sen asemointi
	    Container sisaltopaneeli;
	    sisaltopaneeli = this.getContentPane();
	    sisaltopaneeli.setLayout(new FlowLayout());

	    // Lisätään luodut komponentit sisältöpaneeliin
	    sisaltopaneeli.add(this.teksti);

	    // asetetaan ikkunan koko
	    this.setSize(300, 100);
	}
	
	public void asetaTeksti(String teksti){
		this.teksti = new JLabel(teksti);
	}
	
	

}
