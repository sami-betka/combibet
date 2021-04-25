package combibet.entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorColumn(name="sb")
@SequenceGenerator(
		  name = "SPORT_BET_SEQ_GENERATOR",
		  sequenceName = "SPORT_BET_SEQ",
		  initialValue = 1, allocationSize = 1)
public class SportBet extends Bet{

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPORT_BET_SEQ_GENERATOR")
//	Long id;
	
	
}
