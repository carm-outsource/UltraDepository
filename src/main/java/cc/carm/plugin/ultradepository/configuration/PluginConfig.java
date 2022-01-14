package cc.carm.plugin.ultradepository.configuration;

import cc.carm.lib.easyplugin.configuration.cast.ConfigStringCast;
import cc.carm.lib.easyplugin.configuration.impl.ConfigItem;
import cc.carm.lib.easyplugin.configuration.impl.ConfigSound;
import cc.carm.lib.easyplugin.configuration.message.ConfigMessage;
import cc.carm.lib.easyplugin.configuration.message.ConfigMessageList;
import cc.carm.lib.easyplugin.configuration.values.ConfigValue;
import cc.carm.plugin.ultradepository.manager.ConfigManager;
import cc.carm.plugin.ultradepository.storage.StorageMethod;
import org.bukkit.Material;
import org.bukkit.Sound;

public class PluginConfig {

	public static final ConfigValue<Boolean> DEBUG = new ConfigValue<>(
			"debug", Boolean.class, false
	);

	public static final ConfigValue<Boolean> METRICS = new ConfigValue<>(
			"metrics", Boolean.class, true
	);

	public static final ConfigStringCast<StorageMethod> STORAGE_METHOD = new ConfigStringCast<>(
			"storage.method", StorageMethod::read, StorageMethod.YAML
	);

	/**
	 * 收集配置
	 */
	public static class Collect {

		public static final ConfigValue<Boolean> PICKUP = new ConfigValue<>(
				"collect.pickup", Boolean.class, true
		);

		public static final ConfigValue<Boolean> KILL = new ConfigValue<>(
				"collect.kill", Boolean.class, true
		);

		public static final ConfigValue<Boolean> BREAK = new ConfigValue<>(
				"collect.break", Boolean.class, true
		);

	}

	public static class Sounds {

		public static final ConfigSound COLLECT = new ConfigSound("sounds.collect", Sound.ENTITY_VILLAGER_CELEBRATE);
		public static final ConfigSound SELL_SUCCESS = new ConfigSound("sounds.sell-success");
		public static final ConfigSound SELL_FAIL = new ConfigSound("sounds.sell-fail");
		public static final ConfigSound GUI_CLICK = new ConfigSound("sounds.gui-click");
	}

	/**
	 * 通用配置
	 */
	public static class General {
		/**
		 * 针对每一件物品的额外介绍
		 * 将添加到背包界面内的物品上，避免重复配置
		 */
		public static final ConfigMessageList ADDITIONAL_LORE = new ConfigMessageList(
				ConfigManager::getPluginConfig, "general.additional-lore", new String[]{},
				new String[]{
						"%(item_name)", "%(amount)", "%(price)", "%(sold)", "%(remain)", "%(limit)"
				});

		/**
		 * 提示玩家点击行为的介绍
		 * 将添加到背包界面内的物品上，避免重复配置
		 */
		public static final ConfigMessageList CLICK_LORE = new ConfigMessageList(
				ConfigManager::getPluginConfig,
				"general.click-lore",
				new String[]{}, new String[]{
				"%(item_name)", "%(amount)", "%(price)"
		});

		/**
		 * 售出界面的配置
		 */
		public static class SellGUI {


			public static final ConfigMessage TITLE = new ConfigMessage(
					ConfigManager::getPluginConfig,
					"general.sell-gui.title",
					"&a&l出售", new String[]{
					"%(item_name)", "%(backpack_name)"
			}
			);

			public static class Items {

				public static final ConfigItem ADD = new ConfigItem(
						"general.sell-gui.items.add",
						new String[]{"%(item_name)", "%(amount)"},
						ConfigItem.create(Material.GREEN_STAINED_GLASS_PANE, "&a添加物品 %(amount) 个")
				);

				public static final ConfigItem REMOVE = new ConfigItem(
						"general.sell-gui.items.remove",
						new String[]{"%(item_name)", "%(amount)"},
						ConfigItem.create(Material.RED_STAINED_GLASS_PANE, "&c減少物品 %(amount) 个")
				);

				public static final ConfigItem CONFIRM = new ConfigItem(
						"general.sell-gui.items.confirm",
						new String[]{"%(item_name)", "%(amount)", "%(money)"},
						ConfigItem.create(Material.EMERALD, "&2确认售出")
				);

				public static final ConfigItem CANCEL = new ConfigItem(
						"general.sell-gui.items.cancel",
						ConfigItem.create(Material.REDSTONE, "&4取消售出")
				);

			}

		}

	}
}
