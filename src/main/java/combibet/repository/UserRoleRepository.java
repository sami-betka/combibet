package combibet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import combibet.entity.Gambler;
import combibet.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
	
	List<UserRole> findByUser(Gambler user);
	
	List<UserRole> deleteByUser (Gambler user);

}
