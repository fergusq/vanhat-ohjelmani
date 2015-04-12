package cubegame.multiplayer.packets;

public class MapPacket extends Packet {
	public int x;
	public int y;
	public int z;
	
	public MapPacket(int x, int y, int z, byte[] data) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
	}
	
	public byte[] data;
}
