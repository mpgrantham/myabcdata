package com.canalbrewing.myabcdata.business;

import javax.mail.PasswordAuthentication;

public class MyAbcAuthenticator extends javax.mail.Authenticator 
{
	private PasswordAuthentication authentication;
	
	public MyAbcAuthenticator(String username, String password) 
	{
		authentication = new PasswordAuthentication(username, password);
	}
	
	@Override
	public PasswordAuthentication getPasswordAuthentication()
	{
		return authentication;
	}
}