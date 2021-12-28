package cc.carm.plugin.ultradepository.data;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.storage.DataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UserData {

	public final UUID userUUID;

	DataStorage storage;
	Map<String, DepositoryData> depositories;

	Date day;

	public UserData(UUID userUUID, DataStorage storage,
					Map<String, DepositoryData> depositories, Date day) {
		this.userUUID = userUUID;
		this.storage = storage;
		this.depositories = depositories;
		this.day = day;
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
		return Objects.requireNonNull(getDepositories().putIfAbsent(
				depository.getIdentifier(),
				DepositoryData.emptyContents(depository, this))
		);
	}

	public @Nullable DepositoryItemData getItemData(@NotNull String depositoryID, @NotNull String typeID) {
		DepositoryData data = getDepositoryData(depositoryID);
		if (data == null) return null;
		return data.getItemData(typeID);
	}

	public @NotNull DepositoryItemData getItemData(@NotNull DepositoryItem itemType) {
		return getDepositoryData(itemType.getDepository()).getItemData(itemType);
	}

	public @Nullable Integer getItemAmount(@NotNull String depositoryID, @NotNull String typeID) {
		DepositoryItemData data = getItemData(depositoryID, typeID);
		if (data == null) return null;
		return data.getAmount();
	}

	public @Nullable Integer getItemSold(@NotNull String depositoryID, @NotNull String typeID) {
		checkoutDate();
		DepositoryItemData data = getItemData(depositoryID, typeID);
		if (data == null) return null;
		return data.getSold();
	}


	public @Nullable Integer setItemAmount(@NotNull String depositoryID, @NotNull String typeID, int amount) {
		DepositoryItemData data = getItemData(depositoryID, typeID);
		if (data == null) return null;
		data.setAmount(amount);
		return amount;
	}


	public @Nullable Integer setItemSold(@NotNull String depositoryID, @NotNull String typeID, int soldAmount) {
		DepositoryItemData data = getItemData(depositoryID, typeID);
		if (data == null) return null;
		data.setSold(soldAmount);
		return soldAmount;
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
		return this.day;
	}


	public boolean isCurrentDay() {
		return this.day.equals(new Date(System.currentTimeMillis()));
	}


	public void checkoutDate() {
		if (isCurrentDay()) return;
		this.day = new Date(System.currentTimeMillis()); //更新日期
		getDepositories().values().stream()
				.flatMap(value -> value.getContents().values().stream())
				.forEach(DepositoryItemData::clearSold);
	}


	public void save() throws Exception {
		this.storage.saveUserData(this);
	}

}
