package cc.carm.plugin.ultrabackpack.api.manager;

import cc.carm.plugin.ultrabackpack.api.data.UBUserData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface UBUserManager {

	@Nullable
	UBUserData getData(@NotNull UUID userUUID);

	@NotNull
	UBUserData getData(@NotNull Player player);

	@NotNull
	UBUserData loadData(@NotNull UUID userUUID);


}
