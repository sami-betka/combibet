package combibet.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Gambler {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userName;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	@OneToMany(mappedBy="gambler")
	private List <HorseRacingBet> bets = new ArrayList<HorseRacingBet>();
	
	@OneToMany(mappedBy="gambler")
	private List <Combi> combis = new ArrayList<Combi>();
	
	
}
