package combibet.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.Gambler;
import combibet.entity.HorseRacingBet;
import combibet.entity.SportBet;
import combibet.repository.BetRepository;
import combibet.repository.GamblerRepository;
import combibet.service.BetService;

@Controller
public class BetController {
	
	@Autowired
	BetRepository betRepository;
	
	@Autowired
	GamblerRepository gamblerRepository;
	
	@Autowired
	BetService betService;
	
	@GetMapping("/bet-list")
	public String getBetList (Model model, Principal principal) {
				
		if (principal == null) {
			return "redirect:/login";
		}
		
		Gambler gambler = gamblerRepository.findByUserName(principal.getName());
		
//		List<Bet> betList = betRepository.findAll();
		
		model.addAttribute("betList", betRepository.findAllByGamblerOrderByDateDesc(gambler));
//		model.addAttribute("betList", betRepository.findAllBetsByOrderByIdAsc());

		model.addAttribute("active", true);

		return "bet-list";
	}
	
	@GetMapping("/add-bet")
	public String addBet(@RequestParam(name = "field", defaultValue = "") String field, Model model, Principal principal) {
		
//		Map<Long, Double> surveyMap = new LinkedHashMap<>();
//			surveyMap.put(1l, 1d);
//			surveyMap.put(3l, 3d);
//			surveyMap.put(5l, 5d);
//		model.addAttribute("surveyMap", surveyMap);
		
		if (principal == null) {
			return "redirect:/login";
		}
		
		if (field.equals("horseRacing")) {
			
			model.addAttribute("emptyBet", betService.addHorseRacingBet());
			model.addAttribute("types", BetType.values());
			model.addAttribute("status", BetStatus.values());
			
			return "add-horse-racing-bet";
		}
		
	if (field.equals("sport")) {
			
			model.addAttribute("emptyBet", betService.addSportBet());
			model.addAttribute("status", BetStatus.values());
			
			return "add-sport-bet";
		}
		
	System.out.println(field + "jjjjjj");
	return "redirect:/list";
	}

	@PostMapping(value = "/save-horse-racing-bet")
	public String saveHorseRacingBet(HorseRacingBet bet, BindingResult bindingresult, Principal principal)
			throws IllegalStateException, IOException {
		
		System.out.println(bindingresult.getAllErrors());

		if (bindingresult.hasErrors()) {
			return "redirect:/add-bet?field=horseRacing";
		}
        
		bet.setGambler(gamblerRepository.findByUserName(principal.getName()));
		betRepository.save(bet);
		return "redirect:/list";
	}	
	
	@PostMapping(value = "/save-sport-bet")
	public String saveSportBet(SportBet bet, BindingResult bindingresult, Principal principal)
			throws IllegalStateException, IOException {
		
		System.out.println(bindingresult.getAllErrors());

		if (bindingresult.hasErrors()) {
			return "redirect:/add-bet?field=sport";
		}
        
		bet.setGambler(gamblerRepository.findByUserName(principal.getName()));
		betRepository.save(bet);
		return "redirect:/bet-list";
	}	
	
	@GetMapping(value = "/edit-bet/{id}")
	public String editBet(@PathVariable("id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		Bet bet = betRepository.findById(id).get();
		
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("bet", bet);

		return "updateBet";

	}

	@PostMapping(value = "/update-bet")
	public String updateBet(Bet bet, BindingResult bindingresult, Model model, Principal principal, RedirectAttributes redirect) {
		if (bindingresult.hasErrors()) {
			redirect.addFlashAttribute("createsuccess", true);

			return "redirect:/edit-bet/" + bet.getId();
		}

		betRepository.save(bet);
		return "redirect:/bet-list";

	}
	
	@RequestMapping(value = "/delete-bet/{id}")
	public String deleteBet(@PathVariable("id") Long id, Principal principal) {
		
		if (principal == null) {
			return "redirect:/login";
		}
		
		betRepository.deleteById(id);
		
		return "redirect:/bet-list";
		
	}

}
