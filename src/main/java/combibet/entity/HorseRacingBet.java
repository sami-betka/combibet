package combibet.entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.ToString;

//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
@Entity
@ToString
//@EqualsAndHashCode
@DiscriminatorColumn(name="hrb")
@SequenceGenerator(name = "HORSE_RACING_BET_SEQ_GENERATOR", sequenceName = "HORSE_RACING_BET_SEQ", initialValue = 1, allocationSize = 1)

public class HorseRacingBet extends Bet {

public HorseRacingBet() {
		super();
	}

public HorseRacingBet(int meeting, int race, Combi combi, BetType type, boolean hasWon, double winOdd, Discipline discipline) {
		super();
		this.meeting = meeting;
		this.race = race;
		this.combi = combi;
		this.type = type;
		this.hasWon = hasWon;
		this.winOdd = winOdd;
		this.discipline = discipline;
	}

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HORSE_RACING_BET_SEQ_GENERATOR")
//	private Long id;
//
//	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
//	private LocalDateTime date;
//
//	private String formattedDate;

	private int meeting;

	private int race;
	
	private BetType type;
	
	private boolean hasWon;

//	private String selection;
//
	private double winOdd;
	
	private Discipline discipline;
//
//	private double ante;

//	@ManyToOne
//	private Gambler gambler;

	@ManyToOne
	@JoinColumn(name = "combi_id")
	private Combi combi;

	public int getMeeting() {
		return meeting;
	}

	public void setMeeting(int meeting) {
		this.meeting = meeting;
	}

	public int getRace() {
		return race;
	}

	public void setRace(int race) {
		this.race = race;
	}

	public Combi getCombi() {
		return combi;
	}

	public void setCombi(Combi combi) {
		this.combi = combi;
	}

	public BetType getType() {
		return type;
	}

	public void setType(BetType type) {
		this.type = type;
	}

	public boolean isHasWon() {
		return hasWon;
	}

	public void setHasWon(boolean hasWon) {
		this.hasWon = hasWon;
	}

	public double getWinOdd() {
		return winOdd;
	}

	public void setWinOdd(double winOdd) {
		this.winOdd = winOdd;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

//	private BetStatus status;

//	private BetType type;

//	private String beforeComment;
//	
//	private String afterComment;

//	public String formatDate() {
//
//		LocalDateTime date = this.getDate();
//
//		String day = "";
//		String month = "";
//
//		if (date.getDayOfWeek().toString() == "MONDAY") {
//			day = "Lun";
//		}
//		if (date.getDayOfWeek().toString() == "TUESDAY") {
//			day = "Mar";
//		}
//		if (date.getDayOfWeek().toString() == "WEDNESDAY") {
//			day = "Mer";
//		}
//		if (date.getDayOfWeek().toString() == "THURSDAY") {
//			day = "Jeu";
//		}
//		if (date.getDayOfWeek().toString() == "FRIDAY") {
//			day = "Ven";
//		}
//		if (date.getDayOfWeek().toString() == "SATURDAY") {
//			day = "Sam";
//		}
//		if (date.getDayOfWeek().toString() == "SUNDAY") {
//			day = "Dim";
//		}
//
//		if (date.getMonth().toString() == "JANUARY") {
//			month = "Janvier";
//		}
//		if (date.getMonth().toString() == "FEBRUARY") {
//			month = "Février";
//		}
//		if (date.getMonth().toString() == "MARCH") {
//			month = "Mars";
//		}
//		if (date.getMonth().toString() == "APRIL") {
//			month = "Avril";
//		}
//		if (date.getMonth().toString() == "MAY") {
//			month = "Mai";
//		}
//		if (date.getMonth().toString() == "JUNE") {
//			month = "Juin";
//		}
//		if (date.getMonth().toString() == "JULY") {
//			month = "Juillet";
//		}
//		if (date.getMonth().toString() == "AUGUST") {
//			month = "Août";
//		}
//		if (date.getMonth().toString() == "SEPTEMBER") {
//			month = "Septembre";
//		}
//		if (date.getMonth().toString() == "OCTOBER") {
//			month = "Octobre";
//		}
//		if (date.getMonth().toString() == "NOVEMBER") {
//			month = "Novembre";
//		}
//		if (date.getMonth().toString() == "DECEMBER") {
//			month = "Décembre";
//		}
//
//		return day + " " + date.getDayOfMonth() + " " + month + " " + date.getYear();
//	}

}
