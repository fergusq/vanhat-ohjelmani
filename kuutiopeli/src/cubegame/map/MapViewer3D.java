package cubegame.map;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.*;

import cubegame.Cube;
import cubegame.CubeType;
import cubegame.Entity;
import cubegame.GameState;
import cubegame.GameWindow;
import cubegame.Human;
import cubegame.Texture;
import cubegame.render.CubeRenderer;
import cubegame.save.BCCHelper;
import cubegame.sound.SoundLoader;
import cubegame.texture.TextureLoader;
 
public class MapViewer3D {

	private static final float FOG_DISTANCE = 20;
	HashMap<String, Entity> entities = new HashMap<String, Entity>();
	private CubeMap cubeMap;
	private double angle;
	private FloatBuffer material;
	private int delta;
	private float pitch;
	private float dy;
	private float dx;
	private float yaw;
	private float rotY;
	private float forwardX;
	private float frwardX;
	private float forwardZ;
	private float forwardY;
	private float posX;
	private float posY;
	private float posZ;
	private int kartta;

	public MapViewer3D() {
		try {
			// find out what the current bits per pixel of the desktop is

			int currentBpp = Display.getDisplayMode().getBitsPerPixel();
			// find a display mode at 800x600

			DisplayMode mode = findDisplayMode(800, 600, currentBpp);
			
			// if can't find a mode, notify the user the give up

			if (mode == null) {
				Sys.alert("Error", "800x600x"+currentBpp+" display mode unavailable");
				return;
			}
			
			// configure and create the LWJGL display

			Display.setTitle("Kuutiot");
			Display.setDisplayMode(mode);
			Display.setFullscreen(false);
			
			Display.create();
			
			// initialise the game states

			init();
			gameLoop();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Sys.alert("Error", "Failed: "+e.getMessage());
		}
	}
	
	/**
	 * Defint the light setup to view the scene
	 */
	private void defineLight() {
		FloatBuffer buffer;
		
		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(0.1f).put(0.1f).put(0.1f).put(0.2f); 
		buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, buffer);
		
		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(0.1f).put(0.1f).put(0.1f).put(1);
		buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, buffer);
		
		// setup the ambient light 

		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(0.1f).put(0.1f).put(0.1f).put(0.8f);
		buffer.flip();
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, buffer);
		GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, GL11.GL_TRUE);
		
		// set up the position of the light

		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(0).put(10).put(0).put(1);
		buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, buffer);
		
		GL11.glEnable(GL11.GL_LIGHT0);
		
		material = BufferUtils.createFloatBuffer(4);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		//GL11.glEnable( GL11.GL_FOG ); 
		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
		GL11.glHint( GL11.GL_FOG_HINT, GL11.GL_NICEST);
		GL11.glFogf( GL11.GL_FOG_DENSITY, 2.35f );
	    GL11.glFogf( GL11.GL_FOG_START, FOG_DISTANCE );
	    GL11.glFogf( GL11.GL_FOG_END, FOG_DISTANCE + 4);
	    //GL11.glFog(GL11.GL_FOG_COLOR, createColor(0.0f, 0.6f, 1.0f, 1.0f)); 
	    
	}
	
	private void tick(int delta) {
		// Musta tausta, tyhjennetään puskurit, asetetaan moodi, reset
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // Skaalataan kuutio halutun kokoiseksi
        GL11.glScaled(0.0004, 0.0004, 0.0004);

        // Muutetaan kulmaa
        angle += 0.4;

        // Liikutetaan kameraa metodilla glRotated(x,y,z),
        // jonka jälkeen kutsutaan kuvio listasta 1
        GL11.glRotatef((float) angle, 0.4f, 0.7f, 0.8f);
        render(delta);
		
	}
	
	/**
	 * @see GameState#render(GameWindow, int)
	 */
	public void render(int delta) {
		// reset the view transformation matrix back to the empty

		// state. 

		GL11.glLoadIdentity();

		material.put(1).put(1).put(1).put(1); 
		material.flip();
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, material);
		GL11.glMaterial(GL11.GL_BACK, GL11.GL_DIFFUSE, material);
		
		// draw our background image

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(1, 1, 1);
		/*Sphere starMap = new Sphere();
		red.bind();
		starMap.draw(92 * 3.14f, 10, 10);*/
		
		/*try {
			cubeMap.render();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		float slow = 0.009f;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
			 posX = posX - slow  * (float)Math.sin(Math.toRadians(rotY));
			 posZ = posZ + slow * (float)Math.cos(Math.toRadians(rotY));
			 
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			posX = posX - slow * (float)Math.sin(Math.toRadians(rotY-90));
			posZ = posZ + slow * (float)Math.cos(Math.toRadians(rotY-90));

		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			posX = posX - slow * (float)Math.sin(Math.toRadians(rotY+90));
			posZ = posZ + slow * (float)Math.cos(Math.toRadians(rotY+90));

		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
			posX = posX + slow * (float)Math.sin(Math.toRadians(rotY)); 
			posZ = posZ - slow * (float)Math.cos(Math.toRadians(rotY));

		}
		
		Mouse.setGrabbed(true);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		pitch = dy + Mouse.getY()*100;
		dy = Mouse.getY()*100;
		yaw = dx + Mouse.getX();
		dx = Mouse.getX();
		rotY = yaw * 0.25f;
		GLU.gluLookAt(posX, posY, posZ, 
			posX+(frwardX*1.1f), pitch/10, 
			posZ+(forwardZ*1.1f), 0, 1, 0);//0.6f, ent.getX(), ent.getY());
		
		forwardX = (float) ((float) 0.009f * -Math.sin(Math.toRadians(rotY)));
		forwardY = (float) ((float) 0.009f * Math.sin(Math.toRadians(rotY)));
		forwardZ = (float) ((float) 0.009f * -Math.cos(Math.toRadians(rotY)));
		
		GL11.glCallList(kartta);
	}
	
	/**
	 * Get the current time in milliseconds based on the LWJGL
	 * high res system clock.
	 * 
	 * @return The time in milliseconds based on the LWJGL high res clock
	 */
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * The main game loop which is cycled rendering and updating the
	 * registered game states
	 */
	public void gameLoop() {
		boolean gameRunning = true;
		long lastLoop = getTime();
		
		
		// while the game is running we loop round updating and rendering

		// the current game state

		Thread gameloop = new Thread();
		
		while (gameRunning) {
			// calculate how long it was since we last came round this loop

			// and hold on to it so we can let the updating/rendering routine

			// know how much time to update by

			delta = (int) (getTime() - lastLoop);
			lastLoop = getTime();
			
			// clear the screen and the buffer used to maintain the appearance

			// of depth in the 3D world (the depth buffer)

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			// cause the game state that we're currently running to update

			// based on the amount of time passed

			
			// cause the game state that we're currently running to be 

			// render

			tick(delta);
			
			// finally tell the display to cause an update. We've now

			// rendered out scene we just want to get it on the screen

			// As a side effect LWJGL re-checks the keyboard, mouse and

			// controllers for us at this point

			Display.update();
			
			try {
				Display.sync(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// if the user has requested that the window be closed, either

			// pressing CTRL-F4 on windows, or clicking the close button

			// on the window - then we want to stop the game

			try {
				gameloop.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (Display.isCloseRequested()) {
				gameRunning = false;
				System.exit(0);
			}
		}
	}

	/**
	 * Determine an available display that matches the specified 
	 * paramaters.
	 * 
	 * @param width The desired width of the screen
	 * @param height The desired height of the screen
	 * @param bpp The desired colour depth (bits per pixel) of the screen
	 * @return The display mode matching the requirements or null
	 * if none could be found
	 * @throws LWJGLException Indicates a failure interacting with the LWJGL
	 * library.
	 */
	private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		DisplayMode mode = null;
		
		for (int i=0;i<modes.length;i++) {
			if ((modes[i].getBitsPerPixel() == bpp) || (mode == null)) {
				if ((modes[i].getWidth() == width) && (modes[i].getHeight() == height)) {
					mode = modes[i];
				}
			}
		}
		
		return mode;
	}
	
	/**
	 * Initialise the window and the resources used for the game
	 */
	public void init() {
		// initialise our sound loader to determine if we can

		// play sounds on this system

		SoundLoader.get().init();
		
		// run through some based OpenGL capability settings. Textures

		// enabled, back face culling enabled, depth texting is on,
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);  
	
		// define the properties for the perspective of the scene

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();		
		GLU.gluPerspective(45.0f, ((float) 800) / ((float) 600), 0.1f, 100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); 
		
		defineLight();
		
		material = BufferUtils.createFloatBuffer(4);
		
		ArrayList<Entity> saved = new ArrayList<Entity>();
		try {
			saved.add(BCCHelper.readBCCPlayerFile("player.bcc"));
			//saved.addAll(BCCHelper.readBCCFileAndReturnEntityMap("cubemap.bcc"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*try {
			//spawnFloor(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//if (!saved.isEmpty()) entities.addAll(saved);
		
		BasicMapGenerator gen = new BasicMapGenerator();
		cubeMap = gen.generate();
		
		saved = cubeMap.getEntityMap();
		
		if (!saved.isEmpty()) {
			for (Entity e : saved) 
				if (e instanceof Cube)entities.put((int) e.getX()+"x"+(int) e.getY()+"x"+(int) e.getZ(), e);
		}
		
		cubeMap.convertFromEntityMapToCubeMap(entities);
		
		CubeRenderer r = new CubeRenderer();
		
		kartta = GL11.glGenLists(1);
		  GL11.glNewList(kartta, GL11.GL_COMPILE);
		  for (int x = 0; x < 64; x++){
				for (int y = 0; y < 64; y++){
					for (int z = 0; z < 64; z++){
						try {
							if (cubeMap.getCube(x, y, z) != 0)
								r.listCube(x, y, z, CubeType.getTypeFromNumber(cubeMap.getCube(x, y, z)));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		  }
		  GL11.glEndList();
	}
	
	public static void main(String[] args){
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex){
			
		}
		
		MapViewer3D f = new MapViewer3D();
		System.exit(0);
	}
	
}