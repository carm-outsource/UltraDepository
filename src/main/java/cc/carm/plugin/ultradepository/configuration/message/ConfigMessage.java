package cc.carm.plugin.ultradepository.configuration.message;


import cc.carm.plugin.ultradepository.util.MessageUtil;
import cc.carm.plugin.ultradepository.configuration.values.ConfigValue;
import cc.carm.plugin.ultradepository.manager.ConfigManager;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ConfigMessage extends ConfigValue<String> {

	String[] messageParams;

	public ConfigMessage(String configSection) {
		this(configSection, null);
	}

	public ConfigMessage(String configSection, String defaultValue) {
		this(configSection, defaultValue, null);
	}

	public ConfigMessage(String configSection, String defaultValue, String[] messageParams) {
		super(ConfigManager.getMessageConfig(), configSection, String.class, defaultValue);
		this.messageParams = messageParams;
	}

	public String get(CommandSender sender, Object[] values) {
		if (messageParams != null) {
			return get(sender, messageParams, values);
		} else {
			return get(sender, new String[0], new Object[0]);
		}
	}

	public String get(CommandSender sender, String[] params, Object[] values) {
		List<String> messages = MessageUtil.setPlaceholders(sender, Collections.singletonList(get()), params, values);
		return messages != null && !messages.isEmpty() ? messages.get(0) : "";
	}

	public void send(CommandSender sender) {
		MessageUtil.send(sender, get());
	}

	public void sendWithPlaceholders(CommandSender sender) {
		MessageUtil.sendWithPlaceholders(sender, get());
	}

	public void sendWithPlaceholders(CommandSender sender, String[] params, Object[] values) {
		MessageUtil.sendWithPlaceholders(sender, Collections.singletonList(get()), params, values);
	}

}
