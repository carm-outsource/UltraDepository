package cc.carm.plugin.ultradepository.manager;

import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;
import cc.carm.plugin.ultradepository.data.DepositoryData;
import cc.carm.plugin.ultradepository.data.DepositoryItemData;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.hooker.VaultHooker;
import org.bukkit.entity.Player;

public class EconomyManager {

	VaultHooker hooker;
	boolean initialized;

	public EconomyManager() {
		this.hooker = new VaultHooker();
	}

	public boolean initialize() {
		return initialized = this.hooker.setupEconomy();
	}

	public boolean isInitialized() {
		return initialized;
	}

	public VaultHooker getHooker() {
		return hooker;
	}

	public double sell(Player player, double price, int amount) {
		if (!isInitialized()) return 0D;
		double money = price * amount;
		getHooker().addMoney(player, money);
		return money;
	}

	public void sellAllItem(Player player, UserData userData) {
		sellAllItem(player, userData, true);
	}

	public void sellAllItem(Player player, UserData userData, boolean playSound) {
		userData.getDepositories().values().forEach(depositoryData -> sellAllItem(player, userData, depositoryData, false));
		if (playSound) PluginConfig.Sounds.SELL_SUCCESS.play(player);
	}


	public void sellAllItem(Player player, UserData userData, DepositoryData depositoryData) {
		sellAllItem(player, userData, depositoryData, true);
	}

	public void sellAllItem(Player player, UserData userData, DepositoryData depositoryData, boolean playSound) {
		depositoryData.getContents().values().forEach(value -> sellAllItem(player, userData, value, false));
		if (playSound) PluginConfig.Sounds.SELL_SUCCESS.play(player);
	}


	public void sellAllItem(Player player, UserData userData, DepositoryItemData itemData) {
		sellAllItem(player, userData, itemData, true);
	}

	public void sellAllItem(Player player, UserData userData, DepositoryItemData itemData, boolean playSound) {
		int amount = itemData.getAmount();
		int sold = itemData.getSold();
		int limit = itemData.getSource().getLimit();
		int finalAmount = Math.min(amount, (limit - sold));
		if (finalAmount > 0) sellItem(player, userData, itemData, finalAmount, false);
		if (playSound) PluginConfig.Sounds.SELL_SUCCESS.play(player);
	}

	public void sellItem(Player player, UserData userData, DepositoryItemData itemData, int amount) {
		sellItem(player, userData, itemData, amount, true);
	}

	public void sellItem(Player player, UserData userData, DepositoryItemData itemData, int amount, boolean playSound) {
		userData.addItemSold(itemData.getOwner().getSource().getIdentifier(), itemData.getSource().getTypeID(), amount);
		userData.removeItemAmount(itemData.getOwner().getSource().getIdentifier(), itemData.getSource().getTypeID(), amount);
		double money = sell(player, itemData.getSource().getPrice(), amount);
		PluginMessages.SOLD.send(player, new Object[]{itemData.getSource().getName(), amount, money});
		if (playSound) PluginConfig.Sounds.SELL_SUCCESS.play(player);
	}

}
