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

An example is given in the PersonCrudResource in the method: _allPersons()_.

Here we observe a try-catch catching the high level rather abstract Exception. This
obfuscates the real behavior! We now do not know which exceptions can actually occur. 
They are all handled in the same way, which may not be correct. It is better to be 
explicit. 

## 8. Throwing very abstract generic exceptions

An example is given in the PersonCrudResource in the method: _allPersons()_.

It is bad practice to throw Exception or RuntimeException in your code. It really makes
it hard to understand what went wrong and how to handle the exception if you use an 
programming API that only returns these abstract exceptions. 

Note that Sonar (and other static code analyzers) will probably warn you about this as well.

## 9. Transforming generic exceptions into specific without the required knowledge

...

## 10. Catching and rethrowing the same exceptions

An example is given in the PersonCrudResource in the method: _allPersons()_.

It does not make sense to catch an exception and then rethrow the same exception. But 
this construct can be found in the code and it often occurs if a developer want to
log certain information. In these situations it is better to log information right before
the original exception is thrown, or to log the information where the resulting exception
will eventually be handled and skip the catching it as we did in the example code. 

Catching-and-rethrow is NOT handling it!

## 11. Unnecessary exception chaining

An example is given in the PersonCrudResource in the method: _allPersons()_.

This anti-pattern is related to the "catching and rethrowing the same exceptions"-pattern.
The exception is also not handled, it is just transformed into a different exception for
no obvious reasons. 

The drawback is that you get a deep stack trace and it becomes difficult to figure
out what was the root cause of the exception, either by reading the code or by trying to 
analyse the stack trace.

The solution is to throw a different original exception or catch the thrown exception in 
the location where you handle the exceptions and skip the catching it as we did in the 
example code.

Catching-transform-and-rethrow is NOT handling it!

## 12. Throwing generic exceptions

...

## 13. Logging errors for handled exceptions that are not your responsibility  

...

## 14. Unwanted swallowing of exceptions

An example is given in the PersonCrudResource in the method: _deletePerson(long personId)_.

Sometimes an exception may be thrown that you kind of want to ignore. You may want to
do some kind of handling, but afterwards you want to continue processing as normal. But 
there are situations where you should NOT swallow an exception like nothing happened.

In this example the exception is swallowed like nothing happened and the client using
this REST resource never will know something went wrong and the data that should have been
deleted might still be there! 

# And...

## 15. Handle access denied exceptions

...

## 16. Handle method level security

...

# And...

Always have automated (integration)tests validating the exception scenarios!


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
