package cubegame.network;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

import cubegame.network.SocketThrdServer.ArrayHelper;
import cubegame.network.SocketThrdServer.ChatHelper;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import vho.Timer;
import vho.TimerEvent;
import vho.TimerListener;

class Client extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6791619556889645591L;

	JLabel text, clicked;
	JButton button;
	JPanel panel;
	ArrayList<String> chat = new ArrayList<String>();
	JTextPane chatArea;
	JTextField textField;
	JTextField userField;
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	Timer timer = new Timer();

	String host = "192.168.11.8";

	Client() { // Begin Constructor
		this.setSize(300, 300);

		text = new JLabel("Text to send over socket:");
		textField = new JTextField(20);
		userField = new JTextField(10);
		chatArea = new JTextPane();
		button = new JButton("Send");
		button.addActionListener(this);

		chatArea.setText("Local chat:");

		panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		panel.setBackground(Color.white);
		getContentPane().add(panel);
		panel.add(chatArea);
		panel.add(textField);
		panel.add(button);
		panel.add(userField);
	} // End Constructor

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();

		if (source == button) {
			write();
		}
	}

	private void write() {
		// Send data over socket
		String text = textField.getText();
		out.println(text);
		textField.setText(new String(""));
	}

	private void read() {
		// Receive text from server
		try {
			String line = in.readLine();
			System.out.println("Text received :" + line);
			chat.add(line);
			if (chat.size() > 4)
				chat.remove(0);
			chatArea.setText(((new SocketThrdServer()).new ArrayHelper())
					.toString(chat));
			chatArea.setAutoscrolls(true);
			/*
			 * int i = 0; for (String s : chat){ if (i > chat.size() - 5)
			 * chatArea.setText(chatArea.getText() + "\n" + userField.getText()
			 * + ": " + s); i++; }
			 */
		} catch (IOException e) {
			System.out.println("Read failed");
			System.exit(1);
		}
	}

	public void listenSocket() {
		// Create socket connection
		try {
			socket = new Socket(host, 4444);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + host);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		}

		ServerWorker w = new ServerWorker(socket, chatArea, chat);
		Thread t = new Thread(w);
		t.start();

	}

	public static void main(String[] args) {
		Client frame = new Client();
		frame.setTitle("Client Program");
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

	class readThread implements Runnable {
		public void run() {
			synchronized (syncObject) {
				while (true) {
					read();
				}
			}
		}
	}

	class ServerWorker implements Runnable {
		private Socket client;
		private JTextPane chatArea;
		private ArrayList<String> messages;

		ServerWorker(Socket client, JTextPane textArea, ArrayList<String> chat) {
			this.client = client;
			this.chatArea = textArea;
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
					System.out.println("Text received :" + line);
					chat.add(line);
					if (chat.size() > 4)
						chat.remove(0);
					chatArea
							.setText(((new SocketThrdServer()).new ArrayHelper())
									.toString(chat));
					chatArea.setAutoscrolls(true);
					/*
					 * int i = 0; for (String s : chat){ if (i > chat.size() -
					 * 5) chatArea.setText(chatArea.getText() + "\n" +
					 * userField.getText() + ": " + s); i++; }
					 */
				} catch (IOException e) {
					System.out.println("Read failed");
					System.exit(-1);
				} catch (Exception e) {
					e.printStackTrace();
					out.println("Read failed");
				}
			}
		}
	}

	public static final Object syncObject = new Object();

}
