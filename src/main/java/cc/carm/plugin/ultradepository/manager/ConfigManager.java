package cc.carm.plugin.ultradepository.manager;


import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.file.FileConfig;
import cc.carm.plugin.ultradepository.configuration.gui.GUIActionConfiguration;
import cc.carm.plugin.ultradepository.configuration.gui.GUIActionType;
import cc.carm.plugin.ultradepository.configuration.gui.GUIConfiguration;
import cc.carm.plugin.ultradepository.util.gui.GUIItem;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConfigManager {

	private static FileConfig config;
	private static FileConfig messageConfig;

	public static void initConfig() {
		ConfigManager.config = new FileConfig(Main.getInstance(), "config.yml");
		ConfigManager.messageConfig = new FileConfig(Main.getInstance(), "src/main/resources/messages.yml");
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
