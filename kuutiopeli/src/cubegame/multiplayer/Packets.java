package cubegame.multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cubegame.multiplayer.packets.ChatPacket;
import cubegame.multiplayer.packets.CubeChangePacket;
import cubegame.multiplayer.packets.LoginPacket;
import cubegame.multiplayer.packets.MapPacket;
import cubegame.multiplayer.packets.Packet;
import cubegame.multiplayer.packets.PlayerJoinPacket;
import cubegame.multiplayer.packets.PlayerMovePacket;

public class Packets {

	private static final int maxPackets = 7;
	public static final int CUBE_CHANGE = 1;
	public static final int PLAYER_MOVE = 2;
	public static final int PLAYER_JOIN = 3;
	public static final int LOGIN = 4;
	public static final int MAP = 5;
	public static final int CHAT = 6;
	public static final int KEEP_ALIVE = 0;

	public static boolean isPacket(int id) {
		if (id >= 0 && id < maxPackets) return true;
		return false;
	}

	public static Packet readPacketData(DataInputStream in, int id) {
		switch(id){
		case CUBE_CHANGE:
			return readCubeChangePacket(in);
		case PLAYER_MOVE:
			return readPlayerMovePacket(in);
		case PLAYER_JOIN:
			return readPlayerJoinPacket(in);
		case LOGIN:
			return readLoginPacket(in);
		case MAP:
			return readMapPacket(in);
		case CHAT:
			return readChatPacket(in);
		}
		return null;
	}

	private static Packet readPlayerJoinPacket(DataInputStream in) {
		PlayerJoinPacket p = null;
		try {
			p = new PlayerJoinPacket();
			p.id = in.readInt();
			p.x = in.readFloat();
			p.y = in.readFloat();
			p.z = in.readFloat();
		} catch (IOException e) {e.printStackTrace();
		}
		return p;
	}
	
	private static Packet readChatPacket(DataInputStream in) {
		ChatPacket p = null;
		try {
			p = new ChatPacket("");
			p.setMsg(in.readUTF());
		} catch (IOException e) {e.printStackTrace();
		}
		return p;
	}
	
	private static Packet readLoginPacket(DataInputStream in) {
		PlayerJoinPacket p = null;
		try {
			p = new PlayerJoinPacket();
			p.x = in.readFloat();
			p.y = in.readFloat();
			p.z = in.readFloat();
		} catch (IOException e) {e.printStackTrace();
		}
		return p;
	}

	private static Packet readPlayerMovePacket(DataInputStream in) {
		PlayerMovePacket p = null;
		try {
			p = new PlayerMovePacket();
			p.id = in.readInt();
			p.x = in.readFloat();
			p.y = in.readFloat();
			p.z = in.readFloat();
			p.rotationY = in.readFloat();
			p.pitch = in.readFloat();
			p.legRot = in.readFloat();
		} catch (IOException e) {e.printStackTrace();
		}
		return p;
	}

	private static Packet readCubeChangePacket(DataInputStream in) {
		CubeChangePacket p = null;
		try {
			p = new CubeChangePacket(0, 0, 0, 0);
			p.x = in.readInt();
			p.y = in.readInt();
			p.z = in.readInt();
			p.type = in.readByte();
		} catch (IOException e) {e.printStackTrace();
		}
		return p;
	}
	
	private static Packet readMapPacket(DataInputStream in) {
		MapPacket p = null;
		try {
			p = new MapPacket(0, 0, 0, new byte[128]);
			p.x = in.readInt();
			p.y = in.readInt();
			in.readFully(p.data);
		} catch (IOException e) {e.printStackTrace();
		}
		return p;
	}
	
	public static void writePlayerJoinPacket(DataOutputStream out, PlayerJoinPacket p) {
		try {
			out.writeInt(PLAYER_JOIN);
			out.writeInt(p.id);
			out.writeFloat(p.x);
			out.writeFloat(p.y);
			out.writeFloat(p.z);
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
	public static void writeChatPacket(DataOutputStream out, ChatPacket p) {
		try {
			out.writeInt(CHAT);
			out.writeUTF(p.getMsg());
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
	public static void writeLoginPacket(DataOutputStream out, LoginPacket p) {
		try {
			out.writeInt(LOGIN);
			out.writeFloat(p.x);
			out.writeFloat(p.y);
			out.writeFloat(p.z);
		} catch (IOException e) {e.printStackTrace();
		}
	}

	public static Packet writePlayerMovePacket(DataOutputStream out, PlayerMovePacket p) {
		try {
			out.writeInt(PLAYER_MOVE);
			out.writeInt(p.id);
			out.writeFloat(p.x);
			out.writeFloat(p.y);
			out.writeFloat(p.z);
			out.writeFloat(p.rotationY);
			out.writeFloat(p.pitch);
			out.writeFloat(p.legRot);
		} catch (IOException e) {e.printStackTrace();
		}
		return p;
	}

	public static Packet writeCubeChangePacket(DataOutputStream out, CubeChangePacket p) {
		try {
			out.writeInt(CUBE_CHANGE);
			out.writeInt(p.x);
			out.writeInt(p.y);
			out.writeInt(p.z);
			out.writeByte(p.type);
		} catch (IOException e) {e.printStackTrace();
		}
		return p;
	}
	
	public static void writeMapPacket(DataOutputStream out, MapPacket p) {
		try {
			out.write(p.x);
			out.write(p.y);
			out.write(p.data);
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
	public static void writeKeepAlivePacket(DataOutputStream out) {
		try {
			out.writeInt(KEEP_ALIVE);
			System.out.println("KEEP ALIVE");
		} catch (IOException e) {e.printStackTrace();
		}
	}

	public static void write(DataOutputStream out, Packet p) {
		if (p instanceof CubeChangePacket){
			CubeChangePacket ccp = (CubeChangePacket) p;
			//System.out.println("CUBE CHANGE");
			Packets.writeCubeChangePacket(out, ccp);
			
		} else if (p instanceof MapPacket){
			MapPacket ccp = (MapPacket) p;
			//System.out.println("MAP");
			Packets.writeMapPacket(out, ccp);
			
		} else if (p instanceof ChatPacket){
			ChatPacket cp = (ChatPacket) p;
			//System.out.println("CHAT");
			Packets.writeChatPacket(out, cp);
			
		} else if (p instanceof PlayerMovePacket){
			PlayerMovePacket pmp = (PlayerMovePacket) p;
			Packets.writePlayerMovePacket(out, pmp);
			
		} else if (p instanceof PlayerJoinPacket){
			PlayerJoinPacket pjp = (PlayerJoinPacket) p;
			//System.out.println("PLAYER JOIN");
			Packets.writePlayerJoinPacket(out, pjp);
			
		} else if (p instanceof LoginPacket){
			LoginPacket lp = (LoginPacket) p;
			System.out.println("LOGIN");
			Packets.writeLoginPacket(out, lp);
		} else {
			//System.out.println("KEEP ALIVE");
			Packets.writeKeepAlivePacket(out);
		}
		
	}

}
