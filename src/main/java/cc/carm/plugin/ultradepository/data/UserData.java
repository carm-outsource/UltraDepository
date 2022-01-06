package cc.carm.plugin.ultradepository.data;

import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.util.DateIntUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class UserData {

	public final UUID userUUID;

	Map<String, DepositoryData> depositories;

	int date;

	public UserData(UUID userUUID,
					Map<String, DepositoryData> depositories, int date) {
		this.userUUID = userUUID;
		this.depositories = depositories;
		this.date = date;
	}


	public @NotNull UUID getUserUUID() {
		return this.userUUID;
	}

	public @NotNull Map<String, DepositoryData> getDepositories() {
		return this.depositories;
	}

	public void setDepository(DepositoryData data) {
		this.depositories.put(data.getIdentifier(), data);
	}

	public @Nullable DepositoryData getDepositoryData(String depositoryID) {
		Depository depository = UltraDepository.getDepositoryManager().getDepository(depositoryID);
		if (depository == null) return null;
		return getDepositoryData(depository);
	}

	public @NotNull DepositoryData getDepositoryData(Depository depository) {
		getDepositories().putIfAbsent(depository.getIdentifier(), DepositoryData.emptyContents(depository, this));
		return getDepositories().get(depository.getIdentifier());
	}

	public @Nullable DepositoryItemData getItemData(@NotNull String depositoryID, @NotNull String typeID) {
		DepositoryData data = getDepositoryData(depositoryID);
		if (data == null) return null;
		return data.getItemData(typeID);
	}

	public @NotNull DepositoryItemData getItemData(@NotNull DepositoryItem itemType) {
		checkoutDate();
		return getDepositoryData(itemType.getDepository()).getItemData(itemType);
	}

	public @Nullable Integer getItemAmount(@NotNull String depositoryID, @NotNull String typeID) {
		DepositoryItemData data = getItemData(depositoryID, typeID);
		if (data == null) return null;
		return data.getAmount();
	}

	public @Nullable Integer getItemSold(@NotNull String depositoryID, @NotNull String typeID) {
		DepositoryItemData data = getItemData(depositoryID, typeID);
		if (data == null) return null;
		return data.getSold();
	}


	public @Nullable Integer setItemAmount(@NotNull String depositoryID, @NotNull String typeID, int amount) {
		DepositoryItemData data = getItemData(depositoryID, typeID);
		if (data == null) return null;
		data.setAmount(Math.max(0, amount)); // 最小为0
		return data.getAmount();
	}


	public @Nullable Integer setItemSold(@NotNull String depositoryID, @NotNull String typeID, int soldAmount) {
		DepositoryItemData data = getItemData(depositoryID, typeID);
		if (data == null) return null;
		data.setSold(Math.max(0, soldAmount));
		return data.getSold();
	}


	public @Nullable Integer addItemAmount(@NotNull String depositoryID, @NotNull String typeID, int amount) {
		Integer current = getItemAmount(depositoryID, typeID);
		if (current == null) return null;
		return setItemAmount(depositoryID, typeID, current + amount);
	}


	public @Nullable Integer addItemSold(@NotNull String depositoryID, @NotNull String typeID, int amount) {
		Integer current = getItemSold(depositoryID, typeID);
		if (current == null) return null;
		return setItemSold(depositoryID, typeID, current + amount);
	}


	public @Nullable Integer removeItemAmount(@NotNull String depositoryID, @NotNull String typeID, int amount) {
		return addItemAmount(depositoryID, typeID, -amount);
	}


	public @Nullable Integer removeItemSold(@NotNull String depositoryID, @NotNull String typeID, int amount) {
		return addItemSold(depositoryID, typeID, -amount);
	}


	public Date getDate() {
		return DateIntUtil.getDate(getDateInt());
	}

	public int getDateInt() {
		return this.date;
	}


	public boolean isCurrentDay() {
		return this.date == DateIntUtil.getCurrentDate();
	}


	public void checkoutDate() {
		if (isCurrentDay()) return;

		this.date = DateIntUtil.getCurrentDate(); //更新日期
		getDepositories().values().stream()
				.flatMap(value -> value.getContents().values().stream())
				.forEach(DepositoryItemData::clearSold);
	}

	public Map<String, Map<String, Map<String, Integer>>> serializeToMap() {
		Map<String, Map<String, Map<String, Integer>>> values = new LinkedHashMap<>();

		getDepositories().forEach((depositoryID, depositoryData) -> {
			Map<String, Map<String, Integer>> depositoryDataMap = new LinkedHashMap<>();
			depositoryData.getContents().forEach((itemType, itemData) -> {
				Map<String, Integer> itemDataMap = new HashMap<>();
				if (itemData.getAmount() > 0) itemDataMap.put("amount", itemData.getAmount());
				if (itemData.getSold() > 0) itemDataMap.put("sold", itemData.getSold());
				if (!itemDataMap.isEmpty()) depositoryDataMap.put(itemType, itemDataMap);
			});
			if (!depositoryDataMap.isEmpty()) values.put(depositoryID, depositoryDataMap);
		});

		return values;
	}

}
