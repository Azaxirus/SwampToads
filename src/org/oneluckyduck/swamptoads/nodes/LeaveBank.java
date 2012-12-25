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

public class LeaveBank extends Node {

	@Override
	public boolean activate() {
		return Methods.isInGame() && Methods.isInBank()
				&& Inventory.getCount() < 1;
	}

	@Override
	public void execute() {
		final SceneObject ladder = SceneEntities
				.getNearest(Const.UPSTAIRS_LADDER);

		if (ladder != null && ladder.isOnScreen()) {
			Methods.s("Climbing down ladder");
			ladder.interact("Climb-down", "Staircase");
			final Timer timer = new Timer(1000);
			while (Methods.isInBank()) {
				Task.sleep(200);
				if (!timer.isRunning() || Players.getLocal().getPlane() == 0)
					break;
			}
		} else if (!ladder.isOnScreen()) {
			Methods.s("finding ladder");
			Camera.turnTo(ladder);
		}
	}
}
