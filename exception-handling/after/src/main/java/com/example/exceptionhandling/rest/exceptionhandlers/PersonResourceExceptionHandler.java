package com.example.exceptionhandling.rest.exceptionhandlers;

import com.example.exceptionhandling.dao.exceptions.PersonAlreadyExistsException;
import com.example.exceptionhandling.dao.exceptions.PersonNotFoundException;
import com.example.exceptionhandling.domain.api.ErrorResponse;
import com.example.exceptionhandling.domain.api.Person;
import com.example.exceptionhandling.exceptions.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This error-handler will only handle exceptions from the person-resource since it is bound to that resource using
 * the PersonResourceExceptionHandling-annotation.
 *
 * Note that this error handler does not return meaningful exceptions in the sense that if you do not have a list of
 * possible error codes explaining what they stand for, you do not know what went wrong! This is helpful in making
 * life a bit harder for hackers by not giving away information (information leakage).
 *
 * Note that with JAX-RS we can use ExceptionMappers as equivalent. You just need to implement one exception-mapper
 * for each exception you want to handle. You need to extend ExceptionMapper and you need to annotate the class
 * with @Provider.
 */
@ControllerAdvice (annotations = PersonResourceExceptionHandling.class )
public class PersonResourceExceptionHandler {
    @ResponseBody
    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse<Person> personNotFoundHandler(PersonNotFoundException exception) {
        return new ErrorResponse<>(exception.getPerson(), ErrorCodes.PERSON_NOT_FOUND.getCode());
    }

    @ResponseBody
    @ExceptionHandler(PersonAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse<Person> personAlreadyExistsHandler(PersonAlreadyExistsException exception) {
        return new ErrorResponse<>(exception.getPerson(), ErrorCodes.PERSON_ALREADY_EXISTS.getCode());
    }

    /**
     * This handler handles the bean validations defined for the bean arguments of the person rest-resource. The spring-
     * binder will bind the json to an instance of a person an perform all validations. If any validation fails a
     * MethodArgumentNotValidException will be throw.
     *
     * Note: Using JAX-RS a different exception will be thrown, the ValidationException. Be aware that these kind of
     * exceptions may depend on the framework of choice!
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse<Person> validationExceptionHandler(MethodArgumentNotValidException exception) {
        return new ErrorResponse<>((Person)exception.getBindingResult().getTarget(), ErrorCodes.PERSON_INVALID.getCode());
    }

    @ResponseBody
    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ErrorResponse<Person> handleAccessDeniedException(AccessDeniedException ex) {
        return new ErrorResponse<>(ErrorCodes.ACCESS_DENIED.getCode());
    }

    /**
     * Note: returning an internal-server-error can lead to unwanted side effects. API gateways/proxies can may swallow
     * the internal-server-error responses and replace them with their own response! Be aware of this. If it is possible
     * to return a different status code, that is always preferred!
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse<String> unexpectedExceptionHandler(Exception exception) {
        // todo : it would be wise to log the exception information here as well

        return new ErrorResponse<>(ErrorCodes.UNEXPECTED_EXCEPTION.getCode());
    }
}
