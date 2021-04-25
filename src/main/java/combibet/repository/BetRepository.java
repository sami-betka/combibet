package combibet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import combibet.entity.Bet;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long>{

}
