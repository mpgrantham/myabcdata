package com.canalbrewing.myabcdata.resultsetmapper.exceptions;

public class ResultSetMapperException extends RuntimeException {

    public static final int MAPPING_ERROR = 1000;

    private int exceptionCode = 0;

    public ResultSetMapperException() {
    }

    public ResultSetMapperException(int exceptionCode, String message) {
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
