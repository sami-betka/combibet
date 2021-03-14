package combibet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import combibet.entity.AppRole;
import combibet.repository.AppRoleRepository;

@SpringBootApplication
public class CombibetApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CombibetApplication.class, args);
		
		AppRoleRepository appRoleRepository = ctx.getBean(AppRoleRepository.class);
		if (appRoleRepository.findAll().isEmpty() || appRoleRepository.findAll().equals(null)) {
			appRoleRepository.save(new AppRole(1l, "ROLE_ADMIN"));
			appRoleRepository.save(new AppRole(2l, "ROLE_USER"));
		}
		
	}

}
