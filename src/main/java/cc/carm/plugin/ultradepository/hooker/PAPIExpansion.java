package cc.carm.plugin.ultradepository.hooker;

import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.data.DepositoryItemData;
import cc.carm.plugin.ultradepository.data.UserData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PAPIExpansion extends PlaceholderExpansion {

	private static final List<String> PLACEHOLDERS = Arrays.asList(
			"%UltraDepository_amount_<BackpackID>_<ItemTypeID>%",
			"%UltraDepository_sold_<BackpackID>_<ItemTypeID>%",
			"%UltraDepository_price_<BackpackID>_<ItemTypeID>%",
			"%UltraDepository_remain_<BackpackID>_<ItemTypeID>%",
			"%UltraDepository_capacity_<BackpackID>%",
			"%UltraDepository_used_<BackpackID>%",
			"%UltraDepository_usable_<BackpackID>%"
	);

	private final JavaPlugin plugin;

	public PAPIExpansion(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public @NotNull List<String> getPlaceholders() {
		return PLACEHOLDERS;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public @NotNull String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public @NotNull String getIdentifier() {
		return plugin.getDescription().getName();
	}

	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, @NotNull String identifier) {
		if (player == null) return "加载中...";
		String[] args = identifier.split("_");

		if (args.length < 1) {
			return "Error Params";
		}

		UserData data = UltraDepository.getUserManager().getData(player);

		switch (args[0].toLowerCase()) {
			case "amount": {
				if (args.length < 3) return "Error Params";
				Integer amount = data.getItemAmount(args[1], args[2]);
				if (amount == null) return "Depository or Item not exists";
				else return amount.toString();
			}
			case "sold": {
				if (args.length < 3) return "Error Params";
				Integer sold = data.getItemSold(args[1], args[2]);
				if (sold == null) return "Depository or Item not exists";
				else return sold.toString();
			}
			case "remain": {
				if (args.length < 2) return "Error Params";
				Depository depository = UltraDepository.getDepositoryManager().getDepository(args[1]);
				if (depository == null) return "Depository not exists";
				DepositoryItem item = depository.getItems().get(args[2]);
				if (item == null) return "Depository Item not exists";
				int limit = item.getLimit();
				DepositoryItemData itemData = data.getItemData(item);
				return Integer.toString(limit - itemData.getSold());
			}
			case "limit": {
				if (args.length < 3) return "Error Params";
				Integer limit = UltraDepository.getDepositoryManager().getItemSellLimit(args[1], args[2]);
				if (limit == null) return "Depository or Item not exists";
				else return limit.toString();
			}
			case "price": {
				if (args.length < 3) return "Error Params";
				Double price = UltraDepository.getDepositoryManager().getItemPrice(args[1], args[2]);
				if (price == null) return "Depository or Item not exists";
				else return price.toString();
			}
			case "capacity": {
				if (args.length < 2) return "Error Params";
				Depository depository = UltraDepository.getDepositoryManager().getDepository(args[1]);
				if (depository == null) return "Depository not exists";
				int capacity = depository.getCapacity().getPlayerCapacity(player);
				return capacity < 0 ? "∞" : Integer.toString(capacity);
			}
			case "used": {
				if (args.length < 2) return "Error Params";
				Depository depository = UltraDepository.getDepositoryManager().getDepository(args[1]);
				if (depository == null) return "Depository not exists";
				return Integer.toString(data.getDepositoryData(depository).getUsedCapacity());
			}
			case "usable": {
				if (args.length < 2) return "Error Params";
				Depository depository = UltraDepository.getDepositoryManager().getDepository(args[1]);
				if (depository == null) return "Depository not exists";
				int max = depository.getCapacity().getPlayerCapacity(player);
				int used = data.getDepositoryData(depository).getUsedCapacity();
				return max < 0 ? "∞" : Integer.toString(max - used);
			}
			case "version": {
				return getVersion();
			}
			default: {
				return "参数错误";
			}
		}
	}

}
