package org.oneluckyduck.swamptoads.misc;

import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.imageio.ImageIO;

import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;

public class Methods {

	public static boolean isAutoRetaliateEnabled() {
		final Widget attack = Widgets.get(464);
		final WidgetChild attackTab = Widgets.get(548).getChild(124);
		if (!isWidgetVisible(attack)) {
			attackTab.click(true);
		}
		final WidgetChild retaliate = attack.getChild(5);
		if (isWidgetChildVisible(retaliate)) {
			return retaliate.getText().toLowerCase().contains("On");
		}
		return false;
	}

	public static boolean toggleAutoRetaliate() {
		final Widget attack = Widgets.get(464);
		final WidgetChild attackTab = Widgets.get(548).getChild(124);
		if (!isWidgetVisible(attack)) {
			attackTab.click(true);
		}
		final WidgetChild retaliate = attack.getChild(5);
		if (isWidgetChildVisible(retaliate)) {
			return retaliate.click(true);
		}
		return false;
	}

	public static boolean openMenu(final Entity e, final boolean fast) {
		if (fast) {
			final Point p = e.getCentralPoint();
			Mouse.hop(p.x, p.y);
			return Mouse.click(false);
		} else {
			return Mouse.click(e.getCentralPoint(), false);
		}
	}

	public static boolean isInBank() {
		return Const.INSIDE_BANK.canReach();
	}

	public static void stopScript(final String s) {
		s(s);
		Context.get().getScriptHandler().shutdown();
	}

	public static boolean isRunEnabled() {
		return Widgets.get(750).getChild(2).getTextureId() == 781;
	}

	public static boolean isInArea(Area area) {
		return area.contains(Players.getLocal().getLocation());
	}

	public static boolean isWidgetChildVisible(final WidgetChild wc) {
		return wc != null && wc.validate() && wc.visible() && wc.isOnScreen();
	}

	public static boolean isWidgetVisible(final Widget w) {
		return w != null && w.validate();
	}

	public static boolean inventoryContains(final int id) {
		if (Tabs.getCurrent() != Tabs.INVENTORY) {
			Tabs.INVENTORY.open();
		}
		return isItemVisible(Inventory.getItem(id))
				&& Inventory.getCount(id) > 0;
	}

	public static boolean isInGame() {
		return Game.isLoggedIn() && Game.getClientState() == 11
				&& !Context.resolve().refreshing && !Lobby.isOpen();
	}

	public static void s(final String s) {
		Variables.status = s;
		System.out.println(String.format("[Swamp Toad Picker] %s", s));
	}

	public static void frequentChecks() {
		if (Walking.getEnergy() > 30 && !isRunEnabled()) {
			Mouse.click(Widgets.get(750).getChild(2).getCentralPoint(), true);
		}
		if (!Tabs.INVENTORY.isOpen()) {
			Tabs.INVENTORY.open();
		}
		if (Bank.isOpen()) {
			Bank.close();
		}
	}

	public boolean isIdle() {
		return (Players.getLocal() != null && !Players.getLocal().isMoving()
				&& Players.getLocal().getAnimation() == -1 && (Players
				.getLocal().getInteracting() == null || Players.getLocal()
				.getInteracting().getHpRatio() == 0));
	}

	public static int getPerHour(final int base, final long time) {
		return (int) ((base) * 3600000D / (System.currentTimeMillis() - time));
	}

	public static int[] listToArray(final List<Integer> list) {
		int[] ret = new int[list.size()];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = list.get(i).intValue();
		}
		return ret;
	}

	public static int getPrice(final int id) {
		try {
			String price;
			final URL url = new URL(
					"http://open.tip.it/json/ge_single_item?item=" + id);
			final URLConnection con = url.openConnection();
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				if (line.contains("mark_price")) {
					price = line.substring(line.indexOf("mark_price") + 13,
							line.indexOf(",\"daily_gp") - 1);
					price = price.replace(",", "");
					return Integer.parseInt(price);

				}
			}
		} catch (final Exception ignored) {
			return 0;
		}
		return 0;
	}

	private static boolean isItemVisible(final Item i) {
		return i != null && i.getWidgetChild().validate()
				&& i.getWidgetChild().visible()
				&& i.getWidgetChild().isOnScreen();
	}

	public static boolean depositInventory(final boolean fast) {
		if (fast) {
			final WidgetChild di = Widgets.get(762).getChild(34);
			if (di != null) {
				final Point dip = di.getCentralPoint();
				Mouse.hop(dip.x, dip.y);
				return Mouse.click(true);
			}
		} else {
			return Bank.depositInventory();
		}
		return false;
	}

	public static boolean openBank() {
		if (Menu.isOpen() && Menu.contains("Bank")) {
			return Menu.select("Bank");
		} else {
			final NPC banker = NPCs.getNearest(Const.BANKER_FILTER);
			if (banker != null && banker.isOnScreen()) {
				if (banker.click(false) && Menu.isOpen()
						&& Menu.contains("Bank")) {
					return Menu.select("Bank");
				} else if(!Menu.contains("Bank")){
					final Point p = Mouse.getLocation();
					int r = Random.nextInt(40, 109);
					Mouse.move(p.x + r, p.y + r);
				}
			} else if (!banker.isOnScreen()) {
				Camera.turnTo(banker);
			}
		}
		return false;
	}

	public static Image getImage(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}
}
