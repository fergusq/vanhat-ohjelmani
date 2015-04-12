package cubegame.npc;

import java.io.Serializable;
import java.util.ArrayList;

import com.loader.WavefrontObject;

import cubegame.BoundingBox;
import cubegame.Creature;
import cubegame.Cube;
import cubegame.CubeState;
import cubegame.EntityManager;
import cubegame.npc.ai.AI;

public class NPC extends Creature {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8977606631229951396L;
	protected int healt;
	protected int height;
	
	protected AI ai;
	private boolean onGround;
	private boolean isDeath;
	
	public NPC(float x, float y, float z, float size) {
		super(x, y, z, size, null);
		ai = AI.getAI(AI.PASSIVE_AI, this);
		height = 1;
		healt = 8;
		isDeath = false;
	}
	
	/**
	 * 
	 * @return If creature is dead
	 */
	public boolean hurt(EntityManager m){
		ai.hurt();
		healt--;
		if (healt == 1) ai = AI.getAI(AI.DEAD_AI, this);
		isDeath = healt == 0;
		if (isDeath) m.removeEntity(this);
		return isDeath;
	}
	
	@Override
	public void moveUpdate(EntityManager manager, int delta) {
		entities = null;
		
		ai.update();
		
		setPositionX(getPositionX() + (getVelocityX() * delta) / 1000.0f);
		setPositionY(getPositionY() + ((getVelocityY() * delta) / 1000.0f));
		setPositionZ(getPositionZ() + (getVelocityZ() * delta) / 1000.0f);
		
		//if(!checkIsOnGround()) velocityY -= 0.005;
		
		move((CubeState)manager, delta);
		
		jumpTimeout -= delta;
		if (jumpTimeout <= 0) {
			if (ai.isJumping() && checkIsOnGround()) {
				jump(-1.1f);
				jumpTimeout = 1000;
			}
		}
		
	}

	public void jump(float up){
		velocityY = up;
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
	
	public boolean checkIsOnGround(){
		if (this.prevX == positionX && this.prevY == positionY && this.prevZ == positionZ && onGround) return true;
		if (entities != null){
			for (int i = 0; i < entities.size(); i++){
				if ((int)positionX == (int)entities.get(i).getX() && 
						Math.ceil(positionY-2) == Math.ceil(entities.get(i).getY()) && 
						(int)positionZ == (int)entities.get(i).getZ() && (entities.get(i) instanceof Cube)){
					velocityY = 0;
					//System.out.println("++" + positionY + "++");
					onGround = true;
					return true;
				}
			}
		}
		//System.err.println("!!" + positionY + "!!");
		onGround = false;
		return false;
	}

	public int getHealt() {
		return healt;
	}

	public void setHealt(int healt) {
		this.healt = healt;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public AI getAi() {
		return ai;
	}

	public void setAi(AI ai) {
		this.ai = ai;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
}
