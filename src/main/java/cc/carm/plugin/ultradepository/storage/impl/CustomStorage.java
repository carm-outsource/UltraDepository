package cc.carm.plugin.ultradepository.storage.impl;

import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.storage.DataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.UUID;

public class CustomStorage implements DataStorage {

	@Override
	@TestOnly
	public boolean initialize() {
		UltraDepository.getInstance().error("您选择使用自定义存储，但并没有应用成功。");
		UltraDepository.getInstance().error("请访问 https://github.com/CarmJos/UltraDepository/blob/master/.documentation 获取相关帮助！");
		UltraDepository.getInstance().error("You are using CustomStorage, but not overwrite the methods.");
		UltraDepository.getInstance().error("Please view https://github.com/CarmJos/UltraDepository/blob/master/.documentation to get more information.");
		return false;
	}

	@Override
	@TestOnly
	public void shutdown() {

	}

	@Override
	@TestOnly
	public @Nullable UserData loadData(@NotNull UUID uuid) {
		return null;
	}

	@Override
	@TestOnly
	public void saveUserData(@NotNull UserData data) {

	}

}
