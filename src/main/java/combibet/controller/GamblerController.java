package combibet.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.ConfidenceIndex;
import combibet.entity.Discipline;
import combibet.entity.Gambler;
import combibet.entity.Rule;
import combibet.entity.UserRole;
import combibet.repository.AppRoleRepository;
import combibet.repository.BankrollRepository;
import combibet.repository.BetRepository;
import combibet.repository.GamblerRepository;
import combibet.repository.HorseRacingBetRepository;
import combibet.repository.RuleRepository;
import combibet.repository.UserRoleRepository;
import combibet.service.BankrollService;
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
	HorseRacingBetRepository horseRacingBetRepository;
	
	@Autowired
	BankrollRepository bankrollRepository;
	
	@Autowired
	BankrollService bankrollService;
	
	@Autowired
	RuleRepository ruleRepository;
	
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
	
	@GetMapping("/rules-nabil")
	public String getNabilRules (Model model, Principal principal
//			,@PathVariable(name="gambler", required = true) String gambler
			) {
		
		if (principal == null) {
			return "redirect:/login";
		}
		
		List<Rule> rulesList = ruleRepository.findAllByGambler("nabil");
		Map<String, String> rulesMap = new LinkedHashMap<>();
		
		for (int i = 0; i < rulesList.size(); i++) {
			Rule rule = rulesList.get(i);
			rulesMap.put((i+1) + ") " + rule.getTitle(), rule.getDescription());
		} 
		
		model.addAttribute("active", true);
		model.addAttribute("rules", rulesMap);
		model.addAttribute("gambler", "Nabil");

		return "rules";
	}
	@GetMapping("/rules-sami")
	public String getSamiRules (Model model, Principal principal
//			,@PathVariable(name="gambler", required = true) String gambler
			) {
		
		if (principal == null) {
			return "redirect:/login";
		}
		
		List<Rule> rulesList = ruleRepository.findAllByGambler("sami");
		Map<String, String> rulesMap = new LinkedHashMap<>();
		
		for (int i = 0; i < rulesList.size(); i++) {
			Rule rule = rulesList.get(i);
			rulesMap.put((i+1) + ") " + rule.getTitle(), rule.getDescription());
		} 
		
		model.addAttribute("active", true);
		model.addAttribute("rules", rulesMap);
		model.addAttribute("gambler", "Sami");

		return "rules";
	}
	
	@GetMapping(value = "/add-rule")
	public String addRule(Model model, Principal principal) {

		model.addAttribute("emptyRule", new Rule());

		return "add-rule";
	}

	@PostMapping("/save-rule")
	public String saveRule(Rule rule, BindingResult result, Model model, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			redirect.addFlashAttribute("createsuccess", true);
			return "redirect:/add-rule";
		}
		ruleRepository.save(rule);

		return "redirect:/rules-" + rule.getGambler();
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
	
	@GetMapping("/bet-list-simulation")
	public String getSimulatedBetList (Model model, Principal principal) {
		
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
		
		return "bet-list-simulation";
	}
	
	
	@GetMapping("/multi-filter-bet-list")
	public String getFilteredBetList (
//			@PathVariable(name="id", required = true) Long id,

			@RequestParam(name="id", defaultValue = "", required = true) Long id,
			@RequestParam(name="type", defaultValue = "", required = false) BetType type,
			@RequestParam(name="confidenceIndex", defaultValue = "", required = false) ConfidenceIndex confidenceIndex,
			@RequestParam(name="discipline", defaultValue = "", required = false) Discipline discipline,
			@RequestParam(name="status", defaultValue = "", required = false) BetStatus status,
			@RequestParam(name = "minus", defaultValue = "0", required = false) Double minus,
			@RequestParam(name = "invest", defaultValue = "100", required = false) Double invest,
			@RequestParam(name = "maxOdd", defaultValue = "100", required = false) Double maxOdd,
			@RequestParam(name = "minOdd", defaultValue = "1", required = false) Double minOdd,
			@RequestParam(name="bankrollAmount", defaultValue = "1000", required = false) Double bankrollAmount,
			@RequestParam(name="divider", defaultValue = "10", required = false) Integer divider,
			@RequestParam(name="notPlayed", defaultValue = "false", required = false) String notPlayed,
			@RequestParam(name = "coteCombi", defaultValue = "", required = false) Double coteCombi,

			Model model, Principal principal) {
		

		
		if (principal == null) {
			return "redirect:/login";
		}
		
		Gambler gambler = gamblerRepository.findByUserName(principal.getName());
		
//		List<Bet> bets = betRepository.findAllByGamblerAndTypeOrderByDateAsc(gambler, type);
		List<Bet> bets = horseRacingBetRepository.newFilterSearch(id, maxOdd, minOdd);
    	final Double minus2 = minus;
    	bets.forEach(b->{
    		b.setOdd(b.getOdd() - minus2);
    	});

//		List<Bet> bets = horseRacingBetRepository.filterSearch(id, type, discipline, status, confidenceIndex);
		
		

		bankrollService.currentOddsInCombis(bets);

		System.out.println(bets.size());
//		if(notPlayed.equals("true")) {
			model.addAttribute("betList", bets);
			model.addAttribute("betListInfos", bankrollService
					.betListInfosSimulation(bankrollService.managedBankrollSimulation(bets, divider, bankrollAmount, invest, coteCombi), minus, maxOdd));

//		}
//		if(notPlayed.equals("false") || notPlayed.equals(null) ) {
//			model.addAttribute("betList", bankrollService.suppressNotPlayed(bets));
//			model.addAttribute("betListInfos", bankrollService
//					.betListInfosSimulation(bankrollService.managedBankrollSimulation(bankrollService.suppressNotPlayed(bets), divider, bankrollAmount, invest), minus, maxOdd));
//
//		}
		
		model.addAttribute("id", id);
		System.out.println(id);
		
		model.addAttribute("types", BetType.values());
		System.out.println(type);

		model.addAttribute("disciplines", Discipline.values());
		System.out.println(discipline);

		model.addAttribute("status", BetStatus.values());
		System.out.println(status);

		model.addAttribute("confidenceIndexs", ConfidenceIndex.values());
		System.out.println(confidenceIndex);
		
		System.out.println(notPlayed);
		
		model.addAttribute("field", bankrollRepository.findById(id).get().getBankrollField().getName());

		//////Map pour le graph/////////
		Map<String, Double> surveyMap = new LinkedHashMap<>();
//
//		@SuppressWarnings("unchecked")
//		LinkedList<Double> bankrollAmounts = (LinkedList<Double>) bankrollService
//				.managedBankrollSimulation(graphBets, divider, bankrollAmount).get("bankrollAmounts");
//		@SuppressWarnings("unchecked")
//		LinkedList<String> betsDates = (LinkedList<String>) bankrollService
//				.managedBankrollSimulation(graphBets, divider, bankrollAmount).get("betsDates");
//		
//
//		for (int i = 0; i < bankrollAmounts.size(); i++) {
//
//			surveyMap.put(betsDates.get(i), bankrollAmounts.get(i));
//
//		}
	
		model.addAttribute("surveyMap", surveyMap);
		
//		model.addAttribute("betListInfos", bankrollService.betsInfos(bankrollService.suppressNotPlayed(bets), bankrollRepository.findById(id).get().getStartAmount()));
//		model.addAttribute("betListInfos", bankrollService.betListInfosSimulation(bankrollService.managedBankrollSimulation(bets,divider, bankrollAmount)));

		return "bet-list";
	}
	
//	@GetMapping("/filtered-bet-list-simulation")
//	public String getSimulatedBetListByType (
//			@RequestParam(name="type", defaultValue = "") BetType type,
//			@RequestParam(name="bankrollAmount", defaultValue = "200", required = false) Double bankrollAmount,
//			@RequestParam(name="divider", defaultValue = "20", required = false) Integer divider,
//
//			
//			Model model, Principal principal) {
//		
//		System.out.println(type);
//		
//		
//		if (principal == null) {
//			return "redirect:/login";
//		}
//		
//		Gambler gambler = gamblerRepository.findByUserName(principal.getName());
//		
//		List<Bet> bets = betRepository.findAllByGamblerAndTypeOrderByDateAsc(gambler, type);
//
//		
//		model.addAttribute("betList", bets);
//		model.addAttribute("types", BetType.values());
//				
////		model.addAttribute("betListInfos", betListInfos(bets, 1000d));
//		model.addAttribute("betListInfos", bankrollService.betListInfosSimulation(bankrollService.managedBankrollSimulation(bets,divider, bankrollAmount)));
//
//		return "bet-list-simulation";
//	}
	
	// Ajouter un utilisateur

	@GetMapping(value = "/create-account")
	public String addUser(Model model, Principal principal) {

		model.addAttribute("emptyUser", new Gambler());

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
	
}
