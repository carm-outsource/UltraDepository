package cc.carm.plugin.ultrabackpack.configuration.backpack;

import cc.carm.plugin.ultrabackpack.configuration.gui.GUIConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BackpackConfiguration {

	final String identifier;

	String name;
	GUIConfiguration guiConfiguration;
	BackpackCapacity capacity;

	Map<String, BackpackItem> items;


	public BackpackConfiguration(String identifier, String name,
								 GUIConfiguration guiConfiguration,
								 BackpackCapacity capacity,
								 Map<String, BackpackItem> items) {
		this.identifier = identifier;
		this.name = name;
		this.guiConfiguration = guiConfiguration;
		this.capacity = capacity;
		this.items = items;
	}

	public @NotNull String getIdentifier() {
		return this.identifier;
	}

	public @NotNull String getName() {
		return this.name;
	}


	public @NotNull GUIConfiguration getGUIConfiguration() {
		return this.guiConfiguration;
	}

	public @NotNull BackpackCapacity getCapacity() {
		return this.capacity;
	}

	public @NotNull Map<String, BackpackItem> getItems() {
		return this.items;
	}


}
