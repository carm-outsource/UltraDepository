package cc.carm.plugin.ultrabackpack.api.configuration.backpack;

import org.bukkit.Material;

import java.util.List;

public interface BackpackItem {

	Material getMaterial();

	int getData();

	int getSoldLimit();

	int getSoldPrice();

	int getDisplaySlot();

	String getDisplayName();

	List<String> getDisplayLore();

}
