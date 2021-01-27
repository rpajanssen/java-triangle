package com.example.domain.model;

import com.example.exceptions.UnableToProcessFilePartException;
import com.example.streams.ThrowingConsumer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.core.Form;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

import static com.example.utils.FileUtils.toByteArray;

/**
 * Build as JAX-RS Form given a servlet request.
 */
public class FormBuilder {
    private static final String DEFAULT_ENCODING = "UTF-8";

    private String encoding;
    private String tempLocation;
    private HttpServletRequest request;

    public FormBuilder() {
        this.tempLocation = System.getProperty("java.io.tmpdir");
    }

    public static FormBuilder instance() {
        return new FormBuilder();
    }

    public FormBuilder withLocation(String location) {
        this.tempLocation = location;

        return this;
    }

    public FormBuilder withRequest(HttpServletRequest request) {
        this.request = request;

        return this;
    }

    public Form build() throws IOException, ServletException {
        determineEncoding();

        Form form = new Form();

        Optional.ofNullable(request.getParts()).orElse(new ArrayList<>())
                .forEach(throwingWrapper(part -> extractParam(form, part)));

        return form;
    }

    private void extractParam(Form form, Part part) throws IOException {
        String fileName = part.getSubmittedFileName();
        if (isBinary(part)) {
            form.param(part.getName(), new String(toByteArray(readBinaryFilePart(part, fileName))));
        } else {
            form.param(part.getName(), readStringPart(part));
        }
    }

    private void determineEncoding() throws UnsupportedEncodingException {
        this.encoding = request.getCharacterEncoding();
        if (this.encoding == null) {
            request.setCharacterEncoding(this.encoding = DEFAULT_ENCODING);
        }
    }

    private boolean isBinary(Part part) {
        return part.getSubmittedFileName() != null;
    }

    private File readBinaryFilePart(Part part, String fileName) throws IOException {
        File tempFile = getTemporaryFile(fileName);

        try (BufferedInputStream input = new BufferedInputStream(part.getInputStream(), 8192);
             BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tempFile), 8192);) {

            byte[] buffer = new byte[8192];
            for (int length = 0; ((length = input.read(buffer)) > 0); ) {
                output.write(buffer, 0, length);
            }
        }

        return tempFile;
    }

    private String readStringPart(Part part) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), encoding));

        StringBuilder value = new StringBuilder();
        char[] buffer = new char[8192];
        for (int length; (length = reader.read(buffer)) > 0;) {
            value.append(buffer, 0, length);
        }

        return value.toString();
    }

    private File getTemporaryFile(String fileName) throws IOException {
        File tempFile = new File(tempLocation, fileName);
        tempFile.createNewFile();
        tempFile.deleteOnExit();
        return tempFile;
    }

    static <A extends Part> Consumer<A> throwingWrapper(
            ThrowingConsumer<A, Exception> throwingMapper) {

        return i -> {
            try {
                throwingMapper.apply(i);
            } catch (Exception ex) {
                throw new UnableToProcessFilePartException(ex);
            }
        };
    }
}
