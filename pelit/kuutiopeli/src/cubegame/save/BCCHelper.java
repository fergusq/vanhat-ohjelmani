package cubegame.save;

import cubegame.CubeState;
import cubegame.Entity;
import cubegame.Human;
import cubegame.Texture;
import cubegame.map.CubeMap;
import cubegame.multiplayer.HumanMPPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.loader.WavefrontObject;

public class BCCHelper {
	
	public static void writeBCCPlayerFile(String file, HumanMPPacket h) throws IOException{
		DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
		
		byte[] output = new byte[4];
		
		output[0] = (new Integer((int)h.getX())).byteValue();
		output[1] = (new Integer((int)h.getY())).byteValue();
		output[2] = (new Integer((int)h.getZ())).byteValue();
		
		out.write(output);
	}
	
	public static Human readBCCPlayerFile(String file) throws IOException{
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		
		Human human = new Human(new Texture(0, 0), new WavefrontObject("res/ball.obj"), 
				CubeState.createVector(in.readByte(), 
						in.readByte(), 
						in.readByte()));
		
		return human;
	}
	
	public static void createBCCFile(String file, CubeMap map) throws IOException{
		DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
		
		byte[] output = new byte[128*128*128];
		
		int size = 128;
		
		int counter = 0;
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				for (int z = 0; z < size; z++){
					output[counter] = (new Integer(map.map[x][y][z])).byteValue();
					counter++;
				}
			}
		}
		out.write(output);
	}
	
	public static CubeMap readBCCFile(String file) throws IOException{
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		
		int size = 128;
		
		CubeMap map = new CubeMap(size);
		
		int counter = 0;
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				for (int z = 0; z < size; z++){
					byte b = in.readByte();
					int i = (new Byte(b)).intValue();
					map.setCube(x, y, z, i);
					counter++;
				}
			}
		}
		return map;
	}
	
	public static ArrayList<Entity> readBCCFileAndReturnEntityMap(String filename) throws IOException{
		return readBCCFile(filename).getEntityMap();
	}
	
	/*public static void saveEntityMapAsBCCFile(String filename, ArrayList<Entity> list) throws IOException{
		CubeMap map = new CubeMap();
		map.convertFromEntityMapToCubeMap(list);
		createBCCFile(filename, map);
	}*/
}
