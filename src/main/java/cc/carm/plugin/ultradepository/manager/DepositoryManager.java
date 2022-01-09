package cc.carm.plugin.ultradepository.manager;

import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.event.DepositoryCollectItemEvent;
import com.google.common.collect.HashMultimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")
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

	public void loadDepositories() {
		long start = System.currentTimeMillis();
		UltraDepository.getInstance().log("	开始加载仓库配置...");
		File folder = new File(UltraDepository.getInstance().getDataFolder(), "depositories");
		if (!folder.exists()) {
			folder.mkdir();
		} else if (folder.isDirectory()) {
			folder.delete();
			folder.mkdir();
		}

		File[] files = folder.listFiles();
		if (files == null) return;
		HashMultimap<@NotNull String, @NotNull String> items = HashMultimap.create();
		HashMap<@NotNull String, @NotNull Depository> data = new HashMap<>();
		for (File file : files) {
			String fileName = file.getName();
			if (!file.isFile() || !fileName.toLowerCase().endsWith(".yml")) continue;
			String identifier = fileName.substring(0, fileName.lastIndexOf("."));
			FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
			Depository depository = Depository.loadFrom(identifier, configuration);
			if (depository.getItems().size() > 0) {
				depository.getItems().values().forEach(value -> items.put(value.getTypeID(), depository.getIdentifier()));
				data.put(identifier, depository);
			} else {
				UltraDepository.getInstance().error("	仓库 " + depository.getName() + " 未配置任何物品，请检查相关配置！");
			}
		}
		for (Map.Entry<String, Collection<String>> entry : items.asMap().entrySet()) {
			UltraDepository.getInstance().debug("# " + entry.getKey());
			for (String depositoryID : entry.getValue()) {
				UltraDepository.getInstance().debug("- " + depositoryID);
			}
		}

		this.depositories = data;
		this.itemMap = items;
		UltraDepository.getInstance().log("	仓库配置加载完成，共加载 " + data.size() + " 个仓库，耗时 " + (System.currentTimeMillis() - start) + "ms 。");
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
		return getItemDepositories(itemStack).stream().filter(configuration -> {
			int used = UltraDepository.getUserManager().getData(player).getDepositoryData(configuration).getUsedCapacity();
			int max = configuration.getCapacity().getPlayerCapacity(player);
			return max < 0 || used + itemStack.getAmount() <= max;
		}).collect(Collectors.toSet());
	}

	public @NotNull String getItemTypeID(Material material, int data) {
		if (data == 0) return material.name();
		else return material.name() + ":" + data;
	}

	public @NotNull String getItemTypeID(ItemStack itemStack) {
		return getItemTypeID(itemStack.getType(), itemStack.getDurability());
	}

	public Collection<ItemStack> collectItem(Player player, Collection<ItemStack> items) {
		if (!UltraDepository.getUserManager().isCollectEnabled(player)) {
			UltraDepository.getInstance().debug("player " + player.getName() + " disabled collect, skipped.");
			return items;
		} else return items.stream().filter(item -> !collectItem(player, item)).collect(Collectors.toList());
	}

	public boolean collectItem(Player player, ItemStack item) {
		String typeID = getItemTypeID(item);
		UltraDepository.getInstance().debug("Checking item " + typeID + " ...");
		if (!UltraDepository.getUserManager().isCollectEnabled(player)) {
			UltraDepository.getInstance().debug("Player " + player.getName() + " disabled collect, skipped.");
			return false;
		}
		ItemMeta meta = item.getItemMeta();
		if (meta != null && (meta.hasLore() || meta.hasDisplayName() || meta.hasEnchants())) {
			// 不收集有特殊属性的物品
			UltraDepository.getInstance().debug("Item has special meta, skipped.");
			return false;
		}
		Set<Depository> usableDepositories = getPlayerUsableDepository(player, item);
		if (usableDepositories.size() < 1) {
			UltraDepository.getInstance().debug("Item doesn't has any depository, skipped.");
			return false;
		}

		Depository depository = usableDepositories.stream().findFirst().orElse(null);
		DepositoryItem depositoryItem = depository.getItems().get(typeID);
		int itemAmount = item.getAmount();

		DepositoryCollectItemEvent collectItemEvent = new DepositoryCollectItemEvent(player, depository, depositoryItem, itemAmount);
		Bukkit.getPluginManager().callEvent(collectItemEvent);

		if (collectItemEvent.isCancelled()) return false;
		int finalAmount = collectItemEvent.getItemAmount();

		collectItemEvent.getUserData().addItemAmount(depository.getIdentifier(), typeID, finalAmount);
		if (!player.hasPermission("UltraDepository.silent")) {
			PluginMessages.ITEM_COLLECT.send(player, new Object[]{
					depository.getItems().get(typeID).getName(),
					finalAmount, depository.getName()
			});
			PluginConfig.Sounds.COLLECT.play(player);
		}
		UltraDepository.getInstance().debug("Item collected successfully.");
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
