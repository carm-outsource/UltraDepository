package cc.carm.plugin.ultrabackpack.configuration;

import cc.carm.plugin.ultrabackpack.configuration.values.ConfigValue;

public class PluginConfig {

	public static final ConfigValue<Boolean> DEBUG = new ConfigValue<>(
			"debug", Boolean.class
	);

}
