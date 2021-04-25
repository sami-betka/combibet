package combibet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import combibet.repository.CombiRepository;
import combibet.repository.HorseRacingBetRepository;


@Controller
public class CombiController {
	
	@Autowired
	HorseRacingBetRepository horseRacingBetRepository;
	
	@Autowired
	CombiRepository combiRepository;

	
	
}
