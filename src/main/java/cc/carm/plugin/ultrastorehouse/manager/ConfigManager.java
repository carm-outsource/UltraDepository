package cc.carm.plugin.ultrastorehouse.manager;


import cc.carm.plugin.ultrastorehouse.Main;
import cc.carm.plugin.ultrastorehouse.configuration.file.FileConfig;
import cc.carm.plugin.ultrastorehouse.configuration.gui.GUIActionConfiguration;
import cc.carm.plugin.ultrastorehouse.configuration.gui.GUIActionType;
import cc.carm.plugin.ultrastorehouse.configuration.gui.GUIConfiguration;
import cc.carm.plugin.ultrastorehouse.util.gui.GUIItem;
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

	public static GUIConfiguration readConfiguration(ConfigurationSection section) {
		String title = section.getString("title", "");
		int liens = section.getInt("lines", 6);
		Multimap<GUIItem, Integer> guiItemMap = ArrayListMultimap.create();
		ConfigurationSection itemsSection = section.getConfigurationSection("items");
		if (itemsSection != null) {
			itemsSection.getKeys(false).stream()
					.map(key -> readItem(itemsSection.getConfigurationSection(key)))
					.filter(Objects::nonNull)
					.forEach(entry -> guiItemMap.putAll(entry.getKey(), entry.getValue()));

		}
		return new GUIConfiguration(title, liens, guiItemMap);
	}

	@Nullable
	private static AbstractMap.SimpleEntry<GUIItem, List<Integer>> readItem(@Nullable ConfigurationSection itemSection) {
		if (itemSection == null) return null;
		ItemStack icon = itemSection.getItemStack("icon", new ItemStack(Material.STONE));
		List<Integer> slots = itemSection.getIntegerList("slots");
		int slot = itemSection.getInt("slot", 0);

		List<String> actionsString = itemSection.getStringList("actions");
		List<GUIActionConfiguration> actions = new ArrayList<>();
		for (String actionString : actionsString) {
			int prefixStart = actionString.indexOf("[");
			int prefixEnd = actionString.indexOf("]");
			if (prefixStart < 0 || prefixEnd < 0) continue;

			String prefix = actionString.substring(prefixStart + 1, prefixEnd);
			ClickType clickType = null;
			GUIActionType actionType;
			if (prefix.contains(":")) {
				String[] args = prefix.split(":");
				clickType = readClickType(args[0]);
				actionType = GUIActionType.readActionType(args[1]);
			} else {
				actionType = GUIActionType.readActionType(prefix);
			}

			if (actionType == null) continue;
			actions.add(new GUIActionConfiguration(clickType, actionType, actionString.substring(prefixEnd + 1).trim()));
		}
		GUIItem item = new GUIItem(icon);
		actions.stream().map(GUIActionConfiguration::toClickAction).forEach(item::addClickAction);

		return new AbstractMap.SimpleEntry<>(item, slots.size() > 0 ? slots : Collections.singletonList(slot));
	}

	public static ClickType readClickType(String type) {
		return Arrays.stream(ClickType.values())
				.filter(click -> click.name().equalsIgnoreCase(type))
				.findFirst().orElse(null);
	}


}
