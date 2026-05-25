package com.example.demowithswiggy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity

public class Resuturant {

	    @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Long id;

	    private String name;
	    private String location;
		public Resuturant() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Resuturant(String name, String location) {
			super();
			this.name = name;
			this.location = location;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		
	    
}
