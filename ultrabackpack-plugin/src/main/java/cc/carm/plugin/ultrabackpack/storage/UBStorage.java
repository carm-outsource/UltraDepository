package cc.carm.plugin.ultrabackpack.storage;

import cc.carm.plugin.ultrabackpack.data.UserData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UBStorage {


	boolean initialize();

	void shutdown();

	@NotNull
	UserData loadData(@NotNull UUID uuid) throws Exception;

	void saveUserData(@NotNull UserData data) throws Exception;

}
