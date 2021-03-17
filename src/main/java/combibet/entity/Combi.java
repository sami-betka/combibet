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
public class Combi {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
    @ManyToOne
    private Gambler gambler;
    
    @OneToMany
    private List<Bet> bets;
}
