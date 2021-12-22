package cc.carm.plugin.ultrabackpack.data;

import cc.carm.plugin.ultrabackpack.api.data.UBContentsData;
import cc.carm.plugin.ultrabackpack.api.data.UBUserData;
import cc.carm.plugin.ultrabackpack.api.storage.UBStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserData implements UBUserData {

	public final UUID userUUID;

	UBStorage storage;
	Map<String, UBContentsData> backpacks;

	Date day;

	public UserData(UUID userUUID, UBStorage storage, Map<String, UBContentsData> backpacks, Date day) {
		this.userUUID = userUUID;
		this.storage = storage;
		this.backpacks = backpacks;
		this.day = day;
	}

	@Override
	public @NotNull UUID getUserUUID() {
		return this.userUUID;
	}

	public @NotNull Map<String, UBContentsData> getBackpacks() {
		return this.backpacks;
	}

	@Override
	public @NotNull UBContentsData getBackpack(String backpackID) {
		if (!getBackpacks().containsKey(backpackID)) {
			getBackpacks().put(backpackID, UBContentsData.emptyContents());
		}
		return getBackpacks().get(backpackID);
	}

	@Override
	public @NotNull Set<String> getBackpackIDs() {
		return getBackpacks().keySet();
	}

	@Override
	public int getItemAmount(String backpackID, String typeID) {
		return getBackpack(backpackID).getItemData(typeID).getAmount();
	}

	@Override
	public int getItemSold(String backpackID, String typeID) {
		return getBackpack(backpackID).getItemData(typeID).getSold();
	}

	@Override
	public Date getDataDay() {
		return this.day;
	}

	@Override
	public boolean isCurrentDay() {
		return this.day.equals(new Date(System.currentTimeMillis()));
	}

	@Override
	public void updateDate() {

	}

	@Override
	public void save() throws Exception {
		this.storage.saveUserData(this);
	}

}
