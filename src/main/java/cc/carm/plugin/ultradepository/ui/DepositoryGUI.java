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
		depository.getGUIConfiguration().setupItems(this);
		depository.getItems().values().forEach(depositoryItem -> setItem(depositoryItem.getSlot(), createGUIItem(depositoryItem)));
	}


	private GUIItem createGUIItem(DepositoryItem item) {
		ItemStackFactory factory = new ItemStackFactory(item.getDisplayItem());
		DepositoryItemData itemData = userData.getItemData(item);
		List<String> additionalLore = PluginConfig.General.ADDITIONAL_LORE.get(player, new Object[]{
				item.getName(), itemData.getAmount(), item.getPrice(), itemData.getSold(), item.getLimit()
		});
		additionalLore.forEach(factory::addLore);
		List<String> clickLore = PluginConfig.General.CLICK_LORE.get(player, new Object[]{
				item.getName(), itemData.getAmount(), item.getPrice()
		});
		clickLore.forEach(factory::addLore);

		return new GUIItem(factory.toItemStack()) {
			@Override
			public void onClick(ClickType type) {
				if (itemData.getAmount() < 1) return;
				if (type == ClickType.LEFT) {
					SellItemGUI.open(player, userData, itemData, depository, item);
				} else if (type == ClickType.RIGHT) {
					if (hasEmptySlot(player)) {
						int pickupAmount = Math.min(itemData.getAmount(), item.getMaterial().getMaxStackSize());
						player.getInventory().addItem(item.getRawItem(pickupAmount));
						PluginMessages.PICKUP.send(player, new Object[]{
								item.getName(), pickupAmount
						});
					} else {
						PluginMessages.NO_SPACE.send(player);
					}
					player.closeInventory();
				}
			}
		};
	}

	private boolean hasEmptySlot(Player player) {
		return IntStream.range(0, 36)
				.mapToObj(i -> player.getInventory().getItem(i))
				.anyMatch(i -> i == null || i.getType() == Material.AIR);
	}

	public static void open(Player player, Depository depository) {
		DepositoryGUI gui = new DepositoryGUI(player, depository);
		gui.openGUI(player);
	}

}
