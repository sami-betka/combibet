package combibet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import combibet.entity.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long>{

	List<Rule> findAllByGambler(String gambler);
	
}
