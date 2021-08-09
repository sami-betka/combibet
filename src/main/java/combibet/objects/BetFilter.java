package combibet.objects;

import combibet.entity.BetType;

public class BetFilter {

	private Double bankrollAmount;
	private int divider;
	private BetType type;
	
	
	public BetFilter(Double bankrollAmount, int divider, BetType type) {
		super();
		this.bankrollAmount = bankrollAmount;
		this.divider = divider;
		this.type = type;
	}
	public BetFilter() {
		super();
	}
	public Double getBankrollAmount() {
		return bankrollAmount;
	}
	public void setBankrollAmount(Double bankrollAmount) {
		this.bankrollAmount = bankrollAmount;
	}
	public int getDivider() {
		return divider;
	}
	public void setDivider(int divider) {
		this.divider = divider;
	}
	public BetType getType() {
		return type;
	}
	public void setType(BetType type) {
		this.type = type;
	}
}
