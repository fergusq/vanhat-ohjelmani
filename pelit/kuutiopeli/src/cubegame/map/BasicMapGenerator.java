package cubegame.map;

import java.util.Random;

import cubegame.Cube;
import cubegame.map.level.Level;

public class BasicMapGenerator {
	
	CubeMap map;
	int size = 128;
	int half_size = size / 2;
	
	public BasicMapGenerator(){
		map = new CubeMap(size);
	}
	
	public CubeMap generate(){
		generate3DMap();
		map.tick();
		
		putGeneralMaterials();
		map.tick();
		
		generateCaves();
		map.tick();
		
		populate();
		map.tick();
		
		generateWalls();
		map.tick();
		return map;
	}
	
	public Level generateLevel(Level l){
		map = l;
		
		generate3DMap();
		l.tick();
		
		putGeneralMaterials();
		l.tick();
		
		generateCaves();
		l.tick();
		
		populate();
		l.tick();
		
		generateWalls();
		l.tick();
		return l;
	}
	
	private void generateWalls() {
		int height = 70;
		
		for (int x = 0; x < size; x++){
			for (int z = 0; z < size; z++){
				map.setCube(x, 0, z, Cube.PERMANENT);
			}
		}
		
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				if (y <= height) map.setCube(x, y, 0, Cube.PERMANENT);
				else map.setCube(x, y, 0, Cube.AIR);
			}
		}
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				if (y <= height) map.setCube(x, y, size-1, Cube.PERMANENT);
				else map.setCube(x, y, size-1, Cube.AIR);
			}
		}
		for (int z = 0; z < size; z++){
			for (int y = 0; y < size; y++){
				if (y <= height) map.setCube(0, y, z, Cube.PERMANENT);
				else map.setCube(0, y, z, Cube.AIR);
			}
		}
		for (int z = 0; z < size; z++){
			for (int y = 0; y < size; y++){
				if (y <= height) map.setCube(size-1, y, z, Cube.PERMANENT);
				else map.setCube(size-1, y, z, Cube.AIR);
			}
		}
		
		for (int x = 0; x < size; x++){
			for (int z = 0; z < size; z++){
				//map.setCube(x, size-1, z, Cube.SKYCUBE);
			}
		}
		
	}

	public void generateHeightMap(){
		
		PerlinMapGenerator g = new PerlinMapGenerator(size, 0);
		g.init();
		
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				for (int z = 0; z < size; z++){
					double noise = g.heights[x][z]*10+70;
					int cubeType = 0;
					float height = y + g.heights[x][z]*10;
					if (map.getCube(x, y, z) != 0) continue;
					if (noise > y){
						if (height > MapLayer.GRASS.getLevel()){
							cubeType = Cube.DIRT;
						} else if (height > MapLayer.STONE.getLevel()){
							cubeType = Cube.STONE_1;
						} else if (height <= MapLayer.STONE.getLevel()){
							cubeType = Cube.STONE_2;
						} else cubeType = 1;
					}
					map.setCube(x, y, z, cubeType);
				}
			}
		}
	}
	
	private void generate3DMap(){
		PerlinGenerator g = new PerlinGenerator("YangRED".hashCode());
		Random rand = new Random("YangRED".hashCode());
		
		float scale = 0.1f;
		float yFactor = 2;
		float f3 = 10;
		
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				for (int z = 0; z < size; z++){
					int height = y - 64;
					if (height + (g.noise(x*scale, y*scale, z*scale)*f3-1)*yFactor < 0) map.setCube(x, y, z, Cube.STONE_1);
					else map.setCube(x, y, z, 0);
				}
			}
		}
	}
	
	private void putGeneralMaterials(){
		Random rand = new Random("YangRED".hashCode());
		
		for (int x = 0; x < size; x++){
			for (int z = 0; z < size; z++){
				int y = getTopCube(x, z);
				for (int i = y; i > y-3-rand.nextInt(10); i--){
					if (map.getCube(x, i, z) != 0) map.setCube(x, i, z, Cube.DIRT);
				}
			}
		}
	}
	
	public void generateCaves(){
		
		boolean[][][] flag = new boolean[size][size][size];
		
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				for (int z = 0; z < size; z++){
					double noise = ImprovedNoise.noise(8.1 * x / (float)size, 6.1 * y / (float)size, 6.1 * z / (float)size);
					if (noise < 0){
						flag[x][y][z] = true;
					}
				}
			}
		}
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				for (int z = 0; z < size; z++){
					double noise = ImprovedNoise.noise(6.1 * x / (float)size, 8.1 * y / (float)size, 6.1 * z / (float)size);
					int cubeType = map.getCube(x, y, z);
					if (noise < 0 && flag[x][y][z] && !isGroundCube(cubeType)){
						cubeType = 0;
					}
					map.setCube(x, y, z, cubeType);
				}
			}
		}
	}
	
	public boolean isGroundCube(int cubeType) {
		switch (cubeType) {
		case Cube.GRASS:
			return true;
		case Cube.DIRT:
			return true;
		case Cube.WATER:
			return true;
		default:
			break;
		}
		return false;
	}

	public void populate(){
		//generateSea();
		//generateCaves();
		generatePlants();
		//generateLakes();
	}

	public void generatePlants() {
		for (int x = 0; x < size; x++){
			for (int z = 0; z < size; z++){
				int y = getTopCube(x, z);
				if (map.getCube(x, y, z) == Cube.DIRT){
					map.setCube(x, y, z, Cube.GRASS);
				}
			}
		}
		generateTrees();
	}

	private void generateTrees() {
		Random dice = new Random();
		for (int x = 1; x < size-1; x++){
			for (int z = 1; z < size-1; z++){
				int y = getTopCube(x, z);
				if (map.getCube(x, y, z) == Cube.GRASS){
					if (dice.nextInt(1000) == 1){
						generateTree(x, z);
					}
				}
			}
		}
		
	}

	private void generateTree(int x, int z) {
		int y = getTopOfCube(x, z);
		
		
		if (getTopCube(x-1, z) > y) return;
		if (getTopCube(x+1, z) > y) return;
		if (getTopCube(x-1, z+1) > y) return;
		if (getTopCube(x+1, z+1) > y) return;
		if (getTopCube(x-1, z-1) > y) return;
		if (getTopCube(x+1, z-1) > y) return;
		if (getTopCube(x, z-1) > y) return;
		if (getTopCube(x, z+1) > y) return;
		
		map.setCube(x, y, z, Cube.TREE);
		map.setCube(x, y+1, z, Cube.TREE);
		map.setCube(x, y+2, z, Cube.TREE);
		map.setCube(x, y+3, z, Cube.TREE);
		map.setCube(x, y+4, z, Cube.TREE);
		map.setCube(x, y+5, z, Cube.LEAVES);
		map.setCube(x+1, y+4, z, Cube.LEAVES);
		map.setCube(x-1, y+4, z, Cube.LEAVES);
		map.setCube(x+1, y+4, z+1, Cube.LEAVES);
		map.setCube(x-1, y+4, z+1, Cube.LEAVES);
		map.setCube(x+1, y+4, z-1, Cube.LEAVES);
		map.setCube(x-1, y+4, z-1, Cube.LEAVES);
		map.setCube(x, y+4, z+1, Cube.LEAVES);
		map.setCube(x, y+4, z-1, Cube.LEAVES);
	}

	private int getTopCube(int x, int z) {
		for(int y = size-1; y > 0; y--){
			if (map.getCube(x, y, z) != 0){
				return y;
			}
		}
		return 0;
	}
	
	private int getTopOfCube(int x, int z) {
		for(int y = size-1; y > 0; y--){
			if (map.getCube(x, y, z) != 0){
				return y+1;
			}
		}
		return 0;
	}

	private void generateSea() {
		for (int x = 0; x < size; x++){
			for (int z = 0; z < size; z++){
				for (int y = size; y > 0; y--){
					if (y < MapLayer.SEALEVEL.getLevel()){
						if (map.getCube(x, y, z) == 0){
							map.setCube(x, y, z, Cube.WATER);
						}
						else break;
					}
				}
			}
		}
		
	}
	
}
