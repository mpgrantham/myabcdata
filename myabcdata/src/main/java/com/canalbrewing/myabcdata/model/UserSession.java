package com.canalbrewing.myabcdata.model;

import java.io.Serializable;
import java.util.Date;

public class UserSession implements Serializable
{
	private static final long serialVersionUID = 280591020970526872L;
	
	public static final String USER_ID = "user_id";
	public static final String SESSION_DATETIME = "session_datetime";
	public static final String SESSION_ACTIVE_FL = "session_active_fl";
	
	private String sessionToken;
	private int userId;
	private Date sessionDate;
	private int sessionActiveFl;
	
	public String getSessionToken()
	{
		return sessionToken;
	}
	public void setSessionToken(String sessionToken)
	{
		this.sessionToken = sessionToken;
	}
	public int getUserId()
	{
		return userId;
	}
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	public Date getSessionDate()
	{
		return sessionDate;
	}
	public void setSessionDate(Date sessionDate)
	{
		this.sessionDate = sessionDate;
	}
	public int getSessionActiveFl()
	{
		return sessionActiveFl;
	}
	public void setSessionActiveFl(int sessionActiveFl)
	{
		this.sessionActiveFl = sessionActiveFl;
	}
}
