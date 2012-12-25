package org.oneluckyduck.swamptoads.nodes;

import java.awt.Point;

import org.oneluckyduck.swamptoads.misc.Const;
import org.oneluckyduck.swamptoads.misc.Methods;
import org.oneluckyduck.swamptoads.misc.Variables;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class PickToads extends Node {

	@Override
	public boolean activate() {
		return Methods.isInGame() && Methods.isInArea(Const.TOADS_AREA)
				&& !Inventory.isFull();
	}

	@Override
	public void execute() {
		Methods.frequentChecks();
		final GroundItem toad = GroundItems.getNearest(Const.TOAD_FILTER);
		if (toad != null && toad.isOnScreen()) {
			if (Calculations.distanceTo(toad) > 8) {
				Methods.s("Toad is far");
				toad.getLocation().clickOnMap();
			}
			if (Random.nextInt(0, 50) > 5) {
				Methods.s("Grabbing Swamp toad 1");
				Methods.openMenu(toad, Random.nextInt(0, 8) % 2 == 0);
				if (Menu.isOpen() && Menu.contains("Take", "Swamp toad")) {
					Menu.select("Take", "Swamp toad");
					final Timer timer = new Timer(2000);
					while (toad.validate()) {
						Task.sleep(50);
						if (!timer.isRunning())
							break;
					}
					if(toad.getGroundItem().getName().contains("toad"))
						++Variables.toadsPicked;
				} else if (!Menu.contains("Take", "Swamp toad")) {
					Methods.s("Whoops! I missed");
					final Point p = Mouse.getLocation();
					int r = Random.nextInt(40, 109);
					Mouse.move(p.x + r, p.y + r);
				}
			} else {
				Methods.s("Grabbing Swamp toad 2");
				toad.interact("Take", "Swamp toad");
				final Timer timer = new Timer(2000);
				while (toad.validate()) {
					Task.sleep(50);
					if (!timer.isRunning())
						break;
				}
				if(toad.getGroundItem().getName().contains("toad"))
					++Variables.toadsPicked;
			}
		} else if (toad != null && !toad.isOnScreen()) {
			Camera.turnTo(toad);
		}

	}

}
