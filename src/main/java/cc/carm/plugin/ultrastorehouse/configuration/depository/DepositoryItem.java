package cc.carm.plugin.ultrastorehouse.configuration.depository;

import cc.carm.plugin.ultrastorehouse.util.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

	public ItemStack getDisplayItem() {
		ItemStackFactory factory = new ItemStackFactory(getMaterial(), 1, getData());
		if (getName() != null) factory.setDisplayName(getName());
		if (getLore() != null) factory.setLore(getLore());
		return factory.toItemStack();
	}

}
