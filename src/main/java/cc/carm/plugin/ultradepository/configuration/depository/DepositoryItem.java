package cc.carm.plugin.ultradepository.configuration.depository;

import cc.carm.plugin.ultradepository.util.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class DepositoryItem {

	final @NotNull Material material;
	final int data;

	int slot;

	double price;
	int limit;

	@Nullable String name;
	@Nullable List<String> lore;

	public DepositoryItem(@NotNull Material material, int data,
						  int slot, int price, int limit,
						  @Nullable String name, @Nullable List<String> lore) {
		this.material = material;
		this.data = data;
		this.slot = slot;
		this.price = price;
		this.limit = limit;
		this.name = name;
		this.lore = lore;
	}

	public @NotNull String getTypeID() {
		return getMaterial().name() + (getData() != 0 ? ":" + getData() : "");
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
}
