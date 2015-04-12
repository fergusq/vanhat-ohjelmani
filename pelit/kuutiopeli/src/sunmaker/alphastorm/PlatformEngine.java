package sunmaker.alphastorm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import sunmaker.ecreep.gui.FontRenderer;

public class PlatformEngine implements Engine{

	public GameMap map = new GameMap();
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public Entity player = new Entity();
	private int score;
	private boolean gameOver;
	private boolean flyMode = false;
	private int maxScore;
	private boolean won = false;
	private int level;
	private boolean tick;
	private boolean start = false;
	private float velocityX;
	
	public PlatformEngine() {
		initMap(0);
		player.setPosition(4, 5);
		maxScore = 4;
		level = 0;
		start = true;
	}

	public void initMap(int l) {
		map.clear();
		for (int i = 0; i < map.width; i++) {
			map.setNode(i, l, Node.STONE);
			if (i%6==0) map.setNode(i, l+1, Node.STONE);
			if (i%(8-l)==0) map.setNode(i, l+1, Node.POINT);
			if (l < 6 && i%(7-l)==0) map.setNode(i, l, Node.EMPTY);
			if (l >= 6) if (i%(7-5)==0) map.setNode(i, l, Node.EMPTY);
			if (i%(7)==0 && l+1 != 5) map.setNode(i, 5, Node.FIRE);
			else if (i%(7)==0) map.setNode(i, l+2, Node.FIRE);
		}
		map.setNode(4, l, Node.STONE);
	}
	
	public void onUpdate(int time) {
		
		boolean isTime = time % 50 == 0;
		boolean isTime2 = time % 40 == 0;
		boolean isTime3 = time % 100 == 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && isTime3) {
			if (start) {
				start = false;
				return;
			}
			if (won) goToNextLevel();
			if (gameOver) restart();
			
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && isTime3) {
			//if (start) System.exit(0);
			start = true;
			return;
		}
		
		if (start) return;
		
		if (gameOver) return;
		if (won) return;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
					if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
						won = true;
						return;
					}
				}
			}
		}
		
		if (checkIsPlayerDead()) doGameOver();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && isTime /*&& 
				!isSolid(map.getNode((int) Math.floor(player.x+0.6f), (int) Math.floor(player.y)))*/) {
			if (player.velX < player.x) player.velX = player.x;
			if (velocityX < 0) velocityX = 0;
			player.velX += 0.1f;
			velocityX+=0.01f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && isTime/* && 
				!isSolid(map.getNode((int) Math.floor(player.x-0.6f), (int) Math.floor(player.y)))*/) {
			if (player.velX > player.x) player.velX = player.x;
			if (velocityX > 0) velocityX = 0;
			player.velX -= 0.1f;
			velocityX-=0.01f;
		}
		
		if (isTime) {
			float plus = velocityX / 10;
			if (!isSolid(map.getNode((int) Math.floor(player.x+(plus*5f)), (int) Math.floor(player.y))))
			{
				player.velX += velocityX / 10;
				velocityX -= velocityX / 100;
			}
		}
		    
		if (!isSolid(map.getNode((int) Math.floor(player.velX), (int) Math.floor(player.y)) )) {
			boolean t = movePlayer((player.velX - player.x) / 100, 0);
			
			//player.x += (player.velX * 10) / 1000.0f;
			if (t) player.velX -= (player.velX - player.x) / 10;
		}
		boolean isOnGround = isSolid(map.getNode((int) Math.floor(player.x), (int) Math.floor(player.y - 0.1f)));
		if (!isSolid(map.getNode((int) Math.floor(player.x), (int) Math.floor(player.velY))) &&
				!isSolid(map.getNode((int) Math.floor(player.x+0.3f), (int) Math.floor(player.velY))) &&
				!isSolid(map.getNode((int) Math.floor(player.x-0.3f), (int) Math.floor(player.velY)))) {
			if (!isOnGround && isTime2) {
				player.velY -= 0.01f;
				if (player.velY < player.y) {
					player.velY -= 0.001f;
				}
			} else if (isOnGround || flyMode ){
				if (Keyboard.isKeyDown(Keyboard.KEY_UP) && isTime3) {
					player.velY += 0.7f;
				} else if (isTime3){
					player.velY = player.y;
				}
			}
			int delay = 10000;
			if (player.velY < player.y && !isOnGround) {
				delay = 500;
			}
			movePlayer(0, (player.velY - player.y) / delay);
			//player.y += (player.velY - player.y) / delay;
			
		} else if (map.getNode((int) Math.floor(player.x), (int) Math.floor(player.velY)) == Node.STONE){
			player.velY = player.y;
		}
		
		if (map.getNode((int) Math.floor(player.x), (int) Math.floor(player.y)) == Node.POINT) {
			addPoint();
			map.setNode((int) Math.floor(player.x), (int) Math.floor(player.y), Node.EMPTY);
		}
		
		if (map.getNode((int) Math.floor(player.x), (int) Math.floor(player.y)) == Node.FLYPOINT) {
			flyMode = true;
			map.setNode((int) Math.floor(player.x), (int) Math.floor(player.y), Node.EMPTY);
		}
		
		if (map.getNode((int) Math.floor(player.x), (int) Math.floor(player.y)) == Node.FIRE) {
			doGameOver();
		}
		
	}
	
	private boolean movePlayer(float x, float y) {
		float pXleft = player.x-0.25f; //16 32
		float pYdown = player.y+0.5f;
		float pXright = player.x + 0.25f;
		float pYup = player.y + 1f;
		boolean flag = false;
		
		if (map.getNode((int) Math.floor(pXright), (int)player.y) == Node.STONE && (x > 0)) {
			flag = true;
		}
		if (map.getNode((int) Math.floor(pXleft), (int) player.y) == Node.STONE && (x < 0)){
			flag = true;
		}
		
		if (!flag) {
			player.x += x;
		} else {
			velocityX = 0;
			player.velX = player.x;
			//System.out.println("Collision at " + (int) Math.floor(pX) + ", " + (int) Math.floor(pY));
			//System.out.println("Collision at " + (int) Math.floor(pX) + ", " + (int) Math.floor(pY));
		}
		player.y += y;
		return !flag;
	}

	private void goToNextLevel() {
		level++;
		initMap(level);
		velocityX = 0;
		player.setPosition(4, level+2);
		maxScore = maxScore+1;
		score = 0;
		won = false;
		gameOver = false;
	}

	private void restart() {
		initMap(level);
		velocityX = 0;
		player.setPosition(4, level+2);
		score = 0;
		gameOver = false;
		won = false;
	}

	private void addPoint() {
		score++;
		if (score == maxScore) won  = true;
	}

	private void doGameOver() {
		gameOver = true;
		
	}

	private boolean checkIsPlayerDead() {
		if (player.y < 0) return true;
		return false;
		
	}

	private boolean isSolid(int node) {
		return node == Node.STONE;
	}

	public void onRender() {
		GL11.glLoadIdentity();
		
		GL11.glPushMatrix();
		renderMap();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		renderEntities();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		renderPlayer();
		GL11.glPopMatrix();
		
		renderScore();
	}

	private void renderScore() {
		if (won || gameOver || start) {
			if (System.currentTimeMillis() % 500 == 0) {
				tick = !tick;
			}
		}
		if (start) {
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GL11.glPushMatrix();
			
			GL11.glColor4f(0, 0, 1, 0.7f);
			GL11.glBegin(6); {
				
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(512, 0);
				GL11.glVertex2f(512, 512);
				GL11.glVertex2f(0, 512);
				
			} GL11.glEnd();
			
			GL11.glPopMatrix();
			
			GL11.glDisable(GL11.GL_BLEND);
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			//FontRenderer.sRenderWithColor(256-32*4, 256-48, "-------------------", 1, 1, 1);
			FontRenderer.sRenderBigWithColor(256-32*4, 256-32, "Alphastorm", 1, 1, 1);
			FontRenderer.sRenderWithColor(256-32*4, 256, "Press space to start", 1, 1, 1);
			if (tick) {
				FontRenderer.sRenderWithColor(256-16*6, 500-32, " Press space", 1, 1, 1);
			}
		} else if (gameOver) {
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GL11.glPushMatrix();
			
			GL11.glColor4f(1, 0, 0, 0.7f);
			GL11.glBegin(6); {
				
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(512, 0);
				GL11.glVertex2f(512, 512);
				GL11.glVertex2f(0, 512);
				
			} GL11.glEnd();
			
			GL11.glPopMatrix();
			
			GL11.glDisable(GL11.GL_BLEND);
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			//FontRenderer.sRenderWithColor(256-32*4, 256-48, "-Sorry, but this is-", 1, 1, 1);
			FontRenderer.sRenderBigWithColor(256-32*4, 256-32, "Game Over", 1, 1, 1);
			FontRenderer.sRenderWithColor(256-30*5, 256, "Your score: " + score + "/" + maxScore + " Level " + level, 1, 1, 1);
			if (tick) {
				FontRenderer.sRenderWithColor(256-16*6, 500-32, "Press space", 1, 1, 1);
			}
		} else if (won) {
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GL11.glPushMatrix();
			
			GL11.glColor4f(0, 1, 0, 0.7f);
			GL11.glBegin(6); {
				
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(512, 0);
				GL11.glVertex2f(512, 512);
				GL11.glVertex2f(0, 512);
				
			} GL11.glEnd();
			
			GL11.glPopMatrix();
			
			GL11.glDisable(GL11.GL_BLEND);
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			//FontRenderer.sRenderWithColor(256-32*4, 256-48, "-Sorry, but this is-", 1, 1, 1);
			FontRenderer.sRenderBigWithColor(256-32*7, 256-32, "Level complete", 1, 1, 1);
			FontRenderer.sRenderWithColor(256-32*7, 256, "Welocome to level " + (level+1), 1, 1, 1);
			if (tick) {
				FontRenderer.sRenderWithColor(256-16*6, 500-32, " Press space", 1, 1, 1);
			}
		} else {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			FontRenderer.sRenderWithColor(5, 5, "Score: " + score + "  Level: " + level, 1, 1, 1);
		}
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	private void renderPlayer() {
		
		float factorX = (256*2)/32;
		float factorXb = factorX/2;
		float factorY = (256*2)/map.height;
		float factorYb = factorY/2;
		
		int y = (int) (player.y*factorY);
		
		y = -y;
		y += 256*2;
		y-=factorY;
		
		y+= factorYb;
		
		int x = 256; //(int) ((256-16) - ((256*2)/16 % player.x));
		
		GL11.glColor3f(1, 0, 0);
		GL11.glBegin(6); {
			
			GL11.glVertex2f(x-factorXb, y);
			GL11.glVertex2f(x+factorXb, y);
			GL11.glVertex2f(x+factorXb, y+factorYb);
			GL11.glVertex2f(x-factorXb, y+factorYb);
			
		} GL11.glEnd();
		
	}

	private void renderEntities() {
		/*for (Entity e : entities) {
			
		}*/
		
	}

	private void renderMap() {
		for (int x = (int)(player.x - 16); x < player.x + 16; x+=1){
			for (int y = 0; y < map.height; y++){
				int t = map.getNode((int)(x-8), y);
				if (t == Node.EMPTY) renderNode( (x - player.x), y, Color.BLUE);
				if (t == Node.STONE) renderNode( (x - player.x), y, Color.GRAY);
				if (t == Node.POINT) renderNode( (x - player.x), y, Color.YELLOW);
				if (t == Node.FLYPOINT) renderNode( (x - player.x), y, Color.CYAN);
				if (t == Node.FIRE) renderNode( (x - player.x), y, Color.ORANGE);
			}
		}
		
	}

	private void renderNode(float x, float y, Color c) {
		//GL11.glTranslatef(-x, -y, 0);
		float factorX = (256*2)/16;
		float factorY = (256*2)/map.height;
		y*=factorY;
		x*=factorX;
		y = -y;
		y += 256*2;
		y-=factorY;
		GL11.glColor3f((float)c.getRed()/255, (float)c.getGreen()/255, (float)c.getBlue()/255);
		
		GL11.glPushMatrix();
		
		GL11.glBegin(6); {
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x+factorX, y);
			GL11.glVertex2f(x+factorX, y+factorY);
			GL11.glVertex2f(x, y+factorY);
			
		} GL11.glEnd();
		
		GL11.glPopMatrix();
		
		//GL11.glTranslatef(x, y, 0);
		
	}
	
}
