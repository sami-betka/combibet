package combibet.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Gambler {
	
	@Id
	private Long id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	
	
}
