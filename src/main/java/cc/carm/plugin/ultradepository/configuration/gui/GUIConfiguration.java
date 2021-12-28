package cc.carm.plugin.ultradepository.configuration.gui;

import cc.carm.plugin.ultradepository.util.ColorParser;
import cc.carm.plugin.ultradepository.util.gui.GUI;
import cc.carm.plugin.ultradepository.util.gui.GUIItem;
import cc.carm.plugin.ultradepository.util.gui.GUIType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GUIConfiguration {

	String title;
	int lines;

	Multimap<GUIItem, Integer> guiItems;

	public GUIConfiguration(String title, int lines, Multimap<GUIItem, Integer> guiItems) {
		this.title = title;
		this.lines = lines;
		this.guiItems = guiItems;
	}

	public String getTitle() {
		return ColorParser.parse(title);
	}

	public int getLines() {
		return lines;
	}

	public GUIType getGUIType() {
		switch (lines) {
			case 1:
				return GUIType.ONE_BY_NINE;
			case 2:
				return GUIType.TWO_BY_NINE;
			case 3:
				return GUIType.THREE_BY_NINE;
			case 4:
				return GUIType.FOUR_BY_NINE;
			case 5:
				return GUIType.FIVE_BY_NINE;
			default:
				return GUIType.SIX_BY_NINE;
		}
	}

	public Multimap<GUIItem, Integer> getGuiItems() {
		return guiItems;
	}

	public void setupItems(GUI gui) {
		getGuiItems().forEach((gui::setItem));
	}


	public static GUIConfiguration readConfiguration(@Nullable ConfigurationSection section) {
		if (section == null) {
			return new GUIConfiguration("name", 6, ArrayListMultimap.create());
		}
		String title = section.getString("title", "");
		int lines = section.getInt("lines", 6);
		Multimap<GUIItem, Integer> guiItemMap = ArrayListMultimap.create();
		ConfigurationSection itemsSection = section.getConfigurationSection("items");
		if (itemsSection != null) {
			itemsSection.getKeys(false).stream()
					.map(key -> readItem(itemsSection.getConfigurationSection(key)))
					.filter(Objects::nonNull)
					.forEach(entry -> guiItemMap.putAll(entry.getKey(), entry.getValue()));

		}
		return new GUIConfiguration(title, lines, guiItemMap);
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
