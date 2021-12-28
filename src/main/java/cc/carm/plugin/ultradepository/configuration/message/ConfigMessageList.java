package cc.carm.plugin.ultradepository.configuration.message;


import cc.carm.plugin.ultradepository.util.MessageUtil;
import cc.carm.plugin.ultradepository.configuration.values.ConfigValueList;
import cc.carm.plugin.ultradepository.manager.ConfigManager;
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
		MessageUtil.send(sender, get());
	}

	public void sendWithPlaceholders(@Nullable CommandSender sender) {
		MessageUtil.sendWithPlaceholders(sender, get());
	}

	public void sendWithPlaceholders(@Nullable CommandSender sender, Object[] values) {
		if (messageParams != null) {
			sendWithPlaceholders(sender, messageParams, values);
		} else {
			sendWithPlaceholders(sender);
		}
	}

	public void sendWithPlaceholders(@Nullable CommandSender sender, String[] params, Object[] values) {
		MessageUtil.sendWithPlaceholders(sender, get(), params, values);
	}
}
