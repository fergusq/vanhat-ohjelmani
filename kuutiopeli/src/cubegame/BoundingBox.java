package cubegame;

import org.lwjgl.util.vector.Vector3f;

import geometry.Box;

public class BoundingBox extends Box {
	private Entity entity;
	public float xP;
	public float xN;
	public float yN;
	public float yP;
	public float zN;
	public float zP;

	public BoundingBox(Entity entity, float height, boolean fromCorner) {
		super(getTopAFromEntityPosition(entity),
				getTopBFromEntityPosition(entity),
				getTopCFromEntityPosition(entity),
				getTopDFromEntityPosition(entity),
				getBottomAFromEntityPosition(entity),
				getBottomBFromEntityPosition(entity),
				getBottomCFromEntityPosition(entity),
				getBottomDFromEntityPosition(entity));
		this.entity = entity;

		if(!fromCorner){
			xP = (entity.getX() + entity.getSize());
			xN = (entity.getX() - entity.getSize());
			yP = (entity.getY() + entity.getSize());
			yN = (entity.getY() - height);
			zP = (entity.getZ() + entity.getSize());
			zN = (entity.getZ() - entity.getSize());
		}
		else{
			xP = (entity.getX() + entity.getSize());
			xN = entity.getX();
			yP = (entity.getY() + entity.getSize());
			yN = (entity.getY());
			zP = (entity.getZ() + entity.getSize());
			zN = entity.getZ();
		}
	}
	
	public BoundingBox(float x, float y, float z, float height, boolean fromCorner) {
		super(getTopAFromEntityPosition(new Cube(x, y, z, 0, 1)),
				getTopBFromEntityPosition(new Cube(x, y, z, 0, 1)),
				getTopCFromEntityPosition(new Cube(x, y, z, 0, 1)),
				getTopDFromEntityPosition(new Cube(x, y, z, 0, 1)),
				getBottomAFromEntityPosition(new Cube(x, y, z, 0, 1)),
				getBottomBFromEntityPosition(new Cube(x, y, z, 0, 1)),
				getBottomCFromEntityPosition(new Cube(x, y, z, 0, 1)),
				getBottomDFromEntityPosition(new Cube(x, y, z, 0, 1)));
		
		if(!fromCorner){
			xP = (x + 1);
			xN = (x - 1);
			yP = (y + 1);
			yN = (y - height);
			zP = (z + 1);
			zN = (z - 1);
		}
		else{
			xP = (x + 1);
			xN = x;
			yP = (y + 1);
			yN = y;
			zP = (z + 1);
			zN = z;
		}
	}

	public boolean intersectsWith(BoundingBox box) {

		if ((box.xN <= this.xP) && (box.xP >= this.xN) && (box.zN <= this.zP) && (box.zP >= this.zN) && (box.yN <= this.yP) && (box.yP >= this.yN))
			return true;
		else return false;
        
	}
	
	public double getYOffset(BoundingBox bb)
    {
		double y = 0;
        if(bb.xP <= xN || bb.xN >= xP) return y;
        if(bb.zP <= zN || bb.zN >= zP) return y;
        
        if(bb.yP <= yN)
        {
            double d = yN - bb.yP;
            y = d;
        }
        if(bb.yN >= yP)
        {
            double d = yP - bb.yN;
            y = d;
        }
        return y;
    }
	
	public double getXOffset(BoundingBox bb)
    {
		double x = 0;
        if(bb.yP <= yN || bb.yN >= yP) return x;
        if(bb.zP <= zN || bb.zN >= zP) return x;
        
        if(bb.xP <= xN)
        {
            double d = xN - bb.xP;
            x = d;
        }
        if(bb.xN >= xP)
        {
            double d = xP - bb.xN;
            x = d;
        }
        return x;
    }
	
	public double getZOffset(BoundingBox bb)
    {
		double z = 0;
        if(bb.xP <= xN || bb.xN >= xP) return z;
        if(bb.yP <= yN || bb.yN >= yP) return z;
        
        if(bb.zP <= zN)
        {
            double d = zN - bb.zP;
            z = d;
        }
        if(bb.zN >= zP)
        {
            double d = zP - bb.zN;
            z = d;
        }
        return z;
    }
	
	public double getYOffset(BoundingBox bb, double d1)
    {
		double y = d1;
        if(bb.xP <= xN || bb.xN >= xP) return y;
        if(bb.zP <= zN || bb.zN >= zP) return y;
        
        if(d1 > 0 && bb.yP <= yN)
        {
            double d = yN - bb.yP;
            y = d;
        }
        if(d1 < 0 && bb.yN >= yP)
        {
            double d = yP - bb.yN;
            y = d;
        }
        return y;
    }
	
	public double getXOffset(BoundingBox bb, double d1)
    {
		double x = d1;
        if(bb.yP <= yN || bb.yN >= yP) return x;
        if(bb.zP <= zN || bb.zN >= zP) return x;
        
        if(d1 > 0 && bb.xP <= xN)
        {
            double d = xN - bb.xP;
            x = d;
        }
        if(d1 < 0 && bb.xN >= xP)
        {
            double d = xP - bb.xN;
            x = d;
        }
        return x;
    }
	
	public double getZOffset(BoundingBox bb, double d1)
    {
		double z = d1;
        if(bb.xP <= xN || bb.xN >= xP) return z;
        if(bb.yP <= yN || bb.yN >= yP) return z;
        
        if(d1 > 0 && bb.zP <= zN)
        {
            double d = zN - bb.zP;
            z = d;
        }
        if(d1 < 0 && bb.zN >= zP)
        {
            double d = zP - bb.zN;
            z = d;
        }
        return z;
    }
	
	public void move(float x, float y, float z){
		xP += x;
		xN += x;
		yP += y;
		yN += y;
		zP += z;
		zN += z;
	}

	public float getX(){
		return xN;
	}
	public float getY(){
		return yN;
	}
	public float getZ(){
		return zN;
	}
	
	public static Vector3f getTopAFromEntityPosition(Entity entity) {
		float s = entity.getSize();
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();
		return CubeState.createVector(x - s, y + s, z + s);

	}

	public static Vector3f getTopBFromEntityPosition(Entity entity) {
		float s = entity.getSize();
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x - s, y + s, z - s);
	}

	public static Vector3f getTopCFromEntityPosition(Entity entity) {
		float s = entity.getSize();
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x + s, y + s, z + s);
	}

	public static Vector3f getTopDFromEntityPosition(Entity entity) {
		float s = entity.getSize();
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x - s, y + s, z - s);
	}

	public static Vector3f getBottomAFromEntityPosition(Entity entity) {
		float s = entity.getSize();
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x - s, y - s, z - s);
	}

	public static Vector3f getBottomBFromEntityPosition(Entity entity) {
		float s = entity.getSize();
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x - s, y - s, z + s);
	}

	public static Vector3f getBottomCFromEntityPosition(Entity entity) {
		float s = entity.getSize();
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x + s, y - s, z + s);
	}

	public static Vector3f getBottomDFromEntityPosition(Entity entity) {
		float s = entity.getSize();
		float x = entity.getX();
		float y = entity.getY();
		float z = entity.getZ();

		return CubeState.createVector(x + s, y - s, z - s);
	}
}
