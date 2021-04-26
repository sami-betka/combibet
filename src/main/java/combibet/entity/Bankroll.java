package combibet.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
@SequenceGenerator(
		  name = "BANKROLL_SEQ_GENERATOR",
		  sequenceName = "BANKROLL_SEQ",
		  initialValue = 1, allocationSize = 1)
public class Bankroll {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANKROLL_SEQ_GENERATOR")
	private Long id;
	
	private String name;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startDate;
	
	private String formattedStartDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endDate;

	private double startAmount;
	
//	private double currentAmount;

	@ManyToOne
	private Gambler gambler;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "bankroll")
	private List<Combi> combis;
	
	private boolean isActive;
	
	
	public double calculateCurrentAmount() {

		return this.startAmount + benefit();
	}
	
	public int betNumber() {
		
		return this.combis.size();
	}
	
	public double benefit () {
		
		double benefitAmount = 0;
		
		for(Combi combi : this.combis){
			
			benefitAmount += combi.benefit();
		
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
