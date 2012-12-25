package org.oneluckyduck.swamptoads.misc;

import java.awt.Image;

import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class Const {

	public static final int TOAD_ID = 2150;
	public static final int WORM_ID = 2162;
	public static final int UPSTAIRS_LADDER = 69504;
	public static final int DOWNSTAIRS_LADDER = 69505;

	public static final Timer TIMER = new Timer(0);

	public static final Tile TOADS_LOCATION = new Tile(2419, 3509, 0);
	public static final Tile BANK_LOCATION = new Tile(2445, 3435, 0);
	public static final Tile INSIDE_BANK = new Tile(2445, 3432, 1);
	
	public static final Image BAR = Methods.getImage("http://puu.sh/1p9xg.png");

	public static final Filter<GroundItem> TOAD_FILTER = new Filter<GroundItem>() {

		@Override
		public boolean accept(GroundItem g) {
			return g.getId() == TOAD_ID && g.getLocation().canReach();
		}
	};

	public static final Filter<NPC> BANKER_FILTER = new Filter<NPC>() {
		@Override
		public boolean accept(NPC n) {
			return n.getName().equals("Gnome banker")
					&& n.getLocation().canReach();
		}
	};

	public static final Area STRONGHOLD_AREA = new Area(new Tile[] {
			new Tile(2378, 3524, 0), new Tile(2484, 3522, 0),
			new Tile(2503, 3389, 0), new Tile(2368, 3392, 0),
			new Tile(2377, 3524, 0) });

	public static final Area LADDER_AREA = new Area(new Tile[] {
			new Tile(2440, 3437, 0), new Tile(2450, 3437, 0),
			new Tile(2450, 3430, 0), new Tile(2441, 3430, 0) });

	public static final Area TOADS_AREA = new Area(new Tile[] {
			new Tile(2405, 3519, 0), new Tile(2405, 3513, 0),
			new Tile(2408, 3509, 0), new Tile(2412, 3506, 0),
			new Tile(2417, 3505, 0), new Tile(2422, 3504, 0),
			new Tile(2427, 3504, 0), new Tile(2430, 3508, 0),
			new Tile(2431, 3513, 0), new Tile(2428, 3517, 0),
			new Tile(2424, 3520, 0), new Tile(2408, 3520, 0) });

}
