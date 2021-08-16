package combibet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import combibet.entity.Bankroll;
import combibet.entity.Bet;
import combibet.entity.BetType;
import combibet.entity.Combi;
import combibet.entity.Gambler;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long>{
	
	List <Bet> findByGambler(Gambler gambler);
	
	List <Bet> findAllByGamblerOrderByDateAsc(Gambler gambler);
	
	List <Bet> findAllByGamblerAndTypeOrderByDateAsc(Gambler gambler, BetType type);
	
	List <Bet> findAllByBankrollAndTypeOrderByDateAsc(Bankroll bankroll, BetType type);
	
	List <Bet> findAllByBankrollOrderByDateAsc(Bankroll bankroll);
	

}
