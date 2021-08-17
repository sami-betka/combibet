package combibet.entity;

public enum BankrollField {

	HIPPIQUE("hippique"),
	SPORTIF("sport");
	
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
