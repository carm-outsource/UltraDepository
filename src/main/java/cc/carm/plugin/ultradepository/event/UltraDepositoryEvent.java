package cc.carm.plugin.ultradepository.event;

import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.data.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class UltraDepositoryEvent extends Event {


	@NotNull Player player;
	@NotNull Depository depository;

	public UltraDepositoryEvent(@NotNull Player player, @NotNull Depository depository) {
		this.player = player;
		this.depository = depository;
	}

	public @NotNull UserData getUserData() {
		return UltraDepository.getUserManager().getData(getPlayer());
	}

	public @NotNull Player getPlayer() {
		return player;
	}

	public @NotNull Depository getDepository() {
		return depository;
	}

}
