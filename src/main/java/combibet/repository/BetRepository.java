package combibet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import combibet.entity.Bet;

public interface BetRepository  extends JpaRepository<Bet, Long>{
	
	List <Bet> findAllBetsByOrderByIdAsc();
	

}
