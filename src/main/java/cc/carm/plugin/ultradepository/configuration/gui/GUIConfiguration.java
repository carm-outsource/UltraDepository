package cc.carm.plugin.ultradepository.configuration.gui;

import cc.carm.plugin.ultradepository.util.ColorParser;
import cc.carm.plugin.ultradepository.util.gui.GUI;
import cc.carm.plugin.ultradepository.util.gui.GUIType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUIConfiguration {

	String title;
	int lines;

	List<GUIItemConfiguration> guiItems;

	public GUIConfiguration(String title, int lines, List<GUIItemConfiguration> guiItems) {
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

	public List<GUIItemConfiguration> getGuiItems() {
		return guiItems;
	}

	public void setupItems(Player player, GUI gui) {
		getGuiItems().forEach(itemConfiguration -> itemConfiguration.setupItems(player, gui));
	}


	public static GUIConfiguration readConfiguration(@Nullable ConfigurationSection section) {
		if (section == null) return new GUIConfiguration("name", 6, new ArrayList<>());

		String title = section.getString("title", "");
		int lines = section.getInt("lines", 6);
		ConfigurationSection itemsSection = section.getConfigurationSection("items");
		if (itemsSection == null) return new GUIConfiguration(title, lines, new ArrayList<>());

		return new GUIConfiguration(
				title, lines, itemsSection.getKeys(false).stream()
				.map(key -> GUIItemConfiguration.readFrom(itemsSection.getConfigurationSection(key)))
				.filter(Objects::nonNull)
				.collect(Collectors.toList())
		);
	}

	public static ClickType readClickType(String type) {
		return Arrays.stream(ClickType.values())
				.filter(click -> click.name().equalsIgnoreCase(type))
				.findFirst().orElse(null);
	}

}
