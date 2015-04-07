package cubegame.multiplayer;

public class ServerWritingThread extends Thread {
	private NetServerHandler netHandler;

	public ServerWritingThread(NetServerHandler netServerHandler) {
		this.netHandler = netServerHandler;
	}

	@Override
	public void run() {
		synchronized (netHandler.threadSyncObject) {
			netHandler.writeThreads++;
		}
		try{
		for(; netHandler.isRunning && !netHandler.isTerminating; netHandler.writePacket()) { }
			try {
				sleep(1L);
			}catch (InterruptedException ex){;}
		} finally{
			netHandler.writeThreads--;
		}
}
}
