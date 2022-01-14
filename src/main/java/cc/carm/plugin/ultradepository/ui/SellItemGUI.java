package cc.carm.plugin.ultradepository.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ItemStackFactory;
import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.data.DepositoryItemData;
import cc.carm.plugin.ultradepository.data.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static cc.carm.plugin.ultradepository.configuration.PluginConfig.General.SellGUI.Items.*;

public class SellItemGUI extends GUI {

	final Player player;
	final UserData userData;
	final DepositoryItemData itemData;
	final Depository configuration;
	final DepositoryItem item;

	ItemStack itemDisplay;

	int currentAmount;

	public SellItemGUI(Player player, UserData userData, DepositoryItemData itemData,
					   Depository configuration, DepositoryItem item) {

		super(GUIType.FOUR_BY_NINE, PluginConfig.General.SellGUI.TITLE.get(player, new String[]{
				configuration.getName(), item.getName()
		}));

		this.player = player;
		this.userData = userData;
		this.itemData = itemData;
		this.configuration = configuration;
		this.item = item;
		this.itemDisplay = item.getDisplayItem();

		load(1);
	}

	private void load(int amount) {
		this.currentAmount = Math.max(1, amount); // 不可小于1
		ItemStackFactory factory = new ItemStackFactory(this.itemDisplay);
		List<String> additionalLore = PluginConfig.General.AdditionalLore.AVAILABLE_FOR_SALE
				.get(player, new Object[]{
						getItemName(), getDepositoryAmount(), getItemPrice(),
						getSoldAmount(), (getSellLimit() - getSoldAmount()), getSellLimit()
				});
		additionalLore.forEach(factory::addLore);

		setItem(9, getCurrentAmount() > 1000 ? getRemoveItem(1000) : null);
		setItem(10, getCurrentAmount() > 100 ? getRemoveItem(100) : null);
		setItem(11, getCurrentAmount() > 10 ? getRemoveItem(10) : null);
		setItem(12, getCurrentAmount() > 1 ? getRemoveItem(1) : null);
		setItem(13, new GUIItem(factory.toItemStack()));
		setItem(14, getAddableAmount() >= 1 ? getAddItem(1) : null);
		setItem(15, getAddableAmount() >= 10 ? getAddItem(10) : null);
		setItem(16, getAddableAmount() >= 100 ? getAddItem(100) : null);
		setItem(17, getAddableAmount() >= 1000 ? getAddItem(1000) : null);

		setItem(getCurrentAmount() >= 1 ? getConfirmItem() : null, 27, 28, 29, 30);
		setItem(getCancelItem(), 32, 33, 34, 35);

	}

	private GUIItem getAddItem(int amount) {
		return new GUIItem(ADD.getItem(player, new Object[]{
				getItemName(), amount
		})) {
			@Override
			public void onClick(ClickType type) {
				PluginConfig.Sounds.GUI_CLICK.play(player);
				load(getCurrentAmount() + amount);
				updateView();
			}
		};
	}

	private GUIItem getRemoveItem(int amount) {
		return new GUIItem(REMOVE.getItem(player, new Object[]{
				getItemName(), amount
		})) {
			@Override
			public void onClick(ClickType type) {
				PluginConfig.Sounds.GUI_CLICK.play(player);
				load(getCurrentAmount() - amount);
				updateView();
			}
		};
	}

	private GUIItem getConfirmItem() {
		return new GUIItem(CONFIRM.getItem(player, new Object[]{
				getItemName(), getCurrentAmount(), getTotalMoney()
		})) {
			@Override
			public void onClick(ClickType type) {
				int amount = Math.min(getCurrentAmount(), Math.min(getDepositoryAmount(), getSellLimit() - getSoldAmount()));
				if (amount > 0) UltraDepository.getEconomyManager().sellItem(player, userData, item, amount);
				player.closeInventory();
			}
		};
	}

	private GUIItem getCancelItem() {
		return new GUIItem(CANCEL.getItem(player)) {
			@Override
			public void onClick(ClickType type) {
				PluginConfig.Sounds.SELL_FAIL.play(player);
				player.closeInventory();
			}
		};
	}

	private String getItemName() {
		return this.item.getName();
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	private double getItemPrice() {
		return this.item.getPrice();
	}

	private int getSellLimit() {
		return this.item.getLimit();
	}

	private double getTotalMoney() {
		BigDecimal money = BigDecimal.valueOf(getCurrentAmount() * getItemPrice()).setScale(2, RoundingMode.DOWN);
		return money.doubleValue();
	}

	private int getDepositoryAmount() {
		return userData.getItemData(this.item).getAmount();
	}

	private int getSoldAmount() {
		return userData.getItemData(this.item).getSold();
	}

	private int getAddableAmount() {
		return Math.min(getDepositoryAmount(), getSellLimit() - getSoldAmount()) - getCurrentAmount();
	}

	public static void open(Player player, UserData userData, DepositoryItemData itemData,
							Depository configuration, DepositoryItem item) {
		player.closeInventory();
		if (!UltraDepository.getEconomyManager().isInitialized()) {
			PluginMessages.NO_ECONOMY.send(player);
			return;
		}

		SellItemGUI gui = new SellItemGUI(player, userData, itemData, configuration, item);
		gui.openGUI(player);
	}

}
