package combibet.entity;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

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
@ToString
//@EqualsAndHashCode
@DiscriminatorColumn(name="hrb")
@SequenceGenerator(name = "HORSE_RACING_BET_SEQ_GENERATOR", sequenceName = "HORSE_RACING_BET_SEQ", initialValue = 1, allocationSize = 1)

public class HorseRacingBet extends Bet {

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

//	private String selection;
//
//	private double odd;
//
//	private double ante;

//	@ManyToOne
//	private Gambler gambler;

	@ManyToOne
	@JoinColumn(name = "combi_id")
	private Combi combi;

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
