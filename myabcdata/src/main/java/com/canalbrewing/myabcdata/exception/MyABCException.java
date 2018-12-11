package com.canalbrewing.myabcdata.exception;

import java.io.Serializable;

public class MyABCException extends Exception implements Serializable
{
	private static final long serialVersionUID = 280591020970526872L;
	public static final int INVALID_SESSION = 1000;
	public static final int EXPIRED_SESSION = 1010;
	public static final int INVALID_LOGIN = 500;
	
	private int exceptionCode = 0;
	
	public MyABCException()
	{
		
	}
	
	public MyABCException(int exceptionCode, String message)
	{
		super(message);
		
		this.exceptionCode = exceptionCode;
	}

	public int getExceptionCode()
	{
		return exceptionCode;
	}

	public void setExceptionCode(int exceptionCode)
	{
		this.exceptionCode = exceptionCode;
	}

}
