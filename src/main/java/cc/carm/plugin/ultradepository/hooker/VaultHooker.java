package cc.carm.plugin.ultradepository.hooker;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHooker {

	private Economy econ = null;

	public static boolean hasVault() {
		return Bukkit.getServer().getPluginManager().getPlugin("Vault") != null;
	}

	public boolean setupEconomy() {

		if (!hasVault()) return false;
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) return false;

		this.econ = rsp.getProvider();
		return true;
	}

	public Economy getEconomy() {
		return econ;
	}

	public double getMoney(Player player) {
		if (player != null) {
			try {
				return getEconomy().getBalance(player);
			} catch (NullPointerException ignore) {
			}
		}
		return 0L;
	}

	public double getMoney(OfflinePlayer player) {
		if (player != null) {
			try {
				return getEconomy().getBalance(player);
			} catch (NullPointerException ignore) {
			}
		}
		return 0L;
	}

	public void removeMoney(Player player, double amount) {
		getEconomy().withdrawPlayer(player, amount);
	}

	public void removeMoney(OfflinePlayer player, double amount) {
		getEconomy().withdrawPlayer(player, amount);
	}

	public void addMoney(Player player, double amount) {
		getEconomy().depositPlayer(player, amount);
	}

	public void addMoney(OfflinePlayer player, double amount) {
		getEconomy().depositPlayer(player, amount);
	}

}