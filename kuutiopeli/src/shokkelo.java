import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.*;
 
public class shokkelo {
         
        float dx        = 0.0f;
        float dy        = 0.0f;
        float dt        = 0.0f; 
        float lastTime  = 0.0f; 
        float time      = 0.0f;
        float x, y, z;
        float hiiriSensitivity = 0.05f;
        float pelaajanopeus = 10.0f;
        private Vector3f    position    = null;
        private float       yaw         = 0.0f;
        private float       pitch       = 0.0f;
        private float view_rotx = 20.0f;
        private float view_roty = 30.0f;
        private float view_rotz;
        private int  seinat;
        private float angle;
 
 

 public static void main(String[] args) {
  new shokkelo().execute();
  System.exit(0);
 }
 
 
 private void execute() {
  try {
   init();
  } catch (LWJGLException le) {
   le.printStackTrace();
   System.out.println("Virhe intialisoidessa huonetta!");
   return;
  }
 
  loop();
 
  destroy();
 }
 
 
 
 private void destroy() {
  Display.destroy();
 }
 
 public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
 
 private void loop() {
  long startTime = System.currentTimeMillis() + 5000;
  long fps = 0;
                Mouse.setGrabbed(true);
 
        position = new Vector3f(x, y, z);
 
  while (!Display.isCloseRequested()) {
 
            time = getTime();
            dt = (time - lastTime)/1000.0f;
            lastTime = time;
 
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            yaw(dx * hiiriSensitivity);
            pitch(dy * hiiriSensitivity);
 
 
  if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) System.exit(0);
 
            if (Keyboard.isKeyDown(Keyboard.KEY_W))
            {
                kaveleeteen(pelaajanopeus*dt);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S))
            {
                kaveletaakse(pelaajanopeus*dt);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A))
            {
                kavelevasen(pelaajanopeus*dt);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D))
            {
                kaveleoikea(pelaajanopeus*dt);
            }            
 
   GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
 
         if (position.x > 17) position.x = 17;
         if (position.x < -17) position.x = -17;
         if (position.z > 17) position.z = 17;
         if (position.z < -17) position.z = -17;   
 
    GL11.glMatrixMode(GL11.GL_PROJECTION);
 
   GL11.glPushMatrix();
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(position.x, position.y, position.z);
   
   GL11.glPushMatrix();
   GL11.glScalef(30.0f, 17.0f, 30.0f);
   GL11.glCallList(seinat);
        GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
   GL11.glPopMatrix();
 

   GL11.glPopMatrix();
 
   Display.update();
 
 
   
  }
 }
 
 private void init() throws LWJGLException {
  // create Window of size 300x300
  Display.setLocation((Display.getDisplayMode().getWidth() - 300) / 2,
            (Display.getDisplayMode().getHeight() - 300) / 2);
  Display.setFullscreen(false);
  Display.setTitle("3D-huone");
  Display.create();
 

  GL11.glEnable(GL11.GL_DEPTH_TEST);
 
  seinat = GL11.glGenLists(1);
  GL11.glNewList(seinat, GL11.GL_COMPILE);
       GL11.glBegin(GL11.GL_QUADS);
 
       // etuosa
 
        GL11.glColor3d(1, 0, 0); // taka 
        GL11.glVertex3d( -1.0, -1.0, -1.0);
        GL11.glVertex3d(1.0, -1.0, -1.0);
        GL11.glVertex3d( 1.0, 1.0, -1.0);
        GL11.glVertex3d( -1.0, 1.0, -1.0);
 

        GL11.glColor3d(1, 0, 0); // etu 
        GL11.glVertex3d( -1.0, -1.0, 1.0);
        GL11.glVertex3d(1.0, -1.0, 1.0);
        GL11.glVertex3d(1.0, 1.0, 1.0);
        GL11.glVertex3d(-1.0, 1.0, 1.0);
 

        GL11.glColor3f(166.0f, 83.0f, 0.0f); // pohja
        GL11.glVertex3d(-1.0, -1.0, -1.0);
        GL11.glVertex3d( -1.0, -1.0, 1.0);
        GL11.glVertex3d( 1.0, -1.0, 1.0);
        GL11.glVertex3d(1.0, -1.0, -1.0);
 

        GL11.glColor3d(0, 1, 0); // katto
        GL11.glVertex3d(-1.0, 1.0, -1.0);
        GL11.glVertex3d( -1.0, 1.0, 1.0);
        GL11.glVertex3d( 1.0, 1.0, 1.0);
        GL11.glVertex3d(1.0, 1.0, -1.0);
 

        GL11.glColor3d(0, 0, 1); // vasen seinä
        GL11.glVertex3d( -1.0, -1.0, -1.0);
        GL11.glVertex3d( -1.0, 1.0, -1.0);
        GL11.glVertex3d( -1.0, 1.0, 1.0);
        GL11.glVertex3d( -1.0, -1.0, 1.0);
 
 
 
        GL11.glColor3d(0, 0, 1); // oikea seinä
        GL11.glVertex3d(1.0, -1.0, -1.0);
        GL11.glVertex3d(1.0, 1.0, -1.0);
        GL11.glVertex3d(1.0, 1.0, 1.0);
        GL11.glVertex3d(1.0, -1.0, 1.0);
 
        GL11.glEnd();
  GL11.glEndList();
 
 
 
  GL11.glEnable(GL11.GL_NORMALIZE);
 
  GL11.glMatrixMode(GL11.GL_PROJECTION);
 
 

   GL11.glLoadIdentity();
  
 
  float h = (float) 300 / (float) 300;
  GL11.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
  GL11.glMatrixMode(GL11.GL_MODELVIEW);
  GL11.glLoadIdentity();
 
 }
 
 

public void yaw(float amount)
    {
        
        yaw += amount;
    }
 
    
    public void pitch(float amount)
    {   
               
        pitch -= amount;
    }
 
 

    public void kaveleeteen(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw));
    }
 
 
    public void kaveletaakse(float distance)
    {
        position.x += distance * (float)Math.sin(Math.toRadians(yaw));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
    }
 
    public void kavelevasen(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
    }
 
    public void kaveleoikea(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
    }
 
   }