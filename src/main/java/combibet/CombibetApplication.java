package combibet;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.ConfidenceIndex;
import combibet.entity.Discipline;
import combibet.entity.HorseRacingBet;
import combibet.repository.BetRepository;
import combibet.repository.HorseRacingBetRepository;

@SpringBootApplication
public class CombibetApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CombibetApplication.class, args);
		
//		BankrollRepository bankrollRepository = ctx.getBean(BankrollRepository.class);
//		Bankroll bank = bankrollRepository.findById(2l).get();
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
		}		
	}
		
