package cc.carm.plugin.ultrabackpack.api.data;

public class UBItemData {

	int amount;
	int sold;

	public UBItemData(int amount, int sold) {
		this.amount = amount;
		this.sold = sold;
	}

	public int getAmount() {
		return amount;
	}

	public int getSold() {
		return sold;
	}

	public void clearSold() {
		this.sold = 0;
	}

	public static UBItemData emptyItemData() {
		return new UBItemData(0, 0);
	}

}
