package combibet.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
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

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startDate;
	
	private String formattedStartDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endDate;

	private double startAmount;
	
//	private double currentAmount;

	@ManyToOne
	private Gambler gambler;

	@OneToMany(cascade = CascadeType.REMOVE)
	private List<Combi> bets;
	
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
			
			if (bet.getStatus() == BetStatus.WON) {
				benefitAmount += (bet.getOdd() * bet.getAnte()) - bet.getAnte();
			}
			if (bet.getStatus() == BetStatus.LOSE) {
				benefitAmount -= bet.getAnte();
			}
		}
	
		
		return benefitAmount;
	}
	
	
}
