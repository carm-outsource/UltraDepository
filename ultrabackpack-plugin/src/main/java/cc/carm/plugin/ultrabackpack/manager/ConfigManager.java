package cc.carm.plugin.ultrabackpack.manager;


import cc.carm.plugin.ultrabackpack.Main;
import cc.carm.plugin.ultrabackpack.configuration.file.FileConfig;
import org.bukkit.Material;

public class ConfigManager {

	private static FileConfig config;
	private static FileConfig messageConfig;

	public static void initConfig() {
		ConfigManager.config = new FileConfig(Main.getInstance(), "config.yml");
		ConfigManager.messageConfig = new FileConfig(Main.getInstance(), "messages.yml");
	}

	public static FileConfig getPluginConfig() {
		return config;
	}

	public static FileConfig getMessageConfig() {
		return messageConfig;
	}

	public static void reload() {
		getPluginConfig().reload();
		getMessageConfig().reload();
	}

	public static void saveConfig() {
		getPluginConfig().save();
		getMessageConfig().save();
	}


}
