package combibet.entity;

public enum Discipline {

	TROT("Trot"),
	GALOP("Plat"),
	OBSTACLE("Obstacle"),
	NON_RENSEIGNE("Non-renseigné");


	
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
