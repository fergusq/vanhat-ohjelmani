import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import java.nio.FloatBuffer;

/**
 * Simple Drawing Test
 * http://www.sgi.com/products/software/opengl/examples/redbook/images/material.jpg
 */
public class SimpleDrawingTest
{
	private boolean done = false;

	private FloatBuffer working = BufferUtils.createFloatBuffer(4);

	public static void main(String args[]) throws Exception
	{
		new SimpleDrawingTest().run();
	}


	public void run() throws Exception
	{
		init();
		while (!done)
		{
			input();
			render();
			Display.update();
			Thread.sleep(1000 / 25);
		}
		cleanup();
	}

	private void input()
	{
		Keyboard.poll();

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			done = true;
		}
	}

	private void render()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0f, 0f, -10f);

		float no_mat[] = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
		float mat_ambient[] = new float[]{0.7f, 0.7f, 0.7f, 1.0f};
		float mat_ambient_color[] = new float[]{0.8f, 0.8f, 0.2f, 1.0f};
		float mat_diffuse[] = new float[]{0.1f, 0.5f, 0.8f, 1.0f};
		float mat_specular[] = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
		float no_shininess[] = new float[]{0.0f};
		float low_shininess[] = new float[]{5.0f};
		float high_shininess[] = new float[]{100.0f};
		float mat_emission[] = new float[]{0.3f, 0.2f, 0.2f, 0.0f};
		Sphere sphere = new Sphere();

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		GL11.glPushMatrix();
		GL11.glTranslatef(-3.75f, 3.0f, 0.0f);
		set(no_mat, mat_diffuse, no_mat, no_shininess, no_mat);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-1.25f, 3.0f, 0.0f);
		set(no_mat, mat_diffuse, mat_specular, low_shininess, no_mat);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(1.25f, 3.0f, 0.0f);
		set(no_mat, mat_diffuse, mat_specular, high_shininess, no_mat);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(3.75f, 3.0f, 0.0f);
		set(no_mat, mat_diffuse, no_mat, no_shininess, mat_emission);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-3.75f, 0.0f, 0.0f);
		set(mat_ambient, mat_diffuse, no_mat, no_shininess, no_mat);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-1.25f, 0.0f, 0.0f);
		set(mat_ambient, mat_diffuse, mat_specular, low_shininess, no_mat);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();


		GL11.glPushMatrix();
		GL11.glTranslatef(1.25f, 0.0f, 0.0f);
		set(mat_ambient, mat_diffuse, mat_specular, high_shininess, no_mat);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(3.75f, 0.0f, 0.0f);
		set(mat_ambient, mat_diffuse, no_mat, no_shininess, mat_emission);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-3.75f, -3.0f, 0.0f);
		set(mat_ambient_color, mat_diffuse, no_mat, no_shininess, no_mat);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-1.25f, -3.0f, 0.0f);
		set(mat_ambient_color, mat_diffuse, mat_specular, low_shininess, no_mat);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(1.25f, -3.0f, 0.0f);
		set(mat_ambient_color, mat_diffuse, mat_specular, high_shininess, no_mat);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(3.75f, -3.0f, 0.0f);
		set(mat_ambient_color, mat_diffuse, no_mat, no_shininess, mat_emission);
		sphere.draw(1.0f, 16, 16);
		GL11.glPopMatrix();
	}

	private void createWindow() throws Exception
	{
		Display.setDisplayMode(findDisplayMode(800, 600, Display.getDisplayMode().getBitsPerPixel()));
		Display.create();
	}

	private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		DisplayMode mode = null;
		
		for (int i=0;i<modes.length;i++) {
			if ((modes[i].getBitsPerPixel() == bpp) || (mode == null)) {
				if ((modes[i].getWidth() == width) && (modes[i].getHeight() == height)) {
					mode = modes[i];
				}
			}
		}
		
		return mode;
	}
	
	private void init() throws Exception
	{
		createWindow();
		Keyboard.create();
		initGL();
	}

	private void initGL()
	{
		float ambient[] = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
		float diffuse[] = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
		float specular[] = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
		float position[] = new float[]{0.0f, 3.0f, 2.0f, 0.0f};
		float lmodel_ambient[] = new float[]{0.4f, 0.4f, 0.4f, 1.0f};
		float local_view[] = new float[]{0.0f};

		GL11.glClearColor(0.0f, 0.1f, 0.1f, 0.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, (float) 300 / (float) 200, 0.1f, 100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		loadWorking(ambient);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, working);
		loadWorking(diffuse);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, working);
		loadWorking(specular);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, working);
		loadWorking(position);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, working);
		loadWorking(lmodel_ambient);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, working);
		GL11.glLightModelf(GL11.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view[0]);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
	}

	private void cleanup()
	{
		Keyboard.destroy();
		Display.destroy();
	}

	private void set(float ambient[], float diffuse[], float specular[], float shininess[], float emission[])
	{
		loadWorking(ambient);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, working);
		loadWorking(diffuse);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, working);
		loadWorking(specular);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, working);
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shininess[0]);
		loadWorking(emission);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, working);
	}

	private void loadWorking(float[] data)
	{
		working.rewind();
		working.put(data);
		working.flip();
	}
}
