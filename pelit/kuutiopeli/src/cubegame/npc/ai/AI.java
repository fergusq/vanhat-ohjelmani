package cubegame.npc.ai;

import cubegame.npc.NPC;

public abstract class AI {
	protected NPC npc;
	protected boolean isJumping;
	protected boolean isAngry;
	
	public AI (NPC npc){
		this.npc = npc;
	}
	
	public abstract void update();
	
	public abstract void hurt();

	public static AI getAI(int cap, NPC npc) {
		switch(cap){
		case DEAD_AI:
			return new DeadAI(npc);
		case PASSIVE_AI:
			return new PassiveAI(npc);
		case NEUTRAL_AI:
			return null;
		case AGGRESSIVE_AI:
			return null;
		case ALLY_AI:
			return null;
		}
		return null;
	}
	
	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public boolean isAngry() {
		return isAngry;
	}

	public void setAngry(boolean isAngry) {
		this.isAngry = isAngry;
	}

	public static final int DEAD_AI = 0;
	public static final int PASSIVE_AI = 1;
	public static final int NEUTRAL_AI = 2;
	public static final int AGGRESSIVE_AI = 3;
	public static final int ALLY_AI = 4;

	public void jumpOrNot() {
		npc.jump(-1.1f);
	}
}

