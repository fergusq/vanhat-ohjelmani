package sunmaker.alphastorm;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import sunmaker.ecreep.gui.FontRenderer;

public class RogueEngine implements Engine {

	private GameMap map;
	private Node player;
	private int clickTimeout = 500;

	public RogueEngine() {
		map = new GameMap(32, 32);
		player = new Node(0, 0);
	}

	@Override
	public void onRender() {
		GL11.glLoadIdentity();
		
		GL11.glPushMatrix();
		
		GL11.glColor4f(1, 1, 1, 1);
		
		GL11.glBegin(6); {
			
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(512, 0);
			GL11.glVertex2f(512, 512);
			GL11.glVertex2f(0, 512);
			
		} GL11.glEnd();
		
		GL11.glPopMatrix();
		
		for (int x = 0; x < 32; x++) {
			for (int y = 0; y < 32; y++) {
				renderNode(x, y, map.getNode(x, y));
			}
		}
		
		renderNode(player.x, player.y, PLAYER);
		
	}

	private void renderNode(int x, int y, int node) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		FontRenderer.sRender(x*16, y*16, getNodeText(node));
		GL11.glDisable(GL11.GL_BLEND);
	}

	private String getNodeText(int node) {
		switch (node) {
		case 0:
			return ".";
		case STONE:
			return "#";
		case PLAYER:
			return "@";
		default:
			return " ";
		}
	}

	@Override
	public void onUpdate(int time) {
		boolean isTime = true; //= System.currentTimeMillis() % 50 == 0;
		
		//map.setNode(player.x, player.y, 0);
		
		if (clickTimeout < 0) {
			isTime = false;
			clickTimeout++;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && isTime) {
			player.y -= 1;
			clickTimeout = -500;
		}
		
		if (clickTimeout < 0) {
			isTime = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && isTime) {
			player.y += 1;
			clickTimeout = -500;
		}
		
		if (clickTimeout < 0) {
			isTime = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && isTime) {
			player.x -= 1;
			clickTimeout = -500;
		}
		
		if (clickTimeout < 0) {
			isTime = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && isTime) {
			player.x += 1;
			clickTimeout = -500;
		}
		
		//map.setNode(player.x, player.y, PLAYER);
	}

	public static final int STONE = 1;
	public static final int PLAYER = 2;
}
