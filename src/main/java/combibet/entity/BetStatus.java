package combibet.entity;

public enum BetStatus {

	WON("Gagnant"),
	LOSE("Perdant"),
	PENDING("En attente");
	
	private String name;

	private BetStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
