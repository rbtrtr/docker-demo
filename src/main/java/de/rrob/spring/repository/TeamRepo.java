package de.rrob.spring.repository;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import de.rrob.spring.model.Team;


@Repository

public interface TeamRepo extends CrudRepository<Team, Long> {

    Team findByPlayers(long playerId);
    Team findByName(String name);
    
    
    boolean existsTeamByName(String name);

}