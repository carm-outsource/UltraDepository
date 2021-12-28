package cc.carm.plugin.ultradepository.configuration.message;


import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.file.FileConfig;
import cc.carm.plugin.ultradepository.configuration.values.ConfigValueList;
import cc.carm.plugin.ultradepository.manager.ConfigManager;
import cc.carm.plugin.ultradepository.util.MessageUtil;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigMessageList extends ConfigValueList<String> {

	String[] messageParams;

	public ConfigMessageList(String configSection) {
		super(ConfigManager.getMessageConfig(), configSection, String.class);
	}

	public ConfigMessageList(String configSection, String[] defaultValue) {
		this(configSection, defaultValue, null);
	}

	public ConfigMessageList(String configSection, String[] defaultValue, String[] messageParams) {
		super(ConfigManager.getMessageConfig(), configSection, String.class, defaultValue);
		this.messageParams = messageParams;
	}

	public ConfigMessageList(FileConfig config, String configSection, String[] defaultValue, String[] messageParams) {
		super(config, configSection, String.class, defaultValue);
		this.messageParams = messageParams;
	}

	public List<String> get(@Nullable CommandSender sender) {
		return MessageUtil.setPlaceholders(sender, get());
	}

	public List<String> get(@Nullable CommandSender sender, Object[] values) {
		if (messageParams != null) {
			return get(sender, messageParams, values);
		} else {
			return get(sender);
		}
	}

	public List<String> get(@Nullable CommandSender sender, String[] params, Object[] values) {
		return MessageUtil.setPlaceholders(sender, get(), params, values);
	}

	public void send(@Nullable CommandSender sender) {
		MessageUtil.sendWithPlaceholders(sender, get());
	}

	public void send(@Nullable CommandSender sender, Object[] values) {
		if (messageParams != null) {
			send(sender, messageParams, values);
		} else {
			send(sender);
		}
	}

	public void send(@Nullable CommandSender sender, String[] params, Object[] values) {
		MessageUtil.sendWithPlaceholders(sender, get(), params, values);
	}
}
