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
import combibet.entity.SportBet;
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

		Bet bet = betRepository.findById(id).get();

//		model.addAttribute("bet", bet);
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
//		navbarAttributes(model, principal);

		if (bet.getClass().equals(SportBet.class)) {
			bet = (SportBet) bet;
			model.addAttribute("bet", bet);
			return "update-sport-bet";
		}
		if (bet.getClass().equals(HorseRacingBet.class))
			bet = (HorseRacingBet) bet;
		    model.addAttribute("bet", bet);
		    return "update-horse-racing-bet";

	}

	@PostMapping(value = "/update-horse-racing-bet")
	public String updateHorseRacingBet(HorseRacingBet bet, Model model, BindingResult bindingresult,
			Principal principal, RedirectAttributes redirect) {
		if (bindingresult.hasErrors()) {
			redirect.addFlashAttribute("createsuccess", true);

			return "redirect:/edit-bet?id=" + bet.getId();
		}

		if (principal == null) {
			return "redirect:/login";
		}

		Bet hrb = betRepository.findById(bet.getId()).get();
		
		hrb.setDate(bet.getDate());
		hrb.setType(bet.getType());
		hrb.setSelection(bet.getSelection());
		hrb.setOdd(bet.getOdd());
		hrb.setStatus(bet.getStatus());
		hrb.setAnte(bet.getAnte());
//		hrb.setAfterComment(bet.getAfterComment());
//		hrb.setBeforeComment(bet.getBeforeComment());
//		hrb.setField(bet.getField());
		
		betRepository.save(hrb);

		if (!hrb.getStatus().equals(BetStatus.LOSE)) {
			Combi combi = hrb.getCombi();
			combi.setCurrent(true);
			combiRepository.save(combi);
		}
		if (hrb.getStatus().equals(BetStatus.LOSE)) {
			Combi combi = hrb.getCombi();
			combi.setCurrent(false);
			combiRepository.save(combi);
		}

		System.out.println(hrb.getCombi().getBets().size());

		redirect.addFlashAttribute("show", hrb.getCombi().getId());

		return "redirect:/bankroll-details?id=" + hrb.getCombi().getBankroll().getId();

	}

	@PostMapping(value = "/update-sport-bet")
	public String updateSportBet(SportBet bet, Model model, BindingResult bindingresult, Principal principal,
			RedirectAttributes redirect) {
		if (bindingresult.hasErrors()) {
			redirect.addFlashAttribute("createsuccess", true);

			return "redirect:/edit-bet?=" + bet.getId();
		}

		if (principal == null) {
			return "redirect:/login";
		}

		Bet sb = betRepository.findById(bet.getId()).get();
		
		sb.setDate(bet.getDate());
//		sb.setType(bet.getType());
		sb.setSelection(bet.getSelection());
		sb.setOdd(bet.getOdd());
		sb.setStatus(bet.getStatus());
		sb.setAnte(bet.getAnte());
//		sb.setAfterComment(bet.getAfterComment());
//		sb.setBeforeComment(bet.getBeforeComment());
//		sb.setField(bet.getField());

		betRepository.save(sb);

		if (!sb.getStatus().equals(BetStatus.LOSE)) {
			Combi combi = sb.getCombi();
			combi.setCurrent(true);
			combiRepository.save(combi);
		}
		if (sb.getStatus().equals(BetStatus.LOSE)) {
			Combi combi = sb.getCombi();
			combi.setCurrent(false);
			combiRepository.save(combi);
		}

		System.out.println(sb.getCombi().getBets().size());

		redirect.addFlashAttribute("show", sb.getCombi().getId());

		return "redirect:/bankroll-details?id=" + sb.getCombi().getBankroll().getId();

	}

	@RequestMapping("/delete-bet")
	public String deleteBet(@RequestParam("id") Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {

		Bet bet = betRepository.findById(id).get();

		betRepository.deleteById(bet.getId());

		Combi combi = combiRepository.findById(bet.getCombi().getId()).get();
//		for (Bet b : combi.getBets()) {
//			if (b.getStatus().equals(BetStatus.LOSE)) {
//				combi.setCurrent(false);
//				combiRepository.save(combi);
//			}
//		}
		if(bet.getStatus().equals(BetStatus.LOSE)) {
			combi.setCurrent(true);
			combiRepository.save(combi);
		}
		
		redirectAttributes.addFlashAttribute("show", bet.getCombi().getId());

		return "redirect:/bankroll-details?id=" + bet.getCombi().getBankroll().getId();
	}

}
