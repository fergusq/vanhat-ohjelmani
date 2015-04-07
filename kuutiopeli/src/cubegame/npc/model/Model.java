package cubegame.npc.model;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import cubegame.Creature;
import cubegame.CubeState;
import cubegame.Entity;
import geometry.Box;
import geometry.Face;

public abstract class Model {
	protected ArrayList<Box> modelMap = new ArrayList<Box>();

	public Model() {
	}

	public abstract void render();

	protected void addHexahedron(float x, float y, float z, float sizeX,
			float sizeY, float sizeZ) {
		Creature entity = new Creature(x, y, z, sizeX * sizeZ, null);
		modelMap.add(new Box(getTopAFromEntityPosition(entity, sizeX, sizeY,
				sizeZ), getTopBFromEntityPosition(entity, sizeX, sizeY, sizeZ),
				getTopCFromEntityPosition(entity, sizeX, sizeY, sizeZ),
				getTopDFromEntityPosition(entity, sizeX, sizeY, sizeZ),
				getBottomAFromEntityPosition(entity, sizeX, sizeY, sizeZ),
				getBottomBFromEntityPosition(entity, sizeX, sizeY, sizeZ),
				getBottomCFromEntityPosition(entity, sizeX, sizeY, sizeZ),
				getBottomDFromEntityPosition(entity, sizeX, sizeY, sizeZ)));
	}

	protected void drawHexahedronsFromModelMap() {
		//GL11.glPushMatrix();
		for (int i = 0; i < modelMap.size(); i++) {

			if (modelMap.get(i) instanceof Box) {

				Box b = modelMap.get(i);
				
				drawFace(b.getTop());
			}
		}
		//GL11.glPopMatrix();
	}

	public static void drawFace(Face f) {

		GL11.glBegin(4);

		GL11.glNormal3f(f.getA().x, f.getA().y, f.getA().z);
		
		GL11.glVertex3f(f.getA().x, f.getA().y, f.getA().z);
		GL11.glVertex3f(f.getB().x, f.getB().y, f.getB().z);
		GL11.glVertex3f(f.getC().x, f.getC().y, f.getC().z);
		GL11.glVertex3f(f.getD().x, f.getD().y, f.getD().z);

		GL11.glEnd();

	}

	public static Vector3f getTopAFromEntityPosition(Entity entity,
			float sizeX, float sizeY, float sizeZ) {
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();
		return CubeState.createVector(x - sizeX, y + sizeY, z + sizeZ);

	}

	public static Vector3f getTopBFromEntityPosition(Entity entity,
			float sizeX, float sizeY, float sizeZ) {
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x - sizeX, y + sizeY, z - sizeZ);
	}

	public static Vector3f getTopCFromEntityPosition(Entity entity,
			float sizeX, float sizeY, float sizeZ) {
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x + sizeX, y + sizeY, z + sizeZ);
	}

	public static Vector3f getTopDFromEntityPosition(Entity entity,
			float sizeX, float sizeY, float sizeZ) {
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x - sizeX, y + sizeY, z - sizeZ);
	}

	public static Vector3f getBottomAFromEntityPosition(Entity entity,
			float sizeX, float sizeY, float sizeZ) {
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x - sizeX, y - sizeY, z - sizeZ);
	}

	public static Vector3f getBottomBFromEntityPosition(Entity entity,
			float sizeX, float sizeY, float sizeZ) {
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x - sizeX, y - sizeY, z + sizeZ);
	}

	public static Vector3f getBottomCFromEntityPosition(Entity entity,
			float sizeX, float sizeY, float sizeZ) {
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x + sizeX, y - sizeY, z + sizeZ);
	}

	public static Vector3f getBottomDFromEntityPosition(Entity entity,
			float sizeX, float sizeY, float sizeZ) {
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x + sizeX, y - sizeY, z - sizeZ);
	}
}
