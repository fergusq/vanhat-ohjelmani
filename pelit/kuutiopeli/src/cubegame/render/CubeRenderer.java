package cubegame.render;

import java.io.IOException;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import cubegame.BoundingBox;
import cubegame.Cube;
import cubegame.CubeState;
import cubegame.CubeType;
import cubegame.Texture;
import cubegame.shading.ShadeHelper;

/**
 * CubeRenderer-class renders cubes.
 * It has few bugs...
 * 
 * @see cubegame.Cube#render()
 * @author iikka
 *
 */
public class CubeRenderer {
	
	private boolean useShader;
	private int vertShader;
	private int shader;
	private int fragShader;
	private RenderHelper renderHelper;
	
	public CubeRenderer(){
		//initShader();
		renderHelper = new RenderHelper(1.3f, 0.4f);
	}
	
	private void initShader(){
		useShader = true;
		
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
		
	}
	
	public void renderCube(int x, int y, int z, CubeType type) throws IOException{
		/*renderCube(x, y, z, Cube.getTextureOfType(type),
				false, type,
				true, true, true,
				true, true, true,
				false, false, false,
				false, false, 1f);*/
	}
	
	public void listCube(int x, int y, int z, CubeType type) throws IOException{
		listCube(x, y, z, Cube.getTextureOfType(type),
				false, type,
				true, true, true,
				true, true, true,
				false, false, false,
				false, false, 1f);
	}
	
	public void renderCube(int X, int Y, int Z, int texture,
			boolean drawAllFaces, CubeType type,
			boolean drawTop, boolean drawBottom, boolean drawEast,
			boolean drawWest, boolean drawNorth, boolean drawSouth,
			boolean topInShadow, boolean eastInShadow, boolean westInShadow,
			boolean northInShadow, boolean southInShadow, boolean ihc, float lightLevel) throws IOException {
		
		renderHelper.createLight();
		
		GL11.glPushMatrix();

		GL11.glTranslatef(X, Y, Z);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

		GL11.glTexEnvf( GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE );
		
		if (useShader) {
			ARBShaderObjects.glUseProgramObjectARB(shader);
		}

		/*
		 * int effectMap=ARBShaderObjects.glGetUniformLocationARB(shader,
		 * "effectMap"); if(effectMap<1)
		 * System.out.println("Error accessing effectMap: " + effectMap);
		 */

		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		if (drawAllFaces || type.isNonOpaque())
			GL11.glDisable(GL11.GL_CULL_FACE);

		if (type.getId() == Cube.AIR || type.getId() == Cube.LIGHT || type.getId() == Cube.SKYCUBE){
			topInShadow = false;
			northInShadow = false;
			southInShadow = false;
			eastInShadow = false;
			westInShadow = false;
			lightLevel = 1f;
			
		}
		
		renderHelper.createLight();
		
		if (type.getId() == Cube.TREE){
			int id = Cube.topOfTree.textureID;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		}
		
		GL11.glBegin(4);

		float topY = ihc ? 0.5f : 1f;
		
		if (drawTop || drawAllFaces) {
			// topTexture.bind();
			if (topInShadow){
				GL11.glEnd();
				renderHelper.createShadow();
				GL11.glBegin(4);
			} else  {
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
				GL11.glColor3f(lightLevel, lightLevel, lightLevel); // Shadow in
																	// top face
			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, topY, 0.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, topY, 0.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, topY, 1.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, topY, 0.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, topY, 1.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, topY, 1.0F);
			{
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
				GL11.glColor3f(1f, 1f, 1f);
		}
		
		if (type.getId() == Cube.GRASS){
			GL11.glEnd();
			
			int id = Cube.getTextureOfType(CubeType.dirt).textureID;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			
			GL11.glBegin(4);
		} else if (type.getId() == Cube.TREE){
			GL11.glEnd();
			
			int id = Cube.topOfTree.textureID;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			
			GL11.glBegin(4);
		} else
		if (type.getId() == Cube.SIGN){
			GL11.glEnd();
			
			int id = Cube.getTextureOfType(CubeType.selectFrame).textureID;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			
			GL11.glBegin(4);
		}
		
		if (drawBottom || drawAllFaces) {
			if (type.getId() != Cube.LIGHT && type.getId() != Cube.SKYCUBE && type.getId() != Cube.AIR){
				GL11.glEnd();
				renderHelper.createShadow();
				GL11.glBegin(4);
			} else  {
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
			GL11.glColor3f(lightLevel, lightLevel, lightLevel); // Bottom face
																// is always in
																// shadow
			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);
			
			{
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
			GL11.glColor3f(1f, 1f, 1f);
		}

		if (type.getId() == Cube.GRASS){
			GL11.glEnd();
			
			int id = Cube.sideOfGrass.textureID;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			
			GL11.glBegin(4);
		} if (type.getId() == Cube.TREE){
			GL11.glEnd();
			
			int id = Cube.getTextureOfType(CubeType.tree).textureID;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			
			GL11.glBegin(4);
		} else
			if (type.getId() == Cube.FIRE){
				GL11.glEnd();
				
				int id = Cube.getTextureOfType(CubeType.fire).textureID;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
				
				GL11.glBegin(4);
			}
		
		if (drawEast || drawAllFaces) {
			if (eastInShadow) {
				GL11.glEnd();
				renderHelper.createShadow();
				GL11.glBegin(4);
			} else  {
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
				GL11.glColor3f(lightLevel, lightLevel, lightLevel); // Shadow in
																	// east face
			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(0.0F, topY, 1.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, topY, 0.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(0.0F, topY, 1.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 1.0F);
			{
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
				GL11.glColor3f(1f, 1f, 1f);
		}

		if (drawWest || drawAllFaces) {
			if (westInShadow){
				GL11.glEnd();
				renderHelper.createShadow();
				GL11.glBegin(4);
			} else  {
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
				GL11.glColor3f(lightLevel, lightLevel, lightLevel); // Shadow in
																	// west face
			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, topY, 0.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(1.0F, topY, 1.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, topY, 0.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);
			{
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
				GL11.glColor3f(1f, 1f, 1f);
		}

		if (drawNorth || drawAllFaces) {
			if (northInShadow){
				GL11.glEnd();
				renderHelper.createShadow();
				GL11.glBegin(4);
			} else  {
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
				GL11.glColor3f(lightLevel, lightLevel, lightLevel); // Shadow in
																	// north
																	// face
			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, topY, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, topY, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, topY, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);
			{
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
				GL11.glColor3f(1f, 1f, 1f);
		}

		if (drawSouth || drawAllFaces) {
			if (southInShadow){
				GL11.glEnd();
				renderHelper.createShadow();
				GL11.glBegin(4);
			} else  {
				GL11.glEnd();
				renderHelper.createLight();
				GL11.glBegin(4);
			}
				GL11.glColor3f(lightLevel, lightLevel, lightLevel); // Shadow in
																	// south
																	// face
			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(0.0F, topY, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(1.0F, topY, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(0.0F, topY, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);
			{
				renderHelper.createLight();
			}
				GL11.glColor3f(lightLevel, lightLevel, lightLevel);
		}

		GL11.glEnd();

		GL11.glColor3f(1, 1, 1);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);

		// ARBShaderObjects.glUseProgramObjectARB(0);

		GL11.glPopMatrix();
	}
	
	public void listCube(int X, int Y, int Z, Texture texture,
			boolean drawAllFaces, CubeType type,
			boolean drawTop, boolean drawBottom, boolean drawEast,
			boolean drawWest, boolean drawNorth, boolean drawSouth,
			boolean topInShadow, boolean eastInShadow, boolean westInShadow,
			boolean northInShadow, boolean southInShadow, float lightLevel) {
		
		initShader();

		GL11.glTranslatef(X, Y, Z);

		texture.bind();

		if (useShader) {
			ARBShaderObjects.glUseProgramObjectARB(shader);
		}

		/*
		 * int effectMap=ARBShaderObjects.glGetUniformLocationARB(shader,
		 * "effectMap"); if(effectMap<1)
		 * System.out.println("Error accessing effectMap: " + effectMap);
		 */

		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		if (drawAllFaces || type.isLiquid() || type == CubeType.fire)
			GL11.glDisable(GL11.GL_CULL_FACE);

		if (drawAllFaces)
			GL11.glTranslatef(-0.1f, -0.1f, -0.1f);
		if (drawAllFaces)
			GL11.glScalef(1.2f, 1.2f, 1.2f);

		GL11.glBegin(4);

		if (drawTop || drawAllFaces) {
			// topTexture.bind();
			if (topInShadow)
				GL11.glColor3f(lightLevel+0.2f, lightLevel+0.2f, lightLevel+0.2f); // Shadow in top face
			else {								                    
				GL11.glColor3f(1.2f, 1.2f, 1.2f);
			}
			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 0.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, 1.0F, 0.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 1.0F, 1.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 0.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 1.0F, 1.0F);

			GL11.glNormal3f(0.0F, 1.0F, 0.0F); // Top
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, 1.0F, 1.0F);
			if (topInShadow)
				GL11.glColor3f(1f, 1f, 1f);
		}

		if (drawBottom || drawAllFaces) {
			// bottomTexture.bind();
			GL11.glColor3f(lightLevel, lightLevel, lightLevel); // Bottom face
																// is always in
																// shadow
			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);

			GL11.glNormal3f(0.0F, -1.0F, 0.0F); // Bottom
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);
			GL11.glColor3f(1f, 1f, 1f);
		}

		texture.bind();

		if (drawEast || drawAllFaces) {
			if (eastInShadow)
				GL11.glColor3f(lightLevel-0.2f, lightLevel-0.2f, lightLevel-0.2f); // Shadow in
																	               // east face
			else {								                    
				GL11.glColor3f(0.8f, 0.8f, 0.8f);
			}
			
			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(0.0F, 1.0F, 1.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, 1.0F, 0.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(0.0F, 1.0F, 1.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);

			GL11.glNormal3f(-1.0F, 0.0F, 0.0F); // East
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 1.0F);
			if (eastInShadow)
				GL11.glColor3f(1f, 1f, 1f);
		}

		if (drawWest || drawAllFaces) {
			if (westInShadow)
				GL11.glColor3f(lightLevel-0.2f, lightLevel-0.2f, lightLevel-0.2f); // Shadow in
																	               // west face
			else {								                    
				GL11.glColor3f(0.8f, 0.8f, 0.8f);
			}
			
			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 0.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 1.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 0.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);

			GL11.glNormal3f(1.0F, 0.0F, 0.0F); // West
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);
			if (westInShadow)
				GL11.glColor3f(1f, 1f, 1f);
		}

		if (drawNorth || drawAllFaces) {
			if (northInShadow)
				GL11.glColor3f(lightLevel, lightLevel, lightLevel); // Shadow in
																	// north face
			else {								                    
				GL11.glColor3f(1f, 1f, 1f);
			}													
			
			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, 1.0F, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 1.0F);

			GL11.glNormal3f(0.0F, 0.0F, 1.0F); // North
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 1.0F);
			if (northInShadow)
				GL11.glColor3f(1f, 1f, 1f);
		}

		if (drawSouth || drawAllFaces) {
			if (southInShadow)
				GL11.glColor3f(lightLevel-0.3f, lightLevel-0.3f, lightLevel-0.3f); // Shadow in
																	               // south face
			else {								                    
				GL11.glColor3f(0.65f, 0.65f, 0.65f);
			}
			
			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(0.0F, 1.0F, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(0.0F, 1.0F, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);

			GL11.glNormal3f(0.0F, 0.0F, -1.0F); // South
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);
			if (southInShadow)
				GL11.glColor3f(lightLevel, lightLevel, lightLevel);
		}

		GL11.glEnd();

		GL11.glColor3f(1, 1, 1);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);

		// ARBShaderObjects.glUseProgramObjectARB(0);

	}

	public void renderCube(CubeState cubeManager, int x, int y, int z) {
		
		try {
			
			int t = cubeManager.cubeMap.getCubeType(x, y, z);
			if (t == 0) return;
			
			CubeType type = CubeType.getTypeFromNumber(t);
			if (type == CubeType.air) return;
			
			boolean drawTop = cubeManager.getDrawTop(x, y, z);
			boolean drawBottom = cubeManager.getDrawBottom(x, y, z);
			boolean drawNorth = cubeManager.getDrawNorth(x, y, z);
			boolean drawSouth = cubeManager.getDrawSouth(x, y, z);
			boolean drawEast = cubeManager.getDrawEast(x, y, z);
			boolean drawWest = cubeManager.getDrawWest(x, y, z);
			
			boolean isHalfCube = cubeManager.getIsHalfCube(x, y, z);
			
			if (!drawTop && !drawBottom && !drawNorth && !drawSouth && !drawEast && ! drawWest){
				return;
			}
			
			int texture;
			texture = cubeManager.getTextureOfCube(x, y, z);
			if (type == CubeType.skycube){
				texture = cubeManager.background.textureID;
			} else if (type == CubeType.fire){
				drawTop = false;
				drawBottom = false;
			}
			
			boolean topInShadow = cubeManager.getShadowOfTop(x, y, z);
			boolean eastInShadow = cubeManager.getShadowOfEast(x, y, z);
			boolean westInShadow = cubeManager.getShadowOfWest(x, y, z);
			boolean northInShadow = cubeManager.getShadowOfNorth(x, y, z);
			boolean southInShadow = cubeManager.getShadowOfSouth(x, y, z);
			float lightLevel = 0.5f;
			renderCube(x, y, z, texture, false, type, drawTop, drawBottom, drawEast, drawWest, drawNorth, drawSouth, 
				topInShadow, eastInShadow, westInShadow, northInShadow, southInShadow, isHalfCube, lightLevel);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void renderBoundingBox(BoundingBox bb) {
        GL11.glBegin(3);
        GL11.glVertex3f(bb.xN, bb.yN, bb.zN);
        GL11.glVertex3f(bb.xP, bb.yN, bb.zN);
        GL11.glVertex3f(bb.xP, bb.yN, bb.zP);
        GL11.glVertex3f(bb.xN, bb.yN, bb.zP);
        GL11.glVertex3f(bb.xN, bb.yN, bb.zN);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3f(bb.xN, bb.yP, bb.zN);
        GL11.glVertex3f(bb.xP, bb.yP, bb.zN);
        GL11.glVertex3f(bb.xP, bb.yP, bb.zP);
        GL11.glVertex3f(bb.xN, bb.yP, bb.zP);
        GL11.glVertex3f(bb.xN, bb.yP, bb.zN);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3f(bb.xN, bb.yN, bb.zN);
        GL11.glVertex3f(bb.xN, bb.yP, bb.zN);
        GL11.glVertex3f(bb.xP, bb.yN, bb.zN);
        GL11.glVertex3f(bb.xP, bb.yP, bb.zN);
        GL11.glVertex3f(bb.xP, bb.yN, bb.zP);
        GL11.glVertex3f(bb.xP, bb.yP, bb.zP);
        GL11.glVertex3f(bb.xN, bb.yN, bb.zP);
        GL11.glVertex3f(bb.xN, bb.yP, bb.zP);
        GL11.glEnd();
	}
}

