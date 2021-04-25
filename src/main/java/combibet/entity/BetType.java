package combibet.entity;

public enum BetType {
	
	SIMPLE_GAGNANT("Simple gagnant"),
	SIMPLE_PLACE("Simple placé"),
	COUPLE_GAGNANT("Couplé gagnant"),
	COUPLE_PLACE("Couplé placé"),
	COUPLE_ORDRE("Couplé ordre"),
	TRIO("Trio"),
	TRIO_ORDRE("Trio ordre"),
	TIERCE("Tiercé"),
	DEUX_SUR_QUATRE("2sur4"),
	MULTI("Multi"),
	
	PARI_SPORTIF("");
	
	private String name; 
	
	private BetType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
