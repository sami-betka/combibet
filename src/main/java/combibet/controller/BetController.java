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

import combibet.entity.Bankroll;
import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.ConfidenceIndex;
import combibet.entity.Discipline;
import combibet.entity.HorseRacingBet;
import combibet.entity.SportBet;
import combibet.repository.BankrollRepository;
import combibet.repository.BetRepository;
import combibet.repository.CombiRepository;

@Controller
public class BetController {

	@Autowired
	BetRepository betRepository;

	@Autowired
	CombiRepository combiRepository;
	
	@Autowired
	BankrollRepository bankrollRepository;
	
//	@Autowired
//    TwilioService service;

	@RequestMapping(value = "/edit-bet")
	public String editHorseRacingBet(@RequestParam("id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		Bet bet = betRepository.findById(id).get();

//		model.addAttribute("bet", bet);
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("confidenceIndexs", ConfidenceIndex.values());

		if (bet.getClass().equals(SportBet.class)) {
			bet = (SportBet) bet;
			model.addAttribute("bet", bet);
			return "update-sport-bet";
		}
		if (bet.getClass().equals(HorseRacingBet.class))
			bet = (HorseRacingBet) bet;
		    model.addAttribute("bet", bet);
			model.addAttribute("types", BetType.values());
			model.addAttribute("disciplines", Discipline.values());
			
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

		HorseRacingBet hrb = (HorseRacingBet) betRepository.findById(bet.getId()).get();
		
		hrb.setDate(bet.getDate());
		hrb.setType(bet.getType());
		hrb.setSelection(bet.getSelection());
		if(bet.getWinOdd()>0) {
			hrb.setHasWon(true);
		}
		hrb.setWinOdd(bet.getWinOdd());
		hrb.setOdd(bet.getOdd());
		hrb.setStatus(bet.getStatus());
//		if(bet.getStatus().equals(BetStatus.LOSE)) {
//			hrb.setOdd(0d);
//		}
		hrb.setAnte(bet.getAnte());
//		hrb.setAfterComment(bet.getAfterComment());
//		hrb.setBeforeComment(bet.getBeforeComment());
//		hrb.setField(bet.getField());
		hrb.setDiscipline(bet.getDiscipline());
		hrb.setConfidenceIndex(bet.getConfidenceIndex());

		
		Bet savedHrb = betRepository.save(hrb);
//		savedHrb.getCombi().setStartDate(savedHrb.getCombi().betsAsc().get(0).getDate());
//		savedHrb.getCombi().getBankroll().setStartDate(savedHrb.getCombi().betsAsc().get(0).getDate());

//		if (!hrb.getStatus().equals(BetStatus.LOSE)) {
//			Combi combi = hrb.getCombi();
//			combi.setCurrent(true);
//			combiRepository.save(combi);
//		}
//		if (hrb.getStatus().equals(BetStatus.LOSE)) {
//			Combi combi = hrb.getCombi();
//			combi.setCurrent(false);
//			combiRepository.save(combi);
//		}

//		System.out.println(hrb.getCombi().getBets().size());

//		redirect.addFlashAttribute("show", hrb.getCombi().getId());
		
//		SmsRequest smsRequest = new SmsRequest("+33652463080", "Youhou !");
//		service.sendSms(smsRequest);

		return "redirect:/new-bankroll-details-simulation?id=" + hrb.getBankroll().getId();

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
		sb.setConfidenceIndex(bet.getConfidenceIndex());

//		sb.setAfterComment(bet.getAfterComment());
//		sb.setBeforeComment(bet.getBeforeComment());
//		sb.setField(bet.getField());

		betRepository.save(sb);

//		if (!sb.getStatus().equals(BetStatus.LOSE)) {
//			Combi combi = sb.getCombi();
//			combi.setCurrent(true);
//			combiRepository.save(combi);
//		}
//		if (sb.getStatus().equals(BetStatus.LOSE)) {
//			Combi combi = sb.getCombi();
//			combi.setCurrent(false);
//			combiRepository.save(combi);
//		}

//		System.out.println(sb.getCombi().getBets().size());

//		redirect.addFlashAttribute("show", sb.getCombi().getId());

		return "redirect:/new-bankroll-details?id=" + sb.getBankroll().getId();

	}

	@RequestMapping("/delete-bet")
	public String deleteBet(@RequestParam("id") Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {

		Bet bet = betRepository.findById(id).get();

		betRepository.deleteById(bet.getId());

//		Combi combi = combiRepository.findById(bet.getCombi().getId()).get();
//		combi.setDate(combi.betsAsc().get(0).getDate());
		
		Bankroll bankroll = bankrollRepository.findById(bet.getBankroll().getId()).get();
		if(bankroll.getBets().size() > 0) {

		bankroll.setStartDate(bankroll.getBets().get(0).getDate());	
		bankrollRepository.save(bankroll);
		}
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

		return "redirect:/new-bankroll-details-simulation?id=" + bet.getBankroll().getId();
	}

}
