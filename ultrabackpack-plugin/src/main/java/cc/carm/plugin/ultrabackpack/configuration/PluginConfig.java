package cc.carm.plugin.ultrabackpack.configuration;

import cc.carm.plugin.ultrabackpack.api.util.ItemStackFactory;
import cc.carm.plugin.ultrabackpack.configuration.values.ConfigSectionCast;
import cc.carm.plugin.ultrabackpack.configuration.values.ConfigValue;
import cc.carm.plugin.ultrabackpack.configuration.values.ConfigValueList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PluginConfig {

	public static final ConfigValue<Boolean> DEBUG = new ConfigValue<>(
			"debug", Boolean.class
	);

	public static final ConfigValue<String> STORAGE_METHOD = new ConfigValue<>(
			"storage.method", String.class
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

	/**
	 * 通用配置
	 */
	public static class General {
		/**
		 * 针对每一件物品的额外介绍
		 * 将添加到背包界面内的物品上，避免重复配置
		 */
		public static final ConfigValueList<String> ADDITIONAL_LORE = new ConfigValueList<>(
				"general.additional-lore", String.class
		);

		/**
		 * 提示玩家点击行为的介绍
		 * 将添加到背包界面内的物品上，避免重复配置
		 */
		public static final ConfigValueList<String> CLICK_LORE = new ConfigValueList<>(
				"general.click-lore", String.class
		);

		/**
		 * 售出界面的配置
		 */
		public static class SellGUI {


			public static final ConfigValue<String> TITLE = new ConfigValue<>(
					"general.sell-gui.title", String.class
			);

			public static class Items {

				public static final ConfigSectionCast<ItemStack> ADD = new ConfigSectionCast<>(
						"general.sell-gui.items.add",
						section -> {
							ItemStackFactory factory = new ItemStackFactory(
									Material.matchMaterial(section.getString("type", "STONE")),
									1, section.getInt("data", 0)
							);
							String name = section.getString("name");
							List<String> lore = section.getStringList("lore");
							if (name != null) factory.setDisplayName(name);
							if (!lore.isEmpty()) factory.setLore(lore);
							return factory.toItemStack();
						}, new ItemStack(Material.STONE)
				);

				public static final ConfigSectionCast<ItemStack> REMOVE = new ConfigSectionCast<>(
						"general.sell-gui.items.remove",
						section -> {
							ItemStackFactory factory = new ItemStackFactory(
									Material.matchMaterial(section.getString("type", "STONE")),
									1, section.getInt("data", 0)
							);
							String name = section.getString("name");
							List<String> lore = section.getStringList("lore");
							if (name != null) factory.setDisplayName(name);
							if (!lore.isEmpty()) factory.setLore(lore);
							return factory.toItemStack();
						}, new ItemStack(Material.STONE)
				);

				public static final ConfigSectionCast<ItemStack> CONFIRM = new ConfigSectionCast<>(
						"general.sell-gui.items.confirm",
						section -> {
							ItemStackFactory factory = new ItemStackFactory(
									Material.matchMaterial(section.getString("type", "STONE")),
									1, section.getInt("data", 0)
							);
							String name = section.getString("name");
							List<String> lore = section.getStringList("lore");
							if (name != null) factory.setDisplayName(name);
							if (!lore.isEmpty()) factory.setLore(lore);
							return factory.toItemStack();
						}, new ItemStack(Material.STONE)
				);

				public static final ConfigSectionCast<ItemStack> CANCEL = new ConfigSectionCast<>(
						"general.sell-gui.items.cancel",
						section -> {
							ItemStackFactory factory = new ItemStackFactory(
									Material.matchMaterial(section.getString("type", "STONE")),
									1, section.getInt("data", 0)
							);
							String name = section.getString("name");
							List<String> lore = section.getStringList("lore");
							if (name != null) factory.setDisplayName(name);
							if (!lore.isEmpty()) factory.setLore(lore);
							return factory.toItemStack();
						}, new ItemStack(Material.STONE)
				);

			}

		}

	}

}
