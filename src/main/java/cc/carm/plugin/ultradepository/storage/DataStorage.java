package cc.carm.plugin.ultradepository.storage;

import cc.carm.plugin.ultradepository.data.UserData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface DataStorage {


	boolean initialize();

	void shutdown();

	@Nullable
	UserData loadData(@NotNull UUID uuid) throws Exception;

	void saveUserData(@NotNull UserData data) throws Exception;

}
