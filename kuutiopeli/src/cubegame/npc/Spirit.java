package cubegame.npc;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.loader.WavefrontObject;

import cubegame.BoundingBox;
import cubegame.Creature;
import cubegame.Cube;
import cubegame.CubeState;
import cubegame.CubeType;
import cubegame.Entity;
import cubegame.EntityManager;
import cubegame.GameWindow;
import cubegame.Texture;
import cubegame.TextureLoader;
import cubegame.npc.NPC;
import cubegame.npc.model.HumanoidModel;
import cubegame.particle.ParticleSystem;
import cubegame.physics.PhysicCube;
import cubegame.save.CubeMapFileReader;
import cubegame.sound.Sound;
import cubegame.sound.SoundLoader;

/**
 * The entity representing the player. This entity is responsible for
 * displaying a model, a particle system for the player's engine and
 * for creating shot entities based on player input.
 * 
 * @author Kevin Glass
 */
public class Spirit extends NPC implements Entity{
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
	
	/** The texture applied to the particles that build up the player's shots */
	private Texture shotTexture;
	/** The particle engine used to produce the ship's engine effect */
	private ParticleSystem engine;
	private float rotationY;
	private float velocityX;
	private float velocityY;
	private float velocityZ;
	private float positionX;
	private float positionZ;
	private float positionY;
	private float forwardY;
	private boolean canWalkZP;
	private boolean canWalkZN;
	private boolean canWalkYN;
	private boolean canWalkXN;
	private boolean canWalkYP;
	private boolean canWalkXP;
	private float prevY;
	private float prevX;
	private float prevZ;
	private float pitch;
	private Texture frameTexture;
	private int removeTimeout;
	private int createTimeout;
	private int clickInterval = 300;
	private int delta;
	private long lastLoop;
	private int jumpTimeout;
	public CubeType cubeType = CubeType.grass;
	private int changeUpTimeout;
	private int changeDownTimeout;
	private float leftX;
	private float leftZ;
	private float rightZ;
	private float rightX;
	private float backwardZ;
	private float backwardY;
	private float backwardX;
	private float moveX;
	private float moveZ;
	private Vector3f spawnPoint;
	
	private Sound normalStep;
	private Sound woodStep;
	private Sound stoneStep;
	private Sound gravelStep;
	private Texture createFrameTexture;
	private int changeViewTimeout;
	private boolean viewFrames;
	private final String id;
	private boolean fallDamage = true;
	private BoundingBox bb;
	private int changeCameraTimeout;
	boolean firstPerson = true;
	private Texture bloodTexture;
	private int life = 10000;
	public int legRot;
	private boolean legRotBack;
	
	/**
	 * Create a new NPC-entity
	 * 
	 * @param texture The texture to apply to the player's model
	 * @param spawnPoint Where entity spawn
	 */
	public Spirit(Texture texture, Vector3f spawnPoint) {
		super(spawnPoint.x, spawnPoint.y, spawnPoint.z, 0.4f);
		
		this.texture = texture;
		
		this.spawnPoint = spawnPoint;
		
		this.positionX = spawnPoint.x;
		this.positionY = spawnPoint.y;
		this.positionZ = spawnPoint.z;
		this.moveX = spawnPoint.x;
		this.moveZ = spawnPoint.z;
		
		TextureLoader loader = new TextureLoader();
		
		try {
			frameTexture = loader.getTexture("res/frame.png");
			createFrameTexture = loader.getTexture("res/createframe.png");
			this.shotTexture = loader.getTexture("res/shot.png");
			this.bloodTexture = loader.getTexture("res/blood.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		
		engine = new ParticleSystem();
		
		try {
			normalStep = SoundLoader.get().getOgg("res/sounds/steps/normalStep.ogg");
			woodStep = SoundLoader.get().getOgg("res/sounds/steps/woodStep3.ogg");
			stoneStep = SoundLoader.get().getOgg("res/sounds/steps/stoneStep1.ogg");
			gravelStep = SoundLoader.get().getOgg("res/sounds/steps/gravelStep1.ogg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bb = new BoundingBox(new Creature(-positionX, -positionY, -positionZ, 0.4f, null), 1.5f ,false);
		
		id = CubeMapFileReader.getFreeID();
	}
	
	/**
	 * @see Entity#moveUpdate(EntityManager, int)
	 */
	public void moveUpdate(EntityManager manager, int delta) {
		// if the player is pushing left or right then rotate the

		// ship. Note that the amount rotated is scaled by delta, the 

		// amount of time that has passed. This means that rotation

		// stays framerate independent

		boolean onGround = checkIsOnGround((CubeState) manager);
		
		this.delta = (int) (GameWindow.getTime() - lastLoop);
		lastLoop = GameWindow.getTime();

		// recalculate the forward vector based on the current

		// ship rotation

		forwardX = (float) ((float) 0.009f * -Math.sin(Math.toRadians(getRotationY())));
		forwardY = (float) ((float) 0.009f * Math.sin(Math.toRadians(getRotationY())));
		forwardZ = (float) ((float) 0.009f * -Math.cos(Math.toRadians(getRotationY())));
		
		backwardX = (float) ((float) 0.009f * Math.sin(Math.toRadians(getRotationY())));
		backwardY = (float) ((float) 0.009f * -Math.sin(Math.toRadians(getRotationY())));
		backwardZ = (float) ((float) 0.009f * -Math.cos(Math.toRadians(getRotationY())));
		
		leftX = (float) ((float) 0.009f * Math.sin(Math.toRadians(getRotationY()-90)));
		leftZ = (float) ((float) 0.009f * -Math.cos(Math.toRadians(getRotationY()-90)));
		rightX = (float) ((float) 0.009f * Math.sin(Math.toRadians(getRotationY()+90)));
		rightZ = (float) ((float) 0.009f * -Math.cos(Math.toRadians(getRotationY()+90)));

		boolean moving = false;
		
		ai.update();
		
		float slow = ((CubeState)manager).getLiquidDensity(-positionX, -positionY, -positionZ, 2);
		
		moving = prevX != positionX && prevY != positionY && prevZ != positionZ;
		
		if (moving){
			 if (Sys.getTime() % 250 == 0 && checkIsOnGround((CubeState) manager))getStep().play(1.0f, 1.0f);
			 
			 bb.move(-(positionX-prevX), -(positionY-prevY), -(positionZ-prevZ));
		}
		
		jumpTimeout -= delta;
		if (jumpTimeout <= 0) {
			if (ai.isJumping() && onGround) {
				jump(-0.4f);
				velocityY = -0.4f;
				jumpTimeout = 5000;
			}
		}
		
		//GLU.gluLookAt(positionX, positionY, 0.6f, positionX+(getForwardX()*1.1f), positionY+(forwardY*1.1f), 
		//			   0.6f, positionX, positionY, 1);

		update(manager, delta);
	}
	
	public void update(EntityManager manager, int delta){
		bb.move(-(positionX-prevX), -(positionY-prevY), -(positionZ-prevZ));
		
		engine.x = -positionX;
		engine.y = -(positionY-0.3f);
		engine.z = -positionZ;
		engine.rotX = -((CubeState)manager).player[((CubeState)manager).cam].getPitch();
		engine.rotY = -((CubeState)manager).player[((CubeState)manager).cam].rotationY;
		
		life--;
		if (life < 0) hurt(manager);
		
		setPositionX(getPositionX() + ((getVelocityX() * delta) / 1000.0f));
		
		positionZ += (getVelocityZ() * delta) / 1000.0f;
		
		move((CubeState) manager, delta);
		
		if(!checkIsOnGround((CubeState) manager)){
			velocityY += 0.001;
		}
	}
	
	private Sound getStep() {
		switch (getGroundCube()){
		case Cube.DIRT:
			return normalStep;
		case Cube.SNOW:
			return gravelStep;
		case Cube.STONE_1:
			return stoneStep;
		case Cube.STONE_2:
			return stoneStep;
		case Cube.WOOD:
			return woodStep;
		default:
			return normalStep;	
		}
	}

	private int getGroundCube() {
		/*if (entities != null){
			for (int i = 0; i < entities.size(); i++){
				if ((int)-positionX == (int)entities.get(i).getX() && 
						(int)(-positionY-3.1f) == (int)entities.get(i).getY() && 
						(int)-positionZ == (int)entities.get(i).getZ() && (entities.get(i) instanceof Cube)){
					return ((Cube)entities.get(i)).getType().getId();
				}
			}
		}*/
		return 1;
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

	public float getMoveX() {
		return moveX;
	}

	public void setMoveX(float moveX) {
		this.moveX = moveX;
	}

	public float getMoveZ() {
		return moveZ;
	}

	public void setMoveZ(float moveZ) {
		this.moveZ = moveZ;
	}

	public boolean isCanWalkZP() {
		return canWalkZP;
	}

	public void setCanWalkZP(boolean canWalkZP) {
		this.canWalkZP = canWalkZP;
	}

	public boolean isCanWalkZN() {
		return canWalkZN;
	}

	public void setCanWalkZN(boolean canWalkZN) {
		this.canWalkZN = canWalkZN;
	}

	public boolean isCanWalkYN() {
		return canWalkYN;
	}

	public void setCanWalkYN(boolean canWalkYN) {
		this.canWalkYN = canWalkYN;
	}

	public boolean isCanWalkXN() {
		return canWalkXN;
	}

	public void setCanWalkXN(boolean canWalkXN) {
		this.canWalkXN = canWalkXN;
	}

	public boolean isCanWalkYP() {
		return canWalkYP;
	}

	public void setCanWalkYP(boolean canWalkYP) {
		this.canWalkYP = canWalkYP;
	}

	public boolean isCanWalkXP() {
		return canWalkXP;
	}

	public void setCanWalkXP(boolean canWalkXP) {
		this.canWalkXP = canWalkXP;
	}

	public void setForwardY(float forwardY) {
		this.forwardY = forwardY;
	}
	
	/**
	 * @see Entity#render()
	 */
	public void render() {
		
		 if (legRot > 80) legRotBack = true;
			if (legRot < -80) legRotBack = false;
			
			if (legRotBack) legRot -= 20;
			else legRot += 20;
		
		if (healt <= 1) legRot = 0;
		
		
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		if (healt > 4 && life > 200) {
			shotTexture.bind();
			//engine.draw(shotTexture);
		} else{
			bloodTexture.bind();
			engine.draw(bloodTexture);
		}
		
		
		GL11.glDisable(GL11.GL_BLEND);
	}

	
	/**
	 * @see Entity#getSize()
	 */
	public float getSize() {
		// the size of the player

		return 2;
	}

	/**
	 * @see Entity#collide(EntityManager, Entity)
	 */
	public void collide(EntityManager manager, Entity other) {
		
	}
	/**
	 * Translates and rotate the matrix so that it looks through the camera
     * this dose basic what gluLookAt() does.
     * 
     * @param dy Vertical movement of the camera
     */
    public void lookThrough(float dy)
    {
    	
    	setPitch(dy * 0.005f);
        //roatate the pitch around the X axis
        GL11.glRotatef(getPitch(), 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        GL11.glRotatef(rotationY, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location

        GL11.glTranslatef(positionX, positionY, positionZ);

    }
	
	public void setForwardX(float forwardX) {
		this.forwardX = forwardX;
	}

	public float getForwardX() {
		return forwardX;
	}

	public float getForwardY() {
		return forwardY;
	}

	@Override
	public float getZ() {
		return positionZ;
	}

	@Override
	public boolean collides(Entity other) {
		return false;
		
	}

	@Override
	public float getX() {
		return getPositionX();
	}

	@Override
	public float getY() {
		return positionY;
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
		if (true){
		boolean flag = true;
		/*for (int i = 0; i < entities.size(); i++){
			if ((int)-positionX == (int)entities.get(i).getX() && 
					(int)-positionY-1 == (int)entities.get(i).getY() && 
					(int)-positionZ == (int)entities.get(i).getZ() && !(entities.get(i) instanceof Human)){
				flag = false;
			}
			}*/
		if (flag)setPrevY((int)this.positionY);
		if (flag)this.positionY = positionY;
		}
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionX(float positionX) {
		//if (entities != null){
			boolean flag = true;
			/*for (int i = 0; i < entities.size(); i++){
				if ((int)-positionX == (int)entities.get(i).getX() && 
						(int)-positionY-1 == (int)entities.get(i).getY() && 
						(int)-positionZ == (int)entities.get(i).getZ() && !(entities.get(i) instanceof Human)){
					flag = false;
				}
				}*/
		if (flag)setPrevX(toDec(this.positionX));
		if (flag)this.positionX = positionX;//}
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionZ(float positionZ) {
		if (true){
			boolean flag = true;
			/*for (int i = 0; i < entities.size(); i++){
				if ((int)-positionX == (int)entities.get(i).getX() && 
						(int)-positionY-1 == (int)entities.get(i).getY() && 
						(int)-positionZ == (int)entities.get(i).getZ() && !(entities.get(i) instanceof Human)){
					flag = false;
				}
				}*/
		if(flag)setPrevZ(toDec(this.positionZ));
		if(flag)this.positionZ = positionZ;}
	}
	
	public float getPositionZ() {
		return positionZ;
	}
	
	public void setPosition(float positionX, float positionZ) {
		if (true){
			boolean flag = true;
			/*for (int i = 0; i < entities.size(); i++){
				if ((int)-positionX == (int)entities.get(i).getX() && 
						(int)-positionY-1 == (int)entities.get(i).getY() && 
						(int)-positionZ == (int)entities.get(i).getZ() && !(entities.get(i) instanceof Human)){
					flag = false;
				}
			}*/	
		if(flag)setPrevZ(toDec(this.positionZ));
		if(flag)this.positionZ = positionZ;
		if(flag)setPrevX(toDec(this.positionX));
		if(flag)this.positionX = positionX;
		}
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
	
	private boolean checkIsOnGround(CubeState manager) {
		
		// if (entities == null) {
		for (int xp = (int) (Math.floor(-positionX) - 4); xp < Math.ceil(-positionX) + 4; xp++) {
			for (int yp = (int) (-getPositionY() - 3.5f); yp < -getPositionY() + 4; yp++) {
				for (int zp = (int) (Math.floor(-positionZ)) - 4; zp < Math.ceil(-positionZ) + 4; zp++) {
					PhysicCube c = new PhysicCube(xp, yp, zp, ((CubeState) manager).cubeMap
							.getCubeType(xp, yp, zp), 1);

					if (c == null) continue;
					if (c.getType() == CubeType.air) continue;
					
					boolean flag = checkIsOnGroundViaBoundingBox(c);
					
					if (flag) {

						if (c.getType().isLiquid()) {

							if (jumpTimeout > 1000 || jumpTimeout < 0)
								velocityY = 0.3f;
						} else if (c.getType().isSolid()){
							/*
							 * for (int x = 0; x < velocityY / 1.5f; i++){
							 * 
							 * }
							 */
							//positionY -= 0.001f;
							if (fallDamage ){
								if (velocityY > 2.0f)
									hurt(manager);
								if (velocityY > 3.0f)
									hurt(manager);
								if (velocityY > 4.0f)
									hurt(manager);
							}
							
							if (jumpTimeout > 1000 || jumpTimeout < 0)
								velocityY = 0;
							
						
							return true;
						}

					}
				}
			}
		}
		// }
		return false;
	}

	private boolean checkIsOnGroundViaBoundingBox(PhysicCube c) {
		BoundingBox cubebb = new BoundingBox(c, 1, true);
		
		Creature ent = new Creature(-positionX, -positionY, -positionZ, 0.4f, null);
		bb = new BoundingBox(ent, 1.6f, false);
		double y = (float) cubebb.getYOffset(bb);
		
		return y > -0.5 && y < 0;
	}

	public String getID() {
		// TODO Auto-generated method stub
		return id;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getPitch() {
		return pitch;
	}
}
