package com.canalbrewing.myabcdata.model;

import java.util.Date;

public class UserSession {

    private String sessionToken;
	private int userId;
	private Date sessionDate;
	private int sessionActiveFl;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public int getSessionActiveFl() {
        return sessionActiveFl;
    }

    public void setSessionActiveFl(int sessionActiveFl) {
        this.sessionActiveFl = sessionActiveFl;
    }

    @Override
    public String toString() {
        return "UserSession [sessionActiveFl=" + sessionActiveFl + ", sessionDate=" + sessionDate + ", sessionToken="
                + sessionToken + ", userId=" + userId + "]";
    }
	
	
    
}