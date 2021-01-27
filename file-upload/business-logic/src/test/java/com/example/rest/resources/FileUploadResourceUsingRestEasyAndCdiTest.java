package com.example.rest.resources;


import com.example.domain.api.Form;
import com.example.domain.api.Message;
import com.example.rest.exceptionhandlers.*;
import com.example.resteasy.InMemoryCdiRestServer;
import com.example.resteasy.RestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test is an example of how you can use RestEasy to have a fully wired test of a resource. It demonstrates you
 * can wire all JAX-RS classes but also CDI classes like CDI method interceptors.
 *
 * This integration test will use the Application class. The Application class defines the complete setup of required
 * JAX-RS and CDI classes and by only listing the Application class the whole application will be setup.
 */
@SuppressWarnings("Duplicates")
class FileUploadResourceUsingRestEasyAndCdiTest {
    // use the rest-server that supports CDI
    private InMemoryCdiRestServer server;
    private RestClient restClient;

    /**
     * The before-each is preferred to the before-all because this way we can reset the state in between tests and we
     * can insure we will not experience test interference. This is especially useful when you global state, like we
     * have with the logger that us used by the tracer (a CDI method interceptor). It will most likely be a bit slower
     * because we will re-instantiate and rewire the resource under test for each unit test.
     */
    @BeforeEach
    void setup() throws IOException {
        /*
         * We will construct the resource we want to test ourselves and use the constructor also used for
         * constructor injection to inject the mocked DAO.
         */
        FileUploadResource underTest = new FileUploadResource();
        /*
         * We list the whole shebang of JAX-RS classes we want to wire for this test, resources, filters, interceptors,
         * exception-mappers... the whole shebang! We do not list CDI classes because they will not be supported by
         * the rest-server we will be using.
         */
        Object[] theWholeShebang = {
                underTest,

                ConstraintViolationHandler.class, ValidationExceptionHandler.class,
                UnsupportedEncodingExceptionHandler.class, UnableToProcessFilePartExceptionHandler.class,
                DefaultExceptionHandler.class
        };

        server = InMemoryCdiRestServer.instance(theWholeShebang);
        restClient = new RestClient(server.getHost(), server.getPort());
    }

    /**
     * See comment at the @BeforeEach.
     *
     * We need to reset the global state of the logger after each test. We will use a hardcoded-dao implementation but
     * it has no static data and will be instantiated again for each unit test, and thus be reset. If the hardcoded-dao
     * used static data like the FakeLogger then we also would have to reset it after each test.
     */
    @AfterEach
    void teardown() {
        server.close();
    }


    @Test
    void shouldUploadAFile() throws IOException {
        byte[] inputFile = this.getClass().getClassLoader().getResourceAsStream("test-input.txt").readAllBytes();

        Entity<Form> entity = Entity.json(new Form("MyFile.txt", inputFile));
        Response response = restClient.newRequest("/upload").request().buildPost(entity).invoke();

        // verify response of the resource under test
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        final Message result = response.readEntity(Message.class);
        assertTrue(result.getText().startsWith("Uploaded a file [MyFile.txt] with length 22 to: "));

        Path path = Path.of(result.getText().split(": ")[1]);
        String actual = Files.readString(path);
        assertEquals("a beautiful demo file\n", actual);

        Files.delete(path);
    }
}
