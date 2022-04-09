package combibet;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import combibet.entity.Bankroll;
import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.HorseRacingBet;
import combibet.repository.BankrollRepository;
import combibet.repository.BetRepository;

@SpringBootApplication
public class CombibetApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CombibetApplication.class, args);
		
		
		BankrollRepository bankrollRepository = ctx.getBean(BankrollRepository.class);
		BetRepository betRepository = ctx.getBean(BetRepository.class);
		
//		Bankroll bank = bankrollRepository.findByName("PMU QUOTIDIEN STOP À 0,80 OU 1,350 DU CAPITAL");
//		bank.setName("PMU QUOTIDIEN STOP À 0,80 OU 1,50 DU CAPITAL");
//		bankrollRepository.save(bank);
		
		
		Bankroll test = bankrollRepository.findByName("TEST");
		List<Bet> list = test.getBets();
		list.
//		stream().filter(b->b.getOdd() != 2).
		forEach(b->{
//			b.setOdd(1.8);
//			betRepository.save(b);
			betRepository.delete(b);
		});
		
//		for(int z = 1; z<29; z=+2) {
//
//			for(int i = 0; i<3; i++) {
//		    LocalDateTime date = LocalDateTime.of(2022, 4, z, 00, 00);  
//
//			Bet bet = new HorseRacingBet();
//			bet.setSelection(String.valueOf(i));
//			bet.setOdd(2);
//			bet.setAnte(1);
//			bet.setDate(date);
//			bet.setStatus(BetStatus.WON);
//			bet.setBankroll(test);
//			
//			betRepository.save(bet);
//			test.getBets().add(bet);
//			bankrollRepository.save(test);
//		}
//			for(int i = 0; i<2; i++) {
//			    LocalDateTime date = LocalDateTime.of(2022, 4, z, 00, 00);  
//
//				Bet bet = new HorseRacingBet();
//				bet.setSelection(String.valueOf(i));
//				bet.setOdd(2);
//				bet.setAnte(1);
//				bet.setDate(date);
//				bet.setStatus(BetStatus.LOSE);
//				bet.setBankroll(test);
//				
//				betRepository.save(bet);
//				test.getBets().add(bet);
//				bankrollRepository.save(test);
//			}
//		}
		
		System.out.println("STOP");


		
		
		
//		List<Bet> list = betRepository.findAll().stream().filter(b->b.getDate() == null).collect(Collectors.toList());
//		for(Bet b : list){
//			System.out.println(b.getBankroll().getName());
//			System.out.println(b.getSelection());
//			System.out.println(b.getId());
//			
//		}
//////		
//		all.forEach(bet->{
////			if(bet.getConfidenceIndex() == null) {
////				bet.setConfidenceIndex(ConfidenceIndex.NON_RENSEIGNE);
////				betRepository.save(bet);
////			}
//			if(bet.getBankroll().getId() == 61l) {
////				betRepository.delete(bet);
//			}
//		});
		
//
//		Bankroll bank = bankrollRepository.findById(63l).get();
//		List<Bet> toDelete = new ArrayList<>();
//		for(Bet bet : bank.getBets()) {
//			toDelete.add(bet);
//		}
//		betRepository.deleteAll(toDelete);
//		
//		List<Bet> betList = new ArrayList<>();;
//		Bankroll bank2 = bankrollRepository.findById(54l).get();
//		List<Bet> betList2 = bank2.getBets();
//		Bankroll bank3 = bankrollRepository.findById(55l).get();
//		List<Bet> betList3 = bank3.getBets();
//		Bankroll bank4 = bankrollRepository.findById(61l).get();
//		List<Bet> betList4 = bank4.getBets();
//
//		for (Bet b : betList2) {
//			HorseRacingBet bet = (HorseRacingBet) b;
//
//			HorseRacingBet newBet = new HorseRacingBet();
//			newBet.setAnte(bet.getAnte());
//			newBet.setBankroll(bank);
//			newBet.setConfidenceIndex(bet.getConfidenceIndex());
//			newBet.setDate(bet.getDate());
//			newBet.setGambler(bet.getGambler());
//			newBet.setOdd(bet.getOdd());
//			newBet.setSelection(bet.getSelection());
//			newBet.setStatus(bet.getStatus());
//			newBet.setType(bet.getType());
//			newBet.setField(bet.getField());
//			newBet.setDiscipline(bet.getDiscipline());
//			newBet.setFormattedDate(bet.getFormattedDate());
//			newBet.setId(null);
//			
//			Bet savedBet = betRepository.save(newBet);
//			
//			betList.add(savedBet);
//		}
//		for (Bet b : betList3) {
//			HorseRacingBet bet = (HorseRacingBet) b;
//
//			HorseRacingBet newBet = new HorseRacingBet();
//			newBet.setAnte(bet.getAnte());
//			newBet.setBankroll(bank);
//			newBet.setConfidenceIndex(bet.getConfidenceIndex());
//			newBet.setDate(bet.getDate());
//			newBet.setGambler(bet.getGambler());
//			newBet.setOdd(bet.getOdd());
//			newBet.setSelection(bet.getSelection());
//			newBet.setStatus(bet.getStatus());
//			newBet.setType(bet.getType());
//			newBet.setField(bet.getField());
//			newBet.setDiscipline(bet.getDiscipline());
//			newBet.setFormattedDate(bet.getFormattedDate());
//			newBet.setId(null);
//			
//			Bet savedBet = betRepository.save(newBet);
//			
//			betList.add(savedBet);
//		}
//		for (Bet b : betList4) {
//			HorseRacingBet bet = (HorseRacingBet) b;
//
//			HorseRacingBet newBet = new HorseRacingBet();
//			newBet.setAnte(bet.getAnte());
//			newBet.setBankroll(bank);
//			newBet.setConfidenceIndex(bet.getConfidenceIndex());
//			newBet.setDate(bet.getDate());
//			newBet.setGambler(bet.getGambler());
//			newBet.setOdd(bet.getOdd());
//			newBet.setSelection(bet.getSelection());
//			newBet.setStatus(bet.getStatus());
//			newBet.setType(bet.getType());
//			newBet.setField(bet.getField());
//			newBet.setDiscipline(bet.getDiscipline());
//			newBet.setFormattedDate(bet.getFormattedDate());
//			newBet.setId(null);
//			
//			Bet savedBet = betRepository.save(newBet);
//			
//			betList.add(savedBet);
//		}
//	
//		bank.setBets(betList);
//		bankrollRepository.save(bank);
//		
//		System.out.println("Stop");

		
		
		
		
		
		
		
		
		
		
		//
//		
//		for(Bet bet: ) {
//			bet.setConfidenceIndex(ConfidenceIndex.FIVE);
//		} 
		
//		Bankroll bank = br.findById(44l).get();
//		bank.setName("deuxième bankroll");
//		br.save(bank);
		
//		GamblerRepository gamblerRepository = ctx.getBean(GamblerRepository.class);
//		if (gamblerRepository.findByUserName("tetedestup") == null) {
//			Gambler gambler = new Gambler();
//			gambler.setUserName("tetedestup");
//			gambler.setPassword(EncrytedPasswordUtils.encrytePassword("123"));
//			gamblerRepository.save(gambler);
//		}
//		
//		AppRoleRepository appRoleRepository = ctx.getBean(AppRoleRepository.class);
//		if (appRoleRepository.findAll().isEmpty() || appRoleRepository.findAll().equals(null)) {
//			appRoleRepository.save(new AppRole(1l, "ROLE_ADMIN"));
//			appRoleRepository.save(new AppRole(2l, "ROLE_USER"));
//		}
		
//		HorseRacingBetRepository betRepository = ctx.getBean(HorseRacingBetRepository.class);
//		List<HorseRacingBet> list = betRepository.findAll();
//		
//    	ConfidenceIndex ci = ConfidenceIndex.FIVE;
//		for(HorseRacingBet bet : list) {
//		if(bet.getDiscipline() == null) {
//			
//			
//			bet.setDiscipline(Discipline.NON_RENSEIGNE);;
//			betRepository.save(bet);
		
		////////////////////
		}		
	}
		
