package cc.carm.plugin.ultrabackpack.storage;

import cc.carm.plugin.ultrabackpack.api.data.UBUserData;
import cc.carm.plugin.ultrabackpack.api.storage.UBStorage;
import cc.carm.plugin.ultrabackpack.data.UserData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Date;
import java.util.HashMap;
import java.util.UUID;

public class FileStorage implements UBStorage {

	private File dataContainer;

	@Override
	public boolean initialize() {
		return false;
	}

	@Override
	public @NotNull UserData loadData(@NotNull UUID uuid) {
		return new UserData(uuid, this, new HashMap<>(), new Date(System.currentTimeMillis()));
	}

	@Override
	public void saveUserData(@NotNull UBUserData data) {

	}

}
