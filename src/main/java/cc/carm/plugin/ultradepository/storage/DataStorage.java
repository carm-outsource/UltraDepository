package cc.carm.plugin.ultradepository.storage;

import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.manager.UserManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface DataStorage {

	/**
	 * 在插件加载存储源时执行。
	 *
	 * @return 是否初始化成功
	 */
	boolean initialize();

	/**
	 * 在插件被卸载时执行。
	 */
	void shutdown();

	/**
	 * 用于加载用户数据的方法。<bold>该方法将会被异步运行！</bold>
	 * <br>该方法一般无需自行执行，见 {@link UserManager#loadData(UUID)}
	 * <br>
	 * <br>若不存在该用户的数据，请返回 null 。
	 * <br>若加载出现任何错误，请抛出异常。
	 *
	 * @param uuid 用户UUID
	 * @throws Exception 当出现任何错误时抛出
	 */
	@Nullable
	UserData loadData(@NotNull UUID uuid) throws Exception;

	/**
	 * 用于保存用户数据的方法。 <bold>该方法将会被异步运行！</bold>
	 * <br>该方法一般无需自行执行，见 {@link UserManager#saveData(UserData)}
	 *
	 * @param data 用户数据
	 * @throws Exception 当出现任何错误时抛出
	 */
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
