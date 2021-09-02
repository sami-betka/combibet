package combibet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.ConfidenceIndex;
import combibet.entity.Discipline;
import combibet.entity.HorseRacingBet;

@Repository
public interface HorseRacingBetRepository extends JpaRepository<HorseRacingBet, Long>{

	
	@Query("SELECT b FROM Bet b "
			+ "WHERE b.bankroll.id = :bankroll_id "  
			+ "AND (:type is null or b.type = :type) "    
//			+ "AND b.field = 'hippique' "
			+ "AND (:discipline is null or b.discipline = :discipline) "   
			+ "AND (:status is null or b.status = :status) "   
			+ "AND (:confidenceIndex is null or b.confidenceIndex = :confidenceIndex) "
			+ "ORDER BY b.date")	 
	
	List<Bet> filterSearch(Long bankroll_id, BetType type, Discipline discipline, BetStatus status, ConfidenceIndex confidenceIndex);
}
