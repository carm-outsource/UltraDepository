package cc.carm.plugin.ultrabackpack.api.storage;

import cc.carm.plugin.ultrabackpack.api.data.UBUserData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UBStorage {


	boolean initialize();

	@NotNull
	UBUserData loadData(@NotNull UUID uuid) throws Exception;

	void saveUserData(@NotNull UBUserData data) throws Exception;

}
