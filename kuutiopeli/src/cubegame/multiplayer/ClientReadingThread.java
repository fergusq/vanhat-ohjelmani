package cubegame.multiplayer;

public class ClientReadingThread extends Thread {

	private NetClientHandler netHandler;

	public ClientReadingThread(NetClientHandler netClientHandler) {
		this.netHandler = netClientHandler;
	}

	@Override
	public void run() {
		synchronized (netHandler.threadSyncObject) {
			netHandler.readThreads++;
		}
		try{
			for(; netHandler.isRunning && !netHandler.isTerminating; netHandler.readPacket()) { }
	     	try {
				sleep(1L);
			} catch (InterruptedException ex){;}
		} finally{
			netHandler.readThreads--;
		}
	}
}
