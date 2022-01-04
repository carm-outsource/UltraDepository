package cc.carm.plugin.ultradepository.event;

import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DepositoryCollectItemEvent extends UltraDepositoryEvent implements Cancellable {

	public static HandlerList handlers = new HandlerList();

	boolean cancelled;

	private final DepositoryItem depositoryItem;
	int itemAmount;

	public DepositoryCollectItemEvent(@NotNull Player player, @NotNull Depository depository,
									  @NotNull DepositoryItem depositoryItem, int itemAmount) {
		super(player, depository);
		this.depositoryItem = depositoryItem;
		this.itemAmount = itemAmount;
	}


	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}

	public int getItemAmount() {
		return itemAmount;
	}

	public DepositoryItem getDepositoryItem() {
		return depositoryItem;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
