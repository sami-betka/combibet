package combibet.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import combibet.entity.Bankroll;
import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.Combi;
import combibet.entity.Gambler;
import combibet.entity.HorseRacingBet;
import combibet.repository.BankrollRepository;
import combibet.repository.BetRepository;
import combibet.repository.GamblerRepository;

@Controller
public class BankrollController {

	@Autowired
	BetRepository betRepository;

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

	@GetMapping("/bankroll-details")
	public String bankrollDetails(@RequestParam(name = "id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

//		Gambler gambler = gamblerRepository.findByUserName(principal.getName());

//		List<Bet> betList = betRepository.findAll();

		model.addAttribute("id", id);
		model.addAttribute("combiList", bankrollRepository.findById(id).get().getBets());

//		model.addAttribute("active", true);
		System.out.println(bankrollRepository.findById(id).get().getBets().size());

		return "bankroll-details";
//		return "bankroll-list";

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
		bankroll.setStartDate(LocalDateTime.now());
		bankroll.setBets(new ArrayList<>());
		bankrollRepository.save(bankroll);

		return "redirect:/bankroll-list";
	}

	@GetMapping("/delete-bankroll/{id}")
	public String deleteBankroll(@PathVariable("id") Long id) {

		bankrollRepository.deleteById(id);

		return "redirect:/bankroll-list";
	}

	@GetMapping("/add-combi-to-bankroll")
	public String addCombiToBankroll(@RequestParam(name = "id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		Bankroll bankroll = bankrollRepository.findById(id).get();
		Combi combi = new Combi();
		combi.setBets(new ArrayList<>());

//A suuprimer		
//		for (int i = 0; i < 5; i++) {
//			Bet bet = new HorseRacingBet();
//			bet.setDate(LocalDateTime.now());
//			bet.setSelection("selecta");
//			bet.setOdd(i + 1);
//			if (i == 2 || i == 4) {
//				bet.setStatus(BetStatus.WON);
//			} else {
//				bet.setStatus(BetStatus.PENDING);
//			}
//			bet.setType(BetType.COUPLE_GAGNANT);
//			betRepository.save(bet);
//			System.out.println(bet.getStatus().toString());
//
//			combi.getBets().add(bet);
//			combi.setStartDate(LocalDateTime.now());
//		}
		///////////////////////

		combi.setBankroll(bankroll);
		bankroll.getBets().add(combi);
		Combi savedCombi = betRepository.save(combi);
		bankrollRepository.save(bankroll);

        System.out.println(savedCombi.getId());
		
		return "redirect:/add-horse-racing-bet-to-combi?id=" + savedCombi.getId();
	}

	@GetMapping("/add-horse-racing-bet-to-combi")
	public String addBetToCombi(@RequestParam(name = "id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("id", id);
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("emptyBet", new Combi());

		return "add-horse-racing-bet";
	}

	@PostMapping(value = "/save-horse-racing-bet-to-combi")
	public String saveBetToCombi(@RequestParam(name = "id") Long id, HorseRacingBet bet, BindingResult bindingresult,
			Principal principal, RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {

		if (principal == null) {
			return "redirect:/login";
		}
		if (bindingresult.hasErrors()
//				&& field != "sport"
		) {
			return "redirect:/add-horse-racing-bet-to-combi?id=" + id;
		}
		
		Combi combi = (Combi) betRepository.findById(id).get();
		combi.setStartDate(LocalDateTime.now());
		if(combi.getBets() == null || combi.getBets().isEmpty()) {
			combi.setBets(new ArrayList<>());
		}

		bet.setGambler(gamblerRepository.findByUserName(principal.getName()));
//		bet.setCombi(combi);
		
		combi.getBets().add(bet);
		Bankroll bankroll = bankrollRepository.findById(combi.getBankroll().getId()).get(); 
		bankroll.getBets().add(combi);
		
		betRepository.save(bet);
		betRepository.save(combi);
		bankrollRepository.save(bankroll);
		
		//////////////////

		redirectAttributes.addFlashAttribute("show", true);
		
		return "redirect:/bankroll-details?id=" + bankroll.getId() ;
	}

	@GetMapping("/add-bet-to-bankroll")
	public String addBetToBankroll(Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("emptyBet", new Combi());

		return "add-combi";
	}

	@PostMapping(value = "/save-bet-to-bankroll")
	public String saveBetToBankroll(Bet bet, BindingResult bindingresult, Principal principal)
			throws IllegalStateException, IOException {

		System.out.println(bindingresult.getAllErrors());

		if (bindingresult.hasErrors()) {
			return "redirect:/add-bet-to-bankroll";
		}

		bet.setGambler(gamblerRepository.findByUserName(principal.getName()));
//		bet.setStartDate(LocalDate.now());
//		bankrollRepository.save(bankroll);

		return "redirect:/bankroll-details";
	}

}
