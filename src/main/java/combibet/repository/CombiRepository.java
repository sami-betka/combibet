package combibet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import combibet.entity.Bankroll;
import combibet.entity.Combi;

@Repository
public interface CombiRepository extends JpaRepository<Combi, Long> {
	
	List <Combi> findAllByBankrollOrderByStartDateAsc(Bankroll bankroll);


}
