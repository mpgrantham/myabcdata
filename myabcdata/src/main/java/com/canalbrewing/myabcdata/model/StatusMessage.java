package com.canalbrewing.myabcdata.model;

import java.io.Serializable;

public class StatusMessage implements Serializable
{
	private static final long serialVersionUID = 280591020970526872L;
	
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	
	private String status;
	private String message;
	
	public StatusMessage(String status, String message)
	{
		this.status = status;
		this.message = message;
	}
	
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
}
