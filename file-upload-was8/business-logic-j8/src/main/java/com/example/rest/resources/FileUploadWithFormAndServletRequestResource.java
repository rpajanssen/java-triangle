package com.example.rest.resources;

import com.example.domain.api.Message;
import com.example.domain.model.FormBuilder;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.example.utils.FileUtils.*;

/**
 * This resource accepts a request for a multipart form upload. It processed the raw request itself by gathering the
 * parts and assemble them into a form (FormBuilder).
 *
 * Then we assume we have one form parameter "name" that holds the file-name and one form parameter "attachment" that
 * holds the file (but this of course all depends on your form).
 *
 * You may need to configure your deployment / application server to support multipart uploads. In this example
 * we will apply these configurations in the deployment modules because they can differ per deployment.
 */
@Named
@Path("/formupload")
public class FileUploadWithFormAndServletRequestResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response formPost(@Context HttpServletRequest request) throws IOException, ServletException {
        Form form = FormBuilder.instance()
                .withRequest(request)
                .build();

        String fileName = form.asMap().getFirst("name");
        String filePath = getTempFilePath(getTempDir(), fileName);
        byte[] rawFile = form.asMap().getFirst("attachment").getBytes();
        writeFile(filePath, rawFile);

        return Response.ok(buildMessage(fileName, rawFile.length, filePath)).build();
    }

    private Message buildMessage(String name, long length, String targetFilePath) {
        return new Message(String.format("Uploaded a file [%s] with length %d to: %s", name, length, targetFilePath));
    }
}
