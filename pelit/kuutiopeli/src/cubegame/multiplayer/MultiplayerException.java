package cubegame.multiplayer;

public class MultiplayerException extends Exception {
	private static final long serialVersionUID = 1L;

	public MultiplayerException() {
	}

	public MultiplayerException(String msg) {
		super(msg);
	}

	public MultiplayerException(String message, Throwable cause) {
		super(message, cause);
	}

	public MultiplayerException(Throwable cause) {
		super(cause);
	}
}
