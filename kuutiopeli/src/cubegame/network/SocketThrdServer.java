package cubegame.network;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import com.sun.org.apache.bcel.internal.generic.CPInstruction;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

class SocketThrdServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7073295162584204805L;
	JLabel label = new JLabel("Text received over socket:");
	JPanel panel;
	JTextArea textArea = new JTextArea();
	ServerSocket server = null;

	private ArrayList<String> chat = new ArrayList<String>();
	
	SocketThrdServer() { // Begin Constructor
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.white);
		getContentPane().add(panel);
		panel.add("North", label);
		panel.add("Center", textArea);
	} // End Constructor

	public void listenSocket() {
		try {
			server = new ServerSocket(4444);
		} catch (IOException e) {
			System.out.println("Could not listen on port 4444");
			System.exit(-1);
		}
		while (true) {
			ClientWorker w;
			try {
				w = new ClientWorker(server.accept(), textArea, chat);
				Thread t = new Thread(w);
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
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

	public static void main(String[] args) {
		SocketThrdServer frame = new SocketThrdServer();
		frame.setTitle("Server Program");
		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		frame.addWindowListener(l);
		frame.pack();
		frame.setVisible(true);
		frame.listenSocket();
	}

	class ClientWorker implements Runnable {
		private Socket client;
		private JTextArea textArea;
		private ArrayList<String> messages;

		ClientWorker(Socket client, JTextArea textArea, ArrayList<String> chat) {
			this.client = client;
			this.textArea = textArea;
			messages = chat;
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

			while (true) {
				try {
					line = in.readLine();
					// Send data back to client
					ArrayHelper helper = new ArrayHelper();
					ChatHelper.addChatMessage(line);
					messages.add(line);
					if (messages.size() > 4) messages.remove(0);
					textArea.append("\n" + line);
					String[] msg = textArea.getText().split("\n");
					out.println(msg[msg.length-1]);
				} catch (IOException e) {
					System.out.println("Read failed");
					System.exit(-1);
				} catch (Exception e){
					e.printStackTrace();
					out.println("Read failed");
				}
			}
		}
	}

	static class ChatHelper{
		private static final ArrayList<String> chat = new ArrayList<String>();

		public static void addChatMessage(String message){
			chat.add(message);
			if (chat.size() > 4) chat.remove(0);
		}

		public static ArrayList<String> getChat() {
			return chat;
		}
		
	}
	
	public class ArrayHelper{

		public String toString(ArrayList<String> list){
			String answer = "";
			for (String s : list){
				answer += s + "\n";
			}
			return answer;
		}

	}
	
}
