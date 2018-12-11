package com.canalbrewing.myabcdata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User implements Serializable
{
	private static final long serialVersionUID = 280591020970526872L;
	
	public static final String ID = "id";
	public static final String USER_NM = "user_nm";
	public static final String PASSWORD = "password";
	public static final String SALT = "salt";
	public static final String EMAIL = "email";
	public static final String START_PAGE = "start_page";
	
	private int id;
	private String userNm;
	private String password;
	private String email;
	private byte[] salt;
	private byte[] encryptedPassword;
	private String startPage;
	
	private String passwordResetKey;
	
	private String sessionKey;
	
	private List<Observed> observed = new ArrayList<Observed>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	@JsonIgnore
	public byte[] getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(byte[] encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public List<Observed> getObserved() {
		return observed;
	}

	public void setObserved(List<Observed> observed) {
		this.observed = observed;
	}
	
	public void addObserved(Observed observed)
	{
		this.observed.add(observed);
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	public String getPasswordResetKey() {
		return passwordResetKey;
	}

	public void setPasswordResetKey(String passwordResetKey) {
		this.passwordResetKey = passwordResetKey;
	}

}
