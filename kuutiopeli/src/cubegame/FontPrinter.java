package cubegame;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;


public class FontPrinter {
	@SuppressWarnings("deprecation")
	private TrueTypeFont font;
	private TrueTypeFont font2;
	private Texture texture;

	@SuppressWarnings("deprecation")
	public FontPrinter(Texture texture){
		
		this.texture = texture;
		// load a default java font
		Font awtFont = new Font("Times New Roman", Font.BOLD, 32);
		font = new TrueTypeFont(awtFont, false);
			
		// load font from a .ttf file
		try {
			
			FileInputStream fis;
			fis = new FileInputStream(new File("res/AbductionII.ttf"));
			
			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, fis);
			awtFont2 = awtFont2.deriveFont(32f); // set font size
			font2 = new TrueTypeFont(awtFont2, false);
				
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}
	
	@SuppressWarnings("deprecation")
	public void write(int font, String text, int x, int y){
		switch (font){
		case 0:
			texture.bind();
			font2.drawString(x, y, text, Color.white, 32, 32);
		case 1:
			this.font.drawString(x, y, text, Color.white, 32, 32);
		}
		
	}
}
