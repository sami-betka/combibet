package combibet.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import combibet.repository.BetRepository;

@Controller
public class MainController {
	
	@Autowired
	BetRepository betRepository;
	
	@GetMapping
	public String home () {
		return "redirect:/dashboard";
	}

	@GetMapping("/dashboard")
	public String home(Model model) {
		
		Map<Long, Double> surveyMap = new LinkedHashMap<>();
		surveyMap.put(1l, 1d);
		surveyMap.put(3l, 3d);
		surveyMap.put(5l, 5d);
	model.addAttribute("surveyMap", surveyMap);
	
	model.addAttribute("active", true);
		
		return "dashboard";
	}
	
	
//	@GetMapping("/buttons.html")
//	public String getButtons () {
//		
//		return "buttons";
//	}
//	@GetMapping("/cards.html")
//	public String getCards () {
//		
//		return "cards";
//	}
	
//	@GetMapping("/tables.html")
//	public String getTables (Model model) {
//		
////		List<Bet> betList = betRepository.findAll();
//		
////		model.addAttribute("user", userRepository.findById(1l).get());
//		model.addAttribute("betList", betRepository.findAllBetsByOrderByDateDesc() );
//
//		return "tables";
//	}
	
//	@GetMapping("/charts.html")
//	public String getChartsPage () {
//		
//		return "charts";
//	}
//	
//	@GetMapping("/404.html")
//	public String get404 () {
//		
//		return "404";
//	}
	
	@GetMapping("/login.html")
	public String getLoginPage () {
		
		return "login";
	}
	
	@GetMapping("/register.html")
	public String getRegisterPage () {
		
		return "register";
	}
	
	@GetMapping("/forgot-password.html")
	public String getForgotPasswordPage () {
		
		return "forgot-password";
	}
	
	@GetMapping("/blank.html")
	public String getBlankPage () {
		
		return "blank";
	}
	
	@GetMapping("/utilities-animation.html")
	public String getAnimations () {
		
		return "utilities-animation";
	}
	
	@GetMapping("/utilities-border.html")
	public String getBorders () {
		
		return "utilities-border";
	}
	
	@GetMapping("/utilities-color.html")
	public String getColors () {
		
		return "utilities-color";
	}
	@GetMapping("/utilities-other.html")
	public String getOthers () {
		
		return "utilities-other";
	}
	

}
