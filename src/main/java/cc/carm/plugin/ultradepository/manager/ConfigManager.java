package cc.carm.plugin.ultradepository.manager;


import cc.carm.lib.easyplugin.configuration.file.FileConfig;
import cc.carm.plugin.ultradepository.UltraDepository;

public class ConfigManager {

	private static FileConfig pluginConfiguration;
	private static FileConfig messageConfiguration;

	public static void initConfig() {
		pluginConfiguration = new FileConfig(UltraDepository.getInstance(), "config.yml");
		messageConfiguration = new FileConfig(UltraDepository.getInstance(), "messages.yml");

		FileConfig.pluginConfiguration = () -> pluginConfiguration;
		FileConfig.messageConfiguration = () -> messageConfiguration;
	}

	public static FileConfig getPluginConfig() {
		return pluginConfiguration;
	}

	public static FileConfig getMessageConfig() {
		return messageConfiguration;
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
