package combibet.entity;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
//@DiscriminatorValue("HR")
public class HorseRacingBet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime date;
	
	private String formattedDate;

	private int meeting;

	private int race;
	
private String selection;
	
	private double odd;
	
	private double ante;
	
	@ManyToOne
	private Gambler gambler;
	
//	@ManyToOne
//	private Bankroll bankroll;
	
	@ManyToOne
	private Combi combi;
		
	private BetStatus status;
	
	private BetType type;
	
	private String beforeComment;
	
	private String afterComment;
}
