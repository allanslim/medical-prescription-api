package com.codewarrior.csc686.project.exception;

public class BadRequestException extends RuntimeException {

    public String errorCode;
    public String errorMessage;

    public BadRequestException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
