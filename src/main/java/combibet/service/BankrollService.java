package combibet.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import combibet.entity.Bankroll;
import combibet.entity.BankrollField;
import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.Gambler;
import combibet.entity.HorseRacingBet;
import combibet.repository.BankrollRepository;
import combibet.repository.BetRepository;
import combibet.repository.GamblerRepository;

@Service
public class BankrollService {

	@Autowired
	BankrollRepository bankrollRepository;

	@Autowired
	GamblerRepository gamblerRepository;

	@Autowired
	BetRepository betRepository;

	public Map<String, Object> managedBankrollSimulation(List<Bet> betList, Integer anteDivider,
			Double initialBankrollAmount, Double invest) {

		List<Bet> bets = betList;
		List<Bet> arrangedBets = new ArrayList<>();
		LinkedList<Double> bankrollAmounts = new LinkedList<>();
		LinkedList<String> betsDates = new LinkedList<>();

		Double actualBankrollAmount = initialBankrollAmount;
		Double topAmount = actualBankrollAmount;
		Double topAmountWhenMinimum = actualBankrollAmount;
		Double minimumBankrollAmount = initialBankrollAmount;
		float minimumBankrollPercent = (float) (1.0*(100 * actualBankrollAmount) / topAmount);

		Double ante = topAmount / anteDivider;
		Double anteWhenMinimumAmount = ante;
		Double lastAnte = ante;

//		Double refAnte = ante;
		int lostAntes = 0;
		int maxAnteLost = 0;


		for (int i = 0; i < bets.size(); i++) {

			Bet bet = bets.get(i);
			
			if(bet.getStatus().equals(BetStatus.LOSE)) {
				lostAntes += 1;
				if(lostAntes > maxAnteLost) {
					maxAnteLost = lostAntes;
				}
			}
			if(bet.getStatus().equals(BetStatus.WON)) {
				lostAntes = 0;
			}
			
			Double realOdd = bet.getOdd();
//			Double realAnte = bet.getAnte();

//			int anteInt = ((int) ante) + 1;
//			ante = anteInt;
			
			if(!bet.getBankroll().getId().equals(88l) && !bet.getBankroll().getId().equals(90l)) {
				
				bet.setAnte(ante);

				if(i > 0 && bet.formatDate().get("day").equals(bets.get(i-1).formatDate().get("day"))
						&& (Integer.valueOf(bet.formatDate().get("realHour")) - 2) <= Integer.valueOf(bets.get(i-1).formatDate().get("realHour"))
						) {
					bet.setAnte(lastAnte);
					
				} else {
					lastAnte = ante;
				}

				////////////////////////////////////////////
//				if(bet.getOdd() > 3) {
//					
//					Double expectedWin = bet.getAnte() * 1.5;
//					Double newAnte = expectedWin/bet.getOdd();
//					bet.setAnte(newAnte);
//				}
				///////////////////////////////////////////

			}
		      else {
			        lastAnte = ante;
		           }
			
			Double realAnte = bet.getAnte();

			

			if(bet.getStatus().equals(BetStatus.PENDING)) {
				bet.setAnte(0d);
			}
			if (!bet.getStatus().equals(BetStatus.WON) && !bet.getStatus().equals(BetStatus.SEMI)
					&& !bet.getStatus().equals(BetStatus.NOT_PLAYED_WON)) {
				bet.setOdd(0d);
			}
			

			actualBankrollAmount = actualBankrollAmount - bet.getAnte();

			actualBankrollAmount = actualBankrollAmount + (bet.getAnte() * bet.getOdd());

			bankrollAmounts.add(actualBankrollAmount);
			betsDates.add(String.valueOf(bet.getDate()));


			if (actualBankrollAmount > topAmount) {
				topAmount = actualBankrollAmount;
			
				ante = actualBankrollAmount / anteDivider;
				
				anteWhenMinimumAmount = ante;

			}
			
			if(actualBankrollAmount < minimumBankrollAmount) {
				minimumBankrollAmount = actualBankrollAmount;
//				topAmountWhenMinimum = topAmount;
				anteWhenMinimumAmount = ante;
			}
			
			///////plus bas pourcentage
			if (actualBankrollAmount < topAmount) {
			
				float newMinimumBankrollPercent = (float) (1.0*(100 * actualBankrollAmount) / topAmount);

				if(newMinimumBankrollPercent < minimumBankrollPercent) {
					minimumBankrollPercent = newMinimumBankrollPercent;
				}

			}

			bet.setOdd(realOdd);
			bet.setAnte(realAnte);
			arrangedBets.add(bet);
		}

		Map<String, Object> finalMap = new HashMap<>();
		Double realBankrollAmount = actualBankrollAmount - (initialBankrollAmount - invest);

		finalMap.put("initialList", betList);
		finalMap.put("betList", arrangedBets);
		finalMap.put("initialBankrollAmount", initialBankrollAmount);
		finalMap.put("actualBankrollAmount", actualBankrollAmount);
		finalMap.put("topBankrollAmount", topAmount);
		finalMap.put("topAmountWhenMinimum", topAmountWhenMinimum);
		finalMap.put("minimumBankrollAmount", minimumBankrollAmount);
		finalMap.put("minimumBankrollPercent", minimumBankrollPercent);

		finalMap.put("realBankrollAmount", realBankrollAmount);
		finalMap.put("anteWhenMinimumAmount", anteWhenMinimumAmount);	

		finalMap.put("initialAnte", initialBankrollAmount / anteDivider);
		finalMap.put("divider", anteDivider);
		finalMap.put("maxAnteLost", maxAnteLost);
		finalMap.put("invest", invest);

//		finalMap.put("lastAnte", lastAnte);
		List<Bet> filteredList = arrangedBets
				.stream()
				.filter(b -> !b.getStatus().equals(BetStatus.PENDING))
				.collect(Collectors.toList());
		
		if(filteredList.size() > 0) {
			finalMap.put("lastAnte", filteredList.get(filteredList.size()-1).getAnte());
		}

		/////////////////// Dashboard Infos

		finalMap.put("bankrollAmounts", bankrollAmounts);
		finalMap.put("betsDates", betsDates);

		return finalMap;
	}

//	public Map<String, Object> winManagedBankrollSimulation(List<Bet> betList, Integer anteDivider,
//			Double initialBankrollAmount) {
//
//		List<Bet> bets = betList;
//		List<Bet> arrangedBets = new ArrayList<>();
//		LinkedList<Double> bankrollAmounts = new LinkedList<>();
//		LinkedList<String> betsDates = new LinkedList<>();
//
////		Map<String, Object> finalMap = new HashMap<>();
////		finalMap.put("InitialBankrollAmount", initialBankrollAmount);
//
////        Double initialBankrollAmount = bets.get(0).getBankroll().getStartAmount();
//		Double actualBankrollAmount = initialBankrollAmount;
//		Double topAmount = actualBankrollAmount;
//		Double ante = topAmount / anteDivider;
//
////		bets.get(0).setAnte(ante);
//
//		for (int i = 0; i < bets.size(); i++) {
//
//			Bet bet = bets.get(i);
//			bet.setType(BetType.SIMPLE_GAGNANT);
//			HorseRacingBet hrb = (HorseRacingBet) bet;
//
//			if (!bet.getStatus().equals(BetStatus.WON)) {
//				bet.setOdd(0d);
//			}
//			if (hrb.isHasWon() == false) {
//				bet.setOdd(0d);
//				bet.setStatus(BetStatus.LOSE);
//			} else {
//				hrb.setOdd(hrb.getWinOdd());
//			}
//			bet.setAnte(ante);
//			System.out.println(i + 1 + " 0) mise " + ante);
//
//			actualBankrollAmount = actualBankrollAmount - bet.getAnte();
//
//			actualBankrollAmount = actualBankrollAmount + (bet.getAnte() * bet.getOdd());
//
//			System.out.println(i + 1 + " 1) " + String.format("%.2f", actualBankrollAmount));
//			System.out.println(i + 1 + " 2) bank actuelle " + String.format("%.2f", actualBankrollAmount));
//			System.out.println(i + 1 + " 3) bank max precedente " + String.format("%.2f", topAmount));
//
//			bankrollAmounts.add(actualBankrollAmount);
//			betsDates.add(String.valueOf(bet.getDate()));
//
//			if (actualBankrollAmount > topAmount) {
//
//				ante = actualBankrollAmount / anteDivider;
//
//				topAmount = actualBankrollAmount;
//			}
//
//			System.out.println("");
//
////				if(actualBankrollAmount<0) {
////                   return null;
////                  }
//
//			arrangedBets.add(bet);
//		}
//
//		Map<String, Object> finalMap = new HashMap<>();
//		finalMap.put("betList", arrangedBets);
//		finalMap.put("initialBankrollAmount", initialBankrollAmount);
//		finalMap.put("actualBankrollAmount", actualBankrollAmount);
//		finalMap.put("initialAnte", initialBankrollAmount / anteDivider);
//		finalMap.put("divider", anteDivider);
//
//		/////////////////// Dashboard Infos
//
//		finalMap.put("bankrollAmounts", bankrollAmounts);
//		finalMap.put("betsDates", betsDates);
//
////		return arrangedBets;
//		return finalMap;
//	}

//	public LinkedHashMap<String, String> betListInfos(Map<String, Object> map) {
//
//		LinkedHashMap<String, String> betListInfos = new LinkedHashMap<>();
//
////		Double earnings = 0d;
////		for (Bet b : bets) {
////			earnings = earnings + (b.getOdd() * b.getAnte());
////			if(b.getStatus().equals(BetStatus.LOSE)) {
////				earnings = earnings - b.getAnte();
////			}
////		}
////		Double benefit = earnings - bankrollAmount;
//
//		System.out.println(map.size() + " !!!");
//
//		List<Bet> betList = (List<Bet>) map.get("betList");
//		Double initialBankrollAmount = (Double) map.get("initialBankrollAmount");
//		Double actualBankrollAmount = (Double) map.get("actualBankrollAmount");
//		Double initialAnte = (Double) map.get("initialAnte");
//		Integer divider = (Integer) map.get("divider");
//
//		Double earnings = actualBankrollAmount;
//		Double benefit = earnings - initialBankrollAmount;
//
//		betListInfos.put("Montant bankroll initial", String.valueOf(initialBankrollAmount));
//		betListInfos.put("Montant bankroll actuel", String.valueOf(String.format("%.2f", actualBankrollAmount)));
//		betListInfos.put("Mise initiale", String.valueOf(String.format("%.2f", initialAnte)));
//		betListInfos.put("Prochaine mise", String.valueOf(String.format("%.2f", actualBankrollAmount / 20)));
//
//		betListInfos.put("Diviseur", String.valueOf(divider));
//
//		betListInfos.put("Nombre de paris", String.valueOf(betList.size()));
//
//		List<Double> wonBetsOdds = betList.stream().filter(b -> b.getStatus().equals(BetStatus.WON)).map(Bet::getOdd)
//				.collect(Collectors.toList());
//
//		betListInfos.put("Paris gagnants", String.valueOf(wonBetsOdds.size()));
//		betListInfos.put("Paris perdants", String.valueOf(betList.stream()
//				.filter(b -> b.getStatus().equals(BetStatus.LOSE)).collect(Collectors.toList()).size()));
//
////		betListInfos.put("Gains", String.valueOf(String.format("%.2f", earnings)));
//		betListInfos.put("Bénéfice", String.valueOf(String.format("%.2f", benefit)));
//
//		betListInfos.put("Cote moyenne des paris gagnants", String.valueOf(String.format("%.2f",
//				wonBetsOdds.stream().collect(Collectors.summingDouble(Double::doubleValue)) / wonBetsOdds.size())));
//
//		betListInfos.put("Multiplication du capital : X",
//				String.valueOf(String.format("%.2f", earnings / initialBankrollAmount)));
//
//		return betListInfos;
//	}

	public LinkedHashMap<String, String> betListInfosSimulation(Map<String, Object> map, Double minus, Double maxOdd) {

		LinkedHashMap<String, String> betListInfos = new LinkedHashMap<>();

//		System.out.println(map.size() + " !!!");

		List<Bet> betList = (List<Bet>) map.get("betList");
		List<Bet> initialList = (List<Bet>) map.get("initialList");
		Double initialBankrollAmount = (Double) map.get("initialBankrollAmount");
		Double actualBankrollAmount = (Double) map.get("actualBankrollAmount");
		Double topBankrollAmount = (Double) map.get("topBankrollAmount");
		Double minimumBankrollAmount = (Double) map.get("minimumBankrollAmount");
		Double realBankrollAmount = (Double) map.get("realBankrollAmount");
		Integer maxAnteLost = (Integer) map.get("maxAnteLost");

		Double initialAnte = (Double) map.get("initialAnte");
		Integer divider = (Integer) map.get("divider");
		Double invest = (Double) map.get("invest");
		Double lastAnte = (Double) map.get("lastAnte");
		Double topAmountWhenMinimum = (Double) map.get("topAmountWhenMinimum");


			
		
//		Double topAmountWhenMinimum = (Double) map.get("anteWhenMinimumAmount") * divider;
		float minimumBankrollPercent = (float) map.get("minimumBankrollPercent");
//		Double anteWhenMinimum = (Double) map.get("anteWhenMinimumAmount");
//		float minimumBankrollPercent = (float) (1.0*(100 * minimumBankrollAmount) / anteWhenMinimum * divider);		

		Double earnings = actualBankrollAmount;
		Double benefit = earnings - initialBankrollAmount;
		
		if(betList.size() > 0) {
			betListInfos.put("Période", String.valueOf("Date de début: " + betList.get(0).formatDate().get("day") + ", Date de fin: " + betList.get(betList.size() - 1).formatDate().get("day")));
		}
		if(betList.size() > 0) {
			final LocalDate start = LocalDate.parse(betList.get(0).formatDate().get("day").substring(4), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			  final LocalDate end = LocalDate.parse(betList.get(betList.size()-1).formatDate().get("day").substring(4), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			  final long numberOfDays = start.until(end, ChronoUnit.DAYS);
				betListInfos.put("Nombre de jours", String.valueOf(numberOfDays + 1));
		}

		betListInfos.put("Nombre de paris", String.valueOf(betList
				.stream()
				.filter(b -> !b.getStatus().equals(BetStatus.PENDING))
				.collect(Collectors.toList())
				.size()));

		List<Double> wonBetsOdds = betList.stream().filter(b -> b.getStatus().equals(BetStatus.WON)).map(Bet::getOdd)
				.collect(Collectors.toList());
		List<Double> lostBetsOdds = betList.stream().filter(b -> b.getStatus().equals(BetStatus.LOSE)).map(Bet::getOdd)
				.collect(Collectors.toList());
		
		Double total = 0d;
		for(Double odd : wonBetsOdds) {
			total += odd;
		}
		
		betListInfos.put("Gains", String.valueOf(String.format("%.2f", total)));

		betListInfos.put("Paris gagnants", String.valueOf(wonBetsOdds.size()));
		betListInfos.put("Paris perdants", String.valueOf(betList.stream()
				.filter(b -> b.getStatus().equals(BetStatus.LOSE)).collect(Collectors.toList()).size()));
		
		float winPercentage = (float) (1.0*(100 * wonBetsOdds.size()) / betList
				.stream()
				.filter(b -> !b.getStatus().equals(BetStatus.PENDING))
				.collect(Collectors.toList()).size());

		betListInfos.put("Pourcentage de paris gagnants", String.valueOf(String.format("%.2f", winPercentage)) + "%");

		float roi = (float) (1.0*(100 * total) / betList
				.stream()
				.filter(b -> !b.getStatus().equals(BetStatus.PENDING))
				.collect(Collectors.toList()).size() - 100);
		
		betListInfos.put("ROI", String.valueOf(String.format("%.2f", roi)) + "%");

		
		betListInfos.put("Montant bankroll initial", String.valueOf(initialBankrollAmount));
		betListInfos.put("Montant bankroll actuel", String.valueOf(String.format("%.2f", actualBankrollAmount)));
		betListInfos.put("Montant bankroll le plus elevé", String.valueOf(String.format("%.2f", topBankrollAmount)));
		betListInfos.put("Montant bankroll le plus bas", String.valueOf(String.format("%.2f", minimumBankrollAmount)));
		betListInfos.put("Plus bas pourcentage bankroll restant", String.valueOf(String.format("%.2f", minimumBankrollPercent)) + "%");
		betListInfos.put("Pourcentage bankroll actuel", String.valueOf(String.format("%.2f", (float) (1.0*(100 * actualBankrollAmount) / topBankrollAmount))) + "%");

		betListInfos.put("Montant réel investi", String.valueOf(String.format("%.2f", invest)));
		betListInfos.put("Montant bankroll réel actuel", String.valueOf(String.format("%.2f", realBankrollAmount)));

		betListInfos.put("Maximum de mises perdues consécutivement", String.valueOf(maxAnteLost) + " / " + divider);

		betListInfos.put("Mise initiale", String.valueOf(String.format("%.2f", initialAnte)));
		betListInfos.put("Prochaine mise", String.valueOf(String.format("%.2f", topBankrollAmount / divider)));
		betListInfos.put("Prochaine mise (si même jour)", String.valueOf(String.format("%.2f", lastAnte)));



		betListInfos.put("Diviseur", String.valueOf(divider));
		betListInfos.put("Cotes arrangées", String.valueOf("- " + minus));
		betListInfos.put("Cote maximum autorisée", String.valueOf(String.format("%.2f", maxOdd)));
		betListInfos.put("Bénéfice", String.valueOf(String.format("%.2f", benefit)));

		betListInfos.put("Cote moyenne des paris gagnants", String.valueOf(String.format("%.2f",
				wonBetsOdds.stream().collect(Collectors.summingDouble(Double::doubleValue)) / wonBetsOdds.size())));
		betListInfos.put("Cote moyenne des paris perdants", String.valueOf(String.format("%.2f",
				lostBetsOdds.stream().collect(Collectors.summingDouble(Double::doubleValue)) / lostBetsOdds.size())));

		betListInfos.put("Multiplication du capital de départ : X",
				String.valueOf(String.format("%.2f", earnings / initialBankrollAmount)));
		
		if(betList.size()>0) {
			Bankroll bankroll = betList.get(0).getBankroll();
			if((earnings / initialBankrollAmount) < 1) {
				bankroll.setPositive(false);
			} else {
				bankroll.setPositive(true);
			}
			if(String.valueOf(String.format("%.2f", earnings / initialBankrollAmount)).equals("1,00")) {
				bankroll.setPositive(true);
			}
			
			
			bankroll.setRoi(String.valueOf(String.format("%.2f", roi)) + "%");
			bankroll.setCapitalMultiplication(String.valueOf(String.format("%.2f", earnings / initialBankrollAmount)));
			bankrollRepository.save(bankroll);

		}
	
		

		return betListInfos;
	}

	public LinkedHashMap<String, String> betsInfos(List<Bet> betList, Double initialBankAmount) {

		LinkedHashMap<String, String> infos = new LinkedHashMap<>();
		Double initialBankrollAmount = initialBankAmount;
		Double actualBankrollAmount = initialBankrollAmount;
		Double initialAnte = initialBankrollAmount / 20;
		Double nextAnte = initialAnte;

        infos.put("Nombre de paris", String.valueOf( betList.size()));
		
		List<Double> wonBetsOdds = betList.stream().filter(b -> b.getStatus().equals(BetStatus.WON)).map(Bet::getOdd)
				.collect(Collectors.toList());
		
		Double gains = 0d;
		for(Double cote : wonBetsOdds) {
			gains += cote;
		}
		infos.put("Gains", String.valueOf(Precision.round(gains, 2)));
		infos.put("Bénef", String.valueOf(Precision.round(gains - betList.size(), 2)));

		infos.put("Montant bankroll initial", String.valueOf(actualBankrollAmount));

		LinkedList<Double> bankrollAmounts = new LinkedList<>();
		Double higherAmount = actualBankrollAmount;
		Double smallerAmount = actualBankrollAmount;

		bankrollAmounts.add(actualBankrollAmount);

		for (int i = 0; i < betList.size(); i++) {

			Bet bet = betList.get(i);

			actualBankrollAmount = actualBankrollAmount - bet.getAnte();

			actualBankrollAmount = actualBankrollAmount + (bet.getAnte() * bet.getOdd());
			
			if(actualBankrollAmount > higherAmount) {
				higherAmount = actualBankrollAmount;
			}
			if(actualBankrollAmount < smallerAmount) {
				smallerAmount = actualBankrollAmount;
			}

			bankrollAmounts.add(actualBankrollAmount);

		}

		/// Calculer prochaine mise
//		Double baseBankrollAmount = initialBankAmount;
//		for (Double amount : bankrollAmounts) {
//			if (amount > baseBankrollAmount) {
//				nextAnte = amount / 20;
//			}
//		}

		infos.put("Dernier montant bankroll", String.valueOf(String.format("%.2f", actualBankrollAmount)));
		infos.put("Montant bankroll le plus elevé", String.valueOf(String.format("%.2f", higherAmount)));
		infos.put("Montant bankroll le plus bas", String.valueOf(String.format("%.2f", smallerAmount)));
		
		Double initialAnteLossNumber = (initialBankAmount - smallerAmount) / initialAnte;
		infos.put("Nombre de mises de base perdues au maximum", String.valueOf(String.format("%.2f", initialAnteLossNumber)));

		infos.put("Mise initiale", String.valueOf(String.format("%.2f", initialAnte)));
		infos.put("Prochaine mise", String.valueOf(String.format("%.2f", higherAmount/20)));

		infos.put("Nombre de paris", String.valueOf(betList.size()));

	

		infos.put("Paris gagnants", String.valueOf(wonBetsOdds.size()));
		infos.put("Paris perdants", String.valueOf(betList.stream().filter(b -> b.getStatus().equals(BetStatus.LOSE))
				.collect(Collectors.toList()).size()));
		infos.put("Pourcentage de paris gagnants", String.valueOf(Precision.round((Double) (1.0*(100 * wonBetsOdds.size()) / betList.size()), 2)));
		infos.put("Paris gagnants non-joués", String.valueOf(betList.stream().filter(b -> b.getStatus().equals(BetStatus.NOT_PLAYED_WON))
				.collect(Collectors.toList()).size()));
		infos.put("Paris perdants non-joués", String.valueOf(betList.stream().filter(b -> b.getStatus().equals(BetStatus.NOT_PLAYED_LOSE))
				.collect(Collectors.toList()).size()));
		infos.put("Paris en attente", String.valueOf(betList.stream()
				.filter(b -> b.getStatus().equals(BetStatus.PENDING)).collect(Collectors.toList()).size()));
		infos.put("Paris semi-perdants", String.valueOf(betList.stream()
				.filter(b -> b.getStatus().equals(BetStatus.SEMI)).collect(Collectors.toList()).size()));
		infos.put("Cote moyenne des paris gagnants", String.valueOf(String.format("%.2f",
				wonBetsOdds.stream().collect(Collectors.summingDouble(Double::doubleValue)) / wonBetsOdds.size())));
		infos.put("Multiplication du capital : X",
				String.valueOf(String.format("%.2f", actualBankrollAmount / initialBankrollAmount)));

		return infos;
	}

	public List<Bet> suppressNotPlayed(List<Bet> list) {

		List<Bet> newList = list.stream().filter(b -> !b.getStatus().equals(BetStatus.NOT_PLAYED_LOSE)
				&& !b.getStatus().equals(BetStatus.NOT_PLAYED_WON)).collect(Collectors.toList());
		
		currentOddsInCombis(newList);

		return newList;
	}

	public void transferToAnOtherGambler(Long bankrollId, Long gamblerId) {

		Bankroll bankroll = bankrollRepository.findById(bankrollId).get();
		Bankroll newbankroll = new Bankroll();
		Gambler gambler = gamblerRepository.findById(gamblerId).get();

		// Modifier Bankroll////////////

//	newbankroll.setId(null);
		newbankroll.setActive(bankroll.isActive());
		newbankroll.setBankrollField(bankroll.getBankrollField());
		newbankroll.setBets(new ArrayList<>());
		newbankroll.setCombis(new ArrayList<>());
		newbankroll.setEndDate(bankroll.getEndDate());
		newbankroll.setFormattedStartDate(bankroll.getFormattedStartDate());
		newbankroll.setGambler(gambler);
		newbankroll.setName(bankroll.getName());
		newbankroll.setStartAmount(bankroll.getStartAmount());
		newbankroll.setStartDate(bankroll.getStartDate());

		Bankroll savedBankroll = bankrollRepository.save(newbankroll);

		/////////// Modifier les paris///////////////
		for (Bet b : bankroll.getBets()) {

			HorseRacingBet bet = (HorseRacingBet) b;

			if (bankroll.getBankrollField().equals(BankrollField.HIPPIQUE)) {
				HorseRacingBet newBet = new HorseRacingBet();

				newBet.setAfterComment(bet.getAfterComment());
				newBet.setAnte(bet.getAnte());
				newBet.setBankroll(savedBankroll);
				newBet.setBeforeComment(bet.getBeforeComment());
				newBet.setConfidenceIndex(bet.getConfidenceIndex());
				newBet.setCurrentOddInCombi(bet.getCurrentOddInCombi());
				newBet.setDate(bet.getDate());
				newBet.setField(bet.getField());
				newBet.setFormattedDate(bet.getFormattedDate());
				newBet.setGambler(gambler);
				newBet.setOdd(bet.getOdd());
				newBet.setSelection(bet.getSelection());
				newBet.setStatus(bet.getStatus());
				newBet.setType(bet.getType());
				newBet.setDiscipline(bet.getDiscipline());
				newBet.setHasWon(bet.isHasWon());
				newBet.setMeeting(bet.getMeeting());
				newBet.setRace(bet.getRace());
				newBet.setWinOdd(bet.getWinOdd());

				HorseRacingBet savedBet = betRepository.save(newBet);
				savedBankroll.getBets().add(savedBet);
			}
		}

		bankrollRepository.save(savedBankroll);

	}

	public void currentOddsInCombis(List<Bet> bets) {

		double currentOdd = 1d;

		for (int i = 0; i < bets.size(); i++) {

			bets.get(i).setCurrentOddInCombi(currentOdd * bets.get(i).getOdd());
			currentOdd = bets.get(i).getCurrentOddInCombi();
			if(currentOdd == 0 || bets.get(i).getStatus().equals(BetStatus.LOSE)) {
				currentOdd = 1;
				bets.get(i).setCurrentOddInCombi(0d);
			}
			
		}
	}
	
	public List<Bankroll> getBankrollList (Gambler gambler) {
	
		return bankrollRepository.findAllByGamblerOrderByStartDateDesc(gambler);
	}
	
	public void deleteBankroll (Long bankrollId) {
		
		bankrollRepository.deleteById(bankrollId);
	}
}
