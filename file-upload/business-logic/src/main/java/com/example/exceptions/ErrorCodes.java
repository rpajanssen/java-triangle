package com.example.exceptions;

import javax.ws.rs.core.Response;

public enum ErrorCodes {
    UNABLE_TO_PROCESS_PART("9991", Response.Status.INTERNAL_SERVER_ERROR),

    FORM_INVALID("0020", Response.Status.BAD_REQUEST),
    UNSUPPORTED_ENCODING("0021", Response.Status.BAD_REQUEST),

    URL_OR_ARGUMENTS_INVALID("0030", Response.Status.BAD_REQUEST),
    TOO_MANY_REQUESTS("0101", Response.Status.TOO_MANY_REQUESTS),

    UNEXPECTED_EXCEPTION("9999", Response.Status.INTERNAL_SERVER_ERROR)
    ;

    private final String code;
    private final Response.Status httpStatus;

    ErrorCodes(String code, Response.Status httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public Response.Status getHttpStatus() {
        return httpStatus;
    }

    public static ErrorCodes fromHttpStatusCode(Integer statusCode) {
        if(statusCode == null) {
            return ErrorCodes.UNEXPECTED_EXCEPTION;
        }

        for(ErrorCodes code : values()) {
            if(code.getHttpStatus().getStatusCode() ==  statusCode) {
                return code;
            }
        }

        return ErrorCodes.UNEXPECTED_EXCEPTION;
    }
}
