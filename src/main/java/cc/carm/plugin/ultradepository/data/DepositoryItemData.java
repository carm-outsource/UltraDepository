package cc.carm.plugin.ultradepository.data;

import cc.carm.plugin.ultradepository.configuration.depository.DepositoryItem;

public class DepositoryItemData {

	final DepositoryItem source;
	final DepositoryData owner;

	int amount;
	int sold;

	public DepositoryItemData(DepositoryItem source, DepositoryData owner,
							  int amount, int sold) {
		this.owner = owner;
		this.source = source;
		this.amount = amount;
		this.sold = sold;
	}

	public DepositoryData getOwner() {
		return owner;
	}

	public DepositoryItem getSource() {
		return source;
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

	public int[] applyChanges(int amountChanges, int soldChanges) {
		setAmount(getAmount() + amountChanges);
		setSold(getSold() + soldChanges);
		return new int[]{getAmount(), getSold()};
	}

	public void clearSold() {
		this.sold = 0;
	}

	public static DepositoryItemData emptyItemData(DepositoryItem source, DepositoryData owner) {
		return new DepositoryItemData(source, owner, 0, 0);
	}

	@Override
	public String toString() {
		return "UBItemData{" +
				"amount=" + amount +
				", sold=" + sold +
				'}';
	}


}
