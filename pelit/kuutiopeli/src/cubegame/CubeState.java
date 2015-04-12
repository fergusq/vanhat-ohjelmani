package cubegame;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import sunmaker.ecreep.gui.FontRenderer;

import cubegame.item.Item;
import cubegame.log.Logger;

import com.loader.WavefrontObject;

import cubegame.map.BasicMapGenerator;
import cubegame.map.CubeMap;
import cubegame.map.level.EntitiedLevel;
import cubegame.map.level.Level;
import cubegame.multiplayer.HumanMPPacket;
import cubegame.multiplayer.NetClientHandler;
import cubegame.multiplayer.packets.ChatPacket;
import cubegame.multiplayer.packets.CubeChangePacket;
import cubegame.multiplayer.packets.PlayerMovePacket;
import cubegame.npc.NPC;
import cubegame.npc.Spirit;
import cubegame.physics.HalfPhysicCube;
import cubegame.physics.PhysicCube;
import cubegame.render.BoxRenderer;
import cubegame.render.CubeRenderer;
import cubegame.render.TerrainRenderer;
import cubegame.save.BCCHelper;
import cubegame.sound.Sound;
import cubegame.sound.SoundLoader;



/**
 * This state is responsible for rendering the game world and handling
 * the mechanics of game play.
 * 
 * @see InGameState
 * 
 * @author IH, edited from the original code of Kevin Glass
 */
public class CubeState implements GameState, EntityManager {
	/** The unique name of this state */
	public static final String NAME = "cubeState"; //$NON-NLS-1$
	
	/** The texture for the back drop */
	public Texture background;
	/** The texture for the ship */
	private Texture shipTexture;
	
	private Texture fontTexture;
	
	private Texture guiTexture;
	
	private Texture red;
	@SuppressWarnings("unused")
	private Texture green;
	private Texture blue;
	@SuppressWarnings("unused")
	private Texture yellow;
	@SuppressWarnings("unused")
	private Texture purple;
	
	/** The model of the player's ship */
	private WavefrontObject shipModel;
	
	/** The font used to draw the text to the screen */
	private BitmapFont font;
	
	/** The entity representing the player */
	public Human[] player;
	private float pitch = 0.0f;
	private float dy = 0.0f;
	private float yaw = 0.0f;
	private float dx = 0.0f;
	/** The entities in the game */
	ArrayList<Entity> entities = new ArrayList<Entity>();
	/** The list of entities to be added at the next opportunity */
	private ArrayList<Entity> addList = new ArrayList<Entity>();
	/** The list of entities to be removed at the next opportunity */
	private ArrayList<Entity> removeList = new ArrayList<Entity>();

	/** The OpenGL material properties applied to everything in the game */
	private FloatBuffer material;
	
	/** The current score */
	private int score;
	/** True if the game is over */
	private boolean gameOver;
	
	/** The sound effect to play when rocks split apart */
	private Sound split;
	
	/** The current level of play */
	public int level;
	/** The timeout for the game over message before resetting to the menu */
	private int gameOverTimeout;
	
	WavefrontObject obj = null;
	
	private FloatBuffer fogColor = GLAllocation.createDirectFloatBuffer(16);

	private Sound heart;

	private boolean lagStop = false;

	private boolean isNight;

	/*private Thread lightThread;

	private LightThread lThread;*/

	private int changeViewTimeout;
	
	static final int FOG_DISTANCE = 20;
	
	public final int SIZE = 128;
	public final int HALF_SIZE = SIZE / 2;
	
	public EntitiedLevel cubeMap = new EntitiedLevel(SIZE);
	
	public Logger logger;

	private CubeRenderer cubeRenderer;

	/*private boolean[][][] drawTop;
	private boolean[][][] drawBottom;
	private boolean[][][] drawEast;
	private boolean[][][] drawWest;
	private boolean[][][] drawNorth;
	private boolean[][][] drawSouth;*/
	
	private boolean[][] mapData;

	private int[] textureMap;

	public int cam = 0;

	private int changeCamTimeout;

	private int spawnSpiritTimeout;
	
	public boolean isMultiplayer = false;

	private NetClientHandler nethandler;

	private static Object threadSync = new Object();

	private int liquidFlowingThreads;

	private int delta;

	private int chatTimeout;
	private boolean chat;
	private String chatText = new String();

	public boolean showChat;
	public String rChat = new String();

	public int rChatTimeout;

	private int deaths;

	public boolean lowLagMode = false;

	private int escapeTimeout;
	
	/**
	 * Create a new game state
	 */
	public CubeState() {
	}

	/**
	 * @see GameState#getName()
	 */
	public String getName() {
		return NAME;
	}
	
	/**
	 * Defint the light setup to view the scene
	 */
	private void defineLight() {
		FloatBuffer buffer;
		
		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(0.95f).put(0.95f).put(0.95f).put(0.2f); 
		buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, buffer);
		
		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(0.95f).put(0.95f).put(0.95f).put(1);
		buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, buffer);
		
		// setup the ambient light 

		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(1.8f).put(1.8f).put(1.8f).put(1.8f);
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
		GL11.glEnable( GL11.GL_FOG ); 
		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
		GL11.glHint( GL11.GL_FOG_HINT, GL11.GL_NICEST);
		GL11.glFogf( GL11.GL_FOG_DENSITY, 0.35f );
	    GL11.glFogf( GL11.GL_FOG_START, FOG_DISTANCE-10 );
	    GL11.glFogf( GL11.GL_FOG_END, FOG_DISTANCE-2);
	    GL11.glFog(GL11.GL_FOG_COLOR, createColor(0.0f, 151f/256f, 254f/256f, 1.0f)); 
	    
	}

	/**
	 * @see GameState#init(GameWindow)
	 */
	public void init(GameWindow window) throws IOException {
		defineLight();
		
		TextureLoader loader = new TextureLoader();
		background = loader.getTexture("res/sky.jpg"); //$NON-NLS-1$
		
		shipTexture = loader.getTexture("res/syan.png"); //$NON-NLS-1$
		shipModel = new WavefrontObject("res/Syan.obj"); //$NON-NLS-1$
		
		fontTexture = loader.getTexture("res/font.png"); //$NON-NLS-1$
		font = new BitmapFont(fontTexture, 32, 32, 2);
		
		guiTexture = loader.getTexture("res/x.png"); //$NON-NLS-1$
		
		red = loader.getTexture("res/colors/red.png"); //$NON-NLS-1$
		//green = loader.getTexture("res/colors/green.png");
		blue = loader.getTexture("res/colors/blue.png"); //$NON-NLS-1$
		//yellow = loader.getTexture("res/colors/yellow.png");
		//purple = loader.getTexture("res/colors/purple.png");
		
		//obj = new WavefrontObject("bin/res/floor.obj");
		
		heart = SoundLoader.get().getOgg("res/sounds/loops/voiceOfHeart.ogg"); //$NON-NLS-1$
		//split = SoundLoader.get().getOgg("res/bush.ogg");
		
		logger = new Logger();
		cubeRenderer = new CubeRenderer();
		
		player = new Human[2];
		
		/*drawTop = new boolean[SIZE][SIZE][SIZE];
		drawBottom = new boolean[SIZE][SIZE][SIZE];
		drawNorth = new boolean[SIZE][SIZE][SIZE];
		drawSouth = new boolean[SIZE][SIZE][SIZE];
		drawEast = new boolean[SIZE][SIZE][SIZE];
		drawWest = new boolean[SIZE][SIZE][SIZE];*/
		
		mapData = new boolean[getArrayIndexFromCoords(HALF_SIZE, HALF_SIZE, HALF_SIZE)+1][12];
		
		textureMap = new int[getArrayIndexFromCoords(HALF_SIZE, HALF_SIZE, HALF_SIZE)+1];
		
		nethandler = new NetClientHandler(this);
		
		initLevel();
		
	}
	
	
	private void initLevel() {
		
		entities.clear();
		
		ArrayList<Entity> saved = new ArrayList<Entity>();
		/*try {
			//spawnFloor(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		//BasicMapGenerator gen = new BasicMapGenerator();
		//cubeMap = gen.generate();
		
		//cubeMap.convertFromEntityMapToCubeMap(entities);
		
		cubeMap = new EntitiedLevel(SIZE);
		
		cubeMap.init(this);

		try {
			BCCHelper.createBCCFile(Messages.getString("CubeState.saveFile"), cubeMap); //$NON-NLS-1$
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		player[0] = new Human(shipTexture, shipModel, createVector(0, -11, 0));
		player[1] = new Human(shipTexture, shipModel, createVector(0, -11, 0));
		
		if (!lowLagMode) entities.add(new Spirit(null, createVector(0, 0, 0)));
		
		createShadowsAndConfRender();
		if (!isMultiplayer && !lowLagMode) startFlowControlThread();
		//startPlayerControlThread();
	}

	private void createShadowsAndConfRender(){
		int counter2 = 0;
		final double finalCount = SIZE-1 * SIZE-1 * SIZE-1;
		int prev = 0;
		
		for (int xp = -HALF_SIZE; xp < HALF_SIZE; xp++){
			for (int yp = -HALF_SIZE; yp < HALF_SIZE; yp++){
				for (int zp = -HALF_SIZE; zp < HALF_SIZE; zp++){
					
					counter2++;
					
			PhysicCube cube = new PhysicCube(xp, yp, zp, cubeMap.getCubeType(xp, yp, zp), 1);
			if (cube == null) continue;
			if ((cube).getType().getId() == 0) {
				try {
					textureMap[getArrayIndexFromCoords(xp, yp, zp)] = Cube.getTextureOfType(CubeType.net).textureID;
				} catch (IOException e) {logger.warn(e.getMessage());}
				continue;
			}
			
			{
				if (!lowLagMode) cube.setTopInShadow(checkIsTopOfCubeInShadow(cube));
				cube.setDrawTop(!isCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()));
				cube.setDrawBottom(!isCube((int)cube.getX(), (int)cube.getY()-1, (int)cube.getZ()));
			}
			{
				if (!lowLagMode) cube.setNorthInShadow(checkIsNorthOfCubeInShadow(cube));
				if (!lowLagMode) cube.setSouthInShadow(checkIsSouthOfCubeInShadow(cube));
				cube.setDrawNorth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()+1));
				cube.setDrawSouth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()-1));
			}
			{
				if (!lowLagMode) cube.setEastInShadow(checkIsEastOfCubeInShadow(cube));
				if (!lowLagMode) cube.setWestInShadow(checkIsWestOfCubeInShadow(cube));
				cube.setDrawEast(!isCube((int)cube.getX()-1, (int)cube.getY(), (int)cube.getZ()));
				cube.setDrawWest(!isCube((int)cube.getX()+1, (int)cube.getY(), (int)cube.getZ()));
			}
			
			{
				if (!cube.getType().isNonOpaque()){
					cube.setDrawTop(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()));
					cube.setDrawBottom(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY()-1, (int)cube.getZ()));
					cube.setDrawNorth(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()+1));
					cube.setDrawSouth(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()-1));
					cube.setDrawEast(!isOpaqueSolidCube((int)cube.getX()-1, (int)cube.getY(), (int)cube.getZ()));
					cube.setDrawWest(!isOpaqueSolidCube((int)cube.getX()+1, (int)cube.getY(), (int)cube.getZ()));
				
				}
				else{
					cube.setDrawTop(!isCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()));
					cube.setDrawBottom(!isCube((int)cube.getX(), (int)cube.getY()-1, (int)cube.getZ()));
					cube.setDrawNorth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()+1));
					cube.setDrawSouth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()-1));
					cube.setDrawEast(!isCube((int)cube.getX()-1, (int)cube.getY(), (int)cube.getZ()));
					cube.setDrawWest(!isCube((int)cube.getX()+1, (int)cube.getY(), (int)cube.getZ()));
					
				}
				
				if (!cube.isDrawBottom() && 
						!cube.isDrawEast() && 
						!cube.isDrawNorth() && 
						!cube.isDrawSouth() && 
						!cube.isDrawTop() && 
						!cube.isDrawWest() &&
						!cube.getType().isLiquid()) {
					cube = null;
					continue;
				}
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][0] = cube.isDrawTop();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][1] = cube.isDrawBottom();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][2] = cube.isDrawNorth();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][3] = cube.isDrawSouth();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][4] = cube.isDrawEast();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][5] = cube.isDrawWest();
				
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][6] = cube.isTopInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][7] = cube.isNorthInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][8] = cube.isSouthInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][9] = cube.isEastInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][10] = cube.isWestInShadow();
				
				try {
					textureMap[getArrayIndexFromCoords(xp, yp, zp)] = Cube.getTextureOfType(CubeType.getTypeFromNumber(cubeMap.getCubeType(xp, yp, zp))).textureID;
				} catch (IOException e) {logger.warn(e.getMessage());}
				
				cube = null;
				
			}
			cube = null;
			
			double p = counter2 / finalCount * 100;
			if ((int)p % 10 == 0 && prev != p); 
			prev = (int) p;
		}
				}
			}
		System.gc();
	}
	
	private void createShadowsAndConfRenderAt(int x, int y, int z) {
		
		for (int xp = x-4; xp < x+4; xp++){
			for (int yp = y-4; yp < y+4; yp++){
				for (int zp = z-4; zp < z+4; zp++){
					
			PhysicCube cube = new PhysicCube(xp, yp, zp, cubeMap.getCubeType(xp, yp, zp), 1);
			if (cube == null) continue;
			if ((cube).getType().getId() == 0) {
				try {
					textureMap[getArrayIndexFromCoords(xp, yp, zp)] = Cube.getTextureOfType(CubeType.net).textureID;
				} catch (IOException e) {logger.warn(e.getMessage());}
				continue;
			}
			
			{
				if (!lowLagMode) cube.setTopInShadow(checkIsTopOfCubeInShadow(cube));
				cube.setDrawTop(!isCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()));
				cube.setDrawBottom(!isCube((int)cube.getX(), (int)cube.getY()-1, (int)cube.getZ()));
			}
			{
				if (!lowLagMode) cube.setNorthInShadow(checkIsNorthOfCubeInShadow(cube));
				if (!lowLagMode) cube.setSouthInShadow(checkIsSouthOfCubeInShadow(cube));
				cube.setDrawNorth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()+1));
				cube.setDrawSouth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()-1));
			}
			{
				if (!lowLagMode) cube.setEastInShadow(checkIsEastOfCubeInShadow(cube));
				if (!lowLagMode) cube.setWestInShadow(checkIsWestOfCubeInShadow(cube));
				cube.setDrawEast(!isCube((int)cube.getX()-1, (int)cube.getY(), (int)cube.getZ()));
				cube.setDrawWest(!isCube((int)cube.getX()+1, (int)cube.getY(), (int)cube.getZ()));
			}
			
			{
				if (!cube.getType().isNonOpaque()){
					cube.setDrawTop(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()));
					cube.setDrawBottom(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY()-1, (int)cube.getZ()));
					cube.setDrawNorth(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()+1));
					cube.setDrawSouth(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()-1));
					cube.setDrawEast(!isOpaqueSolidCube((int)cube.getX()-1, (int)cube.getY(), (int)cube.getZ()));
					cube.setDrawWest(!isOpaqueSolidCube((int)cube.getX()+1, (int)cube.getY(), (int)cube.getZ()));
				
				}
				else{
					cube.setDrawTop(!isCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()));
					cube.setDrawBottom(!isCube((int)cube.getX(), (int)cube.getY()-1, (int)cube.getZ()));
					cube.setDrawNorth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()+1));
					cube.setDrawSouth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()-1));
					cube.setDrawEast(!isCube((int)cube.getX()-1, (int)cube.getY(), (int)cube.getZ()));
					cube.setDrawWest(!isCube((int)cube.getX()+1, (int)cube.getY(), (int)cube.getZ()));
					
				}
				
				if (!cube.isDrawBottom() && 
						!cube.isDrawEast() && 
						!cube.isDrawNorth() && 
						!cube.isDrawSouth() && 
						!cube.isDrawTop() && 
						!cube.isDrawWest() &&
						!cube.getType().isLiquid()) {
					cube = null;
					continue;
				}
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][0] = cube.isDrawTop();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][1] = cube.isDrawBottom();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][2] = cube.isDrawNorth();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][3] = cube.isDrawSouth();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][4] = cube.isDrawEast();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][5] = cube.isDrawWest();
				
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][6] = cube.isTopInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][7] = cube.isNorthInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][8] = cube.isSouthInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][9] = cube.isEastInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][10] = cube.isWestInShadow();
				
				try {
					textureMap[getArrayIndexFromCoords(xp, yp, zp)] = Cube.getTextureOfType(CubeType.getTypeFromNumber(cubeMap.getCubeType(xp, yp, zp))).textureID;
				} catch (IOException e) {logger.warn(e.getMessage());}
				
				cube = null;
				
			}
			cube = null;
			
		}
				}
			}
		System.gc();
		
	}
	
	private void createShadowsAndConfRenderAtCube(int xp, int yp, int zp) {
							
			PhysicCube cube = new PhysicCube(xp, yp, zp, cubeMap.getCubeType(xp, yp, zp), 1);
			if (cube == null) return;
			if ((cube).getType().getId() == 0) {
				try {
					textureMap[getArrayIndexFromCoords(xp, yp, zp)] = Cube.getTextureOfType(CubeType.net).textureID;
				} catch (IOException e) {logger.warn(e.getMessage());}
				return;
			}
			
			{
				if (!lowLagMode) cube.setTopInShadow(checkIsTopOfCubeInShadow(cube));
				cube.setDrawTop(!isCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()));
				cube.setDrawBottom(!isCube((int)cube.getX(), (int)cube.getY()-1, (int)cube.getZ()));
			}
			{
				if (!lowLagMode) cube.setNorthInShadow(checkIsNorthOfCubeInShadow(cube));
				if (!lowLagMode) cube.setSouthInShadow(checkIsSouthOfCubeInShadow(cube));
				cube.setDrawNorth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()+1));
				cube.setDrawSouth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()-1));
			}
			{
				if (!lowLagMode) cube.setEastInShadow(checkIsEastOfCubeInShadow(cube));
				if (!lowLagMode) cube.setWestInShadow(checkIsWestOfCubeInShadow(cube));
				cube.setDrawEast(!isCube((int)cube.getX()-1, (int)cube.getY(), (int)cube.getZ()));
				cube.setDrawWest(!isCube((int)cube.getX()+1, (int)cube.getY(), (int)cube.getZ()));
			}
			
			{
				if (!cube.getType().isNonOpaque()){
					cube.setDrawTop(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()));
					cube.setDrawBottom(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY()-1, (int)cube.getZ()));
					cube.setDrawNorth(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()+1));
					cube.setDrawSouth(!isOpaqueSolidCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()-1));
					cube.setDrawEast(!isOpaqueSolidCube((int)cube.getX()-1, (int)cube.getY(), (int)cube.getZ()));
					cube.setDrawWest(!isOpaqueSolidCube((int)cube.getX()+1, (int)cube.getY(), (int)cube.getZ()));
				
				}
				else{
					cube.setDrawTop(!isCube((int)cube.getX(), (int)cube.getY()+1, (int)cube.getZ()));
					cube.setDrawBottom(!isCube((int)cube.getX(), (int)cube.getY()-1, (int)cube.getZ()));
					cube.setDrawNorth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()+1));
					cube.setDrawSouth(!isCube((int)cube.getX(), (int)cube.getY(), (int)cube.getZ()-1));
					cube.setDrawEast(!isCube((int)cube.getX()-1, (int)cube.getY(), (int)cube.getZ()));
					cube.setDrawWest(!isCube((int)cube.getX()+1, (int)cube.getY(), (int)cube.getZ()));
					
				}
				
				if (!cube.isDrawBottom() && 
						!cube.isDrawEast() && 
						!cube.isDrawNorth() && 
						!cube.isDrawSouth() && 
						!cube.isDrawTop() && 
						!cube.isDrawWest() &&
						!cube.getType().isLiquid()) {
					cube = null;
					return;
				}
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][0] = cube.isDrawTop();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][1] = cube.isDrawBottom();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][2] = cube.isDrawNorth();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][3] = cube.isDrawSouth();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][4] = cube.isDrawEast();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][5] = cube.isDrawWest();
				
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][6] = cube.isTopInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][7] = cube.isNorthInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][8] = cube.isSouthInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][9] = cube.isEastInShadow();
				this.mapData[getArrayIndexFromCoords(xp, yp, zp)][10] = cube.isWestInShadow();
				
				try {
					textureMap[getArrayIndexFromCoords(xp, yp, zp)] = Cube.getTextureOfType(CubeType.getTypeFromNumber(cubeMap.getCubeType(xp, yp, zp))).textureID;
				} catch (IOException e) {logger.warn(e.getMessage());}
				
				cube = null;
				
			}
			cube = null;
			
		System.gc();
		
	}
	
	private synchronized void checkLiquidFlow(){
		for (int xp = (int) (-player[cam].getPositionX())-5; xp < -player[cam].getPositionX()+5; xp++){
			for (int yp = (int) (-player[cam].getPositionY())-5; yp < -player[cam].getPositionY()+5; yp++){
				for (int zp = (int) (-player[cam].getPositionZ())-5; zp < -player[cam].getPositionZ()+5; zp++){
					if (CubeType.getTypeFromNumber(cubeMap.getCubeType(xp, yp, zp)).isLiquid()){
						int liquid = cubeMap.getCubeType(xp, yp, zp);
						switch (cubeMap.getCubeType(xp, yp-1, zp))
								{
						case Cube.AIR:
							cubeMap.setCubeType(xp, yp-1, zp, liquid);
							break;
						case Cube.WATER:
							break;
						case Cube.ACID:
							break;
						case Cube.LAVA:
							break;
						default:
							if (cubeMap.getCubeType(xp+1, yp, zp) == 0) cubeMap.setCubeType(xp+1, yp, zp, liquid);
							if (cubeMap.getCubeType(xp-1, yp, zp) == 0) cubeMap.setCubeType(xp-1, yp, zp, liquid);
							if (cubeMap.getCubeType(xp, yp, zp+1) == 0) cubeMap.setCubeType(xp, yp, zp+1, liquid);
							if (cubeMap.getCubeType(xp, yp, zp-1) == 0) cubeMap.setCubeType(xp, yp, zp-1, liquid);
							break;
								}
						
						/*if (cubeMap.getCubeType(xp+1, yp, zp) == 0) cubeMap.setCubeType(xp+1, yp, zp, cubeMap.getCubeType(xp, yp, zp));
						if (cubeMap.getCubeType(xp-1, yp, zp) == 0) cubeMap.setCubeType(xp-1, yp, zp, cubeMap.getCubeType(xp, yp, zp));
						if (cubeMap.getCubeType(xp, yp, zp+1) == 0) cubeMap.setCubeType(xp, yp, zp+1, cubeMap.getCubeType(xp, yp, zp));
						if (cubeMap.getCubeType(xp, yp, zp-1) == 0) cubeMap.setCubeType(xp, yp, zp-1, cubeMap.getCubeType(xp, yp, zp));
						if (cubeMap.getCubeType(xp, yp-1, zp) == 0) cubeMap.setCubeType(xp, yp-1, zp, cubeMap.getCubeType(xp, yp, zp));*/
						createShadowsAndConfRenderAtCube(xp, yp, zp);
						try {
							Thread.sleep(128);
						} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
			}
		}
		cubeMap.tick();
	}
	
	private void startFlowControlThread(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (!isMultiplayer){
					checkLiquidFlow();
				}
			}
				
		});
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}
	
	private void startPlayerControlThread(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true){
					
					try {
						moveUpdate();
						Thread.sleep(10);
					} catch (InterruptedException e) {e.printStackTrace();
					}
					
				}
			}
				
		});
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}
	
	protected void moveUpdate() throws InterruptedException {	
		player[cam].moveUpdate(this, delta);
	}

	private int getArrayIndexFromCoords(int xp, int yp, int zp) {
		xp += HALF_SIZE;
		yp += HALF_SIZE;
		zp += HALF_SIZE;
		
		if (xp > SIZE || xp < 0 || yp > SIZE || yp < 0 || zp > SIZE || zp < 0) return 0;
		
		return (xp*SIZE*SIZE + yp*SIZE + zp);
	}

	/**
	 * @see GameState#render(GameWindow, int)
	 */
	public void render(GameWindow window, int delta) {
		// reset the view transformation matrix back to the empty

		// state. 

		GL11.glLoadIdentity();

		material.put(1).put(1).put(1).put(1); 
		material.flip();
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, material);
		GL11.glMaterial(GL11.GL_BACK, GL11.GL_DIFFUSE, material);

		changeViewTimeout -= delta;
			if (changeViewTimeout <= 0){
				if (Keyboard.isKeyDown(Keyboard.KEY_F)) isNight = !isNight;
				changeViewTimeout = 500;
		}
		
		// draw our background image

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(1, 1, 1);
		/*Sphere starMap = new Sphere();
		red.bind();
		starMap.draw(92 * 3.14f, 10, 10);*/
		if (!isNight) {
			drawBackground(window);
		}
		
		// position the view a way back from the models so we

		// can see them

		//GL11.glTranslatef(0,0,-50);

		// loop through all entities in the game rendering them

		boolean isInWater = isPlayersEyeInsideOfLiquid();
		
		/*try {
			cubeMap.render();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		if (isMultiplayer) cam = 0;
		
		Mouse.setGrabbed(true);
	
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Hiiren kääntö, otetaan yaw ja pitch
		Mouse.updateCursor();
		pitch = Mouse.getDX();//dy + Mouse.getY()*100;
		dy = Mouse.getY()*100;
		yaw = dx + Mouse.getX();
		dx = Mouse.getX();
		player[cam].setRotationY(dx);
		//GLU.gluLookAt(ent.getPositionX(), ent.getPositionY(), ent.getPositionZ(), 
		//	ent.getPositionX()+(ent.getForwardX()*1.1f), pitch/10, 
		//	ent.getPositionZ()+(ent.getForwardZ()*1.1f), 0, 1, 0);//0.6f, ent.getX(), ent.getY());
		player[cam].lookThrough(-dy+18000);
		
		GL11.glPushMatrix();
		for (int xp = (int) (-player[cam].getPositionX())-FOG_DISTANCE; xp < -player[cam].getPositionX()+FOG_DISTANCE; xp++){
			for (int yp = (int) (-player[cam].getPositionY())-FOG_DISTANCE-5; yp < -player[cam].getPositionY()+FOG_DISTANCE+5; yp++){
				for (int zp = (int) (-player[cam].getPositionZ())-FOG_DISTANCE; zp < -player[cam].getPositionZ()+FOG_DISTANCE; zp++){
					if (cubeMap.getCubeType(xp, yp, zp) == 0) continue;
					
					if (!isInWater) GL11.glColor3f(1, 1, 1); else GL11.glColor3f(0.5f, 0.5f, 0.9f);
					
					cubeRenderer.renderCube(this, xp, yp, zp);
		}}}
		
		if (!isNight && !lowLagMode) {
			GL11.glPushMatrix();
			GL11.glTranslatef(-player[cam].getPositionX(), -player[cam].getPositionY(), -player[cam].getPositionZ());
			TerrainRenderer.renderSun();
			GL11.glPopMatrix();
			GL11.glFog(GL11.GL_FOG_COLOR, createColor(0.0f, 151f/256f, 254f/256f, 1.0f)); 
		} else if (!lowLagMode) {
			GL11.glPushMatrix();
			GL11.glTranslatef(-player[cam].getPositionX(), -player[cam].getPositionY(), -player[cam].getPositionZ());
			TerrainRenderer.renderMoon();
			GL11.glPopMatrix();
			GL11.glFog(GL11.GL_FOG_COLOR, createColor(0.0f, 0f, 0f, 1.0f)); 
		}
		
		//if (cam == 1) BoxRenderer.renderBasicPlayer(-player[0].getPositionX(), -player[0].getPositionY(), -player[0].getPositionZ(), 0.4f, 1.4f, 0.4f, -player[0].getRotationY(), -player[0].getPitch());
		//else BoxRenderer.renderBasicPlayer(-player[1].getPositionX(), -player[1].getPositionY(), -player[1].getPositionZ(), 0.4f, 1.4f, 0.4f, -player[1].getRotationY(), -player[1].getPitch());
		
		if (cam == 1){
			BoxRenderer.renderBipedWithCloak(-player[0].getPositionX(), -player[0].getPositionY(), -player[0].getPositionZ(), player[0].getRotationY(), player[0].getPitch(), 0);
			BoxRenderer.renderBipedRightHand(-player[1].getPositionX(), -player[1].getPositionY(), -player[1].getPositionZ(), player[1].getRotationY(), player[1].getPitch(), player[1].legRot);
		}
		else{
			BoxRenderer.renderBipedWithCloak(-player[1].getPositionX(), -player[1].getPositionY(), -player[1].getPositionZ(), player[1].getRotationY(), player[1].getPitch(), 0);
			BoxRenderer.renderBipedRightHand(-player[0].getPositionX(), -player[0].getPositionY(), -player[0].getPositionZ(), player[0].getRotationY(), player[0].getPitch(), player[0].legRot);
		}
		
		player[cam].renderSelectBox();
		
		cubeMap.render(delta, this);
		
		GL11.glPopMatrix();
		
		drawGUI(window);
	}

	/**
	 * Draw the overlay for position, current cube, lifes and radar
	 * 
	 * @param window The window in which the GUI is displayed 
	 */
	private void drawGUI(GameWindow window) {
		window.enterOrtho();
		
		guiTexture.bind();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor3f(1, 1, 1);
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
		GL11.glDisable(GL11.GL_BLEND);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		for (int i = 0; i < player.length && !lowLagMode; i++){
			if (i == cam) continue;
			GL11.glPushMatrix();
				GL11.glTranslatef(player[i].getPositionX()*2+100, player[i].getPositionZ()*2+500, 0);
				GL11.glRotatef(player[i].getRotationY(),0,0,1);
				GL11.glTranslatef(-player[i].getPositionX()*2-100, -player[i].getPositionZ()*2-500, 0);
				GL11.glColor3f(0.5f, 0, 0);
				red.bind();
				GL11.glBegin(GL11.GL_TRIANGLES);
					GL11.glVertex2f(player[i].getPositionX()*2+100+5, player[i].getPositionZ()*2+500+5);
					GL11.glVertex2f(player[i].getPositionX()*2+100, player[i].getPositionZ()*2+500);
					GL11.glVertex2f(player[i].getPositionX()*2+100-5, player[i].getPositionZ()*2+500+5);
				GL11.glEnd();
			GL11.glPopMatrix();
		}
		
		GL11.glPushMatrix();
			GL11.glTranslatef(player[cam].getPositionX()*2+100, player[cam].getPositionZ()*2+500, 0);
			GL11.glRotatef(player[cam].getRotationY(),0,0,1);
			GL11.glTranslatef(-player[cam].getPositionX()*2-100, -player[cam].getPositionZ()*2-500, 0);
			GL11.glColor3f(0, 0, 0.5f);
			blue.bind();
			GL11.glBegin(GL11.GL_TRIANGLES);
				GL11.glVertex2f(player[cam].getPositionX()*2+100+5, player[cam].getPositionZ()*2+500+5);
				GL11.glVertex2f(player[cam].getPositionX()*2+100, player[cam].getPositionZ()*2+500);
				GL11.glVertex2f(player[cam].getPositionX()*2+100-5, player[cam].getPositionZ()*2+500+5);
			GL11.glEnd();
		GL11.glPopMatrix();
		
		renderHotBar(300, 500, 0);
		//fontTexture.bind();
		
		GL11.glColor3f(1,1,0);
		
		/*String str = player[cam].getSelectedCubeType().name;
		font.drawString(0, str, 650 - str.length() * 27, 480);*/
		
		DecimalFormat formater = new DecimalFormat("0.0"); //$NON-NLS-1$
		//font.drawString(1, "TULOS:" + score + " R: " + player.getRotationY(), 5, 5);
		//font.drawString(0, "X: "+ formater.format(player[cam].getPositionX())+ " Y: "+ formater.format(player[cam].getPositionY())+ " Z: "+ formater.format(player[cam].getPositionZ()), 5, 5); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		FontRenderer.sRender(5, 5, "X: "+ formater.format(player[cam].getPositionX())+ " Y: "+ formater.format(player[cam].getPositionY())+ " Z: "+ formater.format(player[cam].getPositionZ()));
		
		FontRenderer.sRender(5, 20, "X: "+ formater.format(player[cam].getMoveX())+ " Y: "+ formater.format(player[cam].getVelocityY())+ " Z: "+ formater.format(player[cam].getMoveZ()));
		
		GL11.glColor3f(1,0,0);
		String lifeString = ""; //$NON-NLS-1$
		for (int i=0;i<player[cam].health;i++) {
			lifeString += ((char) 3); //$NON-NLS-1$
		}
		//font.drawString(0, lifeString, 795 - (lifeString.length() * (27/2)), 5);
		FontRenderer.sRenderWithColor(795 - (lifeString.length() * (27/2)), 5, lifeString, 1, 0, 0);
		
		if (window.delta > 150) font.drawString(0, Messages.getString("ingame.delta") + window.delta + Messages.getString("ingame.lagWarning"), 5, 37); //$NON-NLS-1$ //$NON-NLS-2$
		if (window.delta < 150) font.drawString(0, Messages.getString("ingame.delta") + window.delta, 5, 37); //$NON-NLS-1$

		GL11.glColor3f(1,0,0);
		
		if (gameOver) {
			font.drawString(1, Messages.getString("ingame.gameOver"), 280, 256); //$NON-NLS-1$
			font.drawString(1, Messages.getString("ingame.score") + score, 250, 286); //$NON-NLS-1$
		}
		
		if (chat){
			FontRenderer.sRender(5, 386, "> " + chatText + "_"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (showChat || chat){
			FontRenderer.sRender(5, 370, "> " + rChat); //$NON-NLS-1$
			rChatTimeout--;
			if (rChatTimeout <= 0) showChat = false;
		}
		
		/*if(window.delta > 500){
			if (!lagStop) gameOverTimeout = 10000;
			lagStop = true;
			font.drawString(1, "WARNING! DELTA IS TOO BIG!", 60, 256);
			font.drawString(1, "PLAYING IS IMPOSSIBLE!", 120, 286);
			font.drawString(1, "Exiting to menu: " + (gameOverTimeout/1000) + " sec.", 120, HALF_SIZE-16);
			String countString = "";
			for (int i=0;i<(10000-gameOverTimeout)/1000;i++) {
				countString += "--";
			}
			font.drawString(0, countString, 120, 346);
		}
		else lagStop = false;*/
		
		window.leaveOrtho();
	}
	
	private void renderHotBar(int x, int y, int type) {
		{
			//Texture cube = Cube.getTextureOfType(player[cam].getSelectedCubeType());
			int selected = type == 0 ? player[cam].selectedIndex : player[cam].spell;
			
			int length = 8;
			GL11.glEnable(GL11.GL_BLEND);
			for (int i = 0; i < length; i++)
			{
				int itemId = type == 0 ? player[cam].getTypeFromInventory(i).getItem().shiftedIndex : player[cam].getSpell(i);
				Texture cube0 = !lowLagMode ? getItemTexture(itemId) : null;
		
				int i2 = 0;
				int i3 = 0;
				if (i == 0) i2 = 3;
				if (i == length-1) i3 = 3;
				
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glPushMatrix(); {
					GL11.glColor4f(0f, 0f, 0f, 0.75f);
					GL11.glBegin(GL11.GL_QUADS); {
						GL11.glTexCoord2f(0,0);
						GL11.glVertex2i(x - (23 + i2), y - 26);
						if (type != 0) GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.75f);
						GL11.glTexCoord2f(0,1);
						GL11.glVertex2i(x - (23 + i2), y + 26);
						GL11.glTexCoord2f(1,1);
						GL11.glVertex2i(x + (23 + i3), y + 26);
						if (type != 0) GL11.glColor4f(0f, 0f, 0f, 0.75f);
						GL11.glTexCoord2f(1,0);
						GL11.glVertex2i(x + (23 + i3), y - 26);
					} GL11.glEnd();
				} GL11.glPopMatrix();
				
				if (i == selected)
				{
					int menuF = 0;
					if (itemId == Item.wand.shiftedIndex && !lowLagMode) menuF = 3;
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glPushMatrix(); {
						GL11.glColor4f(1f, 1f, 1f, 0.5f);
						GL11.glBegin(GL11.GL_QUADS); {
							GL11.glTexCoord2f(0,0);
							GL11.glVertex2i(x-23,y-23-menuF);
							GL11.glTexCoord2f(0,1);
							GL11.glVertex2i(x-23,y+23);
							GL11.glTexCoord2f(1,1);
							GL11.glVertex2i(x+23,y+23);
							GL11.glColor4f(1f, 1f, 1f, 0.5f);
							GL11.glTexCoord2f(1,0);
							GL11.glVertex2i(x+23,y-23-menuF);
						} GL11.glEnd();
					} GL11.glPopMatrix();
					
				}
				
				if (i == selected && itemId == Item.wand.shiftedIndex && !lowLagMode)
				{
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glPushMatrix(); {
						GL11.glColor4f(1f, 1f, 1f, 0.5f);
						GL11.glBegin(GL11.GL_QUADS); {
							GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.75f);
							GL11.glTexCoord2f(0,0);
							GL11.glVertex2i(x-32,y-30);
							GL11.glColor4f(1f, 1f, 1f, 0.5f);
							GL11.glTexCoord2f(0,1);
							GL11.glVertex2i(x-23,y-26);
							GL11.glTexCoord2f(1,1);
							GL11.glVertex2i(x+23,y-26);
							GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.75f);
							GL11.glTexCoord2f(1,0);
							GL11.glVertex2i(x+32,y-30);
						} GL11.glEnd();
					} GL11.glPopMatrix();
					
				}
				
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				
				if (itemId < 256 && cube0 != null) {
				
					GL11.glPushMatrix(); {
						cube0.bind();
						GL11.glColor3f(0.9f, 0.9f, 0.9f);
						GL11.glBegin(GL11.GL_QUADS); {
							GL11.glTexCoord2f(0,0);
							GL11.glVertex2i(x-0,y-20);
							GL11.glTexCoord2f(0,1);
							GL11.glVertex2i(x-20,y-10);
							GL11.glTexCoord2f(1,1);
							GL11.glVertex2i(x+0,y+0);
							GL11.glTexCoord2f(1,0);
							GL11.glVertex2i(x+20,y-10);
						} GL11.glEnd();
						GL11.glColor3f(0.8f, 0.8f, 0.8f);
						GL11.glBegin(GL11.GL_QUADS); {
							GL11.glTexCoord2f(0,0);
							GL11.glVertex2i(x-20,y-10);
							GL11.glTexCoord2f(0,1);
							GL11.glVertex2i(x-20,y+10);
							GL11.glTexCoord2f(1,1);
							GL11.glVertex2i(x+0,y+20);
							GL11.glTexCoord2f(1,0);
							GL11.glVertex2i(x+0,y-0);
						} GL11.glEnd();
						GL11.glColor3f(0.7f, 0.7f, 0.7f);
						GL11.glBegin(GL11.GL_QUADS); {
							GL11.glTexCoord2f(0,0);
							GL11.glVertex2i(x-0,y-0);
							GL11.glTexCoord2f(0,1);
							GL11.glVertex2i(x-0,y+20);
							GL11.glTexCoord2f(1,1);
							GL11.glVertex2i(x+20,y+10);
							GL11.glTexCoord2f(1,0);
							GL11.glVertex2i(x+20,y-10);
						} GL11.glEnd();
					} GL11.glPopMatrix();
				} else if (cube0 != null) {
					GL11.glPushMatrix(); {
						cube0.bind();
						GL11.glColor3f(1f, 1f, 1f);
						GL11.glBegin(GL11.GL_QUADS); {
							GL11.glTexCoord2f(0,0);
							GL11.glVertex2i(x-8,y-8);
							GL11.glTexCoord2f(0,1);
							GL11.glVertex2i(x-8,y+8);
							GL11.glTexCoord2f(1,1);
							GL11.glVertex2i(x+8,y+8);
							GL11.glTexCoord2f(1,0);
							GL11.glVertex2i(x+8,y-8);
						} GL11.glEnd();
					} GL11.glPopMatrix();
				}
				x += 46;
			}
			GL11.glDisable(GL11.GL_BLEND);
		}
		if (player[cam].getSelectedItem().getItem().shiftedIndex == Item.wand.shiftedIndex && type == 0){
			renderHotBar(300, 444, 1);
		}
	}

	private Texture getItemTexture(int type) {
		if (type < 256) {
			try {
				return Cube.getBottomTextureOfType(CubeType.getTypeFromNumber(type));
			} catch (IOException e) {}
		} else {
			return Item.getItemFromId(type).tex;
		}
		return null;
	}

	/**
	 * Draw the background image
	 * 
	 * @param window The window to display the background in 
	 */
	private void drawBackground(GameWindow window) {
		window.enterOrtho();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		background.bind();
		
		GL11.glColor3f(1, 1, 1);
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
	 * @see GameState#update(GameWindow, int)
	 */
	public void update(GameWindow window, int delta) {
		if (gameOver) {
			gameOverTimeout -= delta;
			if (gameOverTimeout < 0) {
				window.changeToState(MenuState.NAME);
			}
		}
		
		if (lagStop) {
			gameOverTimeout -= delta;
			if (gameOverTimeout < 0) {
				window.changeToState(MenuState.NAME);
			}
		}
		
		boolean escapeChat = false;
		
		while (Keyboard.next() && chat) {
		    if (Keyboard.getEventKeyState()) {
		    	if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && !chatText.isEmpty()) chatText = chatText.substring(0, chatText.length()-1);
		    	else {
		    		if (Keyboard.getEventKey() == 1) {
		    			chat = false;
		    			chatText = ""; //$NON-NLS-1$
		    			escapeChat = true;
		    			break;
		    		}
		    		if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
		    			chat = false;
		    			sendChatMessage(chatText);
		    			chatText = ""; //$NON-NLS-1$
		    			break;
		    		}
		    		if (Keyboard.getEventKey() == 54) continue;
		    		if (Keyboard.getEventKey() == 42) continue;
		    		if (Keyboard.getEventKey() == Keyboard.KEY_LCONTROL) continue;
		    		if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL) continue;
		    		if (Keyboard.getEventKey() == 56) continue;
		    		if (Keyboard.getEventKey() == 184) continue;
		    		chatText += Keyboard.getEventCharacter();
		    	}
		    }
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			
			if (escapeChat == true) {
				escapeTimeout = 500;
			}
			else if (escapeTimeout <= 0) {
				window.changeToState(MenuState.NAME);
			} else {
				escapeTimeout -= delta;
			}
			
			
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {spawnPlayer();}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {checkLiquidFlow();}

		chatTimeout -= delta;
		if (chatTimeout <= 0 /*&& isMultiplayer TODO*/){
			if (Keyboard.isKeyDown(Keyboard.KEY_T)) chat = true;
			chatTimeout = 500;
		}
		
		spawnSpiritTimeout -= delta;
		if (spawnSpiritTimeout <= 0 && !isMultiplayer){
			if (Keyboard.isKeyDown(Keyboard.KEY_G)) spawnSpirit();
			spawnSpiritTimeout = 500;
		}
		
		changeCamTimeout -= delta;
		if (changeCamTimeout <= 0 && !isMultiplayer){
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
				if (cam == 0) cam = 1; else cam = 0;
				changeCamTimeout = 500;
			}
		}
		
		this.delta = delta;
		
		if (!chat) player[cam].moveUpdate(this, delta);
		
		player[0].update(this, delta);
		if (!isMultiplayer) player[1].update(this, delta);
		
		cubeMap.update(delta, this);
		
		((EntitiedLevel)cubeMap).removeEntities(removeList);
		((EntitiedLevel)cubeMap).addEntities(addList);
		
		removeList.clear();
		addList.clear();
		
		if (Mouse.getX() == 0) {
			Mouse.setCursorPosition(798, Mouse.getY()); // Pidetään hiiri näytöllä
		}
		if (Mouse.getX() >= 799) {
			Mouse.setCursorPosition(2, Mouse.getY());
		}

		/*if (Mouse.getY() < 0) {
			Mouse.setCursorPosition(Mouse.getX(), 600);
		}*/
		if (Mouse.getY() > 360) {
			Mouse.setCursorPosition(Mouse.getX(), 360);
		}
	}
	
	private void sendChatMessage(String chatText2) {
		if (isMultiplayer) nethandler.addPacketToSendQueue(new ChatPacket(chatText2));
		else {
			rChat = Messages.getString("ingame.spChatWarning");
			rChatTimeout = 100;
			showChat = true;
		}
	}

	public void sendMovePacket() {
		nethandler.addPacketToSendQueue(
				new PlayerMovePacket(0, player[0].getPositionX(), player[0].getPositionY(), player[0].getPositionZ(),
						player[0].getRotationY(), player[0].getPitch(), player[0].legRot));
	}
	
	private void spawnSpirit() {
		addEntity(new Spirit(null, 
				createVector(
						player[cam].getPositionX(), 
						player[cam].getPositionY(), 
						player[cam].getPositionZ())
				));
	}

	private boolean checkIsTopOfCubeInShadow(Cube cube) {
		for (int i = (int) cube.getY()+1; i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX(), i, (int)cube.getZ())) return true;
		}
		
		return false;
	}
	private boolean checkIsNorthOfCubeInShadow(Cube cube) {
		for (int i = (int) cube.getY(); i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX(), i, (int)cube.getZ()+1)) return true;
		}
		
		return false;
	}
	private boolean checkIsSouthOfCubeInShadow(Cube cube) {
		for (int i = (int) cube.getY(); i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX(), i, (int)cube.getZ()-1)) return true;
		}
		
		return false;
	}
	private boolean checkIsEastOfCubeInShadow(Cube cube) {
		for (int i = (int) cube.getY(); i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX()-1, i, (int)cube.getZ())) return true;
		}
		
		return false;
	}
	private boolean checkIsWestOfCubeInShadow(Cube cube) {
		for (int i = (int) cube.getY(); i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX()+1, i, (int)cube.getZ())) return true;
		}
		
		return false;
	}
	
	private boolean checkIsTopOfCubeInShadow(PhysicCube cube) {
		for (int i = (int) cube.getY()+1; i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX(), i, (int)cube.getZ())) return true;
		}
		
		return false;
	}
	private boolean checkIsNorthOfCubeInShadow(PhysicCube cube) {
		for (int i = (int) cube.getY(); i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX(), i, (int)cube.getZ()+1)) return true;
		}
		
		return false;
	}
	private boolean checkIsSouthOfCubeInShadow(PhysicCube cube) {
		for (int i = (int) cube.getY(); i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX(), i, (int)cube.getZ()-1)) return true;
		}
		
		return false;
	}
	private boolean checkIsEastOfCubeInShadow(PhysicCube cube) {
		for (int i = (int) cube.getY(); i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX()-1, i, (int)cube.getZ())) return true;
		}
		
		return false;
	}
	private boolean checkIsWestOfCubeInShadow(PhysicCube cube) {
		for (int i = (int) cube.getY(); i < 16-cube.getY(); i++){
			if (isOpaqueCube((int)cube.getX()+1, i, (int)cube.getZ())) return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * @see GameState#enter(GameWindow)
	 */
	public void enter(GameWindow window) {
		
		if (isMultiplayer){
			if (!nethandler.isRunning) nethandler.start();
			entities.clear();
		}
		
		player[cam].health = 10;
		score = 0;
		level = 0;
		gameOver = false;
		lagStop = false;		
		
		//Model testModel = new HumanoidModel();
		
		/*lThread = new RenderThread(entities);
		
		lightThread = new Thread(lThread);
		lightThread.start();*/
		
		
	}

	public void spawnPlayer(){
		player[cam] = new Human(shipTexture, shipModel, createVector(5, -5, 1));
	}

	public boolean isCube(int x, int y, int z){
		/*for(int i = 0; i < entities.size(); i++){
			if (entities.get(i) instanceof Cube){
				Cube cube = (Cube) entities.get(i);
				if (cube.getX() == x && cube.getY() == y && cube.getZ() == z) return true;
			}
		}
		return false;*/
		return cubeMap.isCube(x, y, z);
	}
	
	public boolean isOpaqueSolidCube(int x, int y, int z){
		/*for(int i = 0; i < entities.size(); i++){
			if (entities.get(i) instanceof Cube){
				Cube cube = (Cube) entities.get(i);
				if (cube.getX() == x && cube.getY() == y && cube.getZ() == z && !cube.getType().isNonOpaque() && cube.getType().isSolid()) return true;
			}
		}
		return false;*/
		return cubeMap.isOpaqueSolidCube(x, y, z) && !getIsHalfCube(x, y, z);
	}
	
	public boolean isSolidCube(int x, int y, int z){
		/*for(int i = 0; i < entities.size(); i++){
			if (entities.get(i) instanceof Cube){
				Cube cube = (Cube) entities.get(i);
				if (cube.getX() == x && cube.getY() == y && cube.getZ() == z && cube.getType().isSolid()) return true;
			}
		}
		return false;*/
		return cubeMap.isSolidCube(x, y, z);
	}
	
	public boolean isOpaqueCube(int x, int y, int z){
		/*for(int i = 0; i < entities.size(); i++){
			if (entities.get(i) instanceof Cube){
				Cube cube = (Cube) entities.get(i);
				if (cube.getX() == x && cube.getY() == y && cube.getZ() == z && !cube.getType().isNonOpaque()) return true;
			}
		}
		return false;*/
		return cubeMap.isOpaqueCube(x, y, z);
	}
	
	public boolean isLiquid(int x, int y, int z){
		/*for(int i = 0; i < entities.size(); i++){
			if (entities.get(i) instanceof Cube){
				Cube cube = (Cube) entities.get(i);
				if (cube.getX() == x && cube.getY() == y && cube.getZ() == z && !cube.getType().isNonOpaque()) return true;
			}
		}
		return false;*/
		return CubeType.getTypeFromNumber(cubeMap.getCubeType(x, y, z)).isLiquid();
	}
	
	public NPC isNPC(int x, int y, int z){
		for(int i = 0; i < ((EntitiedLevel)cubeMap).entities.size(); i++){
			if (((EntitiedLevel)cubeMap).entities.get(i) instanceof NPC){
				NPC ent = (NPC) entities.get(i);
				BoundingBox bb = new BoundingBox(ent, 0.4f, false);
				BoundingBox cube = new BoundingBox((new PhysicCube(x, y, z, true)), 1, true);
				if (bb.intersectsWith(cube)) {
					ent.positionX = -ent.positionX;
					ent.positionY = -ent.positionY;
					ent.positionZ = -ent.positionZ;
					return ent;
				}
			}
		}
		return null;
	}
	
	/**
	 * @see GameState#leave(GameWindow)
	 */
	public void leave(GameWindow window) {
		try {
			BCCHelper.createBCCFile(Messages.getString("options.save.saveFile"), cubeMap); //$NON-NLS-1$
			BCCHelper.writeBCCPlayerFile(Messages.getString("options.save.playerSaveFile"), new HumanMPPacket(player[cam].getPositionX(), player[cam].getPositionY(), player[cam].getPositionZ(), player[cam].getID())); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Works only with cubes.
	 * Sends cube to server too.
	 * 
	 * @see Cube
	 * @see EntityManager#addEntity(Entity)
	 */
	public void removeEntity(Entity entity) {
		if (entity instanceof Cube){
			if (cubeMap.getCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ()) != Cube.PERMANENT){
				cubeMap.setCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), 0);
				createShadowsAndConfRenderAt((int)entity.getX(), (int)entity.getY(), (int)entity.getZ());
				cubeMap.tick();
			}
		}
		if (entity instanceof PhysicCube){
			if (cubeMap.getCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ()) != Cube.PERMANENT){
				if (isMultiplayer){ 
					nethandler.addPacketToSendQueue(
						new CubeChangePacket((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), 0));
					return;
				}
				cubeMap.setCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), 0);
				createShadowsAndConfRenderAt((int)entity.getX(), (int)entity.getY(), (int)entity.getZ());
				
				cubeMap.tick();
			}
		}
		if (entity instanceof NPC){
			removeList.add(entity);
		}
	}

	/**
	 * Works only with cubes.
	 * Sends cube to server too.
	 * 
	 * @see Cube
	 * @see EntityManager#addEntity(Entity)
	 */
	public void addEntity(Entity entity) {
		
		if (entity instanceof Cube){
			cubeMap.setCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), ((Cube)entity).getType().getId());
			createShadowsAndConfRenderAt((int)entity.getX(), (int)entity.getY(), (int)entity.getZ());
			cubeMap.tick();
		}
		if (entity instanceof PhysicCube){
			if (cubeMap.getCubeType((int)entity.getX(), (int)entity.getY()-1, (int)entity.getZ()) == Cube.GRASS){
				cubeMap.setCubeType((int)entity.getX(), (int)entity.getY()-1, (int)entity.getZ(), Cube.DIRT);
			}
			if (isMultiplayer) {
				nethandler.addPacketToSendQueue(
					new CubeChangePacket((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(),
							((Cube)entity).getType().getId()));
				return;
			}
			cubeMap.setCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), ((PhysicCube)entity).getType().getId());
			if (entity instanceof HalfPhysicCube) setIsHalfCube((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), true);
			createShadowsAndConfRenderAt((int)entity.getX(), (int)entity.getY(), (int)entity.getZ());
			cubeMap.tick();
		}
		
		if (entity instanceof NPC && !isMultiplayer){
			addList.add(entity);
		}
	}
	
	/**
	 * Works only with cubes.
	 * No multiplayer restrict.
	 * 
	 * @see Cube
	 * @see EntityManager#addEntity(Entity)
	 */
	public void removeCube(Entity entity) {
		if (entity instanceof Cube){
			if (cubeMap.getCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ()) != Cube.PERMANENT){
				cubeMap.setCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), 0);
				createShadowsAndConfRenderAt((int)entity.getX(), (int)entity.getY(), (int)entity.getZ());
				cubeMap.tick();
			}
		}
		if (entity instanceof PhysicCube){
			if (cubeMap.getCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ()) != Cube.PERMANENT){
				cubeMap.setCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), 0);
				createShadowsAndConfRenderAt((int)entity.getX(), (int)entity.getY(), (int)entity.getZ());
				cubeMap.tick();
			}
		}
		if (entity instanceof NPC){
			removeList.add(entity);
		}
	}

	/**
	 * Works only with cubes.
	 * No multiplayer restrict.
	 * 
	 * @see Cube
	 * @see EntityManager#addEntity(Entity)
	 */
	public void addCube(Entity entity) {
		
		if (entity instanceof Cube){
			cubeMap.setCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), ((Cube)entity).getType().getId());
			createShadowsAndConfRenderAt((int)entity.getX(), (int)entity.getY(), (int)entity.getZ());
			cubeMap.tick();
		}
		if (entity instanceof PhysicCube){
			if (cubeMap.getCubeType((int)entity.getX(), (int)entity.getY()-1, (int)entity.getZ()) == Cube.GRASS){
				cubeMap.setCubeType((int)entity.getX(), (int)entity.getY()-1, (int)entity.getZ(), Cube.DIRT);
			}
			cubeMap.setCubeType((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), ((PhysicCube)entity).getType().getId());
			createShadowsAndConfRenderAt((int)entity.getX(), (int)entity.getY(), (int)entity.getZ());
			cubeMap.tick();
		}
		
		if (entity instanceof NPC){
			addList.add(entity);
		}
	}

	/**
	 * @see EntityManager#playerHit()
	 */
	public void playerHit() {
		player[cam].health--;
		if (player[cam].health == 0) heart.play(1.0f, 1.0f);
		if (player[cam].health < 0) {
			if (deaths == 0) {
				deaths = 1;
				player[cam].setVelocityY(0);
				return;
			}
			deaths++;
			gameOver = true;
			gameOverTimeout = 6000;
		}
	}
	
	private FloatBuffer createColor(float f, float f1, float f2, float f3)
	  {
	    this.fogColor.clear();
	    this.fogColor.put(f).put(f1).put(f2).put(f3);
	    this.fogColor.flip();
	    return this.fogColor;
	  }
	
	public boolean canEntitySeePlayer(Entity entity){
		if (entity == null) return false;
		Vector3f ent = new Vector3f(entity.getX(), entity.getY(), entity.getZ());
		Vector3f play = new Vector3f(player[cam].getX(), player[cam].getY(), player[cam].getZ());
		//if (ent.getX() + play.getX() > FOG_DISTANCE || ent.getY() + play.getY() > FOG_DISTANCE || ent.getZ() + play.getZ() > FOG_DISTANCE) return false;
		float x;
		float y;
		float z;
		if (ent.getX() > play.getX()) x = ent.getX() - -play.getX(); else x = -play.getX() - ent.getX();
		if (ent.getY() > play.getY()) y = ent.getY() - -play.getY(); else y = -play.getY() - ent.getY();
		if (ent.getZ() > play.getZ()) z = ent.getZ() - -play.getZ(); else z = -play.getZ() - ent.getZ();
		float hypot = (float) Math.hypot(x, z);
		float hypot2 = (float) Math.hypot(x, y);
		if (hypot > FOG_DISTANCE+2) return false;
		return true;
	}
	
	public static float getDistance(Vector3f ent1, Vector3f ent2){
		float x1 = ent1.x;
		float y1 = ent1.y;
		float z1 = ent1.z;
		
		float x2 = ent2.x;
		float y2 = ent2.y;
		float z2 = ent2.z;
		
		float distance;
		distance = (float) Math.sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2))+((z1-z2)*(z1-z2)));
		
		return distance;
	}
	
	public static boolean isNegative(float f){
		if (Float.toString(f).startsWith("-")) return true; //$NON-NLS-1$
		return false;
	}

	public Vector3f rayTrace(){
		Vector3f vec1 = new Vector3f(0, 0, 0);
		Vector3f vec2 = new Vector3f(1, 0, 0);
		Vector3f vec3 = new Vector3f(0, 1, 0);
		
		
		
		return null;
		
	}
	
	public boolean checkCollision(Vector3f pos, Vector3f newpos){
		//if (entities != null){
			for (int i = 0; i < entities.size(); i++){
				if ((int)Math.floor(newpos.x) == (int)((Entity)entities.get(i)).getX() && 
						(int)Math.floor(newpos.y-1) == (int)((Entity)entities.get(i)).getY() && 
						(int)Math.floor(newpos.z) == (int)((Entity)entities.get(i)).getZ() && !(entities.get(i) instanceof Human)){
					return true;
				}
				else if ((int)Math.floor(newpos.x) == (int)((Entity)entities.get(i)).getX() && 
						(int)Math.floor(newpos.y-2) == (int)((Entity)entities.get(i)).getY() && 
						(int)Math.floor(newpos.z) == (int)((Entity)entities.get(i)).getZ() && !(entities.get(i) instanceof Human)){
					
					return true;
				}
				
			}
			//if (isCube((int)Math.floor(newpos.x), (int)Math.floor(newpos.y), (int)Math.floor(newpos.z))) return true;
		//	return false;
		//}
		return false;
	}
	
	public static Vector3f createVector(float posX, float posY,
			float posZ) {
		return new Vector3f(posX, posY, posZ);
	}

	public ArrayList<BoundingBox> canMove(float x, float y, float z, float height, boolean isNPC) {
		Creature ent = new Creature(x, y, z, 0.4f, null);
		BoundingBox box = new BoundingBox(ent, height, false);
		ArrayList<BoundingBox> bbList = new ArrayList<BoundingBox>();
		//if (entities == null){
			for (int xp = (int) (-player[cam].getPositionX())-2; xp < (-player[cam].getPositionX())+2; xp++){
				for (int yp = (int) (-player[cam].getPositionY())-3; yp < (-player[cam].getPositionY())+2; yp++){
					for (int zp = (int) (-player[cam].getPositionZ())-2; zp < (-player[cam].getPositionZ())+2; zp++){
						PhysicCube entity = new PhysicCube(xp, yp, zp, cubeMap.getCubeType(xp, yp, zp), 1);
				
				//if (entity == null) continue;
				//if(entity instanceof Cube){
					BoundingBox cube = new BoundingBox(entity, (float)1, true);
					if (box.intersectsWith(cube) && (entity).getType().isSolid()){
						
						bbList.add(cube);
					}
				//}
				/*if(entity instanceof Creature && (!(entity instanceof NPC && isNPC))){
					BoundingBox creature = new BoundingBox((Creature) entity, ((Creature) entity).getSize()/4, true);
					if (box.intersectsWith(creature)) return creature;
				}*/
			}}}
		//}
		return bbList;
		//return entities != null ? cubeMap.canMove(x, y, z, height) : null;
	}
	
	public BoundingBox canMoveBig(float x, float y, float z, float height, boolean isNPC) {
		Creature ent = new Creature(x, y, z, 0.7f, null);
		BoundingBox box = new BoundingBox(ent, height, false);
		//if (entities == null){
			for (int xp = (int) (-player[cam].getPositionX())-2; xp < (-player[cam].getPositionX())+2; xp++){
				for (int yp = (int) (-player[cam].getPositionY())-3; yp < (-player[cam].getPositionY())+2; yp++){
					for (int zp = (int) (-player[cam].getPositionZ())-2; zp < (-player[cam].getPositionZ())+2; zp++){
						PhysicCube entity = new PhysicCube(xp, yp, zp, cubeMap.getCubeType(xp, yp, zp), 1);
				if (entity == null) continue;
				//if(entity instanceof Cube){
					BoundingBox cube = new BoundingBox(entity, (float)1, true);
					if (box.intersectsWith(cube) && (entity).getType().isSolid()){
						
						return cube;
					}
				//}
				/*if(entity instanceof Creature && (!(entity instanceof NPC && isNPC))){
					BoundingBox creature = new BoundingBox((Creature) entity, ((Creature) entity).getSize()/4, true);
					if (box.intersectsWith(creature)) return creature;
				}*/
			}}}
		//}
		return null;
		//return entities != null ? cubeMap.canMove(x, y, z, height) : null;
	}
	
	public float getLiquidDensity(float x, float y, float z, float height) {
		Creature ent = new Creature(x, y, z, 0.4f, null);
		BoundingBox box = new BoundingBox(ent, height, false);
		//if (entities == null){
			for (int xp = (int) (-player[cam].getPositionX())-2; xp < (-player[cam].getPositionX())+2; xp++){
				for (int yp = (int) (-player[cam].getPositionY())-2; yp < (-player[cam].getPositionY())+2; yp++){
					for (int zp = (int) (-player[cam].getPositionZ())-2; zp < (-player[cam].getPositionZ())+2; zp++){
						PhysicCube entity = new PhysicCube(xp, yp, zp, cubeMap.getCubeType(xp, yp, zp), 1);
				//if(entity instanceof Cube){
					BoundingBox cube = new BoundingBox(entity, (float)1, true);
					if (box.intersectsWith(cube) && (entity).getType().isLiquid()) return 0.004f;
				//}
			}}}
		//}
		return 0.009f;
		//return player != null ? cubeMap.getLiquidDensity(x, y, z, height) : 0.009f;
	}
	
	public boolean isPlayerInsideOfLiquid() {
		Creature ent = new Creature(-player[cam].getPositionX(), -player[cam].getPositionY(), -player[cam].getPositionZ(), 0.4f, null);
		BoundingBox box = new BoundingBox(ent, 2, false);
		//if (entities == null){
			for (int xp = (int) (-player[cam].getPositionX())-2; xp < (-player[cam].getPositionX())+2; xp++){
				for (int yp = (int) (-player[cam].getPositionY())-3; yp < (-player[cam].getPositionY())+3; yp++){
					for (int zp = (int) (-player[cam].getPositionZ())-2; zp < (-player[cam].getPositionZ())+2; zp++){
						PhysicCube entity = new PhysicCube(xp, yp, zp, cubeMap.getCubeType(xp, yp, zp), 1);
				//if(entity instanceof Cube){
					BoundingBox cube = new BoundingBox(entity, (float)1, true);
					if (box.intersectsWith(cube) && (entity).getType().isLiquid()) return true;
				//}
			}}}
		//}
		return false;
		//return player != null ?  cubeMap.isPlayerInsideOfLiquid(player) : null;
	}
	
	public boolean isPlayersEyeInsideOfLiquid() {
		/*Creature ent = new Creature(-player.getPositionX(), -player.getPositionY(), -player.getPositionZ(), 0.4f, null);
		BoundingBox box = new BoundingBox(ent, 0.0f, false);
		if (entities != null){
			for (int i = 0; i < entities.size(); i++){
				if(entities.get(i) instanceof Cube){
					BoundingBox cube = new BoundingBox((Cube) entities.get(i), (float)1, true);
					if (box.intersectsWith(cube) && ((Cube)entities.get(i)).getType().isLiquid()) return true;
				}
			}
		}
		return false;*/
		if (player != null){
			return cubeMap.isPlayerInsideOfLiquid(player[cam]);
		}
		return false;
	}

	public void addCube(float x, float y, float z, CubeType cubeType) {
		switch (cubeType){
		case selectFrame:
			return;
		default:
			PhysicCube entity = new PhysicCube(x, y, z, cubeType);
			if (isMultiplayer) {
				nethandler.addPacketToSendQueue(
						new CubeChangePacket((int)entity.getX(), (int)entity.getY(), (int)entity.getZ(), cubeType.getId()));
				return;
			}
					
			addEntity(entity);	
		}
	}
	public void addHalfCube(float x, float y, float z, CubeType cubeType) {
		if (isMultiplayer) return;
		switch (cubeType){
		case selectFrame:
			return;
		default:
			addEntity(new HalfPhysicCube(x, y, z, cubeType));	
		}
	}

	public void finalize(){
		try {
			logger.save();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getDrawTop(int x, int y, int z) {
		return mapData[getArrayIndexFromCoords(x, y, z)][0];
	}
	public boolean getDrawBottom(int x, int y, int z) {
		return mapData[getArrayIndexFromCoords(x, y, z)][1];
	}
	public boolean getDrawNorth(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][2];
	}
	public boolean getDrawSouth(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][3];
	}
	public boolean getDrawEast(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][4];
	}
	public boolean getDrawWest(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][5];
	}

	
	public boolean getShadowOfTop(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][6];
	}
	public boolean getShadowOfNorth(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][7];
	}
	public boolean getShadowOfSouth(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][8];
	}
	public boolean getShadowOfEast(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][9];
	}
	public boolean getShadowOfWest(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][10];
	}
	
	public boolean getIsHalfCube(int x, int y, int z) {
		
		return mapData[getArrayIndexFromCoords(x, y, z)][11];
	}
	
	public void setIsHalfCube(int x, int y, int z, boolean is) {
		
		mapData[getArrayIndexFromCoords(x, y, z)][11] = is;
	}

	

	public int getTextureOfCube(int x, int y, int z) {
		return textureMap[getArrayIndexFromCoords(x, y, z)];
	}
	
	public void testCollision(Vector3f pos, Vector3f speed, Vector3f bbox)
	  {
	    float xdir = 1.0F;
	    float ydir = 1.0F;
	    float zdir = 1.0F;

	    if (speed.x != 0.0F) {
	      xdir = speed.x / Math.abs(speed.x);
	    }
	    if (speed.y != 0.0F) {
	      ydir = speed.y / Math.abs(speed.y);
	    }
	    if (speed.x != 0.0F) {
	      zdir = speed.z / Math.abs(speed.z);
	    }

	    int xp = (int)Math.floor(pos.x + xdir * bbox.x);
	    int yp = (int)Math.floor(pos.y - bbox.y);
	    int yp2 = (int)Math.floor(pos.y + bbox.y);
	    int zp = (int)Math.floor(pos.z + zdir * bbox.z);

	    int xi1 = 
	      (int)Math.floor(pos.x + speed.x + xdir * bbox.x + xdir * 0.05f);
	    int yi1 = (int)Math.floor(pos.y + speed.y + bbox.y + 0.05f);
	    int zi1 = 
	      (int)Math.floor(pos.z + speed.z + zdir * bbox.z + zdir * 0.05f);

	    if (((inRange(xi1, yp, zp)) && (cubeMap.getCubeType(xi1, yp, zp) != 0)) || (
	      (inRange(xi1, yp2, zp)) && (cubeMap.getCubeType(xi1, yp2, zp) != 0)))
	    {
	      speed.x = 0.0F;
	      float t = 0.0F;
	      if (xdir == -1.0F) {
	        t = 1.0F;
	      }
	      pos.x = (xi1 + t - xdir * bbox.x - xdir * 0.05f);
	    }

	    if (((inRange(xp, yp, zi1)) && (cubeMap.getCubeType(xp, yp, zi1) != 0)) || (
	      (inRange(xp, yp2, zi1)) && (cubeMap.getCubeType(xp, yp2, zi1) != 0)))
	    {
	      speed.z = 0.0F;
	      float t = 0.0F;
	      if (zdir == -1.0F) {
	        t = 1.0F;
	      }
	      pos.z = (zi1 + t - zdir * bbox.z - zdir * 0.05f);
	    }

	    if ((inRange(xp, yi1, zp)) && (cubeMap.getCubeType(xp, yi1, zp) != 0))
	    {
	      speed.y = 0.0F;
	      float t = 0.0F;
	      if (ydir == -1.0F) {
	        t = 1.0F;
	      }
	      pos.y = (yi1 + t - ydir * bbox.y - ydir * 0.05f);
	    }
	  }

	private boolean inRange(int x, int y, int z) {
		if (x < 0 && x > SIZE) return false;
		if (y < 0 && y > SIZE) return false;
		if (z < 0 && z > SIZE) return false;
		return true;
	}
}

class LightThread implements Runnable{

	private ArrayList<Entity> entityArray;

	public LightThread(ArrayList<Entity> entityArray) {
		this.entityArray = entityArray;
	}
	
	public void run() {
		for (int i=0;i<entityArray.size();i++) {
			Entity entity = (Entity) entityArray.get(i);
			if (entity instanceof Cube){
				int lightLevel = 0;
				for (int x = (int) (entity.getX()-10); x < entity.getX()+10; x++){
					for (int y = (int) (entity.getY()-10); y < entity.getY()+10; y++){
						for (int z = (int) (entity.getZ()-10); z < entity.getZ()+10; z++){
							if (isLight(x, y, z) && lightLevel < CubeState.getDistance(CubeState.createVector(x, y, z), 
									CubeState.createVector(entity.getX(), entity.getY(), entity.getZ()))) 
								lightLevel = (int) CubeState.getDistance(CubeState.createVector(x, y, z), 
										CubeState.createVector(entity.getX(), entity.getY(), entity.getZ()));
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
		
						}
					}
				}
				((Cube) entity).setLightLevel(0.5f - lightLevel);
			}
		}
	}
	
	public boolean isLight(int x, int y, int z){
		for(int i = 0; i < entityArray.size(); i++){
			if (entityArray.get(i) instanceof Cube){
				Cube ent = (Cube) entityArray.get(i);
				if ((int)ent.getX() == x && (int)ent.getY() == y && (int)ent.getZ() == z && ent.getType() == CubeType.light) return true;
			}
		}
		return false;
	}
	
	
}

class RenderThread implements Runnable{

	private HashMap<String, Entity> entityArray = new HashMap<String, Entity>();

	public RenderThread(HashMap<String, Entity> entityArray) {
		this.entityArray = entityArray;
	}
	
	public void run() {
		/*for (int xp = (int) (-CubeState.HALF_SIZE); xp < CubeState.HALF_SIZE; xp++){
			for (int yp = (int) (CubeState.HALF_SIZE); yp < CubeState.HALF_SIZE; yp++){
				for (int zp = (int) (CubeState.HALF_SIZE); zp < CubeState.HALF_SIZE; zp++){
					String key = xp+"x"+yp+"x"+zp;
					Entity entity = (Entity) entityArray.get(key);
			
			//if (!isInWater) GL11.glColor3f(1, 1, 1); else GL11.glColor3f(0.5f, 0.5f, 0.9f);
			if (true){//canEntitySeePlayer(entity)){
				if (entity instanceof Cube){
					entity.render();	
				}
			}
			
			if (entity instanceof Human) 
				{
				
				}
		}}}*/
	}
}