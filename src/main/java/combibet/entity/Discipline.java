package combibet.entity;

public enum Discipline {

	TROT("Trot"),
	GALOP("Galop");	

	
	private String name;

	private Discipline(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
