package combibet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import combibet.entity.Bankroll;
import combibet.entity.Gambler;

@Repository
public interface BankrollRepository extends JpaRepository<Bankroll, Long>{

	List <Bankroll> findAllByGamblerOrderByStartDateDesc(Gambler gambler);

	
}
