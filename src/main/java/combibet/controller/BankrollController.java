package combibet.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import combibet.entity.Bankroll;
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
	public String getBankrollList(Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		Gambler gambler = gamblerRepository.findByUserName(principal.getName());

//		List<Bet> betList = betRepository.findAll();

		model.addAttribute("bankrollList", bankrollRepository.findAllByGamblerOrderByStartDateDesc(gambler));
//		model.addAttribute("betList", betRepository.findAllBetsByOrderByIdAsc());

		model.addAttribute("active", true);

		return "bankroll-list";
	}
	
	@GetMapping("/bankroll-details/{id}")
	public String bankrollDetails (@PathVariable("id") Long id, Model model, Principal principal) {
				
		if (principal == null) {
			return "redirect:/login";
		}
		
//		Gambler gambler = gamblerRepository.findByUserName(principal.getName());
		
//		List<Bet> betList = betRepository.findAll();
		
		model.addAttribute("bankroll", bankrollRepository.findById(id));
//		model.addAttribute("betList", betRepository.findAllBetsByOrderByIdAsc());

//		model.addAttribute("active", true);

		return "bankroll-details";
	}

	@GetMapping("/add-bankroll")
	public String addBankroll(Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("emptyBankroll", new Bankroll());

		return "add-bankroll";
	}

	@PostMapping(value = "/save-bankroll")
	public String saveBankroll(Bankroll bankroll, BindingResult bindingresult, Principal principal)
			throws IllegalStateException, IOException {

		System.out.println(bindingresult.getAllErrors());

		if (bindingresult.hasErrors()) {
			return "redirect:/add-bankroll";
		}

		bankroll.setGambler(gamblerRepository.findByUserName(principal.getName()));
		bankroll.setStartDate(LocalDate.now());
		bankrollRepository.save(bankroll);
				
		return "redirect:/bankroll-list";
	}

}
