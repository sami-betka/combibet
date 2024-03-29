package combibet.entity;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
//@Data
@SequenceGenerator(
		  name = "BANKROLL_SEQ_GENERATOR",
		  sequenceName = "BANKROLL_SEQ",
		  initialValue = 1, allocationSize = 1)
public class Bankroll {

	public Bankroll() {
		super();
	}

	public Bankroll(Long id, String name, LocalDateTime startDate, String formattedStartDate, LocalDateTime endDate,
			double startAmount, Gambler gambler, List<Combi> combis, List<Bet> bets, boolean isActive, BankrollField bankrollField,
			Boolean isPositive, Boolean pendingBets, String roi, String capitalMultiplication) {
		super();
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.formattedStartDate = formattedStartDate;
		this.endDate = endDate;
		this.startAmount = startAmount;
		this.gambler = gambler;
		this.combis = combis;
		this.bets = bets;
		this.isActive = isActive;
		this.isPositive = isPositive;
		this.pendingBets = pendingBets;
		this.bankrollField = bankrollField;
		this.roi = roi;
		this.capitalMultiplication = capitalMultiplication;


	}

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
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "bankroll")
	private List<Bet> bets;
	
	private BankrollField bankrollField;
	
	private boolean isActive;
	
	private Boolean isPositive;
	
	Boolean pendingBets;
	
	String roi;
	
	String capitalMultiplication;
	
	
	public double calculateCurrentAmount() {

		return this.startAmount + benefit();
	}
	
	public int betNumber() {
		
		return this.bets.stream().filter(b -> !b.getStatus().equals(BetStatus.NOT_PLAYED_LOSE) && !b.getStatus().equals(BetStatus.NOT_PLAYED_WON)).collect(Collectors.toList()).size();
											
	}
	
	public double benefit () {
		
		double benefitAmount = 0;
		
		for(Bet bet : this.bets){
			
//			benefitAmount += combi.benefit();
		
		}
	
		
		return benefitAmount;
	}
	
	public String formatStartDate() {

		LocalDateTime date = this.startDate;
		
		if(!this.bets.isEmpty()) {
		date = this.bets
				.stream()
				.sorted(Comparator.comparing(Bet::getDate))
				.findFirst().get().getDate();
		}
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
	
	public String getRoi() {
		
		List<Double> wonBetsOdds = this.bets.stream().filter(b -> b.getStatus().equals(BetStatus.WON)).map(Bet::getOdd)
				.collect(Collectors.toList());
//		List<Bet> bets = this.bets
//				.stream()
//				.filter(b -> b.getStatus())
//				.collect(Collectors.toList());
			
		Double total = 0d;
		for(Double odd : wonBetsOdds) {
			total += odd;
		}
		float roi = (float) (1.0*(100 * total) / this.bets.size())/100;
		
		return String.valueOf(String.format("%.2f", roi)) + "%";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public double getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(double startAmount) {
		this.startAmount = startAmount;
	}

	public Gambler getGambler() {
		return gambler;
	}

	public void setGambler(Gambler gambler) {
		this.gambler = gambler;
	}

	public List<Combi> getCombis() {
		return combis;
	}

	public void setCombis(List<Combi> combis) {
		this.combis = combis;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<Bet> getBets() {
		return bets;
	}

	public void setBets(List<Bet> bets) {
		this.bets = bets;
	}

	public BankrollField getBankrollField() {
		return bankrollField;
	}

	public void setBankrollField(BankrollField bankrollField) {
		this.bankrollField = bankrollField;
	}

	public Boolean getIsPositive() {
		return isPositive;
	}

	public void setPositive(Boolean isPositive) {
		this.isPositive = isPositive;
	}

	public Boolean getPendingBets() {
		return pendingBets;
	}

	public void setPendingBets(Boolean pendingBets) {
		this.pendingBets = pendingBets;
	}

	public void setIsPositive(Boolean isPositive) {
		this.isPositive = isPositive;
	}

	public String getCapitalMultiplication() {
		return capitalMultiplication;
	}

	public void setCapitalMultiplication(String capitalMultiplication) {
		this.capitalMultiplication = capitalMultiplication;
	}

	public void setRoi(String roi) {
		this.roi = roi;
	}
	
	
}
