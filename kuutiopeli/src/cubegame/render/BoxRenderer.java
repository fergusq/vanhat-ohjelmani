package cubegame.render;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import cubegame.Cube;
import cubegame.CubeType;
import cubegame.Texture;
import cubegame.TextureLoader;

public class BoxRenderer {

	private static RenderHelper renderHelper = new RenderHelper(1, 0.5f);
	public static void renderBox(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float rotationY, float rotationX, int[] tex) {
		
		float sY = sizeY / 2;
		float sX = sizeX / 2;
		float sZ = sizeZ / 2;
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y-sY, z);
		GL11.glRotatef(rotationY, 0.0f, 1.0f, 0.0f);
		GL11.glRotatef(rotationX, 1.0f, 0.0f, 0.0f);
		//GL11.glScalef(1.1f, 1.1f, 1.1f);
		
		/*int effectMap=ARBShaderObjects.glGetUniformLocationARB(shader, "effectMap");
		if(effectMap<1) System.out.println("Error accessing effectMap: " + effectMap);*/
		
		GL11.glEnable(GL11.GL_LIGHTING);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[0]);
		
		renderHelper.createLight();
		
		GL11.glBegin(4);

		GL11.glColor3f(1, 1, 1);
		
		GL11.glNormal3f(0.0F, sY, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sY, -sZ);

		GL11.glNormal3f(0.0F, sY, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(-sX, sY, -sZ);

		GL11.glNormal3f(0.0F, sY, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, sY, sZ);

		GL11.glNormal3f(0.0F, sY, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sY, -sZ);

		GL11.glNormal3f(0.0F, sY, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, sY, sZ);

		GL11.glNormal3f(0.0F, sY, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sX, sY, sZ);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[1]);
		renderHelper.createShadow();
		GL11.glBegin(4);
		
		GL11.glNormal3f(0.0F, -sY, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, -sY, sZ);

		GL11.glNormal3f(0.0F, -sY, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(-sX, -sY, sZ);

		GL11.glNormal3f(0.0F, -sY, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sY, -sZ);

		GL11.glNormal3f(0.0F, -sY, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, -sY, sZ);

		GL11.glNormal3f(0.0F, -sY, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sY, -sZ);
		
		GL11.glNormal3f(0.0F, -sY, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sX, -sY, -sZ);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[2]);
		renderHelper.createLight();
		GL11.glBegin(4);
		
		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(-sX, sY, sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(-sX, sY, -sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sY, -sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(-sX, sY, sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sY, -sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(-sX, -sY, sZ);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[3]);
		GL11.glBegin(4);
		
		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sY, -sZ);

		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sX, sY, sZ);

		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sX, -sY, sZ);

		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sY, -sZ);

		GL11.glNormal3f(sX, -sY, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sX, -sY, sZ);

		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sX, -sY, -sZ);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[4]);
		GL11.glBegin(4);
		
		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sY, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(-sX, sY, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sY, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sY, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sY, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sX, -sY, sZ);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[5]);
		GL11.glBegin(4);
		
		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(-sX, sY, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sX, sY, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sX, -sY, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(-sX, sY, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sX, -sY, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(-sX, -sY, -sZ);
		
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		//ARBShaderObjects.glUseProgramObjectARB(0);

		GL11.glPopMatrix();
	}
	
public static void renderBox(float x, float y, float z, float sizeXd, float sizeYd, float sizeZd, float sizeXu, float sizeYu, float sizeZu, float rotationY, float rotationX, boolean mirror, int[] tex) {
		
		float sYd = sizeYd;
		float sXd = sizeXd;
		float sZd = sizeZd;
		
		float sYu = sizeYu;
		float sXu = sizeXu;
		float sZu = sizeZu;
		
		if(mirror)
        {
            float f = sizeXd + sizeXu;
            //sizeX = sizeXd;
            sizeXd = f;
        }
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y-sYd, z);
		GL11.glRotatef(rotationY, 0.0f, 1.0f, 0.0f);
		GL11.glRotatef(rotationX, 1.0f, 0.0f, 0.0f);
		//GL11.glScalef(1.1f, 1.1f, 1.1f);
		
		/*int effectMap=ARBShaderObjects.glGetUniformLocationARB(shader, "effectMap");
		if(effectMap<1) System.out.println("Error accessing effectMap: " + effectMap);*/
		
		GL11.glEnable(GL11.GL_LIGHTING);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[0]);
		
		renderHelper.createLight();
		
		GL11.glBegin(4);

		GL11.glColor3f(1, 1, 1);
		
		GL11.glNormal3f(0.0F, sYu, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXu, sYu, sZd);

		GL11.glNormal3f(0.0F, sYu, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sXd, sYu, sZd);

		GL11.glNormal3f(0.0F, sYu, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXd, sYu, sZu);

		GL11.glNormal3f(0.0F, sYu, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXu, sYu, sZd);

		GL11.glNormal3f(0.0F, sYu, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXd, sYu, sZu);

		GL11.glNormal3f(0.0F, sYu, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sXu, sYu, sZu);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[1]);
		renderHelper.createShadow();
		GL11.glBegin(4);
		
		GL11.glNormal3f(0.0F, sYd, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXu, sYd, sZu);

		GL11.glNormal3f(0.0F, sYd, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sXd, sYd, sZu);

		GL11.glNormal3f(0.0F, sYd, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXd, sYd, sZd);

		GL11.glNormal3f(0.0F, sYd, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXu, sYd, sZu);

		GL11.glNormal3f(0.0F, sYd, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXd, sYd, sZd);
		
		GL11.glNormal3f(0.0F, sYd, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sXu, sYd, sZd);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[2]);
		renderHelper.createLight();
		GL11.glBegin(4);
		
		GL11.glNormal3f(sXd, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXd, sYu, sZu);

		GL11.glNormal3f(sXd, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sXd, sYu, sZd);

		GL11.glNormal3f(sXd, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXd, sYd, sZd);

		GL11.glNormal3f(sXd, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXd, sYu, sZu);

		GL11.glNormal3f(sXd, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXd, sYd, sZd);

		GL11.glNormal3f(sXd, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sXd, sYd, sZu);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[3]);
		GL11.glBegin(4);
		
		GL11.glNormal3f(sXu, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXu, sYu, sZd);

		GL11.glNormal3f(sXu, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sXu, sYu, sZu);

		GL11.glNormal3f(sXu, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXu, sYd, sZu);

		GL11.glNormal3f(sXu, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXu, sYu, sZd);

		GL11.glNormal3f(sXu, sYd, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXu, sYd, sZu);

		GL11.glNormal3f(sXu, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sXu, sYd, sZd);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[4]);
		GL11.glBegin(4);
		
		GL11.glNormal3f(0.0F, 0.0F, sZu); //North
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXu, sYu, sZu);

		GL11.glNormal3f(0.0F, 0.0F, sZu); //North
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sXd, sYu, sZu);

		GL11.glNormal3f(0.0F, 0.0F, sZu); //North
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXd, sYd, sZu);

		GL11.glNormal3f(0.0F, 0.0F, sZu); //North
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXu, sYu, sZu);

		GL11.glNormal3f(0.0F, 0.0F, sZu); //North
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXd, sYd, sZu);

		GL11.glNormal3f(0.0F, 0.0F, sZu); //North
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sXu, sYd, sZu);
		
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex[5]);
		GL11.glBegin(4);
		
		GL11.glNormal3f(0.0F, 0.0F, sZd); //South
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXd, sYu, sZd);

		GL11.glNormal3f(0.0F, 0.0F, sZd); //South
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sXu, sYu, sZd);

		GL11.glNormal3f(0.0F, 0.0F, sZd); //South
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXu, sYd, sZd);

		GL11.glNormal3f(0.0F, 0.0F, sZd); //South
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sXd, sYu, sZd);

		GL11.glNormal3f(0.0F, 0.0F, sZd); //South
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sXu, sYd, sZd);

		GL11.glNormal3f(0.0F, 0.0F, sZd); //South
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sXd, sYd, sZd);
		
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		//ARBShaderObjects.glUseProgramObjectARB(0);

		GL11.glPopMatrix();
	}
	
	public static void renderBasicPlayer(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float rotationY, float rotationX) {
		
		float sYtop = sizeY / 3;
		float sYbottom = sizeY / 2;
		float sX = sizeX / 2;
		float sZ = sizeZ / 2;
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glPushMatrix();
		renderBox(x, y+sYtop, z, sizeX*1.1f, sizeY/4, sizeZ, rotationY, rotationX, new int[]{0, 0, 0, 0, 0, face1.textureID});
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y-sYbottom, z);
		GL11.glRotatef(rotationY, 0.0f, 1.0f, 0.0f);
		//GL11.glScalef(1.1f, 1.1f, 1.1f);
		
		/*int effectMap=ARBShaderObjects.glGetUniformLocationARB(shader, "effectMap");
		if(effectMap<1) System.out.println("Error accessing effectMap: " + effectMap);*/
		
		GL11.glEnable(GL11.GL_LIGHTING);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		try {
			Cube.getTextureOfType(CubeType.net).bind();
		} catch (IOException e) {e.printStackTrace();}
		
		renderHelper.createLight();
		
		GL11.glBegin(4);

		GL11.glColor3f(1, 1, 1);
		
		GL11.glNormal3f(0.0F, sYtop, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sYtop, -sZ);

		GL11.glNormal3f(0.0F, sYtop, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(-sX, sYtop, -sZ);

		GL11.glNormal3f(0.0F, sYtop, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, sYtop, sZ);

		GL11.glNormal3f(0.0F, sYtop, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sYtop, -sZ);

		GL11.glNormal3f(0.0F, sYtop, 0.0F); //Top
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, sYtop, sZ);

		GL11.glNormal3f(0.0F, sYtop, 0.0F); //Top
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sX, sYtop, sZ);
		
		GL11.glEnd();
		renderHelper.createShadow();
		GL11.glBegin(4);
		
		GL11.glNormal3f(0.0F, -sYbottom, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, -sYbottom, sZ);

		GL11.glNormal3f(0.0F, -sYbottom, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(-sX, -sYbottom, sZ);

		GL11.glNormal3f(0.0F, -sYbottom, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sYbottom, -sZ);

		GL11.glNormal3f(0.0F, -sYbottom, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, -sYbottom, sZ);

		GL11.glNormal3f(0.0F, -sYbottom, 0.0F); //Bottom
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sYbottom, -sZ);
		
		GL11.glNormal3f(0.0F, -sYbottom, 0.0F); //Bottom
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sX, -sYbottom, -sZ);
		
		GL11.glEnd();
		renderHelper.createLight();
		GL11.glBegin(4);
		
		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(-sX, sYtop, sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(-sX, sYtop, -sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sYbottom, -sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(-sX, sYtop, sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sYbottom, -sZ);

		GL11.glNormal3f(-sX, 0.0F, 0.0F); //East
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(-sX, -sYbottom, sZ);
		
		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sYtop, -sZ);

		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sX, sYtop, sZ);

		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sX, -sYbottom, sZ);

		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sYtop, -sZ);

		GL11.glNormal3f(sX, -sYbottom, 0.0F); //West
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sX, -sYbottom, sZ);

		GL11.glNormal3f(sX, 0.0F, 0.0F); //West
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sX, -sYbottom, -sZ);
		
		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sYtop, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(-sX, sYtop, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sYbottom, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(sX, sYtop, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-sX, -sYbottom, sZ);

		GL11.glNormal3f(0.0F, 0.0F, sZ); //North
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(sX, -sYbottom, sZ);
		
		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(-sX, sYtop, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(sX, sYtop, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sX, -sYbottom, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(-sX, sYtop, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(sX, -sYbottom, -sZ);

		GL11.glNormal3f(0.0F, 0.0F, -sZ); //South
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(-sX, -sYbottom, -sZ);
		
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		//ARBShaderObjects.glUseProgramObjectARB(0);

		GL11.glPopMatrix();
	}
	
	public static void renderSpirit(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float rotationY, float rotationX) {
		
		float sYtop = sizeY / 3;
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glPushMatrix();
		renderBox(x, y+sYtop, z, sizeX*1.1f, sizeY/4, sizeZ, rotationY, rotationX, new int[]{0, 0, 0, 0, 0, face1.textureID});
		GL11.glPopMatrix();
	}
	
	private static void renderBipedCloak(float legRot){
		float alt = -1F;
		int[] tex = new int[]{blue.textureID, blue.textureID, blue.textureID, blue.textureID, 
				blue.textureID, red.textureID};
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(0, 0, 5);
			GL11.glRotatef(45, 1, 0, 0);
			GL11.glRotatef(legRot/5, 1, 0, 0);
			GL11.glTranslatef(0, 0, -5);
			renderBox(0, 0+alt, 5, -5F, 0.0F, -1F, 5, 20, 1, 0, 0, false, tex); //cloak
			//renderBox(0, 0+alt, 5, -3F, -6F, -1F, 6, 6, 1, 0, 0, false, tex);
		}
		GL11.glPopMatrix();
	}
	
	public static void renderBipedWithCloak(float x, float y, float z, float rotationY, float pitch, int legRot){
		GL11.glPushMatrix();
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glTranslatef(x, y, z);
		GL11.glScalef(0.07f, 0.07f, 0.07f);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(180, 0, 1, 0);
		GL11.glRotatef(rotationY, 0, 1, 0);
		
		renderBipedCloak(legRot);
		
		GL11.glPopMatrix();
		
		renderBiped(x, y, z, rotationY, pitch, legRot);
	}
	
	public static void renderBiped(float x, float y, float z, float rotationY, float pitch, int legRot){
		int[] tex = new int[]{blue.textureID, blue.textureID, blue.textureID, blue.textureID, 
				blue.textureID, blue.textureID};
		int[] head = new int[]{0, 0, 0, 0, 0, face1real.textureID};
		int[] body = new int[]{red.textureID, red.textureID, red.textureID, red.textureID, 
				red.textureID, red.textureID};
		float alt = -1F;
		
		GL11.glPushMatrix();
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glTranslatef(x, y, z);
		GL11.glScalef(0.07f, 0.07f, 0.07f);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(180, 0, 1, 0);
		GL11.glRotatef(rotationY, 0, 1, 0);
		GL11.glPushMatrix();
		{
			GL11.glRotatef(pitch, 1, 0, 0);
			renderBox(0, 0-4, 0, -3.95f, -0F, -4F, 3.95f, 8, 4.1f, 0, 0, false, head); // head
		}
		GL11.glPopMatrix();
		GL11.glColor3f(0, 0, 0);
		renderBox(0, 0, 0, -4F, 0.0F, -2F, 4, 12, 4, 0, 0, false, body); // body
		GL11.glColor3f(1, 1, 1);
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(-5F, 2.0F+alt, 0);
			GL11.glRotatef(-legRot/2, 1, 0, 0);
			GL11.glTranslatef(5F, -(2.0F+alt), 0);
			renderBox(-5F, 2.0F+alt, 0, -3F, -1.5F, -2F, 1, 12, 3.5f, 0, 0, false, tex); // right arm
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(5F, -(2.0F+alt), 0);
			GL11.glRotatef(legRot/2, 1, 0, 0);
			GL11.glTranslatef(-5F, 2.0F+alt, 0);
			renderBox(5F, 2.0F+alt, 0, -1F, -1.5F, -2F, 3, 12, 3.5f, 0, 0, true, tex); // left arm
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(-2F, 12F+alt, 0);
			GL11.glRotatef(legRot, 1, 0, 0);
			GL11.glTranslatef(2F, -(12F+alt), 0);
			renderBox(-2F, 12F+alt, 0, -2F, -1.5F, -1.95F, 4, 12, 3.5f, 0, 0, false, tex); // right leg
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(2.0F, 12F+alt, 0);
			GL11.glRotatef(-legRot, 1, 0, 0);
			GL11.glTranslatef(-2.0F, -(12F+alt), 0);
			renderBox(2.0F, 12F+alt, 0, -4F, -1.5F, -1.95F, 2, 12, 3.5f, 0, 0, true, tex); // left leg
		}
		GL11.glPopMatrix();
		
		GL11.glPopMatrix();
	}
	
	public static void renderBipedRightHand(float x, float y, float z, float rotationY, float pitch, int legRot){
		int[] tex = new int[]{blue.textureID, blue.textureID, blue.textureID, blue.textureID, 
				blue.textureID, blue.textureID};
		float alt = -1F;
		
		GL11.glPushMatrix();
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glTranslatef(x, y, z);
		GL11.glScalef(0.07f, 0.07f, 0.07f);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(rotationY, 0, 1, 0);
		GL11.glRotatef(-pitch, 1, 0, 0);
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(5F, -(2.0F+alt), 0);
			GL11.glRotatef(legRot/2, 1, 0, 0);
			GL11.glTranslatef(-5F, 2.0F+alt, 0);
			renderBox(5F, 2.0F+alt, 0, -1F, -1.5F, -2F, 3, 12, 3.5f, 0, 0, true, tex); // left arm
		}
		GL11.glPopMatrix();
		
		GL11.glPopMatrix();
	}
	
	private static Texture face1;
	private static Texture face1real;
	
	private static Texture suit_bodyTop;
	private static Texture suit_bodySide;
	private static Texture suit_bodyFront;
	private static Texture suit_bodyBack;

	private static Texture red;
	private static Texture green;
	private static Texture blue;
	private static Texture yellow;
	private static Texture purple;
	
	static TextureLoader loader = new TextureLoader();
	static{
		try {
		face1 = loader.getTexture("res/textures/oliot/kasvotmir.png");
		face1real = loader.getTexture("res/textures/oliot/kasvot.png");
		suit_bodyTop = loader.getTexture("res/textures/oliot/Puku_VartaloYlä.png");
		suit_bodySide = loader.getTexture("res/textures/oliot/Puku_VartaloSivu.png");
		suit_bodyFront = loader.getTexture("res/textures/oliot/Puku_VartaloEtu.png");
		suit_bodyBack = loader.getTexture("res/textures/oliot/Puku_VartaloTaka.png");
		
		red = loader.getTexture("res/colors/red.png");
		green = loader.getTexture("res/colors/green.png");
		blue = loader.getTexture("res/colors/blue.png");
		yellow = loader.getTexture("res/colors/yellow.png");
		purple = loader.getTexture("res/colors/purple.png");
		} catch (IOException e) {e.printStackTrace();}
	}
}
