package de.rrob.spring.error;

public class TeamNotFoundException extends RuntimeException{

	public TeamNotFoundException(Long id) {
        super("Team id not found : " + id);
    }
	
}
