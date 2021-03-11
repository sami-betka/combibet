package combibet.entity;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Gambler {
	
	private Long id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	
	
}
