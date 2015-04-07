package cubegame;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.loader.WavefrontObject;

import cubegame.inventory.HotbarInventory;
import cubegame.inventory.ItemStack;
import cubegame.item.BlockItem;
import cubegame.item.Item;
import cubegame.item.WandItem;
import cubegame.npc.NPC;
import cubegame.npc.model.HumanoidModel;
import cubegame.particle.ParticleSystem;
import cubegame.physics.PhysicCube;
import cubegame.render.CubeRenderer;
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
public class Human implements Entity {
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
	public float rotationY;
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
	public int selectedIndex = 0;
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
	private BoundingBox selectbb;
	private boolean legRotBack;
	public int legRot;
	int item;
	private HotbarInventory inventory;
	private boolean onGround;
	private int digTimer;
	public int health = 4;
	public int spell;
	public int spellId;
	private int changeDownTimeout2;
	private int changeUpTimeout2;
	
	/**
	 * Create a new Player entity
	 * 
	 * @param texture The texture to apply to the player's model
	 * @param model The model to display for the player
	 * @param shotTexture The texture to apply to the shot's created when
	 * the player fires
	 */
	public Human(Texture texture, WavefrontObject model, Vector3f spawnPoint) {
		this.texture = texture;
		this.model = model;
		
		this.spawnPoint = spawnPoint;
		
		this.positionX = spawnPoint.x;
		this.positionY = spawnPoint.y;
		this.positionZ = spawnPoint.z;
		this.moveX = spawnPoint.x;
		this.moveZ = spawnPoint.z;
		
		
		inventory = new HotbarInventory();
		this.inventory.setItem(0, Item.pickaxe.shiftedIndex);
		this.inventory.setItem(1, Item.shovel.shiftedIndex);
		this.inventory.setItem(2, Item.axe.shiftedIndex);
		this.inventory.setItem(3, Item.diamondDrill.shiftedIndex);
		this.inventory.setItem(4, Item.firstAid.shiftedIndex);
		this.inventory.setItem(5, Item.wand.shiftedIndex);
		this.inventory.setItem(6, new ItemStack(new BlockItem(Cube.GRASS), 4));
		this.inventory.setItem(7, 0);
		
		TextureLoader loader = new TextureLoader();
		
		try {
			frameTexture = loader.getTexture("res/frame.png");
			createFrameTexture = loader.getTexture("res/createframe.png");
			this.shotTexture = loader.getTexture("res/shot.png");
		} catch (IOException e) {
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

		if (this.selectedIndex == 8) selectedIndex = 0;
		if (this.selectedIndex == -1) selectedIndex = 7;
		
		CubeType cubeType = CubeType.getTypeFromNumber(inventory.getItem(this.selectedIndex).getItem().shiftedIndex);
		ItemStack selected = getSelectedItem();
		
		this.delta = (int) (GameWindow.getTime() - lastLoop);
		lastLoop = GameWindow.getTime();
		
		/*changeUpTimeout -= delta;
		if (changeUpTimeout <= 0){
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				item--;
				changeUpTimeout = 500;
			}
		}
		
		changeDownTimeout -= delta;
		if (changeDownTimeout <= 0){
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				item++;
				changeDownTimeout = 500;
			}
		}*/

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
		
		// count down the timer until a shot can be taken again

		// if the timeout has run out (<= 0) then check if the player

		// wants to fire. If so, fire and then reset the timeout
		
		boolean flag = false;
		boolean ent = false;
		Entity e = null;
		NPC npc = null;
		float[] pos = getDestPos(positionX, positionY, positionZ, getPitch(), 0);
		float[] pos1 = getDestPos(positionX, positionY, positionZ, getPitch(), 0);
		for (float i = 0; i < 5; i += 0.1f){
			pos = getDestPos(positionX, positionY, positionZ, getPitch(), i);
			if ( !((CubeState)manager).isLiquid( (int)pos[0], (int)pos[1], (int)pos[2] ) && ((CubeState)manager).isCube( (int)pos[0], (int)pos[1], (int)pos[2] )) {
				pos1 = getDestPos(positionX, positionY, positionZ, getPitch(), i-0.1);
				flag = true;
				e = new PhysicCube(pos[0], pos[1], pos[2], true);
				ent = false;
				break;
			} else {
				npc = ((CubeState)manager).isNPC(-(int)pos[0], -(int)pos[1], -(int)pos[2]);
				if (npc == null) continue;
				flag = true;
				ent = true;
				break;
				/*NPC npc = ((CubeState)manager).isNPC( -(int)pos[0], -(int)pos[1], -(int)pos[2] );
				if (npc != null){
					e = npc;
					flag = true;
					ent = true;
					break;
				}*/
			}
		}
		
		if (viewFrames && flag){
			//Cube createframes = new Cube(pos1[0], pos1[1], pos1[2], createFrameTexture, true);
			GL11.glPushMatrix();
			
			//GL11.glDisable(GL11.GL_TEXTURE_2D);
			//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			//GL11.glColor3f(0, 0, 0);
            //GL11.glDepthMask(false);
            
			if(!ent && e != null) selectbb = new BoundingBox(e, 1, true);
			else if (npc != null){
				e = new NPC(-npc.getX(), -npc.getY(), -npc.getZ(), 0.4f);
				selectbb = new BoundingBox(e, 1.4f, false);
			}
			//createframes.render();
			
			
			//GL11.glEnable(GL11.GL_TEXTURE_2D);
			//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			
			GL11.glPopMatrix();
		} else selectbb = null;
		
		boolean canDig = false;
		
		if (flag) {
			if (selected.getItem().shiftedIndex < 256 || selected.getStackSize() <= 0) canDig = false;
			else {
				canDig = selected.getItem().onDig((int)pos[0], (int)pos[1], (int)pos[2], ((CubeState)manager).cubeMap, selected);
				inventory.setItem(selectedIndex, selected);
			}
		}
		
		int duration = 4;
		if (!canDig) duration = cubeType.handDuration;
		
		removeTimeout -= delta;
		if (removeTimeout <= 0) {
			if (Mouse.isButtonDown(0)) {
				if (npc != null) npc.hurt(manager);
				else if (flag) {
					digTimer++;
					
					if (digTimer == duration){
						manager.removeEntity(new PhysicCube(pos[0], pos[1], pos[2]));
					}
				}
				//System.out.println("X"+pos[0] + "Y"+pos[1] + "Z"+pos[2]);
				removeTimeout = clickInterval;
			} else {
				digTimer = 0;
			}
		}
		
		createTimeout -= delta;
		if (createTimeout <= 0) {
			if (Mouse.isButtonDown(1)) {
				if (flag && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && selected.getItem().shiftedIndex < 256) {
					//((CubeState)manager).addCube(pos1[0], pos1[1], pos1[2], cubeType);
					selected.getItem().onPlace((int)pos1[0], (int)pos1[1], (int)pos1[2], this, ((CubeState)manager), selected);
					inventory.setItem(selectedIndex, selected);
				} else if (flag && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && selected.getItem().shiftedIndex < 256) {
					((CubeState)manager).addHalfCube(pos1[0], pos1[1], pos1[2], cubeType);
				}
				if (selected.getItem().shiftedIndex > 255 && selected.getStackSize() > 0) {
					selected.getItem().onUse(this);
					if (flag){
						selected.getItem().onPlace((int)pos1[0], (int)pos1[1], (int)pos1[2], this, ((CubeState)manager), selected);
						inventory.setItem(selectedIndex, selected);
					}
				}
				//System.out.println("X"+pos[0] + "Y"+pos[1] + "Z"+pos[2]);
				createTimeout = clickInterval;
			}
		}
		
		if (Mouse.isButtonDown(0)) {legRot = 180;}
		
		// if the player is pushing the thrust key (up) then

		// increse the velocity in the direction we're currently

		// facing

		boolean moving = false;
		
		float slow = ((CubeState)manager).getLiquidDensity(-positionX, -positionY, -positionZ, 2);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
			 setMoveX(positionX - (slow+0.01f) * (float)Math.sin(Math.toRadians(rotationY)));
			 setMoveZ(positionZ + (slow+0.01f) * (float)Math.cos(Math.toRadians(rotationY)));
			 
			 moving = true;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			 setMoveX(positionX - slow * (float)Math.sin(Math.toRadians(rotationY-90)));
			 setMoveZ(positionZ + slow * (float)Math.cos(Math.toRadians(rotationY-90)));
			 
			 moving = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			 setMoveX(positionX - slow * (float)Math.sin(Math.toRadians(rotationY+90)));
			 setMoveZ(positionZ + slow * (float)Math.cos(Math.toRadians(rotationY+90)));
			 
			 moving = true;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
			 setMoveX(positionX + (slow+0.01f) * (float)Math.sin(Math.toRadians(rotationY))); 
			 setMoveZ(positionZ - (slow+0.01f) * (float)Math.cos(Math.toRadians(rotationY)));
			 
			 moving = true;
		}
		
		
		if (moving){
			 if (Sys.getTime() % 250 == 0 && checkIsOnGround((CubeState) manager))getStep().play(1.0f, 1.0f);
			 
			 bb.move(-(positionX-prevX), -(positionY-prevY), -(positionZ-prevZ));
			 
			 if (((CubeState)manager).isMultiplayer)((CubeState)manager).sendMovePacket();
		}
		
		if (slow != 0.009f){
			
		}
		
		/*if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			setPositionY(getPositionY() + (delta / 100.0f));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			setPositionY(getPositionY() - (delta / 100.0f));
		}*/
		
		jumpTimeout -= delta;
		if (jumpTimeout <= 0) {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && onGround) {
				if (slow == 0.009f){
					velocityY = -4.7f;
					jumpTimeout = 1000;
				}
				else {
					velocityY = -0.5f;
				}
				
			}
		}
		
		changeViewTimeout -= delta;
		if (changeViewTimeout <= 0){
		if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
			viewFrames = !viewFrames;
				changeViewTimeout = 500;
			}
		}
		
		changeCameraTimeout -= delta;
		if (changeCameraTimeout <= 0){
		if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			firstPerson  = !firstPerson;
				changeCameraTimeout = 500;
			}
		}
		
		changeUpTimeout -= delta;
		if (changeUpTimeout <= 0){
		if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
			this.selectedIndex = this.selectedIndex + 1;
				changeUpTimeout = 500;
			}
		}
		
		changeDownTimeout -= delta;
		if (changeDownTimeout <= 0){
		if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
			this.selectedIndex = this.selectedIndex - 1;
				changeDownTimeout = 500;
			}
		}
		
		changeUpTimeout2 -= delta;
		if (changeUpTimeout2 <= 0){
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				this.spell = this.spell + 1;
				if (spell >= 9) spell = 0;
				spellId = WandItem.items.getItem(spell).getItem().shiftedIndex;
				changeUpTimeout2 = 500;
			}
		}
		
		changeDownTimeout2 -= delta;
		if (changeDownTimeout2 <= 0){
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				this.spell = this.spell - 1;
				if (spell <= -1) spell = 8;
				spellId = WandItem.items.getItem(spell).getItem().shiftedIndex;
				changeDownTimeout2 = 500;
			}
		}
		
		//GLU.gluLookAt(positionX, positionY, 0.6f, positionX+(getForwardX()*1.1f), positionY+(forwardY*1.1f), 
		//			   0.6f, positionX, positionY, 1);

		if (legRot > 90) legRotBack = true;
		if (legRot < -90) legRotBack = false;
		
		if (legRotBack) legRot -= 1;
		else legRot += 1;
		
	}
	
	public void update(EntityManager manager, int delta){
		engine.x = -positionX;
		engine.y = -positionY;
		engine.z = -positionZ;
		engine.rotX = -((CubeState)manager).player[((CubeState)manager).cam].getPitch();
		engine.rotY = -((CubeState)manager).player[((CubeState)manager).cam].rotationY;
		
		setPositionX(getPositionX() + ((getVelocityX() * delta) / 1000.0f));
		
		positionZ += (getVelocityZ() * delta) / 1000.0f;
		
		
		move((CubeState) manager, delta);

		
		if(!checkIsOnGround((CubeState) manager)){
			onGround = false;
			velocityY += 0.1;
		} else onGround = true;
	}
	
	public ItemStack getSelectedItem()
	{
		return getTypeFromInventory(this.selectedIndex);
	}
	
	public ItemStack getTypeFromInventory(int index) {
		
		return inventory.getItem(index);
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
		
		boolean bool = false;
		
		ArrayList<BoundingBox> b = manager.canMove(-positionX, -positionY, -moveZ, 1.4f, false);
		if (fixCollision(manager, b, false)) bool = true;
		
		b = manager.canMove(-moveX, -positionY, -positionZ, 1.4f, false);
		if (fixCollision(manager, b, false)) bool = true;
		
		b = manager.canMove(-positionX, -(positionY + ((getVelocityY() * delta) / 750.0f)), -positionZ, 1.4f, false);
		if (fixCollision(manager, b, true)) bool = true;
		
		if (bool) {
			b = manager.canMove(-moveX, -(positionY + ((getVelocityY() * delta) / 750.0f)), -moveZ, 1.4f, false);
			fixCollision(manager, b, false);
		}
		//b = manager.canMove(-moveX, -(positionY + ((getVelocityY() * delta) / 750.0f)), -moveZ, 1.4f, false);
		//fixCollision(manager, b);
		
		/*//Mouse.setGrabbed(false);
		
		Vector3f pos = CubeState.createVector(-positionX, -positionY+0.1f, -positionZ);
		Vector3f speed = CubeState.createVector(-(moveX - positionX), -positionY, -(moveZ - positionZ));
		Vector3f bbox = CubeState.createVector(0.5f, 2f, 0.5f);
		
		manager.testCollision(pos, speed, bbox);
		
		moveX = -speed.x;
		moveZ = -speed.z;
		
		positionX = -pos.x;
		//positionY = pos.y;
		positionZ = -pos.z;
		
		positionX += moveX - positionX;
		positionZ += moveZ - positionZ;
		setPositionY(getPositionY() + ((getVelocityY() * delta) / 750.0f));
		*/
		moveX = positionX;
		moveZ = positionZ;
	}

	private boolean fixCollision(CubeState manager, ArrayList<BoundingBox> b, boolean yGravity) {
		boolean stopped = false;
		boolean stoppedX = false;
		boolean stoppedZ = false;
		for (BoundingBox cube : b) {

			if (cube != null){
				if(positionX < prevX) canWalkXN = false; else canWalkXP = false;
				if(positionY < prevY);
				if(positionZ < prevZ) canWalkZN = false; else canWalkZP = false;
				if (cube.getXOffset(bb) > 0) {
					if(-cube.getX() < positionX) positionX += (cube.getXOffset(bb)); else positionX -= (cube.getXOffset(bb));
					stopped = true;
					stoppedX = true;
				} else {
					//setPositionX(moveX);
				}
				if(-cube.getY() < positionY && velocityY < 0) {
					if (yGravity) positionY += 0.001f;
				} else if (manager.isSolidCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ())){
					if (!manager.isSolidCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()) 
							&& !manager.isSolidCube((int)cube.getX(), (int)cube.getY()+2, (int)cube.getZ()) 
							&& !manager.isSolidCube((int)cube.getX(), (int)cube.getY()+3, (int)cube.getZ()) &&
							!manager.isSolidCube((int)cube.getX(), (int)cube.getY()+4, (int)cube.getZ()) && 
							cube.getY() <= -(positionY+1.5f)) {
						positionY -= 0.5f;
						if (yGravity) setPositionY(getPositionY() + ((getVelocityY() * delta) / 750.0f));
					}
				}
				if (cube.getZOffset(bb) > 0) {
					if(-cube.getZ() < positionZ) positionZ += (cube.getZOffset(bb)); else positionZ -= (cube.getZOffset(bb));
					stopped = true;
					stoppedZ = true;
				} else {
					//setPositionZ(moveZ);
				}
			} 
		}
		if (b.isEmpty() && !stopped) {
			{
				if(positionX < prevX) canWalkXN = true; else canWalkXP = true;
				if(positionY < prevY);
				if(positionZ < prevZ) canWalkZN = true; else canWalkZP = true;
				setPositionX(moveX);
				setPositionZ(moveZ);
				if (yGravity) setPositionY(getPositionY() + ((getVelocityY() * delta) / 750.0f));
			}
		} else {
			//if (!stoppedX) setPositionX(moveX);
			//if (!stoppedZ) setPositionZ(moveZ);
		}
		return stopped;
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
	
	public void renderSelectBox(){
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        //GL11.glDisable(GL11.GL_LIGHTING);
        
		if (selectbb != null) CubeRenderer.renderBoundingBox(selectbb);
		
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
	}
	
	/**
	 * @see Entity#render()
	 */
	public void render() {
		GL11.glPushMatrix();
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		//GL11.glColor3f(0, 0, 0);
		
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		GL11.glPopMatrix();
		
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		shotTexture.bind();
		engine.draw(shotTexture);
		
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

    
    private void renderBody() {
    	HumanoidModel m = new HumanoidModel();
    	GL11.glPushMatrix();
    	{
    		GL11.glEnable(GL11.GL_LIGHTING);
    		GL11.glTranslatef(-positionX, -positionY, -positionZ);
    		GL11.glRotatef(getRotationY(),0,1,0);
    		
    		GL11.glColor3f(1, 1, 1);
    		m.render();
    	}
    	GL11.glPopMatrix();
		
	}

	public float[] getDestPos(float x, float y, float z, float dy, double d)
    {
      float yrotrad = rotationY / 180.0F * 3.141593F;
      float xrotrad = getPitch() / 180.0F * 3.141593F;
      float mod = (float) Math.cos(getPitch() / 180.0F * 3.141593F);
      
      Vector3f newpos = new Vector3f();
      newpos.set(-x, -y, -z);
      newpos.x += d*mod*(float)Math.sin(yrotrad);
      newpos.z -= d*mod*(float)Math.cos(yrotrad);
      newpos.y -= d*(float)Math.sin(xrotrad);

      float xp = (float) Math.floor(newpos.x);
      float yp = (float) Math.floor(newpos.y);
      float zp = (float) Math.floor(newpos.z);

      float[] a = { xp, yp, zp };
      return a;
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
		if (other == null) return false;
		if (-other.getX() < positionX+getSize() && -other.getX() > positionX-getSize() && 
				-other.getY() < positionY+getSize() && -other.getY() > positionY-getSize() && 
				-other.getZ() < positionZ+getSize() && -other.getZ() > positionZ-getSize()) {
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
		for (int xp = (int) (Math.floor(-positionX) - 2); xp < Math.ceil(-positionX) + 2; xp++) {
			for (int yp = (int) (-getPositionY() - 3.5f); yp < -getPositionY() + 2; yp++) {
				for (int zp = (int) (Math.floor(-positionZ)) - 2; zp < Math.ceil(-positionZ) + 2; zp++) {
					PhysicCube c = new PhysicCube(xp, yp, zp, ((CubeState) manager).cubeMap
							.getCubeType(xp, yp, zp), 1);

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
							if (fallDamage){
								if (velocityY > 10.0f)
									manager.playerHit();
								if (velocityY > 20.0f)
									manager.playerHit();
								if (velocityY > 30.0f)
									manager.playerHit();
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
		return id;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getPitch() {
		return pitch;
	}

	public int getSpell(int i) {
		int id = 0;
		id = WandItem.items.getItem(i).getItem().shiftedIndex;
		return id;
	}
}