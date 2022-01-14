package cc.carm.plugin.ultradepository.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.utils.ItemStackFactory;
import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.data.DepositoryItemData;
import cc.carm.plugin.ultradepository.data.UserData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DepositoryGUI extends GUI {

	Player player;
	UserData userData;
	Depository depository;

	private DepositoryGUI(Player player, Depository depository) {
		super(depository.getGUIConfiguration().getGUIType(), depository.getGUIConfiguration().getTitle());

		this.player = player;
		this.userData = UltraDepository.getUserManager().getData(player);
		this.depository = depository;

		setupItems();
	}

	public void setupItems() {
		loadConfigItems();
		loadDepositoryItems();
	}

	public void loadConfigItems() {
		depository.getGUIConfiguration().setupItems(player, this);
	}

	public void loadDepositoryItems() {
		depository.getItems().values().forEach(depositoryItem -> setItem(depositoryItem.getSlot(), createGUIItem(depositoryItem)));
	}

	private GUIItem createGUIItem(DepositoryItem item) {
		return new GUIItem(getItemIcon(player, userData, item)) {
			@Override
			public void onClick(ClickType type) {
				DepositoryItemData itemData = userData.getItemData(item);
				if (itemData.getAmount() < 1) {
					PluginConfig.Sounds.SELL_FAIL.play(player);
					PluginMessages.NO_ENOUGH_ITEM.send(player);
					return;
				}

				if (canSell(item) && type == ClickType.LEFT) {
					int sellableAmount = item.getLimit() - itemData.getSold();
					if (sellableAmount >= 1) {
						SellItemGUI.open(player, userData, itemData, depository, item);
					} else {
						PluginConfig.Sounds.SELL_FAIL.play(player);
						PluginMessages.ITEM_SOLD_LIMIT.send(player, new Object[]{sellableAmount, item.getLimit()});
						player.closeInventory();
					}
				} else if (type == ClickType.RIGHT) {

					if (hasEmptySlot(player)) {
						int takeoutAmount = Math.min(itemData.getAmount(), item.getMaterial().getMaxStackSize());
						userData.removeItemAmount(
								item.getDepository().getIdentifier(), item.getTypeID(), takeoutAmount
						);
						player.getInventory().addItem(item.getRawItem(takeoutAmount));
						PluginMessages.ITEM_TAKEOUT.send(player, new Object[]{
								item.getName(), takeoutAmount
						});
						PluginConfig.Sounds.TAKEOUT.play(player);

						setDisplay(getItemIcon(player, userData, item)); // 刷新物品显示
						loadConfigItems(); // 更新配置中的其他物品
						updateView();
					} else {
						PluginMessages.NO_SPACE.send(player);
						player.closeInventory();
					}
				}
			}
		};
	}

	public static boolean hasEmptySlot(Player player) {
		return IntStream.range(0, 36)
				.mapToObj(i -> player.getInventory().getItem(i))
				.anyMatch(i -> i == null || i.getType() == Material.AIR);
	}

	public static ItemStack getItemIcon(@NotNull Player player,
										@NotNull UserData userData,
										@NotNull DepositoryItem item) {
		DepositoryItemData itemData = userData.getItemData(item);
		ItemStackFactory factory = new ItemStackFactory(item.getDisplayItem());
		getExtraLore(player, itemData).forEach(factory::addLore);
		return factory.toItemStack();
	}

	public static List<String> getExtraLore(@NotNull Player player,
											@NotNull DepositoryItemData itemData) {
		DepositoryItem item = itemData.getSource();
		int canSell = item.getLimit() - itemData.getSold();

		List<String> lore = new ArrayList<>();
		if (canSell(item)) {
			lore.addAll(PluginConfig.General.AdditionalLore.AVAILABLE_FOR_SALE.get(player, new Object[]{
					item.getName(), itemData.getAmount(), item.getPrice(),
					itemData.getSold(), canSell, item.getLimit()
			}));
			lore.addAll(PluginConfig.General.ClickLore.AVAILABLE_FOR_SALE.get(player, new Object[]{
					item.getName(), itemData.getAmount(), item.getPrice()
			}));
		} else {
			lore.addAll(PluginConfig.General.AdditionalLore.NOT_FOR_SALE.get(player, new Object[]{
					item.getName(), itemData.getAmount()
			}));
			lore.addAll(PluginConfig.General.ClickLore.NOT_FOR_SALE.get(player, new Object[]{
					item.getName(), itemData.getAmount()
			}));
		}
		return lore;
	}

	public static boolean canSell(DepositoryItem item) {
		return UltraDepository.getEconomyManager().isInitialized()
				&& item.getLimit() > 0 && item.getPrice() > 0;
	}

	public static void open(@NotNull Player player, @NotNull Depository depository) {
		player.closeInventory();
		DepositoryGUI gui = new DepositoryGUI(player, depository);
		gui.openGUI(player);
	}

}
