package cubegame;


public enum CubeType {
	air(0, false, true, false, 0, -1),
	net(1, false, false, true, 0, 36, "Net"),
	dirt(2, false, false, true, CubeType.materialDirt, 36, "Dirt"),
	stone1(3, false, false, true, CubeType.materialStone, 36, "Stone"),
	stone2(4, false, false, true, CubeType.materialStone, 36, "Stone"),
	glass(5, false, true, true, CubeType.materialGlass, 8, "Glass"),
	metal1(6, false, false, true, 0, 36, "Metal"),
	metal2(7, false, false, true, 0, 36, "Metal"),
	wood(8, false, false, true, CubeType.materialWood, 36, "Wood"),
	grass(9, false, false, true, CubeType.materialDirt, 36, "Grass"),
	blackAndYellow(10, false, false, true, 0, 36, "Black And Yellow"),
	snow(11, false, false, true, 0, 16, "Snow"),
	water(12, true, true, false, CubeType.materialLiquid, -1, "Water"),
	lava(13, true, true, false, CubeType.materialLiquid, -1, "Lava"),
	acid(14, true, true, false, CubeType.materialLiquid, -1, "Poison"),
	light(15, false, false, true, 0, 36, "Light"),
	sign(16, false, true, false, 0, 36, "Sign"),
	tree(17, false, false, true, CubeType.materialWood, 36, "Tree"),
	leaves(18, false, true, true, 0, 6, "Leaves"),
	fire(19, false, false, false, 0, 36, "Fire"),
	selectFrame(101, false, true, true, 0, 36, "Frame"),
	unknow(102, false, false, true, 0, 36, "Unknow"),
	permanent(103, false, true, true, CubeType.materialStone, -1, "Permanent"),
	skycube(104, false, true, true, 0, 36, "Sky");
	
	
	private int id;
	private boolean isLiquid;
	private boolean isTransparent;
	private boolean solid;
	private int material;
	int handDuration;
	public String name;
	public static final int MAX_TYPES = 104;
	public static CubeType[] typeList;
	
	private CubeType(int id, boolean isLiquid, boolean isTransparent, boolean isSolid, int material, int hd){
		this.setId(id);
		this.setLiquid(isLiquid);
		this.setTransparent(isTransparent);
		this.setSolid(isSolid);
		this.setMaterial(material);
		name = "type";
		handDuration = hd;
		addToList();
	}
	
	private CubeType(int id, boolean isLiquid, boolean isTransparent, boolean isSolid, int material, int hd, String name){
		this.setId(id);
		this.setLiquid(isLiquid);
		this.setTransparent(isTransparent);
		this.setSolid(isSolid);
		this.setMaterial(material);
		this.name = name;
		handDuration = hd;
		addToList();
	}

	public boolean isNonOpaque() {
		return isTransparent;
	}

	public void setTransparent(boolean isTransparent) {
		this.isTransparent = isTransparent;
	}

	public static int getMaxTypes() {
		return MAX_TYPES;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setLiquid(boolean isLiquid) {
		this.isLiquid = isLiquid;
	}

	public boolean isLiquid() {
		return isLiquid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isSolid() {
		return solid;
	}

	private void addToList(){
		if (typeList == null) typeList = new CubeType[MAX_TYPES+1];
		typeList[id] = this;
	}
	
	public static CubeType getTypeFromNumber(int id){
		if (id > typeList.length) return air;
		/*switch(id){
		case 0:
			return air;
		case 1:
			return net;
		case 2:
			return dirt;
		case 3: 
			return stone1;
		case 4:
			return stone2;
		case 5:
			return glass;
		case 6:
			return metal1;
		case 7:
			return metal2;
		case 8:
			return wood;
		case 9:
			return grass;
		case 10:
			return blackAndYellow;
		case 11:
			return snow;
		case 12:
			return water;
		case 13:
			return lava;
		case 14:
			return acid;
		case 15:
			return light;
		case 16:
			return sign;
		case 17:
			return tree;
		case 18:
			return leaves;
		case 19:
			return fire;
		case 101:
			return selectFrame;
		case 103:
			return permanent;
		case 104:
			return skycube;
		default:
			return air;
		}*/
		return CubeType.typeList[id];
	
	}
	
	public void setMaterial(int material) {
		this.material = material;
	}

	public int getMaterial() {
		return material;
	}

	public final static int materialStone = 1;
	public final static int materialDirt = 2;
	public final static int materialWood = 3;
	public final static int materialLiquid = 4;
	public final static int materialGlass = 5;
	
}
