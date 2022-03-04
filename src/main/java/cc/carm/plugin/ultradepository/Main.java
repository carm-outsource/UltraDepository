package cc.carm.plugin.ultradepository;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.plugin.ultradepository.command.DepositoryCommand;
import cc.carm.plugin.ultradepository.configuration.PluginConfig;
import cc.carm.plugin.ultradepository.hooker.GHUpdateChecker;
import cc.carm.plugin.ultradepository.hooker.PAPIExpansion;
import cc.carm.plugin.ultradepository.listener.CollectListener;
import cc.carm.plugin.ultradepository.listener.UserListener;
import cc.carm.plugin.ultradepository.manager.ConfigManager;
import cc.carm.plugin.ultradepository.manager.DepositoryManager;
import cc.carm.plugin.ultradepository.manager.EconomyManager;
import cc.carm.plugin.ultradepository.manager.UserManager;
import cc.carm.plugin.ultradepository.storage.DataStorage;
import cc.carm.plugin.ultradepository.storage.StorageMethod;
import cc.carm.plugin.ultradepository.util.JarResourceUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class Main extends EasyPlugin {
    private static Main instance;

    private DataStorage storage;

    private UserManager userManager;
    private EconomyManager economyManager;
    private DepositoryManager depositoryManager;

    public Main() {
        super(new EasyPluginMessageProvider.zh_CN());
    }

    @Override
    protected void load() {
        instance = this;
    }

    @Override
    protected boolean initialize() {

        log("加载配置文件...");
        if (!ConfigManager.initialize()) {
            log("初始化配置文件失败，放弃加载。");
            return false;
        }

        log("初始化存储方式...");
        StorageMethod storageMethod = PluginConfig.STORAGE_METHOD.getOptional().orElse(StorageMethod.YAML);
        log("	正在使用 " + storageMethod.name() + " 进行数据存储");

        this.storage = storageMethod.createStorage();
        if (!storage.initialize()) {
            error("初始化存储失败，请检查配置文件。");
            storage.shutdown();
            return false;
        }

        log("加载经济系统...");
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            this.economyManager = new EconomyManager();
            if (!economyManager.initialize()) {
                error("经济系统初始化失败，关闭出售功能。");
            }
        } else {
            log("	&7[-] 检测到未安装Vault，关闭出售功能。");
        }

        log("加载仓库管理器...");
        this.depositoryManager = new DepositoryManager();
        getDepositoryManager().loadDepositories();

        log("加载用户系统...");
        this.userManager = new UserManager();


        log("注册监听器...");
        regListener(new UserListener());
        regListener(new CollectListener());
        GUI.initialize(this);

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

        if (PluginConfig.CHECK_UPDATE.get()) {
            log("开始检查更新...");
            GHUpdateChecker checker = new GHUpdateChecker(getLogger(), "CarmJos", "UltraDepository");
            getScheduler().runAsync(() -> checker.checkUpdate(getDescription().getVersion()));
        } else {
            log("已禁用检查更新，跳过。");
        }

        getUserManager().loadPlayersData();
        return true;
    }

    @Override
    protected void shutdown() {
        if (!isInitialized()) return;

        log("保存现有用户数据...");
        getUserManager().saveAll();
        getUserManager().getDataCache().clear();

        log("释放存储源...");
        getStorage().shutdown();

        log("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);

    }

    protected DataStorage getStorage() {
        return storage;
    }

    public static Main getInstance() {
        return instance;
    }

    protected UserManager getUserManager() {
        return userManager;
    }

    protected EconomyManager getEconomyManager() {
        return economyManager;
    }

    protected DepositoryManager getDepositoryManager() {
        return depositoryManager;
    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.get();
    }

    @Override
    public void outputInfo() {
        Optional.ofNullable(JarResourceUtils.readResource(this.getResource("PLUGIN_INFO"))).ifPresent(this::log);
    }

}
