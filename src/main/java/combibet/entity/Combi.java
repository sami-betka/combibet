package combibet.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.EqualsAndHashCode;
import lombok.ToString;

//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
@Entity
@EqualsAndHashCode
@ToString
@SequenceGenerator(name = "COMBI_SEQ_GENERATOR", sequenceName = "COMBI_SEQ", initialValue = 1, allocationSize = 1)

public class Combi {

	public Combi() {
		super();
	}

	public Combi(Long id, LocalDateTime startDate, String formattedStartDate, LocalDateTime endDate, double ante,
			Gambler gambler, BetStatus status, BetType type, String beforeComment, String afterComment,
			boolean isCurrent, Bankroll bankroll, List<Bet> bets) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.formattedStartDate = formattedStartDate;
		this.endDate = endDate;
		this.ante = ante;
		this.gambler = gambler;
		this.status = status;
		this.type = type;
		this.beforeComment = beforeComment;
		this.afterComment = afterComment;
		this.isCurrent = isCurrent;
		this.bankroll = bankroll;
		this.bets = bets;
	}

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMBI_SEQ_GENERATOR")
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
	@JoinColumn(name = "bankroll_id")
	private Bankroll bankroll;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "combi")
	private List<Bet> bets;

	
	public List<Bet> betsAsc() {
		
		List<Bet> bets = this.bets;

		bets.sort((o1,o2) -> o1.getDate().compareTo(o2.getDate())); 
		
		double currentOdd = 1d;
//		bets.get(0).setCurrentOddInCombi(bets.get(0).getOdd());
		
		for(int i = 0; i< bets.size(); i++) {
			
			bets.get(i).setCurrentOddInCombi(currentOdd * bets.get(i).getOdd());
			currentOdd = bets.get(i).getCurrentOddInCombi();
		}

		return bets;
	}

	public double benefit() {

		double benefitAmount = 0;

		for (Bet bet : this.bets) {

			if (bet.getStatus() == BetStatus.WON) {
				benefitAmount += (bet.getOdd() * bet.getAnte()) - bet.getAnte();
			}
			if (bet.getStatus() == BetStatus.LOSE) {
				benefitAmount -= bet.getAnte();
			}
		}

		return benefitAmount;
	}

	public String formatStartDate() {

		LocalDateTime date = this.startDate;

		String day = "";
		String month = "";

		if (date.getDayOfWeek().toString() == "MONDAY") {
			day = "Lundi";
		}
		if (date.getDayOfWeek().toString() == "TUESDAY") {
			day = "Mardi";
		}
		if (date.getDayOfWeek().toString() == "WEDNESDAY") {
			day = "Mercredi";
		}
		if (date.getDayOfWeek().toString() == "THURSDAY") {
			day = "Jeudi";
		}
		if (date.getDayOfWeek().toString() == "FRIDAY") {
			day = "Vendredi";
		}
		if (date.getDayOfWeek().toString() == "SATURDAY") {
			day = "Samedi";
		}
		if (date.getDayOfWeek().toString() == "SUNDAY") {
			day = "Dimanche";
		}

		if (date.getMonth().toString() == "JANUARY") {
			month = "Janvier";
		}
		if (date.getMonth().toString() == "FEBRUARY") {
			month = "Février";
		}
		if (date.getMonth().toString() == "MARCH") {
			month = "Mars";
		}
		if (date.getMonth().toString() == "APRIL") {
			month = "Avril";
		}
		if (date.getMonth().toString() == "MAY") {
			month = "Mai";
		}
		if (date.getMonth().toString() == "JUNE") {
			month = "Juin";
		}
		if (date.getMonth().toString() == "JULY") {
			month = "Juillet";
		}
		if (date.getMonth().toString() == "AUGUST") {
			month = "Août";
		}
		if (date.getMonth().toString() == "SEPTEMBER") {
			month = "Septembre";
		}
		if (date.getMonth().toString() == "OCTOBER") {
			month = "Octobre";
		}
		if (date.getMonth().toString() == "NOVEMBER") {
			month = "Novembre";
		}
		if (date.getMonth().toString() == "DECEMBER") {
			month = "Décembre";
		}

		return day + " " + date.getDayOfMonth() + " " + month + " " + date.getYear();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public String getFormattedStartDate() {
		return formattedStartDate;
	}

	public void setFormattedStartDate(String formattedStartDate) {
		this.formattedStartDate = formattedStartDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public double getAnte() {
		return ante;
	}

	public void setAnte(double ante) {
		this.ante = ante;
	}

	public Gambler getGambler() {
		return gambler;
	}

	public void setGambler(Gambler gambler) {
		this.gambler = gambler;
	}

	public BetStatus getStatus() {
		return status;
	}

	public void setStatus(BetStatus status) {
		this.status = status;
	}

	public BetType getType() {
		return type;
	}

	public void setType(BetType type) {
		this.type = type;
	}

	public String getBeforeComment() {
		return beforeComment;
	}

	public void setBeforeComment(String beforeComment) {
		this.beforeComment = beforeComment;
	}

	public String getAfterComment() {
		return afterComment;
	}

	public void setAfterComment(String afterComment) {
		this.afterComment = afterComment;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public Bankroll getBankroll() {
		return bankroll;
	}

	public void setBankroll(Bankroll bankroll) {
		this.bankroll = bankroll;
	}

	public List<Bet> getBets() {
		return bets;
	}

	public void setBets(List<Bet> bets) {
		this.bets = bets;
	}

}
