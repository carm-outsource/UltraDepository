package cc.carm.plugin.ultrabackpack.api.data;

import java.util.HashMap;
import java.util.Map;

public class UBContentsData {

	private final Map<String, UBItemData> contents;

	public UBContentsData(Map<String, UBItemData> contents) {
		this.contents = contents;
	}

	public Map<String, UBItemData> getContents() {
		return this.contents;
	}

	public UBItemData getItemData(String itemType) {
		return getContents().get(itemType);
	}


	public static UBContentsData emptyContents() {
		return new UBContentsData(new HashMap<>());
	}

}
