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

import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.Combi;
import combibet.entity.HorseRacingBet;
import combibet.repository.BetRepository;
import combibet.repository.CombiRepository;

@Controller
public class BetController {
	
	@Autowired
	BetRepository betRepository;
	
	@Autowired
	CombiRepository combiRepository;
	
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
		hrb.setDate(bet.getDate());
		hrb.setType(bet.getType());
		hrb.setSelection(bet.getSelection());
		hrb.setOdd(bet.getOdd());
		hrb.setStatus(bet.getStatus());
		
//		System.out.println(hrb.toString());
		horseRacingBetRepository.save(hrb);
		
		if(bet.getStatus().equals(BetStatus.LOSE)) {
			Combi combi = combiRepository.findById(hrb.getCombi().getId()).get();
			combi.setCurrent(false);
			combiRepository.save(combi);
		}
		
		System.out.println(hrb.getCombi().getBets().size());

		redirect.addFlashAttribute("show", hrb.getCombi().getId());
		
		return "redirect:/bankroll-details?id=" + hrb.getCombi().getBankroll().getId();
		
	}

	@RequestMapping("/delete-bet")
	public String deleteBet(@RequestParam("id") Long id, Model model, Principal principal) {
		
		Bet bet = betRepository.findById(id).get();	
		
		betRepository.deleteById(bet.getId());
		
		Combi combi = combiRepository.findById(bet.getCombi().getId()).get();
		for(Bet b : combi.getBets()) {
			if(b.getStatus().equals(BetStatus.LOSE)) {
				combi.setCurrent(false);
				combiRepository.save(combi);
			}
		}
		
		return "redirect:/bankroll-details?id=" + bet.getCombi().getBankroll().getId();
	}
	
}
