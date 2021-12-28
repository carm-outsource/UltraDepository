package cc.carm.plugin.ultradepository.manager;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.data.UserData;
import com.google.common.collect.HashMultimap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class DepositoryManager {

	/**
	 * 记录仓库ID对应的仓库实例
	 */
	public HashMap<@NotNull String, @NotNull Depository> depositories;

	/**
	 * 用于记录储存每个物品ID所对应的背包ID
	 */
	public HashMultimap<@NotNull String, @NotNull String> itemMap;

	public DepositoryManager() {
		this.depositories = new HashMap<>();
		this.itemMap = HashMultimap.create();
	}

	public @NotNull HashMap<@NotNull String, @NotNull Depository> getDepositories() {
		return depositories;
	}

	public boolean hasDepository(@NotNull String depositoryID) {
		return getDepositories().containsKey(depositoryID);
	}

	public boolean hasItem(@NotNull String depositoryID, @NotNull String itemTypeID) {
		Depository configuration = getDepository(depositoryID);
		if (configuration == null) return false;
		return hasItem(configuration, itemTypeID);
	}

	public boolean hasItem(@NotNull Depository depository, @NotNull String itemTypeID) {
		return depository.getItems().containsKey(itemTypeID);
	}

	public @Nullable Depository getDepository(@NotNull String depositoryID) {
		return getDepositories().get(depositoryID);
	}

	public Set<Depository> getItemDepositories(ItemStack itemStack) {
		return getItemDepositories(itemStack.getType(), itemStack.getDurability());
	}

	public @Nullable Set<Depository> getItemDepositories(Material material, int data) {
		return Optional.ofNullable(itemMap.get(getItemTypeID(material, data)))
				.map(set -> set.stream().map(this::getDepository).collect(Collectors.toSet()))
				.orElse(null);
	}

	public Set<Depository> getPlayerUsableDepository(Player player, ItemStack itemStack) {
		String typeID = getItemTypeID(itemStack);
		return getItemDepositories(itemStack).stream().filter(configuration -> {
			int currentAmount = Optional.ofNullable(Main.getUserManager().getData(player)
					.getItemAmount(configuration.getIdentifier(), typeID)).orElse(0);
			int depositoryCapacity = configuration.getCapacity().getPlayerCapacity(player);
			return currentAmount + itemStack.getAmount() <= depositoryCapacity;
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
		ItemMeta meta = item.getItemMeta();
		if (meta != null && (meta.hasLore() || meta.hasDisplayName() || meta.hasEnchants())) {
			// 不收集有特殊属性的物品
			return false;
		}
		Set<Depository> usableDepositories = getPlayerUsableDepository(player, item);
		if (usableDepositories.size() < 1) return false;
		Depository depository = usableDepositories.stream().findFirst().orElse(null);

		String typeID = getItemTypeID(item);
		UserData data = Main.getUserManager().getData(player);
		int itemAmount = item.getAmount();
		data.addItemAmount(depository.getIdentifier(), typeID, itemAmount);
		return true;
	}

	/**
	 * 获得某背包配置中的某件物品单价，最低为0。
	 *
	 * @param depositoryID 背包ID
	 * @param itemTypeID   物品ID
	 * @return 若为空，则该背包或该物品不存在。
	 */
	public @Nullable Double getItemPrice(@NotNull String depositoryID, @NotNull String itemTypeID) {
		Depository configuration = getDepository(depositoryID);
		if (configuration == null) return null;
		DepositoryItem item = configuration.getItems().get(itemTypeID);
		if (item == null) return null;
		return item.getPrice();
	}

	/**
	 * 获得某背包配置中的某件物品每日售出限制，最低为0。
	 *
	 * @param depositoryID 背包ID
	 * @param itemTypeID   物品ID
	 * @return 若为空，则该背包或该物品不存在。
	 */
	public @Nullable Integer getItemSellLimit(@NotNull String depositoryID, @NotNull String itemTypeID) {
		Depository configuration = getDepository(depositoryID);
		if (configuration == null) return null;
		DepositoryItem item = configuration.getItems().get(itemTypeID);
		if (item == null) return null;
		return item.getLimit();
	}

	/**
	 * 获得某背包配置中的某件物品每日售出限制，最低为0。
	 *
	 * @param depository 背包
	 * @param itemTypeID 物品ID
	 * @return 若为空，则该背包或该物品不存在。
	 */
	public @Nullable Integer getItemSellLimit(@NotNull Depository depository, @NotNull String itemTypeID) {
		DepositoryItem item = depository.getItems().get(itemTypeID);
		if (item == null) return null;
		return item.getLimit();
	}


}
