package cc.carm.plugin.ultrabackpack.api.configuration.backpack;

import cc.carm.plugin.ultrabackpack.api.configuration.gui.GUIConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface BackpackConfiguration {

	@NotNull String getIdentifier();

	@NotNull String getName();

	@NotNull GUIConfiguration getGUIConfiguration();

	@NotNull BackpackCapacity getCapacity();

	int getCapacityFor(Player player);

	@NotNull Map<String, BackpackItem> getItems();


}
