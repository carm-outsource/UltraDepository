package cc.carm.plugin.ultradepository.storage;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.configuration.values.ConfigValue;
import cc.carm.plugin.ultradepository.data.DepositoryData;
import cc.carm.plugin.ultradepository.data.DepositoryItemData;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.util.DateIntUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FileStorage implements DataStorage {

	private static final ConfigValue<String> FILE_PATH = new ConfigValue<>(
			"storage.file-path", String.class, "data"
	);

	private File dataContainer;

	@Override
	public boolean initialize() {
		dataContainer = new File(Main.getInstance().getDataFolder(), FILE_PATH.get());
		if (!dataContainer.exists()) {
			return dataContainer.mkdir();
		} else {
			return dataContainer.isDirectory();
		}
	}

	@Override
	public void shutdown() {
		// 似乎没什么需要做的？
	}

	public File getDataContainer() {
		return dataContainer;
	}

	@Override
	public @Nullable UserData loadData(@NotNull UUID uuid) {
		File userDataFile = new File(getDataContainer(), uuid + ".yml");
		if (!userDataFile.exists()) {
			Main.debug("当前文件夾内不存在玩家 " + uuid + " 的数据，视作新档。");
			return null;
		}

		YamlConfiguration userDataConfig = YamlConfiguration.loadConfiguration(userDataFile);
		int dateInt = userDataConfig.getInt("date", DateIntUtil.getCurrentDate());
		UserData userData = new UserData(uuid, new HashMap<>(), dateInt);

		ConfigurationSection depositoriesSection = userDataConfig.getConfigurationSection("depositories");
		if (depositoriesSection != null) {
			for (String depositoryID : depositoriesSection.getKeys(false)) {

				Depository depository = Main.getDepositoryManager().getDepository(depositoryID);
				if (depository == null) continue;

				ConfigurationSection depositorySection = depositoriesSection.getConfigurationSection(depositoryID);
				if (depositorySection == null) continue;

				DepositoryData depositoryData = DepositoryData.emptyContents(depository, userData);

				for (String itemTypeID : depositorySection.getKeys(false)) {
					DepositoryItem item = depository.getItems().get(itemTypeID);
					if (item == null) continue;

					ConfigurationSection itemSection = depositorySection.getConfigurationSection(itemTypeID);
					if (itemSection == null) continue;

					depositoryData.getContents().put(item.getTypeID(), new DepositoryItemData(
							item, depositoryData,
							itemSection.getInt("amount", 0),
							itemSection.getInt("sold", 0)
					));

				}

				if (!depositoryData.getContents().isEmpty()) userData.setDepository(depositoryData);
			}
		}
		return userData;
	}

	@Override
	public void saveUserData(@NotNull UserData data) throws IOException {
		YamlConfiguration userDataConfig = new YamlConfiguration();
		userDataConfig.set("date", data.getDateInt());
		userDataConfig.createSection("depositories", data.serializeToMap());
		userDataConfig.save(new File(getDataContainer(), data.getUserUUID() + ".yml"));
	}

}
