package de.rrob.spring.error;

public class InsertionException extends RuntimeException{

	public InsertionException(String msg) {
        super("failed to insert with message: " + msg);
    }
	
	
}
