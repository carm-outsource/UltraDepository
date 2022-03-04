package cc.carm.plugin.ultradepository.manager;


import cc.carm.lib.easyplugin.configuration.file.FileConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesInitializer;
import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;
import cc.carm.plugin.ultradepository.util.JarUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfig pluginConfiguration;
    private MessagesConfig messageConfiguration;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean initialize() {

        try {
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                //没找到配置文件，可能是第一次加载此插件
                //把一些英文版的东西复制出来，方便英文用户使用。
                UltraDepository.getInstance().log("	未找到配置文件，生成默认配置...");
                JarUtil.copyFolderFromJar(
                        "i18n", plugin.getDataFolder(),
                        JarUtil.CopyOption.COPY_IF_NOT_EXIST
                );

            }

            pluginConfiguration = new FileConfig(plugin, "config.yml");
            messageConfiguration = new MessagesConfig(plugin, "messages.yml");

            FileConfig.pluginConfiguration = () -> pluginConfiguration;
            FileConfig.messageConfiguration = () -> messageConfiguration;

            MessagesInitializer.initialize(messageConfiguration, PluginMessages.class);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public FileConfig getPluginConfig() {
        return pluginConfiguration;
    }

    public MessagesConfig getMessageConfig() {
        return messageConfiguration;
    }

    public void reload() {
        try {
            getPluginConfig().reload();
            getMessageConfig().reload();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            getPluginConfig().save();
            getMessageConfig().save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
