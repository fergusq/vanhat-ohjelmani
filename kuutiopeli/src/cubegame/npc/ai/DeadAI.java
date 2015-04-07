package cubegame.npc.ai;

import cubegame.npc.NPC;

public class DeadAI extends AI {

	public DeadAI(NPC npc) {
		super(npc);
	}

	@Override
	public void hurt() {
		npc.jump(0.2f);
	}

	@Override
	public void update() {
	}

}
