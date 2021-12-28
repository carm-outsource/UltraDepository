package cc.carm.plugin.ultradepository.storage;

import cc.carm.plugin.ultradepository.data.UserData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DataStorage {


	boolean initialize();

	void shutdown();

	@NotNull
	UserData loadData(@NotNull UUID uuid) throws Exception;

	void saveUserData(@NotNull UserData data) throws Exception;

}
