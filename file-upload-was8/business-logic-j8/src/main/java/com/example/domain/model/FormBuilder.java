package com.example.domain.model;

import com.example.exceptions.UnableToProcessFilePartException;
import com.example.streams.ThrowingConsumer;
import com.example.utils.HttpPartReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.core.Form;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Build as JAX-RS Form given a servlet request.
 */
public class FormBuilder {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private String encoding;
    private HttpServletRequest request;

    public static FormBuilder instance() {
        return new FormBuilder();
    }


    public FormBuilder withRequest(HttpServletRequest request) throws UnsupportedEncodingException {
        this.request = request;

        determineEncoding();

        return this;
    }

    public Form build() throws IOException, ServletException {
        Form form = new Form();

        Optional.ofNullable(request.getParts()).orElse(new ArrayList<>())
                .forEach(throwingWrapper(part -> extractParam(form, part)));

        return form;
    }

    private void extractParam(Form form, Part part) throws IOException {
        form.param(part.getName(), HttpPartReader.extractParamValue(part, encoding));
    }

    private void determineEncoding() throws UnsupportedEncodingException {
        this.encoding = request.getCharacterEncoding();
        if (this.encoding == null) {
            request.setCharacterEncoding(this.encoding = DEFAULT_ENCODING);
        }
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
