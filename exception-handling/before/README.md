# What?

This example project will demonstrate some bad exception handling in your REST resources.

# Requirements

* Maven 3.6.1 (or higher)
* Java 11 (or higher)

# TL;DR

Just run all tests: `mvn clean test`

Note: for the integration tests we use SpringCloudContract to generate the tests before they 
are run by maven as a regular unit test.

# So... what is wrong with it?

## Missing REST resource error handling

...

## Missing error-handling filter 

What if you have a CORS filter that throws an exception?

## Missing error-handling resource

...

## Duplicated catch blocks

...

## Using high level HTTP code at wrong abstraction level

...

## Using low level persistence code at wrong abstraction level

...

## Catching very abstract generic exceptions

...

## Transforming generic exception into specific without the required knowledge

...

## Catching and rethrowing the same exception

...

## Logging errors for handled exceptions that are not your responsibility  

...

# And...

## Handle access denied exceptions

...

## Handle method level security

...


# Alternative solutions

## Configure an ExceptionHandlerResolver

- Use DefaultHandlerExceptionResolver but the drawback is that the response body is 
always null while it also only handles a limited set of Spring exceptions.

- Use ResponseStatusExceptionResolver by annotating the exception classes but the drawback 
is that the response body is always null!

- Implement a custom HandlerExceptionResolver but the code is verbose and feels like
boilerplate so a better solution is to use the @ControllerAdvice functionality described
above.

## Throw ResponseStatusException (Spring 5+)

Drawbacks of this approach are:
- duplicated exception handling blocks
- introducing boilerplate exception handling again in resources
- risk of handling the same exception in different ways
- not mutually exclusive with the controller-advice


# Links

https://www.baeldung.com/exception-handling-for-rest-with-spring
