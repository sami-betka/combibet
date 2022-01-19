package combibet.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

import combibet.entity.Bankroll;
import combibet.entity.BankrollField;
import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.entity.Combi;
import combibet.entity.ConfidenceIndex;
import combibet.entity.Discipline;
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

		model.addAttribute("bankrollList", bankrollService.getBankrollList(gambler));
		model.addAttribute("active", true);

		return "bankroll-list";
	}	

	@GetMapping("/new-bankroll-details-simulation")
	public String newBankrollDetailsSimulation(@RequestParam(name = "id", defaultValue = "") Long id,
			@RequestParam(name = "type", defaultValue = "") BetType type,
			@RequestParam(name = "bankrollAmount", defaultValue = "", required = false) Double bankrollAmount,
			@RequestParam(name = "minus", defaultValue = "0", required = false) Double minus,
			@RequestParam(name = "invest", defaultValue = "100", required = false) Double invest,
			@RequestParam(name = "maxOdd", defaultValue = "100", required = false) Double maxOdd,
			@RequestParam(name = "minOdd", defaultValue = "1", required = false) Double minOdd,
			@RequestParam(name = "divider", defaultValue = "10", required = false) Integer divider, Model model,
			Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

//		List<Object> finalList = new ArrayList<>();

		Bankroll bankroll = bankrollRepository.findById(id).get();
		
		
		
		if(bankrollAmount == null) {
			bankrollAmount = bankroll.getStartAmount();
		}
		if(invest == null) {
			invest = bankrollAmount;
		}
        if(bankroll.getName().equals("TOTAL-MIX-MAX")) {
        	bankroll = setMixMax(bankroll);
        }
        if(bankroll.getId().equals(65l) || bankroll.getId().equals(79l) || bankroll.getId().equals(73l) || bankroll.getId().equals(77l) || bankroll.getId().equals(78l)) {
        	minus = 0d;
        }
    	final Double minus2 = minus;
		
//		Gambler gambler = gamblerRepository.findByUserName(principal.getName());

		List<Bet> bets = new ArrayList<>();

		if (type != null) {
			bets = betRepository.findAllByBankrollAndTypeOrderByDateAsc(bankroll, type)
					.stream()
					.filter(b -> b.getOdd() < maxOdd
							&& b.getOdd() > minOdd
							)
					.collect(Collectors.toList());
			bets.forEach(bet->{
				Double odd = bet.getOdd();
				bet.setOdd(odd - minus2);
			});
			model.addAttribute("betList", bankrollService.suppressNotPlayed(bets));

		} else {
			bets = betRepository.findAllByBankrollOrderByDateAsc(bankroll)
					.stream()
					.filter(b -> b.getOdd() < maxOdd
							&& b.getOdd() > minOdd
							)
					.collect(Collectors.toList());
			bets.forEach(bet->{
				Double odd = bet.getOdd();
				bet.setOdd(odd - minus2);
			});
			model.addAttribute("betList", bankrollService.suppressNotPlayed(bets));
		}
		
		
//		if(bankroll.getId().equals(65l)) {
//			bets = bets.stream().filter(b->b.getOdd()<1.6).collect(Collectors.toList());
//		}

		model.addAttribute("id", id);
		model.addAttribute("types", BetType.values());
		model.addAttribute("bankrollName", bankroll.getName());
		model.addAttribute("field", bankroll.getBankrollField().getName());

//		model.addAttribute("betListInfos", bets);
//		model.addAttribute("betListInfos", bankrollService.betListInfos(bets, bankrollAmount));
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("disciplines", Discipline.values());
		model.addAttribute("confidenceIndexs", ConfidenceIndex.values());
		
		model.addAttribute("betListInfos", bankrollService
				.betListInfosSimulation(bankrollService.managedBankrollSimulation(bankrollService.suppressNotPlayed(bets), divider, bankrollAmount, invest), minus, maxOdd));

//		return "bet-list-simulation";

		Map<String, Double> surveyMap = new LinkedHashMap<>();

		LinkedList<Double> bankrollAmounts = (LinkedList<Double>) bankrollService
				.managedBankrollSimulation(bankrollService.suppressNotPlayed(bets), divider, bankrollAmount, invest).get("bankrollAmounts");
		LinkedList<String> betsDates = (LinkedList<String>) bankrollService
				.managedBankrollSimulation(bankrollService.suppressNotPlayed(bets), divider, bankrollAmount, invest).get("betsDates");

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


	@GetMapping("/add-horse-racing-bet-to-bankroll")
	public String addHorseRacingBetToBankroll(@RequestParam(name = "id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("id", id);
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("disciplines", Discipline.values());
		model.addAttribute("confidenceIndexs", ConfidenceIndex.values());
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
		if(bet.getConfidenceIndex() == null) {
			bet.setConfidenceIndex(ConfidenceIndex.NON_RENSEIGNE);
		}
//		if (bet.getStatus().equals(BetStatus.LOSE)) {
//			bet.setOdd(0d);
//		}

		List<Bet> betList = bankroll.getBets();
		HorseRacingBet savedHrb = betRepository.save(bet);
		System.out.println(bet.getId());
		System.out.println(savedHrb.getId());
		betList.add(savedHrb);
//		combi.getBets().clear();
		bankroll.setBets(betList);
		bankroll.setStartDate(betList
				.stream()
				.sorted(Comparator.comparing(Bet::getDate))
				.findFirst().get().getDate());


		bankrollRepository.save(bankroll);

		//////////////////

		redirectAttributes.addFlashAttribute("show", id);

		return "redirect:/new-bankroll-details-simulation?id=" + bankroll.getId();
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
		model.addAttribute("confidenceIndexs", ConfidenceIndex.values());
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

////////////////////
	public Bankroll setMixMax(Bankroll bankroll) {

		Bankroll bank = bankroll;
		List<Bet> toDelete = new ArrayList<>();
		for(Bet bet : bank.getBets()) {
			toDelete.add(bet);
		}
		betRepository.deleteAll(toDelete);
		
		List<Bet> betList = new ArrayList<>();;
		
		Bankroll bank1 = bankrollRepository.findByName("Le meilleur Pronostic");
		List<Bet> betList1 = bank1.getBets();
		Bankroll bank2 = bankrollRepository.findByName("ROCCA PRONOS");
		List<Bet> betList2 = bank2.getBets();
		Bankroll bank3 = bankrollRepository.findByName("NINJA");
		List<Bet> betList3 = bank3.getBets();
		Bankroll bank4 = bankrollRepository.findByName("PRAIMFAYA");
		List<Bet> betList4 = bank4.getBets();
		Bankroll bank5 = bankrollRepository.findByName("MADI PRONOS");
		List<Bet> betList5 = bank5.getBets();

		for (Bet b : betList1) {
			HorseRacingBet bet = (HorseRacingBet) b;

			HorseRacingBet newBet = new HorseRacingBet();
			newBet.setAnte(bet.getAnte());
			newBet.setBankroll(bank);
			newBet.setConfidenceIndex(bet.getConfidenceIndex());
			newBet.setDate(bet.getDate());
			newBet.setGambler(bet.getGambler());
			newBet.setOdd(bet.getOdd());
			newBet.setSelection(bet.getSelection());
			newBet.setStatus(bet.getStatus());
			newBet.setType(bet.getType());
			newBet.setField(bet.getField());
			newBet.setDiscipline(bet.getDiscipline());
			newBet.setFormattedDate(bet.getFormattedDate());
			newBet.setId(null);
			
			Bet savedBet = betRepository.save(newBet);
			
			betList.add(savedBet);
		}
		for (Bet b : betList2) {
			HorseRacingBet bet = (HorseRacingBet) b;

			HorseRacingBet newBet = new HorseRacingBet();
			newBet.setAnte(bet.getAnte());
			newBet.setBankroll(bank);
			newBet.setConfidenceIndex(bet.getConfidenceIndex());
			newBet.setDate(bet.getDate());
			newBet.setGambler(bet.getGambler());
			newBet.setOdd(bet.getOdd());
			newBet.setSelection(bet.getSelection());
			newBet.setStatus(bet.getStatus());
			newBet.setType(bet.getType());
			newBet.setField(bet.getField());
			newBet.setDiscipline(bet.getDiscipline());
			newBet.setFormattedDate(bet.getFormattedDate());
			newBet.setId(null);
			
			Bet savedBet = betRepository.save(newBet);
			
			betList.add(savedBet);
		}
		for (Bet b : betList3) {
			HorseRacingBet bet = (HorseRacingBet) b;

			HorseRacingBet newBet = new HorseRacingBet();
			newBet.setAnte(bet.getAnte());
			newBet.setBankroll(bank);
			newBet.setConfidenceIndex(bet.getConfidenceIndex());
			newBet.setDate(bet.getDate());
			newBet.setGambler(bet.getGambler());
			newBet.setOdd(bet.getOdd());
			newBet.setSelection(bet.getSelection());
			newBet.setStatus(bet.getStatus());
			newBet.setType(bet.getType());
			newBet.setField(bet.getField());
			newBet.setDiscipline(bet.getDiscipline());
			newBet.setFormattedDate(bet.getFormattedDate());
			newBet.setId(null);
			
			Bet savedBet = betRepository.save(newBet);
			
			betList.add(savedBet);
		}
		for (Bet b : betList4) {
			HorseRacingBet bet = (HorseRacingBet) b;

			HorseRacingBet newBet = new HorseRacingBet();
			newBet.setAnte(bet.getAnte());
			newBet.setBankroll(bank);
			newBet.setConfidenceIndex(bet.getConfidenceIndex());
			newBet.setDate(bet.getDate());
			newBet.setGambler(bet.getGambler());
			newBet.setOdd(bet.getOdd());
			newBet.setSelection(bet.getSelection());
			newBet.setStatus(bet.getStatus());
			newBet.setType(bet.getType());
			newBet.setField(bet.getField());
			newBet.setDiscipline(bet.getDiscipline());
			newBet.setFormattedDate(bet.getFormattedDate());
			newBet.setId(null);
			
			Bet savedBet = betRepository.save(newBet);
			
			betList.add(savedBet);
		}
		for (Bet b : betList5) {
			HorseRacingBet bet = (HorseRacingBet) b;

			HorseRacingBet newBet = new HorseRacingBet();
			newBet.setAnte(bet.getAnte());
			newBet.setBankroll(bank);
			newBet.setConfidenceIndex(bet.getConfidenceIndex());
			newBet.setDate(bet.getDate());
			newBet.setGambler(bet.getGambler());
			newBet.setOdd(bet.getOdd());
			newBet.setSelection(bet.getSelection());
			newBet.setStatus(bet.getStatus());
			newBet.setType(bet.getType());
			newBet.setField(bet.getField());
			newBet.setDiscipline(bet.getDiscipline());
			newBet.setFormattedDate(bet.getFormattedDate());
			newBet.setId(null);
			
			Bet savedBet = betRepository.save(newBet);
			
			betList.add(savedBet);
		}
	
		bank.setBets(betList);
				
		System.out.println("Stop");
		
		return bankrollRepository.save(bank);
	}
	
	
	@GetMapping("/transfer-bankroll")
	public String transferToAnOtherGambler(
			@RequestParam(name = "bankrollId") Long bankrollId, 
			@RequestParam(name = "gamblerId") Long gamblerId,
			Model model, Principal principal) {
				
		if (principal == null) {
			return "redirect:/login";
		}
		
		bankrollService.transferToAnOtherGambler(bankrollId, gamblerId);
		
		return "redirect:/bankroll-list";
	}
	

	@GetMapping("/delete-bankroll/{id}")
	public String deleteBankroll(@PathVariable("id") Long id) {

		bankrollService.deleteBankroll(id);
        
		return "redirect:/bankroll-list";
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
