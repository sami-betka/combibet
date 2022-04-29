package combibet.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
//@Data
@SequenceGenerator(
		  name = "RULE_SEQ_GENERATOR",
		  sequenceName = "RULE_SEQ",
		  initialValue = 1, allocationSize = 1)
public class Rule {

	public Rule() {
		super();
	}

	public Rule(Long id, String title, String description, String gambler) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.gambler = gambler;


	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RULE_SEQ_GENERATOR")
	private Long id;
	
	private String title;
	
	private String description;
	
	private String gambler;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGambler() {
		return gambler;
	}

	public void setGambler(String gambler) {
		this.gambler = gambler;
	}


}
