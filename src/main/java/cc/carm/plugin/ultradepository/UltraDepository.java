package cc.carm.plugin.ultradepository;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import cc.carm.lib.easyplugin.utils.MessageUtils;
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
import cc.carm.plugin.ultradepository.storage.StorageMethod;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class UltraDepository extends EasyPlugin {

	private static UltraDepository instance;

	private static DataStorage storage;

	private static UserManager userManager;
	private static EconomyManager economyManager;
	private static DepositoryManager depositoryManager;

	public UltraDepository() {
		super(new EasyPluginMessageProvider.zh_CN());
	}

	@Override
	public void load() {
		instance = this;

		log("加载配置文件...");
		ConfigManager.initConfig();

		GUI.initialize(this);
	}

	@Override
	public boolean initialize() {

		log("初始化存储方式...");
		StorageMethod storageMethod = PluginConfig.STORAGE_METHOD.get();
		if (storageMethod == null) {
			log("初始化存储方式失败，放弃加载");
			return false;
		}

		storage = storageMethod.createStorage();
		log("	正在使用 " + storageMethod.name() + " 进行数据存储");

		if (!storage.initialize()) {
			error("存储初始化失败，请检查配置文件。");
			return false;
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
		registerCommand("UltraDepository", new DepositoryCommand());

		if (MessageUtils.hasPlaceholderAPI()) {
			log("注册变量...");
			new PAPIExpansion(this).register();
		} else {
			log("检测到未安装PlaceholderAPI，跳过变量注册。");
		}

		if (PluginConfig.METRICS.get()) {
			log("启用统计数据...");
			Metrics metrics = new Metrics(this, 13777);
			metrics.addCustomChart(new SingleLineChart(
					"active_depositories",
					() -> getDepositoryManager().getDepositories().size())
			);
			metrics.addCustomChart(new SimplePie("storage_method", storageMethod::name));
			metrics.addCustomChart(new SimplePie("economy_enabled", () -> economyManager.isInitialized() ? "YES" : "NO"));
			metrics.addCustomChart(new SimplePie("papi_version", () -> {
				Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
				if (plugin == null) return "Not installed";
				else return plugin.getDescription().getVersion();
			}));
		}
		return true;
	}

	@Override
	public void shutdown() {
		if (!isInitialized()) return;

		log("保存现有用户数据...");
		getUserManager().saveAll();
		getUserManager().getDataCache().clear();

		log("释放存储源...");
		getStorage().shutdown();

		log("卸载监听器...");
		Bukkit.getServicesManager().unregisterAll(this);
	}

	public static DataStorage getStorage() {
		return storage;
	}

	public static UltraDepository getInstance() {
		return instance;
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

	@Override
	public boolean isDebugging() {
		return PluginConfig.DEBUG.get();
	}

	@Override
	public void outputInfo() {
		log(" ",
				"&6 _    _ _ _            &e  _____                       _ _                   ",
				"&6| |  | | | |            &e|  __ \\                     (_) |                  ",
				"&6| |  | | | |_ _ __ __ _ &e| |  | | ___ _ __   ___  ___ _| |_ ___  _ __ _   _ ",
				"&6| |  | | | __| '__/ _` |&e| |  | |/ _ \\ '_ \\ / _ \\/ __| | __/ _ \\| '__| | | |",
				"&6| |__| | | |_| | | (_| |&e| |__| |  __/ |_) | (_) \\__ \\ | || (_) | |  | |_| |",
				"&6 \\____/|_|\\__|_|  \\__,_|&e|_____/ \\___| .__/ \\___/|___/_|\\__\\___/|_|   \\__, |",
				"&6                                    &e| |                               __/ |",
				"&6                                    &e|_|                              |___/ ",
				"&f请访问项目主页查看详细插件介绍 &8/ &fView GitHub to get more information",
				"&8-> &6https://github.com/CarmJos/UltraDepository",
				" "
		);
	}
}
