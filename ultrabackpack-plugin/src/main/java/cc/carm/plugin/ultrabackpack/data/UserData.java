package cc.carm.plugin.ultrabackpack.data;

import cc.carm.plugin.ultrabackpack.Main;
import cc.carm.plugin.ultrabackpack.api.data.UBContentsData;
import cc.carm.plugin.ultrabackpack.api.data.UBItemData;
import cc.carm.plugin.ultrabackpack.api.data.UBUserData;
import cc.carm.plugin.ultrabackpack.storage.UBStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserData implements UBUserData {

	public final UUID userUUID;

	UBStorage storage;
	Map<String, UBContentsData> backpacks;

	Date day;

	public UserData(UUID userUUID, UBStorage storage,
					Map<String, UBContentsData> backpacks, Date day) {
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
	public @Nullable UBContentsData getBackpack(String backpackID) {
		if (!Main.getBackpackManager().hasBackpack(backpackID)) return null;
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
	public @Nullable UBItemData getItemData(@NotNull String backpackID, @NotNull String typeID) {
		UBContentsData data = getBackpack(backpackID);
		if (data == null) return null;
		if (!Main.getBackpackManager().hasItem(backpackID, typeID)) return null;
		UBItemData itemData = data.getItemData(typeID);
		if (itemData == null) {
			itemData = UBItemData.emptyItemData();
			data.getContents().put(typeID, itemData);
		}
		return itemData;
	}


	@Override
	public @Nullable Integer getItemAmount(@NotNull String backpackID, @NotNull String typeID) {
		UBItemData data = getItemData(backpackID, typeID);
		if (data == null) return null;
		return data.getAmount();
	}

	@Override
	public @Nullable Integer getItemSold(@NotNull String backpackID, @NotNull String typeID) {
		checkoutDate();
		UBItemData data = getItemData(backpackID, typeID);
		if (data == null) return null;
		return data.getSold();
	}

	@Override
	public @Nullable Integer setItemAmount(@NotNull String backpackID, @NotNull String typeID, int amount) {
		UBItemData data = getItemData(backpackID, typeID);
		if (data == null) return null;
		data.setAmount(amount);
		return amount;
	}

	@Override
	public @Nullable Integer setItemSold(@NotNull String backpackID, @NotNull String typeID, int soldAmount) {
		UBItemData data = getItemData(backpackID, typeID);
		if (data == null) return null;
		data.setSold(soldAmount);
		return soldAmount;
	}

	@Override
	public @Nullable Integer addItemAmount(@NotNull String backpackID, @NotNull String typeID, int amount) {
		Integer current = getItemAmount(backpackID, typeID);
		if (current == null) return null;
		return setItemAmount(backpackID, typeID, current + amount);
	}

	@Override
	public @Nullable Integer addItemSold(@NotNull String backpackID, @NotNull String typeID, int amount) {
		Integer current = getItemSold(backpackID, typeID);
		if (current == null) return null;
		return setItemSold(backpackID, typeID, current + amount);
	}

	@Override
	public @Nullable Integer removeItemAmount(@NotNull String backpackID, @NotNull String typeID, int amount) {
		return addItemAmount(backpackID, typeID, -amount);
	}

	@Override
	public @Nullable Integer removeItemSold(@NotNull String backpackID, @NotNull String typeID, int amount) {
		return addItemSold(backpackID, typeID, -amount);
	}

	@Override
	public Date getDate() {
		return this.day;
	}

	@Override
	public boolean isCurrentDay() {
		return this.day.equals(new Date(System.currentTimeMillis()));
	}

	@Override
	public void checkoutDate() {
		if (isCurrentDay()) return;
		this.day = new Date(System.currentTimeMillis()); //更新日期
		getBackpacks().values().stream()
				.flatMap(value -> value.getContents().values().stream())
				.forEach(UBItemData::clearSold);
	}

	@Override
	public void save() throws Exception {
		this.storage.saveUserData(this);
	}

}
