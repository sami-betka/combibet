package combibet.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.Combi;
import combibet.repository.CombiRepository;
import combibet.repository.HorseRacingBetRepository;


@Controller
public class CombiController {
	
	@Autowired
	HorseRacingBetRepository horseRacingBetRepository;
	
	@Autowired
	CombiRepository combiRepository;

	@RequestMapping("/delete-combi")
	public String deleteCombi(@RequestParam("id") Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {

		Combi combi = combiRepository.findById(id).get();
		
		Long bankrollId = combi.getBankroll().getId();

		combiRepository.deleteById(combi.getId());

//		Combi combi = combiRepository.findById(bet.getCombi().getId()).get();
//		for (Bet b : combi.getBets()) {
//			if (b.getStatus().equals(BetStatus.LOSE)) {
//				combi.setCurrent(false);
//				combiRepository.save(combi);
//			}
//		}
//		if(bet.getStatus().equals(BetStatus.LOSE)) {
//			combi.setCurrent(true);
//			combiRepository.save(combi);
//		}
		
//		redirectAttributes.addFlashAttribute("show", bet.getCombi().getId());

		return "redirect:/bankroll-details?id=" + bankrollId;
	}
	
}
