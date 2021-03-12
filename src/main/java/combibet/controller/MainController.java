package combibet.controller;

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
	public String home() {
		
		return "dashboard";
	}
	

}
