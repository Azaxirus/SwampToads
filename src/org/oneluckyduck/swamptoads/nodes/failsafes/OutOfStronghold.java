package org.oneluckyduck.swamptoads.nodes.failsafes;

import org.oneluckyduck.swamptoads.misc.Const;
import org.oneluckyduck.swamptoads.misc.Methods;
import org.powerbot.core.script.job.state.Node;

public class OutOfStronghold extends Node {

	@Override
	public boolean activate() {
		return Methods.isInGame() && !Methods.isInArea(Const.STRONGHOLD_AREA);
	}

	@Override
	public void execute() {
		if (Methods.isInGame())
			Methods.stopScript("We are out of the stronghold. Stopping script");
	}
}
