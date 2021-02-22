# JAX-RS with Websphere deployment 

We are using Liberty profile to locally deploy a WAS instance and run our
application with a mvn command.

# Available resources

## FileUploadResource

Requires some additional configuration for the JSON-B deserialization. In the application
class we need to configure the deserializer so it understands that the byte[] object on the 
Form class will be delivered as a base64 encoded string value property in the JSON
body. 

We also added some dependencies in the pom for this.

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

Note: all starts up fine but for one error message:

``` [WARNING ] CWWKC0044W: An exception occurred while scanning class and annotation data. The exception was java.lang.IllegalArgumentException
   [INFO]  at org.objectweb.asm.ClassReader.<init>(Unknown Source)
   [INFO]  at [internal classes]
```

This implies there is some class, most likely from an incompatible java version that won't 
initiate. I haven't been able to pin down which class/dependency this is. That the
error message is crap and doesn't help you one bit is a known issue with IBM. The two
resources work as expected non-the-less!

# Interesting reads

- https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_feat.html
- https://www.ibm.com/support/knowledgecenter/en/SSAW57_8.5.5/com.ibm.websphere.nd.multiplatform.doc/ae/twbs_jaxrs_configjaxrs11method.html
- https://openliberty.io/guides/maven-intro.html#creating-the-project-pom-file
- https://openliberty.io/guides/maven-intro.html
- https://adambien.blog/roller/abien/entry/jax_rs_json_b_configuration
