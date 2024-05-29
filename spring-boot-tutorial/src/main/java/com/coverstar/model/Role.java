package com.coverstar.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
public class Role implements Serializable{

	private static final long serialVersionUID = 8374992679263373931L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", unique=true, length=100)
	private String name;
	
	@Lob
	@Column(name="description")
	private String description;
	
	@ManyToMany(mappedBy="roles")
	private Set<Account> users = new HashSet<>();
}
