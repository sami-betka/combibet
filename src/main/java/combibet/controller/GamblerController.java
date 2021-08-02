package combibet.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.Gambler;
import combibet.entity.UserRole;
import combibet.repository.AppRoleRepository;
import combibet.repository.BetRepository;
import combibet.repository.GamblerRepository;
import combibet.repository.UserRoleRepository;
import combibet.utils.EncrytedPasswordUtils;

@Controller
public class GamblerController {
	
	@Autowired
	GamblerRepository gamblerRepository;
	
	@Autowired
	UserRoleRepository userRoleRepository;
	
	@Autowired
	AppRoleRepository appRoleRepository;
	
	@Autowired
	BetRepository betRepository;
	
	@Autowired
	static EncrytedPasswordUtils encrytedPasswordUtils;
	
	
	@GetMapping("/my-infos")
	public String getMyInfos (Model model, Principal principal) {
		
		if (principal == null) {
			return "redirect:/login";
		}
		
		model.addAttribute("active", true);

		return "user";
	}
	
	@GetMapping("/bet-list")
	public String getMyBets (Model model, Principal principal) {
		
		if (principal == null) {
			return "redirect:/login";
		}
		
		Gambler gambler = gamblerRepository.findByUserName(principal.getName());
		
		List<Bet> bets =  betRepository.findAllByGamblerOrderByDateAsc(gambler);
		
		model.addAttribute("betList", bets);
		model.addAttribute("types", BetType.values());
		
		Double sum = 0d;
		for(Bet b : bets) {
			sum = sum + b.getOdd();
		}
		Double benef = sum-bets.size();
		model.addAttribute("benef", "Nombre de paris = " + bets.size() + ", gains = " + sum + ", Benefice = " 
		        + benef + ", Paris gagnants = " + bets.stream().filter(b-> b.getStatus().equals(BetStatus.WON)).collect(Collectors.toList()).size() + ", Paris perdants = " 
				+ bets.stream().filter(b-> b.getStatus().equals(BetStatus.LOSE)).collect(Collectors.toList()).size());
		
		return "bet-list";
	}
	
	@GetMapping("/filtered-bet-list")
	public String getMyBetsByType (@RequestParam("type") BetType type, Model model, Principal principal) {
		
		if (principal == null) {
			return "redirect:/login";
		}
		
		Gambler gambler = gamblerRepository.findByUserName(principal.getName());
		
		List<Bet> bets = betRepository.findAllByGamblerAndTypeOrderByDateAsc(gambler, type);

		
		model.addAttribute("betList", bets);
		model.addAttribute("types", BetType.values());
		
		Double sum = 0d;
		for(Bet b : bets) {
			sum = sum + b.getOdd();
		}
		Double benef = sum-bets.size();
		model.addAttribute("betListInfos", "Nombre de paris = " + bets.size() + ", gains = " + sum + ", Benefice = " 
		        + benef + ", Paris gagnants = " + bets.stream().filter(b-> b.getStatus().equals(BetStatus.WON)).collect(Collectors.toList()).size() + ", Paris perdants = " 
				+ bets.stream().filter(b-> b.getStatus().equals(BetStatus.LOSE)).collect(Collectors.toList()).size());

		return "bet-list";
	}
	
	// Ajouter un utilisateur

	@GetMapping(value = "/create-account")
	public String addUser(Model model, Principal principal) {

		model.addAttribute("emptyUser", new Gambler());

//		navbarAttributes(model, principal);
		return "register";
	}

	@PostMapping("/save-user")
	public String saveUser(Gambler user, BindingResult result, Model model, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			redirect.addFlashAttribute("createsuccess", true);
			return "redirect:/create-account";
		}
		user.setPassword(encrytedPasswordUtils.encrytePassword(user.getPassword()));

		UserRole userRole = new UserRole(user, appRoleRepository.findById(2L).get());

		gamblerRepository.save(user);
		userRoleRepository.save(userRole);

		return "redirect:/login";
	}
	
///////////////////////////////////////////////////////PRIVATE///////////////////////////////////////////////////////////////////
	
	
	
	private Map<String, String> betListInfos (List<Bet> bets){
		
		Map<String, String> betListInfos = new HashMap<>();
		
		Double earnings = 0d;
		for(Bet b : bets) {
			earnings = earnings + b.getOdd();
		}
		Double benefit = earnings-bets.size();
		
		betListInfos.put("earnings", String.valueOf(earnings));
		betListInfos.put("benefit", String.valueOf(benefit));
		betListInfos.put("betsNumber", String.valueOf(bets.size()));
		List<Double> wonBetsOdds = bets
				.stream()
				.filter(b-> b.getStatus().equals(BetStatus.WON))
				.map(Bet::getOdd)
				.collect(Collectors.toList());
		
		betListInfos.put("wonBetsNumber", String.valueOf(wonBetsOdds.size()));
		betListInfos.put("lostBetsNumber", String.valueOf(bets
				.stream()
				.filter(b-> b.getStatus().equals(BetStatus.LOSE))
				.collect(Collectors.toList())
				.size()));

		betListInfos.put("averageOdd", String.valueOf(wonBetsOdds
				.stream()
				.collect(Collectors.summingDouble(Double::doubleValue))));




		
		return betListInfos;
	}


}
