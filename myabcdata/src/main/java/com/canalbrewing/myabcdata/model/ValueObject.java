package com.canalbrewing.myabcdata.model;

import java.io.Serializable;

public class ValueObject implements Serializable
{
	private static final long serialVersionUID = 280591020970526872L;
	
	protected int id;
	protected String value;
	
	public ValueObject()
	{
	}
	
	public ValueObject(int id, String value)
	{
		this.id = id;
		this.value = value;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getStrId() {
		return String.valueOf(id);
	}

}
