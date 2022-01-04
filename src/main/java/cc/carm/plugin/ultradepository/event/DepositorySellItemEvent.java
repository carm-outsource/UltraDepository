package cc.carm.plugin.ultradepository.event;

import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DepositorySellItemEvent extends UltraDepositoryEvent {

	public static HandlerList handlers = new HandlerList();

	int beforeAmount;
	int afterAmount;
	double earnedMoney;

	public DepositorySellItemEvent(@NotNull Player player, @NotNull DepositoryItem depositoryItem,
								   int beforeAmount, int afterAmount, double earnedMoney) {
		super(player, depositoryItem.getDepository());
		this.beforeAmount = beforeAmount;
		this.afterAmount = afterAmount;
		this.earnedMoney = earnedMoney;
	}

	public int getBeforeAmount() {
		return beforeAmount;
	}

	public int getAfterAmount() {
		return afterAmount;
	}

	public double getEarnedMoney() {
		return earnedMoney;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
