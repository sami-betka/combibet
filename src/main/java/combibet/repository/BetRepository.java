package combibet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import combibet.entity.Bet;
import combibet.entity.Gambler;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long>{
	
	List <Bet> findByGambler(Gambler gambler);

}
