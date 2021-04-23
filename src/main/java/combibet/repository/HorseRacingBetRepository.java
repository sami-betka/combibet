package combibet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import combibet.entity.HorseRacingBet;

@Repository
public interface HorseRacingBetRepository extends JpaRepository<HorseRacingBet, Long>{

}
