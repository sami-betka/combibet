package combibet.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import combibet.entity.HorseRacingBet;
import combibet.repository.HorseRacingBetRepository;

@Controller
public class BetController {
	
	@Autowired
	HorseRacingBetRepository horseRacingBetRepository;

	@RequestMapping("/delete-bet")
	public String deleteBet(@RequestParam("id") Long id, Model model, Principal principal) {
		
		HorseRacingBet hrb = horseRacingBetRepository.findById(id).get();	
		
		horseRacingBetRepository.deleteById(hrb.getId());
		
		return "redirect:/bankroll-details?id=" + hrb.getCombi().getBankroll().getId();
	}
	
}
