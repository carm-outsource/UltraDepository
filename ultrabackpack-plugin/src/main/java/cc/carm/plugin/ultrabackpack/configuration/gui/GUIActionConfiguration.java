package cc.carm.plugin.ultrabackpack.configuration.gui;

import cc.carm.plugin.ultrabackpack.util.gui.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GUIActionConfiguration {


	@Nullable ClickType clickType;
	final @NotNull GUIActionType actionType;
	final @Nullable String actionContent;

	public GUIActionConfiguration(@Nullable ClickType clickType, @NotNull GUIActionType actionType, @Nullable String actionContent) {
		this.clickType = clickType;
		this.actionType = actionType;
		this.actionContent = actionContent;
	}

	public @Nullable ClickType getClickType() {
		return clickType;
	}

	public @NotNull GUIActionType getActionType() {
		return actionType;
	}

	public @Nullable String getActionContent() {
		return actionContent;
	}

	public void checkAction(Player player, ClickType type) {
		if (getClickType() == null || getClickType() == type) executeAction(player);
	}

	public void executeAction(Player targetPlayer) {
		getActionType().getExecutor().accept(targetPlayer, getActionContent());
	}

	public GUIItem.GUIClickAction toClickAction() {
		return new GUIItem.GUIClickAction() {
			@Override
			public void run(ClickType type, Player player) {
				checkAction(player, type);
			}
		};
	}

}
