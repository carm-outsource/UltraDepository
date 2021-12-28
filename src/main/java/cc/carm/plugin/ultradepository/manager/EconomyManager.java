package cc.carm.plugin.ultradepository.manager;

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

}
