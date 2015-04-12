package cubegame.multiplayer;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import cubegame.Cube;
import cubegame.Entity;
import cubegame.Human;
import cubegame.save.CubeMapFileReader;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;

class ClientWorker implements Runnable {
	private Socket client;
	private JTextArea textArea;
	private ArrayList<Cube> map;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	ClientWorker(Socket client, ArrayList<Cube> map) {
		this.client = client;
		this.map = map;
	}

	public void run() {
		String line;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(client
					.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}
		
		try {
			output = new ObjectOutputStream(client.getOutputStream());
			input = new ObjectInputStream(client.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while (true) {
			try {
				line = in.readLine();
				if (line != null)sendResponse(line, out);
			} catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (MultiplayerException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendResponse(String line, PrintWriter out) throws IOException, ClassNotFoundException, MultiplayerException {
		if (line.equals("CUBEMAP")){
			ArrayList<CubePacket> answer = new ArrayList<CubePacket>();
			for (Cube cube : map){
				answer.add(new CubePacket(cube.getX(), cube.getY(), cube.getZ(), cube.getType()));
			}
			output.writeObject(answer);
		}
		if (line.equals("ADDMAP")){
			Object obj = input.readObject();
			if (obj instanceof ArrayList<?>){
				ArrayList<CubePacket> response = (ArrayList<CubePacket>) obj;
				ArrayList<Cube> answer = new ArrayList<Cube>();
				for (CubePacket packet : response){
					answer.add(new Cube(packet.getX(), packet.getY(), packet.getZ(), packet.getType()));
				}
				map.addAll(answer);
				out.println("ADDMAP_READ");
			}
			
		}
		if (line.equals("REMOVEMAP")){
			
			Object obj = input.readObject();
			ArrayList<Cube> answer = new ArrayList<Cube>();
			if (obj instanceof ArrayList<?>){
				ArrayList<CubePacket> response = (ArrayList<CubePacket>) obj;
				for (CubePacket packet : response){
					answer.add(new Cube(packet.getX(), packet.getY(), packet.getZ(), packet.getType()));
				}
				map.addAll(answer);
				out.println("REMOVEMAP_READ");
			}
			
			ArrayList removeList = null;
			if (obj instanceof ArrayList) removeList = answer; else throw new MultiplayerException("Failed to read removelist");
			
			for (int i = 0; i < removeList.size(); i++) {
				for (int t = 0; t < map.size(); t++) {
					if (removeList.get(i) instanceof Cube) {
						if (map.get(t) instanceof Cube) {
							Cube remove = (Cube) removeList.get(i);
							Cube entity = (Cube) map.get(t);
							if (remove.getX() == entity.getX()
									&& remove.getY() == entity.getY()
									&& remove.getZ() == entity.getZ()) {
								map.remove(t);
							}
						}
					}
				}
			}
		}
		if (line.equals("PLAYER_JOINED")){
			System.out.println("PLAYER_JOINED");
		}
	}
}

public class ServerThread extends Applet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5422608994892666874L;
	
	ServerSocket server = null;
	
	private ArrayList<Cube> map;

	private Canvas display_parent;

	void initSavefile() { // Begin Constructor
		
		
	} // End Constructor

	public void startLWJGL() {
		try {
			Display.setParent(display_parent);
			Display.create();
		} catch (LWJGLException e) {
				e.printStackTrace();
		}
	}
	
	private void stopLWJGL() {
		Display.destroy();
	}

	public void start() {
		listenSocket();
	}

	public void stop() {
		
	}
	
	public void destroy() {
		remove(display_parent);
		super.destroy();
	}
	
	public void init() {
		setLayout(new BorderLayout());
		try {
			display_parent = new Canvas() {
				public final void addNotify() {
					super.addNotify();
					startLWJGL();
				}
				public final void removeNotify() {
					stopLWJGL();
					super.removeNotify();
				}
			};
			display_parent.setSize(getWidth(),getHeight());
			add(display_parent);
			display_parent.setFocusable(true);
			display_parent.requestFocus();
			display_parent.setIgnoreRepaint(true);
			setVisible(true);

			initSavefile();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to create display");
		}
	}

	
	public void listenSocket() {
		try {
			server = new ServerSocket(35655);
		} catch (IOException e) {
			System.out.println("Could not listen on port 35655");
			System.exit(-1);
		}
		while (true) {
			ClientWorker w;
			try {
				w = new ClientWorker(server.accept(), map);
				Thread t = new Thread(w);
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed: 35655");
				System.exit(-1);
			}
		}
	}

	protected void finalize() {
		// Objects created in run method are finalized when
		// program terminates and thread exits
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}
}
