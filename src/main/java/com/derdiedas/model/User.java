package com.derdiedas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class User {

	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
	
	@Column(unique=true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
    private String firstName;
    
	@Column(nullable = false)
    private String lastName;
}
