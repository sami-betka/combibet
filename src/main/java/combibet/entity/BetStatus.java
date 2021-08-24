package combibet.entity;

public enum BetStatus {

	WON("Gagnant"),
	LOSE("Perdant"),
	PENDING("En attente"),
	SEMI("Semi-perdant"),
	NOT_PLAYED_WON("Gagnant-non-joué"),
	NOT_PLAYED_LOSE("Perdant-non-joué");	

	
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
