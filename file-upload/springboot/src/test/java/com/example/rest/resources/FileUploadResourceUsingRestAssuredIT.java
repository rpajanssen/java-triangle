package com.example.rest.resources;

import com.example.AvailableProfiles;
import com.example.domain.api.Form;
import com.example.domain.api.Message;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This unit integration test starts a full fledged application against which these tests are ran. The SpringBootTest
 * annotation tells Spring to start the app with the embedded application server. We have configured for the port to be
 * randomly selected. We can inject the selected port-number into this test by using the LocalServerPort annotation, this
 * way we will be able to configure RestAssured - which we will use to write the rest-api tests - so it can send the
 * http requests to the application.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(AvailableProfiles.LOCAL)
class FileUploadResourceUsingRestAssuredIT {
    private static final String BASE_URL = "http://localhost";
    private static final String BASE_API = "/api/upload";

    @LocalServerPort
    private int port;

    @BeforeAll
    static void initialSetup() {
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = port;

        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    void shouldUploadFile() throws IOException {
        byte[] inputFile = this.getClass().getClassLoader().getResourceAsStream("test-input.txt").readAllBytes();

        Form form = new Form("MyFile.txt", inputFile);

        Message message = given().when().contentType(ContentType.JSON).body(form).post(BASE_API).as(Message.class);

        // verify response of the resource under test
        assertTrue(message.getText().startsWith("Uploaded a file [MyFile.txt] with length 22 to: "));

        Path path = Path.of(message.getText().split(": ")[1]);
        String actual = Files.readString(path);
        assertEquals("a beautiful demo file\n", actual);

        Files.delete(path);
    }

}
