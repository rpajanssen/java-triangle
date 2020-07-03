# What?

This example project will demonstrate some bad exception handling in your REST resources.

# Requirements

* Maven 3.6.1 (or higher)
* Java 11 (or higher)

# TL;DR

Just run all tests: `mvn clean test`

Note: for the integration tests we use SpringCloudContract to generate the tests before they 
are run by maven as a regular unit test. The generated tests can be found in `target\generated-test-sources`.

# What?

This example project will demonstrate how to fix the bad exception handling examples given 
in the **before** project.

# Requirements

* Maven 3.6.1 (or higher)
* Java 11 (or higher)

# TL;DR

Just run all tests: `mvn clean test`

Note: for the integration tests we use SpringCloudContract to generate the tests before they 
are run by maven as a regular unit test.

# So... how have we solved the issues?

## 1. REST resource error handling

A good solution to implement REST resource error handling is to implement an error handler that 
addresses this as a cross-cutting concern. This means that we only have to implement an error handler 
once and it will be re-used by multiple resources.

In Spring we implement a class and annotate it with `@ControllerAdvice` to register it as an REST 
resource error handler. In this class we can implement error handler methods by annotating
the methods with `@ExceptionHandler([<class>])`. We can pass a list of exception classes this handler
should handle.

We can have multiple of these classes annotated with `@ControllerAdvice`. And if we create a qualifier
we can bind the the exception handler class to a resource by both annotating them with the qualifier.
Then only that handler will be used for that resource.

In our example we have the `PersonResourceExceptionHandler` error handler class and we created the
qualifier `PersonResourceExceptionHandling` and annotated both the error handler class and resource 
with it.

Since we can cover each exception in the handler we have full control over the response that will
be send back to the consumer.

Another option is to implement the error handler methods in the resource itself. Drawback is that
the resource will become larger, it will have more code that distracts from the business functionality.
And you can not re-use the error handlers so if you have multiple resources you will end up
duplicating code.

The JAX-RS alternative is : **ExceptionMapper**. And you need to implement an exception-mapper
for each exception you want to handle.

### Alternative solutions

#### Configure an ExceptionHandlerResolver

- Use Spring's DefaultHandlerExceptionResolver but the drawback is that the response body is 
always null while it also only handles only a limited set of Spring exceptions.

- Use Spring's ResponseStatusExceptionResolver by annotating the exception classes but the drawback 
is that the response body is always null!

- Implement a custom HandlerExceptionResolver but the code is verbose and feels like
boilerplate so a better solution is to use the @ControllerAdvice functionality described
above.

#### Throw ResponseStatusException (Spring 5+)

Drawbacks of this approach are:
- duplicated exception handling blocks
- introducing boilerplate exception handling again in resources
- risk of handling the same exception in different ways
- not mutually exclusive with the controller-advice

The JAX-RS alternative is : **WebApplicationException** (and support for a limited set
of specific resource exceptions like **BadRequestException**).

## 2. Missing error-handling filter 

It might be possible you have (implemented and) configured request/response filters. It would be 
great if these filters never threw exceptions but only returned responses, but if they don't
you need to handle the exceptions. 

_Note: if they do return a response... make sure it is compliant with your REST resources API 
domain model!_

Because the code in the filters is executed before the code from the REST resource is called or 
after the REST resource has finished executing, any exception thrown will not be handled by
the exception handler from example 1. An example to catch these exception is to have a
request filter that is executed first in the chain of filters and catches all exceptions and
transforms them into proper responses matching your API domain model.

In this example we implemented the `ErrorHandlingFilter` class that does exactly that.

The JAX-RS alternative is : **ContainerRequestFilter** and **ContainerResponseFilter**.

## 3. Missing error-handling resource

There are still other exception scenarios. What if a rate limiter blocked access to a resource?
Or another unhandled exception occurred? Then Spring will will redirect to an error resource!

To have full control over the response you can implement your own error resource as we did in
this example with the **ErrorHandlerResource**.

This resource will respond with an error response compliant with our REST domain API model.

I am not aware of a JAX-RS alternative. It could be that you need to configure your application
server (or other, like an api gateway) to redirect to a resource for error handling.

## 4. Duplicating catch blocks

In the __before__ example we did not have any exception-handler implementation and try-catch 
statements were written in the resources and delegates that were called from the resource.

Most of these try-catch blocks duplicated the same boilerplate. The boilerplate makes it more
difficult to read and understand the code, it distracts from what is really important. And
we all understand why code duplication is something you need to avoid.

By implementing exception-handlers and extracting them into a single class (the one annotated
with `@Controlleradvice`) we have cleaned up the resource a lot. All the boilerplate is gone!

And even the code in the exception-handlers is cleaner because no try-catch block is needed
anymore!

This code is now cleaner and because of this better to read, better to understand and easier 
to write a unit test for.

Note: other possibilities to get rid of this kind of code duplication is to extract the 
duplicated code into a method or to implement a template (Template pattern) that captures
the try-catch blocks. But of course, implementing a cross-cutting-concern solution is in this
scenario the best option!

## 5. Using high abstraction level code at wrong lower abstraction level

In the _before_ example in the DBPersonDAO in the method _void update(Person person)_ we noticed
an exception was thrown and an HTTP error code was set. 

But... this DAO is not aware it runs in an application exposing a REST API. The DAO should not
have any knowledge about how code that calls the DAO will handle its exceptions.

The best practice is to always use one abstraction level in a class and in a method. Push 
code up or down depending on the level of abstraction. You will also reduce the change of
circular dependencies.

In this example we fixed the problem by not returning an HTTP error code. If this exception
results in an HTTP response with a certain code, it is up to the resource/exception-handler.

## 6. Using low level code at wrong higher abstraction level

In the _before_ example in the PersonCrudResource in the method _updatePerson(Person person)_
we wrote some code to handle exceptions. In that resource we caught a _PersistenceException_.
But... the resource can't possibly know this exception can be thrown. It is not aware which
low level persistence layer is being used. So this catch-block may even be a piece of dead 
code. We also pull in persistence layer dependencies into the resource and we run a higher 
risk of introducing circular dependencies.

In this case we should remove the catch block for this exception. We did this anyway because 
we implemented an exception-handler class. But if we had not it would be best to remove the 
catch-block.

## 7. Catching abstract generic exceptions

An example is given in the PersonCrudResource in the method: _allPersons()_.

Here we observe a try-catch catching the high level rather abstract Exception. This
obfuscates the real behavior! We now do not know which exceptions can actually occur. 
They are all handled in the same way, which may not be correct. It is better to be 
explicit. 

## 8. Throwing abstract generic exceptions

An example is given in the PersonCrudResource in the method: _allPersons()_.

It is bad practice to throw Exception or RuntimeException in your code. It really makes
it hard to understand what went wrong and how to handle the exception if you use an 
programming API that only returns these generic exceptions. 

Another example is given in the DBPersonDAO in the method: _findById(long id)_.

Throwing generic exceptions you loose the knowledge of what went wrong. If you observe
a method declaration in an interface that throws an Exception, it does not give you
any indication what can go wrong. If it throws exceptions like PersonAlreadyExistsException
and BlacklistedLastNameException then you know these are the only two expected exceptions
and you know what you need to handle. The code has become cleaner and quicker to 
comprehend. Also the exception handlers can now be cleaner. You don't need to use 
code constructs like instanceof or similar.

Note that Sonar (and other static code analyzers) will probably warn you about this as well.

## 9. Catching and rethrowing the same exceptions

An example is given in the PersonCrudResource in the method: _allPersons()_.

It does not make sense to catch an exception and then rethrow the same exception. But 
this construct can be found in the code and it often occurs if a developer wants to
log certain information. In these situations it is better to log information right before
the original exception is thrown, or to log the information where the resulting exception
will eventually be handled and skip the try-catching as we have it in this example code. 

Catching-and-rethrow is NOT handling it!

## 10. Unnecessary exception chaining

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

Catch-transform-and-rethrow is NOT handling it!

## 11. Unwanted swallowing of exceptions

An example is given in the PersonCrudResource in the method: _deletePerson(long personId)_.

Sometimes an exception may be thrown that you kind of want to ignore. You may want to
do some kind of handling, but afterwards you want to continue processing as normal. But 
there are situations where you should NOT swallow an exception like nothing happened.

In this example the exception is swallowed like nothing happened and the client using
this REST resource never will know something went wrong and the data that should have been
deleted might still be there! 

## 12. Logging errors for handled exceptions that are not your responsibility  

An example is given in the DBPersonDAO in the method: _add(Person person)_.

Often whenever an exception is thrown some information is logged to a logger, and often
it is done at error level. But you always have to be careful what you sent to the logger!
If you flood your logger, you might not be able to find the information you are looking for.
If you log a lot of warning or errors, that actually are not, you start to ignore the
logged information and won't even notice actual errors anymore!

The example given is a perfect example where an error is logged when an exception is thrown 
that should not have been logged. It is a functional exception, that should be handled in the
appropriate location in your code. But it is not an error of the DAO that a user/application 
is looking for information that does not exist!

Another example is given in the PersonCrudResource in the method: _updatePerson(Person person)_.

Two exceptions are caught and for both we log information. But... for one of them this is
obsolete. Can you guess which one?

Note: do not add log statements for debugging purposes. Write unit/integration tests instead
and start tracing from your IDE! This will speed up your development by a factor 1000!!!


# And...

## 13. Handle access denied exceptions

This example application has an admin resource (that doesn't really do anything :)) but you need
to be authenticated and have an ADMIN role to access it.

What happens when the authorization fails when you call that resource? Will it give you a nice
JSON response back... or... an HTML file? 

## 14. Handle method level security

It is possible to apply security policies to methods (as example by using the @Secured). 

Instead of letting the default Spring error handling kick in, it makes sense again to get some 
control over the responses.

# And...

Always have automated (integration)tests validating the exception scenarios!

# Resource calls

## Valid person calls

### Get all persons

curl http://localhost:8080/api/person

### add a person

curl --header "Content-Type: application/json" \
    --request POST \
    --data '{"firstName":"Jan","lastName":"Janssen"}' \
    http://localhost:8080/api/person
                       
### update a person

curl --header "Content-Type: application/json" \
    --request PUT \
    --data '{"id":"1001", "firstName":"Jan","lastName":"Jansen"}' \
    http://localhost:8080/api/person
                       
### delete a person

curl --request DELETE http://localhost:8080/api/person/1001     


## Invalid person calls

### Add a person with invalid first name

curl --header "Content-Type: application/json" \
    --request POST \
    --data '{"firstName":"","lastName":"Janssen"}' \
    http://localhost:8080/api/person
                       
### Add an existing person
 
curl --header "Content-Type: application/json" \
    --request POST \
    --data '{"id":"1001", "firstName":"Jan","lastName":"Janssen"}' \
    http://localhost:8080/api/person                       
                       

### update a non-existing person

curl --header "Content-Type: application/json" \
    --request PUT \
    --data '{"id":"666", "firstName":"Jan","lastName":"Jansen"}' \
    http://localhost:8080/api/person


## Valid admin calls

curl http://localhost:8080/api/admin/demo 

# Links

https://www.baeldung.com/exception-handling-for-rest-with-spring
https://thepracticaldeveloper.com/2019/09/09/custom-error-handling-rest-controllers-spring-boot/
https://www.toptal.com/java/spring-boot-rest-api-error-handling
