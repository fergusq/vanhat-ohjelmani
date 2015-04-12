package cubegame.save;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.loader.WavefrontObject;

import cubegame.Cube;
import cubegame.CubeState;
import cubegame.CubeType;
import cubegame.Entity;
import cubegame.Human;
import cubegame.Texture;


/**
 * This is my famous class to read and write savefiles
 * 
 * @author iikka
 *
 */
public class CubeMapFileReader {

	public static ArrayList<Entity> getSavefile(String filename) {
		String mapCode = "";
		ArrayList<Entity> map = new ArrayList<Entity>();
		map.clear();
		String line = "";
		int count = 0;
		try {
			FileReader r = new FileReader(filename);
			BufferedReader reader = new BufferedReader(r);
			while ((line = reader.readLine()) != null) {
				mapCode += line + "\n";
				map.add(parseLine(line));
				//System.out.println("Cube read");
				count++;
			}
			r.close();
			reader.close();
		} catch (Exception e) {
			System.out.println("Fail reading map code: " + line);
			System.out.println("Fail reading map code @ " + filename + ":" + count + " " + e.toString());
			e.printStackTrace();
		}
		return map;
	}

	public static boolean createSavefile(String file, ArrayList map){
		try { 
			File savefile = new File(file);
			FileWriter writer = new FileWriter(savefile);
			for (Object obj : map){
				if (obj instanceof Cube){
					Cube cube = (Cube) obj;
					writer.write("C" + cube.getType().getId()+"C"+cube.getX()+"C"+cube.getY()+"C"+cube.getZ()+"\n");
					//System.out.println("Cube saved: " + "C" + cube.getType().getId()+"C"+cube.getX()+"C"+cube.getY()+"C"+cube.getZ());
				}
				if (obj instanceof Human){
					Human human = (Human) obj;
					writer.write("H"+human.getX()+"H"+human.getY()+"H"+human.getZ()+"H"+human.getID()+"\n");
					System.out.println("Human saved: " + "H"+human.getX()+"H"+human.getY()+"H"+human.getZ());
				}
			}
			writer.flush();
			writer.close();
			
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}

		return false;
		
	}
	
	public static Entity parseLine(String line) {
		//if (!line.matches("[0-102]C[-64-64]C[-64-64]C[-64-64]")) return null;
		if (line.startsWith("C")){
			String[] rawLine = line.split("C");
			Cube cube = new Cube(
					Float.parseFloat(rawLine[2]), 
					Float.parseFloat(rawLine[3]), 
					Float.parseFloat(rawLine[4]), 
					CubeType.getTypeFromNumber(Integer.parseInt(rawLine[1])));
			//System.out.println("File parsed: " + line);
			return cube;
		}
		if (line.startsWith("H")){
			String[] rawLine = line.split("H");
			Human human = new Human(new Texture(0, 0), new WavefrontObject("res/ball.obj"), 
					CubeState.createVector(Float.parseFloat(rawLine[1]), 
					Float.parseFloat(rawLine[2]), 
					Float.parseFloat(rawLine[3])));
			//System.out.println("File parsed: " + line);
			return human;
		}
		String[] rawLine = line.split("C");
		Cube cube = new Cube(
				Float.parseFloat(rawLine[2]), 
				Float.parseFloat(rawLine[3]), 
				Float.parseFloat(rawLine[4]), 
				CubeType.getTypeFromNumber(Integer.parseInt(rawLine[1])));
		//System.out.println("File parsed: " + line);
		return cube;
	}

	public static String getFreeID() {
		Random dice = new Random();
		while (true){
			int id = dice.nextInt();
			if (!ids.contains(new Integer(id))) {
				ids.add(new Integer(id)); 
				return "" + id;
			}
		}
	}
	
	private static final ArrayList<Integer> ids = new ArrayList<Integer>();

	public static ArrayList<Entity> getServerSavefile(String filename) {
		String mapCode = "";
		ArrayList<Entity> map = new ArrayList<Entity>();
		map.clear();
		String line = "";
		int count = 0;
		try {
			FileReader r = new FileReader(filename);
			BufferedReader reader = new BufferedReader(r);
			while ((line = reader.readLine()) != null) {
				mapCode += line + "\n";
				map.add(parseServerLine(line));
				//System.out.println("Cube read");
				count++;
			}
			r.close();
			reader.close();
		} catch (Exception e) {
			System.out.println("Fail reading map code: " + line);
			System.out.println("Fail reading map code @ " + filename + ":" + count + " " + e.toString());
			e.printStackTrace();
		}
		return map;
	}

	private static Entity parseServerLine(String line) {
		if (line.startsWith("C")){
			String[] rawLine = line.split("C");
			Cube cube = new Cube(
					Float.parseFloat(rawLine[2]), 
					Float.parseFloat(rawLine[3]), 
					Float.parseFloat(rawLine[4]), 
					CubeType.getTypeFromNumber(Integer.parseInt(rawLine[1])));
			//System.out.println("File parsed: " + line);
			return cube;
		}
		if (line.startsWith("H")){
			String[] rawLine = line.split("H");
			//System.out.println("File parsed: " + line);
		}
		String[] rawLine = line.split("C");
		Cube cube = new Cube(
				Float.parseFloat(rawLine[2]), 
				Float.parseFloat(rawLine[3]), 
				Float.parseFloat(rawLine[4]), 
				CubeType.getTypeFromNumber(Integer.parseInt(rawLine[1])));
		//System.out.println("File parsed: " + line);
		return cube;
	}

	public static boolean createServerSavefile(String file, ArrayList map){
		try { 
			File savefile = new File(file);
			FileWriter writer = new FileWriter(savefile);
			for (Object obj : map){
				if (obj instanceof Cube){
					Cube cube = (Cube) obj;
					writer.write("C" + cube.getType().getId()+"C"+cube.getX()+"C"+cube.getY()+"C"+cube.getZ()+"\n");
					//System.out.println("Cube saved: " + "C" + cube.getType().getId()+"C"+cube.getX()+"C"+cube.getY()+"C"+cube.getZ());
				}
			}
			writer.flush();
			writer.close();
			
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}

		return false;
		
	}
}
