package cc.carm.plugin.ultradepository.manager;


import cc.carm.lib.easyplugin.configuration.file.FileConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesInitializer;
import cc.carm.plugin.ultradepository.UltraDepository;
import cc.carm.plugin.ultradepository.configuration.PluginMessages;

public class ConfigManager {

    private static FileConfig pluginConfiguration;
    private static MessagesConfig messageConfiguration;

    public static boolean initialize() {
        try {
            pluginConfiguration = new FileConfig(UltraDepository.getInstance(), "config.yml");
            messageConfiguration = new MessagesConfig(UltraDepository.getInstance(), "messages.yml");

            FileConfig.pluginConfiguration = () -> pluginConfiguration;
            FileConfig.messageConfiguration = () -> messageConfiguration;

            MessagesInitializer.initialize(messageConfiguration, PluginMessages.class);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static FileConfig getPluginConfig() {
        return pluginConfiguration;
    }

    public static MessagesConfig getMessageConfig() {
        return messageConfiguration;
    }

    public static void reload() {
        try {
            getPluginConfig().reload();
            getMessageConfig().reload();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            getPluginConfig().save();
            getMessageConfig().save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
