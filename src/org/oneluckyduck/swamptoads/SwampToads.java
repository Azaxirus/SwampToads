package org.oneluckyduck.swamptoads;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.oneluckyduck.swamptoads.misc.Const;
import org.oneluckyduck.swamptoads.misc.Methods;
import org.oneluckyduck.swamptoads.misc.Variables;
import org.oneluckyduck.swamptoads.nodes.Banking;
import org.oneluckyduck.swamptoads.nodes.EnterBank;
import org.oneluckyduck.swamptoads.nodes.LeaveBank;
import org.oneluckyduck.swamptoads.nodes.PickToads;
import org.oneluckyduck.swamptoads.nodes.WalkToBank;
import org.oneluckyduck.swamptoads.nodes.WalkToSwamp;
import org.oneluckyduck.swamptoads.nodes.failsafes.OutOfStronghold;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.input.Mouse.Speed;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.bot.Context;

@Manifest(authors = { "OneLuckyDuck" }, name = "Swamp Toad Picker", description = "Picks up swamp toads", version = 1.0, vip = true)
public class SwampToads extends ActiveScript implements PaintListener {

	public static Tree jobContainer = null;
	public static ArrayList<Node> jobs = new ArrayList<Node>();

	public void onStart() {
		Mouse.setSpeed(Speed.FAST);

		Variables.toadPrice = Methods.getPrice(Const.TOAD_ID);
		Variables.startTime = System.currentTimeMillis();
		Methods.s("Toad price: " + Variables.toadPrice);
		Variables.ready = true;
		// Context.resolve().refresh();
	}

	@Override
	public int loop() {
		if (Methods.isInGame() && Variables.ready) {
			if (jobContainer != null) {
				final Node job = jobContainer.state();
				if (job != null) {
					jobContainer.set(job);
					getContainer().submit(job);
					job.join();
				}
			} else {
				jobs.add(new Banking());
				jobs.add(new LeaveBank());
				jobs.add(new EnterBank());
				jobs.add(new WalkToSwamp());
				jobs.add(new WalkToBank());
				jobs.add(new PickToads());
				// jobs.add(new OutOfStronghold());
				jobContainer = new Tree(jobs.toArray(new Node[jobs.size()]));
			}
		}
		return 100;
	}

	@Override
	public void onRepaint(Graphics g1) {
		// if (!Methods.isInGame())
		// return;
		// if (Methods.isInGame()) {
		final Point p = Mouse.getLocation();
		final Graphics2D g = (Graphics2D) g1;

		int profint = (Variables.toadPrice * Variables.toadsPicked);

		int pickedHour = Methods.getPerHour(Variables.toadsPicked,
				Variables.startTime);
		int profintHour = Methods.getPerHour(profint, Variables.startTime);

		String picked = DecimalFormat.getInstance().format(
				Variables.toadsPicked);
		String pickedHourly = DecimalFormat.getInstance().format(pickedHour);

		String profit = DecimalFormat.getInstance().format(profint);
		String profitHourly = DecimalFormat.getInstance().format(profintHour);

		g.setColor(Mouse.isPressed() ? Color.GREEN.brighter() : Color.RED
				.brighter());
		g.drawOval(p.x - 3, p.y - 3, 6, 6);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) Game.getDimensions().getWidth(), 50);

		g.setColor(Color.GRAY);
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.drawString("Run Time: " + Const.TIMER.toElapsedString(), 3, 12);
		g.drawString(String.format("Swamp Toads Picked (hr): %s (%s)", picked,
				pickedHourly), 3, 25);
		g.drawString(
				String.format("Profit (hr): %s (%s)", profit, profitHourly), 3,
				38);

		g.setFont(new Font("Kristen ITC", Font.BOLD, 11));
		g.drawString("Swamp Toad Picker by OneLuckyDuck", 5, 375);

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.BLUE);
		g2.setFont(new Font("Arial", Font.BOLD, 12));
		g2.drawImage(Const.BAR, 0, 510, null, null);
		g2.drawString("Status: " + Variables.status, 15, 522);

		if (Methods.isInArea(Const.TOADS_AREA)) {
			final GroundItem toad = GroundItems.getNearest(Const.TOAD_FILTER);
			if (toad != null) {
				for (Polygon p1 : toad.getLocation().getBounds()) {
					g2.drawPolygon(p1);
				}
			}
		}
		// }
	}
}
