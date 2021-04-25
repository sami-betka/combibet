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
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
@SequenceGenerator(
		  name = "BANKROLL_SEQ_GENERATOR",
		  sequenceName = "BANKROLL_SEQ",
		  initialValue = 1, allocationSize = 1)
public class Bankroll {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANKROLL_SEQ_GENERATOR")
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

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "bankroll")
	private List<Combi> combis;
	
	private boolean isActive;
	
	
	public double calculateCurrentAmount() {

		return this.startAmount + benefit();
	}
	
	public int betNumber() {
		
		return this.combis.size();
	}
	
	public double benefit () {
		
		double benefitAmount = 0;
		
		for(Combi combi : this.combis){
			
			benefitAmount += combi.benefit();
		
		}
	
		
		return benefitAmount;
	}
	
	
}
