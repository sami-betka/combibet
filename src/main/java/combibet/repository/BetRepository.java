package combibet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import combibet.entity.Bet;

public interface BetRepository  extends JpaRepository<Bet, Long>{

}
