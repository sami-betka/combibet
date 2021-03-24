package combibet.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import combibet.entity.Gambler;
import combibet.repository.BankrollRepository;
import combibet.repository.GamblerRepository;

@Controller
public class BankrollController {
	
	@Autowired
	BankrollRepository bankrollRepository;
	
	@Autowired
	GamblerRepository gamblerRepository;
	
	
	@GetMapping("/bankroll-list")
	public String getBankrollList (Model model, Principal principal) {
				
		if (principal == null) {
			return "redirect:/login";
		}
		
		Gambler gambler = gamblerRepository.findByUserName(principal.getName());
		
//		List<Bet> betList = betRepository.findAll();
		
		model.addAttribute("betList", bankrollRepository.findAllByGamblerOrderByStartDateDesc(gambler));
//		model.addAttribute("betList", betRepository.findAllBetsByOrderByIdAsc());

		model.addAttribute("active", true);

		return "bankroll-list";
	}

}
