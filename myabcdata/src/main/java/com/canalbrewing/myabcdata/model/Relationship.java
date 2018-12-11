package com.canalbrewing.myabcdata.model;

import java.io.Serializable;

public class Relationship implements Serializable
{
	private static final long serialVersionUID = 280591020970526872L;
	
	public static final String ID = "id";
	public static final String RELATIONSHIP = "relationship";
	
	private int id;
	private String relationship;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	
}
