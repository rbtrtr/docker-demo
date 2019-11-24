package de.rrob.spring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.rrob.spring.error.TeamNotFoundException;
import de.rrob.spring.model.Player;
import de.rrob.spring.repository.PlayerRepo;

@RestController
public class PlayerController {

	@Autowired
	PlayerRepo playerRepo;
	
	@GetMapping("/players")
	Iterable<Player> findAll(){
		return playerRepo.findAll();
		
	}
	
	@GetMapping("/players/{id}")
	Player findOneById(@PathVariable Long id){
		return playerRepo.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
		
	}
}
