package com.canalbrewing.myabcdata.exception;

public class MyAbcDataException extends Exception {

	private static final long serialVersionUID = 280591020970526872L;

	public static final int INVALID_SESSION = 1000;
	public static final int EXPIRED_SESSION = 1010;
	public static final int NOT_FOUND = 400;
	public static final int INVALID_LOGIN = 500;
	public static final int DUPLICATE = 600;
	public static final int INVALID_DELETE = 700;
	public static final int UNAUTHORIZED = 403;

	public static final int OTHER = 2000;

	private int exceptionCode = 0;

	public MyAbcDataException() {

	}

	public MyAbcDataException(int exceptionCode, String message) {
		super(message);

		this.exceptionCode = exceptionCode;
	}

	public int getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

}