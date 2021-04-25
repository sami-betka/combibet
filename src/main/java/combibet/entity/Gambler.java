package combibet.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Entity
@Data
@SequenceGenerator(
		  name = "GAMBLER_SEQ_GENERATOR",
		  sequenceName = "GAMBLER_SEQ",
		  initialValue = 1, allocationSize = 1)
public class Gambler {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GAMBLER_SEQ_GENERATOR")

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
