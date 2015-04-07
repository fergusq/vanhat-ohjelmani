package cubegame.multiplayer.packets;

public class LoginPacket extends Packet {

	public float x;
	public float y;
	public float z;
	public int id;
	
	public LoginPacket(){
		
	}
	
	public LoginPacket(float x, float y, float z, int id) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
	}

}
