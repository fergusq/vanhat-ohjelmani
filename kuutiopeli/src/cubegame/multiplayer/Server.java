package cubegame.multiplayer;

import java.io.IOException;
import java.net.ServerSocket;

import cubegame.map.BasicMapGenerator;
import cubegame.map.CubeMap;
import cubegame.multiplayer.packets.ChatPacket;
import cubegame.multiplayer.packets.CubeChangePacket;
import cubegame.multiplayer.packets.MapPacket;
import cubegame.multiplayer.packets.Packet;
import cubegame.multiplayer.packets.PlayerJoinPacket;
import cubegame.multiplayer.packets.PlayerMovePacket;

public class Server {

	private NetServerHandler player1;
	private NetServerHandler player2;
	CubeMap map;

	public Server(){
		System.out.println("Initializing...");
		BasicMapGenerator g = new BasicMapGenerator();
		System.out.println("Generating map...");
		map = g.generate();

		ServerSocket s;
		try {
			System.out.println("Generating server socket...");
			s = new ServerSocket(35655);
			
			System.out.println("Waiting for first connection...");
			player1 = new NetServerHandler(this, s.accept());
			player1.start();
			System.out.println("CONNECT FROM " + player1.host);
			
			System.out.println("Waiting for second connection...");
			player2 = new NetServerHandler(this, s.accept());
			player2.start();
			System.out.println("CONNECT FROM " + player2.host);
			
		} catch (IOException e) {
			System.out.println("Error! Stopping server...");
			stop();
			e.printStackTrace();
		}
		
	}
	
	
	
	private void stop() {
		if (player1 != null) player1.stop();
		if (player2 != null) player2.stop();
	}



	public void packetReaded(Packet p, int id, String host) {
		if (player1 == null) return;
		if (id == Packets.LOGIN){
			System.out.println("Player joined: " + host);
			if (host.equals(player1.host)){
				System.out.println("Sending map to player 1");
				sendMapToPlayer(player1);
				if (player2 == null) return;
				PlayerJoinPacket p2 = new PlayerJoinPacket(0, 0, 0, 2);
				player2.addPacketToSendQueue(p2);
				
			} else {
				System.out.println("Sending map to player 2");
				sendMapToPlayer(player2);
				PlayerJoinPacket p2 = new PlayerJoinPacket(0, 0, 0, 1);
				player1.addPacketToSendQueue(p2);
				
			}
		}
		if (id == Packets.CUBE_CHANGE){
			System.out.println("CUBE CHANGE READ");
			CubeChangePacket ccp = (CubeChangePacket) p;
			map.setCube(ccp.x, ccp.y, ccp.z, ccp.type);
			
			player1.addPacketToSendQueue(ccp);
			
			if (player2 == null) return;
			player2.addPacketToSendQueue(ccp); 
			
		}
		if (id == Packets.PLAYER_MOVE){
			PlayerMovePacket pmp = (PlayerMovePacket) p;
			if (host.equals(player1.host)){
				if (player2 == null) return;
				player2.addPacketToSendQueue(pmp);
			} else {
				player1.addPacketToSendQueue(pmp);
			}
			
		}
		if (id == Packets.CHAT){
			ChatPacket cp = (ChatPacket) p;
			if (host.equals(player1.host)){
				if (player2 == null) return;
				player2.addPacketToSendQueue(cp);
			} else {
				player1.addPacketToSendQueue(cp);
			}
			
		}
	}

	private void sendMapToPlayer(NetServerHandler player) {
		System.out.println("Sending map to " + player.host);
		for (int x = 0; x < map.size; x++){
			for (int y = 0; y < map.size; y++){
				player.addPacketToSendQueue(new MapPacket(x, y, 0, map.map[x][y]));
			}
		}
		System.out.println("Map sent to " + player.host);
	}

	public static void main(String[] args){
		Server server = new Server();
	}
}
