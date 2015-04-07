package cubegame.multiplayer.packets;

public class PlayerMovePacket extends Packet {
	public float x;
	public float y;
	public float z;
	public float rotationY;
	public float pitch;
	public float legRot;
	public int id;
	
	public PlayerMovePacket(){
		
	}
	
	public PlayerMovePacket(int id, float x, float y, float z, float rotationY,
			float pitch, float legRot) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotationY = rotationY;
		this.pitch = pitch;
		this.legRot = legRot;
	}
}
