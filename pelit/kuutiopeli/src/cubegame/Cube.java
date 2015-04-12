package cubegame;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import cubegame.Texture;
import cubegame.TextureLoader;
import cubegame.shading.ShadeHelper;

/**
 * @author IH
 * */
public class Cube implements Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float X;
	private float Y;
	private float Z;

	private int shader=0;
    private int vertShader=0;
    private int fragShader=0;

	
	private Texture texture;
	private Texture topTexture;
	private Texture bottomTexture;
	private boolean isSelectFrame = false;
	private CubeType type;
	private boolean useShader;
	
	private boolean topInShadow;
	private boolean eastInShadow;
	private boolean westInShadow;
	private boolean northInShadow;
	private boolean southInShadow;
	
	private float lightLevel ;
	
	public boolean isDrawTop() {
		return drawTop;
	}
	public void setDrawTop(boolean drawTop) {
		this.drawTop = drawTop;
	}
	public boolean isDrawBottom() {
		return drawBottom;
	}
	public void setDrawBottom(boolean drawBottom) {
		this.drawBottom = drawBottom;
	}
	public boolean isDrawNorth() {
		return drawNorth;
	}
	public void setDrawNorth(boolean drawNorth) {
		this.drawNorth = drawNorth;
	}
	public boolean isDrawSouth() {
		return drawSouth;
	}
	public void setDrawSouth(boolean drawSouth) {
		this.drawSouth = drawSouth;
	}
	public boolean isDrawEast() {
		return drawEast;
	}
	public void setDrawEast(boolean drawEast) {
		this.drawEast = drawEast;
	}
	public boolean isDrawWest() {
		return drawWest;
	}
	public void setDrawWest(boolean drawWest) {
		this.drawWest = drawWest;
	}

	private boolean drawTop = true;
	private boolean drawBottom = true;
	private boolean drawNorth = true;
	private boolean drawSouth = true;
	private boolean drawEast = true;
    private boolean drawWest = true;
	private boolean cullFace = true;
	private float size = 1;
	
	private static Texture blackAndYellow;
	private static Texture dirt;
	private static Texture glass;
	private static Texture grass;
	public static Texture sideOfGrass;
	private static Texture metal1;
	private static Texture metal2;
	private static Texture net;
	private static Texture selectFrame;
	private static Texture snow;
	private static Texture stone1;
	private static Texture stone2;
	private static Texture wood;
	private static Texture permanent;
	private static Texture water;
	private static Texture lava;
	private static Texture acid;
	private static Texture light;
	private static Texture sign;
	private static Texture fire;
	private static Texture tree;
	public static Texture topOfTree;
	private static Texture leaves;
	
	public static final int BOTTOM_FACE = 0;
	public static final int TOP_FACE = 1;
	public static final int NORTH_FACE = 2;
	public static final int SOUTH_FACE = 3;
	public static final int EAST_FACE = 4;
	public static final int WEST_FACE = 5;
	
	static TextureLoader loader = new TextureLoader();
	
	
	static{
		try {
		blackAndYellow = loader.getTexture("res/textures/varoitusraita.png");
		dirt = loader.getTexture("res/textures/maa.png");
		glass = loader.getTexture("res/textures/lasi.png");
		grass = loader.getTexture("res/textures/ruoho.png");
		sideOfGrass = loader.getTexture("res/textures/nurmi-sivu.png");
		metal1 = loader.getTexture("res/textures/metalli-1.png");
		metal2 = loader.getTexture("res/textures/metalli-2.png");
		net = loader.getTexture("res/cube.png");
		selectFrame = loader.getTexture("res/frame.png");
		snow = loader.getTexture("res/textures/lumi.png");
		stone1 = loader.getTexture("res/textures/kivi-1.png");
		stone2 = loader.getTexture("res/textures/kivi-2.png");
		wood = loader.getTexture("res/textures/puu.png");
		permanent = loader.getTexture("res/textures/perma.png");
		water = loader.getTexture("res/textures/vesi.png");
		lava = loader.getTexture("res/textures/tex4000/laava.png");
		acid = loader.getTexture("res/textures/myrkky.png");
		light = loader.getTexture("res/textures/valo.png");
		sign = loader.getTexture("res/textures/video.gif");
		fire = loader.getTexture("res/textures/tex4000/tuli.png");
		tree = loader.getTexture("res/textures/tex4000/puunrunko.png");
		topOfTree = loader.getTexture("res/textures/tex4000/puunrunko-ylä-ja-alaosa.png");
		leaves = loader.getTexture("res/textures/tex4000/lehdet.png");
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public Cube(float x, float y, float z) {
		X = x;
		Y = y;
		Z = z;
		
		texture = net; this.bottomTexture = net; this.topTexture = net;
		
		setType(CubeType.net);
		
		initShader();
	}
	public Cube(float x, float y, float z, Texture texture, boolean isSelectFrame) {
		X = x;
		Y = y;
		Z = z;
		
		setType(CubeType.unknow);
		
		this.texture = texture; this.bottomTexture = texture; this.topTexture = texture;
		
		this.isSelectFrame  = isSelectFrame;
		
		if (isSelectFrame) setType(CubeType.selectFrame);
		
		//initShader();
	}
	
	public Cube(float x, float y, float z, CubeType type) {
		X = x;
		Y = y;
		Z = z;
		
		this.isSelectFrame = false;
		this.setType(type);
		try {
			this.texture = getTextureOfType(type);
			this.bottomTexture = getBottomTextureOfType(type);
			this.topTexture = getTopTextureOfType(type);
		} catch (IOException e) {
			e.printStackTrace();
		}

		initShader();
	}
	
	public Cube(float x, float y, float z, int type, float size) {
		X = x;
		Y = y;
		Z = z;
		setType(CubeType.getTypeFromNumber(type));
		this.size  = size;
	}
	
	
	
	private void initShader(){
		/*
		 * create the shader program. If OK, create vertex and fragment shaders
		 */
		shader = ARBShaderObjects.glCreateProgramObjectARB();

		if (shader != 0) {
			vertShader = ShadeHelper.createVertShader("screen.vert");
			fragShader = ShadeHelper.createFragShader("screen.frag");
		} else
			useShader = false;

		/*
		 * if the vertex and fragment shaders setup sucessfully, attach them to
		 * the shader program, link the sahder program (into the GL context I
		 * suppose), and validate
		 */
		if (vertShader != 0 && fragShader != 0) {
			ARBShaderObjects.glAttachObjectARB(shader, vertShader);
			ARBShaderObjects.glAttachObjectARB(shader, fragShader);
			ARBShaderObjects.glLinkProgramARB(shader);
			ARBShaderObjects.glValidateProgramARB(shader);
			useShader = ShadeHelper.printLogInfo(shader);
		} else
			useShader = false;
		
		lightLevel = 0.5f;
		size = 1;
	}

	@SuppressWarnings("unused")
	private void initTexture(){
		ByteBuffer data=ByteBuffer.allocateDirect((int) (4*texture.getWidth()*texture.getHeight()));
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
		GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
		GL11.GL_NEAREST);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, (int)texture.getWidth(), (int)texture.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
	}
	
	public static cubegame.Texture getTextureOfType(CubeType type) throws IOException {
		TextureLoader loader = new TextureLoader();
		if (type == null)
		{
			return net;
		}
		switch(type){
		case blackAndYellow:
			return blackAndYellow;
		case dirt:
			return dirt;
		case glass:
			return glass;
		case grass:
			return grass;
		case metal1:
			return metal1;
		case metal2:
			return metal2;
		case net:
			return net;
		case selectFrame:
			return selectFrame;
		case snow:
			return snow;
		case stone1:
			return stone1;
		case stone2:
			return stone2;
		case wood:
			return wood;
		case permanent:
			return permanent;
		case water:
			return water;
		case lava:
			return lava;
		case acid:
			return acid;
		case light:
			return light;
		case sign:
			return sign;
		case fire:
			return fire;
		case tree:
			return tree;
		case leaves:
			return leaves;
		case unknow:
			return net;
		default:
			return net;
		}
	}
	public static cubegame.Texture getTopTextureOfType(CubeType type) throws IOException {
		switch(type){
		case sign:
			return sign;
		}
		return getTextureOfType(type);
	}
	public static cubegame.Texture getBottomTextureOfType(CubeType type) throws IOException {
		switch(type){
		case sign:
			return glass;
		}
		return getTextureOfType(type);
	}
	@Override
	public void collide(EntityManager manager, Entity other) {
		// TODO Auto-generated method stub
		if (other instanceof Human){
			/*Human player = (Human) other;
			if (other.getX() < X+1 && other.getX() > X) player.setMoveX(player.getPrevX());
			if (other.getZ() < Z+1 && other.getX() > Z) player.setMoveZ(player.getPrevZ());*/
		}
	}

	@Override
	public boolean collides(Entity other) {
		// TODO Auto-generated method stub
		if (other.getX() < X+1 && other.getX() > X-1 && 
				other.getY() < Y+1 && other.getY() > Y-1 && 
				other.getZ() < Z+1 && other.getZ() > Z-1) return true;
		
		float otherSize = other.getSize();
		float range = (otherSize + getSize());
		range *= range *= range;

		// Get the distance on X and Y between the two entities, then

		// find the squared distance between the two.

		float dx = getX() - other.getX();
		float dy = getY() - -other.getY();
		float dz = getZ() - other.getZ();
		float distance = (dx*dx)+(dy*dy)+(dz*dz);
		
		// if the squared distance is less than the squared range

		// then we've had a collision!

		if (distance <= range && other instanceof Human){
			/*Human player = (Human) other;
			if (-other.getZ() < Z+1 && -other.getZ() > Z-1) player.setPositionZ(player.getPrevZ());
			if (-other.getY() < Y+1 && -other.getY() > Y-1) player.setPositionY(player.getPrevY());
			if (-other.getX() < X+1 && -other.getX() > X-1) player.setPositionX(player.getPrevX());*/
		}
		
		return (distance <= range);
	}

	@Override
	public float getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return X;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return Y;
	}

	@Override
	public float getZ() {
		// TODO Auto-generated method stub
		return Z;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		GL11.glPushMatrix();
		
		GL11.glTranslatef(X, Y, Z);

		texture.bind();
		
		if(useShader) {
        //    ARBShaderObjects.glUseProgramObjectARB(shader);
        }
		
		/*int effectMap=ARBShaderObjects.glGetUniformLocationARB(shader, "effectMap");
		if(effectMap<1) System.out.println("Error accessing effectMap: " + effectMap);*/
		
		GL11.glEnable(GL11.GL_LIGHTING);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		if (isSelectFrame || getType().isLiquid()) GL11.glDisable(GL11.GL_CULL_FACE);
		
		if(isSelectFrame) GL11.glTranslatef(-0.1f, -0.1f, -0.1f);
		if(isSelectFrame) GL11.glScalef(1.2f, 1.2f, 1.2f);
		
		GL11.glBegin(4);

		if(drawTop || isSelectFrame){
			topTexture.bind();
		if (topInShadow) GL11.glColor3f(lightLevel, lightLevel, lightLevel); //Shadow in top face
		GL11.glNormal3f(0.0F, 1.0F, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(1.0F, 1.0F, 0.0F);

		GL11.glNormal3f(0.0F, 1.0F, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 0.0F);

		GL11.glNormal3f(0.0F, 1.0F, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 1.0F, 1.0F);

		GL11.glNormal3f(0.0F, 1.0F, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(1.0F, 1.0F, 0.0F);

		GL11.glNormal3f(0.0F, 1.0F, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 1.0F, 1.0F);

		GL11.glNormal3f(0.0F, 1.0F, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(1.0F, 1.0F, 1.0F);
		if (topInShadow) GL11.glColor3f(1f, 1f, 1f);
		}
		
		if(drawBottom || isSelectFrame){
			bottomTexture.bind();
		GL11.glColor3f(lightLevel, lightLevel, lightLevel); //Bottom face is always in shadow
		GL11.glNormal3f(0.0F, -1.0F, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(1.0F, 0.0F, 1.0F);

		GL11.glNormal3f(0.0F, -1.0F, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(0.0F, 0.0F, 1.0F);

		GL11.glNormal3f(0.0F, -1.0F, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 0.0F);

		GL11.glNormal3f(0.0F, -1.0F, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(1.0F, 0.0F, 1.0F);

		GL11.glNormal3f(0.0F, -1.0F, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 0.0F);
		
		GL11.glNormal3f(0.0F, -1.0F, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(1.0F, 0.0F, 0.0F);
		GL11.glColor3f(1f, 1f, 1f);
		}
		
		texture.bind();
		
		if(drawEast || isSelectFrame){
		if (eastInShadow) GL11.glColor3f(lightLevel, lightLevel, lightLevel); //Shadow in east face
		GL11.glNormal3f(-1.0F, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 1.0F);

		GL11.glNormal3f(-1.0F, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 0.0F);

		GL11.glNormal3f(-1.0F, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 0.0F);

		GL11.glNormal3f(-1.0F, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 1.0F);

		GL11.glNormal3f(-1.0F, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 0.0F);

		GL11.glNormal3f(-1.0F, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 1.0F);
		if (eastInShadow) GL11.glColor3f(1f, 1f, 1f);
		}
		
		if(drawWest || isSelectFrame){
		if (westInShadow) GL11.glColor3f(lightLevel, lightLevel, lightLevel); //Shadow in west face
		GL11.glNormal3f(1.0F, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(1.0F, 1.0F, 0.0F);

		GL11.glNormal3f(1.0F, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(1.0F, 1.0F, 1.0F);

		GL11.glNormal3f(1.0F, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(1.0F, 0.0F, 1.0F);

		GL11.glNormal3f(1.0F, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(1.0F, 1.0F, 0.0F);

		GL11.glNormal3f(1.0F, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(1.0F, 0.0F, 1.0F);

		GL11.glNormal3f(1.0F, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(1.0F, 0.0F, 0.0F);
		if (westInShadow) GL11.glColor3f(1f, 1f, 1f);
		}
		
		if (drawNorth || isSelectFrame){
		if (northInShadow) GL11.glColor3f(lightLevel, lightLevel, lightLevel); //Shadow in north face
		GL11.glNormal3f(0.0F, 0.0F, 1.0F); //North
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(1.0F, 1.0F, 1.0F);

		GL11.glNormal3f(0.0F, 0.0F, 1.0F); //North
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 1.0F);

		GL11.glNormal3f(0.0F, 0.0F, 1.0F); //North
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 1.0F);

		GL11.glNormal3f(0.0F, 0.0F, 1.0F); //North
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(1.0F, 1.0F, 1.0F);

		GL11.glNormal3f(0.0F, 0.0F, 1.0F); //North
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 1.0F);

		GL11.glNormal3f(0.0F, 0.0F, 1.0F); //North
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(1.0F, 0.0F, 1.0F);
		if (northInShadow) GL11.glColor3f(1f, 1f, 1f);
		}
		
		
		if(drawSouth || isSelectFrame){
		if (southInShadow) GL11.glColor3f(lightLevel, lightLevel, lightLevel); //Shadow in south face
		GL11.glNormal3f(0.0F, 0.0F, -1.0F); //South
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 0.0F);

		GL11.glNormal3f(0.0F, 0.0F, -1.0F); //South
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(1.0F, 1.0F, 0.0F);

		GL11.glNormal3f(0.0F, 0.0F, -1.0F); //South
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(1.0F, 0.0F, 0.0F);

		GL11.glNormal3f(0.0F, 0.0F, -1.0F); //South
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 0.0F);

		GL11.glNormal3f(0.0F, 0.0F, -1.0F); //South
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(1.0F, 0.0F, 0.0F);

		GL11.glNormal3f(0.0F, 0.0F, -1.0F); //South
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 0.0F);
		if (southInShadow) GL11.glColor3f(lightLevel, lightLevel, lightLevel);
		}
		
		GL11.glEnd();
		
		GL11.glColor3f(1, 1, 1);
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		//ARBShaderObjects.glUseProgramObjectARB(0);

		GL11.glPopMatrix();
	}

	public boolean isEastInShadow() {
		return eastInShadow;
	}
	public void setEastInShadow(boolean eastInShadow) {
		this.eastInShadow = eastInShadow;
	}
	public boolean isWestInShadow() {
		return westInShadow;
	}
	public void setWestInShadow(boolean westInShadow) {
		this.westInShadow = westInShadow;
	}
	public boolean isNorthInShadow() {
		return northInShadow;
	}
	public void setNorthInShadow(boolean northInShadow) {
		this.northInShadow = northInShadow;
	}
	public boolean isSouthInShadow() {
		return southInShadow;
	}
	public void setSouthInShadow(boolean southInShadow) {
		this.southInShadow = southInShadow;
	}
	
	@Override
	public void moveUpdate(EntityManager manager, int delta) {
		if (manager instanceof CubeState){
			//CubeState mng = (CubeState) manager;
			//if (mng.isPlayerInsideOfLiquid() && delta % 3 == 0) cullFace = false; else cullFace = true;
		}

	}
	
	public void setTopInShadow(boolean inShader) {
		this.topInShadow = inShader;
	}
	public boolean isTopInShadow() {
		return topInShadow;
	}
	public void setType(CubeType type) {
		this.type = type;
	}
	public CubeType getType() {
		return type;
	}
	
	public void setLightLevel(float lightLevel) {
		this.lightLevel = lightLevel;
	}
	public float getLightLevel() {
		return lightLevel;
	}

	public void finalize(){
		texture = null;
		loader = null;
	}
	
	public static final int AIR = 0;
	public static final int NET = 1;
	public static final int DIRT = 2;
	public static final int STONE_1 = 3;
	public static final int STONE_2 = 4;
	public static final int GLASS = 5;
	public static final int METAL_1 = 6;
	public static final int METAL_2 = 7;
	public static final int WOOD = 8;
	public static final int GRASS = 9;
	public static final int BLACK_AND_YELLOW = 10;
	public static final int SNOW = 11;
	public static final int WATER = 12;
	public static final int LAVA = 13;
	public static final int ACID = 14;
	public static final int LIGHT = 15;
	public static final int SIGN = 16;
	public static final int TREE = 17;
	public static final int LEAVES = 18;
	public static final int FIRE = 19;
	public static final int SELECT_FRAME = 101;
	public static final int UNKNOW = 102;
	public static final int PERMANENT = 103;
	public static final int SKYCUBE = 104;

}
