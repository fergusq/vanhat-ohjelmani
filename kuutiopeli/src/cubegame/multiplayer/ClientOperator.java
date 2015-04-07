package cubegame.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cubegame.Cube;
import cubegame.Entity;
import cubegame.save.CubeMapFileReader;

public class ClientOperator {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String host;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public ClientOperator(String host){
		this.host = host;
		listenSocket();
	}
	
	public void listenSocket() {
		// Create socket connection
		try {
			System.out.println("CONNECTING TO HOST");
			socket = new Socket(host, 35655);
			
			System.out.println("CREATING INPUT/OUTPUT STREAM");
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			
			System.out.println("CREATING OBJECT INPUT STREAM");
			input = new ObjectInputStream(socket.getInputStream());
			System.out.println("CREATING OBJECT OUTPUT STREAM");
			output = new ObjectOutputStream(socket.getOutputStream());
			
			System.out.println("LOG IN TO SERVER");
			out.println("PLAYER_JOINED");
			
			System.out.println("CONNECTED");
			
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
	
	public ArrayList<Cube> getMap() throws IOException, ClassNotFoundException{
		out.println("CUBEMAP");
		ArrayList<CubePacket> response = (ArrayList<CubePacket>) input.readObject();
		ArrayList<Cube> answer = new ArrayList<Cube>();
		for (CubePacket packet : response){
			answer.add(new Cube(packet.getX(), packet.getY(), packet.getZ(), packet.getType()));
		}
		return answer;
	}
	
	public boolean sendAddedAndRemovedCubes(ArrayList addList, ArrayList removeList) throws IOException, MultiplayerException{
		out.println("ADDMAP");
		ArrayList<CubePacket> newAddList = new ArrayList<CubePacket>();
		for (Object rawCube : addList){
			if (rawCube instanceof Cube){
				Cube cube = (Cube) rawCube;
				newAddList.add(new CubePacket(cube.getX(), cube.getY(), cube.getZ(), cube.getType()));
			}
		}
		output.writeObject(newAddList);
		if (in.readLine() != "ADDMAP_READ"){
			throw new MultiplayerException("Failed to send add map!");
		}
		
		ArrayList<CubePacket> newRemoveList = new ArrayList<CubePacket>();
		for (Object rawCube : removeList){
			if (rawCube instanceof Cube){
				Cube cube = (Cube) rawCube;
				newRemoveList.add(new CubePacket(cube.getX(), cube.getY(), cube.getZ(), cube.getType()));
			}
		}
		out.println("REMOVEMAP");
		output.writeObject(removeList);
		if (in.readLine() != "REMOVEMAP_READ"){
			throw new MultiplayerException("Failed to send remove map!");
		}
		return true;
	}
}
