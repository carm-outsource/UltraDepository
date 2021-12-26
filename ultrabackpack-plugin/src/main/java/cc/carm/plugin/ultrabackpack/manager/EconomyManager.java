package cc.carm.plugin.ultrabackpack.manager;

import cc.carm.plugin.ultrabackpack.hooker.VaultHooker;
import org.bukkit.entity.Player;

public class EconomyManager {

	VaultHooker hooker;
	boolean initialized;

	public EconomyManager() {
		this.hooker = new VaultHooker();
	}

	public boolean initialize() {
		boolean success = this.hooker.setupEconomy();
		this.initialized = success;

		return success;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public VaultHooker getHooker() {
		return hooker;
	}

	public void sell(Player player, double price, int amount) {
		if (!isInitialized()) return;
		getHooker().addMoney(player, price * amount);
	}

}
