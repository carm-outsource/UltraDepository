package cc.carm.plugin.ultrabackpack.manager;

import cc.carm.plugin.ultrabackpack.Main;
import cc.carm.plugin.ultrabackpack.configuration.backpack.BackpackConfiguration;
import cc.carm.plugin.ultrabackpack.configuration.backpack.BackpackItem;
import cc.carm.plugin.ultrabackpack.data.UserData;
import com.google.common.collect.HashMultimap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

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

	public Set<BackpackConfiguration> getItemBackpacks(ItemStack itemStack) {
		return getItemBackpacks(itemStack.getType(), itemStack.getDurability());
	}

	public @Nullable Set<BackpackConfiguration> getItemBackpacks(Material material, int data) {
		return Optional.ofNullable(itemMap.get(getItemTypeID(material, data)))
				.map(set -> set.stream().map(this::getBackpack).collect(Collectors.toSet()))
				.orElse(null);
	}

	public Set<BackpackConfiguration> getPlayerUsableBackpack(Player player, ItemStack itemStack) {
		String typeID = getItemTypeID(itemStack);
		return getItemBackpacks(itemStack).stream().filter(configuration -> {
			int currentAmount = Optional.ofNullable(Main.getUserManager().getData(player)
					.getItemAmount(configuration.getIdentifier(), typeID)).orElse(0);
			int backpackCapacity = configuration.getCapacity().getPlayerCapacity(player);
			return currentAmount + itemStack.getAmount() <= backpackCapacity;
		}).collect(Collectors.toSet());
	}

	public @NotNull String getItemTypeID(Material material, int data) {
		return material.name() + ":" + data;
	}

	public @NotNull String getItemTypeID(ItemStack itemStack) {
		return getItemTypeID(itemStack.getType(), itemStack.getDurability());
	}

	public Collection<ItemStack> collectItem(Player player, Collection<ItemStack> items) {
		if (!Main.getUserManager().isCollectEnabled(player)) return new ArrayList<>();
		else return items.stream().filter(item -> collectItem(player, item)).collect(Collectors.toList());
	}

	public boolean collectItem(Player player, ItemStack item) {
		if (!Main.getUserManager().isCollectEnabled(player)) return false;
		Set<BackpackConfiguration> usableBackpacks = getPlayerUsableBackpack(player, item);
		if (usableBackpacks.size() < 1) return false;
		BackpackConfiguration configuration = usableBackpacks.stream().findFirst().orElse(null);

		String typeID = getItemTypeID(item);
		UserData data = Main.getUserManager().getData(player);
		int itemAmount = item.getAmount();
		data.addItemAmount(configuration.getIdentifier(), typeID, itemAmount);
		return true;
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
	public @Nullable Integer getItemSellLimit(@NotNull String backpackID, @NotNull String itemTypeID) {
		BackpackConfiguration configuration = getBackpack(backpackID);
		if (configuration == null) return null;
		BackpackItem item = configuration.getItems().get(itemTypeID);
		if (item == null) return null;
		return item.getLimit();
	}

	/**
	 * 获得某背包配置中的某件物品每日售出限制，最低为0。
	 *
	 * @param backpack   背包
	 * @param itemTypeID 物品ID
	 * @return 若为空，则该背包或该物品不存在。
	 */
	public @Nullable Integer getItemSellLimit(@NotNull BackpackConfiguration backpack, @NotNull String itemTypeID) {
		BackpackItem item = backpack.getItems().get(itemTypeID);
		if (item == null) return null;
		return item.getLimit();
	}


}
