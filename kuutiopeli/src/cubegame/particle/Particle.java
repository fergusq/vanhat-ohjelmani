package cubegame.particle;

import org.lwjgl.opengl.GL11;

public class Particle {

	  ///////////////// Constants /////////////////////////

	  // Constants for indexing.
	  public static final int X = 0;
	  public static final int Y = 1;
	  public static final int Z = 2;

	  //////////////// Variables /////////////////////////

	  private float lifetime = 100f;
	  private float decay = 1f;
	  // The pariticls resites inside a rectangle.
	  private float size = 0.4f;

	  private float pos[] = {0.0f, 0.0f, 0.0f};
	  private float speed[] = {0.0f, 0.0f, 0.0f};

	  ///////////////// Functions /////////////////////////

	  Particle( float lifetime, float decay, float size, float x, float y, float z)
	    {
	      if( lifetime != 0) { this.lifetime = lifetime; }
	      if( decay != 0) { this.decay = decay; }
	      if( size != 0) { this.size = size; }

	      pos[X] = x;
	      pos[Y] = y;
	      pos[Z] = z;
	    }

	  public float getLifetime() { return lifetime; }

	  public float getPosX() { return pos[X]; }
	  public float getPosY() { return pos[Y]; }
	  public float getPosZ() { return pos[Z]; }

	  public float getSpeedX() { return speed[X]; }
	  public float getSpeedY() { return speed[Y]; }
	  public float getSpeedZ() { return speed[Z]; }

	  public void setSpeed( float sx, float sy, float sz ) 
	    { 
	      speed[X] = sx;
	      speed[Y] = sy;
	      speed[Z] = sz;
	    }

	  public void incSpeedX( float ds ) { speed[X] += ds; }
	  public void incSpeedY( float ds ) { speed[Y] += ds; }
	  public void incSpeedZ( float ds ) { speed[Z] += ds; }

	  public boolean isAlive() { return (lifetime > 0.0); }

	  public void evolve()
	    {
	      lifetime -= decay;
	      // Update locaton.
	      for(int i=0; i<3; i++)
		pos[i] += speed[i];
	    }

	  public void draw(float rotY, float rotX)
	    { 
	      final float halfSize = size / 2f;
	      final float x = pos[X]-halfSize;
	      final float y = pos[Y]-halfSize;
	      final float xs = pos[X]+halfSize;
	      final float ys = pos[Y]+halfSize;
	      // Particle as small rectangle.
	      GL11.glPushMatrix();
	      
	      GL11.glTranslatef(pos[X], pos[Y], pos[Z]);
	      GL11.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
			GL11.glTranslatef(-pos[X], -pos[Y], -pos[Z]);
	      
	      GL11.glBegin(GL11.GL_QUADS); {
		GL11.glTexCoord2f( 0f, 0f );
		GL11.glVertex3f( x, y, pos[Z] );
		GL11.glTexCoord2f( 1f, 0f );
		GL11.glVertex3f( xs, y, pos[Z] );
		GL11.glTexCoord2f( 1f, 1f );
		GL11.glVertex3f( xs, ys, pos[Z] );
		GL11.glTexCoord2f( 0f, 1f );
		GL11.glVertex3f( x, ys, pos[Z] );
		
		GL11.glTexCoord2f( 0f, 1f );
		GL11.glVertex3f( x, ys, pos[Z] );
		GL11.glTexCoord2f( 1f, 1f );
		GL11.glVertex3f( xs, ys, pos[Z] );
		GL11.glTexCoord2f( 1f, 0f );
		GL11.glVertex3f( xs, y, pos[Z] );
		GL11.glTexCoord2f( 0f, 0f );
		GL11.glVertex3f( x, y, pos[Z] );	
	      } GL11.glEnd();
	      
	      GL11.glPopMatrix();
	    }

	}
