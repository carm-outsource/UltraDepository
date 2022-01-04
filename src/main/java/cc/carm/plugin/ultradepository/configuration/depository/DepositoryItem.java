package cc.carm.plugin.ultradepository.configuration.depository;

import cc.carm.lib.easyplugin.utils.ItemStackFactory;
import cc.carm.plugin.ultradepository.UltraDepository;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class DepositoryItem {

	final Depository depository;

	final @NotNull Material material;
	final int data;

	int slot;

	double price;
	int limit;

	@Nullable String name;
	@Nullable List<String> lore;

	public DepositoryItem(@NotNull Depository depository,
						  @NotNull Material material, int data,
						  int slot, double price, int limit,
						  @Nullable String name, @Nullable List<String> lore) {
		this.depository = depository;
		this.material = material;
		this.data = data;
		this.slot = slot;
		this.price = price;
		this.limit = limit;
		this.name = name;
		this.lore = lore;
	}

	public Depository getDepository() {
		return depository;
	}

	public @NotNull String getTypeID() {
		return UltraDepository.getDepositoryManager().getItemTypeID(getMaterial(), getData());
	}

	public @NotNull Material getMaterial() {
		return material;
	}

	public int getData() {
		return data;
	}

	public int getSlot() {
		return slot;
	}

	public double getPrice() {
		return price;
	}

	public int getLimit() {
		return limit;
	}

	public @Nullable String getName() {
		return name;
	}

	public @Nullable List<String> getLore() {
		return lore;
	}

	public ItemStack getRawItem(int amount) {
		return new ItemStack(getMaterial(), amount, (short) getData());
	}

	public ItemStack getDisplayItem() {
		ItemStackFactory factory = new ItemStackFactory(getRawItem(1));
		if (getName() != null) factory.setDisplayName(getName());
		if (getLore() != null) factory.setLore(getLore());
		return factory.toItemStack();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DepositoryItem that = (DepositoryItem) o;
		return data == that.data && material == that.material;
	}

	@Override
	public int hashCode() {
		return Objects.hash(material, data);
	}

	public static DepositoryItem readFrom(Depository depository, String typeID, ConfigurationSection section) {
		try {
			Material material;
			int data = 0;
			if (typeID.contains(":")) {
				String[] args = typeID.split(":");
				material = Material.matchMaterial(args[0]);
				data = Integer.parseInt(args[1]);
			} else {
				material = Material.matchMaterial(typeID);
			}

			if (material == null) throw new NullPointerException(typeID);
			return new DepositoryItem(
					depository, material, data,
					section.getInt("slot", 0),
					section.getDouble("price", 0),
					section.getInt("limit", 0),
					section.getString("name", material.name()),
					section.getStringList("lore")
			);

		} catch (Exception ex) {
			UltraDepository.getInstance().error("没有与 " + typeID + " 匹配的物品！");
			UltraDepository.getInstance().error("No match material of " + typeID + " !");
			return null;
		}
	}
}
