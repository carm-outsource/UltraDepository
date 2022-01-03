package cc.carm.plugin.ultradepository.storage;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;
import cc.carm.plugin.ultradepository.configuration.values.ConfigValue;
import cc.carm.plugin.ultradepository.data.DepositoryData;
import cc.carm.plugin.ultradepository.data.DepositoryItemData;
import cc.carm.plugin.ultradepository.data.UserData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JSONStorage implements DataStorage {

	private static final ConfigValue<String> FILE_PATH = new ConfigValue<>(
			"storage.file-path", String.class, "data"
	);

	private File dataContainer;

	public static final Gson GSON = new Gson();
	public static final JsonParser PARSER = new JsonParser();

	@Override
	public boolean initialize() {
		dataContainer = new File(Main.getInstance().getDataFolder(), FILE_PATH.get());
		if (!dataContainer.exists()) {
			return dataContainer.mkdir();
		} else {
			return dataContainer.isDirectory();
		}
	}

	@Override
	public void shutdown() {
		// 似乎没什么需要做的？
	}

	public File getDataContainer() {
		return dataContainer;
	}

	@Override
	public @Nullable UserData loadData(@NotNull UUID uuid) throws Exception {
		File userDataFile = new File(getDataContainer(), uuid + ".json");
		if (!userDataFile.exists()) {
			Main.debug("当前文件夾内不存在玩家 " + uuid + " 的数据，视作新档。");
			return null;
		}

		JsonElement dataElement = PARSER.parse(new FileReader(userDataFile));
		if (!dataElement.isJsonObject()) throw new NullPointerException(userDataFile.getName());
		JsonObject dataObject = dataElement.getAsJsonObject();

		int dateInt = dataObject.get("date").getAsInt();
		JsonObject repositoriesObject = dataObject.getAsJsonObject("depositories");

		UserData userData = new UserData(uuid, new HashMap<>(), dateInt);

		for (Map.Entry<String, JsonElement> entry : repositoriesObject.entrySet()) {
			Depository depository = Main.getDepositoryManager().getDepository(entry.getKey());
			if (depository == null) continue;

			DepositoryData contentsData = parseContentsData(depository, userData, entry.getValue());
			if (contentsData != null) userData.setDepository(contentsData);

		}

		return userData;
	}

	@Override
	public void saveUserData(@NotNull UserData data) throws Exception {
		JsonObject dataObject = new JsonObject();
		dataObject.addProperty("date", data.getDateInt());
		dataObject.add("depositories", GSON.toJsonTree(data.serializeToMap()));

		FileWriter writer = new FileWriter(new File(getDataContainer(), data.getUserUUID() + ".json"));
		writer.write(GSON.toJson(dataObject));
		writer.flush();
		writer.close();
	}

	protected DepositoryData parseContentsData(@NotNull Depository source,
											   @NotNull UserData owner,
											   @NotNull JsonElement contentsElement) {
		return contentsElement.isJsonObject() ? parseContentsData(source, owner, contentsElement.getAsJsonObject()) : null;
	}

	protected DepositoryData parseContentsData(@NotNull Depository source,
											   @NotNull UserData owner,
											   @NotNull JsonObject contentsObject) {
		DepositoryData data = DepositoryData.emptyContents(source, owner);
		for (Map.Entry<String, JsonElement> entry : contentsObject.entrySet()) {

			DepositoryItem item = source.getItems().get(entry.getKey());
			if (item == null) continue;

			DepositoryItemData itemData = parseItemData(item, data, entry.getValue());
			if (itemData != null) data.getContents().put(item.getTypeID(), itemData);

		}
		return data;
	}

	protected DepositoryItemData parseItemData(@NotNull DepositoryItem source,
											   @NotNull DepositoryData owner,
											   @NotNull JsonElement itemElement) {
		return itemElement.isJsonObject() ? parseItemData(source, owner, itemElement.getAsJsonObject()) : null;
	}

	protected DepositoryItemData parseItemData(@NotNull DepositoryItem source,
											   @NotNull DepositoryData owner,
											   @NotNull JsonObject itemObject) {
		int amount = itemObject.has("amount") ? itemObject.get("amount").getAsInt() : 0;
		int sold = itemObject.has("sold") ? itemObject.get("sold").getAsInt() : 0;
		if (amount == 0 && sold == 0) return null;

		return new DepositoryItemData(source, owner, amount, sold);
	}

}
