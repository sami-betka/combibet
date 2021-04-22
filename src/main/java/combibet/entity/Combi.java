package combibet.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
//@DiscriminatorValue("C")
public class Combi {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BET_SEQ_GENERATOR")
	private Long id;
		
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startDate;
	
	private String formattedStartDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endDate;
	
	private double ante;
	
	@ManyToOne
	private Gambler gambler;
	
//	@ManyToOne
//	private Bankroll bankroll;
			
	private BetStatus status;
	
	private BetType type;
	
	private String beforeComment;
	
	private String afterComment;
	
	private boolean isCurrent;
	
	@ManyToOne
	@JoinColumn( name="bankroll_id" )
	private Bankroll bankroll;
	    
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "combi")
    private List<HorseRacingBet> bets;
    
    public String formatStartDate() {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = this.startDate;
		String formattedDateTime = dateTime.format(formatter);
//		System.out.println(formattedDateTime);
		
		return formattedDateTime;
    }
    
	public int betNumber() {
		
		return this.bets.size();
	}
	
	public double benefit () {
		
		double benefitAmount = 0;
		
		for(HorseRacingBet bet : this.bets){
			
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
