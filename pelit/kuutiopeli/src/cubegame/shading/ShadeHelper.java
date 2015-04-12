package cubegame.shading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;


public class ShadeHelper {
		/**
	    * With the exception of syntax, setting up vertex and fragment shaders
	    * is the same.
	    * @param the name and path to the vertex shader
	    */
	    public static int createVertShader(String filename){
	        //vertShader will be non zero if succefully created

	        int vertShader = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
	        //if created, convert the vertex shader code to a String
	        if(vertShader==0){return 0;}
	        String vertexCode="";
	        String line;
	        try{
	        	FileReader r = new FileReader(filename);
	            BufferedReader reader=new BufferedReader(r);
	            while((line=reader.readLine())!=null){
	                vertexCode+=line + "\n";
	            }
	            r.close();
	            reader.close();
	        }catch(Exception e){
	            System.out.println("Fail reading vertex shading code: " + e.toString());
	            
	            vertexCode = "varying vec4 vertColor;\nvoid main(){\ngl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;\nvertColor = vec4(0.6, 0.3, 0.4, 1.0);\n}";
	        }
	        /*
	        * associate the vertex code String with the created vertex shader
	        * and compile
	        */
	        ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
	        ARBShaderObjects.glCompileShaderARB(vertShader);
	        //if there was a problem compiling, reset vertShader to zero
	        if(!printLogInfo(vertShader)){
	            vertShader=0;
	        }
	        //if zero we won't be using the shader
	        return vertShader;
	    }

	    /**
		    * With the exception of syntax, setting up vertex and fragment shaders
		    * is the same.
		    * @param the name and path to the fragment shader
		    */
	    public static int createFragShader(String filename){

	        int fragShader = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
	        if(fragShader==0){return 0;}
	            String fragCode="";
	            String line;
	        try{
	        	FileReader r = new FileReader(filename);
	            BufferedReader reader=new BufferedReader(r);
	            while((line=reader.readLine())!=null){
	                fragCode+=line + "\n";
	            }
	            r.close();
	            reader.close();
	        }catch(Exception e){
	            System.out.println("Fail reading fragment shading code: " + e.toString());
	            fragCode ="varying vec4 vertColor;\nvoid main(){\ngl_FragColor = vertColor;\n}";
	        }
	        ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
	        ARBShaderObjects.glCompileShaderARB(fragShader);
	        if(!printLogInfo(fragShader)){
	            fragShader=0;
	        }

	        return fragShader;
	    }

	    
	    /**
	     * oddly enough, checking the success when setting up the shaders is
	     * verbose upon success. If the reference iVal becomes greater
	     * than 1, the setup being examined (obj) has been successful, the
	     * information gets printed to System.out, and true is returned.
	     */
	     public static boolean printLogInfo(int obj){
	         IntBuffer iVal = BufferUtils.createIntBuffer(1);
	         ARBShaderObjects.glGetObjectParameterARB(obj,
	         ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

	         int length = iVal.get();
	         if (length > 1) {
	             // We have some info we need to output.
	             ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
	             iVal.flip();
	             ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
	             byte[] infoBytes = new byte[length];
	             infoLog.get(infoBytes);
	             String out = new String(infoBytes);
	             System.out.println("Info log:\n"+out);
	         }
	         else return false;
	         return true;
	     }

}
