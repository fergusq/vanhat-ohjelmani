package cubegame;

import java.io.IOException;

public interface GameState {
	public String getName();
	
	public void init(GameWindow window) throws IOException;
	
	public void render(GameWindow window, int delta);
	
	public void update(GameWindow window, int delta);
	
	public void enter(GameWindow window);

	public void leave(GameWindow window);
}
