package cc.carm.plugin.ultrastorehouse.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DepositoryData {

	private final Map<@NotNull String, @NotNull ItemData> contents;

	public DepositoryData(Map<@NotNull String, @NotNull ItemData> contents) {
		this.contents = contents;
	}

	public @NotNull Map<String, ItemData> getContents() {
		return this.contents;
	}

	public @Nullable ItemData getItemData(@NotNull String itemType) {
		return getContents().get(itemType);
	}

	public static DepositoryData emptyContents(Set<String> itemTypes) {
		return new DepositoryData(itemTypes.stream().collect(Collectors.toMap(
				item -> item, item -> ItemData.emptyItemData(), (a, b) -> b
		)));
	}

}
