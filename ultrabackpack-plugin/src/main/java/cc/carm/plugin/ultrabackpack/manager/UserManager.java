package cc.carm.plugin.ultrabackpack.manager;

import cc.carm.plugin.ultrabackpack.Main;
import cc.carm.plugin.ultrabackpack.api.manager.UBUserManager;
import cc.carm.plugin.ultrabackpack.data.UserData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.HashMap;
import java.util.UUID;

public class UserManager implements UBUserManager {

	private final HashMap<UUID, UserData> dataCache = new HashMap<>();

	public HashMap<UUID, UserData> getDataCache() {
		return dataCache;
	}

	@Override
	public @Nullable UserData getData(@NotNull UUID userUUID) {
		return getDataCache().get(userUUID);
	}

	@Override
	public @NotNull UserData getData(@NotNull Player player) {
		return getDataCache().get(player.getUniqueId());
	}

	@Override
	public @NotNull UserData loadData(@NotNull UUID userUUID) {
		try {
			return Main.getStorage().loadData(userUUID);
		} catch (Exception e) {
			Main.error("无法正常加载玩家数据，玩家操作将不会被保存，请检查数据配置！");
			Main.error("Error occurred when loading user's data, please check the configuration!");
			return new UserData(userUUID, Main.getStorage(), new HashMap<>(), new Date(System.currentTimeMillis()));
		}
	}


	public boolean isCollectEnabled(Player player) {
		return player.hasPermission("UltraBackpack.use") &&
				player.hasPermission("UltraBackpack.auto") &&
				player.hasPermission("UltraBackpack.auto.enable");
	}
}
