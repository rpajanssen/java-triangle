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

# Links

https://www.baeldung.com/exception-handling-for-rest-with-spring
https://thepracticaldeveloper.com/2019/09/09/custom-error-handling-rest-controllers-spring-boot/
https://www.toptal.com/java/spring-boot-rest-api-error-handling
