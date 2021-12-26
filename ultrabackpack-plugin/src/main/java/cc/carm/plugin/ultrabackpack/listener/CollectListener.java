package cc.carm.plugin.ultrabackpack.listener;

import cc.carm.plugin.ultrabackpack.configuration.PluginConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.UUID;

public class CollectListener implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (!PluginConfig.Collect.BREAK.get()) return;
		Player player = event.getPlayer();

		Collection<ItemStack> drops = event.getBlock().getDrops();

	}

	@EventHandler
	public void onBreak(EntityDeathEvent event) {
		if (!PluginConfig.Collect.KILL.get()) return;
		Player player = event.getEntity().getKiller();
		if (player == null) return;

		Collection<ItemStack> drops = event.getDrops();
	}

	public void onPickup(EntityPickupItemEvent event) {
		if (!PluginConfig.Collect.PICKUP.get()) return;
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();

		UUID thrower = event.getItem().getThrower();
		if (thrower != null && thrower.equals(player.getUniqueId())) return;

		ItemStack item = event.getItem().getItemStack();

	}


}
