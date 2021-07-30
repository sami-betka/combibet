package combibet.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import combibet.entity.SportBet;
import combibet.repository.BankrollRepository;
import combibet.repository.BetRepository;
import combibet.repository.CombiRepository;
import combibet.repository.GamblerRepository;

@Controller
public class BankrollController {

	@Autowired
	CombiRepository combiRepository;
	
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

        Bankroll bankroll = bankrollRepository.findById(id).get();
		
//		for(Combi c : bankroll.getCombis()) {
//			System.out.println(c.isCurrent());
//			System.out.println(c.getBets().size());
//			
//			if(c.getBets().size() == 0) {
//				c.setCurrent(true);
//				combiRepository.save(c);
//			}
//			if(c.getBets().size() < 0) {
//				
//				for(HorseRacingBet b : c.getBets()) {
//					if(b.getStatus().equals(BetStatus.LOSE)) {
//						c.setCurrent(false);
//						combiRepository.save(c);
//					}
//				}
//		
//			}			
//			
//		}

		model.addAttribute("id", id);
//		model.addAttribute("combiListAsc", bankroll.getCombis());
		model.addAttribute("combiListAsc", combiRepository.findAllByBankrollOrderByStartDateAsc(bankroll));

		
//		model.addAttribute("active", true);
//		System.out.println(bankrollRepository.findById(id).get().getStartDate().getDayOfWeek());

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
//		bankroll.setStartDate(LocalDateTime.now());
		bankroll.setStartDate(combiRepository.findAllByBankrollOrderByStartDateAsc(bankroll).get(0).getStartDate());
		bankroll.setCombis(new ArrayList<>());
		bankrollRepository.save(bankroll);

		return "redirect:/bankroll-list";
	}

	@GetMapping("/delete-bankroll/{id}")
	public String deleteBankroll(@PathVariable("id") Long id) {

		bankrollRepository.deleteById(id);

		return "redirect:/bankroll-list";
	}

	@GetMapping("/add-combi-to-bankroll")
	public String addCombiToBankroll(@RequestParam(name = "id") Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {

		if (principal == null) {
			return "redirect:/login";
		}

		Bankroll bankroll = bankrollRepository.findById(id).get();
		Combi combi = new Combi();
		combi.setBets(new ArrayList<>());


		combi.setBankroll(bankroll);
		combi.setStartDate(LocalDateTime.now());
		combi.setCurrent(true);
		Combi savedCombi = combiRepository.save(combi);
		bankroll.getCombis().add(savedCombi);
		bankrollRepository.save(bankroll);

        System.out.println(savedCombi.getId());
		
		redirectAttributes.addFlashAttribute("show", savedCombi.getId());
		return "redirect:/bankroll-details?id=" + id;
//		return "redirect:/add-horse-racing-bet-to-combi?id=" + savedCombi.getId();
	}

	@GetMapping("/add-horse-racing-bet-to-combi")
	public String addHorseRacingBetToCombi(@RequestParam(name = "id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("id", id);
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("emptyBet", new HorseRacingBet());

		return "add-horse-racing-bet";
	}

	@PostMapping(value = "/save-horse-racing-bet-to-combi")
	public String saveHorseRacingBetToCombi(@RequestParam(name = "id") Long id, HorseRacingBet bet, BindingResult bindingresult,
			Principal principal, RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {

		if (principal == null) {
			return "redirect:/login";
		}
		if (bindingresult.hasErrors()) {
//			System.out.println(bet.getDate().toString());
			System.out.println(bindingresult.getAllErrors().toString());
			return "redirect:/add-horse-racing-bet-to-combi?id=" + id;
		}
		
		bet.setId(null);
		Combi combi = combiRepository.findById(id).get();


		bet.setGambler(gamblerRepository.findByUserName(principal.getName()));		
		bet.setCombi(combi);
		bet.setField("Hippique");
		
		List <Bet> betList = combi.getBets();
		HorseRacingBet savedHrb = betRepository.save(bet);
		System.out.println(bet.getId());
		System.out.println(savedHrb.getId());
		betList.add(savedHrb);
//		combi.getBets().clear();
		combi.setBets(betList);

		if(bet.getStatus().equals(BetStatus.LOSE)) {
			combi.setCurrent(false);
		}
		
		Combi savedCombi = combiRepository.save(combi);
		Bankroll bankroll = bankrollRepository.findById(combi.getBankroll().getId()).get(); 
		bankroll.getCombis().add(savedCombi);
		
		bankrollRepository.save(bankroll);
		
		//////////////////

		redirectAttributes.addFlashAttribute("show", id);
		
		return "redirect:/bankroll-details?id=" + bankroll.getId() ;
	}
	
	@GetMapping("/add-sport-bet-to-combi")
	public String addSportBetToCombi(@RequestParam(name = "id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("id", id);
//		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("emptyBet", new SportBet());

		return "add-sport-bet";
	}

	@PostMapping(value = "/save-sport-bet-to-combi")
	public String saveSportBetToCombi(@RequestParam(name = "id") Long id, SportBet bet, BindingResult bindingresult,
			Principal principal, RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {

		if (principal == null) {
			return "redirect:/login";
		}
		if (bindingresult.hasErrors()) {
//			System.out.println(bet.getDate().toString());
			System.out.println(bindingresult.getAllErrors().toString());
			return "redirect:/add-horse-racing-bet-to-combi?id=" + id;
		}
		
		bet.setId(null);
		Combi combi = combiRepository.findById(id).get();


		bet.setGambler(gamblerRepository.findByUserName(principal.getName()));		
		bet.setCombi(combi);
		bet.setField("Sport");
		bet.setType(BetType.PARI_SPORTIF);
		
		List <Bet> betList = combi.getBets();
		SportBet savedSb = betRepository.save(bet);
		System.out.println(bet.getId());
		System.out.println(savedSb.getId());
		betList.add(savedSb);
//		combi.getBets().clear();
		combi.setBets(betList);

		if(bet.getStatus().equals(BetStatus.LOSE)) {
			combi.setCurrent(false);
		}
		
		Combi savedCombi = combiRepository.save(combi);
		Bankroll bankroll = bankrollRepository.findById(combi.getBankroll().getId()).get(); 
		bankroll.getCombis().add(savedCombi);
		
		bankrollRepository.save(bankroll);
		
		//////////////////

		redirectAttributes.addFlashAttribute("show", id);
		
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

//	@PostMapping(value = "/save-bet-to-bankroll")
//	public String saveBetToBankroll(Bet bet, BindingResult bindingresult, Principal principal)
//			throws IllegalStateException, IOException {
//
//		System.out.println(bindingresult.getAllErrors());
//
//		if (bindingresult.hasErrors()) {
//			return "redirect:/add-bet-to-bankroll";
//		}
//
//		bet.setGambler(gamblerRepository.findByUserName(principal.getName()));
////		bet.setStartDate(LocalDate.now());
////		bankrollRepository.save(bankroll);
//
//		return "redirect:/bankroll-details";
//	}

}
