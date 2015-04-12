package cubegame.render;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import cubegame.Cube;
import cubegame.CubeType;
import cubegame.Texture;
import cubegame.TextureLoader;

public class TerrainRenderer {

	static RenderHelper helper = new RenderHelper(1.5f, 1.5f);
	
	public static void renderSun() {
		float y = 75f;
		float s = 10f;
		
		GL11.glPushMatrix();
		{
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			//GL11.glDisable(GL11.GL_TEXTURE_2D);
			//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			try {
				Cube.getTextureOfType(CubeType.light).bind();
			} catch (IOException e) {e.printStackTrace();
			}
			
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			
			GL11.glColor3f(1f, 1f, 1f);
			
			GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(-s, y, -s);
			
			GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(s, y, -s);
			
			GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(s, y, s);
			
			GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(-s, y, s);
			
			
			/*GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(s, y, -s);
			
			GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(s, y, s);
			
			GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(-s, y, s);
			
			GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(-s, y, -s);*/
			
			GL11.glEnd();

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		GL11.glPopMatrix();
	}

	public static void renderMoon() {
		float y = 75f;
		float s = 5f;
		
		GL11.glPushMatrix();
		{
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			//GL11.glDisable(GL11.GL_TEXTURE_2D);
			//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			moon.bind();
			
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			
			GL11.glColor3f(1f, 1f, 1f);
			
			GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(-s, y, -s);
			
			GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(s, y, -s);
			
			GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(s, y, s);
			
			GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(-s, y, s);
			
			
			/*GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(s, y, -s);
			
			GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(s, y, s);
			
			GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(-s, y, s);
			
			GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(-s, y, -s);*/
			
			GL11.glEnd();

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		GL11.glPopMatrix();
	}

	private static Texture moon;
	
	static{
		TextureLoader l = new TextureLoader();
		try {
			moon = l.getTexture("res/textures/tex4000/kuu.png");
		} catch (IOException e) {e.printStackTrace();
		}
	}
}
