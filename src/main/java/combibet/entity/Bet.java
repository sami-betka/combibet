package combibet.entity;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
@Entity
@ToString
@EqualsAndHashCode
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="bet_type")
@SequenceGenerator(name = "BET_SEQ_GENERATOR", sequenceName = "BET_SEQ", initialValue = 1, allocationSize = 1)

public abstract class Bet {

	public Bet() {
		super();
	}

	public Bet(Long id, LocalDateTime date, Bankroll bankroll, String formattedDate, String field, String selection, double odd,
			double currentOddInCombi, double ante, Gambler gambler, Combi combi, BetType type, BetStatus status,
			String beforeComment, String afterComment) {
		super();
		this.id = id;
		this.date = date;
		this.formattedDate = formattedDate;
		this.field = field;
		this.selection = selection;
		this.odd = odd;
		this.currentOddInCombi = currentOddInCombi;
		this.ante = ante;
		this.gambler = gambler;
		this.combi = combi;
		this.type = type;
		this.bankroll = bankroll;
		this.status = status;
		this.beforeComment = beforeComment;
		this.afterComment = afterComment;
	}

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BET_SEQ_GENERATOR")
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime date;

	private String formattedDate;
	
	private String field;

	private String selection;

	private double odd;
	
	private double currentOddInCombi;

	private double ante;

	@ManyToOne
	private Gambler gambler;
	
	@ManyToOne
	@JoinColumn(name = "bankroll_id")
	private Bankroll bankroll;

	@ManyToOne
	@JoinColumn(name = "combi_id")
	private Combi combi;
	
	private BetType type;

	private BetStatus status;

	private String beforeComment;

	private String afterComment;

	public String formatDate() {

		LocalDateTime date = this.date;

		String day = "";
		String month = "";

		if (date.getDayOfWeek().toString() == "MONDAY") {
			day = "Lun";
		}
		if (date.getDayOfWeek().toString() == "TUESDAY") {
			day = "Mar";
		}
		if (date.getDayOfWeek().toString() == "WEDNESDAY") {
			day = "Mer";
		}
		if (date.getDayOfWeek().toString() == "THURSDAY") {
			day = "Jeu";
		}
		if (date.getDayOfWeek().toString() == "FRIDAY") {
			day = "Ven";
		}
		if (date.getDayOfWeek().toString() == "SATURDAY") {
			day = "Sam";
		}
		if (date.getDayOfWeek().toString() == "SUNDAY") {
			day = "Dim";
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public double getOdd() {
		return odd;
	}

	public void setOdd(double odd) {
		this.odd = odd;
	}

	public double getCurrentOddInCombi() {
		return currentOddInCombi;
	}

	public void setCurrentOddInCombi(double currentOddInCombi) {
		this.currentOddInCombi = currentOddInCombi;
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

	public BetStatus getStatus() {
		return status;
	}

	public void setStatus(BetStatus status) {
		this.status = status;
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

	public Bankroll getBankroll() {
		return bankroll;
	}

	public void setBankroll(Bankroll bankroll) {
		this.bankroll = bankroll;
	}

}
