package geometry;

import org.lwjgl.util.vector.Vector3f;

public class Box {
	protected Face top;
	protected Face bottom;
	protected Face north;
	protected Face south;
	protected Face east;
	protected Face west;
	//                         Top/Bottom|Front/s  |left/e   |right/w  |back/n
	protected Vector3f topA;//  a - - c //b - - d //d - - c //a - - b //c - - a
	protected Vector3f topB;//  -     - //-     - //-     - //-     - //-     -
	protected Vector3f topC;//  -     - //-     - //-     - //-     - //-     -
	protected Vector3f topD;//  b - - d //a - - c //c - - d //b - - a //d - - b
	protected Vector3f bottomA;// a - - c
	protected Vector3f bottomB;// -     -
	protected Vector3f bottomC;// -     -
	protected Vector3f bottomD;// b - - d
	
	public Box(Vector3f topA, Vector3f topB, Vector3f topC, Vector3f topD,
			Vector3f bottomA, Vector3f bottomB, Vector3f bottomC, Vector3f bottomD) {
		super();
		this.topA = topA;
		this.topB = topB;
		this.topC = topC;
		this.topD = topD;
		this.bottomA = bottomA;
		this.bottomB = bottomB;
		this.bottomC = bottomC;
		this.bottomD = bottomD;
		this.top = new Face(topA, topB, topC, topD);
		this.bottom = new Face(bottomA, bottomB, bottomC, bottomD);
		this.north = new Face(topC, bottomB, topA, bottomD);
		this.south = new Face(topB, bottomA, topD, bottomC);
		this.east = new Face(topD, bottomC, topC, bottomD);
		this.west = new Face(topA, bottomB, topB, bottomA);
	}
	
	public Box(Face top, Face bottom, Face north, Face south, Face east, Face west) {
		super();
		this.top = top;
		this.bottom = bottom;
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
		
		topA = top.getA();
		topB = top.getB();
		topC = top.getC();
		topD = top.getD();
		
		bottomA = bottom.getA();
		bottomB = bottom.getB();
		bottomC = bottom.getC();
		bottomD = bottom.getD();
	}

	public Face getTop() {
		return top;
	}

	public void setTop(Face top) {
		this.top = top;
	}

	public Face getBottom() {
		return bottom;
	}

	public void setBottom(Face bottom) {
		this.bottom = bottom;
	}

	public Face getNorth() {
		return north;
	}

	public void setNorth(Face north) {
		this.north = north;
	}

	public Face getSouth() {
		return south;
	}

	public void setSouth(Face south) {
		this.south = south;
	}

	public Face getEast() {
		return east;
	}

	public void setEast(Face east) {
		this.east = east;
	}

	public Face getWest() {
		return west;
	}

	public void setWest(Face west) {
		this.west = west;
	}

	public Vector3f getTopA() {
		return topA;
	}

	public void setTopA(Vector3f topA) {
		this.topA = topA;
	}

	public Vector3f getTopB() {
		return topB;
	}

	public void setTopB(Vector3f topB) {
		this.topB = topB;
	}

	public Vector3f getTopC() {
		return topC;
	}

	public void setTopC(Vector3f topC) {
		this.topC = topC;
	}

	public Vector3f getTopD() {
		return topD;
	}

	public void setTopD(Vector3f topD) {
		this.topD = topD;
	}

	public Vector3f getBottomA() {
		return bottomA;
	}

	public void setBottomA(Vector3f bottomA) {
		this.bottomA = bottomA;
	}

	public Vector3f getBottomB() {
		return bottomB;
	}

	public void setBottomB(Vector3f bottomB) {
		this.bottomB = bottomB;
	}

	public Vector3f getBottomC() {
		return bottomC;
	}

	public void setBottomC(Vector3f bottomC) {
		this.bottomC = bottomC;
	}

	public Vector3f getBottomD() {
		return bottomD;
	}

	public void setBottomD(Vector3f bottomD) {
		this.bottomD = bottomD;
	}
	
}
