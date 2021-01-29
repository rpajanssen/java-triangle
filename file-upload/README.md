# What?

This example project will show you how you can implement a file upload using JAX-RS that is framework and 
platform/application server agnostic. You can run it no matter if you are using WebSphere, OpenLiberty, Tomcat, Jersey, 
RestEasy, ... and more.

# Why?

It makes code more portable and makes it easy for you as a developer to switch. It will save the ABN AMRO money
in case of platform migrations because your code will still work.

## JAX-RS with RestEasy as a fat jar

This module creates a deployment using the JAX-RS [RestEasy](https://resteasy.github.io/) implementation. 
It also uses the maven shade-plugin to create a fat jar so you can deploy this application like you would with a 
SpringBoot application. 

## SpringBoot

This module packages the application as a fat jar using [SpringBoot](https://spring.io/projects/spring-boot) and
[RestEasy](https://resteasy.github.io/) as the JAX-RS implementation.

It uses the **resteasy-spring-boot-starter** to integrate SpringBoot with RestEasy.

## Additional note

In the deployments we need to configure the multipart setup up. One way to do this is to use the @MultipartConfig 
annotation that youcan apply to a servlet implementation class like:
```@MultipartConfig(location = "/tmp", maxFileSize = 35000000, maxRequestSize = 218018841, fileSizeThreshold = 0)```

# Requirements

* maven 3.6.1 (or higher)
* java 11

# Tl;DR

I just wanna run the all tests: `mvn clean integration-test`

# Implementations

## FileUploadResource

Implementation of the file-upload where the payload is a json holding the properties and the file(s) as a
byte array property. 

Uploading a file by the client is nothing more than a api call passing a json as body.

## FileUploadWithFormAndServletRequestResource

Implementation of the file-upload that accepts a form submit with a file as attachment.

Using this approach we have kept the resource implementation agnostic, bit you might need additional multipart 
configuration setup. See deployment modules for the details depending on the deployment.

# Example resource calls

Below are some sample curl calls you can run when you started your application. 

## upload 1 : FileUploadResource

curl -X POST -H 'Content-type: application/json' \
          -H 'Accept: application/json' \
          -d '{"attachment":"97329810197117116105102117108321001011091113210210510810110=","name":"MyFile.txt"}' \
          http://localhost:8080/api/upload

or          
          
POST http://localhost:8080/api/upload
Accept: application/json
Content-Type: application/json

{
  "attachment": "97329810197117116105102117108321001011101113210210510810110=",
  "name": "MyFile"
}          

## upload 2 : FileUploadWithFormAndServletRequestResource

curl -X POST -H 'Accept: application/json' \
          -F 'name=MyFile.txt' \
          -F 'attachment=@business-logic/src/test/resources/test-input.txt' \
          http://localhost:8080/api/formupload


or

POST http://localhost:8080/api/formupload
Accept: application/json
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="name"

MyFile.txt
--WebAppBoundary
Content-Disposition: form-data; name="attachment"; filename="test-input.txt"

< business-logic/src/test/resources/test-input.txt
--WebAppBoundary--
