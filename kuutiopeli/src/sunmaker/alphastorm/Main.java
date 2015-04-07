package sunmaker.alphastorm;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import cubegame.log.Logger;

import sunmaker.ecreep.texture.BitmapFont;
import sunmaker.ecreep.texture.Texture;
import sunmaker.ecreep.texture.TextureLoader;

public class Main {

	Engine e;
	private Logger logger;
	
	public Main (Engine e) {
		this.e = e;
	}
	
	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(256*2, 256*2));
			Display.create();	
			Display.setTitle("Alphastorm");
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		init();

		
        long l = System.currentTimeMillis();
        long l1 = 0L;
		
		while (!Display.isCloseRequested()) {

            long l2 = System.currentTimeMillis();
            long l3 = l2 - l;
            if(l3 > 2000L)
            {
                logger.warn("Can't keep up! Did the system time change, or is the server overloaded?");
                l3 = 2000L;
            }
            if(l3 < 0L)
            {
                logger.warn("Time ran backwards! Did the system time change?");
                l3 = 0L;
            }
            l1 += l3;
            l = l2;
			
            while(l1 > 50L) 
            {
                l1 -= 50L;

            }
            
			e.onRender();

			e.onUpdate((int)l);
			
			Display.update();
            //try {
			//	Thread.sleep(1L);
			//} catch (InterruptedException e) { logger.warn(e.getMessage()); }
		}

		Display.destroy();
	}

	private void init() {
		/*Texture fontTex = null;
		try {
		fontTex = (new TextureLoader()).getTexture("font.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//font = new BitmapFont(fontTex, 32, 32);*/
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 256*2, 256*2, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main m = new Main(new PlatformEngine());

		m.start();

	}

}
