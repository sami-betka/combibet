package combibet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import combibet.entity.Combi;

@Repository
public interface CombiRepository extends JpaRepository<Combi, Long> {

}
