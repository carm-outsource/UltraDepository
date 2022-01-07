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

	/**
	 * Support old data which is forced to use data value.
	 * 老数据强制给typeID加上了data值(包括0)，然而1.13后每个物品都有对应的Material。
	 * 自插件v1.1.6版本开始不再强制，但需要为此额外做出支持，避免玩家数据丢失。
	 *
	 * @param typeID ID源数据
	 * @return 正确ID数据
	 * @since v1.1.6
	 */
	static String getFixedTypeID(String typeID) {
		String trueID = typeID;
		if (typeID.contains(":")) {
			try {
				String[] args = trueID.split(":");
				if (Integer.parseInt(args[1]) == 0) {

					trueID = args[0];
				}
			} catch (Exception ignore) {
			}
		}
		return trueID;
	}

}
