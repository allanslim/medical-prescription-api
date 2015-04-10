package com.codewarrior.csc686.project.endpoint;

import com.codewarrior.csc686.project.exception.BadRequestException;
import com.codewarrior.csc686.project.model.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.ParseException;

@ControllerAdvice
public class RestErrorHandler {


    private static final Logger LOG = LoggerFactory.getLogger(RestErrorHandler.class);

    @ExceptionHandler
    @ResponseBody
    public BaseResponse handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {

        String errorCode = "INTERNAL_SERVER_ERROR";
        String errorMessage = "Error happened in the backend.";

        int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

        if (exception instanceof BadRequestException || exception instanceof ParseException) {

            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            BadRequestException e = (BadRequestException) exception;
            errorCode = e.errorCode;
            errorMessage = e.errorMessage;

        }else if( exception instanceof SQLException) {
            LOG.error("backend exception", exception);
        }

        response.setStatus(statusCode);

        return new BaseResponse(errorCode, errorMessage);
    }

}