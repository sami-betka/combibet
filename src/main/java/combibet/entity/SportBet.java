package combibet.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@SequenceGenerator(
		  name = "SPORT_BET_SEQ_GENERATOR",
		  sequenceName = "SPORT_BET_SEQ",
		  initialValue = 1, allocationSize = 1)
public class SportBet{

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPORT_BET_SEQ_GENERATOR")
	Long id;

	
}
