package combibet.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Bet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Date date;
	
	private int meeting;
	
	private int race;
	
	private String selection;
	
	private double odd;
	
	private BetType type;
	
	private BetStatus status;
	
	private String comment;
}
