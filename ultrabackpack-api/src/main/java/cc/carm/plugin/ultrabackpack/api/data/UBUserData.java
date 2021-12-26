package cc.carm.plugin.ultrabackpack.api.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.Set;
import java.util.UUID;

public interface UBUserData {


	@NotNull UUID getUserUUID();

	@Nullable UBContentsData getBackpack(String backpackID);

	@NotNull Set<String> getBackpackIDs();

	@Nullable UBItemData getItemData(@NotNull String backpackID, @NotNull String typeID);

	@Nullable Integer getItemAmount(@NotNull String backpackID, @NotNull String typeID);

	@Nullable Integer getItemSold(@NotNull String backpackID, @NotNull String typeID);

	@Nullable Integer setItemAmount(@NotNull String backpackID, @NotNull String typeID, int amount);

	@Nullable Integer setItemSold(@NotNull String backpackID, @NotNull String typeID, int amount);

	@Nullable Integer addItemAmount(@NotNull String backpackID, @NotNull String typeID, int amount);

	@Nullable Integer addItemSold(@NotNull String backpackID, @NotNull String typeID, int amount);

	@Nullable Integer removeItemAmount(@NotNull String backpackID, @NotNull String typeID, int amount);

	@Nullable Integer removeItemSold(@NotNull String backpackID, @NotNull String typeID, int amount);

	Date getDate();

	boolean isCurrentDay();

	void checkoutDate();

	void save() throws Exception;

}
