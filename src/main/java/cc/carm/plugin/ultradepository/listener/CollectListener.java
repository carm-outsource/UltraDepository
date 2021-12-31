package cc.carm.plugin.ultradepository.listener;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CollectListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onBreak(BlockDropItemEvent event) {
		if (event.isCancelled() || !PluginConfig.Collect.BREAK.get()) return;

		Player player = event.getPlayer();
		if (!Main.getUserManager().isCollectEnabled(player)) return;
		if (event.getBlock().getType().isOccluding()) return;

		List<Item> droppedItems = event.getItems();
		if (droppedItems.isEmpty()) return;

		for (Item drop : droppedItems) {
			Main.debug("Dropped " + drop.getType().name() + " " + drop.getItemStack().getAmount());
		}

		event.getItems().removeIf(item -> Main.getDepositoryManager().collectItem(player, item.getItemStack()));

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(EntityDeathEvent event) {
		if (!PluginConfig.Collect.KILL.get()) return;

		Player player = event.getEntity().getKiller();
		if (player == null) return;
		if (!Main.getUserManager().isCollectEnabled(player)) return;

		Collection<ItemStack> finalDrops = Main.getDepositoryManager().collectItem(player, event.getDrops());
		event.getDrops().clear();
		event.getDrops().addAll(finalDrops);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPickup(EntityPickupItemEvent event) {
		if (event.isCancelled() || !PluginConfig.Collect.PICKUP.get()) return;
		if (!(event.getEntity() instanceof Player)) return;

		Player player = (Player) event.getEntity();
		if (!Main.getUserManager().isCollectEnabled(player)) return;

		// 自己扔出去的东西不计入背包
		UUID thrower = event.getItem().getThrower();
		if (thrower != null && thrower.equals(player.getUniqueId())) return;

		ItemStack item = event.getItem().getItemStack();
		Main.debug("Picked up " + item.getType().name() + " " + item.getAmount());
		if (Main.getDepositoryManager().collectItem(player, item)) {
			event.setCancelled(true);
			event.getItem().remove();
		}
	}


}
