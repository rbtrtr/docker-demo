package de.rrob.spring;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import de.rrob.spring.model.Player;
import de.rrob.spring.repository.PlayerRepo;
import de.rrob.spring.service.SoccerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

	@MockBean
	PlayerRepo playerRepo;
	
	@Autowired 
	SoccerService soccerService;
	
	@Before
	public void setUp() {
		
	    Player alex = new Player();
	    alex.setId(1l);
	    alex.setPosition("midField");
	    alex.setName("alex");
	 
	    List<Player> players = new ArrayList<Player>();
	    players.add(alex);
	    
	    Mockito.when(playerRepo.findByTeamId(1l))
	      .thenReturn(players);
	}

	@Test
	public void testSoccerService() {
		List<String> players = soccerService.getAllTeamPlayers(1l);
		
		assertEquals(1l, players.size());
		assertEquals("alex", players.get(0));
	}

}

