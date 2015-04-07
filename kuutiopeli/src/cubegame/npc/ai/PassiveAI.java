package cubegame.npc.ai;

import java.util.Random;

import cubegame.npc.NPC;

public class PassiveAI extends AI {

	public PassiveAI(NPC npc) {
		super(npc);
	}

	@Override
	public void hurt() {
		npc.jump(-0.5f);
	}

	@Override
	public void update() {
		Random dice = new Random();
		
		float slow = 0.009f;
		
		if (npc.checkIsOnGround()) slow = 0.0003f;
		
		if (dice.nextInt(5) >= 2){
			 npc.setMoveX(npc.getPositionX() - slow * (float)Math.sin(Math.toRadians(npc.getRotationY())));
			 npc.setMoveZ(npc.getPositionZ() + slow * (float)Math.cos(Math.toRadians(npc.getRotationY())));
		}
		if (dice.nextInt(150) == 1){
			npc.setRotationY(dice.nextInt(360));
		}
		if (dice.nextInt(150) == 1){
			setJumping(true);
		}
		if (dice.nextInt(150) == 1){
			setJumping(false);
		}
		//System.out.println("X" + npc.getPositionX() +"Y"+ npc.getPositionY() +"Z"+ npc.getPositionZ());
	}
	
	public void jumpOrNot() {
		npc.jump(-1.1f);
	}

}
