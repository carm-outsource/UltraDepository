package cc.carm.plugin.ultrabackpack.configuration;

import cc.carm.plugin.ultrabackpack.configuration.values.ConfigValue;

public class PluginConfig {

	public static final ConfigValue<Boolean> DEBUG = new ConfigValue<>(
			"debug", Boolean.class
	);

	public static class Database {
		public static final ConfigValue<String> DRIVER_NAME = new ConfigValue<>(
				"database.driver", String.class, "com.mysql.jdbc.Driver"
		);

		public static final ConfigValue<String> URL = new ConfigValue<>(
				"database.url", String.class, "jdbc:mysql://127.0.0.1:3306/minecraft"
		);


		public static final ConfigValue<String> USERNAME = new ConfigValue<>(
				"database.username", String.class, "username"
		);
		public static final ConfigValue<String> PASSWORD = new ConfigValue<>(
				"database.password", String.class, "password"
		);

	}


}
