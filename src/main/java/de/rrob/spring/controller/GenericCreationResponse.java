package de.rrob.spring.controller;

public class GenericCreationResponse {

	private Long id;
	private String message;
	
	public GenericCreationResponse(Long id, String message) {
		super();
		this.id = id;
		this.message = message;
	}

	public Long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}
	
	
}
