package cc.carm.plugin.ultradepository.util.gui;

import cc.carm.plugin.ultradepository.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GUIListener implements Listener {

	final GUI currentGUI;
	final Player player;

	public GUIListener(GUI gui, Player player) {
		this.currentGUI = gui;
		this.player = player;
	}

	public GUI getCurrentGUI() {
		return currentGUI;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		getCurrentGUI().rawClickListener(event);

		Player p = (Player) event.getWhoClicked();
		if (event.getSlot() != -999) {
			try {
				if (GUI.getOpenedGUI(p) == getCurrentGUI()
						&& event.getClickedInventory() != null
						&& event.getClickedInventory().equals(getCurrentGUI().inv)
						&& getCurrentGUI().items[event.getSlot()] != null) {
					getCurrentGUI().items[event.getSlot()].realRawClickAction(event);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				Main.error("error cause by GUI(" + getCurrentGUI() + "), name=" + getCurrentGUI().name);
				e.printStackTrace();
				return;
			}
		} else if (getCurrentGUI().cancelOnOuter) {
			event.setCancelled(true);
		}

		if (GUI.hasOpenedGUI(p)
				&& GUI.getOpenedGUI(p) == getCurrentGUI()
				&& event.getClickedInventory() != null) {

			if (event.getClickedInventory().equals(getCurrentGUI().inv)) {
				if (getCurrentGUI().cancelOnTarget) event.setCancelled(true);

				if (event.getSlot() != -999 && getCurrentGUI().items[event.getSlot()] != null) {

					GUIItem item = getCurrentGUI().items[event.getSlot()];

					if (item.isActionActive()) {
						item.onClick(event.getClick());
						item.rawClickAction(event);
						item.actions.forEach(action -> action.run(event.getClick(), player));
					}

					item.actionsIgnoreActive.forEach(action -> action.run(event.getClick(), player));

				}
			} else if (event.getClickedInventory().equals(p.getInventory()) && getCurrentGUI().cancelOnSelf) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().equals(getCurrentGUI().inv) || e.getInventory().equals(p.getInventory())) {
			getCurrentGUI().onDrag(e);
		}

	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();
		if (event.getInventory().equals(getCurrentGUI().inv)) {
			HandlerList.unregisterAll(this);
			getCurrentGUI().listener = null;
			getCurrentGUI().onClose();
			GUI.removeOpenedGUI(p);
		}
	}

}
