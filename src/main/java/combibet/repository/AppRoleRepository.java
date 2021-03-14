package combibet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import combibet.entity.AppRole;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {

//	 @Query("Select ur.appRole.roleName from " + UserRole.class.getName() + " ur " 
//	          + " where ur.appUser.userId = :userId ")

//	@Query("SELECT a FROM Affair a LEFT JOIN a.manager u1 LEFT JOIN a.contributions c LEFT JOIN c.moContributors moc "
//			+ "WHERE (u1.nni = :nni OR moc.nni = :nni) " + "AND (:status is null OR a.status=:status)")
	
	@Query("SELECT ur.appRole.roleName FROM UserRole ur "
			+ "WHERE ur.user.id = :userId")
	public List<String> findAllByUserId(Long userId);

}