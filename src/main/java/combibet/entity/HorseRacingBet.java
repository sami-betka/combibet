package combibet.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("HR")
public class HorseRacingBet extends Bet {

	private int meeting;

	private int race;
	
	private BetType type;

}
