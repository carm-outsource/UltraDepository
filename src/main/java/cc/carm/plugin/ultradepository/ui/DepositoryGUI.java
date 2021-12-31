package cc.carm.plugin.ultradepository.ui;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.data.DepositoryItemData;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.util.ItemStackFactory;
import cc.carm.plugin.ultradepository.util.gui.GUI;
import cc.carm.plugin.ultradepository.util.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.IntStream;

public class DepositoryGUI extends GUI {

	Player player;
	UserData userData;
	Depository depository;

	public DepositoryGUI(Player player, Depository depository) {
		super(depository.getGUIConfiguration().getGUIType(), depository.getGUIConfiguration().getTitle());

		this.player = player;
		this.userData = Main.getUserManager().getData(player);
		this.depository = depository;

		setupItems();
	}

	public void setupItems() {
		depository.getGUIConfiguration().setupItems(player, this);
		depository.getItems().values().forEach(depositoryItem -> setItem(depositoryItem.getSlot(), createGUIItem(depositoryItem)));
	}


	private GUIItem createGUIItem(DepositoryItem item) {
		DepositoryItemData itemData = userData.getItemData(item);
		int remain = item.getLimit() - itemData.getSold();

		ItemStackFactory factory = new ItemStackFactory(item.getDisplayItem());
		List<String> additionalLore = PluginConfig.General.ADDITIONAL_LORE.get(player, new Object[]{
				item.getName(), itemData.getAmount(), item.getPrice(),
				itemData.getSold(), remain, item.getLimit()
		});

		additionalLore.forEach(factory::addLore);
		List<String> clickLore = PluginConfig.General.CLICK_LORE.get(player, new Object[]{
				item.getName(), itemData.getAmount(), item.getPrice()
		});
		clickLore.forEach(factory::addLore);

		return new GUIItem(factory.toItemStack()) {
			@Override
			public void onClick(ClickType type) {
				if (itemData.getAmount() < 1) {
					PluginMessages.NO_ENOUGH_ITEM.send(player);
					return;
				}

				if (type == ClickType.LEFT) {
					if (remain >= 1) {
						SellItemGUI.open(player, userData, itemData, depository, item);
					} else {
						PluginMessages.ITEM_SOLD_LIMIT.send(player, new Object[]{remain, item.getLimit()});
					}
				} else if (type == ClickType.RIGHT) {

					if (hasEmptySlot(player)) {
						int pickupAmount = Math.min(itemData.getAmount(), item.getMaterial().getMaxStackSize());
						userData.removeItemAmount(item.getDepository().getIdentifier(), item.getTypeID(), pickupAmount);
						player.getInventory().addItem(item.getRawItem(pickupAmount));
						PluginMessages.PICKUP.send(player, new Object[]{
								item.getName(), pickupAmount
						});
						setupItems(); //刷新GUI
						updateView();
					} else {
						PluginMessages.NO_SPACE.send(player);
						player.closeInventory();
					}
				}
			}
		};
	}

	private boolean hasEmptySlot(Player player) {
		return IntStream.range(0, 36)
				.mapToObj(i -> player.getInventory().getItem(i))
				.anyMatch(i -> i == null || i.getType() == Material.AIR);
	}

	public static void open(@NotNull Player player, @NotNull Depository depository) {
		player.closeInventory();
		DepositoryGUI gui = new DepositoryGUI(player, depository);
		gui.openGUI(player);
	}

}
