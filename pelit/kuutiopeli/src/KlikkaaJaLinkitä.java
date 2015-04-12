import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUConstants;
import org.lwjgl.util.glu.*;

import cubegame.texture.TextureLoader;
 
public class KlikkaaJaLinkitä {
 
	enum Vaihe{
		Alku,
		nappi1,
		nappi2,
		nappi3,
		nappi4,
		törmäys
	}
	
	Vaihe vaihe = Vaihe.Alku;
	/** position of quad */
	float x = 400, y = 300;
	float vX = 400, vY = 300; //Vastustaja
	/** angle of quad rotation */
	float rotation = 0;
 
	/** time at last frame */
	long lastFrame;
 
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	
	/** is VSync Enabled */
	boolean vsync;
	
	TextureLoader texLoader = new TextureLoader();
 
	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
 
		initGL(); // init OpenGL
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
		
		vaihe = Vaihe.nappi1;
		
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
 
			update(delta);
			renderGL();
 
			Display.update();
			Display.sync(60); // cap fps to 60fps
		}
 
		Display.destroy();
	}
 
	public void update(int delta) {
		// rotate quad
		rotation += 0.15f * delta;
 
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) vX -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) vX += 0.35f * delta;
 
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) vY -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) vY += 0.35f * delta;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) System.exit(0);
 
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_F) {
		        	setDisplayMode(800, 600, !Display.isFullscreen());
		        }
		        else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
		        	vsync = !vsync;
		        	Display.setVSyncEnabled(vsync);
		        }
		    }
		}
		
		Mouse.setGrabbed(true);
		
		x = Mouse.getX();
		y = Mouse.getY();
		
		// keep quad on the screen
		if (x < 0) x = 0;
		if (x > 800) x = 800;
		if (y < 0) y = 0;
		if (y > 600) y = 600;
		
		// keep quad on the screen
		if (vX < 0) vX = 0;
		if (vX > 800) vX = 800;
		if (vY < 0) vY = 0;
		if (vY > 600) vY = 600;
		
		if (x > 10 && x < 30 && y > 10 && y < 30 && vaihe == Vaihe.nappi1) vaihe = Vaihe.nappi2; //nappi 1
		
		if (x > 770 && x < 790 && y > 570 && y < 590 && vaihe == Vaihe.nappi2) vaihe = Vaihe.nappi3; //nappi 2
		
		if (x > 10 && x < 30 && y > 570 && y < 590 && vaihe == Vaihe.nappi3) vaihe = Vaihe.nappi4; //nappi 3
		
		if (x > 770 && x < 790 && y > 10 && y < 30 && vaihe == Vaihe.nappi4) vaihe = Vaihe.nappi1; //nappi 4
		
		if (x > vX - 10 && x < vX + 10 && y > vY - 10 && y < vY + 10) { vaihe = Vaihe.törmäys; } //vastustaja
		
		if (vaihe == Vaihe.törmäys);
 
		updateFPS(); // update FPS Counter
	}
 
	/**
	 * Set the display mode to be used 
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public void setDisplayMode(int width, int height, boolean fullscreen) {

		// return if requested DisplayMode is already set
                if ((Display.getDisplayMode().getWidth() == width) && 
			(Display.getDisplayMode().getHeight() == height) && 
			(Display.isFullscreen() == fullscreen)) {
			return;
		}
		
		try {
			DisplayMode targetDisplayMode = null;
			
			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;
				
				for (int i=0;i<modes.length;i++) {
					DisplayMode current = modes[i];
					
					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against the 
						// original display mode then it's probably best to go for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
						    (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width,height);
			}
			
			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);
			
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
		}
	}
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
 
	    return delta;
	}
 
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
 
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps + " X: " + x + " Y: " + y);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
 
	public void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 600, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
 
	public void renderGL() {
		// Clear The Screen And The Depth Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
 
		// R,G,B,A Set The Color To Blue One Time Only
		GL11.glColor3f(0.5f, 0.5f, 1.0f);
 
		// draw quad
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0);
			//GL11.glRotatef(rotation, 0f, 0f, 1f);
			GL11.glTranslatef(-x, -y, 0);
 
			GL11.glBegin(GL11.GL_QUADS); //hiiri
				GL11.glVertex2f(x - 5, y - 5);
				GL11.glVertex2f(x + 5, y - 5);
				GL11.glVertex2f(x + 5, y + 5);
				GL11.glVertex2f(x - 5, y + 5);
			GL11.glEnd();
			
			if (vaihe != null && vaihe == Vaihe.nappi1) GL11.glColor3f(1.5f, 0.0f, 0.0f); else GL11.glColor3f(0.5f, 0.5f, 1.0f);
			GL11.glBegin(GL11.GL_QUADS); //nappi 1
				GL11.glVertex2f(10, 10);
				GL11.glVertex2f(10, 30);
				GL11.glVertex2f(30, 10);
				GL11.glVertex2f(30, 30);
			GL11.glEnd();
			
			if (vaihe != null && vaihe == Vaihe.nappi2) GL11.glColor3f(1.5f, 0.0f, 0.0f); else GL11.glColor3f(0.5f, 0.5f, 1.0f);
			GL11.glBegin(GL11.GL_QUADS); //nappi 2
				GL11.glVertex2f(770, 570);
				GL11.glVertex2f(770, 590);
				GL11.glVertex2f(790, 570);
				GL11.glVertex2f(790, 590);
			GL11.glEnd();
	
			if (vaihe != null && vaihe == Vaihe.nappi3) GL11.glColor3f(1.5f, 0.0f, 0.0f); else GL11.glColor3f(0.5f, 0.5f, 1.0f);
			GL11.glBegin(GL11.GL_QUADS); //nappi 3
				GL11.glVertex2f(10, 570);
				GL11.glVertex2f(10, 590);
				GL11.glVertex2f(30, 570);
				GL11.glVertex2f(30, 590);
			GL11.glEnd();
			
			if (vaihe != null && vaihe == Vaihe.nappi4) GL11.glColor3f(1.5f, 0.0f, 0.0f); else GL11.glColor3f(0.5f, 0.5f, 1.0f);
			GL11.glBegin(GL11.GL_QUADS); //nappi 4
				GL11.glVertex2f(770, 10);
				GL11.glVertex2f(770, 30);
				GL11.glVertex2f(790, 10);
				GL11.glVertex2f(790, 30);
			GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(vX, vY, 0);
		GL11.glRotatef(rotation, 0f, 0f, 1f);
		GL11.glTranslatef(-vX, -vY, 0);
		
		if (vaihe != null && vaihe == Vaihe.törmäys) GL11.glColor3f(1.5f, 0.0f, 0.0f); else GL11.glColor3f(0.5f, 0.5f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS); //vastustaja
			GL11.glVertex2f(vX - 10, vY - 10);
			GL11.glVertex2f(vX + 10, vY - 10);
			GL11.glVertex2f(vX + 10, vY + 10);
			GL11.glVertex2f(vX - 10, vY + 10);
		GL11.glEnd();
		GL11.glPopMatrix();
		

	}
 
	public static void main(String[] argv) {
		KlikkaaJaLinkitä fullscreenExample = new KlikkaaJaLinkitä();
		fullscreenExample.start();
	}
}
