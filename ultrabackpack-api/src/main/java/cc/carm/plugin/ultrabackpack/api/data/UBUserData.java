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

	int getItemAmount(String backpackID, String typeID);

	int getItemSold(String backpackID, String typeID);

	Date getDataDay();

	boolean isCurrentDay();

	void updateDate();

	void save() throws Exception;

}
