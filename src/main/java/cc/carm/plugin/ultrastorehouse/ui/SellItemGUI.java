package cc.carm.plugin.ultrastorehouse.ui;

import cc.carm.plugin.ultrastorehouse.Main;
import cc.carm.plugin.ultrastorehouse.data.ItemData;
import cc.carm.plugin.ultrastorehouse.util.ItemStackFactory;
import cc.carm.plugin.ultrastorehouse.configuration.PluginConfig;
import cc.carm.plugin.ultrastorehouse.configuration.PluginMessages;
import cc.carm.plugin.ultrastorehouse.configuration.depository.ItemDepository;
import cc.carm.plugin.ultrastorehouse.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultrastorehouse.data.UserData;
import cc.carm.plugin.ultrastorehouse.util.gui.GUI;
import cc.carm.plugin.ultrastorehouse.util.gui.GUIItem;
import cc.carm.plugin.ultrastorehouse.util.gui.GUIType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static cc.carm.plugin.ultrastorehouse.configuration.PluginConfig.General.SellGUI.Items.*;

public class SellItemGUI extends GUI {

	final Player player;
	final UserData userData;
	final ItemData itemData;
	final ItemDepository configuration;
	final DepositoryItem item;

	ItemStack itemDisplay;

	int currentAmount;

	public SellItemGUI(Player player, UserData userData, ItemData itemData,
					   ItemDepository configuration, DepositoryItem item) {
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
		loadIcon();
		loadButtons();
	}

	private void loadIcon() {
		ItemStackFactory factory = new ItemStackFactory(this.itemDisplay);
		List<String> additionalLore = PluginConfig.General.ADDITIONAL_LORE.get(player, new Object[]{
				getItemName(), getRemainAmount(), getItemPrice(), getSoldAmount(), getSellLimit()
		});
		additionalLore.forEach(factory::addLore);
		setItem(4, new GUIItem(factory.toItemStack()));
	}

	private void loadButtons() {
		if (getCurrentAmount() > 1000) setItem(0, getRemoveItem(1000));
		if (getCurrentAmount() > 100) setItem(1, getRemoveItem(100));
		if (getCurrentAmount() > 10) setItem(2, getRemoveItem(10));
		if (getCurrentAmount() > 1) setItem(3, getRemoveItem(1));
		if (getAddableAmount() > 1) setItem(5, getAddItem(1));
		if (getAddableAmount() > 10) setItem(6, getAddItem(10));
		if (getAddableAmount() > 100) setItem(7, getAddItem(100));
		if (getAddableAmount() > 1000) setItem(8, getAddItem(1000));

		if (getCurrentAmount() >= 1) setItem(getConfirmItem(), 27, 28, 29, 30);
		setItem(getCancelItem(), 32, 33, 34, 35);
	}

	private GUIItem getAddItem(int amount) {
		ItemStackFactory factory = new ItemStackFactory(Add.TYPE.get());
		factory.setDurability(Add.DATA.get());
		factory.setDisplayName(Add.NAME.get(player, new Object[]{
				getItemName(), getCurrentAmount()
		}));
		factory.setLore(Add.LORE.get(player, new Object[]{
				getItemName(), getCurrentAmount()
		}));

		return new GUIItem(factory.toItemStack()) {
			@Override
			public void onClick(ClickType type) {
				load(getCurrentAmount() + amount);
				updateView();
			}
		};
	}

	private GUIItem getRemoveItem(int amount) {
		ItemStackFactory factory = new ItemStackFactory(Remove.TYPE.get());
		factory.setDurability(Remove.DATA.get());
		factory.setDisplayName(Remove.NAME.get(player, new Object[]{
				getItemName(), getCurrentAmount()
		}));
		factory.setLore(Remove.LORE.get(player, new Object[]{
				getItemName(), getCurrentAmount()
		}));
		return new GUIItem(factory.toItemStack()) {
			@Override
			public void onClick(ClickType type) {
				load(getCurrentAmount() - amount);
				updateView();
			}
		};
	}

	private GUIItem getConfirmItem() {
		ItemStackFactory factory = new ItemStackFactory(Confirm.TYPE.get());
		factory.setDurability(Confirm.DATA.get());
		factory.setDisplayName(Confirm.NAME.get(player, new Object[]{
				getItemName(), getCurrentAmount(), getTotalMoney()
		}));
		factory.setLore(Confirm.LORE.get(player, new Object[]{
				getItemName(), getCurrentAmount(), getTotalMoney()
		}));
		return new GUIItem(factory.toItemStack()) {
			@Override
			public void onClick(ClickType type) {
				int amount = Math.min(getCurrentAmount(), Math.min(getRemainAmount(), getSellLimit() - getSoldAmount()));
				if (amount > 0) {
					double money = Main.getEconomyManager().sell(player, getItemPrice(), amount);
					PluginMessages.SOLD.sendWithPlaceholders(player, new Object[]{
							getItemName(), amount, money
					});
				}
				player.closeInventory();
			}
		};
	}

	private GUIItem getCancelItem() {
		ItemStackFactory factory = new ItemStackFactory(Cancel.TYPE.get());
		factory.setDurability(Cancel.DATA.get());
		factory.setDisplayName(Cancel.NAME.get());
		factory.setLore(Cancel.LORE.get());
		return new GUIItem(factory.toItemStack()) {
			@Override
			public void onClick(ClickType type) {
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
		return getCurrentAmount() * getItemPrice();
	}

	private int getRemainAmount() {
		return this.itemData.getAmount();
	}

	private int getSoldAmount() {
		return this.itemData.getSold();
	}

	private int getAddableAmount() {
		return Math.min(getRemainAmount(), getSellLimit() - getSoldAmount()) - getCurrentAmount();
	}

	public static void open(Player player, UserData userData, ItemData itemData,
							ItemDepository configuration, DepositoryItem item) {
		if (!Main.getEconomyManager().isInitialized()) return;
		SellItemGUI gui = new SellItemGUI(player, userData, itemData, configuration, item);
		gui.openGUI(player);
	}

}
