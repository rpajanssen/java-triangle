package com.example.rest.adapters;

import com.example.domain.api.Form;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Base64;

/**
 * Example JSONB adapter dat will be used during REST calls to (de)serialize the Form instances.
 *
 * The standard approach to (de)serialize a byte[] is to convert it to a base64 encoded string. That is the
 * main logic in this adapter.
 *
 * For this adapter to be used you need to add it to the Jsonb configuration. In this example application it can be
 * added in the JAXRS application class.
 */
public class FormJsonBAdapter implements JsonbAdapter<Form, JsonObject> {

    @Override
    public JsonObject adaptToJson(Form form) {
        return Json.createObjectBuilder()
                .add("name", form.getName())
                .add("attachment", Base64.getEncoder().encodeToString(form.getAttachment()))
                .build();
    }

    @Override
    public Form adaptFromJson(JsonObject json) {
        Form form = new Form(
                json.getString("name"),
                Base64.getDecoder().decode(json.getString("attachment"))
        );

        return form;
    }
}
