package com.example.rest.resources;

import com.example.domain.api.Form;
import com.example.domain.api.Message;

import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.example.utils.FileUtils.*;

/**
 * Implementation of the file-upload where the payload is a json holding the properties and the file(s) as a
 * byte array property.
 *
 * Uploading a file by the client is nothing more then a api call passing a json as body.
 *
 * This code works within all frameworks, platforms/application servers.
 */
@Named
@Path("/upload")
public class FileUploadResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response jsonPost(@Valid Form form) throws IOException {
        String filePath = getTempFilePath(getTempDir(), form.getName());
        writeFile(filePath, form.getAttachment());

        return Response.ok(buildMessage(form.getName(), form.getAttachment().length, filePath)).build();
    }

    private Message buildMessage(String name, long length, String targetFilePath) {
        return new Message(String.format("Uploaded a file [%s] with length %d to: %s", name, length, targetFilePath));
    }
}
