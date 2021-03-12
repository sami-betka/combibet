package combibet.controller;

import java.io.IOException;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import combibet.entity.Bet;
import combibet.entity.BetStatus;
import combibet.entity.BetType;
import combibet.repository.BetRepository;

@Controller
@RequestMapping("/bets")
public class BetController {
	
	@Autowired
	BetRepository betRepository;
	
	@GetMapping("/list")
	public String getBetList (Model model) {
		
//		List<Bet> betList = betRepository.findAll();
		
//		model.addAttribute("user", userRepository.findById(1l).get());
		model.addAttribute("betList", betRepository.findAllBetsByOrderByIdAsc() );


		return "table";
	}
	
	@GetMapping("/add-bet")
	public String addBet(Model model) {


		Bet bet = new Bet();

		model.addAttribute("emptyBet", bet);
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());

		return "add-bet";
//		return "register";


	}

	@PostMapping(value = "/save-bet")
	public String saveBet(@Valid Bet bet, BindingResult bindingresult)
			throws IllegalStateException, IOException {
		
		System.out.println(bindingresult.getAllErrors());

		if (bindingresult.hasErrors()) {
			return "redirect:/bets/add-bet";
		}

		betRepository.save(bet);
		return "redirect:/tables.html";

	}	
	
	@GetMapping(value = "/edit-bet/{id}")
	public String editBet(@PathVariable("id") Long id, Model model, Principal principal) {

//		if (principal == null) {
//			return "redirect:/login";
//		}

		Bet bet = betRepository.findById(id).get();
		
		model.addAttribute("types", BetType.values());
		model.addAttribute("status", BetStatus.values());
		model.addAttribute("bet", bet);

		return "updateBet";

	}

	@PostMapping(value = "/update-bet")
	public String updateBet(@Valid Bet bet, BindingResult bindingresult, Model model, Principal principal, RedirectAttributes redirect) {
		if (bindingresult.hasErrors()) {
			redirect.addFlashAttribute("createsuccess", true);

			return "redirect:/bets/edit-bet/" + bet.getId();
		}

		betRepository.save(bet);
		return "redirect:/bets/bet-list";

	}

}
