package cc.carm.plugin.ultrabackpack.listener;

import cc.carm.plugin.ultrabackpack.Main;
import cc.carm.plugin.ultrabackpack.configuration.PluginConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.UUID;

public class CollectListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onBreak(BlockBreakEvent event) {
		if (event.isCancelled() || !event.isDropItems() || !PluginConfig.Collect.BREAK.get()) return;

		Player player = event.getPlayer();
		if (!Main.getUserManager().isCollectEnabled(player)) return;

		Location location = event.getBlock().getLocation();
		World world = event.getBlock().getWorld();

		Collection<ItemStack> drops;
		if (player.getItemInUse() == null) {
			drops = event.getBlock().getDrops();
		} else {
			drops = event.getBlock().getDrops(player.getItemInUse(), player.getPlayer());
		}

		if (drops.isEmpty()) return;
		event.setDropItems(false);

		Collection<ItemStack> finalDrops = Main.getBackpackManager().collectItem(player, drops);
		finalDrops.forEach(finalDrop -> world.dropItemNaturally(location, finalDrop));

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(EntityDeathEvent event) {
		if (!PluginConfig.Collect.KILL.get()) return;

		Player player = event.getEntity().getKiller();
		if (player == null) return;
		if (!Main.getUserManager().isCollectEnabled(player)) return;

		Collection<ItemStack> finalDrops = Main.getBackpackManager().collectItem(player, event.getDrops());
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
		if (Main.getBackpackManager().collectItem(player, item)) {
			event.setCancelled(true);
			event.getItem().remove();
		}
	}


}
