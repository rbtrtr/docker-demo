package de.rrob.spring.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.rrob.spring.error.InsertionException;
import de.rrob.spring.error.TeamNotFoundException;
import de.rrob.spring.model.Team;
import de.rrob.spring.repository.TeamRepo;
import de.rrob.spring.service.SoccerService;

@RestController
public class TeamController {

	@Autowired
	TeamRepo teamRepo;
	@Autowired 
	SoccerService soccerService;
	
	@GetMapping("/teams")
	Iterable<Team> findAll(){
		return teamRepo.findAll();
		
	}
	
	@GetMapping("/teams/{id}")
	Team findOneById(@PathVariable Long id){
		return teamRepo.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
		
	}
	
	@PostMapping("/teams")
	public ResponseEntity<GenericCreationResponse> addAddTeam(@RequestBody @Valid Team team) {
		
		if(teamRepo.existsTeamByName(team.getName()))
			throw new InsertionException("team with name "+team.getName()+" already exists. Please use PUT to update.");
		
		soccerService.addTeam(team);
		
		Long id = teamRepo.findByName(team.getName()).getId();
		return new ResponseEntity<GenericCreationResponse>(
				new GenericCreationResponse(id, "successfully created team")
				, HttpStatus.CREATED);
	}
	
	@PutMapping("/teams")
	public ResponseEntity<GenericCreationResponse> updateTeam(@RequestBody @Valid Team team) {
		
		if(!teamRepo.existsTeamByName(team.getName()))
			throw new InsertionException("team with name "+team.getName()+" does not exists. Please use POST to add.");
		

		soccerService.updateTeam(team);
		
		return new ResponseEntity<GenericCreationResponse>(
				new GenericCreationResponse(team.getId(), "successfully updated team")
				, HttpStatus.OK);
	}
	
}
