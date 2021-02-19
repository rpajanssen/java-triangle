package com.example.rest.exceptionhandlers;

import com.example.domain.api.ErrorResponse;
import com.example.exceptions.ErrorCodes;
import com.example.exceptions.UnableToProcessFilePartException;

import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Named
@Provider
public class UnableToProcessFilePartExceptionHandler implements ExceptionMapper<UnableToProcessFilePartException> {

    @Override
    public Response toResponse(UnableToProcessFilePartException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(ErrorCodes.UNABLE_TO_PROCESS_PART.getCode(), Response.Status.INTERNAL_SERVER_ERROR.name()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
