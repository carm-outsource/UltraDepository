package cc.carm.plugin.ultrabackpack.api.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class UBContentsData {

	private final Map<@NotNull String, @NotNull UBItemData> contents;

	public UBContentsData(Map<@NotNull String, @NotNull UBItemData> contents) {
		this.contents = contents;
	}

	public @NotNull Map<String, UBItemData> getContents() {
		return this.contents;
	}

	public @Nullable UBItemData getItemData(@NotNull String itemType) {
		return getContents().get(itemType);
	}

	public static UBContentsData emptyContents() {
		return new UBContentsData(new HashMap<>());
	}

}
