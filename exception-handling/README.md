# What?

This example project will demonstrate good and bad examples of exception handling in your REST 
resources.

The **before** examples has some code with common occurring mistakes. The **after** examples
addresses these mistakes. 

# Requirements

* maven 3.6.1 (or higher)
* java 11 (or higher)

# TL;DR

Just run all tests, navigate to the before/after folder and run: `mvn clean test`

Note: for the integration tests we use SpringCloudContract to generate the tests before they 
are run by maven as a regular unit test. The generated tests can be found in `<module>\target\generated-test-sources`.

For more on integration testing examples see: https://p-bitbucket.nl.eu.abnamro.com:7999/projects/COESD/repos/devcon2019

# Links

https://www.baeldung.com/exception-handling-for-rest-with-spring
https://thepracticaldeveloper.com/2019/09/09/custom-error-handling-rest-controllers-spring-boot/
https://www.toptal.com/java/spring-boot-rest-api-error-handling
