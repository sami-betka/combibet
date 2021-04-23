package combibet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import combibet.entity.Gambler;

@Repository
public interface GamblerRepository extends JpaRepository<Gambler, Long>{

	Gambler findByUserName(String userName);
	
	
	
}
