package cubegame.multiplayer;

public class ServerReadingThread extends Thread {

	private NetServerHandler netHandler;

	public ServerReadingThread(NetServerHandler netHandler) {
		this.netHandler = netHandler;
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
		}catch (InterruptedException ex){;}
		} finally{
			netHandler.readThreads--;
		}
	}
}
