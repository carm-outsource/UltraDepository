package cc.carm.plugin.ultradepository.configuration;


import cc.carm.plugin.ultradepository.configuration.message.ConfigMessageList;

public class PluginMessages {

	public static final ConfigMessageList HELP_CONSOLE = new ConfigMessageList("help.console");

	public static final ConfigMessageList HELP_PLAYER = new ConfigMessageList("help.player");


	public static final ConfigMessageList SOLD = new ConfigMessageList(
			"item-sold", new String[0], new String[]{
			"%(item)", "%(amount)", "%(money)"
	});

	public static final ConfigMessageList PICKUP = new ConfigMessageList(
			"item-pickup", new String[0], new String[]{
			"%(item)", "%(amount)"
	});

	public static final ConfigMessageList COLLECTED = new ConfigMessageList(
			"item-collected", new String[0], new String[]{
			"%(item)", "%(amount)", "%(depository)"
	});


	public static final ConfigMessageList NO_ECONOMY = new ConfigMessageList("no-economy");

	public static final ConfigMessageList NO_SPACE = new ConfigMessageList("no-space");

	public static final ConfigMessageList NO_DEPOSITORY = new ConfigMessageList("no-depository");

	public static final ConfigMessageList NO_ITEM = new ConfigMessageList("no-item");

	public static final ConfigMessageList NO_ENOUGH_ITEM = new ConfigMessageList("no-enough-item");


	public static final ConfigMessageList ITEM_SOLD_LIMIT = new ConfigMessageList(
			"item-sold-limit", new String[0], new String[]{
			"%(amount)", "%(limit)"
	});

	public static final ConfigMessageList WRONG_NUMBER = new ConfigMessageList("wrong-number");
}
