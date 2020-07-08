package com.example.exceptionhandling.cloudcontracts;

import com.example.exceptionhandling.AvailableProfiles;
import com.example.exceptionhandling.FantasticSpringbootApplication;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

/**
 * CDC: (see the pom.xml for additional documentation) This is a base test class that will be extended by all generated
 * classes derived from the contracts in the "/resources/contracts/miscellaneous/wired" folder.
 */
@SuppressWarnings("unused")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FantasticSpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(AvailableProfiles.LOCAL)
public abstract class MiscellaneousBase {
    private static final String BASE_URL = "http://localhost";

    @LocalServerPort
    private int port;

    @Autowired
    protected WebApplicationContext context;

    @BeforeEach
    public void setup() {
        // Using the MOCK test mode Spring Cloud Contract will generate integration test that use a MockMvc and
        // RestAssured. Our base class needs to setup this environment.

        RestAssured.baseURI = BASE_URL;
        RestAssured.port = port;

        // we need to explicitly configure spring-security using mock-mvc, if we don't then although the spring-security
        // configuration is executed upon application startup, it will never be enforced during the tests
        RestAssuredMockMvc.webAppContextSetup(context, SecurityMockMvcConfigurers.springSecurity());
    }

    @AfterEach
    public void cleanup() {
        RestAssuredMockMvc.reset();
    }
}
