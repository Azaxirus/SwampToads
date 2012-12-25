package org.oneluckyduck.swamptoads.nodes;

import org.oneluckyduck.swamptoads.misc.Const;
import org.oneluckyduck.swamptoads.misc.Methods;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class EnterBank extends Node {

	@Override
	public boolean activate() {
		return Methods.isInGame() && !Methods.isInBank() && Methods.isInArea(Const.LADDER_AREA) && Inventory.isFull();
	}

	@Override
	public void execute() {
		final SceneObject ladder = SceneEntities
				.getNearest(Const.DOWNSTAIRS_LADDER);

		if (ladder != null && ladder.isOnScreen()) {
			Methods.s("Climbing up ladder");
			ladder.interact("Climb-up", "Staircase");
			final Timer timer = new Timer(1000);
			while (!Methods.isInBank()) {
				Task.sleep(200);
				if (!timer.isRunning() || Players.getLocal().getPlane() == 1)
					break;
			}
		} else if (!ladder.isOnScreen()) {
			Methods.s("finding ladder");
			Camera.turnTo(ladder);
		}

	}

}
