package combibet.entity;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="field")
public abstract class Bet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime date;
	
	private String formattedDate;
	
	private String selection;
	
	private double odd;
	
	private double ante;
	
	@ManyToOne
	private Gambler gambler;
	
//	@ManyToOne
//	private Bankroll bankroll;
	
//	@ManyToOne
//	private Combi combi;
		
	private BetStatus status;
	
	private BetType type;
	
	private String beforeComment;
	
	private String afterComment;
	
		
	
}
