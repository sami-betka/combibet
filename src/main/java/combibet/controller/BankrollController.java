package combibet.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import combibet.entity.BankrollField;
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
import combibet.service.BankrollService;

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

	@Autowired
	BankrollService bankrollService;

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

		Bankroll bankroll = bankrollRepository.findById(id).get();

		model.addAttribute("id", id);

		model.addAttribute("combiListAsc", combiRepository.findAllByBankrollOrderByDateAsc(bankroll));

		return "bankroll-details";
//		return "bankroll-list";

	}

	@GetMapping("/new-bankroll-details")
	public String newBankrollDetails(@RequestParam(name = "id", defaultValue = "") Long id,
			@RequestParam(name = "type", defaultValue = "") BetType type,
			@RequestParam(name = "bankrollAmount", defaultValue = "200", required = false) Double bankrollAmount,
			@RequestParam(name = "divider", defaultValue = "20", required = false) Integer divider, Model model,
			Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

//		List<Object> finalList = new ArrayList<>();

		Bankroll bankroll = bankrollRepository.findById(id).get();

//		Gambler gambler = gamblerRepository.findByUserName(principal.getName());

		List<Bet> bets = new ArrayList<>();

		if (type != null) {
			bets = betRepository.findAllByBankrollAndTypeOrderByDateAsc(bankroll, type);
//			model.addAttribute("betList", bets);

		} else {
			bets = betRepository.findAllByBankrollOrderByDateAsc(bankroll);
//			model.addAttribute("betList", bets);
		}
		
		model.addAttribute("betList", bankrollService.suppressNotPlayed(bets));
		
		model.addAttribute("id", id);
		model.addAttribute("types", BetType.values());
		model.addAttribute("bankrollName", bankroll.getName());
		model.addAttribute("field", bankroll.getBankrollField().getName());
		model.addAttribute("betListInfos", bankrollService.betsInfos(bankrollService.suppressNotPlayed(bets), bankroll.getStartAmount()));

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
		
		return "bet-list";
	}

	@GetMapping("/new-bankroll-details-simulation")
	public String newBankrollDetailsSimulation(@RequestParam(name = "id", defaultValue = "") Long id,
			@RequestParam(name = "type", defaultValue = "") BetType type,
			@RequestParam(name = "bankrollAmount", defaultValue = "200", required = false) Double bankrollAmount,
			@RequestParam(name = "divider", defaultValue = "20", required = false) Integer divider, Model model,
			Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

//		List<Object> finalList = new ArrayList<>();

		Bankroll bankroll = bankrollRepository.findById(id).get();

//		Gambler gambler = gamblerRepository.findByUserName(principal.getName());

		List<Bet> bets = new ArrayList<>();

		if (type != null) {
			bets = betRepository.findAllByBankrollAndTypeOrderByDateAsc(bankroll, type);
			model.addAttribute("betList", bets);

		} else {
			bets = betRepository.findAllByBankrollOrderByDateAsc(bankroll);
			model.addAttribute("betList", bets);
		}

		model.addAttribute("id", id);
		model.addAttribute("types", BetType.values());
		model.addAttribute("bankrollName", bankroll.getName());
		model.addAttribute("field", bankroll.getBankrollField().getName());

//		model.addAttribute("betListInfos", bets);
//		model.addAttribute("betListInfos", bankrollService.betListInfos(bets, bankrollAmount));
		model.addAttribute("betListInfos", bankrollService
				.betListInfosSimulation(bankrollService.managedBankrollSimulation(bets, divider, bankrollAmount)));

//		return "bet-list-simulation";

		Map<String, Double> surveyMap = new LinkedHashMap<>();

		LinkedList<Double> bankrollAmounts = (LinkedList<Double>) bankrollService
				.managedBankrollSimulation(bets, divider, bankrollAmount).get("bankrollAmounts");
		LinkedList<String> betsDates = (LinkedList<String>) bankrollService
				.managedBankrollSimulation(bets, divider, bankrollAmount).get("betsDates");

		for (int i = 0; i < bankrollAmounts.size(); i++) {

			surveyMap.put(betsDates.get(i), bankrollAmounts.get(i));

		}

		model.addAttribute("surveyMap", surveyMap);
		return "bet-list";
	}

	@GetMapping("/win-bankroll-details")
	public String winBankrollDetails(@RequestParam(name = "id", defaultValue = "") Long id,
			@RequestParam(name = "type", defaultValue = "") BetType type,
			@RequestParam(name = "bankrollAmount", defaultValue = "200", required = false) Double bankrollAmount,
			@RequestParam(name = "divider", defaultValue = "20", required = false) Integer divider, Model model,
			Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

//		List<Object> finalList = new ArrayList<>();

		Bankroll bankroll = bankrollRepository.findById(id).get();

//		Gambler gambler = gamblerRepository.findByUserName(principal.getName());

		List<Bet> bets = new ArrayList<>();

		if (type != null) {
			bets = betRepository.findAllByBankrollAndTypeOrderByDateAsc(bankroll, type);
			for (Bet bet : bets) {
				HorseRacingBet hrb = (HorseRacingBet) bet;
				if (hrb.isHasWon() == false) {
					hrb.setOdd(0d);
					hrb.setStatus(BetStatus.LOSE);
				} else {
					hrb.setOdd(hrb.getWinOdd());
					hrb.setType(BetType.SIMPLE_GAGNANT);
				}
			}
			model.addAttribute("betList", bets);

		} else {
			bets = betRepository.findAllByBankrollOrderByDateAsc(bankroll);
			for (Bet bet : bets) {
				HorseRacingBet hrb = (HorseRacingBet) bet;
				if (hrb.isHasWon() == false) {
					hrb.setOdd(0d);
					hrb.setStatus(BetStatus.LOSE);
				} else {
					hrb.setOdd(hrb.getWinOdd());
					hrb.setType(BetType.SIMPLE_GAGNANT);
				}
			}
			model.addAttribute("betList", bets);
		}

		model.addAttribute("id", id);
		model.addAttribute("types", BetType.values());
		model.addAttribute("bankrollName", bankroll.getName());
		model.addAttribute("field", bankroll.getBankrollField().getName());

//		return "bet-list-simulation";

		Map<String, Double> surveyMap = new LinkedHashMap<>();
//
//		LinkedList<Double> bankrollAmounts = (LinkedList<Double>) bankrollService
//				.managedBankrollSimulation(bets, divider, bankrollAmount).get("bankrollAmounts");
//		LinkedList<String> betsDates = (LinkedList<String>) bankrollService
//				.managedBankrollSimulation(bets, divider, bankrollAmount).get("betsDates");
//
//		for (int i = 0; i < bankrollAmounts.size(); i++) {
//
//			surveyMap.put(betsDates.get(i), bankrollAmounts.get(i));
//
//		}
//
		model.addAttribute("surveyMap", surveyMap);
		model.addAttribute("betListInfos", bankrollService.betsInfos(bets, bankroll.getStartAmount()));

		return "bet-list";
	}

	@GetMapping("/win-bankroll-simulation")
	public String winBankrollDetailsSimulation(@RequestParam(name = "id", defaultValue = "") Long id,
			@RequestParam(name = "type", defaultValue = "") BetType type,
			@RequestParam(name = "bankrollAmount", defaultValue = "200", required = false) Double bankrollAmount,
			@RequestParam(name = "divider", defaultValue = "20", required = false) Integer divider, Model model,
			Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

//		List<Object> finalList = new ArrayList<>();

		Bankroll bankroll = bankrollRepository.findById(id).get();

//		Gambler gambler = gamblerRepository.findByUserName(principal.getName());

		List<Bet> bets = new ArrayList<>();

		if (type != null) {
			bets = betRepository.findAllByBankrollAndTypeOrderByDateAsc(bankroll, type);
			model.addAttribute("betList", bets);

		} else {
			bets = betRepository.findAllByBankrollOrderByDateAsc(bankroll);
			model.addAttribute("betList", bets);
		}

		model.addAttribute("id", id);
		model.addAttribute("types", BetType.values());
		model.addAttribute("bankrollName", bankroll.getName());
		model.addAttribute("field", bankroll.getBankrollField().getName());

//		model.addAttribute("betListInfos", bets);
//		model.addAttribute("betListInfos", bankrollService.betListInfos(bets, bankrollAmount));
		model.addAttribute("betListInfos", bankrollService
				.betListInfosSimulation(bankrollService.winManagedBankrollSimulation(bets, divider, bankrollAmount)));

//		return "bet-list-simulation";

		Map<String, Double> surveyMap = new LinkedHashMap<>();

		LinkedList<Double> bankrollAmounts = (LinkedList<Double>) bankrollService
				.managedBankrollSimulation(bets, divider, bankrollAmount).get("bankrollAmounts");
		LinkedList<String> betsDates = (LinkedList<String>) bankrollService
				.managedBankrollSimulation(bets, divider, bankrollAmount).get("betsDates");

		for (int i = 0; i < bankrollAmounts.size(); i++) {

			surveyMap.put(betsDates.get(i), bankrollAmounts.get(i));

		}

		model.addAttribute("surveyMap", surveyMap);
		return "bet-list";

	}

	@GetMapping("/add-bankroll")
	public String addBankroll(Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("emptyBankroll", new Bankroll());
		model.addAttribute("fields", BankrollField.values());

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
		bankroll.setActive(true);
//		bankroll.setStartDate(combiRepository.findAllByBankrollOrderByStartDateAsc(bankroll).get(0).getStartDate());
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
	public String addCombiToBankroll(@RequestParam(name = "id") Long id, Model model, Principal principal,
			RedirectAttributes redirectAttributes) {

		if (principal == null) {
			return "redirect:/login";
		}

		Bankroll bankroll = bankrollRepository.findById(id).get();
		Combi combi = new Combi();
		combi.setBets(new ArrayList<>());

		combi.setBankroll(bankroll);
		combi.setDate(LocalDateTime.now());
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
	public String saveHorseRacingBetToCombi(@RequestParam(name = "id") Long id, HorseRacingBet bet,
			BindingResult bindingresult, Principal principal, RedirectAttributes redirectAttributes)
			throws IllegalStateException, IOException {

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
		if (bet.getStatus().equals(BetStatus.LOSE)) {
			bet.setOdd(0d);
		}

		List<Bet> betList = combi.getBets();
		HorseRacingBet savedHrb = betRepository.save(bet);
		System.out.println(bet.getId());
		System.out.println(savedHrb.getId());
		betList.add(savedHrb);
//		combi.getBets().clear();
		combi.setBets(betList);

		if (bet.getStatus().equals(BetStatus.LOSE)) {
			combi.setCurrent(false);
		}
		combi.setDate(combi.betsAsc().get(0).getDate());
		Combi savedCombi = combiRepository.save(combi);
		Bankroll bankroll = bankrollRepository.findById(combi.getBankroll().getId()).get();
		bankroll.getCombis().add(savedCombi);

		if (bankroll.getCombis().size() == 1) {
			bankroll.setStartDate(bankroll.getCombis().get(0).getBets().get(0).getDate());
		}

		bankrollRepository.save(bankroll);

		//////////////////

		redirectAttributes.addFlashAttribute("show", id);

		return "redirect:/bankroll-details?id=" + bankroll.getId();
	}

	@GetMapping("/add-horse-racing-bet-to-bankroll")
	public String addHorseRacingBetToBankroll(@RequestParam(name = "id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("id", id);
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("emptyBet", new HorseRacingBet());

		return "add-horse-racing-bet";
	}

	@PostMapping(value = "/save-horse-racing-bet-to-bankroll")
	public String saveHorseRacingBetToBankroll(@RequestParam(name = "id") Long id, HorseRacingBet bet,
			BindingResult bindingresult, Principal principal, RedirectAttributes redirectAttributes)
			throws IllegalStateException, IOException {

		if (principal == null) {
			return "redirect:/login";
		}
		if (bindingresult.hasErrors()) {
//			System.out.println(bet.getDate().toString());
			System.out.println(bindingresult.getAllErrors().toString());
			return "redirect:/add-horse-racing-bet-to-bankroll?id=" + id;
		}

		bet.setId(null);
//		Combi combi = combiRepository.findById(id).get();
		Bankroll bankroll = bankrollRepository.findById(id).get();

		bet.setGambler(gamblerRepository.findByUserName(principal.getName()));
		bet.setBankroll(bankroll);
		bet.setField("hippique");
		if (bet.getWinOdd() > 0) {
			bet.setHasWon(true);
		}
		if (bet.getStatus().equals(BetStatus.LOSE)) {
			bet.setOdd(0d);
		}

		List<Bet> betList = bankroll.getBets();
		HorseRacingBet savedHrb = betRepository.save(bet);
		System.out.println(bet.getId());
		System.out.println(savedHrb.getId());
		betList.add(savedHrb);
//		combi.getBets().clear();
		bankroll.setBets(betList);

//		if(bet.getStatus().equals(BetStatus.LOSE)) {
//			combi.setCurrent(false);
//		}
//		combi.setStartDate(combi.betsAsc().get(0).getDate());
//		Combi savedCombi = combiRepository.save(combi);
//		Bankroll bankroll = bankrollRepository.findById(combi.getBankroll().getId()).get(); 
//		bankroll.getCombis().add(savedCombi);

//		 if(bankroll.getCombis().size() == 1) {
//				bankroll.setStartDate(bankroll.getCombis().get(0).getBets().get(0).getDate());
//			}

		bankrollRepository.save(bankroll);

		//////////////////

		redirectAttributes.addFlashAttribute("show", id);

		return "redirect:/new-bankroll-details?id=" + bankroll.getId();
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
		bet.setField("sport");
//		bet.setType(BetType.PARI_SPORTIF);

		List<Bet> betList = combi.getBets();
		SportBet savedSb = betRepository.save(bet);
		System.out.println(bet.getId());
		System.out.println(savedSb.getId());
		betList.add(savedSb);
//		combi.getBets().clear();
		combi.setBets(betList);

		if (bet.getStatus().equals(BetStatus.LOSE)) {
			combi.setCurrent(false);
		}

		Combi savedCombi = combiRepository.save(combi);
		Bankroll bankroll = bankrollRepository.findById(combi.getBankroll().getId()).get();
		bankroll.getCombis().add(savedCombi);

		bankrollRepository.save(bankroll);

		//////////////////

		redirectAttributes.addFlashAttribute("show", id);

		return "redirect:/bankroll-details?id=" + bankroll.getId();
	}

	@GetMapping("/add-sport-bet-to-bankroll")
	public String addSportBetToBankroll(@RequestParam(name = "id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("id", id);
//		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("emptyBet", new SportBet());

		return "add-sport-bet";
	}

	@PostMapping(value = "/save-sport-bet-to-bankroll")
	public String saveSportBetToBankroll(@RequestParam(name = "id") Long id, SportBet bet, BindingResult bindingresult,
			Principal principal, RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {

		if (principal == null) {
			return "redirect:/login";
		}
		if (bindingresult.hasErrors()) {
//			System.out.println(bet.getDate().toString());
			System.out.println(bindingresult.getAllErrors().toString());
			return "redirect:/add-sport-bet-to-bankroll?id=" + id;
		}

		bet.setId(null);
//		Combi combi = combiRepository.findById(id).get();
		Bankroll bankroll = bankrollRepository.findById(id).get();

		bet.setGambler(gamblerRepository.findByUserName(principal.getName()));
		bet.setBankroll(bankroll);
		bet.setField("sport");
		if (bet.getStatus().equals(BetStatus.LOSE)) {
			bet.setOdd(0d);
		}

		List<Bet> betList = bankroll.getBets();
		SportBet savedSb = betRepository.save(bet);
		System.out.println(bet.getId());
		System.out.println(savedSb.getId());
		betList.add(savedSb);
//		combi.getBets().clear();
		bankroll.setBets(betList);

//		if(bet.getStatus().equals(BetStatus.LOSE)) {
//			combi.setCurrent(false);
//		}
//		combi.setStartDate(combi.betsAsc().get(0).getDate());
//		Combi savedCombi = combiRepository.save(combi);
//		Bankroll bankroll = bankrollRepository.findById(combi.getBankroll().getId()).get(); 
//		bankroll.getCombis().add(savedCombi);

//		 if(bankroll.getCombis().size() == 1) {
//				bankroll.setStartDate(bankroll.getCombis().get(0).getBets().get(0).getDate());
//			}

		bankrollRepository.save(bankroll);

		//////////////////

		redirectAttributes.addFlashAttribute("show", id);

		return "redirect:/new-bankroll-details?id=" + bankroll.getId();
	}

//	@GetMapping("/add-bet-to-bankroll")
//	public String addBetToBankroll(Model model, Principal principal) {
//
//		if (principal == null) {
//			return "redirect:/login";
//		}
//
//		model.addAttribute("emptyBet", new Combi());
//
//		return "add-combi";
//	}

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
