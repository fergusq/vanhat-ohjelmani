package cubegame.multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cubegame.CubeState;
import cubegame.CubeType;
import cubegame.MenuState;
import cubegame.multiplayer.packets.ChatPacket;
import cubegame.multiplayer.packets.CubeChangePacket;
import cubegame.multiplayer.packets.LoginPacket;
import cubegame.multiplayer.packets.MapPacket;
import cubegame.multiplayer.packets.Packet;
import cubegame.multiplayer.packets.PlayerJoinPacket;
import cubegame.multiplayer.packets.PlayerMovePacket;
import cubegame.physics.PhysicCube;

public class NetClientHandler {
	
	private CubeState entityManager;
	private Socket socket;
	private String host = MenuState.serverHost;
	private DataOutputStream out;
	private DataInputStream in;
	private Thread readingThread;
	private Thread writingThread;
	public boolean isRunning;
	public boolean isTerminating;
	public int readThreads;
	private ArrayList<Packet> packetSendQueue = new ArrayList<Packet>();
	public int writeThreads;
	public static Object sendQueueLock = new Object();
	
	public static  Object threadSyncObject = new Object();

	public NetClientHandler(CubeState state){
		this.entityManager = state;
		readingThread = new ClientReadingThread(this);
		writingThread = new ClientWritingThread(this);
	}
	
	public void start(){
		connect();
	}
	public void stop(){
		try {
			in.close();
			out.close();
			socket.close();
			readingThread.stop();
			writingThread.stop();
		} catch (IOException e) {e.printStackTrace();
		}
		
	}

	private void connect() {
		// Create socket connection
		try {
			System.out.println("CONNECTING TO HOST");
			socket = new Socket(host, 35655);
			
			System.out.println("CREATING INPUT/OUTPUT STREAM");
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			
			isRunning = true;
			isTerminating = false;
			
			login();
			
			readingThread.start();
			writingThread.start();
			
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: "+host);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

	private void login() {
		addPacketToSendQueue(new LoginPacket(entityManager.player[0].getPositionX(), 
				entityManager.player[0].getPositionY(),
				entityManager.player[0].getPositionZ(), 0));
		
	}
	
	
	public void addPacketToSendQueue(Packet packet) {
		synchronized (sendQueueLock) {
			packetSendQueue.add(packet);
		}
	}

	public void readPacket() {
		try {
			int i = in.read();
			if (Packets.isPacket(i)){
				Packet p = Packets.readPacketData(in, i);
				
				if (i == Packets.CUBE_CHANGE){
					CubeChangePacket ccp = (CubeChangePacket) p;
					handleCubeChange(ccp);
				} else if (i == Packets.MAP){
					MapPacket mp = (MapPacket) p;
					readMap(mp);
				} else if (i == Packets.CHAT){
					ChatPacket cp = (ChatPacket) p;
					handleChat(cp);
				} else if (i == Packets.PLAYER_MOVE){
					PlayerMovePacket pmp = (PlayerMovePacket) p;
					handlePlayerMove(pmp);
				} else if (i == Packets.PLAYER_JOIN){
					PlayerJoinPacket pjp = (PlayerJoinPacket) p;
					handlePlayerJoin(pjp);
				}
			}
		} catch (IOException e) {e.printStackTrace();
		}
		
	}
	

	private void handlePlayerJoin(PlayerJoinPacket pjp) {
		entityManager.player[1].setPositionX(pjp.x);
		entityManager.player[1].setPositionY(pjp.y);
		entityManager.player[1].setPositionZ(pjp.z);
		showChat("New player joined to fun!");
	}

	private void handlePlayerMove(PlayerMovePacket pmp) {
		entityManager.player[1].setPositionX(pmp.x);
		entityManager.player[1].setPositionY(pmp.y);
		entityManager.player[1].setPositionZ(pmp.z);
		entityManager.player[1].rotationY = pmp.rotationY;
		entityManager.player[1].setPitch(pmp.pitch);
		entityManager.player[1].legRot = (int) pmp.legRot;
		
	}

	private void handleChat(ChatPacket cp) {
		showChat(cp.getMsg());
		System.out.println("Chat Received: " + cp.getMsg());
	}

	private void showChat(String msg) {
		entityManager.showChat = true;
		entityManager.rChat = msg;
		entityManager.rChatTimeout = 1000;
	}

	private void handleCubeChange(CubeChangePacket ccp) {
		if (ccp.type == 0){
			entityManager.removeCube(new PhysicCube(ccp.x, ccp.y, ccp.z));
		} else {
			entityManager.addCube(new PhysicCube(ccp.x, ccp.y, ccp.z, CubeType.getTypeFromNumber(ccp.type)));
		}
	}

	private void readMap(MapPacket map) {
		for (int i = 0; i < map.data.length; i++){
			entityManager.cubeMap.setCube(map.x, map.y, i, map.data[i]);
		}
		
	}

	public void writePacket() {
		if (packetSendQueue.isEmpty()) return;
		Packet p;
		synchronized (sendQueueLock) {
			p = (Packet) packetSendQueue.remove(0);
		}
		/*if (p instanceof CubeChangePacket){
			CubeChangePacket ccp = (CubeChangePacket) p;
			System.out.println("CUBE CHANGE");
			Packets.writeCubeChangePacket(out, ccp);
		} else if (p instanceof ChatPacket){
			ChatPacket cp = (ChatPacket) p;
			System.out.println("Chat: " + cp.getMsg());
			Packets.writeChatPacket(out, cp);
		} else if (p instanceof PlayerMovePacket){
			PlayerMovePacket pmp = (PlayerMovePacket) p;
			Packets.writePlayerMovePacket(out, pmp);
		} else if (p instanceof LoginPacket){
			LoginPacket pjp = (LoginPacket) p;
			System.out.println("PLAYER LOGIN");
			Packets.writeLoginPacket(out, pjp);
		} else {
			System.out.println("KEEP ALIVE");
			Packets.writeKeepAlivePacket(out);
		}*/
		Packets.write(out, p);
	}
	
}
