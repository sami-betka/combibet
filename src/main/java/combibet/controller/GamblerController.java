package combibet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GamblerController {
	
	
	@GetMapping("/my-infos")
	public String getMyInfos () {
		
		return "user";
	}
	

}
