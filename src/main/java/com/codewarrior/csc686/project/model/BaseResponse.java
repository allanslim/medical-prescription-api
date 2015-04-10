package com.codewarrior.csc686.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties( ignoreUnknown = true )
public class BaseResponse implements Serializable
{
	private static final long serialVersionUID = -1782628850974741567L;

    private String errorCode;
    private String errorMessage;

	public BaseResponse() {}

	public BaseResponse( String errorCode, String errorMessage ) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() { return errorCode; }

    public String getErrorMessage() { return errorMessage; }
}
