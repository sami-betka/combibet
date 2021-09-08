package combibet.entity;

public enum ConfidenceIndex {
	

	FIVE("5/10"),
	SIX("6/10"),
	SEVEN("7/10"),
	EIGHT("8/10"),
	NINE("9/10"),
	TEN("10/10"),
	NON_RENSEIGNE("Non-renseigné");

	
	private String name;

	private ConfidenceIndex(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
