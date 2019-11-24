package de.rrob.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.rrob.spring.model.Player;

@Repository

public interface PlayerRepo extends CrudRepository<Player, Long> {

    List<Player> findByTeamId(long teamId);
    Optional<Player> findByTeamIdAndName(long teamId, String name);

}