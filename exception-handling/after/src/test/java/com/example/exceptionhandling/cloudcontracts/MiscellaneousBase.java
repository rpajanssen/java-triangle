package com.example.exceptionhandling.cloudcontracts;

import com.example.exceptionhandling.AvailableProfiles;
import com.example.exceptionhandling.FantasticSpringbootApplication;
import com.example.exceptionhandling.rest.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.ws.rs.client.WebTarget;

/**
 * CDC: (see the pom.xml for additional documentation) This is a base test class that will be extended by all generated
 * classes derived from the contracts in the "/resources/contracts/miscellaneous" folder.
 */
@SuppressWarnings("unused")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FantasticSpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(AvailableProfiles.LOCAL)
public abstract class MiscellaneousBase {
    private static final String HOST = "localhost";

    @LocalServerPort
    private int port;

    private RestClient restClient;

    // Using the JAX-RS test mode Spring Cloud Contract will generate integration test that use a WebTarget. Our
    // base class need to provide an instance.
    protected WebTarget webTarget;

    @BeforeEach
    public void setup() {
        restClient = new RestClient(HOST, port);

        webTarget = restClient.target();
    }
}
