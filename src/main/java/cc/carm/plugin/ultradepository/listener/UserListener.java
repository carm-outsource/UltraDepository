package cc.carm.plugin.ultradepository.listener;

import cc.carm.plugin.ultradepository.Main;
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
		Main.debug("尝试加载玩家 " + event.getName() + " 的数据...");
		Main.getUserManager().getDataCache().put(uuid, Main.getUserManager().loadData(uuid));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPreLoginMonitor(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			Main.getUserManager().getDataCache().remove(event.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent e) {
		UserData data = Main.getUserManager().getData(e.getPlayer().getUniqueId());
		if (data == null) {
			e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			e.setKickMessage(Main.getInstance().getName() + " 数据未被正确加载，请重新进入。");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		Main.getScheduler().runAsync(() -> Main.getUserManager().unloadData(playerUUID, true));
	}

}
