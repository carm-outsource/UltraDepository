package cc.carm.plugin.ultradepository.command;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.ui.DepositoryGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DepositoryCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(
			@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String alias, @NotNull String[] args) {

		if (sender instanceof Player && args.length >= 1) {
			Depository depository = Main.getDepositoryManager().getDepository(args[0]);
			if (depository != null) DepositoryGUI.open((Player) sender, depository);
		}

		return false;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(
			@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String alias, @NotNull String[] args) {


		return null;
	}
}
