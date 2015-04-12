package cubegame.multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cubegame.multiplayer.packets.ChatPacket;
import cubegame.multiplayer.packets.CubeChangePacket;
import cubegame.multiplayer.packets.MapPacket;
import cubegame.multiplayer.packets.Packet;
import cubegame.multiplayer.packets.PlayerJoinPacket;
import cubegame.multiplayer.packets.PlayerMovePacket;

public class NetServerHandler {
	
	private Socket socket;
	String host;
	private DataOutputStream out;
	private DataInputStream in;
	private Thread readingThread;
	private Thread writingThread;
	public boolean isRunning;
	public boolean isTerminating;
	public int readThreads;
	private ArrayList<Packet> packetSendQueue = new ArrayList<Packet>();
	public int writeThreads;
	private Server server;
	public static Object sendQueueLock = new Object();
	public static  Object threadSyncObject = new Object();

	public NetServerHandler(Server ser, Socket s){
		readingThread = new ServerReadingThread(this);
		writingThread = new ServerWritingThread(this);
		this.server = ser;
		socket = s;
		host = s.getRemoteSocketAddress() + ":" + s.getPort();
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
			
			System.out.println("CREATING INPUT/OUTPUT STREAM");
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			
			isRunning = true;
			isTerminating = false;
			
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
				
				server.packetReaded(p, i, host);
			}
		} catch (IOException e) {e.printStackTrace();
		}
		
	}
	

	public void writePacket() {
		System.out.print("");
		if (packetSendQueue.isEmpty()) return;
		Packet p;
		synchronized (sendQueueLock) {
			p = (Packet) packetSendQueue.remove(0);
		}
		
		Packets.write(out, p);
	}

	
}

