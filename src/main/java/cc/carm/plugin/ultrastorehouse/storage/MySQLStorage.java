package cc.carm.plugin.ultrastorehouse.storage;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import cc.carm.lib.easysql.api.action.query.SQLQuery;
import cc.carm.plugin.ultrastorehouse.Main;
import cc.carm.plugin.ultrastorehouse.data.DepositoryData;
import cc.carm.plugin.ultrastorehouse.data.ItemData;
import cc.carm.plugin.ultrastorehouse.configuration.PluginConfig;
import cc.carm.plugin.ultrastorehouse.configuration.values.ConfigValue;
import cc.carm.plugin.ultrastorehouse.data.UserData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySQLStorage implements DataStorage {

	private static final ConfigValue<String> DRIVER_NAME = new ConfigValue<>(
			"storage.mysql.driver", String.class, "com.mysql.jdbc.Driver"
	);

	private static final ConfigValue<String> URL = new ConfigValue<>(
			"storage.mysql.url", String.class, "jdbc:mysql://127.0.0.1:3306/minecraft"
	);

	private static final ConfigValue<String> USERNAME = new ConfigValue<>(
			"storage.mysql.username", String.class, "username"
	);
	private static final ConfigValue<String> PASSWORD = new ConfigValue<>(
			"storage.mysql.password", String.class, "password"
	);

	public enum SQLTables {

		USER_DATA("ub_data", new String[]{
				"`uuid` VARCHAR(36) NOT NULL PRIMARY KEY", // 用户的UUID
				"`data` MEDIUMTEXT NOT NULL",// 背包内具体物品
				"`day` DATE NOT NULL", // 记录卖出数量的所在天
		});

		String name;
		String[] columns;

		SQLTables(String name, String[] columns) {
			this.name = name;
			this.columns = columns;
		}


		public static void createTables(SQLManager sqlManager) throws SQLException {
			for (SQLTables value : values()) {
				sqlManager.createTable(value.getName())
						.setColumns(value.getColumns())
						.build().execute();
			}
		}

		public String getName() {
			return name;
		}

		public String[] getColumns() {
			return columns;
		}

	}

	public static final Gson GSON = new Gson();
	public static final JsonParser PARSER = new JsonParser();

	SQLManager sqlManager;

	@Override
	public boolean initialize() {

		try {
			Main.log("	尝试连接到数据库...");
			this.sqlManager = EasySQL.createManager(DRIVER_NAME.get(), URL.get(), USERNAME.get(), PASSWORD.get());
			this.sqlManager.setDebugMode(PluginConfig.DEBUG.get());
		} catch (Exception exception) {
			Main.error("无法连接到数据库，请检查配置文件。");
			Main.error("Could not connect to the database, please check the configuration.");
			exception.printStackTrace();
			return false;
		}

		try {
			Main.log("	创建插件所需表...");
			SQLTables.createTables(sqlManager);
		} catch (SQLException exception) {
			Main.error("无法创建插件所需的表，请检查数据库权限。");
			Main.error("Could not create necessary tables, please check the database privileges.");
			exception.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public void shutdown() {
		Main.log("	关闭数据库连接...");
		EasySQL.shutdownManager(getSQLManager());
	}

	@Override
	public @NotNull UserData loadData(@NotNull UUID uuid) throws Exception {
		long start = System.currentTimeMillis();
		Main.debug("正通过 MySQLStorage 加载 " + uuid + " 的用户数据...");
		try (SQLQuery query = createAction(uuid).execute()) {
			ResultSet resultSet = query.getResultSet();
			Map<String, DepositoryData> dataMap = new HashMap<>();
			if (resultSet != null && resultSet.next()) {
				String dataJSON = resultSet.getString("data");
				Date date = resultSet.getDate("day");
				JsonElement dataElement = PARSER.parse(dataJSON);
				if (dataElement.isJsonObject()) {
					dataElement.getAsJsonObject().entrySet().forEach(entry -> {
						String backpackID = entry.getKey();
						DepositoryData contentsData = parseContentsData(entry.getValue());
						if (contentsData != null) dataMap.put(backpackID, contentsData);
					});
				}
				Main.debug("通过 MySQLStorage 加载 " + uuid + " 的用户数据完成，"
						+ "耗时 " + (System.currentTimeMillis() - start) + "ms。");
				return new UserData(uuid, this, dataMap, date);
			}
			Main.debug("当前库内不存在玩家 " + uuid + " 的数据，视作新档。");
			return new UserData(uuid, this, new HashMap<>(), new Date(System.currentTimeMillis()));
		} catch (Exception exception) {
			throw new Exception(exception);
		}
	}

	@Override
	public void saveUserData(@NotNull UserData data) throws Exception {
		long start = System.currentTimeMillis();
		Main.debug("正通过 MySQLStorage 保存 " + data.getUserUUID() + " 的用户数据...");

		JsonObject dataObject = new JsonObject();

		data.getBackpacks().forEach((id, contents) -> dataObject.add(id, serializeContentsData(contents)));

		try {
			getSQLManager().createReplace(SQLTables.USER_DATA.getName())
					.setColumnNames("uuid", "data", "day")
					.setParams(data.getUserUUID(), GSON.toJson(dataObject), data.getDate())
					.execute();
		} catch (SQLException exception) {
			Main.error("在保存玩家 #" + data.getUserUUID() + " 的数据时出现异常。");
			Main.error("Error occurred when saving #" + data.getUserUUID() + " data.");
			throw new Exception(exception);
		}

		Main.debug("通过 MySQLStorage 保存 " + data.getUserUUID() + " 的用户数据完成，" +
				"耗时 " + (System.currentTimeMillis() - start) + "ms。");

	}

	private SQLManager getSQLManager() {
		return sqlManager;
	}

	private PreparedQueryAction createAction(UUID uuid) {
		return getSQLManager().createQuery()
				.inTable(SQLTables.USER_DATA.getName())
				.addCondition("uuid", uuid.toString())
				.setLimit(1).build();
	}

	private DepositoryData parseContentsData(JsonElement contentsElement) {
		return contentsElement.isJsonObject() ? parseContentsData(contentsElement.getAsJsonObject()) : null;
	}

	private DepositoryData parseContentsData(JsonObject contentsObject) {
		Map<String, ItemData> contents = new HashMap<>();
		for (Map.Entry<String, JsonElement> entry : contentsObject.entrySet()) {
			String itemType = entry.getKey();
			ItemData data = parseItemData(entry.getValue());
			contents.put(itemType, data);
		}
		return new DepositoryData(contents);
	}

	private ItemData parseItemData(JsonElement itemElement) {
		return itemElement.isJsonObject() ? parseItemData(itemElement.getAsJsonObject()) : null;
	}

	private ItemData parseItemData(JsonObject itemObject) {
		if (!itemObject.has("amount") || !itemObject.has("sold")) {
			return ItemData.emptyItemData();
		} else {
			return new ItemData(itemObject.get("amount").getAsInt(), itemObject.get("sold").getAsInt());
		}
	}

	@Nullable
	private JsonObject serializeContentsData(@Nullable DepositoryData contentsData) {
		if (contentsData == null) return null;
		JsonObject contentsObject = new JsonObject();
		contentsData.getContents().forEach((typeID, item) -> contentsObject.add(typeID, serializeItemData(item)));
		return contentsObject;
	}

	@NotNull
	private JsonObject serializeItemData(@NotNull ItemData itemData) {
		JsonObject itemObject = new JsonObject();
		itemObject.addProperty("amount", itemData.getAmount());
		itemObject.addProperty("sold", itemData.getSold());
		return itemObject;
	}

}
