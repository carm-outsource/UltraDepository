package cc.carm.plugin.ultradepository.data;

import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DepositoryData {

	final UserData owner;
	final Depository source;
	private final Map<@NotNull String, @NotNull DepositoryItemData> contents;

	public DepositoryData(@NotNull Depository source, @NotNull UserData owner,
						  Map<@NotNull String, @NotNull DepositoryItemData> contents) {
		this.owner = owner;
		this.source = source;
		this.contents = contents;
	}

	public String getIdentifier() {
		return getSource().getIdentifier();
	}

	public Depository getSource() {
		return source;
	}

	public @NotNull Map<String, DepositoryItemData> getContents() {
		return this.contents;
	}

	public @Nullable DepositoryItemData getItemData(@NotNull String itemType) {
		DepositoryItem item = getSource().getItems().get(itemType);
		if (item != null) {
			return getContents().putIfAbsent(item.getTypeID(), DepositoryItemData.emptyItemData(item, this));
		} else {
			return null;
		}
	}

	public @NotNull DepositoryItemData getItemData(@NotNull DepositoryItem item) {
		return Objects.requireNonNull(getContents().putIfAbsent(item.getTypeID(), DepositoryItemData.emptyItemData(item, this)));
	}

	public int getUsedCapacity() {
		return getContents().values().stream().mapToInt(DepositoryItemData::getAmount).sum();
	}

	public static DepositoryData emptyContents(Depository source, UserData owner) {
		return new DepositoryData(source, owner, new HashMap<>());
	}

}
