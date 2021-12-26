package cc.carm.plugin.ultrabackpack.hooker;

import cc.carm.plugin.ultrabackpack.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class UBExpansion extends PlaceholderExpansion {

	private static final List<String> PLACEHOLDERS = Arrays.asList(
			"%UltraBackpack_amount_<BackpackID>_<ItemTypeID>%",
			"%UltraBackpack_sold_<BackpackID>_<ItemTypeID>%",
			"%UltraBackpack_price_<BackpackID>_<ItemTypeID>%",
			"%UltraBackpack_remain_<BackpackID>_<ItemTypeID>%",
			"%UltraBackpack_capacity_<BackpackID>%"
	);

	Main main;

	public UBExpansion(Main main) {
		this.main = main;
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
		return main.getDescription().getAuthors().toString();
	}

	@Override
	public @NotNull String getIdentifier() {
		return main.getDescription().getName();
	}

	@Override
	public @NotNull String getVersion() {
		return main.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, @NotNull String identifier) {
		if (player == null) return "加载中...";
		String[] args = identifier.split("_");

		if (args.length < 1) {
			return "参数不足";
		}

		switch (args[0].toLowerCase()) {
			case "amount": {
				if (args.length < 3) return "参数不足";
				Integer amount = Main.getUserManager().getData(player).getItemAmount(args[2], args[3]);
				if (amount == null) return "背包或物品不存在";
				else return amount.toString();
			}
			case "sold": {
				if (args.length < 3) return "参数不足";
				Integer sold = Main.getUserManager().getData(player).getItemSold(args[2], args[3]);
				if (sold == null) return "背包或物品不存在";
				else return sold.toString();
			}
			case "remain": {
				if (args.length < 3) return "参数不足";

				Integer sold = Main.getUserManager().getData(player).getItemSold(args[2], args[3]);
				if (sold == null) return "背包或物品不存在";

				Integer limit = Main.getBackpackManager().getItemSellLimit(args[2], args[3]);
				if (limit == null) return "背包或物品不存在";

				return Integer.toString(limit - sold);
			}
			case "limit": {
				if (args.length < 3) return "参数不足";
				Integer limit = Main.getBackpackManager().getItemSellLimit(args[2], args[3]);
				if (limit == null) return "背包或物品不存在";
				else return limit.toString();
			}
			case "price": {
				if (args.length < 3) return "参数不足";
				Double price = Main.getBackpackManager().getItemPrice(args[2], args[3]);
				if (price == null) return "背包或物品不存在";
				else return price.toString();
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
