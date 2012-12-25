package org.oneluckyduck.swamptoads.nodes;

import org.oneluckyduck.swamptoads.misc.Const;
import org.oneluckyduck.swamptoads.misc.Methods;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;

public class Banking extends Node {

	@Override
	public boolean activate() {
		return Methods.isInGame() && Methods.isInBank() && Inventory.isFull();
	}

	@Override
	public void execute() {
		if(!Bank.isOpen()) {
			Methods.s("Opening bank");
			Methods.openBank();
			final Timer timer = new Timer(3000);
			while(!Bank.isOpen()) {
				Task.sleep(5);
				if(!timer.isRunning()) break;
			}
		}
		if(Bank.isOpen()) {
			if(Inventory.getCount() > 0) {
				Methods.s("Banking inventory");
				Methods.depositInventory(Random.nextInt(0, 11) % 2 == 0);
				final Timer timer = new Timer(3000);
				while(Inventory.getCount() > 0) {
					Task.sleep(5);
					if(!timer.isRunning()) break;
				}
			}
			if(Inventory.getCount() < 1) {
				Methods.s("Leaving bank");
				Const.INSIDE_BANK.clickOnMap();
				Task.sleep(200);
			}
		}
	}
}
