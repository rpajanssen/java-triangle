# What?

This example project will demonstrate some bad exception handling in your REST resources.

# Requirements

* Maven 3.6.1 (or higher)
* Java 11 (or higher)

# TL;DR

Just run all tests: `mvn clean test`

Note: for the integration tests we use SpringCloudContract to generate the tests before they 
are run by maven as a regular unit test. The generated tests can be found in `target\generated-test-sources`.

# So... what is wrong with it?

## 1. Missing REST resource error handling

If we observe the REST resource we can't see any exception handling! What will this resource 
return when an exception occurs? What will the response be there is a DB connection problem?
What will the response be if the data requested for update is not found? What will the response
be if a validation fails? 

Will the response be predictable? Will it be the one you expect it to be? Will the response be
without information leakage? 

Can you explain to the consumers of your resource what the error responses will be?

As it is, some default Spring components will handle the exceptions and prepare a response.
But you obviously would like a bit more control over the response. Maybe you even have a API 
domain model describing error responses and you always would like to have that returned!

## 2. Missing error-handling filter 

What happens when an exception occurs before your resource is called? Maybe you have implemented 
a filter and the filter throws an (unexpected) exception, what will the response be? 

As it is, some default Spring components will handle the exceptions and prepare a response. 
And again you may want to have more control over the response as described in example 1 above.

## 3. Missing error-handling resource

There are still other exception scenarios. What if a rate limiter blocked access to a resource?
Or another unhandled exception occurred? Then Spring will will redirect to an error resource!

But if you don't take over... you can't control the response! 

## 4. Duplicating catch blocks

Observing the PersonCrudResource you will notice all the error handling boilerplate that
pollutes the resource and how similar all that boilerplate code looks. There is a lot of code 
duplication going on. If you have multiple resources this code wil be duplicated across resources
as well. 

## 5. Using high abstraction level code at wrong lower abstraction level

An example is given in the DBPersonDAO in the method: _void update(Person person)_.

Here we see that an exception is being thrown and we also set the Http status code
for the response. This is wrong because:
- We now pull in higher level HTTP dependencies in a lower level persistence class.
- This DAO is not aware its result will eventually be used in a REST resource. 
- We run the risk of creating circular dependencies between the high/low level classes.
- We can't reuse the DAO anymore for anything else.

Always use one abstraction level in a class and in a method. Push code up or down depending
on the level of abstraction.

## 6. Using low level code at wrong higher abstraction level

An example is given in the PersonCrudResource in the method: _updatePerson(Person person)_.

Here we observe a PersistenceException is caught and handled. This is wrong because:
- We now pull in lower abstraction level dependencies into this higher abstraction level class.
- The resource is unaware this exception can be thrown! The resource has no way of telling
that a DAO implementation is used that uses the javax.persistence API to persist something 
to the database. This exception may NEVER be thrown!
- We run the risk of creating circular dependencies between the high/low level classes.

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

The solution is to throw a different original exception or catch the thrown exception 
only in the location where you handle the exceptions and skip the catching it as we did in 
the example code.

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

