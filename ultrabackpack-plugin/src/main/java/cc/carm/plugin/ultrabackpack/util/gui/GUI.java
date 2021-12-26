package cc.carm.plugin.ultrabackpack.util.gui;

import cc.carm.plugin.ultrabackpack.Main;
import cc.carm.plugin.ultrabackpack.api.util.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class GUI {

	private static final HashMap<Player, GUI> openedGUIs = new HashMap<>();

	GUIType type;
	String name;
	public GUIItem[] items;
	public Inventory inv;

	boolean cancelOnTarget = true;
	boolean cancelOnSelf = true;
	boolean cancelOnOuter = true;

	Map<String, Object> flags;

	public GUIListener listener;

	public GUI(GUIType type, String name) {
		this.type = type;
		this.name = ColorParser.parse(name);
		switch (type) {
			case ONE_BY_NINE:
				this.items = new GUIItem[9];
				break;
			case TWO_BY_NINE:
				this.items = new GUIItem[18];
				break;
			case THREE_BY_NINE:
				this.items = new GUIItem[27];
				break;
			case FOUR_BY_NINE:
				this.items = new GUIItem[36];
				break;
			case FIVE_BY_NINE:
				this.items = new GUIItem[45];
				break;
			case SIX_BY_NINE:
				this.items = new GUIItem[54];
				break;
			case CANCEL:
			default:
				this.items = null;
		}
	}


	public final void setItem(int index, GUIItem item) {
		if (item == null) {
			this.items[index] = new GUIItem(new ItemStack(Material.AIR));
		} else {
			this.items[index] = item;
		}
	}

	/**
	 * 批量添加GUI Item
	 *
	 * @param item  物品
	 * @param index 对应格
	 */
	public void setItem(GUIItem item, int... index) {
		Arrays.stream(index).forEach(i -> setItem(i, item));
	}

	public void setItem(GUIItem item, int start, int end) {
		IntStream.rangeClosed(start, end).forEach(i -> setItem(i, item));
	}

	public GUIItem getItem(int index) {
		return this.items[index];
	}

	/**
	 * 更新玩家箱子的视图
	 */
	public void updateView() {
		if (this.inv != null) {
			List<HumanEntity> viewers = this.inv.getViewers();
			IntStream.range(0, this.items.length).forEach(index -> {
				GUIItem item = items[index];
				if (item == null) {
					inv.setItem(index, new ItemStack(Material.AIR));
				} else {
					inv.setItem(index, items[index].display);
				}
			});

			for (HumanEntity p : viewers) {
				((Player) p).updateInventory();
			}
		}
	}

	/**
	 * 设置是否取消点击GUI内物品的事件
	 * 如果不取消，玩家可以从GUI中拿取物品。
	 *
	 * @param b 是否取消
	 */
	public void setCancelledIfClickOnTarget(boolean b) {
		this.cancelOnTarget = b;
	}

	/**
	 * 设置是否取消点击自己背包内物品的事件
	 * 如果不取消，玩家可以从自己的背包中拿取物品。
	 *
	 * @param b 是否取消
	 */
	public void setCancelledIfClickOnSelf(boolean b) {
		this.cancelOnSelf = b;
	}

	/**
	 * 设置是否取消点击GUI外的事件
	 * 如果不取消，玩家可以把物品从GUI或背包中丢出去
	 *
	 * @param b 是否取消
	 */
	public void setCancelledIfClickOnOuter(boolean b) {
		this.cancelOnOuter = b;
	}

	public void addFlag(String flag, Object obj) {
		if (this.flags == null) this.flags = new HashMap<>();
		this.flags.put(flag, obj);
	}

	public Object getFlag(String flag) {
		if (this.flags == null) return null;
		else
			return this.flags.get(flag);
	}

	public void setFlag(String flag, Object obj) {
		if (this.flags == null) this.flags = new HashMap<>();
		this.flags.replace(flag, obj);
	}

	public void removeFlag(String flag) {
		if (this.flags == null) this.flags = new HashMap<>();
		this.flags.remove(flag);
	}

	public void rawClickListener(InventoryClickEvent event) {
	}

	public void openGUI(Player player) {
		Inventory inv;
		if (this.type == GUIType.CANCEL) {
			throw new NullPointerException("被取消或不存在的GUI");
		}
		inv = Bukkit.createInventory(null, this.items.length, this.name);

		for (int index = 0; index < this.items.length; index++) {
			if (items[index] == null) {
				inv.setItem(index, new ItemStack(Material.AIR));
			} else {
				inv.setItem(index, items[index].display);
			}
		}
		setOpenedGUI(player, this);
		this.inv = inv;
		player.openInventory(inv);

		if (listener == null) {
			Main.regListener(listener = new GUIListener(this, player));
		}

	}

	/**
	 * 拖动GUI内物品时执行的代码
	 *
	 * @param event InventoryDragEvent
	 */
	public void onDrag(InventoryDragEvent event) {
	}

	/**
	 * 关闭GUI时执行的代码
	 */
	public void onClose() {
	}


	public static void setOpenedGUI(Player player, GUI gui) {
		openedGUIs.put(player, gui);
	}

	public static boolean hasOpenedGUI(Player player) {
		return openedGUIs.containsKey(player);
	}

	public static GUI getOpenedGUI(Player player) {
		return openedGUIs.get(player);
	}

	public static void removeOpenedGUI(Player player) {
		openedGUIs.remove(player);
	}

}
