package cubegame.multiplayer.packets;

public class CubeChangePacket extends Packet {
	public int x;
	public int y;
	public int z;
	
	public CubeChangePacket(int x, int y, int z, int type) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}
	
	public int type;
}
