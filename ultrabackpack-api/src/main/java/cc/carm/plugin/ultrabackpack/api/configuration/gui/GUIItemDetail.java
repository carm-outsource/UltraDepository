package cc.carm.plugin.ultrabackpack.api.configuration.gui;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface GUIItemDetail {


	ItemStack getIcon();

	List<GUIAction> getActions();


}
