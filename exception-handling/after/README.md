# What?

This example project will demonstrate how to resolve the bad exception handling examples 
given the the **before** project.

# Requirements

* Maven 3.6.1 (or higher)
* Java 11 (or higher)

# TL;DR

Just run all tests: `mvn clean test`

Note: for the integration tests we use Spring cloud-contract. The test will be generated based
on contracts after which they will be run. The generated tests can be found in `target\generated-test-sources`.
If you mark this folder as a test-resources folder in your IDE then you run the generated tests 
from you IDE like regular unit tests (useful for debugging/tracing).

Note: we switched to a RestEasy JAX-RS client in our generated tests instead of using Springs MockMVC.
This as example enables us to write/generate unit tests that can test the behavior of servlet filters,
something we could not do in the **before** example that uses MockMVC.

Just compare the base test classes in the **cloudcontracts** package to see the difference. Note
how the base test classes using RestEasy instead of MockMVC are less complex and simpler. Also note
that if we wanted to test the Spring security behavior we still would have to add additional
configuration to the MockMVC setup (like adding a Spring security MockMVC configurer). Also the pom 
has cleaned up nicely. No longer do we need to worry about mistakes in the Spring BOM. We no longer 
have to manually override and import RestAssured dependencies and we no longer need the Spring 
security test dependency. It is all cleaner and neater using RestEasy instead of Spring MockMVC.

To see what the **after** module looked like using MockMVC, checkout the tag _MOCKMVC_ from git.

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
What if you have a bug in your exception-handler class (@ControllerAdvice) resulting in a new
unexpected exception? Or another unhandled exception occurred? Then Spring will will redirect 
to an error resource!

To have full control over the response you can implement your own error resource as we did in
this example with the **ErrorHandlerResource**.

This resource will respond with an error response compliant with our REST domain API model.

I am not aware of a JAX-RS alternative (or if this is a thing in JAX-RS world).

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

In the _before_ example in the `DBPersonDAO` in the method _void update(Person person)_ we noticed
an exception was thrown and an HTTP error code was set. 

But... this DAO is not aware it runs in an application exposing a REST API. The DAO should not
have any knowledge about how code that calls the DAO will handle its exceptions.

The best practice is to always use one abstraction level in a class and in a method. Push 
code up or down depending on the level of abstraction. You will also reduce the change of
circular dependencies.

In this example we fixed the problem by not returning an HTTP error code. If this exception
results in an HTTP response with a certain code, it is up to the resource/exception-handler.

## 6. Assuming implementation details

In the _before_ example in the `PersonCrudResource` in the method _updatePerson(Person person)_
we wrote some code to handle exceptions. In that resource we caught a _PersistenceException_.
But... the resource can't possibly know this exception can be thrown. It is not aware which
low level persistence layer is being used. So this catch-block may even be a piece of dead 
code.

In this case we should remove the catch block for this exception. We did this anyway because 
we implemented an exception-handler class. But if we had not it would be best to remove the 
catch-block.

## 7. Catching abstract generic exceptions

In the _before_ example in the `PersonCrudResource` in the method _allPersons()_
we wrote some code to handle exceptions, but it only caught very abstract Exception
instances.

It is always best to be specific about which exceptions can occur and declare these.
And maybe even handle them differently. Of course you can always have a generic
error handler, especially for unsigned exceptions, but then it is certainly best to make
sure you have a cross-cutting-concern solution in place, like we already discussed with
the exception-handler. 

## 8. Throwing abstract generic exceptions

In the _before_ example in the `PersonCrudResource` in the method: _updatePerson()_ we have some
code that throws new very abstract Exception instances.

It is bad practice to throw Exception or RuntimeException in your code. It really makes
it hard to understand what went wrong and how to handle the exception if you use an 
programming API that only returns these generic exceptions. 

Another example is given in the `DBPersonDAO` in the method: _findById(long id)_.

So always throw meaningful exceptions. In the resource we can factor out the exception 
handling because we have introduced the use of the exception-handler class.

## 9. Catching and rethrowing the same exceptions

In the _before_ example in the `PersonCrudResource` in the method _updatePerson()_ we have 
some exception handling code that catches the exception and then rethrows it.

This often has no value and only adds boilerplate to your code. If you write code like
this with the intend to add (temporary) debug log/system.out statements... don't! Learn how 
to use your IDE and how to debug and trace with it. You will become more productive!

If you only have a catch block to do some error logging, most likely this logging is in 
the wrong location in your code! Log something where the exception actually handled. 

## 10. Unnecessary exception chaining

In the _before_ example in the `PersonCrudResource` in the method _allPersons()_ we have 
some exception handling code that chains exceptions.

When applied often in your code you get a deep stack trace and it becomes difficult to figure
out what the root cause of the exception is, either by reading the code or by trying to 
analyse the stack trace.

Often there is no value in chaining so you can ask yourselves the question why you are
catching the exception in the first place. In this example we rethrow the exact same
exception, so chaining does not have any value, and we should not have caught the exception.

## 11. Unwanted swallowing of exceptions

In the _before_ example in the `PersonCrudResource` in the method _deletePerson(long personId)_ 
we swallow an exception.

Sometimes an exception may be thrown that you kind of want to ignore. You may want to
do some kind of handling, but afterwards you want to continue processing as normal. But 
there are situations where you should NOT swallow an exception like nothing happened. This
example is one of them. The consumer of our REST API will now never know if something has 
gone wrong, and assume the data was deleted, while in fact nothing may have been deleted.

## 12. Logging errors for handled exceptions that are not your responsibility  

In the _before_ example in the `DBPersonDAO` in the method _add(Person person)_ we catch
exception and then log an error for it.

Another _before_ example is given in the `PersonCrudResource` in the method: _updatePerson(Person person)_.

In both these example we log errors but you can question why? Are they unexpected technical 
errors of the application (that should be logged)? No, these are valid functional execution
path where we just need to send a proper response to the consumer of our REST API so it can
act accordingly. Any logging our part will flood the log file and result in false error 
log entries. Whenever you have to many errors in your logs that are NOT really errors, it will
lead to you (and the rest of the team) to start ignoring errors in the log files since you
experience they mostly don't mean anything and are a waste of time researching. So... all 
logging within your application has lost its value!

So only log when something unexpected has happened that you need to deal with in your 
application!

Note: do not add log statements for debugging purposes. Write unit/integration tests instead
and start tracing from your IDE! This will speed up your development by a factor 1000!!!

# And...

## 13. Handle access denied exceptions

To make sure that the response of our REST API is compliant with our domain model we need to make
sure we handle the authorization exceptions that can occur.

Since we have a Spring application in this example, we can configure Spring Security to use
a specific error handler. In this example we have configured such an error handler in the `SecurityConfiguration`
configuration class. 

## 14. Handle method level security

It is possible to apply security policies to methods (as example by using the @Secured). 

Instead of letting the default Spring error handling kick in, it makes sense again to get some 
control over the responses.

Since these exceptions occur within the context of our REST resource we can use the exception-handler
class as discussed earlier to handle these.

In this example we added a handler for the `AccessDeniedException` exception in the 
 `PersonResourceExceptionHandler` class.`

# And...

Always have automated (integration)tests validating the exception scenarios! We added these
test scenarios to the contracts.

Note that we switched from using Springs MockMVC to a Rest-Easy client in our tests to be able
to test the full set of features our application has on offer, like testing the behavior of
servlet filters.

# Example resource calls

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

## Trigger annoying filter

curl -H "DumbHeader: whatever" http://localhost:8080/api/person
