package cubegame.multiplayer;

public class ClientWritingThread extends Thread {

		private NetClientHandler netHandler;

		public ClientWritingThread(NetClientHandler netClientHandler) {
			this.netHandler = netClientHandler;
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
