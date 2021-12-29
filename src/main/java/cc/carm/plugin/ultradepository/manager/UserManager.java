package cc.carm.plugin.ultradepository.manager;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.util.DateIntUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class UserManager {

	private final HashMap<UUID, UserData> dataCache = new HashMap<>();

	public HashMap<UUID, UserData> getDataCache() {
		return dataCache;
	}

	public @Nullable UserData getData(@NotNull UUID userUUID) {
		return getDataCache().get(userUUID);
	}

	public @NotNull UserData getData(@NotNull Player player) {
		return getDataCache().get(player.getUniqueId());
	}

	public @NotNull UserData loadData(@NotNull UUID userUUID) {
		try {
			return Main.getStorage().loadData(userUUID);
		} catch (Exception e) {
			Main.error("无法正常加载玩家数据，玩家操作将不会被保存，请检查数据配置！");
			Main.error("Could not load user's data, please check the data configuration!");
			return new UserData(userUUID, Main.getStorage(), new HashMap<>(), DateIntUtil.getCurrentDate());
		}
	}

	public void unloadData(UUID uuid, boolean save) {
		UserData data = getData(uuid);
		if (data == null) return;
		if (save) saveData(data);
		dataCache.remove(uuid);
	}

	public void saveData(UserData data) {
		try {
			data.save();
			Main.debug(" 玩家 " + data.getUserUUID() + " 数据已保存。");
		} catch (Exception e) {
			Main.error("无法正常保存玩家数据，请检查数据配置！");
			Main.error("Could not save user's data, please check the data configuration!");
			e.printStackTrace();
		}
	}

	public void saveAll() {
		dataCache.values().forEach(this::saveData);
	}

	public boolean isCollectEnabled(Player player) {
		return player.hasPermission("UltraDepository.use") &&
				player.hasPermission("UltraDepository.auto") &&
				player.hasPermission("UltraDepository.auto.enable");
	}


}
