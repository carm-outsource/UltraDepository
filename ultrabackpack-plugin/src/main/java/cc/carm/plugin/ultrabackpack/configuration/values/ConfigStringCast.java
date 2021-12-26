package cc.carm.plugin.ultrabackpack.configuration.values;

import cc.carm.plugin.ultrabackpack.configuration.file.FileConfig;
import cc.carm.plugin.ultrabackpack.manager.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ConfigStringCast<V> {

	FileConfig source;

	String configSection;
	@NotNull Function<String, V> valueCast;
	V defaultValue;

	V valueCache;
	long updateTime;

	public ConfigStringCast(String configSection, @NotNull Function<String, V> valueCast) {
		this(configSection, valueCast, null);
	}

	public ConfigStringCast(String configSection, @NotNull Function<String, V> valueCast, V defaultValue) {
		this(ConfigManager.getPluginConfig(), configSection, valueCast, defaultValue);
	}

	public ConfigStringCast(FileConfig source, String configSection, @NotNull Function<String, V> valueCast, V defaultValue) {
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
		if (!getConfiguration().contains(this.configSection)) return setDefault();
		try {
			V finalValue = this.valueCast.apply(getConfiguration().getString(this.configSection));
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

	public void set(V value) {
		getConfiguration().set(this.configSection, value);
		this.save();
	}

	public void save() {
		this.source.save();
	}

	public V setDefault() {
		set(this.defaultValue);
		return this.defaultValue;
	}

}
