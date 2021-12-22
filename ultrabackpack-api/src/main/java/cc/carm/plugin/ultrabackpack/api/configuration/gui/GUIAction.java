package cc.carm.plugin.ultrabackpack.api.configuration.gui;

import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GUIAction {

	enum ActionType {

		COMMAND,
		CONSOLE,
		MESSAGE,
		SOUND,
		CLOSE

	}

	@Nullable ClickType getClickType();

	@NotNull ActionType getActionType();

	@NotNull String getActionContent();

	void executeAction();

}
