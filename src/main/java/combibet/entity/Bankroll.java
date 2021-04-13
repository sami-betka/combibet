package combibet.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Bankroll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private double startAmount;
	
//	private double currentAmount;

	@ManyToOne
	private Gambler gambler;

	@OneToMany
	private List<Bet> bets;
	
	private boolean isActive;
	
	
	
	
	public double calculateCurrentAmount() {

		return this.startAmount + benefit();
	}
	
	public int betNumber() {
		
		return this.bets.size();
	}
	
	public double benefit () {
		
		double benefitAmount = 0;
		
		for(Bet bet : this.bets){
			
			if (bet.getStatus().equals(BetStatus.WON)) {
				benefitAmount += (bet.getOdd() * bet.getAnte()) - bet.getAnte();
			}
			if (bet.getStatus().equals(BetStatus.LOSE)) {
				benefitAmount -= bet.getAnte();
			}
		}
		
		return benefitAmount;
	}
	
	
}
