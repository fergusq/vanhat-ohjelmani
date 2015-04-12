package cubegame.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * Used to generate shadows.
 * 
 * @see cubegame.shading.ShadeHelper
 * @author iikka
 *
 */
public class RenderHelper {
	private FloatBuffer material = BufferUtils.createFloatBuffer(4);
	private FloatBuffer buffer0 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer buffer1 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer buffer2 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer buffer3 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer buffer4 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer buffer5 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer buffer6 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer buffer7 = BufferUtils.createFloatBuffer(4);
	private ByteBuffer buffer8 = BufferUtils.createByteBuffer(4);
	private ByteBuffer buffer9 = BufferUtils.createByteBuffer(4);
	
	public RenderHelper(float lightLevel, float shadowLevel){
		buffer0.put(lightLevel).put(lightLevel).put(lightLevel).put(0.2f); 
		buffer0.flip();
		
		buffer1.put(lightLevel).put(lightLevel).put(lightLevel).put(1);
		buffer1.flip();
		
		// setup the ambient light 

		buffer2.put(lightLevel+0.2f).put(lightLevel+0.2f).put(lightLevel+0.2f).put(0.8f);
		buffer2.flip();
		
		// set up the position of the light

		buffer3.put(0).put(10).put(0).put(1);
		buffer3.flip();
		
		
		buffer4.put(shadowLevel).put(shadowLevel).put(shadowLevel).put(0.2f); 
		buffer4.flip();
		
		buffer5.put(shadowLevel).put(shadowLevel).put(shadowLevel).put(1);
		buffer5.flip();
		
		// setup the ambient light 

		buffer6.put(shadowLevel-0.2f).put(shadowLevel-0.2f).put(shadowLevel-0.2f).put(0.8f);
		buffer6.flip();
		
		// set up the position of the light

		buffer7.put(0).put(10).put(0).put(1);
		buffer7.flip();
		
		buffer8.put((byte) shadowLevel).put((byte) shadowLevel).put((byte) shadowLevel).put((byte) 0.2f); 
		buffer8.flip();
		buffer9.put((byte) lightLevel).put((byte) lightLevel).put((byte) lightLevel).put((byte) 0.2f); 
		buffer9.flip();
	}
	
	/**
	 * Configure custom light-level
	 * @param lightLevel
	 */
	public void setShadow(float lightLevel) {
		
		material.clear();
		material.put(lightLevel).put(lightLevel).put(lightLevel).put(lightLevel); 
		material.flip();
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, material);
		GL11.glMaterial(GL11.GL_BACK, GL11.GL_DIFFUSE, material);

		
	}

	/**
	 * Sets light-level to lighter
	 * 
	 * @see RenderHelper#createShadow()
	 */
	public void createLight() {
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, buffer0);
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, buffer1);
		
		// setup the ambient light 

		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, buffer2);
		GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, GL11.GL_TRUE);
		
		// set up the position of the light

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, buffer3);
		
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glDisable(GL11.GL_LIGHT1);
		
		 GL11.glColorPointer(4, true, 32, buffer9);
		
		 GL11.glEnableClientState(32886 /*GL_COLOR_ARRAY_EXT*/);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	/**
	 * Sets darker light-level
	 * 
	 * @see RenderHelper#createLight()
	 */
	public void createShadow() {
		
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, buffer4);
		
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, buffer5);
		
		// setup the ambient light 

		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, buffer6);
		GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, GL11.GL_TRUE);
		
		// set up the position of the light

		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, buffer7);
		
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_LIGHT0);
		
		
	}
	
}
