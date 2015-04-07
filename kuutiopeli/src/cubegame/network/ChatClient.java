package cubegame.network;

import java.io.*;
import java.net.*;

public class ChatClient {
	static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	static BufferedReader stdIn = new BufferedReader(new InputStreamReader(
			System.in));
    public static void main(String[] args) throws IOException {

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Host: ");
        String host = stdIn.readLine();
        
        System.out.print("Nickname: ");
        String nickname = stdIn.readLine();
        
        try {
            socket = new Socket(host, 4444);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + host);
            System.exit(1);
        }

        out.println("** " + nickname + " joined to chat.");
        
        try {
			Thread r = new Thread(new ClientReader(in, nickname));
			Thread w = new Thread(new ClientWriter(out, nickname));
			r.start();
			w.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        /*while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            if (fromServer.equals("Bye."))
                break;
		    
            System.out.print(nickname + ": ");
            fromUser = stdIn.readLine();
	    if (fromUser != null) {
                out.println(nickname + ": " + fromUser);
	    }
        }*/
    }
    
    protected void finalize() {
		// Objects created in run method are finalized when
		// program terminates and thread exits
		try {
			out.close();
		in.close();
		stdIn.close();
		socket.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}
}
class ClientWriter implements Runnable {
	private PrintWriter out;
	private String nickname;

	ClientWriter(PrintWriter out, String nickname) throws IOException, InterruptedException {
		this.out = out;
		this.nickname = nickname;
	}

	private void writingThread(PrintWriter out) throws IOException,
			InterruptedException {
		String fromClient;

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		while (true) {
			fromClient = stdIn.readLine();
			if (fromClient != null) {
				out.println(nickname + ": " + fromClient);
				if (fromClient.equals("Bye.")) System.out.println("Server is closing your OUT. You can now close your client.");
			}

			Thread.sleep(10);
		}
	}

	@Override
	public void run() {
		try {
			writingThread(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class ClientReader implements Runnable {
	private BufferedReader in;
	private String nickname;

	ClientReader(BufferedReader in, String nickname) throws IOException, InterruptedException {
		this.in = in;
		this.nickname = nickname;
	}

	private void readingThread(BufferedReader in) throws IOException,
			InterruptedException {
		String fromServer;
		while ((fromServer = in.readLine()) != null) {
			if (!fromServer.startsWith(nickname)) System.out.println(fromServer);
			if (fromServer.equals("Bye."))
				break;
		}
		Thread.sleep(10);
	}

	@Override
	public void run() {
		try {
			readingThread(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

