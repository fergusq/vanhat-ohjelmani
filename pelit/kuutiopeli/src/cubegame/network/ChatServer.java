package cubegame.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	static ServerSocket server = null;
	static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	static BufferedReader stdIn = new BufferedReader(new InputStreamReader(
			System.in));
	static ArrayList<BufferedReader> ins = new ArrayList<BufferedReader>();
	static ArrayList<PrintWriter> outs = new ArrayList<PrintWriter>();

	public static void main(String[] args) throws IOException {

		try {
			server = new ServerSocket(4444);
			socket = server.accept();
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			ins.add(in);
			outs.add(out);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		out.println("Chat started");

		Thread r;
		Thread w;
		ServerReader reader = null;
		ServerWriter writer = null;
		try {
			writer = new ServerWriter(outs);
			reader = new ServerReader(ins, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			r = new Thread(reader);
			w = new Thread(writer);
			r.start();
			w.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			Socket s = server.accept();
			PrintWriter o = new PrintWriter(s.getOutputStream(), true);
			ins.add(new BufferedReader(
					new InputStreamReader(s.getInputStream())));
			outs.add(o);
			o.println("Welcome to chat. We have " + (ins.size() - 1)
					+ " members online. Type: \"Bye.\" to quit.");
			try {
				// reader.addIn(ins.get(ins.size()-1));
				// writer.addOut(outs.get(outs.size()-1));
				r = new Thread(reader);
				// w = new Thread(writer);
				r.start();
				// w.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * while ((fromUser = in.readLine()) != null) {
		 * System.out.println(fromUser); if (fromUser.equals("Bye.")) break;
		 * 
		 * System.out.print("Server: "); fromServer = stdIn.readLine(); if
		 * (fromServer != null) { out.println(fromServer); } }
		 */

		/*
		 * out.close(); in.close(); stdIn.close(); socket.close();
		 * server.close();
		 */
	}

	protected void finalize() {
		// Objects created in run method are finalized when
		// program terminates and thread exits
		try {
			out.close();
			in.close();
			stdIn.close();
			socket.close();
			server.close();
			for (BufferedReader in : ins) {
				in.close();
			}
			for (PrintWriter out : outs) {
				out.close();
			}
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}
}

class ServerWriter implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1859270843539401966L;

	private ArrayList<PrintWriter> outs = new ArrayList<PrintWriter>();

	ServerWriter(ArrayList<PrintWriter> outs) throws IOException {
		this.outs = outs;
	}

	public void addOut(PrintWriter printWriter) {
		outs.add(printWriter);

	}

	@SuppressWarnings("unused")
	private void writingThread(PrintWriter out) throws IOException,
			InterruptedException {
		String fromServer;

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		while (true) {
			fromServer = stdIn.readLine();
			if (fromServer != null) {
				out.println("Server: " + fromServer);
			}

			Thread.sleep(10);
		}
	}

	public void sendMessageToAllClients(String message) {
		if (message != null) {
			for (PrintWriter out : outs) {
				out.println(message);
			}
		}

	}

	@Override
	public void run() {
		try {
			/*
			 * for (PrintWriter out : outs){ writingThread(out); }
			 */
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(
					System.in));
			while (true) {
				sendMessageToAllClients("Server: " + stdIn.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class ServerReader implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4853845160380991577L;

	private ArrayList<BufferedReader> ins = new ArrayList<BufferedReader>();

	private ServerWriter writer = null;

	ServerReader(ArrayList<BufferedReader> in) throws IOException {
		this.ins = in;
	}

	public ServerReader(ArrayList<BufferedReader> in, ServerWriter writer) {
		this.ins = in;
		this.writer = writer;
	}

	public void addIn(BufferedReader bufferedReader) {
		ins.add(bufferedReader);

	}

	@SuppressWarnings("unused")
	private void readingThread(BufferedReader in) throws IOException,
			InterruptedException {
		String fromUser;
		while ((fromUser = in.readLine()) != null) {
			System.out.println(fromUser);
			if (fromUser.equals("Bye."))
				break;

		}
		Thread.sleep(10);
	}

	@Override
	public void run() {
		for (BufferedReader in : ins) {
			// readingThread(in);
			ReadingThread r = new ReadingThread(in, writer);
			Thread t = new Thread(r);
			t.start();
		}

	}
}

class ReadingThread implements Runnable {
	private BufferedReader in;
	private ServerWriter writer;

	ReadingThread(BufferedReader in) throws IOException, InterruptedException {
		this.in = in;
	}

	public ReadingThread(BufferedReader in, ServerWriter writer) {
		this.in = in;
		this.writer = writer;
	}

	private void readingThread(BufferedReader in) throws IOException,
			InterruptedException {
		String fromClient;
		while ((fromClient = in.readLine()) != null) {
			writer.sendMessageToAllClients(fromClient);
			System.out.println(fromClient);
			String nickname = fromClient.split(":")[0];
			if (fromClient.equals(nickname + ": Bye.")) {
				writer.sendMessageToAllClients("** User " + nickname
						+ " is leaving.");
				System.out.println("** User " + nickname + " is leaving.");
				in.close();
				System.out.println("INFO: " + nickname + "'s IN is closed.");

			}

		}
		Thread.sleep(10);
	}

	@Override
	public void run() {
		try {
			readingThread(in);
		} catch (IOException e) {
			System.err.println("java.io.IOException - Is user online?");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
