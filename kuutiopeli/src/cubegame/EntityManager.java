package cubegame;

/**
 * The description of the class holding and maintaining the list of 
 * entities within the game. This interface forms the contract between
 * the entities being held in the game and their container. It provides
 * the entity logic a callback to the game code in a non-coupled manner.
 * 
 * @author Kevin Glass
 */
public interface EntityManager {
	/**
	 * Remove an entity form the game (i.e. remove a rock when its
	 * destroyed)
	 * 
	 * @param entity The entity to be removed
	 */
	public void removeEntity(Entity entity);
	
	/**
	 * Add an entity to the game (i.e. add a shot when the player fires)
	 * 
	 * @param entity The entity to be added.
	 */
	public void addEntity(Entity entity);
	
	/**
	 * Notification that the player was hit by a rock
	 */
	public void playerHit();
}
