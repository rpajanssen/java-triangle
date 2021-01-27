# JAX-RS with RestEasy deployment (as a fat jar)

This module packages [RestEasy](https://resteasy.github.io/) as the JAX-RS implementation.

We package it the application in a fat jar using the maven shade plugin.

# Available resources

## FileUploadResource

Requires no additional configuration.

## FileUploadWithFormResource

Requires the configuration for multipart request.

We are using Jetty and in the Main class we set up the server. Here we have defined the multipart configuration for the 
servlet.

## Build en run

- build   : ```mvn clean install```
- package : ```mvn clean package```
- run     : ```java -jar target/fatjar-<version number>.jar```

Demo api calls can be found in the main readme.





