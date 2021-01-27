package com.example.rest.exceptionhandlers;

import com.example.domain.api.ErrorResponse;
import com.example.exceptions.ErrorCodes;

import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.UnsupportedEncodingException;

/**
 * Example default exception handler that will return a proper error-response in the response body and a
 * HTTP 400 status code since we encountered an exception during input validation.
 *
 * See DefaultExceptionHandler class for a lot more explanation about the patterns and examples!
 */
@Named
@Provider
public class UnsupportedEncodingExceptionHandler implements ExceptionMapper<UnsupportedEncodingException> {

    @Override
    public Response toResponse(UnsupportedEncodingException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(Response.Status.BAD_REQUEST.name(), ErrorCodes.UNSUPPORTED_ENCODING.getCode()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
