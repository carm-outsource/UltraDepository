package cc.carm.plugin.ultrastorehouse.data;

import cc.carm.plugin.ultrastorehouse.Main;
import cc.carm.plugin.ultrastorehouse.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultrastorehouse.configuration.depository.ItemDepository;
import cc.carm.plugin.ultrastorehouse.storage.DataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserData {

	public final UUID userUUID;

	DataStorage storage;
	Map<String, DepositoryData> backpacks;

	Date day;

	public UserData(UUID userUUID, DataStorage storage,
					Map<String, DepositoryData> backpacks, Date day) {
		this.userUUID = userUUID;
		this.storage = storage;
		this.backpacks = backpacks;
		this.day = day;
	}


	public @NotNull UUID getUserUUID() {
		return this.userUUID;
	}

	public @NotNull Map<String, DepositoryData> getBackpacks() {
		return this.backpacks;
	}


	public @Nullable DepositoryData getBackpackData(String backpackID) {
		ItemDepository configuration = Main.getBackpackManager().getDepository(backpackID);
		if (configuration == null) return null;
		return getBackpackData(configuration);
	}

	public @NotNull DepositoryData getBackpackData(ItemDepository backpack) {
		if (!getBackpacks().containsKey(backpack.getIdentifier())) {
			getBackpacks().put(backpack.getIdentifier(), DepositoryData.emptyContents(backpack.getItems().keySet()));
		}
		return getBackpacks().get(backpack.getIdentifier());
	}


	public @NotNull Set<String> getBackpackIDs() {
		return getBackpacks().keySet();
	}


	public @Nullable ItemData getItemData(@NotNull String backpackID, @NotNull String typeID) {
		DepositoryData data = getBackpackData(backpackID);
		if (data == null) return null;
		if (!Main.getBackpackManager().hasItem(backpackID, typeID)) return null;
		ItemData itemData = data.getItemData(typeID);
		if (itemData == null) {
			itemData = ItemData.emptyItemData();
			data.getContents().put(typeID, itemData);
		}
		return itemData;
	}

	public @NotNull ItemData getItemData(@NotNull ItemDepository backpack, @NotNull DepositoryItem itemType) {
		DepositoryData data = getBackpackData(backpack);
		ItemData itemData = data.getItemData(itemType.getTypeID());
		if (itemData == null) {
			itemData = ItemData.emptyItemData();
			data.getContents().put(itemType.getTypeID(), itemData);
		}
		return itemData;
	}


	public @Nullable Integer getItemAmount(@NotNull String backpackID, @NotNull String typeID) {
		ItemData data = getItemData(backpackID, typeID);
		if (data == null) return null;
		return data.getAmount();
	}


	public @Nullable Integer getItemSold(@NotNull String backpackID, @NotNull String typeID) {
		checkoutDate();
		ItemData data = getItemData(backpackID, typeID);
		if (data == null) return null;
		return data.getSold();
	}


	public @Nullable Integer setItemAmount(@NotNull String backpackID, @NotNull String typeID, int amount) {
		ItemData data = getItemData(backpackID, typeID);
		if (data == null) return null;
		data.setAmount(amount);
		return amount;
	}


	public @Nullable Integer setItemSold(@NotNull String backpackID, @NotNull String typeID, int soldAmount) {
		ItemData data = getItemData(backpackID, typeID);
		if (data == null) return null;
		data.setSold(soldAmount);
		return soldAmount;
	}


	public @Nullable Integer addItemAmount(@NotNull String backpackID, @NotNull String typeID, int amount) {
		Integer current = getItemAmount(backpackID, typeID);
		if (current == null) return null;
		return setItemAmount(backpackID, typeID, current + amount);
	}


	public @Nullable Integer addItemSold(@NotNull String backpackID, @NotNull String typeID, int amount) {
		Integer current = getItemSold(backpackID, typeID);
		if (current == null) return null;
		return setItemSold(backpackID, typeID, current + amount);
	}


	public @Nullable Integer removeItemAmount(@NotNull String backpackID, @NotNull String typeID, int amount) {
		return addItemAmount(backpackID, typeID, -amount);
	}


	public @Nullable Integer removeItemSold(@NotNull String backpackID, @NotNull String typeID, int amount) {
		return addItemSold(backpackID, typeID, -amount);
	}


	public Date getDate() {
		return this.day;
	}


	public boolean isCurrentDay() {
		return this.day.equals(new Date(System.currentTimeMillis()));
	}


	public void checkoutDate() {
		if (isCurrentDay()) return;
		this.day = new Date(System.currentTimeMillis()); //更新日期
		getBackpacks().values().stream()
				.flatMap(value -> value.getContents().values().stream())
				.forEach(ItemData::clearSold);
	}


	public void save() throws Exception {
		this.storage.saveUserData(this);
	}

}
