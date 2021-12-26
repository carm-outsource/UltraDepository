package cc.carm.plugin.ultrabackpack.configuration.gui;

import cc.carm.plugin.ultrabackpack.util.gui.GUI;
import cc.carm.plugin.ultrabackpack.util.gui.GUIItem;
import cc.carm.plugin.ultrabackpack.util.gui.GUIType;
import com.google.common.collect.Multimap;

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
		return title;
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

}
