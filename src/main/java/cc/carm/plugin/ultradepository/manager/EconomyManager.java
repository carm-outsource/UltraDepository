package cc.carm.plugin.ultradepository.manager;

import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.data.DepositoryData;
import cc.carm.plugin.ultradepository.data.DepositoryItemData;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.event.DepositorySellItemEvent;
import cc.carm.plugin.ultradepository.hooker.VaultHooker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
		BigDecimal money = BigDecimal.valueOf(price * amount).setScale(2, RoundingMode.DOWN);
		getHooker().addMoney(player, money.doubleValue());
		return money.doubleValue();
	}

	public void sellAllItem(Player player, UserData userData) {
		sellAllItem(player, userData, true);
	}

	public void sellAllItem(Player player, UserData userData, boolean playSound) {
		userData.getDepositories().values().stream().map(DepositoryData::getSource)
				.forEach(depositoryData -> sellAllItem(player, userData, depositoryData, false));
		if (playSound) PluginConfig.Sounds.SELL_SUCCESS.play(player);
	}


	public void sellAllItem(Player player, UserData userData, Depository depository) {
		sellAllItem(player, userData, depository, true);
	}

	public void sellAllItem(Player player, UserData userData, Depository depository, boolean playSound) {
		depository.getItems().values().forEach(value -> sellAllItem(player, userData, value, false));
		if (playSound) PluginConfig.Sounds.SELL_SUCCESS.play(player);
	}


	public void sellAllItem(Player player, UserData userData, DepositoryItem depositoryItem) {
		sellAllItem(player, userData, depositoryItem, true);
	}

	public void sellAllItem(Player player, UserData userData, DepositoryItem depositoryItem, boolean playSound) {
		DepositoryItemData itemData = userData.getItemData(depositoryItem);
		int amount = itemData.getAmount();
		int sold = itemData.getSold();
		int limit = depositoryItem.getLimit();
		int finalAmount = Math.min(amount, (limit - sold));
		if (finalAmount > 0) sellItem(player, userData, depositoryItem, finalAmount, false);
		if (playSound) PluginConfig.Sounds.SELL_SUCCESS.play(player);
	}

	public void sellItem(Player player, UserData userData, DepositoryItem depositoryItem, int amount) {
		sellItem(player, userData, depositoryItem, amount, true);
	}

	public void sellItem(Player player, UserData userData, DepositoryItem depositoryItem, int amount, boolean playSound) {
		int[] changes = userData.getItemData(depositoryItem).applyChanges(-amount, amount);
		double money = sell(player, depositoryItem.getPrice(), amount);
		Bukkit.getPluginManager().callEvent(new DepositorySellItemEvent(
				player, depositoryItem, changes[0] + amount, changes[0], money
		));

		PluginMessages.SOLD.send(player, new Object[]{depositoryItem.getName(), amount, money});
		if (playSound) PluginConfig.Sounds.SELL_SUCCESS.play(player);
	}

}
