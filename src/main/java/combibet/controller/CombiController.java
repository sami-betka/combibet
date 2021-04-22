package combibet.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.HorseRacingBet;
import combibet.repository.HorseRacingBetRepository;


@Controller
public class CombiController {
	
	@Autowired
	HorseRacingBetRepository horseRacingBetRepository;
	
	@RequestMapping(value = "/edit-bet")
	public String editHorseRacingBet(@RequestParam("id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		HorseRacingBet bet = horseRacingBetRepository.findById(id).get();

		model.addAttribute("bet", bet);
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
//		navbarAttributes(model, principal);
		return "update-horse-racing-bet";

	}

	@PostMapping(value = "/update-bet")
	public String updateUser(HorseRacingBet bet, Model model, BindingResult bindingresult, Principal principal,RedirectAttributes redirect) {
		if (bindingresult.hasErrors()) {
			redirect.addFlashAttribute("createsuccess", true);

			return "redirect:/edit-bet?=" + bet.getId();
		}
		
		if (principal == null) {
			return "redirect:/login";
		}

		HorseRacingBet hrb =  horseRacingBetRepository.findById(bet.getId()).get();
		System.out.println(hrb.getCombi());
		horseRacingBetRepository.save(bet);

		redirect.addFlashAttribute("show", hrb.getCombi().getId());
		
		return "redirect:/bankroll-details?=" + hrb.getCombi().getBankroll().getId();
		
	}
}
