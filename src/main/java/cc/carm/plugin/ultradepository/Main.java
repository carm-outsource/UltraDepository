package cc.carm.plugin.ultradepository;

import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.hooker.PAPIExpansion;
import cc.carm.plugin.ultradepository.listener.CollectListener;
import cc.carm.plugin.ultradepository.listener.UserListener;
import cc.carm.plugin.ultradepository.manager.DepositoryManager;
import cc.carm.plugin.ultradepository.manager.ConfigManager;
import cc.carm.plugin.ultradepository.manager.EconomyManager;
import cc.carm.plugin.ultradepository.manager.UserManager;
import cc.carm.plugin.ultradepository.storage.DataStorage;
import cc.carm.plugin.ultradepository.storage.FileStorage;
import cc.carm.plugin.ultradepository.storage.MySQLStorage;
import cc.carm.plugin.ultradepository.util.ColorParser;
import cc.carm.plugin.ultradepository.util.MessageUtil;
import cc.carm.plugin.ultradepository.util.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main extends JavaPlugin {

	private static Main instance;
	private static SchedulerUtils scheduler;

	private static DataStorage storage;

	private static UserManager userManager;
	private static EconomyManager economyManager;
	private static DepositoryManager depositoryManager;

	@Override
	public void onEnable() {
		instance = this;
		scheduler = new SchedulerUtils(this);
		log(getName() + " " + getDescription().getVersion() + " &7开始加载...");
		long startTime = System.currentTimeMillis();

		log("加载配置文件...");
		ConfigManager.initConfig();

		log("初始化存储方式...");
		if (PluginConfig.STORAGE_METHOD.get().equalsIgnoreCase("mysql")) {
			log("	正在使用 MySQL 进行数据存储");
			storage = new MySQLStorage();
		} else {
			log("	正在使用 文件 进行数据存储");
			storage = new FileStorage();
		}

		if (!storage.initialize()) {
			error("存储初始化失败，请检查配置文件。");
			setEnabled(false);
			return;
		}

		log("加载用户系统...");
		userManager = new UserManager();

		log("加载经济系统...");
		if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
			economyManager = new EconomyManager();
			if (!economyManager.initialize()) {
				error("经济系统初始化失败，关闭出售功能。");
			}
		} else {
			log("	&7[-] 检测到未安装Vault，关闭出售功能。");
		}

		log("加载背包管理器...");
		depositoryManager = new DepositoryManager();


		log("注册监听器...");
		regListener(new UserListener());
		regListener(new CollectListener());

		log("注册指令...");


		if (MessageUtil.hasPlaceholderAPI()) {
			log("注册变量...");
			new PAPIExpansion(this).register();
		} else {
			log("检测到未安装PlaceholderAPI，跳过变量注册。");
		}

		log("加载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

	}

	@Override
	public void onDisable() {
		log(getName() + " " + getDescription().getVersion() + " 开始卸载...");
		long startTime = System.currentTimeMillis();

		log("保存现有用户数据...");


		log("释放存储源...");
		getStorage().shutdown();

		log("卸载监听器...");
		Bukkit.getServicesManager().unregisterAll(this);

		log("卸载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");
	}

	public static DataStorage getStorage() {
		return storage;
	}

	public static SchedulerUtils getScheduler() {
		return scheduler;
	}

	public static UserManager getUserManager() {
		return userManager;
	}

	public static EconomyManager getEconomyManager() {
		return economyManager;
	}

	public static DepositoryManager getDepositoryManager() {
		return depositoryManager;
	}

	/**
	 * 注册监听器
	 *
	 * @param listener 监听器
	 */
	public static void regListener(@NotNull Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, getInstance());
	}

	public static void log(@Nullable String message) {
		Bukkit.getConsoleSender().sendMessage(ColorParser.parse("[" + getInstance().getName() + "] " + message));
	}

	public static void error(String message) {
		log("&c[ERROR] &r" + message);
	}

	public static void debug(@Nullable String message) {
		if (PluginConfig.DEBUG.get()) log("[DEBUG] " + message);
	}

	public static Main getInstance() {
		return instance;
	}

	public static void registerCommand(String commandName,
									   @NotNull CommandExecutor executor) {
		registerCommand(commandName, executor, null);
	}

	public static void registerCommand(String commandName,
									   @NotNull CommandExecutor executor,
									   @Nullable TabCompleter tabCompleter) {
		PluginCommand command = Bukkit.getPluginCommand(commandName);
		if (command == null) return;
		command.setExecutor(executor);
		if (tabCompleter != null) command.setTabCompleter(tabCompleter);
	}
}
