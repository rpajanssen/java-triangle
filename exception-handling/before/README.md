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

## 1. Missing REST resource error handling

...

## 2. Missing error-handling filter 

What if you have a CORS filter that throws an exception?

## 3. Missing error-handling resource

...

## 4. Duplicating catch blocks

...

## 5. Using high abstraction level code at wrong lower abstraction level

An example is given in the DBPersonDAO in the method: _void update(Person person)_.

Here we see that an exception is being thrown and we also set the Http status code
for the response. This is wrong because:
- This DAO is not aware its result will eventually be used in a REST resource. 
- We now pull in higher level HTTP dependencies in a lower level persistence class.
- We run the risk of creating circular dependencies between the high/low level classes.
- We can't reuse the DAO anymore for anything else.

Always use one abstraction level in a class and in a method.

## 6. Using low level code at wrong higher abstraction level

An example is given in the PersonCrudResource in the method: _updatePerson(Person person)_.

Here we observe a PersistenceException is caught and handled. This is wrong because:
- The resource is unaware this exception can be thrown! The resource has no way of telling
that a DAO implementation is used that uses the javax.persistence API to persist something 
to the database. This exception may NEVER be thrown!
- We now pull in lower abstraction level dependencies into this higher abstraction level class.
- We run the risk of creating circular dependencies between the high/low level classes.

## 7. Catching very abstract generic exceptions

...

## 8. Transforming generic exceptions into specific without the required knowledge

...

## 9. Catching and rethrowing the same exceptions

...

## 10. Unnecessary exception chaining

...

## 11. Throwing generic exceptions

...

## 12. Logging errors for handled exceptions that are not your responsibility  

...

# And...

## 13. Handle access denied exceptions

...

## 14. Handle method level security

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
