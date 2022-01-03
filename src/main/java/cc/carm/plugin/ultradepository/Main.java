package cc.carm.plugin.ultradepository;

import cc.carm.plugin.ultradepository.command.DepositoryCommand;
import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.hooker.PAPIExpansion;
import cc.carm.plugin.ultradepository.listener.CollectListener;
import cc.carm.plugin.ultradepository.listener.UserListener;
import cc.carm.plugin.ultradepository.manager.ConfigManager;
import cc.carm.plugin.ultradepository.manager.DepositoryManager;
import cc.carm.plugin.ultradepository.manager.EconomyManager;
import cc.carm.plugin.ultradepository.manager.UserManager;
import cc.carm.plugin.ultradepository.storage.DataStorage;
import cc.carm.plugin.ultradepository.storage.YAMLStorage;
import cc.carm.plugin.ultradepository.storage.JSONStorage;
import cc.carm.plugin.ultradepository.storage.MySQLStorage;
import cc.carm.plugin.ultradepository.util.ColorParser;
import cc.carm.plugin.ultradepository.util.MessageUtil;
import cc.carm.plugin.ultradepository.util.SchedulerUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main extends JavaPlugin {

	private static Main instance;
	private static Metrics metrics;
	private static SchedulerUtils scheduler;

	private static DataStorage storage;

	private static UserManager userManager;
	private static EconomyManager economyManager;
	private static DepositoryManager depositoryManager;

	boolean initialized = false;

	@Override
	public void onEnable() {
		instance = this;
		scheduler = new SchedulerUtils(this);
		outputPlugin();
		log(getName() + " " + getDescription().getVersion() + " &7开始加载...");
		long startTime = System.currentTimeMillis();

		log("加载配置文件...");
		ConfigManager.initConfig();

		log("初始化存储方式...");
		if (PluginConfig.STORAGE_METHOD.get().equalsIgnoreCase("mysql")) {
			log("	正在使用 MySQL 进行数据存储");
			storage = new MySQLStorage();
		} else if (PluginConfig.STORAGE_METHOD.get().equalsIgnoreCase("json")) {
			log("	正在使用 JSON 进行数据存储");
			storage = new JSONStorage();
		} else {
			log("	正在使用 YAML 进行数据存储");
			storage = new YAMLStorage();
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

		log("加载仓库管理器...");
		depositoryManager = new DepositoryManager();
		getDepositoryManager().loadDepositories();

		log("注册监听器...");
		regListener(new UserListener());
		regListener(new CollectListener());

		log("注册指令...");
		registerCommand("UltraDepository", new DepositoryCommand(), new DepositoryCommand());

		if (MessageUtil.hasPlaceholderAPI()) {
			log("注册变量...");
			new PAPIExpansion(this).register();
		} else {
			log("检测到未安装PlaceholderAPI，跳过变量注册。");
		}

		if (PluginConfig.METRICS.get()) {
			log("启用统计数据...");
			metrics = new Metrics(this, 13777);
			metrics.addCustomChart(new SingleLineChart(
					"active_depositories",
					() -> getDepositoryManager().getDepositories().size())
			);
			metrics.addCustomChart(new SimplePie("storage_method", () -> getStorage().getClass().getSimpleName()));
			metrics.addCustomChart(new SimplePie("economy_enabled", () -> economyManager.isInitialized() ? "YES" : "NO"));
			metrics.addCustomChart(new SimplePie("papi_version", () -> {
				Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
				if (plugin == null) return "Not installed";
				else return plugin.getDescription().getVersion();
			}));
		}

		initialized = true;
		log("加载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

	}

	@Override
	public void onDisable() {
		if (!initialized) return;
		outputPlugin();
		log(getName() + " " + getDescription().getVersion() + " 开始卸载...");
		long startTime = System.currentTimeMillis();

		log("保存现有用户数据...");
		getUserManager().saveAll();

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

	public static void outputPlugin() {
		log(" _    _ _ _             _____                       _ _                   ");
		log("| |  | | | |           |  __ \\                     (_) |                  ");
		log("| |  | | | |_ _ __ __ _| |  | | ___ _ __   ___  ___ _| |_ ___  _ __ _   _ ");
		log("| |  | | | __| '__/ _` | |  | |/ _ \\ '_ \\ / _ \\/ __| | __/ _ \\| '__| | | |");
		log("| |__| | | |_| | | (_| | |__| |  __/ |_) | (_) \\__ \\ | || (_) | |  | |_| |");
		log(" \\____/|_|\\__|_|  \\__,_|_____/ \\___| .__/ \\___/|___/_|\\__\\___/|_|   \\__, |");
		log("                                   | |                               __/ |");
		log("                                   |_|                              |___/ ");
	}
}
