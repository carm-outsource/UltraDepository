package cc.carm.plugin.ultradepository.command;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.data.DepositoryData;
import cc.carm.plugin.ultradepository.data.DepositoryItemData;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.ui.DepositoryGUI;
import cc.carm.plugin.ultradepository.util.ColorParser;
import cc.carm.plugin.ultradepository.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DepositoryCommand implements CommandExecutor, TabCompleter {


	private boolean helpConsole(CommandSender sender) {
		PluginMessages.HELP_CONSOLE.send(sender);
		return true;
	}

	private boolean helpPlayer(Player player) {
		PluginMessages.HELP_PLAYER.send(player);
		return true;
	}

	@Override
	public boolean onCommand(
			@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String alias, @NotNull String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length < 1) return helpPlayer(player);
			switch (args[0].toLowerCase()) {
				case "open": {
					if (!player.hasPermission("UltraDepository.use")) {
						return false;
					}
					if (args.length < 2) return helpPlayer(player);
					Depository depository = Main.getDepositoryManager().getDepository(args[1]);
					if (depository == null) {
						PluginMessages.NO_DEPOSITORY.send(player);
						return true;
					}
					DepositoryGUI.open((Player) sender, depository);
					return true;
				}
				case "sell": {
					if (!player.hasPermission("UltraDepository.Command.Sell")) {
						return false;
					}
					if (!Main.getEconomyManager().isInitialized()) {
						PluginConfig.Sounds.SELL_FAIL.play(player);
						PluginMessages.NO_ECONOMY.send(player);
						return true;
					}
					if (args.length < 4) return helpPlayer(player);
					Depository depository = Main.getDepositoryManager().getDepository(args[1]);
					if (depository == null) {
						PluginConfig.Sounds.SELL_FAIL.play(player);
						PluginMessages.NO_DEPOSITORY.send(player);
						return true;
					}

					DepositoryItem item = depository.getItems().get(args[2]);
					if (item == null) {
						PluginConfig.Sounds.SELL_FAIL.play(player);
						PluginMessages.NO_ITEM.send(player);
						return true;
					}

					int amount = -1;
					try {
						amount = Integer.parseInt(args[3]);
					} catch (Exception ignore) {
					}
					if (amount <= 0) {
						PluginConfig.Sounds.SELL_FAIL.play(player);
						PluginMessages.WRONG_NUMBER.send(player);
						return true;
					}

					UserData userData = Main.getUserManager().getData(player);
					DepositoryItemData itemData = userData.getItemData(item);
					int limit = item.getLimit();
					int sold = itemData.getSold();
					int currentAmount = itemData.getAmount();

					if (currentAmount < amount) {
						PluginConfig.Sounds.SELL_FAIL.play(player);
						PluginMessages.NO_ENOUGH_ITEM.send(player);
						return true;
					}

					if (currentAmount > (limit - sold)) {
						PluginConfig.Sounds.SELL_FAIL.play(player);
						PluginMessages.ITEM_SOLD_LIMIT.send(player, new Object[]{(limit - sold), limit});
						return true;
					}

					Main.getEconomyManager().sellItem(player, userData, itemData, amount);
					return true;
				}
				case "sellall": {
					if (!player.hasPermission("UltraDepository.Command.SellAll")) {
						return false;
					}
					if (!Main.getEconomyManager().isInitialized()) {
						PluginConfig.Sounds.SELL_FAIL.play(player);
						PluginMessages.NO_ECONOMY.send(player);
						return true;
					}
					UserData userData = Main.getUserManager().getData(player);

					String depositoryID = args.length >= 2 ? args[1] : null;
					String itemID = args.length >= 3 ? args[2] : null;

					Depository depository = null;
					if (depositoryID != null) {
						depository = Main.getDepositoryManager().getDepository(depositoryID);
						if (depository == null) {
							PluginConfig.Sounds.SELL_FAIL.play(player);
							PluginMessages.NO_DEPOSITORY.send(player);
							return true;
						}
					}

					if (depository == null) {
						Main.getEconomyManager().sellAllItem(player, userData);
						sender.sendMessage("Success! " + player.getName() + "'s items had been sold.");
						return true;
					}

					DepositoryItem item = null;
					if (itemID != null) {
						item = depository.getItems().get(itemID);
						if (item == null) {
							PluginConfig.Sounds.SELL_FAIL.play(player);
							PluginMessages.NO_ITEM.send(player);
							return true;
						}
					}

					if (item == null) {
						Main.getEconomyManager().sellAllItem(player, userData, userData.getDepositoryData(depositoryID));
						return true;
					}

					Main.getEconomyManager().sellAllItem(player, userData, userData.getItemData(item));
					return true;
				}
				default:
					return helpPlayer(player);
			}
		} else {
			if (args.length < 1) return helpConsole(sender);
			switch (args[0].toLowerCase()) {
				case "info": {
					if (args.length < 2) return helpConsole(sender);
					Player player = Bukkit.getPlayer(args[1]);
					if (player == null) {
						sender.sendMessage("Player does not exist.");
						return false;
					}
					UserData userData = Main.getUserManager().getData(player);

					String depositoryID = args.length >= 3 ? args[2] : null;
					String itemID = args.length >= 4 ? args[3] : null;

					Depository depository = null;
					if (depositoryID != null) {
						depository = Main.getDepositoryManager().getDepository(depositoryID);
						if (depository == null) {
							PluginMessages.NO_DEPOSITORY.send(player);
							return true;
						}
					}
					sender.sendMessage(ColorParser.parse("&fInfo &6" + player.getName() + " &f:"));
					if (depository == null) {
						userData.getDepositories().values().forEach(depositoryData -> {
							MessageUtil.send(sender, "&8# &e" + depositoryData.getIdentifier());
							depositoryData.getContents().values().forEach(itemData -> {
								String typeID = itemData.getSource().getTypeID();
								int amount = itemData.getAmount();
								int sold = itemData.getSold();
								MessageUtil.send(sender, "&8- &f" + typeID + " &7[&f " + amount + "&8|&f " + sold + "&7]");
							});
						});
						return true;
					}

					DepositoryItem item = null;
					if (itemID != null) {
						item = depository.getItems().get(itemID);
						if (item == null) {
							PluginMessages.NO_ITEM.send(player);
							return true;
						}
					}

					if (item == null) {
						DepositoryData depositoryData = userData.getDepositoryData(depository);
						MessageUtil.send(sender, "&8# &e" + depositoryData.getIdentifier());
						depositoryData.getContents().values().forEach(itemData -> {
							String typeID = itemData.getSource().getTypeID();
							int amount = itemData.getAmount();
							int sold = itemData.getSold();
							MessageUtil.send(sender, "&8- &f" + typeID + " &7[&f " + amount + "&8|&f " + sold + "&7]");
						});
						return true;
					}

					DepositoryItemData itemData = userData.getItemData(item);
					String typeID = itemData.getSource().getTypeID();
					int amount = itemData.getAmount();
					int sold = itemData.getSold();

					MessageUtil.send(sender, "&8# &e" + depository.getIdentifier());
					MessageUtil.send(sender, "&8- &f" + typeID + " &7[&f " + amount + "&8|&f " + sold + "&7]");
					return true;
				}
				case "add": {
					if (args.length < 5) return true;
					Player player = Bukkit.getPlayer(args[1]);
					if (player == null) {
						sender.sendMessage("Player does not exist.");
						return false;
					}

					Depository depository = Main.getDepositoryManager().getDepository(args[2]);
					if (depository == null) {
						PluginMessages.NO_DEPOSITORY.send(sender);
						return true;
					}

					DepositoryItem item = depository.getItems().get(args[3]);
					if (item == null) {
						PluginMessages.NO_ITEM.send(sender);
						return true;
					}

					int amount = -1;
					try {
						amount = Integer.parseInt(args[4]);
					} catch (Exception ignore) {
					}
					if (amount <= 0) {
						PluginMessages.WRONG_NUMBER.send(sender);
						return true;
					}

					Integer after = Main.getUserManager().getData(player)
							.addItemAmount(depository.getIdentifier(), item.getTypeID(), amount);

					if (after != null) {
						sender.sendMessage("Success! Now " + player.getName() + "'s " + item.getTypeID() + " is " + after + " .");
					} else {
						sender.sendMessage("Failed!");
					}

					return true;
				}
				case "remove": {
					if (args.length < 4) return true;
					Player player = Bukkit.getPlayer(args[1]);
					if (player == null) {
						sender.sendMessage("Player does not exist.");
						return false;
					}

					Depository depository = Main.getDepositoryManager().getDepository(args[2]);
					if (depository == null) {
						PluginMessages.NO_DEPOSITORY.send(sender);
						return true;
					}

					DepositoryItem item = depository.getItems().get(args[3]);
					if (item == null) {
						PluginMessages.NO_ITEM.send(sender);
						return true;
					}

					Integer amount = null;
					try {
						amount = Integer.parseInt(args[4]);
					} catch (Exception ignore) {
					}
					if (amount != null && amount < 0) {
						PluginMessages.WRONG_NUMBER.send(sender);
						return true;
					}

					UserData userData = Main.getUserManager().getData(player);
					if (amount != null) {
						Integer after = userData.removeItemAmount(depository.getIdentifier(), item.getTypeID(), amount);
						if (after != null) {
							sender.sendMessage("Success! Now " + player.getName() + "'s " + item.getTypeID() + " is " + after + " .");
						} else {
							sender.sendMessage("Failed!");
						}
					} else {
						userData.setItemAmount(depository.getIdentifier(), item.getTypeID(), 0);
						sender.sendMessage("Success! Cleared " + player.getName() + "'s " + item.getTypeID() + " .");
					}
					return true;
				}
				case "sell": {
					if (args.length < 2) return true;
					Player player = Bukkit.getPlayer(args[1]);
					if (player == null) {
						sender.sendMessage("Player does not exist.");
						return false;
					}
					String depositoryID = args.length >= 3 ? args[2] : null;
					String itemID = args.length >= 4 ? args[3] : null;

					Depository depository = null;
					if (depositoryID != null) {
						depository = Main.getDepositoryManager().getDepository(depositoryID);
						if (depository == null) {
							PluginMessages.NO_DEPOSITORY.send(sender);
							return true;
						}
					}

					UserData userData = Main.getUserManager().getData(player);

					if (depository == null) {
						Main.getEconomyManager().sellAllItem(player, userData);
						sender.sendMessage("Success! " + player.getName() + "'s items had been sold.");
						return true;
					}

					DepositoryItem item = null;
					if (itemID != null) {
						item = depository.getItems().get(itemID);
						if (item == null) {
							PluginMessages.NO_ITEM.send(player);
							return true;
						}
					}
					if (item == null) {
						Main.getEconomyManager().sellAllItem(player, userData, userData.getDepositoryData(depository));
						sender.sendMessage("Success! " + player.getName() + "'s " + depository.getIdentifier() + " had been sold.");
						return true;
					}

					Integer amount = null;
					if (args.length >= 5) {
						try {
							amount = Integer.parseInt(args[4]);
						} catch (Exception ignore) {
						}
					}

					if (amount != null && amount < 0) {
						PluginMessages.WRONG_NUMBER.send(sender);
						return true;
					}

					if (amount == null) {
						Main.getEconomyManager().sellAllItem(player, userData, userData.getItemData(item));
						sender.sendMessage("Success! " + player.getName() + "'s " + item.getTypeID() + " had been sold.");
						return true;
					}

					DepositoryItemData itemData = userData.getItemData(item);

					int limit = item.getLimit();
					int sold = itemData.getSold();
					int currentAmount = itemData.getAmount();

					if (currentAmount < amount) {
						PluginMessages.NO_ENOUGH_ITEM.send(sender);
						return true;
					}

					if (currentAmount > (limit - sold)) {
						PluginMessages.ITEM_SOLD_LIMIT.send(sender, new Object[]{(limit - sold), limit});
						return true;
					}

					Main.getEconomyManager().sellItem(player, userData, userData.getItemData(item), amount);
					sender.sendMessage("Success! " + player.getName() + "'s " + amount + " " + item.getTypeID() + " had been sold.");
					return true;
				}
				default:
					return helpConsole(sender);
			}
		}
	}


	@Nullable
	@Override
	public List<String> onTabComplete(
			@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String alias, @NotNull String[] args) {
		List<String> allCompletes = new ArrayList<>();
		if (sender instanceof Player) {
			// 玩家指令部分
			Player player = (Player) sender;
			if (player.hasPermission("UltraDepository.use")) {
				switch (args.length) {
					case 1: {
						allCompletes.add("open");
						if (player.hasPermission("UltraDepository.Command.Sell")) allCompletes.add("sell");
						if (player.hasPermission("UltraDepository.Command.SellAll")) allCompletes.add("sellAll");
						break;
					}
					case 2: {
						String aim = args[0];
						if (aim.equalsIgnoreCase("open")
								|| (aim.equalsIgnoreCase("sell")
								&& player.hasPermission("UltraDepository.Command.Sell"))
								|| (aim.equalsIgnoreCase("sellAll")
								&& player.hasPermission("UltraDepository.Command.SellAll"))) {
							allCompletes.addAll(Main.getDepositoryManager().getDepositories().keySet());
						}
						break;
					}
					case 3: {
						String aim = args[0];
						String depositoryID = args[1];
						if ((aim.equalsIgnoreCase("sell")
								&& player.hasPermission("UltraDepository.Command.Sell"))
								|| (aim.equalsIgnoreCase("sellAll")
								&& player.hasPermission("UltraDepository.Command.SellAll"))) {
							Depository depository = Main.getDepositoryManager().getDepository(depositoryID);
							if (depository != null) {
								allCompletes.addAll(depository.getItems().keySet());
							}
						}
						break;
					}
				}
			}
		} else {
			//后台指令部分
			switch (args.length) {
				case 1: {
					allCompletes.add("info");
					allCompletes.add("add");
					allCompletes.add("remove");
					allCompletes.add("sell");
					break;
				}
				case 2: {
					allCompletes = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
					break;
				}
				case 3: {
					allCompletes.addAll(Main.getDepositoryManager().getDepositories().keySet());
					break;
				}
				case 4: {
					Depository depository = Main.getDepositoryManager().getDepository(args[2]);
					if (depository != null) {
						allCompletes.addAll(depository.getItems().keySet());
					}
					break;
				}
			}
		}

		return allCompletes.stream()
				.filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
				.limit(10).collect(Collectors.toList());
	}
}
