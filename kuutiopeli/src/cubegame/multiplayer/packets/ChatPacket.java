package cubegame.multiplayer.packets;

public class ChatPacket extends Packet {

	private String msg;

	public ChatPacket(String message) {
		this.setMsg(message);
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
