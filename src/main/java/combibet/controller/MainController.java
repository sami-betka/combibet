package combibet.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import combibet.entity.Gambler;
import combibet.entity.UserRole;
import combibet.repository.GamblerRepository;
import combibet.repository.UserRoleRepository;

@Controller
public class MainController {
		
	@Autowired
	GamblerRepository gamblerRepository;
	
	@Autowired
	UserRoleRepository userRoleRepository;
	
	@GetMapping
	public String home () {
		return "redirect:/dashboard";
	}
	
//	@GetMapping("/choose-field")
//	public String chooseField () {
//		
//		return "choose-field";
//	}

	@GetMapping("/dashboard")
	public String home(Model model) {
		
		Map<String, Double> surveyMap = new LinkedHashMap<>();
		surveyMap.put("1", 1d);
		surveyMap.put("2", 3d);
		surveyMap.put("3", 5d);
		surveyMap.put("4", 2d);
		surveyMap.put("5", 4d);
		surveyMap.put("6", 8d);
		surveyMap.put("7", 2d);
		surveyMap.put("8", 3d);
		surveyMap.put("9", 7d);
	model.addAttribute("surveyMap", surveyMap);
	
//	model.addAttribute("keys", surveyMap.keySet());
//	model.addAttribute("values", surveyMap.values());

	
	model.addAttribute("active", true);
		
		return "dashboard";
	}
	
	
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model, Principal principal) {
    	
    	if(principal != null) {
    		return "redirect:/bankroll-list";
    	}
 
        return "login";
    }
    
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model, Principal principal) {
        model.addAttribute("title", "Logout");
//        navbarAttributes(model, principal);
		return "redirect:/bet-list";

//        return "logoutSuccessfulPage";
    }
    
    @RequestMapping(value = "/userAccountInfo", method = RequestMethod.GET)
    public String loginSuccess(Model model, Principal principal) {
    	
    	Gambler user = gamblerRepository.findByUserName(principal.getName());
    	for(UserRole userRole: userRoleRepository.findAll()) {
    		if(userRole.getAppRole().getRoleId() ==1 && userRole.getUser().getId()==user.getId()) {
    			return "redirect:/admingate";
    		}
    	}
		return "redirect:/bankroll-list";
    }
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
