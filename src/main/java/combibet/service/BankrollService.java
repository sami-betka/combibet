package combibet.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import combibet.entity.Bet;
import combibet.entity.BetStatus;

@Service
public class BankrollService {

	public List<Bet> managedBankrollSimulation(List<Bet> betList,int anteDivider, Double InitialBankrollAmount) {

		List<Bet> bets = betList;
		List<Bet> arrangedBets = new ArrayList<>();

		Double bAmount = InitialBankrollAmount;
		Double ante = bAmount / anteDivider;

//		bets.get(0).setAnte(ante);

		for (int i = 0; i < bets.size(); i++) {

			Bet bet = bets.get(i);
			if (!bet.getStatus().equals(BetStatus.WON)) {
				bet.setOdd(0d);
			}
			bet.setAnte(ante);
			System.out.println(i+1 + " 5) mise " + ante);

			Double actualBankrollAmount = bAmount - bet.getAnte();			
			
			actualBankrollAmount = actualBankrollAmount + (bet.getAnte() * bet.getOdd());
			
			    System.out.println(i+1 + " 1) " + actualBankrollAmount);
				System.out.println(i+1 + " 2) bank actuelle " + actualBankrollAmount);
				System.out.println(i+1 + " 3) bank precedente " + bAmount);


			if (actualBankrollAmount > bAmount) {

				ante = actualBankrollAmount / anteDivider;
				
				

				bAmount = actualBankrollAmount;
//				System.out.print(newBankrollAmount);
			}
			
				System.out.println("");
			
				if(bAmount<0) {
                   return null;
                  }

			arrangedBets.add(bet);
		}

		return arrangedBets;
	}

	public LinkedHashMap<String, String> betListInfos(List<Bet> bets, Double bankrollAmount) {

		LinkedHashMap<String, String> betListInfos = new LinkedHashMap<>();

		Double earnings = 0d;
		for (Bet b : bets) {
			earnings = earnings + b.getOdd() * b.getAnte();
		}
		Double benefit = earnings - bankrollAmount;

		betListInfos.put("Montant bankroll initial", String.valueOf(bankrollAmount));
		betListInfos.put("Nombre de paris", String.valueOf(bets.size()));

		List<Double> wonBetsOdds = bets.stream().filter(b -> b.getStatus().equals(BetStatus.WON)).map(Bet::getOdd)
				.collect(Collectors.toList());

		betListInfos.put("Paris gagnants", String.valueOf(wonBetsOdds.size()));
		betListInfos.put("Paris perdants", String.valueOf(
				bets.stream().filter(b -> b.getStatus().equals(BetStatus.LOSE)).collect(Collectors.toList()).size()));

		betListInfos.put("Gains", String.valueOf(String.format("%.2f", earnings)));
		betListInfos.put("Bénéfice", String.valueOf(String.format("%.2f", benefit)));

		betListInfos.put("Cote moyenne des paris gagnants", String.valueOf(

				wonBetsOdds.stream().collect(Collectors.summingDouble(Double::doubleValue)) / wonBetsOdds.size()));

		betListInfos.put("Multiplication du capital : X",
				String.valueOf(String.format("%.2f", earnings / bankrollAmount)));

		return betListInfos;
	}
}
