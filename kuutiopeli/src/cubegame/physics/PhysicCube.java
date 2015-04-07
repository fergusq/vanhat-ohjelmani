package cubegame.physics;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import cubegame.CubeState;
import cubegame.CubeType;
import cubegame.Entity;
import cubegame.EntityManager;
import cubegame.Human;
import cubegame.shading.ShadeHelper;

public class PhysicCube implements Entity{
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
	
	public static final int BOTTOM_FACE = 0;
	public static final int TOP_FACE = 1;
	public static final int NORTH_FACE = 2;
	public static final int SOUTH_FACE = 3;
	public static final int EAST_FACE = 4;
	public static final int WEST_FACE = 5;
	
	public PhysicCube(float x, float y, float z) {
		X = x;
		Y = y;
		Z = z;
		
		setType(CubeType.net);
	}
	public PhysicCube(float x, float y, float z, boolean isSelectFrame) {
		X = x;
		Y = y;
		Z = z;
		
		setType(CubeType.unknow);
		
		this.isSelectFrame  = isSelectFrame;
		
		if (isSelectFrame) setType(CubeType.selectFrame);
	}
	
	public PhysicCube(float x, float y, float z, CubeType type) {
		X = x;
		Y = y;
		Z = z;
		
		this.isSelectFrame = false;
		this.setType(type);
	}
	
	public PhysicCube(float x, float y, float z, int type, float size) {
		X = x;
		Y = y;
		Z = z;
		setType(CubeType.getTypeFromNumber(type));
		this.size  = size;
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
		
		GL11.glPolygonMode(GL11.GL_FRONT_FACE, GL11.GL_POLYGON_OFFSET_LINE);
		
		/*if(useShader) {
        //    ARBShaderObjects.glUseProgramObjectARB(shader);
        }*/
		
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
	public static final int SELECT_FRAME = 101;
	public static final int UNKNOW = 102;
	public static final int PERMANENT = 103;
	
	public void finalize(){
		
	}
}
