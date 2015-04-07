package cubegame.credits;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cubegame.GameState;
import cubegame.GameWindow;
import cubegame.MenuState;
import cubegame.Texture;
import cubegame.TextureLoader;

public class SplashState implements GameState {

	public static final String NAME = "splash";
	private Texture starstoneImage;
	private int splashCounter;
	private int image;
	private Texture sunMakerImage;
	
	@Override
	public void enter(GameWindow window) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public void init(GameWindow window) throws IOException {
		image = STARSTONE;
		starstoneImage = (new TextureLoader()).getTexture("res/starstone.jpg");
		sunMakerImage = (new TextureLoader()).getTexture("res/sunMaker.jpg");
	}

	@Override
	public void leave(GameWindow window) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameWindow window, int delta) {
		drawBackground(window);
	}

	private void drawBackground(GameWindow window) {
		window.enterOrtho();
		
		switch(image){
		case STARSTONE:
			starstoneImage.bind();
			break;
		case SUN_MAKER:
			sunMakerImage.bind();
			break;
		}
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0,0);
			GL11.glVertex2i(0,0);
			GL11.glTexCoord2f(0,0.58f);
			GL11.glVertex2i(0,600);
			GL11.glTexCoord2f(0.78f,0.58f);
			GL11.glVertex2i(800,600);
			GL11.glTexCoord2f(0.78f,0);
			GL11.glVertex2i(800,0);
		GL11.glEnd();
		
		window.leaveOrtho();
	}
	
	@Override
	public void update(GameWindow window, int delta) {
		splashCounter++;
		
		if (splashCounter == 600) image = SUN_MAKER;
		if (splashCounter == 1200) window.changeToState(MenuState.NAME);
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) window.changeToState(MenuState.NAME);
	}

	private static final int STARSTONE = 0;
	private static final int SUN_MAKER = 1;
}
