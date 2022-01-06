package cc.carm.plugin.ultradepository.manager;


import cc.carm.lib.easyplugin.configuration.file.FileConfig;
import cc.carm.plugin.ultradepository.UltraDepository;

public class ConfigManager {

	private static FileConfig pluginConfiguration;
	private static FileConfig messageConfiguration;

	public static boolean initialize() {
		try {
			pluginConfiguration = new FileConfig(UltraDepository.getInstance(), "config.yml");
			messageConfiguration = new FileConfig(UltraDepository.getInstance(), "messages.yml");

			FileConfig.pluginConfiguration = () -> pluginConfiguration;
			FileConfig.messageConfiguration = () -> messageConfiguration;
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static FileConfig getPluginConfig() {
		return pluginConfiguration;
	}

	public static FileConfig getMessageConfig() {
		return messageConfiguration;
	}

	public static void reload() {
		try {
			getPluginConfig().reload();
			getMessageConfig().reload();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void saveConfig() {
		try {
			getPluginConfig().save();
			getMessageConfig().save();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
