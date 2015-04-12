package cubegame.particle;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import cubegame.Texture;

public class ParticleSystem {

	  ///////////////// Constants /////////////////////////

	  private final int MAX_PARTICLES = 500;
	  // We may want more than one texture for particl evolution.
	  final int nbTexture = 2;

	  //////////////// Variables /////////////////////////

	  private Particle p[] = new Particle[MAX_PARTICLES];
	  // Array for texture ids.
	  // Texturs are share by particles 
	  private int[] textures = new int[ nbTexture ] ;
	  
	  public float x, y, z, rotY, rotX;

	  ///////////////// Functions /////////////////////////

	  public void init()
	    {
		  x = y = z = 0;
		  
	      // Particles are tranparent.
	      GL11.glEnable( GL11.GL_BLEND );    
	      GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE );
	      
	      // Prepare textures for paricles.
	      GL11.glEnable( GL11.GL_TEXTURE_2D );
	      
	      // Square textures size.
	      byte b[]; // Array for texture data.
	      final int size = 256;
	      // Texture 0.
	      b = calcTextureData( size, 4, 250, 50, 5, 2f );
	      initTexture(b, 0, size );
	      // Texture 1.
	      b = calcTextureData( size, 4, 50, 50, 55, 1f );
	      initTexture(b, 1, size );
	    }

	  private void initTexture(byte b[], int index, int size )
	    {
	      GL11.glBindTexture( GL11.GL_TEXTURE_2D,textures[ index ] );
	      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	      // Set texture data.
	      ByteBuffer texture = BufferUtils.createByteBuffer(b.length);
	      texture.put( b, 0, b.length );
	      GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, size, size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texture ) ;
	    }

	  //Create particle Texture.
	  private byte[] calcTextureData( int size, int bytesPerPixel,
					  int r, int g, int b, 
					  float alphaScale )
	    {      
	      final int halfSize = size/2;
	      final int nbBytes = size*size*bytesPerPixel;
	      final int nbBytesRow = size*bytesPerPixel;
	      // Byte-Array for Image.
	      byte[] data = new byte[nbBytes];
	      
	      //RGB-Werte 150,150,255 (some blue)
	      for(int i=0; i < nbBytes; i+=bytesPerPixel){
		data[i]   = (byte)r;
		data[i+1] = (byte)g;
		data[i+2] = (byte)b;
		//data[i+3] = (byte)255; // Calc below.
	      }      
	      // Re-calc Alpha with max in center and radia reduction.
	      final int maxAlpha = 100;
		for(int y=0; y < size; y++) {
		  for(int x=0; x < size; x++) {
		    int dx = x - halfSize;
		    int dy = y - halfSize;
		    int a = maxAlpha - 
		      (int) ( Math.sqrt((double)(dx*dx+dy*dy)) 
		      * alphaScale );
		    if (a < 0) { a = 0; }
		    data[ y * nbBytesRow + x * bytesPerPixel + 3 ] = (byte)a;
		  }
		}	
		return data;
	    }


	  private Particle createParticle(float x, float y, float z)
	    {
	      Particle p = new Particle( 50f, 0f, 0.1f, x, y, z);
	      p.setSpeed( // Init random Speed.
		0.001f - (float)Math.random() / 500.0f,
		0.008f - (float)Math.random() / 1000.0f,
		0.001f - (float)Math.random() / 500.0f 
	      );
	      return p;
	    }

	  ////////////////// draw ////////////////////////////////
	  
	  public void draw(Texture tex)
	    {

	      GL11.glDepthMask( false );

	      // Loop over particles.
	      for( int i=0; i < MAX_PARTICLES; i++ )
	      {
		// Create new particles for continuous effect.
		if ( p[i] == null ) 
		{
		  p[i] = createParticle(x, y, z);
		  break; // Create one particle per time step.
		}

		// Kill particle if it died.
		if (!p[i].isAlive() ) 
		{
		  p[i] = createParticle(x, y, z);
		}

		// Apply gravity.
		p[i].incSpeedY( -0.0004f );
		p[i].evolve();

		// Select texture and draw.
		if( p[i].getLifetime() > 200 ) {
			tex.bind();
		} else {
			//tex.bind();
		}
		p[i].draw(rotY, rotX);

	      } // end particle loop.

	      GL11.glDepthMask( true );
	    }

	}
