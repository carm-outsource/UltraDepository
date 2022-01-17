package cc.carm.plugin.ultradepository.listener;

import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;
import cc.carm.plugin.ultradepository.data.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPreLogin(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			return;
		}
		UUID uuid = event.getUniqueId();
		UltraDepository.getUserManager().getDataCache()
				.put(uuid, UltraDepository.getUserManager().loadData(uuid));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPreLoginMonitor(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			UltraDepository.getUserManager().getDataCache().remove(event.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent e) {
		UserData data = UltraDepository.getUserManager().getData(e.getPlayer().getUniqueId());
		if (data == null) {
			e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			e.setKickMessage(PluginMessages.LOAD_FAILED.get(e.getPlayer()));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		UltraDepository.getInstance().getScheduler()
				.runAsync(() -> UltraDepository.getUserManager().unloadData(playerUUID, true));
	}

}
