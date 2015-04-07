package cubegame;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.loader.WavefrontObject;

/**
 * Entity with ground collision check
 * 
 * @see Entity
 * @see Human
 * @see cubegame.npc.NPC
 * @author IH, edited from the original code of Kevin Glass
 *
 */
public class Creature implements Entity{

	/** The texture to apply to the model */
	private Texture texture;
	/** The model to be displayed for the player */
	private WavefrontObject model;
	
	/** The X component of the forward vector - used to place shots */
	private float forwardX = 0;
	/** The Y component of the forward vector - used to place shots */
	private float forwardZ = 1;
	
	/** The timeout that counts down until the player can shoot again */
	private int shotTimeout;
	/** The interval in milliseconds between player shots */
	private int shotInterval = 300;
	
	public Creature(float x, float y, float z, float size, WavefrontObject model) {
		this.positionX = x;
		this.positionY = y;
		this.positionZ = z;
		this.moveX = x;
		this.moveZ = z;
		this.size = size;
		this.model = model;
	}

	protected float rotationY;
	protected float velocityX;
	protected float velocityY;
	protected float velocityZ;
	protected float positionX;
	protected float positionZ;
	protected float positionY;
	protected float forwardY;
	protected boolean canWalkZP;
	protected boolean canWalkZN;
	protected boolean canWalkYN;
	protected boolean canWalkXN;
	protected boolean canWalkYP;
	protected boolean canWalkXP;
	protected float prevY;
	protected float prevX;
	protected float prevZ;
	protected float pitch;
	protected ArrayList<Entity> entities;
	protected int delta;
	protected long lastLoop;
	protected int jumpTimeout;
	protected float size;
	protected float moveX;
	protected float moveZ;

	public void collide(EntityManager manager, Entity other) {
		
	}

	@Override
	public boolean collides(Entity other) {
		// TODO Auto-generated method stub
		if (other.getX() < positionX+getSize() && other.getX() > positionX-getSize() && 
				other.getY() < positionY+getSize() && other.getY() > positionY-getSize() && 
				other.getZ() < positionZ+getSize() && other.getZ() > positionZ-getSize()) {
			/*if (other instanceof Cube){
				Cube cube = (Cube) other;
				if (getZ() < -cube.getZ()+1 && getZ() > -cube.getZ()-1) setPositionZ(getPrevZ());
				if (getY() < -cube.getY()+1 && getY() > -cube.getY()-1) setPositionY(getPrevY());
				if (getX() < -cube.getX()+1 && getX() > -cube.getX()-1) setPositionX(getPrevX());
			}*/
			return true;
		}
		
		
		return false;
	}

	@Override
	public float getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return positionX;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return positionY;
	}

	@Override
	public float getZ() {
		// TODO Auto-generated method stub
		return positionZ;
	}

	public void render() {

		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glPushMatrix();

		GL11.glTranslatef(getPositionX(),getPositionY(),positionZ);

		GL11.glRotatef(getRotationY(),0,1,0);
		

		//GL11.glRotatef(90,1,0,0);

		GL11.glScalef(0.01f,0.01f,0.01f);
		
		GL11.glColor3f(1, 1, 1);
		
		model.render();

		GL11.glPopMatrix();
		
	}

	@Override
	public void moveUpdate(EntityManager manager, int delta) {
		forwardX = (float) Math.sin(Math.toRadians(getRotationY()));
		forwardZ = (float) -Math.cos(Math.toRadians(getRotationY()));
		
		setPositionX(getPositionX() + ((getVelocityX() * delta) / 1000.0f));
		setPositionY(getPositionY() + ((getVelocityY() * delta) / 1000.0f));
		positionZ += (getVelocityZ() * delta) / 1000.0f;
		
		if(!checkIsOnGround())velocityY -= 0.005;
		
		move((CubeState)manager, delta);
		
	}
	
	private void move(CubeState manager, int delta) {
		BoundingBox bb = new BoundingBox(positionX, positionY, positionZ, 1.4f, false);
		ArrayList<BoundingBox> b = manager.canMove(-moveX, -(positionY + ((getVelocityY() * delta) / 750.0f)), -moveZ, 1.4f, false);
		for (BoundingBox cube : b) {

		if (cube != null){
			if(positionX < prevX) canWalkXN = false; else canWalkXP = false;
			if(positionY < prevY);
			if(positionZ < prevZ) canWalkZN = false; else canWalkZP = false;
			if (cube.getXOffset(bb) != 0) {
				if(-cube.getX() < positionX) positionX += cube.getXOffset(bb); else positionX -= cube.getXOffset(bb);
			} else {
				setPositionX(moveX);
			}
			if(-cube.getY() < positionY && velocityY < 0) {
				positionY += 0.001f;
			} else if (manager.isSolidCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ())){
				if (!manager.isSolidCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()) 
						&& !manager.isSolidCube((int)cube.getX(), (int)cube.getY()+2, (int)cube.getZ()) 
						&& !manager.isSolidCube((int)cube.getX(), (int)cube.getY()+3, (int)cube.getZ()) &&
						!manager.isSolidCube((int)cube.getX(), (int)cube.getY()+4, (int)cube.getZ())) {
					positionY -= 0.5f;
					setPositionY(getPositionY() + ((getVelocityY() * delta) / 750.0f));
				}
			}
			if (cube.getZOffset(bb) != 0) {
				if(-cube.getZ() < positionZ) positionZ += cube.getZOffset(bb); else positionZ -= cube.getZOffset(bb);
			} else {
				setPositionZ(moveZ);
			}
		} 
		}
		if (b.isEmpty()) {
			{
				if(positionX < prevX) canWalkXN = true; else canWalkXP = true;
				if(positionY < prevY);
				if(positionZ < prevZ) canWalkZN = true; else canWalkZP = true;
				setPositionX(moveX);
				setPositionZ(moveZ);
				setPositionY(getPositionY() + ((getVelocityY() * delta) / 750.0f));
			}
		}
		moveX = positionX;
		moveZ = positionZ;
	}

	public float getForwardZ() {
		return forwardZ;
	}

	public void setForwardZ(float forwardZ) {
		this.forwardZ = forwardZ;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setPositionY(float positionY) {
		setPrevY(this.positionY);
		this.positionY = positionY;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionX(float positionX) {
		setPrevX(this.positionX);
		this.positionX = positionX;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionZ(float positionZ) {
		setPrevZ(this.positionZ);
		this.positionZ = positionZ;
	}
	
	public float getPositionZ() {
		return positionZ;
	}
	
	public void setVelocityZ(float velocityZ) {
		this.velocityZ = velocityZ;
	}

	public float getVelocityZ() {
		return velocityZ;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	public float getVelocityY() {
		return velocityY;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	public float getVelocityX() {
		return velocityX;
	}

	public void setPrevZ(float prevZ) {
		this.prevZ = prevZ;
	}

	public float getPrevZ() {
		return prevZ;
	}

	public void setPrevX(float prevX) {
		this.prevX = prevX;
	}

	public float getPrevX() {
		return prevX;
	}

	public void setPrevY(float prevY) {
		this.prevY = prevY;
	}

	public float getPrevY() {
		return prevY;
	}
	
	private float toDec(float f1){
		DecimalFormat form = new DecimalFormat("0.0");
		String rawFloat = form.format(f1);
		rawFloat = rawFloat.replaceAll(",", ".");
		float answer = Float.parseFloat(rawFloat);
		return answer;
	}
	
	private boolean checkIsOnGround(){
		if (entities != null){
			for (int i = 0; i < entities.size(); i++){
				if ((int)positionX == (int)entities.get(i).getX() && 
						(int)(positionY-2) == (int)entities.get(i).getY() && 
						(int)positionZ == (int)entities.get(i).getZ() && (entities.get(i) instanceof Cube)){
					GameWindow.configUIMngr();
					velocityY = 0;
					return true;
				}
			}
		}
		return false;
	}

	public void setMoveX(float moveX) {
		this.moveX = moveX;
	}

	public float getMoveX() {
		return moveX;
	}

	public void setMoveZ(float moveZ) {
		this.moveZ = moveZ;
	}

	public float getMoveZ() {
		return moveZ;
	}

}
