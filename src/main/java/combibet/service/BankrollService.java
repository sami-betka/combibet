package combibet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.HorseRacingBet;

@Service
public class BankrollService {

	public Map<String, Object> managedBankrollSimulation(List<Bet> betList, Integer anteDivider, Double initialBankrollAmount) {

		List<Bet> bets = betList;
		List<Bet> arrangedBets = new ArrayList<>();
		LinkedList<Double> bankrollAmounts = new LinkedList<>();
		LinkedList<String> betsDates = new LinkedList<>();
		

		Double actualBankrollAmount = initialBankrollAmount;
		Double topAmount = actualBankrollAmount;
		Double ante = topAmount / anteDivider;


		for (int i = 0; i < bets.size(); i++) {

			Bet bet = bets.get(i);
			if (!bet.getStatus().equals(BetStatus.WON) && !bet.getStatus().equals(BetStatus.SEMI)) {
				bet.setOdd(0d);
			}
			bet.setAnte(ante);
//			System.out.println(i+1 + " 0) mise " + ante);

			actualBankrollAmount = actualBankrollAmount - bet.getAnte();			
			
			actualBankrollAmount = actualBankrollAmount + (bet.getAnte() * bet.getOdd());
			
//			    System.out.println(i+1 + " 1) " + String.format("%.2f", actualBankrollAmount));
//				System.out.println(i+1 + " 2) bank actuelle " + String.format("%.2f", actualBankrollAmount));
//				System.out.println(i+1 + " 3) bank max precedente " + String.format("%.2f", topAmount));

				bankrollAmounts.add(actualBankrollAmount);
				betsDates.add(String.valueOf(bet.getDate()));

			if (actualBankrollAmount > topAmount) {

				ante = actualBankrollAmount / anteDivider;
				
				topAmount = actualBankrollAmount;
			}
			
//				System.out.println("");
			
//				if(actualBankrollAmount<0) {
//                   return null;
//                  }

			arrangedBets.add(bet);
		}
		
		Map<String, Object> finalMap = new HashMap<>();
		finalMap.put("betList", arrangedBets);
		finalMap.put("initialBankrollAmount", initialBankrollAmount);
		finalMap.put("actualBankrollAmount", actualBankrollAmount);
		finalMap.put("initialAnte", initialBankrollAmount/anteDivider );
		finalMap.put("divider",anteDivider );

		
		///////////////////Dashboard Infos
		
		finalMap.put("bankrollAmounts", bankrollAmounts);
		finalMap.put("betsDates", betsDates);

		
		return finalMap;
	}
	
	public Map<String, Object> winManagedBankrollSimulation(List<Bet> betList, Integer anteDivider, Double initialBankrollAmount) {

		List<Bet> bets = betList;
		List<Bet> arrangedBets = new ArrayList<>();
		LinkedList<Double> bankrollAmounts = new LinkedList<>();
		LinkedList<String> betsDates = new LinkedList<>();
		
//		Map<String, Object> finalMap = new HashMap<>();
//		finalMap.put("InitialBankrollAmount", initialBankrollAmount);

//        Double initialBankrollAmount = bets.get(0).getBankroll().getStartAmount();
		Double actualBankrollAmount = initialBankrollAmount;
		Double topAmount = actualBankrollAmount;
		Double ante = topAmount / anteDivider;

//		bets.get(0).setAnte(ante);

		for (int i = 0; i < bets.size(); i++) {

			Bet bet = bets.get(i);
			bet.setType(BetType.SIMPLE_GAGNANT);
			HorseRacingBet hrb = (HorseRacingBet) bet;
			
			if (!bet.getStatus().equals(BetStatus.WON)) {
				bet.setOdd(0d);
			}
			if(hrb.isHasWon()==false) {
				bet.setOdd(0d);
				bet.setStatus(BetStatus.LOSE);
			}else {
				hrb.setOdd(hrb.getWinOdd());
			}
			bet.setAnte(ante);
			System.out.println(i+1 + " 0) mise " + ante);

			actualBankrollAmount = actualBankrollAmount - bet.getAnte();			
			
			actualBankrollAmount = actualBankrollAmount + (bet.getAnte() * bet.getOdd());
			
			    System.out.println(i+1 + " 1) " + String.format("%.2f", actualBankrollAmount));
				System.out.println(i+1 + " 2) bank actuelle " + String.format("%.2f", actualBankrollAmount));
				System.out.println(i+1 + " 3) bank max precedente " + String.format("%.2f", topAmount));

				bankrollAmounts.add(actualBankrollAmount);
				betsDates.add(String.valueOf(bet.getDate()));

			if (actualBankrollAmount > topAmount) {

				ante = actualBankrollAmount / anteDivider;
				
				topAmount = actualBankrollAmount;
			}
			
				System.out.println("");
			
//				if(actualBankrollAmount<0) {
//                   return null;
//                  }

			arrangedBets.add(bet);
		}
		
		Map<String, Object> finalMap = new HashMap<>();
		finalMap.put("betList", arrangedBets);
		finalMap.put("initialBankrollAmount", initialBankrollAmount);
		finalMap.put("actualBankrollAmount", actualBankrollAmount);
		finalMap.put("initialAnte", initialBankrollAmount/anteDivider );
		finalMap.put("divider",anteDivider );

		
		///////////////////Dashboard Infos
		
		finalMap.put("bankrollAmounts", bankrollAmounts);
		finalMap.put("betsDates", betsDates);

		



//		return arrangedBets;
		return finalMap;
	}

	
	public LinkedHashMap<String, String> betListInfos(Map<String, Object> map) {

		LinkedHashMap<String, String> betListInfos = new LinkedHashMap<>();


//		Double earnings = 0d;
//		for (Bet b : bets) {
//			earnings = earnings + (b.getOdd() * b.getAnte());
//			if(b.getStatus().equals(BetStatus.LOSE)) {
//				earnings = earnings - b.getAnte();
//			}
//		}
//		Double benefit = earnings - bankrollAmount;
		
		System.out.println(map.size() + " !!!");
		
		List<Bet> betList = (List<Bet>) map.get("betList");
		Double initialBankrollAmount = (Double) map.get("initialBankrollAmount");
		Double actualBankrollAmount = (Double) map.get("actualBankrollAmount");
		Double initialAnte = (Double) map.get("initialAnte");
		Integer divider = (Integer) map.get("divider");
		
		Double earnings = actualBankrollAmount;
		Double benefit = earnings - initialBankrollAmount;


		


		betListInfos.put("Montant bankroll initial", String.valueOf(initialBankrollAmount));
		betListInfos.put("Montant bankroll actuel", String.valueOf(String.format("%.2f", actualBankrollAmount)));
		betListInfos.put("Mise initiale", String.valueOf(String.format("%.2f", initialAnte)));
		betListInfos.put("Diviseur", String.valueOf(divider));

		betListInfos.put("Nombre de paris", String.valueOf(betList.size()));

		List<Double> wonBetsOdds = betList.stream().filter(b -> b.getStatus().equals(BetStatus.WON)).map(Bet::getOdd)
				.collect(Collectors.toList());

		betListInfos.put("Paris gagnants", String.valueOf(wonBetsOdds.size()));
		betListInfos.put("Paris perdants", String.valueOf(
				betList.stream().filter(b -> b.getStatus().equals(BetStatus.LOSE)).collect(Collectors.toList()).size()));

//		betListInfos.put("Gains", String.valueOf(String.format("%.2f", earnings)));
		betListInfos.put("Bénéfice", String.valueOf(String.format("%.2f", benefit)));

		betListInfos.put("Cote moyenne des paris gagnants", String.valueOf(String.format("%.2f", wonBetsOdds.stream()
				.collect(Collectors.summingDouble(Double::doubleValue)) / wonBetsOdds.size())));

		betListInfos.put("Multiplication du capital : X",
				String.valueOf(String.format("%.2f", earnings / initialBankrollAmount)));

		return betListInfos;
	}
	
	public LinkedHashMap<String, String> betListInfosSimulation(Map<String, Object> map) {

		LinkedHashMap<String, String> betListInfos = new LinkedHashMap<>();


//		Double earnings = 0d;
//		for (Bet b : bets) {
//			earnings = earnings + (b.getOdd() * b.getAnte());
//			if(b.getStatus().equals(BetStatus.LOSE)) {
//				earnings = earnings - b.getAnte();
//			}
//		}
//		Double benefit = earnings - bankrollAmount;
		
		System.out.println(map.size() + " !!!");

		
		List<Bet> betList = (List<Bet>) map.get("betList");
		Double initialBankrollAmount = (Double) map.get("initialBankrollAmount");
		Double actualBankrollAmount = (Double) map.get("actualBankrollAmount");
		Double initialAnte = (Double) map.get("initialAnte");
		Integer divider = (Integer) map.get("divider");
		
		Double earnings = actualBankrollAmount;
		Double benefit = earnings - initialBankrollAmount;


		


		betListInfos.put("Montant bankroll initial", String.valueOf(initialBankrollAmount));
		betListInfos.put("Montant bankroll actuel", String.valueOf(String.format("%.2f", actualBankrollAmount)));
		betListInfos.put("Mise initiale", String.valueOf(String.format("%.2f", initialAnte)));
		betListInfos.put("Diviseur", String.valueOf(divider));

		betListInfos.put("Nombre de paris", String.valueOf(betList.size()));

		List<Double> wonBetsOdds = betList.stream().filter(b -> b.getStatus().equals(BetStatus.WON)).map(Bet::getOdd)
				.collect(Collectors.toList());

		betListInfos.put("Paris gagnants", String.valueOf(wonBetsOdds.size()));
		betListInfos.put("Paris perdants", String.valueOf(
				betList.stream().filter(b -> b.getStatus().equals(BetStatus.LOSE)).collect(Collectors.toList()).size()));

//		betListInfos.put("Gains", String.valueOf(String.format("%.2f", earnings)));
		betListInfos.put("Bénéfice", String.valueOf(String.format("%.2f", benefit)));

		betListInfos.put("Cote moyenne des paris gagnants", String.valueOf(String.format("%.2f", wonBetsOdds.stream()
				.collect(Collectors.summingDouble(Double::doubleValue)) / wonBetsOdds.size())));

		betListInfos.put("Multiplication du capital : X",
				String.valueOf(String.format("%.2f", earnings / initialBankrollAmount)));

		return betListInfos;
	}
	
	public LinkedHashMap<String, String> betsInfos (List<Bet> betList, Double initialBankAmount){
		
		LinkedHashMap<String, String> infos = new LinkedHashMap<>();
		Double initialBankrollAmount = initialBankAmount;
		Double actualBankrollAmount = initialBankrollAmount;

		infos.put("Montant bankroll initial", String.valueOf(actualBankrollAmount));

		
		LinkedList<Double> bankrollAmounts = new LinkedList<>();
		
		bankrollAmounts.add(actualBankrollAmount);

		
		for(int i = 0; i < betList.size(); i++) {
			
			Bet bet = betList.get(i);
			
            actualBankrollAmount = actualBankrollAmount - bet.getAnte();			
			
			actualBankrollAmount = actualBankrollAmount + (bet.getAnte() * bet.getOdd());
			
			bankrollAmounts.add(actualBankrollAmount);

		}
		
		infos.put("Dernier montant bankroll",  String.valueOf(String.format("%.2f", actualBankrollAmount)));
		infos.put("Nombre de paris", String.valueOf(betList.size()));

		List<Double> wonBetsOdds = betList.stream().filter(b -> b.getStatus().equals(BetStatus.WON)).map(Bet::getOdd)
				.collect(Collectors.toList());

		infos.put("Paris gagnants", String.valueOf(wonBetsOdds.size()));
		infos.put("Paris perdants", String.valueOf(
				betList.stream().filter(b -> b.getStatus().equals(BetStatus.LOSE)).collect(Collectors.toList()).size()));
		infos.put("Paris semi-perdants", String.valueOf(
				betList.stream().filter(b -> b.getStatus().equals(BetStatus.SEMI)).collect(Collectors.toList()).size()));
		infos.put("Cote moyenne des paris gagnants", String.valueOf(String.format("%.2f", wonBetsOdds.stream()
				.collect(Collectors.summingDouble(Double::doubleValue)) / wonBetsOdds.size())));
		infos.put("Multiplication du capital : X",
				String.valueOf(String.format("%.2f", actualBankrollAmount / initialBankrollAmount)));
		
		return infos;
	}
}