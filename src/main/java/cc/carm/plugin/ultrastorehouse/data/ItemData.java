package cc.carm.plugin.ultrastorehouse.data;

public class ItemData {

	int amount;
	int sold;

	public ItemData(int amount, int sold) {
		this.amount = amount;
		this.sold = sold;
	}

	public int getAmount() {
		return amount;
	}

	public int getSold() {
		return sold;
	}

	public void setAmount(int amount) {
		this.amount = Math.max(0, amount);
	}

	public void setSold(int sold) {
		this.sold = Math.max(0, sold);
	}

	public void clearSold() {
		this.sold = 0;
	}

	public static ItemData emptyItemData() {
		return new ItemData(0, 0);
	}

	@Override
	public String toString() {
		return "UBItemData{" +
				"amount=" + amount +
				", sold=" + sold +
				'}';
	}
}
