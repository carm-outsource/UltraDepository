package cc.carm.plugin.ultradepository.storage;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.values.ConfigValue;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.util.DateUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Date;
import java.util.HashMap;
import java.util.UUID;

public class FileStorage implements DataStorage {

	private static final ConfigValue<String> FILE_PATH = new ConfigValue<>(
			"storage.file-path", String.class, "data"
	);

	private File dataContainer;

	@Override
	public boolean initialize() {
		return false;
	}

	@Override
	public void shutdown() {
		// 似乎没什么需要做的？
	}

	@Override
	public @NotNull UserData loadData(@NotNull UUID uuid) {
		long start = System.currentTimeMillis();
		Main.debug("正通过 FileStorage 加载 " + uuid + " 的用户数据...");
		return new UserData(uuid, this, new HashMap<>(), DateUtil.getCurrentDate());
	}

	@Override
	public void saveUserData(@NotNull UserData data) {
		long start = System.currentTimeMillis();
		Main.debug("正通过 FileStorage 保存 " + data.getUserUUID() + " 的用户数据...");

		Main.debug(
				"通过 FileStorage 保存 " + data.getUserUUID() + " 的用户数据完成，" +
						"耗时 " + (System.currentTimeMillis() - start) + "ms。"
		);
	}

}
