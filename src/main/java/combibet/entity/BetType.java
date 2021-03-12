package combibet.entity;

public enum BetType {
	
	HORSERACE("Courses hippiques"),
	FOOTBALL("Football");
	
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
