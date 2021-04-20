package combibet.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("C")
public class Combi extends Bet{
		
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
	private LocalDateTime startDate;
	
	private String formattedStartDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endDate;
	
	private boolean isCurrent;
	
	@ManyToOne
	private Bankroll bankroll;
	    
	@OneToMany(cascade = CascadeType.REMOVE)
    private List<Bet> bets;
    
    public String formatStartDate() {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = this.startDate;
		String formattedDateTime = dateTime.format(formatter);
//		System.out.println(formattedDateTime);
		
		return formattedDateTime;
		    	
    }
    
    
}
