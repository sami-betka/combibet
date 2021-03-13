package combibet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GamblerController {
	
	
	@GetMapping("/my-infos")
	public String getMyInfos (Model model) {
		
		model.addAttribute("active", true);

		return "user";
	}
	

}
