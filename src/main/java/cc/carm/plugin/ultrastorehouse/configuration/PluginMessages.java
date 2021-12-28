package cc.carm.plugin.ultrastorehouse.configuration;


import cc.carm.plugin.ultrastorehouse.configuration.message.ConfigMessageList;

public class PluginMessages {

	public static final ConfigMessageList SOLD = new ConfigMessageList(
			"item-sold", new String[0], new String[]{
			"%(item)", "%(amount)", "%(money)"
	});
	public static final ConfigMessageList COLLECTED = new ConfigMessageList(
			"item-collected", new String[0], new String[]{
			"%(item)", "%(amount)", "%(backpack)"
	});
}
