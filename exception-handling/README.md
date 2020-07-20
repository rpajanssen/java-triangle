# What?

This example project will demonstrate good and bad examples of exception handling in your REST 
resources.

The **before** module has example code with common occurring mistakes. The **after** module
addresses these mistakes. 

The branch _no-inline-documentation_ has inline javadoc removed from the code so we don't have 
visible spoilers during a presentation and we can apply a mob-review approach.

The tag _MOCKMVC_ shows the **after** module code using MOCKMVC.

# Requirements

* maven 3.6.1 (or higher)
* java 11 (or higher)

# TL;DR

To run all tests run: `mvn clean test`

Note: for the integration tests we use Spring cloud-contract. The test will be generated based
on contracts after which they will be run. The generated tests can be found in `<module>\target\generated-test-sources`.
If you mark this folder as a test-resources folder in your IDE then you run the generated tests 
from you IDE like regular unit tests (useful for debugging/tracing).

For more on integration testing see: https://p-bitbucket.nl.eu.abnamro.com:7999/projects/COESD/repos/devcon2019

# ABN AMRO libraries

ABN AMRO standards: 
- http://coe.nl.eu.abnamro.com:9000/oca/oca-rest/standards-and-guidelines/rest@abnamro.html
- http://coe.nl.eu.abnamro.com:9000/oca/oca-rest/standards-and-guidelines/yaml-guidelines/standard-and-guidelines.html#135

The ABN AMRO has two libraries in support for rest API's, one for JAX-RS and one for Spring. Too
bad the readme text never mentions JAX-RS/Spring, the names are inconsistent and not self-explanatory:
- ja019_restutils : https://p-bitbucket.nl.eu.abnamro.com:7999/projects/GENJ/repos/ja019_restutils/browse
- ja129_aabresponsehandler : https://p-bitbucket.nl.eu.abnamro.com:7999/projects/GENJ/repos/ja129_aabresponsehandler/browse
- https://p-bitbucket.nl.eu.abnamro.com:7999/projects/GENJ/repos/ja024_sharedstructuralutils/browse

## ja019_restutils

This library supports JAX-RS based applications. The intend is that it will resolve any exception in your endpoint and return
a proper error response. 

But the implementation (design) is flawed. It implements an JAX-RS ExceptionMapper. This is indeed the preferred 
cross-cutting-concern solution. However the implementation is mixed with another concept using the `WebApplicationException` 
instances. This is a concept where JAX-RS transforms exceptions into error responses given the information present 
in `WebApplicationException` instances. This implies you need to re-introduce all the boilerplate exception handling 
in your resources and rethrow exceptions as a `WebApplicationException` instance. This will lead to a boilerplate 
in your resources, will lead to code duplication and may lead to different ways to handle the same exception! So this 
is not a preferred solution. Combining these two does not make any sense at all!

Another drawback is of course that if you don't use the `WebApplicationException` in combination with this library, which 
is preferred, then you probably throw your own custom exceptions in your code, like `PersonAlreadyExistedException`. But...
how will the generic implementation of the ExceptionMapper in this library know how to handle this exception. It does not know!
It will most likely return an internal-server-error status code where it should return a bad-request!

Even if you throw `AABException` instances the result will always be an internal-server-error!

And are really all exceptions being handled? What about an exception occurring in a filter or an reader/writer-interceptor?
What will your application return when an exception occurs in in there? Do you have control over the handling of these
exceptions using this library? Most likely not!

Another drawback of this library is that it has multiple responsibilities. Error handling is just one of them. Decorating
requests for observability purposes is another. If you would only want the error handling... that is nit possible.

_Our advice: write your own custom JAX-RS exception mapper and keep your resources free from boilerplate and... be compliant
with ABN AMRO standards._

### Some findings (not all!)

- **returns error responses not complying with the ABN AMRO standards**
- **AABExceptions will always lead to internal server errors**
- **custom exceptions will always lead to internal server errors**
- **you can only have full control over the response status code if you choose to write unclean code (using `WebApplicationException`)**
- **heavy use of LogInterceptor bindings icw additional log debug statements (a no go for a long time already)**
- **master pom 2 instead of 3**  
- **compiles with source/target java 7 but depends on java 8 EE api**
- implements its own rest parameter validation while JAX-RS and bean-validation already provides this for you
- code needs some cleanup
  - mixing/combining two different CDI concepts
  - following the flow of execution you jump all over the place from top to bottom and back
  - argument names like arg0, arg1, arg2.... a deadly sin
  - poor javadoc
  - better package/class/method names
  - magic strings/numbers
  - incorrect/messy class/package structure introducing incorrect/dangerous dependencies
 

## ja129_aabresponsehandler

This library supports Spring MVC based applications. The intend is that it will resolve any exception in your 
endpoint and return a proper error response. 

This implementation is functionally a bit better then the JAX-RS equivalent. When you are luck it will be compliant
with the ABN AMRO error response standard, but you depend on how the serializer serializes the used java Map 
implementation. This is because the domain API for the error response is only half implemented and for the missing
part a Map is used and... fingers crossed!

A generic exception handler (`ControllerAdvice`) has been implemented. And it covers a specific set of exceptions
to return a proper response. As long as you use the libraries that are supported by the generic handler it will create 
the proper error response. The same is true for using `AABExceptions`. However security exceptions are not covered like
the `AccessDeniedException`. And custom exceptions not extending AABExceptions are not supported as well! So these 
exceptions may result in a wrong error response if left up to this generic handler.

Another improvement over the JAX-RS equivalent library is that the handler does not support `ResponseStatusException` -
the equivalent of the `WebApplicationException` - and thus it is not mixing these two different concepts.

As a fall back this library also implements a custom error resource, which should cover all exceptions raised outside 
the context of your resource. Be careful because this resource however does not cover all possible security exceptions 
being raised so a wrong error response could be returned when these exceptions are raised.

A major drawback of this library is that it has multiple responsibilities. Error handling is just one of them. Decorating
requests for observability purposes is another. So if you want to use this library to handle exceptions but you want to 
use an open standard like OpenTelemetry for observability to send data to Azure AppInsights or Zipkin... well you are 
in trouble!

_Our advice: with a bit of luck, using this library, you will comply with the ABN AMRO error response standard. But of 
course you don't want to depend on luck so please pressure the team responsible for this component to fix the API error
response domain model. And be aware that some security exceptions and custom exceptions may lead to unwanted error 
response http status codes if you  leave it up to the provided exception handler! Note that you can still write your 
own exception handler for these!_

### Some findings (not all!)

- **domain of the REST error response only half implemented so you depend on luck to be compliant with ABN AMRO error
  response standards**
- **the ControllerAdvice is annotated as RestController (a bug)**
- **the ControllerAdvice doesn't handle AccessDeniedExceptions**
- **the GenericErrorHandler - which is an error resource - is annotated with Controller instead of RestController**
- **the GenericErrorHandler does not cover all security exceptions since it only checks for unauthorized status code and
  not for the forbidden status code** 
- **does not use a observability standards (tracing) like OpenTelemetry (how is this integrated with OpenTelemetry servers
  like Azure AppInsights, Jeager, Zipkin, ...)**    
- **library has to many responsibilities and might prevent you from complying with upcoming observability standards**  
- code needs to be cleaned up 
  - correct functionality depending on hidden side effects of methods (as example the actual setting of the response 
    status code in the ResponseHelper)
  - mixing injection methods
  - package names : **generics** in Java has a specific meaning and it is always a bad idea to have your organizational 
    structure come back in the package structure... it should only be functional (we don't want everyone to refactor if 
    another team takes over responsibility... do we?)
    `com.abnamro.genj.generics.exception` should have been something like `com.abnamro.rest.exceptionhandler` 
    `com.abnamro.genj.generics.responsehandler` should have been something like `com.abnamro.rest.resources`
  - better package/class/method names
  - incorrect/messy class/package structure introducing incorrect/dangerous dependencies 
  - sloppy code formatting/styling
  - magic strings/numbers
  - poor javadoc
  - using verbose/old/unclean coding techniques  
- readme says its for a SpringBoot application, which is incorrect, it is for each Spring application based on SpringMVC


# Links

https://www.baeldung.com/exception-handling-for-rest-with-spring
https://thepracticaldeveloper.com/2019/09/09/custom-error-handling-rest-controllers-spring-boot/
https://www.toptal.com/java/spring-boot-rest-api-error-handling
https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter7/exception_handling.html
