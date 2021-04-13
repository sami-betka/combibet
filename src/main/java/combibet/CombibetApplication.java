package combibet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import combibet.entity.AppRole;
import combibet.entity.Gambler;
import combibet.repository.AppRoleRepository;
import combibet.repository.GamblerRepository;
import combibet.utils.EncrytedPasswordUtils;

@SpringBootApplication
public class CombibetApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CombibetApplication.class, args);
		
		GamblerRepository gamblerRepository = ctx.getBean(GamblerRepository.class);
		if (gamblerRepository.findByUserName("tetedestup") == null) {
			Gambler gambler = new Gambler();
			gambler.setUserName("tetedestup");
			gambler.setPassword(EncrytedPasswordUtils.encrytePassword("123"));
			gamblerRepository.save(gambler);
		}
		
		AppRoleRepository appRoleRepository = ctx.getBean(AppRoleRepository.class);
		if (appRoleRepository.findAll().isEmpty() || appRoleRepository.findAll().equals(null)) {
			appRoleRepository.save(new AppRole(1l, "ROLE_ADMIN"));
			appRoleRepository.save(new AppRole(2l, "ROLE_USER"));
		}
		
	}

}
