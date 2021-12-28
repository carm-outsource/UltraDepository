package cc.carm.plugin.ultrastorehouse.configuration.depository;

import cc.carm.plugin.ultrastorehouse.configuration.gui.GUIConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ItemDepository {

	final String identifier;

	String name;
	GUIConfiguration guiConfiguration;
	DepositoryCapacity capacity;

	Map<String, DepositoryItem> items;


	public ItemDepository(String identifier, String name,
						  GUIConfiguration guiConfiguration,
						  DepositoryCapacity capacity,
						  Map<String, DepositoryItem> items) {
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

	public @NotNull DepositoryCapacity getCapacity() {
		return this.capacity;
	}

	public @NotNull Map<String, DepositoryItem> getItems() {
		return this.items;
	}

}
