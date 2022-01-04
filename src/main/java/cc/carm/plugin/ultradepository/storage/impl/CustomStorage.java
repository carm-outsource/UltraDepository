package cc.carm.plugin.ultradepository.storage.impl;

import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.storage.DataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.UUID;

public class CustomStorage implements DataStorage {

	@Override
	@TestOnly
	public boolean initialize() {
		return false;
	}

	@Override
	@TestOnly
	public void shutdown() {

	}

	@Override
	@TestOnly
	public @Nullable UserData loadData(@NotNull UUID uuid) {
		return null;
	}

	@Override
	@TestOnly
	public void saveUserData(@NotNull UserData data) {

	}
	
}
