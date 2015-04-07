package cubegame;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import sunmaker.ecreep.*;
import sunmaker.ecreep.gui.GUIInput;
import sunmaker.ecreep.gui.GUIMenu;

/**
 * The menu state display options for the player to start the game
 * 
 * @author Kevin Glass
 */
public class MenuState implements GameState {
	/** The game unique name of this state */
	public static final String NAME = "menu"; //$NON-NLS-1$
	/** The index of the state the game option */
	private static final int START = 0;
	/** The index of the exit the game option */
	private static final int EXIT = 1;
	
	private static final int CUBE = 2;
	
	private static final int MULTIPLAYER = 3;
	
	/** The texture to display in the background */
	private Texture background;
	/** The font to draw to the screen with */
	private BitmapFont font;
	
	/** The options to present to the user */
	private String[] options = new String[] {Messages.getString("menu.startGame"), Messages.getString("menu.exit"), Messages.getString("menu.lowLag"), Messages.getString("menu.multiplayer")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	/** The index of the option selected */
	private int selected = 0;
	private GUIMenu menu;
	private GUIInput host;
	private int cx;
	private int cy;
	private boolean isMultiplayer;
	private int timer;
	public static String serverHost = Messages.getString("options.multiplayer.host");
	public static String serverNick = "Player";
	
	/**
	 * @see org.newdawn.asteroids.GameState#getName()
	 */
	public String getName() {
		return NAME;
	}

	/**
	 * @see cubegame.GameState#init(cubegame.GameWindow)
	 */
	public void init(GameWindow window) throws IOException {
		TextureLoader loader = new TextureLoader();
		background = loader.getTexture(Messages.getString("menu.background")); //$NON-NLS-1$
		
		Texture fontTexture = loader.getTexture(Messages.getString("menu.fontImage")); //$NON-NLS-1$
		font = new BitmapFont(fontTexture, 32, 32);
		
		menu = new GUIMenu(Messages.getString("menu.title"), options, (Color) Color.BLUE, 800, 600);
		host = new GUIInput(Messages.getString("menu.enterIp"), (Color) Color.BLUE, 800, 600);
		host.text = serverHost;
	}

	/**
	 * @see cubegame.GameState#render(cubegame.GameWindow, int)
	 */
	public void render(GameWindow window, int delta) {
		GL11.glColor3f(0.2f,0.2f,0.3f);
		//drawBackground(window);
		
		window.enterOrtho();

		/*GL11.glColor3f(1f,1f,1f);
		font.drawString(1, Messages.getString("MenuState.title"), 280, 210); //$NON-NLS-1$
		
		for (int i=0;i<options.length;i++) {
			GL11.glColor3f(0.5f,0.5f,0);
			if (selected == i) {
				GL11.glColor3f(1,1,0.3f);
			}
			font.drawString(0, options[i], 270, 280+(i*40));
		}
		*/
		if (!isMultiplayer) {
			menu.render(cx, cy);
		} else {
			host.render(cx, cy);
		}
		
		window.leaveOrtho();
	}

	/**
	 * Draw a background to the window
	 * 
	 * @param window The window to which the background should be drawn
	 */
	private void drawBackground(GameWindow window) {
		window.enterOrtho();
		
		background.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0,0);
			GL11.glVertex2i(0,0);
			GL11.glTexCoord2f(0,1);
			GL11.glVertex2i(0,600);
			GL11.glTexCoord2f(1,1);
			GL11.glVertex2i(800,600);
			GL11.glTexCoord2f(1,0);
			GL11.glVertex2i(800,0);
		GL11.glEnd();
		
		window.leaveOrtho();
	}
	
	/**
	 * @see org.newdawn.asteroids.GameState#moveUpdate(org.newdawn.asteroids.GameWindow, int)
	 */
	public void update(GameWindow window, int delta) {
		cx = Mouse.getX();
		cy = -Mouse.getY()+600;
		
		if (!isMultiplayer)
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
					selected--;
					if (selected < 0) {
						selected = options.length - 1;
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					selected++;
					if (selected >= options.length) {
						selected = 0;
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					if (selected == START) {
						window.changeToSingleplayer();
						window.changeToState(CubeState.NAME);
					}
					if (selected == EXIT) {
						System.exit(0);
					}
					if (selected == CUBE) {
						window.changeToSingleplayer();
						window.changeToState(CubeState.NAME);
					}
					if (selected == MULTIPLAYER) {
						/*window.changeToMultiplayer();
						window.changeToState(CubeState.NAME);*/
						isMultiplayer = true;
					}
				}
			}
		}
		
		if (isMultiplayer) host.update(cx, cy);
		
		if (Mouse.isButtonDown(0) && !isMultiplayer){
			String option = menu.click(cx, cy);
			if (option == null) return;
			if (option.equalsIgnoreCase(Messages.getString("menu.startGame"))){
				window.changeToSingleplayer();
				window.bestQuality();
				window.changeToState(CubeState.NAME);
			} else if (option.equalsIgnoreCase(Messages.getString("menu.exit"))){
				System.exit(0);
			} else if (option.equalsIgnoreCase(Messages.getString("menu.lowLag"))){
				window.changeToSingleplayer();
				window.startLowLagMode();
				window.changeToState(CubeState.NAME);
			} else if (option.equalsIgnoreCase(Messages.getString("menu.multiplayer"))){
				isMultiplayer = true;
				System.out.println("Multiplayer...");
				timer = 100;
				return;
			}
		} else if (host.select && isMultiplayer && timer <= 0){
			if (host.cancel) {
				isMultiplayer = false;
				return;
			}
			serverHost  = host.text;
			System.out.println("Ip selected: " + serverHost);
			window.changeToMultiplayer();
			window.changeToState(CubeState.NAME);
			isMultiplayer = false;
		} else {
			timer--;
		}
	}

	/**
	 * @see cubegame.GameState#enter(cubegame.GameWindow)
	 */
	public void enter(GameWindow window) {
		Mouse.setGrabbed(false);
	}

	/**
	 * @see cubegame.GameState#leave(cubegame.GameWindow)
	 */
	public void leave(GameWindow window) {
	}

}
