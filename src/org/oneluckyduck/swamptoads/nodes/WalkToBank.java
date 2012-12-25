package org.oneluckyduck.swamptoads.nodes;

import org.oneluckyduck.swamptoads.misc.Const;
import org.oneluckyduck.swamptoads.misc.Methods;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;

public class WalkToBank extends Node {

	@Override
	public boolean activate() {
		return Methods.isInGame() && Inventory.isFull() && !Methods.isInBank() && !Methods.isInArea(Const.LADDER_AREA);
	}

	@Override
	public void execute() {
		Methods.s("Walking back to bank");
		Walking.walk(Const.BANK_LOCATION);
		final Timer timer = new Timer(500);
		while(timer.isRunning()) {
			Task.sleep(5);
			if(Players.getLocal().isMoving()) {
				timer.reset();
			}
		}
		
	}

}
