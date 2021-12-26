package cc.carm.plugin.ultrabackpack.manager;

import cc.carm.plugin.ultrabackpack.configuration.backpack.BackpackConfiguration;
import cc.carm.plugin.ultrabackpack.configuration.backpack.BackpackItem;
import com.google.common.collect.HashMultimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class BackpackManager {

	public HashMap<@NotNull String, @NotNull BackpackConfiguration> backpacks;

	/**
	 * 用于记录储存每个物品ID所对应的背包ID
	 */
	public HashMultimap<@NotNull String, @NotNull String> itemMap;

	public BackpackManager() {
		this.backpacks = new HashMap<>();
		this.itemMap = HashMultimap.create();
	}

	public @NotNull HashMap<@NotNull String, @NotNull BackpackConfiguration> getBackpacks() {
		return backpacks;
	}

	public boolean hasBackpack(@NotNull String backpackID) {
		return getBackpacks().containsKey(backpackID);
	}

	public boolean hasItem(@NotNull String backpackID, @NotNull String itemTypeID) {
		BackpackConfiguration configuration = getBackpack(backpackID);
		if (configuration == null) return false;
		return configuration.getItems().containsKey(itemTypeID);
	}

	public @Nullable BackpackConfiguration getBackpack(@NotNull String backpackID) {
		return getBackpacks().get(backpackID);
	}

	/**
	 * 获得某背包配置中的某件物品单价，最低为0。
	 *
	 * @param backpackID 背包ID
	 * @param itemTypeID 物品ID
	 * @return 若为空，则该背包或该物品不存在。
	 */
	public @Nullable Double getItemPrice(@NotNull String backpackID, @NotNull String itemTypeID) {
		BackpackConfiguration configuration = getBackpack(backpackID);
		if (configuration == null) return null;
		BackpackItem item = configuration.getItems().get(itemTypeID);
		if (item == null) return null;
		return item.getPrice();
	}

	/**
	 * 获得某背包配置中的某件物品每日售出限制，最低为0。
	 *
	 * @param backpackID 背包ID
	 * @param itemTypeID 物品ID
	 * @return 若为空，则该背包或该物品不存在。
	 */
	public @Nullable Integer getItemLimit(@NotNull String backpackID, @NotNull String itemTypeID) {
		BackpackConfiguration configuration = getBackpack(backpackID);
		if (configuration == null) return null;
		BackpackItem item = configuration.getItems().get(itemTypeID);
		if (item == null) return null;
		return item.getLimit();
	}


}
