package combibet.entity;

public enum BankrollField {

	PARI_HIPPIQUE("Pari-hippique"),
	PARI_SPORTIF("Pari-sportif");
	
	private String name; 
	
	private BankrollField(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
