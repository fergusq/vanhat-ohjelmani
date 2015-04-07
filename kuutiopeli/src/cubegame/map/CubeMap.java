package cubegame.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ProgressMonitor;

import org.lwjgl.util.vector.Vector3f;

import cubegame.BoundingBox;
import cubegame.Creature;
import cubegame.Cube;
import cubegame.CubeState;
import cubegame.CubeType;
import cubegame.Entity;
import cubegame.Human;
import cubegame.Texture;
import cubegame.physics.PhysicCube;
import cubegame.render.CubeRenderer;

public class CubeMap {
	public byte[][][] map;
	public byte[][][] addmap;
	
	public int size;
	private int half_size;
	
	public CubeMap(int size) {
		this.size = size;
		half_size = size / 2;
		map = new byte[size][size][size];
		addmap = new byte[size][size][size];
	}
	
	public void tick(){
		System.arraycopy(addmap, 0, map, 0, size);
	}
	
	public void setCubeType(int x, int y, int z, int type){
		if (x < -half_size || x > half_size-1) return;
		if (y < -half_size || y > half_size-1) return;
		if (z < -half_size || z > half_size-1) return;
		addmap[x+half_size][y+half_size][z+half_size] = (byte) type;
	}
	
	public void setCube(int x, int y, int z, int type){
		if (x < 0 || x > size-1) return;
		if (y < 0 || y > size-1) return;
		if (z < 0 || z > size-1) return;
		addmap[x][y][z] = (byte) type;
	}
	
	public int getCubeType(int x, int y, int z){
		if (x < -half_size || x > half_size-1) return 0;
		if (y < -half_size || y > half_size-1) return 0;
		if (z < -half_size || z > half_size-1) return 0;
		return map[x+half_size][y+half_size][z+half_size];
	}
	
	public int getCube(int x, int y, int z){
		if (x < 0 || x > size-1) return 0;
		if (y < 0 || y > size-1) return 0;
		if (z < 0 || z > size-1) return 0;
		return map[x][y][z];
	}
	
	public boolean canPlaceCubeAt(int x, int y, int z){
		return isSolidCube(x, y, z);
	}
	
	public int getTopCube(int x, int z) {
		for(int y = size-1; y > 0; y--){
			if (getCube(x, y, z) != 0){
				return y;
			}
		}
		return 0;
	}
	
	public int getTopOfCube(int x, int z) {
		for(int y = size-1; y > 0; y--){
			if (getCube(x, y, z) != 0){
				return y+1;
			}
		}
		return 0;
	}
	
	public void convertFromEntityMapToCubeMap(HashMap<String, Entity> entities){
		for (int xp = -1; xp < 1; xp++){
			for (int yp = -2; yp < 3; yp++){
				for (int zp = -1; zp < 1; zp++){
					Entity e = entities.get(xp+"x"+yp+"x"+zp);
					if (e != null && e instanceof Cube){
						map[(int) e.getX()+half_size][(int) e.getY()+half_size][(int) e.getZ()+half_size] = (byte) ((Cube) e).getType().getId();
					}
				}
			}
		}
	}
	
	public BoundingBox canMove(float x, float y, float z, float height) {
		Creature ent = new Creature(x, y, z, 0.4f, null);
		BoundingBox entBox = new BoundingBox(ent, height, false);
		PhysicCube cube;
		for (int xp = -1; xp < 1; xp++){
			for (int yp = -2; yp < 3; yp++){
				for (int zp = -1; zp < 1; zp++){
					cube = new PhysicCube((int)(Math.floor((double)x))+xp, (int)(Math.floor((double)x))+yp, (int)(Math.floor((double)x))+zp, getCubeType((int)(Math.floor((double)x))+xp, (int)(Math.floor((double)x))+yp, (int)(Math.floor((double)x))+zp), 1);
					BoundingBox box = new BoundingBox(cube, (float)1, true);
					if (entBox.intersectsWith(box) && cube.getType().isSolid()) {
						return box;
					}
				}
			}
		}
		return null;
	}
	
	public boolean isPlayerInsideOfLiquid(Human p){
		//Creature ent = new Creature(-p.getPositionX(), -p.getPositionY(), -p.getPositionZ(), 0.4f, null);
		//Cube cube;
		//BoundingBox entBox = new BoundingBox(ent, 0.0f, false);
		
		/*for (int x = -1; x < 1; x++){
			for (int y = -1; y < 1; y++){
				for (int z = -1; z < 1; z++){
					cube = new Cube(p.getPositionX()+x, p.getForwardY()+y, p.getPositionZ()+z, map[(int) (x)+half_size][(int) (y)+half_size][(int) (z)+half_size], 1);
					BoundingBox box = new BoundingBox((Entity) cube, (float)1, true);
					if (box.intersectsWith(entBox) && cube.getType().isLiquid()) return true;
				}
			}
		}*/
		
		
		return false;
	}
	
	public float getLiquidDensity(float x, float y, float z, float height) {
		Creature ent = new Creature(x, y, z, 0.4f, null);
		BoundingBox entBox = new BoundingBox(ent, height, false);
		Cube cube;
		/*for (int xp = -1; xp < 1; xp++){
			for (int yp = -1; yp < 1; yp++){
				for (int zp = -1; zp < 1; zp++){
					cube = new Cube(x+xp, y+yp, z+zp, map[(int) (x+xp)+half_size][(int) (y+yp)+half_size][(int) (z+zp)+half_size], 1);
					BoundingBox box = new BoundingBox((Entity) cube, (float)1, true);
					if (box.intersectsWith(entBox) && cube.getType().isLiquid()) return 0.004f;
				}
			}
		}*/
		return 0.009f;
	}
	
	public boolean isCube(int x, int y, int z){
		if (x < -half_size || x >= half_size) return false;
		if (y < -half_size || y >= half_size) return false;
		if (z < -half_size || z >= half_size) return false;
		if (map[x+half_size][y+half_size][z+half_size] != 0) return true;
		return false;
	}
	
	public boolean isOpaqueSolidCube(int x, int y, int z){
		if (x < -half_size || x >= half_size) return false;
		if (y < -half_size || y >= half_size) return false;
		if (z < -half_size || z >= half_size) return false;
		if (CubeType.getTypeFromNumber(map[x+half_size][y+half_size][z+half_size]).isSolid() && !CubeType.getTypeFromNumber(map[x+half_size][y+half_size][z+half_size]).isNonOpaque()) return true;
		return false;
	}
	
	public boolean isSolidCube(int x, int y, int z){
		if (x < -half_size || x >= half_size) return false;
		if (y < -half_size || y >= half_size) return false;
		if (z < -half_size || z >= half_size) return false;
		if (CubeType.getTypeFromNumber(map[x+half_size][y+half_size][z+half_size]).isSolid()) return true;
		return false;
	}
	
	public boolean isOpaqueCube(int x, int y, int z){
		if (x < -half_size || x >= half_size) return false;
		if (y < -half_size || y >= half_size) return false;
		if (z < -half_size || z >= half_size) return false;
		if (!CubeType.getTypeFromNumber(map[x+half_size][y+half_size][z+half_size]).isNonOpaque()) return true;
		return false;
	}
	
	public boolean checkIsTopOfCubeInShadow(int x, int y, int z) {
		for (int i = y+1; i < 16-y; i++){
			if (isOpaqueCube(x, i, z)) return true;
		}
		
		return false;
	}
	
	public boolean checkIsNorthOfCubeInShadow(int x, int y, int z) {
		for (int i = y; i < 16-y; i++){
			if (isOpaqueCube(x, i, z+1)) return true;
		}
		
		return false;
	}
	
	public boolean checkIsSouthOfCubeInShadow(int x, int y, int z) {
		for (int i = y; i < 16-y; i++){
			if (isOpaqueCube(x, i, z-1)) return true;
		}
		
		return false;
	}
	
	public boolean checkIsEastOfCubeInShadow(int x, int y, int z) {
		for (int i = y; i < 16-y; i++){
			if (isOpaqueCube(x-1, i, z)) return true;
		}
		
		return false;
	}
	
	public boolean checkIsWestOfCubeInShadow(int x, int y, int z) {
		for (int i = (int) y; i < 16-y; i++){
			if (isOpaqueCube(x+1, i, z)) return true;
		}
		
		return false;
	}
	
	public void render() throws IOException{
		CubeRenderer render = new CubeRenderer();
		for (int x = 1; x < size-1; x++){
			for (int y = 1; y < size-1; y++){
				for (int z = 1; z < size-1; z++){
					//int type = map[(int) (x)][(int) (y)][(int) (z)];
					//Texture texture = Cube.getTextureOfType(CubeType.getTypeFromNumber(type));
					//render.renderCube(x-half_size, y-half_size, z-half_size, texture, false, CubeType.getTypeFromNumber(type), true, true, true, true, true, true, checkIsTopOfCubeInShadow(x, y, z), checkIsEastOfCubeInShadow(x, y, z), checkIsWestOfCubeInShadow(x, y, z), checkIsNorthOfCubeInShadow(x, y, z), checkIsSouthOfCubeInShadow(x, y, z), 0.5f);
				}
			}
		}
	}
	
	public ArrayList<Entity> getEntityMap(){
		ArrayList<Entity> e = new ArrayList<Entity>();
		int counter = 0;
		int counter2 = 0;
		final double finalCount = size-1 * size-1 * size-1;
		int prev = 0;
		ProgressMonitor pm = new ProgressMonitor(null, "Converting to entity map...", "", 0, 100);
		pm.setProgress(0);
		for (int x = 1; x < size-1; x++){
			for (int y = 1; y < size-1; y++){
				for (int z = 1; z < size-1; z++){
					counter2++;
					int type = map[(int) (x)][(int) (y)][(int) (z)];
					if (type == 0) continue;
					if (type != 0){
						Cube cube = new Cube(x-half_size, y-half_size, z-half_size, CubeType.getTypeFromNumber(type));
						e.add(cube);
						//System.out.println("Cube converted: " + x + ", " + y + ", " + z + ".");
						counter++;
					}
					double p = counter2 / finalCount * 100;
					if ((int)p % 10 == 0 && prev != p); //System.out.println("Converting: " + (int)p); 
					pm.setProgress((int) p);
					pm.setNote((int)p + " %");
					prev = (int) p;
				}
			}
		}
		pm.close();
		System.out.println("Converted: " + counter + " cubes.");
		return e;
	}
	
	public double[][] getHeightMap(){
		double[][] answer = new double[size*10][size*10];
		for (int rawX = 0; rawX < size*10; rawX++){
			for (int rawY = 0; rawY < size*10; rawY++){
				answer[rawX][rawY] = -1.0;
			}
		}
		int maxY = 0;
		for (int x = 0; x < size-1; x++){
			for (int y = 0; y < size-1; y++){
				for (int z = 0; z < size-1; z++){
					if (map[(int) (x)][(int) (y)][(int) (z)] != 0 && y > maxY) maxY = y;
				}
			}
		}
		int minY = maxY;
		for (int x = 0; x < size-1; x++){
			for (int y = minY; y < 0; y++){
				for (int z = 0; z < size-1; z++){
					if (map[(int) (x)][(int) (y)][(int) (z)] != 0 && y < minY) minY = y;
				}
			}
		}
		for (int x = 0; x < size-1; x++){
			for (int y = 0; y < size-1; y++){
				for (int z = 0; z < size-1; z++){
					int type = map[(int) (x)][(int) (y)][(int) (z)];
					if (type != 0){
						for (int xm = x*10; xm < x*10 + 10; xm++){
							for (int ym = z*10; ym < z*10 + 10; ym++){
								answer[xm][ym] = ((double)y) / ((double)(maxY)/2) - (double)1.0;
							}
						}
					}
					else{
						answer[x*10][z*10] = 0.9;
					}
				}
			}
		}
		return answer;
		
	}
}
