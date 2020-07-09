package com.example.exceptionhandling.rest.resources;

import com.example.exceptionhandling.domain.api.ErrorResponse;
import com.example.exceptionhandling.exceptions.ErrorCodes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Error controller that transforms all rerouted errors into a response using the required rest-api domain model.
 * This error controller will typically handle errors that have risen before execution of the REST Resource was or after
 * execution flow has returned from the REST Resource. Exceptions will then NOT be handled by a (global) exception
 * handler.
 *
 * An is when you have a bug in your exception-handler class (@ControllerAdvice) resulting in a new
 * unexpected exception thrown from that class.
 */
@RestController
public class ErrorHandlerResource implements ErrorController {
    private static final String PROPERTY_STATUS_CODE = "javax.servlet.error.status_code";
    private static final String PROPERTY_EXCEPTION  = "javax.servlet.error.exception";

    @Value("${server.error.path}")
    private String path;

    @Override
    public String getErrorPath() {
        return path;
    }

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ErrorResponse> jsonError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(PROPERTY_STATUS_CODE);
        Exception exception = (Exception) request.getAttribute(PROPERTY_EXCEPTION);

        // todo : it would be wise to log the exception information here as well

        ErrorCodes errorCode = ErrorCodes.fromHttpStatusCode(statusCode);
        ErrorResponse<?> errorResponse = new ErrorResponse<>(errorCode.getCode());

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
}
