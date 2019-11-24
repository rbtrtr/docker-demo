package de.rrob.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import de.rrob.spring.model.Player;
import de.rrob.spring.model.Team;
import de.rrob.spring.repository.PlayerRepo;
import de.rrob.spring.repository.TeamRepo;

@Service
public class SoccerService {

    @Autowired
    private PlayerRepo playerRepository;

    @Autowired
    private TeamRepo teamRepository;
    
    @Autowired
	JdbcTemplate jdbcTemplate;
    
    @Value( "${spring.jpa.properties.hibernate.default_schema}" )
    String databaseSchema;

    public List<String> getAllTeamPlayers(long teamId) {

        List<String> result = new ArrayList<String>();
        List<Player> players = playerRepository.findByTeamId(teamId);

        for (Player player : players) {
            result.add(player.getName());
        }

        return result;

    }

    public void addPlayer(Long teamId, String name, String position, int number) {
        
        Player player = new Player();
        player.setName(name);
        player.setNum(number);
        player.setPosition(position);

        Team team = teamRepository
        				.findById(teamId)
        				.orElseThrow(() -> new RuntimeException("No team found by id: " + teamId));
        player.setTeam(team);
        team.getPlayers().add(player);

        playerRepository.save(player);
        

    }
    
    @Transactional
    public void addTeam(Team team, List<Player> players) {
    	
    	// manually determine ids from database sequence - annotated approach wasn't successfull - hiberante always tries to start with the same initial value after restarting application
    	team.setId(fetchNextVal("team_seq"));
    	for(Player player : players) {
    		player.setId(fetchNextVal("player_seq"));
    	}
    	players.forEach(player -> player.setTeam(team));
    	teamRepository.save(team);
    	
    }
    
    private Long fetchNextVal(String sequence) {
    	return jdbcTemplate.queryForObject("select nextval ('"+databaseSchema+"."+sequence+"');", Long.class);
    }
    
 // used by rest controller POST
    @Transactional
    public void addTeam(Team team) {
    	
    	// manually determine ids from database sequence - annotated approach wasn't successfull - hiberante always tries to start with the same initial value after restarting application
    	team.setId(fetchNextVal("team_seq"));
    	for(Player player : team.getPlayers()) {
    		player.setId(fetchNextVal("player_seq"));
    		player.setTeam(team);
    	}
    	
    	teamRepository.save(team);
    	
    }
    
    // used by rest controller PUT
    @Transactional
    public void updateTeam(Team team) {
    
    	// set teamId
		Long teamId = teamRepository.findByName(team.getName()).getId();
		team.setId(teamId);
		
		// set playerId
		for(Player p : team.getPlayers()) {
			p.setTeam(team);
			Optional<Player> result = playerRepository.findByTeamIdAndName(teamId, p.getName());
			
			if(result.isPresent()) {
				result.ifPresent(player -> p.setId(player.getId()));
			} else {
				p.setId(fetchNextVal("player_seq"));
			}
			
		}

		teamRepository.save(team);
		
		// delete player that doesn't exist anymore
		List<Player> dbPlayers = playerRepository.findByTeamId(teamId);
		dbPlayers
			.stream()
			.filter(player -> !team.getPlayers().contains(player))
			.forEach(player -> playerRepository.deleteById(player.getId()));
			
	}
    	
}