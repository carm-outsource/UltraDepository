package cc.carm.plugin.ultradepository.configuration.depository;

import cc.carm.plugin.ultradepository.configuration.gui.GUIConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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

	public static Depository loadFrom(String identifier, FileConfiguration configuration) {
		return new Depository(
				identifier, configuration.getString("name"),
				GUIConfiguration.readConfiguration(configuration.getConfigurationSection("gui")),
				new DepositoryCapacity(
						configuration.getInt("capacity.default", 0),
						configuration.getStringList("capacity.permissions")
				),
				readItems(configuration.getConfigurationSection("items"))
		);
	}

	private static Map<String, DepositoryItem> readItems(ConfigurationSection section) {
		if (section == null) return new HashMap<>();
		Map<String, DepositoryItem> items = new HashMap<>();
		for (String key : section.getKeys(false)) {
			ConfigurationSection itemSection = section.getConfigurationSection(key);
			if (itemSection != null) {
				items.put(key, DepositoryItem.readFrom(key, itemSection));
			}
		}
		return items;
	}
}
