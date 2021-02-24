# JAX-RS with Websphere deployment 

We are using Liberty profile to locally deploy a WAS instance and run our application with a mvn command.

# Available resources

## FileUploadResource

Requires some additional configuration for the JSON-B deserialization. In the application
class we need to configure the deserializer so it understands that the byte[] object on the 
Form class will be delivered as a base64 encoded string value property in the JSON
body. 

We show you two ways of implementing this. The simple way is to configure the binary
data strategy to use BASE64 (this will result in (de)serialization of byte[] into base64 encoded
strings). This is the default solution and we configure this in the application class.

The more comples solution is to write your own JSONB adapter (Websphere uses JSONB for 
(de)serialization) for the Form class and configure this adapter to be used. This is
again configured in the application class. The actual addition of the adapter has been 
commented out. To actually use it outcomment the configuration of the strategy and 
incomment the addition of this adapter.

In the server.xml we nee to configure the jsonb feature to be deployed.

We also added some jsonb dependencies in the pom.

## FileUploadWithFormResource

Requires the configuration for multipart request. We configured the servlet to support multipart
requests in the ```web.xml``` :
```
    <servlet>
        <servlet-name>com.example.TheApplication</servlet-name>

        <multipart-config>
            <max-file-size>100000</max-file-size>
            <max-request-size>200000</max-request-size>
            <file-size-threshold>50000</file-size-threshold>
        </multipart-config>
    </servlet>
```

## Build en run

- build   : in root folder:  ```mvn clean package install```
- run     : in websphere folder: ```mvn liberty:run```

Demo api calls can be found in the main readme.


# Interesting reads

- https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_feat.html
- https://www.ibm.com/support/knowledgecenter/en/SSAW57_8.5.5/com.ibm.websphere.nd.multiplatform.doc/ae/twbs_jaxrs_configjaxrs11method.html
- https://openliberty.io/guides/maven-intro.html#creating-the-project-pom-file
- https://openliberty.io/guides/maven-intro.html
- https://developer.ibm.com/languages/java/articles/j-javaee8-json-binding-3/
- https://adambien.blog/roller/abien/entry/jax_rs_json_b_configuration
