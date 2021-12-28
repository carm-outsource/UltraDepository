package cc.carm.plugin.ultradepository.configuration.depository;

import cc.carm.plugin.ultradepository.configuration.gui.GUIConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class Depository {

	final String identifier;

	String name;
	GUIConfiguration guiConfiguration;
	DepositoryCapacity capacity;

	Map<String, DepositoryItem> items;


	public Depository(String identifier, String name,
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


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Depository that = (Depository) o;
		return identifier.equals(that.identifier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier);
	}
}
