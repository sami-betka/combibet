package combibet.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
@ToString
@SequenceGenerator(name = "COMBI_SEQ_GENERATOR", sequenceName = "COMBI_SEQ", initialValue = 1, allocationSize = 1)

public class Combi {

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

}
