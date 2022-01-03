package cc.carm.plugin.ultradepository.manager;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.storage.DataStorage;
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
			long start = System.currentTimeMillis();
			DataStorage storage = Main.getStorage();
			Main.debug("正通过 " + storage.getClass().getSimpleName() + " 加载 " + userUUID + " 的用户数据...");
			UserData data = Main.getStorage().loadData(userUUID);

			if (data == null) {
				Main.debug("当前还不存在玩家 " + userUUID + " 的数据，视作新档。");
				return new UserData(userUUID, new HashMap<>(), DateIntUtil.getCurrentDate());
			}

			Main.debug("通过 " + storage.getClass().getSimpleName() + "加载 " + userUUID + " 的用户数据完成，"
					+ "耗时 " + (System.currentTimeMillis() - start) + "ms。");

			return data;
		} catch (Exception e) {
			Main.error("无法正常加载玩家数据，玩家操作将不会被保存，请检查数据配置！");
			Main.error("Could not load user's data, please check the data configuration!");
			return new UserData(userUUID, new HashMap<>(), DateIntUtil.getCurrentDate());
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
			long start = System.currentTimeMillis();
			DataStorage storage = Main.getStorage();

			Main.debug("正通过 " + storage.getClass().getSimpleName() + " 保存 " + data.getUserUUID() + " 的用户数据...");
			storage.saveUserData(data);

			Main.debug("通过 " + storage.getClass().getSimpleName() + " 保存 " + data.getUserUUID() + " 的用户数据完成，" +
					"耗时 " + (System.currentTimeMillis() - start) + "ms。");

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
