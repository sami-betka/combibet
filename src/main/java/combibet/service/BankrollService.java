package combibet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import combibet.entity.Bet;
import combibet.entity.BetStatus;

@Service
public class BankrollService {

	public Map<String, Object> managedBankrollSimulation(List<Bet> betList,int anteDivider, Double initialBankrollAmount) {

		List<Bet> bets = betList;
		List<Bet> arrangedBets = new ArrayList<>();
		
//		Map<String, Object> finalMap = new HashMap<>();
//		finalMap.put("InitialBankrollAmount", initialBankrollAmount);


		Double actualBankrollAmount = initialBankrollAmount;
		Double topAmount = initialBankrollAmount;
		Double ante = topAmount / anteDivider;

//		bets.get(0).setAnte(ante);

		for (int i = 0; i < bets.size(); i++) {

			Bet bet = bets.get(i);
			if (!bet.getStatus().equals(BetStatus.WON)) {
				bet.setOdd(0d);
			}
			bet.setAnte(ante);
			System.out.println(i+1 + " 0) mise " + ante);

			actualBankrollAmount = actualBankrollAmount - bet.getAnte();			
			
			actualBankrollAmount = actualBankrollAmount + (bet.getAnte() * bet.getOdd());
			
			    System.out.println(i+1 + " 1) " + actualBankrollAmount);
				System.out.println(i+1 + " 2) bank actuelle " + actualBankrollAmount);
				System.out.println(i+1 + " 3) bank precedente " + topAmount);


			if (actualBankrollAmount > topAmount) {

				ante = actualBankrollAmount / anteDivider;
				
				topAmount = actualBankrollAmount;
//				System.out.print(newBankrollAmount);
			}
			
				System.out.println("");
			
				if(actualBankrollAmount<0) {
                   return null;
                  }

			arrangedBets.add(bet);
		}
		
		Map<String, Object> finalMap = new HashMap<>();
		finalMap.put("betList", arrangedBets);
		finalMap.put("initialBankrollAmount", initialBankrollAmount);
		finalMap.put("actualBankrollAmount", actualBankrollAmount);


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
		
		List<Bet> betList = (List<Bet>) map.get("betList");
		Double initialBankrollAmount = (Double) map.get("initialBankrollAmount");
		Double actualBankrollAmount = (Double) map.get("actualBankrollAmount");
		
		Double earnings = actualBankrollAmount;
		Double benefit = earnings - initialBankrollAmount;


		


		betListInfos.put("Montant bankroll initial", String.valueOf(initialBankrollAmount));
		betListInfos.put("Montant bankroll actuel", String.valueOf(actualBankrollAmount));

		betListInfos.put("Nombre de paris", String.valueOf(betList.size()));

		List<Double> wonBetsOdds = betList.stream().filter(b -> b.getStatus().equals(BetStatus.WON)).map(Bet::getOdd)
				.collect(Collectors.toList());

		betListInfos.put("Paris gagnants", String.valueOf(wonBetsOdds.size()));
		betListInfos.put("Paris perdants", String.valueOf(
				betList.stream().filter(b -> b.getStatus().equals(BetStatus.LOSE)).collect(Collectors.toList()).size()));

		betListInfos.put("Gains", String.valueOf(String.format("%.2f", earnings)));
		betListInfos.put("Bénéfice", String.valueOf(String.format("%.2f", benefit)));

		betListInfos.put("Cote moyenne des paris gagnants", String.valueOf(

				wonBetsOdds.stream().collect(Collectors.summingDouble(Double::doubleValue)) / wonBetsOdds.size()));

		betListInfos.put("Multiplication du capital : X",
				String.valueOf(String.format("%.2f", earnings / initialBankrollAmount)));

		return betListInfos;
	}
}
