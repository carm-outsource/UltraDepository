package cc.carm.plugin.ultrastorehouse.configuration.values;

import cc.carm.plugin.ultrastorehouse.configuration.file.FileConfig;
import cc.carm.plugin.ultrastorehouse.manager.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ConfigSectionCast<V> {

	FileConfig source;

	String configSection;
	@NotNull Function<ConfigurationSection, V> valueCast;
	V defaultValue;

	V valueCache;
	long updateTime;

	public ConfigSectionCast(String configSection, @NotNull Function<ConfigurationSection, V> valueCast) {
		this(configSection, valueCast, null);
	}

	public ConfigSectionCast(String configSection,
							 @NotNull Function<ConfigurationSection, V> valueCast,
							 V defaultValue) {
		this(ConfigManager.getPluginConfig(), configSection, valueCast, defaultValue);
	}

	public ConfigSectionCast(FileConfig source, String configSection,
							 @NotNull Function<ConfigurationSection, V> valueCast,
							 V defaultValue) {
		this.source = source;
		this.configSection = configSection;
		this.valueCast = valueCast;
		this.defaultValue = defaultValue;
	}

	public FileConfiguration getConfiguration() {
		return this.source.getConfig();
	}


	public @Nullable V get() {
		if (valueCache != null && !this.source.isExpired(this.updateTime)) return valueCache;
		if (!getConfiguration().contains(this.configSection)) return defaultValue;
		try {
			V finalValue = this.valueCast.apply(getConfiguration().getConfigurationSection(this.configSection));
			if (finalValue != null) {
				this.valueCache = finalValue;
				this.updateTime = System.currentTimeMillis();
				return finalValue;
			} else {
				return defaultValue;
			}
		} catch (Exception ignore) {
			return defaultValue;
		}
	}

	public void set(ConfigurationSection section) {

	}

	public void save() {
		this.source.save();
	}

}
