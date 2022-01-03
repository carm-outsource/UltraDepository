package cc.carm.plugin.ultradepository.storage;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import cc.carm.lib.easysql.api.action.query.SQLQuery;
import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.configuration.depository.Depository;
import cc.carm.plugin.ultradepository.configuration.values.ConfigValue;
import cc.carm.plugin.ultradepository.data.DepositoryData;
import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.util.DateIntUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySQLStorage extends JSONStorage {

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
	public @Nullable UserData loadData(@NotNull UUID uuid) throws Exception {
		try (SQLQuery query = createAction(uuid).execute()) {
			ResultSet resultSet = query.getResultSet();

			if (resultSet == null || !resultSet.next()) return null;

			String dataJSON = resultSet.getString("data");
			Date date = resultSet.getDate("day");
			UserData data = new UserData(uuid, new HashMap<>(), DateIntUtil.getDateInt(date));

			JsonElement dataElement = PARSER.parse(dataJSON);
			if (dataElement.isJsonObject()) {
				for (Map.Entry<String, JsonElement> entry : dataElement.getAsJsonObject().entrySet()) {
					Depository depository = Main.getDepositoryManager().getDepository(entry.getKey());
					if (depository == null) continue;

					DepositoryData contentsData = parseContentsData(depository, data, entry.getValue());
					if (contentsData != null) data.setDepository(contentsData);

				}
			}
			return data;

		} catch (Exception exception) {
			throw new Exception(exception);
		}
	}

	@Override
	public void saveUserData(@NotNull UserData data) throws Exception {
		getSQLManager().createReplace(SQLTables.USER_DATA.getName())
				.setColumnNames("uuid", "data", "day")
				.setParams(data.getUserUUID(), GSON.toJson(data.serializeToMap()), data.getDate())
				.execute();
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

}
