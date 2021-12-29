package cc.carm.plugin.ultradepository.data;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.storage.DataStorage;
import cc.carm.plugin.ultradepository.util.DateIntUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.Map;
import java.util.UUID;

public class UserData {

	public final UUID userUUID;

	DataStorage storage;
	Map<String, DepositoryData> depositories;

	int date;

	public UserData(UUID userUUID, DataStorage storage,
					Map<String, DepositoryData> depositories, int date) {
		this.userUUID = userUUID;
		this.storage = storage;
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
		Depository depository = Main.getDepositoryManager().getDepository(depositoryID);
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
		return new Date(DateIntUtil.getDateMillis(getDateInt()));
	}

	public int getDateInt() {
		return this.date;
	}


	public boolean isCurrentDay() {
		return this.date == DateIntUtil.getCurrentDate();
	}


	public void checkoutDate() {
		if (isCurrentDay()) {
			Main.debug("Date is not change, skip clear sold amount.");
			return;
		}
		this.date = DateIntUtil.getCurrentDate(); //更新日期
		Main.debug("Date changed, clear sold.");
		getDepositories().values().stream()
				.flatMap(value -> value.getContents().values().stream())
				.forEach(DepositoryItemData::clearSold);
	}


	public void save() throws Exception {
		this.storage.saveUserData(this);
	}

}
